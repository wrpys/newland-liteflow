package com.yomahub.liteflow.core;

import cn.hutool.http.HttpUtil;
import com.yomahub.liteflow.model.base.Event;
import com.yomahub.liteflow.slot.ContractContext;
import com.yomahub.liteflow.util.JsonUtil;
import com.yomahub.liteflow.util.LiteFlowProxyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Objects;

/**
 * End节点抽象类
 *
 * @author WRP
 * @since 2023/3/27
 */
public abstract class NodeEndComponent extends NodeComponent {

    private static final Logger LOGGER = LoggerFactory.getLogger(NodeEndComponent.class);

    @Override
    public void process() throws Exception {
        ContractContext context = this.getContextBean(ContractContext.class);

        // TODO 调用结束回调接口
        StringBuilder url = new StringBuilder("http://localhost:8080/custom/end");
        LOGGER.info("url:{}", url);
        String body = HttpUtil.post(url.toString(), JsonUtil.toJsonString(context.getOutput()));

        Event event = JsonUtil.parseObject(body, Event.class);
        Class<?> originalClass = LiteFlowProxyUtil.getUserClass(this.getClass());
        if (Objects.equals(event.getCode(), "1")) {
            this.getSlot().setEndResult(originalClass.getName(), true);
        } else {
            this.getSlot().setEndResult(originalClass.getName(), false);
        }
    }

    public abstract Map<String, String> getArgs();

}
