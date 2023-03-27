package com.yomahub.liteflow.springboot;

import com.yomahub.liteflow.core.FlowExecutor;
import org.springframework.beans.factory.InitializingBean;

/**
 * 执行器初始化类
 * 主要用于在启动时执行执行器的初始化方法，避免在运行执行器时第一次初始化而耗费时间
 * @author Bryan.Zhang
 */
public class LiteflowExecutorInit implements InitializingBean {

    private final FlowExecutor flowExecutor;

    public LiteflowExecutorInit(FlowExecutor flowExecutor) {
        this.flowExecutor = flowExecutor;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        flowExecutor.init(true);
    }
}
