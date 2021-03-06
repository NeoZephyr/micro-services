## 入口网关
### 网关作用
1. 提供客户端一个统一的接入地址，将用户的请求动态路由到不同的业务服务上，并且做一些必要的协议转换工作
2. 植入一些服务治理的策略，比如服务的熔断、降 级，流量控制和分流等等
3. 客户端的认证和授权的实现
4. 黑白名单
5. 日志记录

### 网关性能
为了提升网关对于请求的并行处理能力，一般使用线程池来并行的执行请求

如果后端的服务拆分得不多，可以针对不同的服务，采用不同的线程池，这样商品服务的故障就不会影响到支付服务和用户服务了

在线程池内部可以针对不同的服务，甚至不同的接口做线程的保护。比如说，线程池的最大线程数是 1000，那么可以给每个服务设置一个最多可以使用的配额


## 出口网关
在出口网关中，对调用外部的 API 做统一的认证、授 权，审计以及访问控制

