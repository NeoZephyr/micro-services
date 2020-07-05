## 切面编程
静态代理，典型的代表是 AspectJ，它的特点是在编译期做切面代码注入。静态代理增加了编译的时间，在运行期就基本对于性能没有影响

动态代理，典型的代表是 Spring AOP，它的特点是在运行期做切面代码注入。动态代理不会去修改生成的 Class 文件，而是会在运行期生成一个代理对象，代理对象对源对象做了字节码增强，来完成切面所要执行的操作。由于在运行期需要生成代理对象，所以动态代理的性能要比静态代理要差


## 日志
使用静态代理的方式

```java
@Aspect
public class Tracer {
    @Around(value = "execution(public methodsig)", argNames = "pjp")
    public Object trace(ProceedingJoinPoint pjp) throws Throwable {
        TraceContext traceCtx = TraceContext.get();
        String requestId = reqCtx.getRequestId();
        String sig = pjp.getSignature().toShortString();
        boolean isSuccessful = false;
        String errorMsg = "";
        Object result = null;
        long start = System.currentTimeMillis();

        try {
            result = pjp.proceed();
            isSuccessful = true;
            return result;
        } catch (Throwable t) {
            isSuccessful = false;
            errorMsg = t.getMessage();
            return result;
        } finally {
            long elapseTime = System.currentTimeMillis() - start;
            Logs.info("rid : " + requestId + ", start time: " + start + ", elapseTime: " + elapseTime);
        }
    }
}
```

如果日志太多，可以考虑对请求做采样。比如想采样 10% 的日志，那么可以只打印 requestId % 10 == 0 的请求


## 分布式 Trace
采用 traceId + spanId 这两个数据维度来记录服务之间的调用关系。traceId 就是 requestId，串起单次请求，用 spanId 记录每一次远程调用

1. 用户到 A 服务之后会初始化一个 traceId 为 100，spanId 为 1
2. A 调用 B 服务时，traceId 不变，而 spanId 用 1.1 标识，代表上一级的 spanId 是 1，这一级的调用次序是 1
3. A 调用 C 服务时，traceId 不变，spanId 变为 1.2，代表上一级的 spanId 还是 1，而调用次序则变成了 2


A 服务在发起 RPC 请求服务 B 前，先从线程上下文中获取当前的 traceId 和 spanId，然后，依据上面的逻辑生成本次调用的 spanId，再将 spanId 和 traceId 序 列化后，装配到请求体中，发送给服务方 B

服务方 B 获取请求后，从请求体中反序列化出 spanId 和 traceId，同时设置到线程上下文中，以便给下次调用使用。在服务 B 调用完成返回响应前，计算出服务 B 的执行时间发送给消息队列
