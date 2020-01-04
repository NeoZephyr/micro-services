## 微服务公共关注点

### 服务发现和 LB
Spring Cloud: Eureka + Ribbon
K8s: Service

### API 网关
Spring Cloud: Zuul
K8s: Ingress

### 配置管理
Spring Cloud: Spring Cloud Config
K8s: ConfigMaps/Secrets

### 弹性和容错
Spring Cloud: Hystrix
K8s: HealthCheck/Probe/ServiceMesh

### 日志监控
Spring Cloud: ELK
K8s: EFK

### Metrics 监控
Spring Cloud: Actuator/MicroMeter + Prometheus
K8s: Heapster + Prometheus

### 调用链监控
Spring Cloud: SpringCloud Sleuth/Zipkin
K8s: Jaeger/Zipkin

### 应用打包
Spring Cloud: Uber Jar/War
K8s: Docker Image/Helm

### 调度和发布
K8s: Scheduler

### 自愈和自动伸缩
K8s: Scheduler/AutoScaler

### 进程隔离
K8s: Docker/Pod

### 环境管理
K8s: Namespace/Auththorization

### 资源配额
K8s: CPU/Mem Limit , Namespace Quotas

### 流量治理
K8s: ServiceMesh




