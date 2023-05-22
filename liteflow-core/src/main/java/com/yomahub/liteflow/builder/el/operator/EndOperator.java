package com.yomahub.liteflow.builder.el.operator;

import com.yomahub.liteflow.builder.el.operator.base.BaseOperator;
import com.yomahub.liteflow.common.ChainConstant;
import com.yomahub.liteflow.flow.FlowBus;
import com.yomahub.liteflow.flow.element.condition.EndCondition;

/**
 * EL规则中的End的操作符
 *
 * @author WRP
 * @since 2023/3/27
 */
public class EndOperator extends BaseOperator<EndCondition> {

    @Override
    public EndCondition build(Object[] objects) throws Exception {
        EndCondition endCondition = new EndCondition();
        endCondition.setId(ChainConstant.END);
        endCondition.setRunId(FlowBus.getRunId(this.getContractId()));
        return endCondition;
    }
}
