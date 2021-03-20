# 公共关注点

## 配置管理

Dubbo: Nacos
Spring Cloud: Spring Cloud Config
K8s: ConfigMaps/Secrets

## 服务发现和负载均衡

Dubbo: Zk/Nacos + Client
Spring Cloud: Eureka + Ribbon
K8s: Service

## 弹性和容错

Dubbo: Sentinel
Spring Cloud: Hystrix
K8s: HealthCheck/Probe/ServiceMesh

## api 网关

Dubbo: NA
Spring Cloud: Zuul
K8s: Ingress

## 服务安全

## 日志监控

Dubbo: ELK
Spring Cloud: ELK
K8s: EFK

## metrics 监控

Dubbo: Dubbo Admin/Monitor
Spring Cloud: Actuator/MicroMeter + Prometheus
K8s: Heapster + Prometheus

## 调用链监控

## 调度和发布

## 自愈和自动伸缩


微服务
规模小，容易测试、维护
单一职责
尽可能早地创建原型
可移植性比效率更重要

原子服务
独立进程
隔离部署
去中心化服务治理

缺点：
基础设施建设、复杂度高
分布式系统会带来固有的复杂性。开发者不得不使用 RPC 或者消息传递，来实现进程间通信；此外，必须要写代码来处理消息传递中速度过慢或者服务不可用等局部失效问题
在微服务架构应用中，需要更新不同服务所使用的不同的数据库，从而对开发者提出了更高的要求和挑战
测试一个基于微服务架构的应用也是很复杂的任务
服务模块间的依赖，应用的升级有可能会波及多个服务模块的修改
对运维基础设施的挑战比较大

基础设施自动化
CICD：Gitlab + Gitlab Hooks + k8s
Testing：测试环境、单元测试、API自动化测试
在线运行时：k8s，以及一系列 Prometheus、ELK、Conrtol Panle


可用性 & 兼容性设计
隔离
超时控制
负载保护
限流
降级
重试
负载均衡

发送的数据要更保守，意味着最小化的传送必要的信息，接收时更开放意味着要最大限度的容忍冗余数据，保证兼容性



微服务优点
服务组件化、去中心化
分散服务治理、分散数据管理
强调业务单一性、弱化业务耦合度
容错设计、资源合理分配

微服务缺点
协调沟通
扩展难道、维护难度

DNS做负载
通过给客户端解析不同的IP地址，让客户端的流量直接到达各个服务器
延时性问题：在做出调度策略改变以后，由于DNS各级节点的缓存并不会及时的在客户端生效，而且DNS负载的调度策略比较简单，无法满足业务需求

在DNS服务器中配置多个A记录，如：
www.mysite.comINA114.100.80.1
www.mysite.comINA114.100.80.2
www.mysite.comINA114.100.80.3.
每次域名解析请求都会根据负载均衡算法计算一个不同的IP地址返回，这样A记录中配置的多个服务器就构成一个集群，并可以实现负载均衡。
DNS域名解析负载均衡的优点是将负载均衡工作交给DNS，省略掉了网络管理的麻烦，缺点就是DNS可能缓存A记录，不受网站控制。事实上，大型网站总是部分使用DNS域名解析，作为第一级负载均衡手段，然后再在内部做第二级负载均衡


四层负载均衡
工作在OSI模型的传输层，主要工作是转发，它在接收到客户端的流量以后通过修改数据包的地址信息将流量转发到应用服务器

TCP的连接建立，即三次握手是客户端和服务器直接建立的，负载均衡设备只是起到一个类似路由器的转发动作

七层负载均衡
工作在OSI模型的应用层，因为它需要解析应用层流量，所以七层负载均衡在接到客户端的流量以后，还需要一个完整的TCP/IP协议栈。七层负载均衡会与客户端建立一条完整的连接并将应用层的请求流量解析出来，再按照调度算法选择一个应用服务器，并与应用服务器建立另外一条连接将请求发送过去，因此七层负载均衡的主要工作就是代理

负载均衡和前端的客户端建立连接(三次握手)后，才可能接受到客户端发送的真正应用层内容的报文，然后再根据该报文中的特定字段，再加上负载均衡设备设置的服务器选择方式，决定最终选择的内部服务器

智能化：访问一个网站的用户流量，可以通过七层的方式，将对图片类的请求转发到特定的图片服务器并可以使用缓存技术；将对文字类的请求可以转发到特定的文字服务器并可以使用压缩技术

