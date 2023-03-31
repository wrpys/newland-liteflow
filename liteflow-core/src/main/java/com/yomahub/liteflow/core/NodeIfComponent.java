package com.yomahub.liteflow.core;

import com.yomahub.liteflow.model.base.Event;
import com.yomahub.liteflow.slot.ContractContext;
import com.yomahub.liteflow.util.LiteFlowProxyUtil;
import com.yomahub.liteflow.util.SpringExpressionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * IF节点抽象类
 *
 * @author Bryan.Zhang
 * @since 2.8.5
 */
public abstract class NodeIfComponent extends NodeComponent {

    private static final Logger LOGGER = LoggerFactory.getLogger(NodeIfComponent.class);

    @Override
    public void process() throws Exception {
        ContractContext context = this.getContextBean(ContractContext.class);

        String expr = this.getExpr();
        Boolean ifResult = SpringExpressionUtil.parseExpression(expr, context);

        Class<?> originalClass = LiteFlowProxyUtil.getUserClass(this.getClass());
        this.getSlot().setIfResult(originalClass.getName(), ifResult);
    }

    public abstract String getExpr() throws Exception;
}
