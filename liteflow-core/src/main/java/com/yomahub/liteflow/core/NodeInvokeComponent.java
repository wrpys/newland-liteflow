package com.yomahub.liteflow.core;

import cn.hutool.http.HttpUtil;
import com.yomahub.liteflow.model.base.Event;
import com.yomahub.liteflow.slot.ContractContext;
import com.yomahub.liteflow.util.JsonUtil;
import com.yomahub.liteflow.util.LiteFlowProxyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * Invoke节点抽象类
 *
 * @author WRP
 * @since 2023/3/27
 */
public abstract class NodeInvokeComponent extends NodeComponent {

    private static final Logger LOGGER = LoggerFactory.getLogger(NodeInvokeComponent.class);

    @Override
    public void process() throws Exception {
        ContractContext context = this.getContextBean(ContractContext.class);

        // TODO 调用函数服务
        StringBuilder url = new StringBuilder("http://localhost:8080/custom/");
        url.append(this.getFunName()).append("/").append(this.getFunVersion());
        LOGGER.info("url:{}", url);
        String body = HttpUtil.post(url.toString(), JsonUtil.toJsonString(context.getInput()));

        Event event = JsonUtil.parseObject(body, Event.class);
        Class<?> originalClass = LiteFlowProxyUtil.getUserClass(this.getClass());
        if (Objects.equals(event.getCode(), "1")) {
            this.getSlot().setInvokeResult(originalClass.getName(), true);
        } else {
            this.getSlot().setInvokeResult(originalClass.getName(), false);
        }

    }

    public abstract String getFunName();

    public abstract String getFunVersion();
}