SYNFlood攻击
四层模式下这些SYN攻击都会被转发到后端的服务器上；而七层模式下这些SYN攻击自然在负载均衡设备上就截止，不会影响后台服务器的正常运营

在七层层面设定多种策略，过滤特定报文，例如SQLInjection等应用层面的特定攻击手段

负载均衡的算法
随机算法
轮询：当服务器群中各服务器的处理能力相同时，且每笔业务处理量差异不大时，最适合使用这种算法
加权轮询
最少连接
加权最少连接
普通哈希
IP地址散列
URL散列


dns来实施负载均衡
优点：
利用第三方dns实施，服务端架构不用动
少了一层网络请求

不足:
dns只具备解析功能，不能保证对应外网ip的可用性
当web-server需要扩容时，通过dns扩容生效时间长





反向代理的优势：
隐藏真实后端服务
负载均衡集群
高可用集群
缓存静态内容实现动静分离
安全限流
静态文件压缩
解决多个服务跨域问题
合并静态请求(HTTP/2.0后已经被弱化)
防火墙
SSL以及http2



四层负载均衡，也就是主要通过报文中的目标地址和端口，再加上负载均衡设备设置的服务器选择方式，决定最终选择的内部服务器

七层负载均衡，也称为“内容交换”，也就是主要通过报文中的真正有意义的应用层内容，
再加上负载均衡设备设置的服务器选择方式，决定最终选择的内部服务器

七层负载均衡服务器先代理最终的服务器和客户端建立连接（TCP 三次握手）后，才可能接收到客户端发送的真正应用层内容的报文， 然后再根据该报文中的特定字段，再加上负载均衡设备设置的服务器选择方式，决定最终选择的内部服务器
在这种情况下，负载均衡设备和前端的客户端以及后端的服务器会分别建立 TCP 连接


七层应用负载均衡使得整个网络更智能化，负载均衡器可以将图片类的请求转发到特定的图片服务器并可以使用缓存技术，将文字类的请求转发到特定的文字服务器并可以使用压缩技术，实现客户请求的 Header 重写 或对关键字进行过滤，还可以避免常见的 SYN Flood 攻击直接攻击到后台服务器

七层应用负载均衡可以提高流量智能化，同时必不可免的带来设备配置复杂，负载均衡压力增高以及故障排查上的复杂性等问题


负载均衡算法
静态负载均衡算法
轮询，比率，优先权

动态负载均衡算法
最少的连接数
最快响应速度

ID生成的核心需求有两点：
全局唯一：唯一主键是第一范式
趋势有序：InnoDB表的数据写入顺序能和B+树索引的叶子节点顺序一致的话，这时候存取效率最高

分布式唯一ID需要满足以下条件：
高可用性：不能有单点故障
全局唯一性：不能出现重复的ID号
趋势递增：在MySQL InnoDB引擎中使用的是聚集索引，由于多数RDBMS使用B-tree的数据结构来存储索引数据，在主键的选择上面我们应该尽量使用有序的主键保证写入性能
时间有序：以时间为序，或者ID里包含时间。这样一是可以少一个索引，二是冷热数据容易分离
分片支持：可以控制ShardingId。比如某一个用户的文章要放在同一个分片内，这样查询效率高，修改也容易
单调递增：保证下一个ID一定大于上一个ID，例如事务版本号、IM增量消息、排序等特殊需求
长度适中：不要太长，最好64bit。使用long比较好操作，如果是96bit，那就要各种移位相当的不方便，还有可能有些组件不能支持这么大的ID
信息安全：如果ID是连续的，恶意用户的扒取工作就非常容易做了，直接按照顺序下载指定URL即可；如果是订单号就更危险了，竞争对手可以直接知道我们一天的单量。所以在一些应用场景下，会需要ID无规则、不规则


### 数据库自增长序列或字段
优点：
1）简单，代码方便，性能可以接受
2）数字 ID 天然排序，对分页或者需要排序的结果很有帮助

缺点：
1）不同数据库语法和实现不同，数据库迁移的时候或多数据库版本支持的时候需要处理
2）在性能达不到要求的情况下，比较难于扩展
3）分表分库的时候会有麻烦

优化方案：
1）针对主库单点，如果有多个Master库，则每个Master库设置的起始数字不一样，步长一样，可以是Master的个数。这样就可以有效生成集群中的唯一ID，也可以大大降低ID生成数据库操作的负载

