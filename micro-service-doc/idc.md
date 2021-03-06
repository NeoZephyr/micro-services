## 多机房部署
假如有两个机房 A 和 B 都部署了应用服务，数据库的主库和从库部署在 A 机房，那么机房 B 的应用有两种方式访问到数据

1. 直接跨机房读取 A 机房的从库
2. 在机房 B 部署一个从库，跨机房同步主库的数据，然后机房 B 的应用就可以读取这个从库的数据

无论是哪一种方式，都涉及到跨机房的数据传输，这对机房之间延迟情况有比较高的要求。机房之间的延迟，和机房之间的距离息息相关

北京同地双机房之间的专线延迟一般在 1ms ~ 3ms（接口响应时间需要控制在 200ms 之内）
国内异地双机房之间的专线延迟会在 50ms 之内
从国内想要访问部署在美国西海岸的服务，这个延迟会在 100ms ~ 200ms 左右


## 同城双活
### 数据库
首先，数据库的主库可以部署在一个机房中，比如部署在 A 机房中，那么 A 和 B 机房数据都会被写入到 A 机房中

然后，在 A、B 两个机房中各部署一个从库，通过主从复制的方式，从主库中同步数据，这样双机房的查询请求可以查询本机房的从库

一旦 A 机房发生故障，可以通过主从切换的方式，将 B 机房的从库提升为主库，达到容灾的目的

### 缓存
缓存也可以部署在两个机房中，查询请求也读取本机房的缓存，如果缓存中数据不存在，就穿透到本机房的从库中，加载数据。数据的更新可以更新双机房中的数据，保证数据的一致性

不同机房的服务会向注册中心，注册不同的服务组，而不同机房的客户端，只订阅同机房的服务组，这样就可以实现调用尽量发生在本机房内，避免跨机房的调用


## 异地多活
在数据写入时，保证只写本机房的数据存储服务，再采取数据同步的方案，将数据同步到异地机房中。一般来说，数据同步的方案有两种:
1. 基于存储系统的主从复制，比如 MySQL 和 Redis。也就是在一个机房部署主库，在异地机房部署从库，两者同步主从复制, 实现数据的同步
2. 基于消息队列的方式。一个机房产生写入请求后，会写一条消息到消息队列，另一个机房的应用消费这条消息后，再执行业务处理逻辑，写入到存储服务中

可以基于消息的方式，同步缓存的数据、HBase 数据等。然后基于存储，主从复制同步 MySQL、Redis 等数据

无论是采取哪种方案，数据从一个机房，传输到另一个机房都会有延迟，所以，需要尽量保证用户在读取自己的数据时，读取数据主库所在的机房。为了达到这一点，需要对用户做分片，让一个用户每次的读写都尽量在同一个机房中。同时，在数据读取和服务调用时，也要尽量调用本机房的服务
