package com.rjgf.threecost.crawdata.util.aspect;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Aspect
@Component
@Slf4j
public class MyAspect {


    private static final String START = "start";
    private static final String END = "end";

    @Pointcut("execution(* com.rjgf.threecost.crawdata.controller..*.*(..))")
    public void logPointCut() {}


    @Before("logPointCut()") //前置通知
    public void before(JoinPoint point) {
        try {
            printLog(point,START);
        } catch (Exception e) {
            log.error("RequestTimeOutAspect 打印出现异常：" + e.getMessage());
        }
    }

    @After("logPointCut()") //后置通知
    public void after(JoinPoint point) {
        try {
            printLog(point,END);
        } catch (Exception e) {
            log.error("RequestTimeOutAspect 打印出现异常：" + e.getMessage());
        }
    }



    /**
     * 定义了一个切入点，RequestTimeAnnotation注解的地方会添加打印请求前后时间
     */
    @Pointcut("@within(com.rjgf.threecost.crawdata.util.aspect.RequestTimeAnnotation)")
    public void requestTimeAnnotationLog() {}

    @Before("requestTimeAnnotationLog()") //前置通知
    public void requestTimeAnnotationBefore(JoinPoint point) {
        try {
            printLog(point,START);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("RequestTimeOutAspect 打印出现异常：" + e.getMessage());
        }
    }

    @After("requestTimeAnnotationLog()") //后置通知
    public void requestTimeAnnotationAfter(JoinPoint point) {
        try {
            printLog(point,END);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("RequestTimeOutAspect 打印出现异常：" + e.getMessage());
        }
    }

    private void printLog(JoinPoint joinPoint, String type) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String time = dateFormat.format(new Date());
        //请求的 类名、方法名
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();

        String value = joinPoint.getTarget().getClass().getAnnotation(RequestTimeAnnotation.class).value();
        log.info("注解的路径 value ========== " + value);
        //请求的参数
        Object[] args = joinPoint.getArgs();
        String threadName = Thread.currentThread().getName();
        long millis = System.currentTimeMillis();
        //打印日志
        log.error("thread：{} class：{} method：{} param: {} type：{} format time: {} currentTimeMillis: {} " , threadName, className,methodName, args, type,time, millis);
    }

}
