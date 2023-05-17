package com.yomahub.liteflow.core;

import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.yomahub.liteflow.model.base.Event;
import com.yomahub.liteflow.slot.ContractContext;
import com.yomahub.liteflow.util.JsonUtil;
import com.yomahub.liteflow.util.LiteFlowProxyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * Fun节点抽象类
 *
 * @author WRP
 * @since 2023/3/27
 */
public abstract class NodeFunComponent extends NodeComponent {

    private static final Logger LOGGER = LoggerFactory.getLogger(NodeFunComponent.class);

    @Override
    public void process() throws Exception {
        ContractContext context = this.getContextBean(ContractContext.class);

        // TODO 调用函数服务
        StringBuilder url = new StringBuilder("http://localhost:8080/custom/");
        url.append(this.getFunName()).append("/").append(this.getFunVersion());
        LOGGER.info("url:{}", url);
        HttpResponse httpResponse = HttpUtil.createPost(url.toString()).body(JsonUtil.toJsonString(context.getInput())).execute();
        if (httpResponse.getStatus() != 200) {
            throw new RuntimeException("请求异常！" + httpResponse.body());
        }

        Event event = JsonUtil.parseObject(httpResponse.body(), Event.class);
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
