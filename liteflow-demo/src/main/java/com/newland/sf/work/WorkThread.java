package com.newland.sf.work;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.yomahub.liteflow.core.FlowExecutor;
import com.yomahub.liteflow.flow.LiteflowResponse;
import com.yomahub.liteflow.model.base.Event;
import com.yomahub.liteflow.slot.ContractContext;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author WRP
 * @since 2023/4/14
 */
public class WorkThread {

    private static final Logger LOGGER = LoggerFactory.getLogger(WorkThread.class);

    /**
     * 工作线程数
     */
    private Integer threadPoolSize;

    /**
     * 流程执行器
     */
    private FlowExecutor flowExecutor;

    /**
     * 计数器
     */
    private AtomicInteger atomicInteger;

    /**
     * 工作线程池
     */
    private ExecutorService executorService;

    /**
     * 本地Hash表 Map<contractId+stepId, threadName>
     */
    private Map<String, String> eventSttpMap;

    public WorkThread(Integer threadPoolSize, FlowExecutor flowExecutor) {

        this.threadPoolSize = threadPoolSize;
        this.flowExecutor = flowExecutor;

        atomicInteger = new AtomicInteger();

        ThreadFactory threadFactory = new ThreadFactoryBuilder()
                .setNameFormat("work-pool-%d").build();
        executorService = new ThreadPoolExecutor(threadPoolSize, threadPoolSize, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(2), threadFactory,
                new ThreadPoolExecutor.AbortPolicy());

        eventSttpMap = new ConcurrentHashMap<>(threadPoolSize);
    }

    /**
     * 计数器值是否小于等于工作线程数
     *
     * @return
     */
    public boolean isFree() {
        if (atomicInteger.incrementAndGet() <= this.threadPoolSize) {
            return true;
        } else {
            decrement();
            return false;
        }
    }

    /**
     * 计数器减一
     */
    public void decrement() {
        atomicInteger.decrementAndGet();
    }


    /**
     * 执行
     */
    public boolean execute(Event event, ProceedingJoinPoint joinPoint, Class argClazz) {
        final String stepKey = event.getContractId() + "-" + event.getPreRunId();

        // 本地Hash表是否存在事件步骤-已存在
        if (eventSttpMap.containsKey(stepKey)) {
            // 工作线程是否存活-是
            if (isAlive(eventSttpMap.get(stepKey))) {
                LOGGER.info("线程还存活，不处理。契约ID:{},步骤ID:{},线程NAME:{}", event.getContractId(), event.getPreRunId(), eventSttpMap.get(stepKey));
                decrement();
            }
            // 工作线程是否存活-否
            else {
                joinThreadPool(event, stepKey, joinPoint, argClazz);
            }
        }
        // 本地Hash表是否存在事件步骤-不存在
        else {
            joinThreadPool(event, stepKey, joinPoint, argClazz);
        }

        return true;
    }

    /**
     * 加入工作线程池
     *
     * @param event
     * @param stepKey
     * @param joinPoint
     * @param argClazz
     */
    private void joinThreadPool(Event event, String stepKey, ProceedingJoinPoint joinPoint, Class argClazz) {
        executorService.execute(() -> {
            eventSttpMap.put(stepKey, Thread.currentThread().getName());
            try {
                Object result = joinPoint.proceed();
                // 执行契约
                ContractContext contractContext = new ContractContext<>(argClazz);
                contractContext.setData((Event) result);
                LiteflowResponse response = flowExecutor.execute2Resp2(event.getContractId(), event.getChainId(), event.getPreRunId(), event.getEventId(), event.getStepResultMap(), contractContext);
                // 更改契约状态为【结束】
                if (response.isSuccess()) {

                }
                // 根据异常重试
                else {
                    LOGGER.error(event.getContractId() + "契约执行失败", response.getCause());
                }
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
            eventSttpMap.remove(stepKey);
            decrement();
        });
    }

    /**
     * 判断线程是否存活
     *
     * @param threadName
     * @return
     */
    private boolean isAlive(String threadName) {
        // 获取所有线程
        ThreadGroup currentGroup = Thread.currentThread().getThreadGroup();
        int noThreads = currentGroup.activeCount();
        Thread[] lstThreads = new Thread[noThreads];
        currentGroup.enumerate(lstThreads);
        // 遍历线程
        for (int i = 0; i < noThreads; i++) {
            if (lstThreads[i].getName().equals(threadName)) {
                return lstThreads[i].isAlive();
            }
        }
        return false;
    }

}