### UUID
可以利用数据库也可以利用程序生成
优点：
1）简单，代码方便
2）生成ID性能非常好，基本不会有性能问题
3）全球唯一，在遇见数据迁移，系统数据合并，或者数据库变更等情况下，可以从容应对

缺点：
1）没有排序，无法保证趋势递增，入库性能变差（B+树索引的分裂）
2）UUID往往是使用字符串存储，查询的效率比较低
3）存储空间比较大，如果是海量数据库，就需要考虑存储量的问题
4）传输数据量大
5）可读性差

### Redis生成ID
依赖Redis的单线程生成全局唯一的ID

优点：
1）不依赖于数据库，灵活方便，且性能优于数据库
2）数字ID天然排序，对分页或者需要排序的结果很有帮助

缺点：
1）生成ID性能不够
2）引入新的组件，增加系统复杂度

优化方案：
使用Redis集群来获取更高的吞吐量

### Snowflake
1. 41位的时间序列（精确到毫秒，41位的长度可以使用69年）
2. 10位的机器标识（10位的长度最多支持部署1024个节点）
3. 12位的计数顺序号（12位的计数顺序号支持每个节点每毫秒产生4096个ID序号）
4. 最高位是符号位，始终为0

1 + 41 + 10 + 12
优点：
高性能，低延迟；独立的应用
按时间有序

缺点：
需要独立的开发和部署
强依赖时钟,如果主机时间回拨,则会造成重复ID
ID虽然有序,但是不连续

snowflake方案依赖系统时钟，如果机器时钟回拨，就有可能生成重复ID
1. 关闭系统NTP同步，这样就不会产生时钟调整
2. 与上次更新时间作比较，在时钟回拨这段时间，不生成ID，直接返回ERROR_CODE，直到时钟追上，恢复服务
3. 如果遇到超过容忍限度的回拨，上报报警系统，并把自身从集群节点中摘除
4.

相当于 setnx + expire
set lock:order 1 ex 5 nx

redis 加锁超时问题：
1. redis 分布式锁不要用于较长时间的任务
2. set 的时候设置随机数，在解锁 del 的时候

lua 脚本判断并删除
```
if redis.call("get",KEYS[1]) == ARGV[1] then
    return redis.call("del",KEYS[1])
else
    return 0
end
```
可重入锁
```
public class RedisWithReentrantLock {

  private ThreadLocal<Map<String, Integer>> lockers = new ThreadLocal<>();

  private Jedis jedis;

  public RedisWithReentrantLock(Jedis jedis) {
    this.jedis = jedis;
  }

  private boolean _lock(String key) {
    return jedis.set(key, "", "nx", "ex", 5L) != null;
  }

  private void _unlock(String key) {
    jedis.del(key);
  }

  private Map<String, Integer> currentLockers() {
    Map<String, Integer> refs = lockers.get();
    if (refs != null) {
      return refs;
    }
    lockers.set(new HashMap<>());
    return lockers.get();
  }

  public boolean lock(String key) {
    Map<String, Integer> refs = currentLockers();
    Integer refCnt = refs.get(key);
    if (refCnt != null) {
      refs.put(key, refCnt + 1);
      return true;
    }
    boolean ok = this._lock(key);
    if (!ok) {
      return false;
    }
    refs.put(key, 1);
    return true;
  }

  public boolean unlock(String key) {
    Map<String, Integer> refs = currentLockers();
    Integer refCnt = refs.get(key);
    if (refCnt == null) {
      return false;
    }
    refCnt -= 1;
    if (refCnt > 0) {
      refs.put(key, refCnt);
    } else {
      refs.remove(key);
      this._unlock(key);
    }
    return true;
  }

  public static void main(String[] args) {
    Jedis jedis = new Jedis();
    RedisWithReentrantLock redis = new RedisWithReentrantLock(jedis);
    System.out.println(redis.lock("codehole"));
    System.out.println(redis.lock("codehole"));
    System.out.println(redis.unlock("codehole"));
    System.out.println(redis.unlock("codehole"));
  }

}
```

分布式锁
Redis分布式锁：利用Redis的setnx命令。此命令同样是原子性操作，只有在key不存在的情况下，才能set成功
加锁：setnx（key，1）：当一个线程执行setnx返回1，说明key原本不存在，该线程成功得到了锁；当一个线程执行setnx返回0，说明key已经存在，该线程抢锁失败
解锁：del（key）
锁超时：expire（key， 30）

