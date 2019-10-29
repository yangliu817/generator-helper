package cn.yangliu.mybatis.handler;

import cn.yangliu.comm.entity.JsonResult;
import cn.yangliu.mybatis.anonntations.JsonResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;

/**
 * @author 杨柳
 * @date 2019-10-29
 */
@ControllerAdvice
public class ResponseBodyAdvice implements org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        Class<?> declaringClass = methodParameter.getMethod().getDeclaringClass();
        return declaringClass.isAnnotationPresent(JsonResponse.class);
    }

    @Override
    public Object beforeBodyWrite(Object data, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        return new JsonResult<>(data);
    }
}
