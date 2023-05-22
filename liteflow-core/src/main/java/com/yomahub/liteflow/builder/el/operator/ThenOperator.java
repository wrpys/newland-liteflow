package com.yomahub.liteflow.builder.el.operator;

import com.yomahub.liteflow.builder.el.operator.base.BaseOperator;
import com.yomahub.liteflow.builder.el.operator.base.OperatorHelper;
import com.yomahub.liteflow.common.ChainConstant;
import com.yomahub.liteflow.flow.FlowBus;
import com.yomahub.liteflow.flow.element.Executable;
import com.yomahub.liteflow.flow.element.Node;
import com.yomahub.liteflow.flow.element.condition.ThenCondition;

/**
 * EL规则中的THEN的操作符
 *
 * @author Bryan.Zhang
 * @since 2.8.0
 */
public class ThenOperator extends BaseOperator<ThenCondition> {

    @Override
    public ThenCondition build(Object[] objects) throws Exception {
        OperatorHelper.checkObjectSizeGtZero(objects);

        ThenCondition thenCondition = new ThenCondition();
        thenCondition.setRunId(FlowBus.getRunId(this.getContractId()));
        thenCondition.setId(ChainConstant.THEN);

        for (Object obj : objects) {
            if (obj instanceof Node) {
                Node node = (Node) obj;
                Node n = node.copy();
                n.setRunId(FlowBus.getRunId(this.getContractId()));
                thenCondition.addExecutable(n);
            } else {
                thenCondition.addExecutable(OperatorHelper.convert(obj, Executable.class));
            }
        }
        return thenCondition;
    }
}
