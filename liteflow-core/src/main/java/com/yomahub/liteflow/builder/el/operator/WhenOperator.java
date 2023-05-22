package com.yomahub.liteflow.builder.el.operator;

import com.yomahub.liteflow.builder.el.operator.base.BaseOperator;
import com.yomahub.liteflow.builder.el.operator.base.OperatorHelper;
import com.yomahub.liteflow.common.ChainConstant;
import com.yomahub.liteflow.flow.FlowBus;
import com.yomahub.liteflow.flow.element.Executable;
import com.yomahub.liteflow.flow.element.Node;
import com.yomahub.liteflow.flow.element.condition.WhenCondition;

/**
 * EL规则中的WHEN的操作符
 * @author Bryan.Zhang
 * @since 2.8.0
 */
public class WhenOperator extends BaseOperator<WhenCondition> {

    @Override
    public WhenCondition build(Object[] objects) throws Exception {
        OperatorHelper.checkObjectSizeGtZero(objects);

        WhenCondition whenCondition = new WhenCondition();
        whenCondition.setRunId(FlowBus.getRunId(this.getContractId()));
        whenCondition.setId(ChainConstant.WHEN);
        for (Object obj : objects) {
            if (obj instanceof Node) {
                Node node = (Node) obj;
                Node n = node.copy();
                n.setRunId(FlowBus.getRunId(this.getContractId()));
                whenCondition.addExecutable(n);
            } else {
                whenCondition.addExecutable(OperatorHelper.convert(obj, Executable.class));
            }
        }
        return whenCondition;
    }
}
