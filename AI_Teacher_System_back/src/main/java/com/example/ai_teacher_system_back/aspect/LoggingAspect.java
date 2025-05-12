package com.example.ai_teacher_system_back.aspect;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * 日志切面
 * 用于增强系统日志功能，记录方法调用和异常信息
 */
@Aspect
@Component
@Slf4j
public class LoggingAspect {

    /**
     * 定义切点 - 所有controller包下的方法
     */
    @Pointcut("execution(* com.example.ai_teacher_system_back.controller..*.*(..))") 
    public void controllerPointcut() {}
    
    /**
     * 定义切点 - 所有service包下的方法
     */
    @Pointcut("execution(* com.example.ai_teacher_system_back.service..*.*(..))") 
    public void servicePointcut() {}
    
    /**
     * 定义切点 - 所有repository包下的方法
     */
    @Pointcut("execution(* com.example.ai_teacher_system_back.repository..*.*(..))") 
    public void repositoryPointcut() {}
    
    /**
     * 环绕通知 - 记录方法执行时间和参数
     */
    @Around("controllerPointcut() || servicePointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String className = methodSignature.getDeclaringType().getSimpleName();
        String methodName = methodSignature.getName();
        
        log.debug("开始执行: {}.{}() 参数: {}", className, methodName, Arrays.toString(joinPoint.getArgs()));
        
        long startTime = System.currentTimeMillis();
        Object result;
        
        try {
            result = joinPoint.proceed();
            long endTime = System.currentTimeMillis();
            log.debug("结束执行: {}.{}() 耗时: {}ms", className, methodName, (endTime - startTime));
            return result;
        } catch (Exception e) {
            log.error("执行异常: {}.{}() 异常信息: {}", className, methodName, e.getMessage());
            throw e;
        }
    }
    
    /**
     * 异常通知 - 详细记录异常信息
     */
    @AfterThrowing(pointcut = "controllerPointcut() || servicePointcut() || repositoryPointcut()", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String className = methodSignature.getDeclaringType().getSimpleName();
        String methodName = methodSignature.getName();
        
        log.error("异常详情 - 类: {}, 方法: {}, 参数: {}, 异常类型: {}, 异常消息: {}", 
                className, 
                methodName, 
                Arrays.toString(joinPoint.getArgs()),
                e.getClass().getName(),
                e.getMessage());
        
        // 记录异常堆栈信息
        if (log.isDebugEnabled()) {
            log.debug("异常堆栈:", e);
        }
    }
}