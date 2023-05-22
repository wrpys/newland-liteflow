package com.yomahub.liteflow.builder.el.operator;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import com.yomahub.liteflow.builder.el.operator.base.BaseOperator;
import com.yomahub.liteflow.builder.el.operator.base.OperatorHelper;
import com.yomahub.liteflow.core.NodeIfComponent;
import com.yomahub.liteflow.enums.NodeTypeEnum;
import com.yomahub.liteflow.exception.ELParseException;
import com.yomahub.liteflow.flow.FlowBus;
import com.yomahub.liteflow.flow.element.Executable;
import com.yomahub.liteflow.flow.element.Node;
import com.yomahub.liteflow.flow.element.condition.IfCondition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * EL规则中的ELIF的操作符
 *
 * @author Bryan.Zhang
 * @since 2.8.5
 */
public class ElifOperator extends BaseOperator<IfCondition> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ElifOperator.class);

    @Override
    public IfCondition build(Object[] objects) throws Exception {
        OperatorHelper.checkObjectSizeEqThree(objects);

        //解析caller
        IfCondition ifCondition = OperatorHelper.convert(objects[0], IfCondition.class);

        //解析第一个参数
        final String expr = OperatorHelper.convert(objects[1], String.class);
        if (!expr.startsWith("input") && !expr.startsWith("output")) {
            LOGGER.error("Spring EL表达式[{}]有误，使用不存在的变量！", expr);
            throw new ELParseException("Spring EL表达式[" + expr + "]有误，使用不存在的变量！");
        }

        Node node = new Node();
        NodeIfComponent nodeIfComponent = new NodeIfComponent() {
            @Override
            public String getExpr() throws Exception {
                return expr;
            }
        };
        nodeIfComponent.setSelf(nodeIfComponent);
        nodeIfComponent.setNodeId(StrUtil.format("ELIF('{}')", expr));
        node.setInstance(nodeIfComponent);
        node.setType(NodeTypeEnum.IF);
        node.setRunId(FlowBus.getRunId(this.getContractId()));

        //解析第二个参数
        Executable trueCaseExecutableItem;
        if (objects[2] instanceof Node) {
            Node nodeTmp = (Node) objects[2];
            Node n = nodeTmp.copy();
            n.setRunId(FlowBus.getRunId(this.getContractId()));
            trueCaseExecutableItem = n;
        } else {
            trueCaseExecutableItem = OperatorHelper.convert(objects[2], Executable.class);
        }

        //构建一个内部的IfCondition
        IfCondition ifConditionItem = new IfCondition();
//        ifConditionItem.setRunId(FlowBus.getRunId(this.getContractId()));
        ifConditionItem.setExecutableList(ListUtil.toList(node));
        ifConditionItem.setTrueCaseExecutableItem(trueCaseExecutableItem);

        //因为可能会有多个ELIF，所以每一次拿到的caller总是最开始大的if，需要遍历到没有falseCaseExecutable的地方。
        //塞进去是一个新的IfCondition
        IfCondition loopIfCondition = ifCondition;
        while (true) {
            if (loopIfCondition.getFalseCaseExecutableItem() == null) {
                loopIfCondition.setFalseCaseExecutableItem(ifConditionItem);
                break;
            } else {
                loopIfCondition = (IfCondition) loopIfCondition.getFalseCaseExecutableItem();
            }
        }

        return ifCondition;
    }
}
