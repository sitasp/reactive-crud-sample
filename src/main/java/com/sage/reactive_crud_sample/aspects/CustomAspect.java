package com.sage.reactive_crud_sample.aspects;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.lang.reflect.Method;

@Aspect
@Component
public class CustomAspect {
    @Around("execution(reactor.core.publisher.Mono *..*(..) ) && @annotation(com.example.demo.annotation.CustomEnumeration)")
    public Object handleCustomEnumeration(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        boolean isGetter = method.getName().startsWith("get");

        Object[] args = joinPoint.getArgs();
        Object target = joinPoint.getTarget();

        if (isGetter) {
            // Getter method
            return Mono.fromCallable(() -> {
                Object enumValue = method.invoke(target);
                return enumValue.toString();
            });
        } else {
            // Setter method
            return Mono.fromCallable(() -> {
                String stringValue = (String) args[0];
                Class<?> enumType = method.getParameterTypes()[0];
                Object enumValue = Enum.valueOf((Class<Enum>) enumType, stringValue);
                method.invoke(target, enumValue);
                return null;
            });
        }
    }
}
