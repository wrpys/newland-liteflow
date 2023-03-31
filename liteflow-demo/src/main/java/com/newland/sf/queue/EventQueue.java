package com.newland.sf.queue;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author WRP
 * @since 2023/3/28
 */
@Component
public class EventQueue {

    private static BlockingQueue<ProceedingJoinPoint> queue = new LinkedBlockingQueue(10);

    public BlockingQueue<ProceedingJoinPoint> getQueue() {
        return queue;
    }

}