1. setnx和expire的非原子性：线程在 setnx 之后挂掉导致死锁
```
if（setnx（key，1） == 1）{
    expire（key，30）
    try {
        do something ......
    } finally {
        del（key）
    }
}
```

使用 set（key，1，30，NX）取代 set(key, 1)

2. del 导致误删
线程成功得到了锁之后执行的很慢，锁过期自动释放，其他线程得到了锁。随后之前的线程执行完删除锁，而此时还有线程没有执行完成

在del释放锁之前做一个判断，验证当前的锁是不是自己加的锁：可以在加锁的时候把当前的线程ID当做value，并在删除之前验证key对应的value是不是自己线程的ID
加锁：
String threadId = Thread.currentThread().getId()
set（key，threadId ，30，NX）
解锁：
if（threadId .equals(redisClient.get(key))）{
    del(key)
}

判断和释放锁是两个独立操作，不是原子性：使用 lua 脚本
String luaScript =
"
if redis.call('get', KEYS[1]) == ARGV[1] then
  return redis.call('del', KEYS[1])
else
  return 0
end";

redisClient.eval(luaScript , Collections.singletonList(key), Collections.singletonList(threadId));

解决并发问题
让获得锁的线程开启一个守护线程，用来给快要过期的锁“续航”
当过去了29秒，线程A还没执行完，这时候守护线程会执行expire指令，为这把锁“续命”20秒。守护线程从第29秒开始执行，每20秒执行一次。


Zookeeper分布式锁：利用Zookeeper的顺序临时节点，来实现分布式锁和等待队列。Zookeeper设计的初衷，就是为了实现分布式锁服务的



Zookeeper
Zookeeper的数据存储基于节点，这种节点叫做Znode
Znode 包含数据、子节点引用、访问权限等
data：Znode存储的数据信息
ACL：记录Znode的访问权限，即哪些人或哪些IP可以访问本节点
stat：包含Znode的各种元数据，比如事务ID、版本号、时间戳、大小等
child：当前节点的子节点引用，类似于二叉树的左孩子右孩子

Zookeeper是为读多写少的场景所设计。Znode并不是用来存储大规模业务数据，而是用于存储少量的状态和配置信息，每个节点的数据最大不能超过1MB

Zookeeper的基本操作和事件通知
create 创建节点
delete 删除节点
exists 判断节点是否存在
getData 获得一个节点的数据
setData 设置一个节点的数据
getChildren 获取节点下的所有子节点

Zookeeper客户端在请求读操作的时候，可以选择是否设置Watch：当这个Znode发生改变，也就是调用了create，delete，setData方法的时候，将会触发Znode上注册的对应事件，请求Watch的客户端会接收到异步通知

Zookeeper Service集群是一主多从结构
在更新数据时，首先更新到主节点，再同步到从节点
在读取数据时，直接读取任意从节点

为了保证主从节点的数据一致性，Zookeeper采用了ZAB协议，这种协议非常类似于一致性算法Paxos和Raft

ZAB协议所定义的三种节点状态
Looking：选举状态
Following：Follower节点（从节点）所处的状态
Leading ：Leader节点（主节点）所处状态

最大ZXID：节点本地的最新事务编号，包含epoch和计数两部分

ZAB的崩溃恢复阶段
Leader election：选举阶段
此时集群中的节点处于Looking状态。它们会各自向其他节点发起投票，投票当中包含自己的服务器ID和最新事务ID（ZXID）

Discovery：发现阶段
用于在从节点中发现最新的ZXID和事务日志
接收所有Follower发来各自的最新epoch值。Leader从中选出最大的epoch，基于此值加1，生成新的epoch分发给各个Follower。

各个Follower收到全新的epoch后，返回ACK给Leader，带上各自最大的ZXID和历史事务日志。Leader选出最大的ZXID，并更新自身历史日志

Synchronization：同步阶段
把Leader刚才收集得到的最新历史事务日志，同步给集群中所有的Follower。只有当半数Follower同步成功，这个准Leader才能成为正式的Leader

至此，故障恢复成功

Broadcast 写入数据
1.客户端发出写入数据请求给任意Follower
2.Follower把写入数据请求转发给Leader
3.Leader采用二阶段提交方式，先发送Propose广播给Follower
4.Follower接到Propose消息，写入日志成功后，返回ACK消息给Leader
5.Leader接到半数以上ACK消息，返回成功给客户端，并且广播Commit请求给Follower

