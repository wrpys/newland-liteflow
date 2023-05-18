package com.yomahub.liteflow.builder.el.operator;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import com.yomahub.liteflow.builder.el.operator.base.BaseOperator;
import com.yomahub.liteflow.common.ChainConstant;
import com.yomahub.liteflow.core.NodeEndComponent;
import com.yomahub.liteflow.enums.NodeTypeEnum;
import com.yomahub.liteflow.flow.element.Node;
import com.yomahub.liteflow.flow.element.condition.EndCondition;

import java.util.HashMap;
import java.util.Map;

/**
 * EL规则中的End的操作符
 *
 * @author WRP
 * @since 2023/3/27
 */
public class EndOperator extends BaseOperator<EndCondition> {

    @Override
    public EndCondition build(Object[] objects) throws Exception {

        Node node = new Node();
        NodeEndComponent nodeEndComponent = new NodeEndComponent() {

            @Override
            public Map<String, String> getArgs() {
                return new HashMap<>();
            }
        };
        nodeEndComponent.setSelf(nodeEndComponent);
        nodeEndComponent.setNodeId(StrUtil.format("END_{}", 1));
        node.setInstance(nodeEndComponent);
        node.setType(NodeTypeEnum.END);

        EndCondition endCondition = new EndCondition();
        endCondition.setId(ChainConstant.END);
        endCondition.setExecutableList(ListUtil.toList(node));
        return endCondition;
    }
}
