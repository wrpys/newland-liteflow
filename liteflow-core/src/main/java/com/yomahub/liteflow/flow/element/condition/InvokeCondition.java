package com.yomahub.liteflow.flow.element.condition;

import cn.hutool.core.collection.ListUtil;
import com.yomahub.liteflow.enums.ConditionTypeEnum;
import com.yomahub.liteflow.enums.NodeTypeEnum;
import com.yomahub.liteflow.exception.IfTypeErrorException;
import com.yomahub.liteflow.flow.element.Node;
import com.yomahub.liteflow.slot.DataBus;
import com.yomahub.liteflow.slot.Slot;
import com.yomahub.liteflow.util.LiteFlowProxyUtil;

/**
 * 函数调用Condition
 *
 * @author WRP
 * @since 2023/3/27
 */
public class InvokeCondition extends Condition {

    @Override
    public void execute(Integer slotIndex) throws Exception {
        if (ListUtil.toList(NodeTypeEnum.INVOKE).contains(getInvokeNode().getType())) {
            //先执行Invoke节点
            this.getInvokeNode().setCurrChainId(this.getCurrChainId());
            this.getInvokeNode().execute(slotIndex);

            Slot slot = DataBus.getSlot(slotIndex);
            //这里可能会有spring代理过的bean，所以拿到user原始的class
            Class<?> originalClass = LiteFlowProxyUtil.getUserClass(this.getInvokeNode().getInstance().getClass());
            //拿到Invoke执行过的结果
            boolean invokeResult = slot.getInvokeResult(originalClass.getName());
            // TODO

        } else {
            throw new IfTypeErrorException("if instance must be NodeInvokeComponent");
        }
    }

    @Override
    public ConditionTypeEnum getConditionType() {
        return ConditionTypeEnum.TYPE_INVOKE;
    }

    public Node getInvokeNode() {
        return (Node) this.getExecutableList().get(0);
    }
}