Znode 类型
持久节点 （PERSISTENT）：默认的节点类型。创建节点的客户端与zookeeper断开连接后，该节点依旧存在
持久节点顺序节点（PERSISTENT_SEQUENTIAL）：所谓顺序节点，就是在创建节点时，Zookeeper根据创建的时间顺序给该节点名称进行编号
临时节点（EPHEMERAL）：和持久节点相反，当创建节点的客户端与zookeeper断开连接后，临时节点会被删除
临时顺序节点（EPHEMERAL_SEQUENTIAL） ：在创建节点时，Zookeeper根据创建的时间顺序给该节点名称进行编号；当创建节点的客户端与zookeeper断开连接后，临时节点会被删除

Zookeeper分布式锁的原理
获取锁
首先，在Zookeeper当中创建一个持久节点ParentLock。当第一个客户端想要获得锁时，需要在ParentLock这个节点下面创建一个临时顺序节点 Lock1
之后，Client1查找ParentLock下面所有的临时顺序节点并排序，判断自己所创建的节点Lock1是不是顺序最靠前的一个。如果是第一个节点，则成功获得锁
如果再有一个客户端 Client2 前来获取锁，则在ParentLock下载再创建一个临时顺序节点Lock2
Client2查找ParentLock下面所有的临时顺序节点并排序，判断自己所创建的节点Lock2是不是顺序最靠前的一个，结果发现节点Lock2并不是最小的。于是，Client2向排序仅比它靠前的节点Lock1注册Watcher，用于监听Lock1节点是否存在。这意味着Client2抢锁失败，进入了等待状态。

释放锁
任务完成，客户端显示释放，Client1会显示调用删除节点Lock1的指令
任务执行过程中，客户端崩溃，相关联的节点Lock1会随之自动删除

由于Client2一直监听着Lock1的存在状态，当Lock1节点被删除，Client2会立刻收到通知。这时候Client2会再次查询ParentLock下面的所有节点，确认自己创建的节点Lock2是不是目前最小的节点。如果是最小，则Client2顺理成章获得了锁

zookeeper 锁
优点
1. 框架封装，容易实现
2. 有等待锁的队列，提升抢锁效率
缺点
添加删除节点性能较低

redis 锁
优点
set、del 指令性能较高
缺点
1. 实现复杂，需要考虑超时、原子性、误删除等
2. 没有锁等待队列，只能通过客户端自旋来等锁，效率低下

zookeeper 锁与 redis 锁均支持可重入逻辑


### 分布式事务
分布式事务用于在分布式系统中保证不同节点之间的数据一致性

XA协议包含两阶段提交（2PC）和三阶段提交（3PC）两种实现

两阶段提交协议：将分布式事务分为两个阶段，一个是准备阶段，一个是提交阶段，两个阶段都由事务管理器发起。基于两阶段提交协议，事务管理器能够最大限度地保证跨数据库操作的事务的原子性，是分布式系统环境下最严格的事务实现方法

性能问题
在提交事务的过程中，事务管理器需要和每个参与者进行准备和提交的操作的协调，在准备阶段锁定资源，在提交阶段消费资源，但是由于参与者较多，锁定资源和消费资源之间的时间差被拉长，导致响应速度较慢，在此期间产生死锁或者不确定结果的可能性较大。因此，在互联网行业里，为了追求性能的提升，很少使用两阶段提交协议

1. 在事务执行过程中，各个节点占用着数据库资源，只有当所有节点准备完毕，事务协调者才会通知提交，参与者提交后释放资源。这样的过程有着非常明显的性能问题
2. 一旦事务协调者节点挂掉，参与者收不到提交或是回滚通知，参与者会一直处于中间状态无法完成事务
3. 如果发生局部网络问题，一部分事务参与者收到了提交消息，另一部分事务参与者没收到提交消息，那么就导致了节点之间数据的不一致


最大努力保证模式：适用于对一致性要求并不十分严格但是对性能要求较高的场景
具体的实现方法是，在更新多个资源时，将多个资源的提交尽量延后到最后一刻处理，这样的话，如果业务流程出现问题，则所有的资源更新都可以回滚，事务仍然保持一致。若在提交多个资源时发生了系统问题，就需要进行实时补偿，将已提交的事务进行回滚

开始消息事务
开始数据库事务
接收消息
更新数据库
提交数据库事务
提交消息事务

