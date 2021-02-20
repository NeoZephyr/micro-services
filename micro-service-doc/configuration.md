## 配置存储


## 变更推送
### 轮询查询
如果有很多应用服务器都去轮询拉取配置，由于返回的配置项可能会很大，配置中心服务的带宽就会成为瓶颈

为配置中心的每一个配置项，多存储一个根据配置项计算出来的 MD5 值。配置项一旦变化，这个 MD5 值也会随之改变。在轮询查询的时候，需要先确认存储的 MD5 值，和配置中心的 MD5 是不是一致的。只有不一致才会从配置中心拉取最新的配置

### 长连推送


## 高可用
配置中心客户端在获取到配置信息后，会同时把配置信息同步地写入到内存缓存，并且异步地写入到文件缓存中

内存缓存的作用是降低客户端和配置中心的交互频率，提升配置获取的性能；文件的缓存的作用就是灾备，当应用程序重启时，一旦配置中心发生故障，那么应用程序就会优先使用文件中的配置，这样虽然无法得到配置的变更消息，但是应用程序还是可以启动起来的