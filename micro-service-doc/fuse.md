## 断路器模式
服务调用方为每一个调用的服务维护一个有限状态机，在这个状态机中会有三种状态：关闭、半打开和打开

当调用失败的次数累积到一定的阈值时，熔断状态从关闭态切换到打开态。一般在实现时，如果调用成功一次，就会重置调用失败次数

当熔断处于打开状态时，启动一个超时计时器，当计时器超时后，状态切换到半打开态。也可以通过设置一个定时器，定期地探测服务是否恢复

在熔断处于半打开状态时，请求可以达到后端服务，如果累计一定的成功次数后，状态切换到关闭态；如果出现调用失败的情况，则切换到打开态


当熔断器处于 Open 状态时，定期地检测 Redis 组件是否可用
```java
new Timer("RedisPort-Recover", true).scheduleAtFixedRate(new TimerTask() {
    public void run() {
        if (breaker.isOpen()) {
            Jedis jedis = null;

            try {
                jedis = connPool.getResource();
                jedis.ping(); // 验证 redis 是否可用
                successCount.set(0); // 重置连续成功的计数
                breaker.setHalfOpen(); // 设置为半打开态
            } catch (Exception ignore) {
            } finally {
                if (jedis != null) {
                    jedis.close();
                }
            }
        }
    }
}, 0, recoverInterval);
```

```java
if (breaker.isOpen()) {
    return null;
}
```

```java
K value = null;
Jedis jedis = null;

try {
    jedis = connPool.getResource();
    value = callback.call(jedis);

    if (breaker.isHalfOpen()) { // 如果是半打开状态
        if (successCount.incrementAndGet() >= SUCCESS_THRESHOLD) {
            failCount.set(0); // 清空失败数
            breaker.setClose(); // 设置为关闭态
        }
    }
    return value;
} catch (JedisException je) {
    if (breaker.isClose()) { // 如果是关闭态
        if (failCount.incrementAndGet() >= FAILS_THRESHOLD) {
            breaker.setOpen(); // 设置为打开态
        }
    } else if (breaker.isHalfOpen()) { // 如果是半打开态
        breaker.setOpen(); // 直接设置为打开态
    }

    throw  je;
} finally {
    if (jedis != null) {
        jedis.close();
    }
}
```


## 降级
### 开关降级
在代码中预先埋设一些开关，用来控制服务调用的返回值。开关关闭的时候正常调用远程服务，开关打开时则执行降级的策略。这些开关的值可以存储在配置中心中，当系统出现问题需要降级时，只需要通过配置中心动态更改开关的值，就可以实现不重启服务快速地降级远程服务

```java
boolean switcherValue = getFromConfigCenter("degrade.comment");

if (!switcherValue) {
    List<Comment> comments = getCommentList();
} else {
    List<Comment> comments = new ArrayList();
}
```

降级策略：
1. 读数据场景，一般是直接返回降级数据。比如，如果数据库的压力比较大，在降级的时候，可以考虑只读取缓存的数据，而不再读取数据库中的数据；如果非核心接口出现问题，可以直接返回服务繁忙或者返回固定的降级数据

2. 轮询查询场景，比如每隔 30 秒轮询获取未读数，可以降低获取数据的频率

3. 写数据场景，一般考虑把同步写转换成异步写，这样可以牺牲一些数据一致性和实效性来保证系统的可用性