如果第5步失败，则可以将消息队列和数据库事务全部回滚，保持一致。如果第5步成功，第6步遇到了网络超时等问题，则这是唯一可能产生问题的情况，在这种情况下，消息的消费过程并没有被提交到消息队列，消息队列可能会重新发送消息给其他消息处理服务，这会导致消息被重复消费，但是可以通过幂等处理来保证消除重复消息带来的影响。

事务补偿机制：性能很高，并且能够尽最大可能地保证事务的最终一致性


事务路由：为不同的数据源配置不同的事务管理器
```
<?xml version="1.0?>
<beans>
 <bean id="sharding-db-trx0"class="org.springframework.jdbc.datasource.Data SourceTransactionManager">
   <property name="dataSource">
     <ref bean="sharding-db0" />
   </property>
 </bean>
 <bean id="sharding-db-trx1"
   class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
   <property name="dataSource">
     <ref bean="sharding-db1" />
   </property>
 </bean>
 <bean id="sharding-db-trx2"
   class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
   <property name="dataSource">
         <ref bean="sharding-db2" />
   </property>
 </bean>
 <bean id="sharding-db-trx3"
   class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
   <property name="dataSource">
         <ref bean="sharding-db3" />
   </property>
 </bean>

 <bean id="shardingTransactionManager" class="com.robert.dbsplit.core. ShardingTransactionManager">
   <property name="proxyTransactionManagers">
     <map value-type="org.springframework.transaction.PlatformTran sactionManager">
       <entry key="sharding0" value-ref="sharding-db-trx0" />
       <entry key="sharding1" value-ref="sharding-db-trx1" />
       <entry key="sharding2" value-ref="sharding-db-trx2" />
       <entry key="sharding3" value-ref="sharding-db-trx3" />
     </map>
   </property>
 </bean>

 <aop:config>
       <aop:advisor advice-ref="txAdvice" pointcut="execution(* com.robert.biz.*insert(..))"/>
       <aop:advisor advice-ref="txAdvice" pointcut="execution(* com.robert.biz.*update(..))"/>
       <aop:advisor advice-ref="txAdvice" pointcut="execution(* com.robert.biz.*delete(..))"/>
 </aop:config>
 <tx:advice id="txAdvice" transaction-manager="shardingTransactionManager">
   <tx:attributes>
     <tx:method name="*" rollback-for="java.lang.Exception"/>
   </tx:attributes>
 </tx:advice>

</beans>
```
```
public class ShardingContextHolder<T> {
   private static final ThreadLocal shardHolder = new ThreadLocal();

   public static <T> void setShard(T shard) {
       Validate.notNull(shard, "请指定某个分片数据库！");
       shardHolder.set(shard);
   }

   public static <T> T getShard() {
       return (T) shardHolder.get();
   }
}
```
```
public class ShardingTransactionManager implements PlatformTransactionManager {
   private Map<Object, PlatformTransactionManager> proxyTransactionManagers =
           new HashMap<Object, PlatformTransactionManager>();

  protected PlatformTransactionManager getTargetTransactionManager() {
       Object shard = ShardingContextHolder.getShard();
       Validate.notNull(shard, "必须指定一个路由的shard！");
       return targetTransactionManagers.get(shard);
   }
   public void setProxyTransactionManagers(Map<Object, PlatformTransaction Manager> targetTransactionManagers) {
       this.targetTransactionManagers = targetTransactionManagers;
   }

   public void commit(TransactionStatus status) throws TransactionException {
       getProxyTransactionManager().commit(status);
   }

   public TransactionStatus getTransaction(TransactionDefinition definition) throws TransactionException {
       return getProxyTransactionManager().getTransaction(definition);
   }

   public void rollback(TransactionStatus status) throws TransactionException
   {
       getProxyTransactionManager().rollback(status);
   }
}
```

```
RoutingContextHolder.setShard("sharding0");
return userService.create(user);
```

XA三阶段提交
XA三阶段提交在两阶段提交的基础上增加了CanCommit阶段，并且引入了超时机制。一旦事物参与者迟迟没有接到协调者的commit请求，会自动进行本地commit。这样有效解决了协调者单点故障的问题。但是性能问题和不一致的问题仍然没有根本解决

MQ事务
利用消息中间件来异步完成事务的后一半更新，实现系统的最终一致性。这个方式避免了像XA协议那样的性能问题

