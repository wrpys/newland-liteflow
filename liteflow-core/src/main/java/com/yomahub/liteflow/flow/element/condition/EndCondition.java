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
 * 结束处理Condition
 *
 * @author WRP
 * @since 2023/3/27
 */
public class EndCondition extends Condition {

    @Override
    public void execute(Integer slotIndex) throws Exception {
        if (ListUtil.toList(NodeTypeEnum.END).contains(getEndNode().getType())) {
            //先执行END节点
            this.getEndNode().setCurrChainId(this.getCurrChainId());
            this.getEndNode().execute(slotIndex);

            Slot slot = DataBus.getSlot(slotIndex);
            //这里可能会有spring代理过的bean，所以拿到user原始的class
            Class<?> originalClass = LiteFlowProxyUtil.getUserClass(this.getEndNode().getInstance().getClass());
            //拿到END执行过的结果
            boolean endResult = slot.getEndResult(originalClass.getName());
            // TODO

        } else {
            throw new IfTypeErrorException("if instance must be NodeEndComponent");
        }
    }

    @Override
    public ConditionTypeEnum getConditionType() {
        return ConditionTypeEnum.TYPE_END;
    }

    public Node getEndNode() {
        return (Node) this.getExecutableList().get(0);
    }
}
