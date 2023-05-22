package com.yomahub.liteflow.builder.el.operator;

import cn.hutool.core.util.StrUtil;
import com.yomahub.liteflow.builder.el.operator.base.BaseOperator;
import com.yomahub.liteflow.builder.el.operator.base.OperatorHelper;
import com.yomahub.liteflow.flow.FlowBus;
import com.yomahub.liteflow.flow.element.condition.InvokeCondition;

/**
 * EL规则中的INVOKE的操作符
 *
 * @author WRP
 * @since 2023/3/27
 */
public class InvokeOperator extends BaseOperator<InvokeCondition> {

    @Override
    public InvokeCondition build(Object[] objects) throws Exception {
        OperatorHelper.checkObjectSizeEq(objects, 2, 3);

        //解析第一个参数
        final String funName = OperatorHelper.convert(objects[0], String.class);
        final String funVersion = OperatorHelper.convert(objects[1], String.class);

        InvokeCondition invokeCondition = new InvokeCondition();
        invokeCondition.setId(StrUtil.format("INVOKE('{}','{}')", funName, funVersion));
        invokeCondition.setRunId(FlowBus.getRunId(this.getContractId()));
        invokeCondition.setFunName(funName);
        invokeCondition.setFunVersion(funVersion);
        return invokeCondition;
    }
}
