package com.yomahub.liteflow.builder.el.operator;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import com.yomahub.liteflow.builder.el.operator.base.BaseOperator;
import com.yomahub.liteflow.builder.el.operator.base.OperatorHelper;
import com.yomahub.liteflow.common.ChainConstant;
import com.yomahub.liteflow.core.NodeInvokeComponent;
import com.yomahub.liteflow.enums.NodeTypeEnum;
import com.yomahub.liteflow.flow.element.Node;
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

        Node node = new Node();

        NodeInvokeComponent nodeInvokeComponent = new NodeInvokeComponent() {
            @Override
            public String getFunName() {
                return funName;
            }

            @Override
            public String getFunVersion() {
                return funVersion;
            }
        };
        nodeInvokeComponent.setSelf(nodeInvokeComponent);
        nodeInvokeComponent.setNodeId(StrUtil.format("INVOKE('{}','{}')", funName, funVersion));
        node.setInstance(nodeInvokeComponent);
        node.setType(NodeTypeEnum.INVOKE);

        InvokeCondition invokeCondition = new InvokeCondition();
        invokeCondition.setId(ChainConstant.INVOKE);
        invokeCondition.setExecutableList(ListUtil.toList(node));
        return invokeCondition;
    }
}
