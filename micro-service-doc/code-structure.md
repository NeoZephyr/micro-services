## Multi-Repo
### 优点
1. 明确所有权
2. 更容易管理
3. push/pull 等操作更有效率

### 缺点
1. 多个代码库中难以统一进行标准化
2. 关注某一服务时，失去上下文，缺乏对全局的了解

## Mono-Repo
### 优点
1. 方便在本地运行整个平台，便于了解所有服务是如何协同工作的，以及可以尽早的发现错误
2. 比较容易地实现多个服务统一标准化
3. 更加有效地代码审查
4. 有效地开发出通用组件，使得每个微服务的代码变得越来越小
5. 方便重构

```xml
<properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <java.version>1.8</java.version>
    <spring.boot.version>2.1.2.RELEASE</spring.boot.version>
    <spring.cloud.version>Greenwich.RELEASE</spring.cloud.version>
</properties>
```
```xml
<modules>
    <module>common-lib</module>
    <module>sms-svc</module>
    <module>sms-api</module>
    <module>gateway</module>
</modules>
```
```xml
<dependencyManagement>
    <dependencies>
        <!-- Spring Boot -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-dependencies</artifactId>
            <version>${spring.boot.version}</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
        <!-- Spring Cloud -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-dependencies</artifactId>
            <version>${spring.cloud.version}</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
        <!-- Common -->
        <dependency>
            <groupId>com.pain.joy</groupId>
            <artifactId>common-lib</artifactId>
            <version>1.0.1</version>
        </dependency>
    </dependencies>
</dependencyManagement>

<dependencies>
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <scope>provided</scope>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-devtools</artifactId>
        <optional>true</optional>
    </dependency>
</dependencies>
```