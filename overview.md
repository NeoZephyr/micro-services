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