TCC事务
TCC事务是Try、Commit、Cancel三种指令的缩写，其逻辑模式类似于XA两阶段提交，但是实现方式是在代码层面来人为实现


Raft 算法：依靠 状态机 和 主从同步 的方式，在各个节点之间实现数据的一致性
1.选取主节点
Raft算法为节点定义了三种角色：
1.Leader（主节点）
2.Follower（从节点）
3.Candidate（参与投票竞争的节点）
第一步，在最初，还没有一个主节点的时候，所有节点的身份都是Follower。每一个节点都有自己的计时器，当计时达到了超时时间（Election Timeout），该节点会转变为Candidate
第二步，成为Candidate的节点，会首先给自己投票，然后向集群中其他所有的节点发起请求，要求大家都给自己投票
第三步，其他收到投票请求且还未投票的Follower节点会向发起者投票，发起者收到反馈通知后，票数增加
第四步，当得票数超过了集群节点数量的一半，该节点晋升为Leader节点。Leader节点会立刻向其他节点发出通知，告诉大家自己才是老大。收到通知的节点全部变为Follower，并且各自的计时器清零

Leader节点需要每隔一段时间向集群其他节点发送心跳通知，表明你们的老大还活着
一旦Leader节点挂掉，发不出通知，那么计时达到了超时时间的Follower节点会转变为Candidate节点，发起选主投票，周而复始


2.同步数据
第一步，由客户端提交数据到Leader节点。
第二步，由Leader节点把数据复制到集群内所有的Follower节点。如果一次复制失败，会不断进行重试。
第三步，Follower节点们接收到复制的数据，会反馈给Leader节点。
第四步，如果Leader节点接收到超过半数的Follower反馈，表明复制成功。于是提交自己的数据，并通知客户端数据提交成功。
第五步，由Leader节点通知集群内所有的Follower节点提交数据，从而完成数据同步流程


两阶段提交协议(2PC)
投票阶段：参与者将操作结果通知协调者
提交阶段：收到参与者的通知后，协调者再向参与者发出通知，根据反馈情况决定各参与者是否要提交还是回滚


CAP理论
一个分布式系统不可能同时满足一致性（C：Consistency）、可用性（A：Availability）和分区容错性（P：Partition tolerance）这三个基本需求，最多只能同时满足其中两项

BASE理论
Basically Available（基本可用）、Soft state（软状态）和Eventually consistent（最终一致性）


```
tar -xzvf zookeeper-3.4.11.tar.gz
```
```
vi /etc/profile

export ZOOKEEPER_HOME=/usr/local/zookeeper
export PATH=$PATH:$ZOOKEEPER_HOME/bin
```
zoo.cfg
```
tickTime：用于计算的时间单元
initLimit：用于集群，允许从节点连接并同步到主节点的初始化连接时间
syncLimit：用于集群，主节点与从节点发送消息，请求与应答时间长度
dataDir：
dataLogDir：日志目录
clientPort：连接服务器端口
```

```
./zkServer.sh start
./zkServer.sh status
./zkServer.sh stop
```

Zookeeper 基本数据模型
1. 树形结构
每一个节点都称之为 znode，可以有子节点，也可以有数据
每个节点分为临时节点和永久节点，临时节点在客户端断开后消失
每个节点都有各自的版本号
每当节点数据发生变化，该节点的版本号会累加
删除/修改过时节点，版本号不匹配则会报错
每个节点存储的数据不宜过大
节点可以设置权限，通过权限可以限制用户的访问

客户端连接
```
./zkCli.sh
```
客户端连接之后
```
help
ls /
ls /zookeeper

ls2 /

节点状态
stat /

取出节点数据
get /

创建节点
create /pain pain
get /pain

创建临时节点
create -e /pain/tmp tmp
get /pain/tmp

创建顺序节点
create -s /pain/seq seq
get /pain/seq

通过版本号实现乐观锁
set /pain pain <version>

delete /pain <version>
```

针对每个节点的操作，都会有一个监督者；当监控的某个对象发生了变化，则触发 watcher 事件；zookeeper 中的 watcher 是一次性的，触发后立即销毁

触发 watcher 事件类型
1. 节点创建事件
2. 节点删除事件
3. 节点数据变化事件

触发节点创建事件：NodeCreated
```
stat /pain watch
create /pain pain
```

节点数据变化事件：NodeDataChanged
```
get /pain watch
set /pain pain
```

节点删除事件：NodeDeleted
```
get /pain watch
delete /pain
```

