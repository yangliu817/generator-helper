package cn.yangliu.mybatis.aop;

import cn.yangliu.mybatis.UpcallService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class AopHandler {

    @Pointcut("execution(* cn.yangliu.mybatis.UpcallService.*(..))")
    public void pointcut() {
    }


    @Around("pointcut()")
    public Object excute(ProceedingJoinPoint joinPoint) throws Throwable {
        UpcallService target = (UpcallService) joinPoint.getTarget();
        target.init();
        try {
            return joinPoint.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            throw throwable;
        }
    }

}
