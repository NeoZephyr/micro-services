# security

## 过滤器

### BasicAuthenticationFilter

如果在请求中找到一个 basic auth http 头，则尝试用该头中的用户名和密码验证用户

```http
GET http://localhost:8080/users/greeting
Authorization: Basic user c8e2a704-1636-4111-816a-65121251ebe2
```

### UsernamePasswordAuthenticationFilter

如果在请求参数或者 post 的 request body 中找到用户名、密码，则尝试用这些值对用户进行身份验证

### DefaultLoginPageGeneratingFilter

默认登录页面生成过滤器，用于生成一个登录页面

### DefaultLogoutPageGeneratingFilter

生成注销页面

### FilterSecurityInterceptor

过滤安全拦截器，用于授权逻辑

## 核心组件

### SecurityContext

存储当前认证的用户的详细信息

### SecurityContextHolder

工具类，提供对安全上下文的访问

### Authentication

存储了当前用户的详细信息

Principal 为用户的信息
Credentials 为密码
Authorities 为权限


JdbcUserDetailsManager
JdbcDaoImpl