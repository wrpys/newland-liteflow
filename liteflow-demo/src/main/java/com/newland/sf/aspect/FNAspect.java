//package com.newland.sf.aspect;
//
//import com.newland.sf.annotation.SLFFunction;
//import com.yomahub.liteflow.model.base.Event;
//import com.newland.sf.queue.EventQueue;
//import com.newland.sf.utils.Json;
//import com.yomahub.liteflow.core.FlowExecutor;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.reflect.MethodSignature;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.lang.reflect.Method;
//import java.util.concurrent.BlockingQueue;
//import java.util.concurrent.TimeUnit;
//
//@Aspect
//@Component
//public class FNAspect {
//    private static final Logger LOGGER = LoggerFactory.getLogger(FNAspect.class);
//
//    @Autowired
//    private EventQueue eventQueue;
//
//    @Autowired
//    private FlowExecutor flowExecutor;
//
////    @Before("@annotation(slfFunction)")
////    public void before(JoinPoint joinPoint, SLFFunction slfFunction) {
////        LOGGER.info("SLFFunction before begin");
////        String name = slfFunction.name();
////        String version = slfFunction.version();
////
////        LOGGER.info("SLFFunction name:{},version:{}", name, version);
////    }
//
//    @Around("@annotation(slfFunction)")
//    public Object around(ProceedingJoinPoint joinPoint, SLFFunction slfFunction) throws Throwable {
//
//        LOGGER.info("SLFFunction aroundApply begin");
//
//        String name = slfFunction.name();
//        String version = slfFunction.version();
//
//        LOGGER.info("SLFFunction name:{},version:{}", name, version);
//
//        // 获取连接点方法
//        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
//        Method method = methodSignature.getMethod();
//
//        Object[] args = joinPoint.getArgs();
//
//        if (args.length != 1) {
//            throw new Exception("参数个数有误！");
//        }
//
//        Event param = (Event) args[0];
//        param.setFunName(name);
//        param.setFunVersion(version);
//
//        LOGGER.info("eventId:{}", param.getEventId());
//        LOGGER.info("eventType:{}", param.getEventType());
//        LOGGER.info("contractId:{}", param.getContractId());
//
//        BlockingQueue<ProceedingJoinPoint> queue = eventQueue.getQueue();
//        if (queue.size() >= 10) {
//            param.setCode("0");
//            param.setMsg("已超过队列长度，请重试！");
//        } else {
//            try {
//
//                LOGGER.info("joinPoint:{}", joinPoint);
//
//                boolean flag = queue.offer(joinPoint, 1000L, TimeUnit.MILLISECONDS);
//                if (flag) {
//                    param.setCode("1");
//                    param.setMsg("操作成功！");
//                } else {
//                    param.setCode("0");
//                    param.setMsg("操作失败，请重试！");
//                }
//            } catch (InterruptedException e) {
//                param.setCode("0");
//                param.setMsg("中断失败，请重试！");
//            }
//        }
//        LOGGER.info("result:{}", Json.toJson(param));
//        return param;
//
//
//
//        // 业务逻辑执行
////        Object object = joinPoint.proceed();
////        Object object = "test";
//
////        DefaultContext defaultContext = new DefaultContext();
////        defaultContext.setData("input", param);
////        defaultContext.setData("result", object);
////        LiteflowResponse response = flowExecutor.execute2Resp("custom", null, defaultContext);
////        LOGGER.info("requestId:{}", response.getRequestId());
////
////
////        LOGGER.info("result:{}", object);
//
////        return param;
//    }
//
//}
