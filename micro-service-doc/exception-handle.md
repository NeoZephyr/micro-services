```java
public enum ResultCode {
    SUCCESS(HttpServletResponse.SC_OK, "Operation is Successful"),

    FAILURE(HttpServletResponse.SC_BAD_REQUEST, "Biz Exception"),

    INTERNAL_SERVER_ERROR(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error"),

    PARAM_BIND_ERROR(HttpServletResponse.SC_BAD_REQUEST, "Parameter Binding Error"),

    final int code;

    final String msg;
}
```
```java
public class BaseResponse {
    private String message;
    private ResultCode code = ResultCode.SUCCESS;

    public boolean isSuccess() {
        return code == ResultCode.SUCCESS;
    }
}
```
```java
@RestControllerAdvice
public class GlobalExceptionTranslator {

    @ExceptionHandler(BindException.class)
    public BaseResponse handleError(BindException e) {
        logger.warn("Bind Exception", e);
        FieldError error = e.getFieldError();
        String message = String.format("%s:%s", error.getField(), error.getDefaultMessage());
        return BaseResponse
                .builder()
                .code(ResultCode.PARAM_BIND_ERROR)
                .message(message)
                .build();
    }

    @ExceptionHandler(ServiceException.class)
    public BaseResponse handleError(ServiceException e) {
        logger.error("Service Exception", e);
        return BaseResponse
                .builder()
                .code(e.getResultCode())
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(Throwable.class)
    public BaseResponse handleError(Throwable e) {
        logger.error("Internal Server Error", e);
        return BaseResponse
                .builder()
                .code(ResultCode.INTERNAL_SERVER_ERROR)
                .message(e.getMessage())
                .build();
    }
}
```

```java
@Controller
public class GlobalErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object statusCode = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object exception = request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);

        return "error";
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
```