NodeChildrenChanged
```
ls /pain watch
create /pain/tmp tmp
```

NodeChildrenChanged
```
ls /pain watch
delete /pain/tmp
```

不会触发事件
```
ls /pain watch
set /pain/tmp tmp
```

节点数据变化事件：NodeDataChanged
```
get /pain/tmp watch
set /pain/tmp tmp
```

acl 权限控制
1. 针对节点可以设置相关读写等权限，目的为了保障数据安全性
2. 权限 permissions 可以指定不同的权限范围以及角色

getAcl：获取某个节点的 acl 权限信息
setAcl：设置某个节点的 acl 权限信息
addauth：输入认证授权信息，注册时输入明文密码

zookeeper 的 acl 通过 schema:id:permissions 来构成权限列表
schema：代表采用的某种权限机制
id：代表允许访问的用户
permissions：权限组合字符串

schema 种类：
1. world：下面只有一个 id，即只有一个用户，即 anyone。格式为：world:anyone:[permissions]
2. auth：代表认证登录，需要注册用户有权限就可以。格式为：auth:user:password:[permissions]
3. digest：需要对密码加密才能访问。格式为：digest:username:BASE64(SHA1(password)):[permissions]
4. ip：限制 ip 进行访问。格式为：ip:192.168.1.1:[permissions]
5. super：代表超级管理员，具有所有权限

permissions 构成：
crdwa
c：CREATE，创建子节点
r：RAED，获取节点/子节点
w：WRITE，设置节点数据
d：DELETE，删除子节点
a：ADMIN，设置权限

```
create /pain/tmp tmp
getAcl /pain/tmp
setAcl /pain/tmp world:anyone:crwa
```

```
登录
addauth digest pain:pain
setAcl /pain/tmp auth:pain:pain:cdrwa
getAcl /pain/tmp

get /pain/tmp
```

```
setAcl /pain/tmp digest:pain:<加密的密码>:cdrwa
```

```
setAcl /pain/tmp ip:192.168.1.1:cdrwa
```

super
1. 修改 zkServer.sh 增加 super 管理员
2. 重启 zkServer.sh

zookeeper 四字命令
```
yum install nc

查看状态
echo stat | nc 192.168.1.1 2181

是否启动
echo ruok | nc 192.168.1.1 2181

未经处理的会话和临时节点
echo dump | nc 192.168.1.1 2181

查看服务器配置
echo conf | nc 192.168.1.1 2181

展示连接到服务器的客户端信息
echo cons | nc 192.168.1.1 2181

查看环境变量
echo envi | nc 192.168.1.1 2181

监控健康信息
echo mntr | nc 192.168.1.1 2181

展示 watcher 信息
echo wchs | nc 192.168.1.1 2181

展示 session 与 watch 信息
echo wchc | nc 192.168.1.1 2181

展示 path 与 watch 信息
echo wchp | nc 192.168.1.1 2181
```

zookeeper 集群配置
zoo.cfg 配置
```
clientPort=2181

server.1=192.168.1.111:2888:3888
server.2=192.168.1.112:2888:3888
server.3=192.168.1.113:2888:3888
```
在 data 目录下创建 myid 文件，内容分别为 1、2、3

```
./zkCli.sh -server localhost:2181
./zkServer.sh status
```

zookeeper java 客户端
curator

zookeeper 作用
1. master 节点选举
2. 统一配置文件管理（watcher）
3. 发布与订阅
4. 分布式锁
5. 集群管理



HBase
Namespace：相当于 RDBMS 中的数据库
Table：表名必须是能用在文件路径里的合法名字
Row：每一行代表着一个数据对象
Column：

RowKey：唯一标识一行记录，不可被改变

HBase 基于 Hadoop 的 HDFS 进行存储

HBase 写流程
1. Client 先访问 zookeeper，得到对应的 RegionServer 地址
2. Client 对 RegionServer 发起写请求，RegionServer 接受数据写入内存
3. 当 MemStore 的大小达到一定的值后，flush 到 StoreFile 并存储到 HDFS

HBase 读流程
1. Client 先访问 zookeeper，得到对应的 RegionServer 地址
2. Client 对 RegionServer 发起读请求
3. 当 RegionServer 收到 client 的读请求后，先扫描自己的 Memstore，再扫描 BlockCache，如果还没有找到则去 StoreFile 中读取数据，然后将数据返回给 Client