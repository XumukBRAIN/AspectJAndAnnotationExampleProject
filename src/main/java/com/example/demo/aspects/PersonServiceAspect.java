package com.example.demo.aspects;

import com.example.demo.annotations.Sout;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class PersonServiceAspect {

    @Before(value = "execution(* com.example.demo.services.*.*(..))")
    public void sout(JoinPoint joinPoint) {
        Class<?> aClass = joinPoint.getTarget().getClass();
        Method[] methods = aClass.getMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(Sout.class)) {
                System.out.println("Пришел запрос на получение рандомных людей!");
            }
        }
    }
}
