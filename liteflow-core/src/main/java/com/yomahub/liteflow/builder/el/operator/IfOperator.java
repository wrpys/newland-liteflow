package com.yomahub.liteflow.builder.el.operator;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import com.yomahub.liteflow.builder.el.operator.base.BaseOperator;
import com.yomahub.liteflow.builder.el.operator.base.OperatorHelper;
import com.yomahub.liteflow.core.NodeIfComponent;
import com.yomahub.liteflow.enums.NodeTypeEnum;
import com.yomahub.liteflow.exception.ELParseException;
import com.yomahub.liteflow.flow.element.Executable;
import com.yomahub.liteflow.flow.element.Node;
import com.yomahub.liteflow.flow.element.condition.IfCondition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * EL规则中的IF的操作符
 *
 * @author Bryan.Zhang
 * @since 2.8.5
 */
public class IfOperator extends BaseOperator<IfCondition> {

    private static final Logger LOGGER = LoggerFactory.getLogger(IfOperator.class);

    @Override
    public IfCondition build(Object[] objects) throws Exception {
        OperatorHelper.checkObjectSizeEq(objects, 2, 3);

        //解析第一个参数
        final String expr = OperatorHelper.convert(objects[0], String.class);
        if (!expr.startsWith("input") || !expr.startsWith("output")) {
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
        nodeIfComponent.setNodeId(StrUtil.format("IF('{}')", expr));
        node.setInstance(nodeIfComponent);
        node.setType(NodeTypeEnum.IF);

        //解析第二个参数
        Executable trueCaseExecutableItem = OperatorHelper.convert(objects[1], Executable.class);

        //解析第三个参数，如果有的话
        Executable falseCaseExecutableItem = null;
        if (objects.length == 3) {
            falseCaseExecutableItem = OperatorHelper.convert(objects[2], Executable.class);
        }

        IfCondition ifCondition = new IfCondition();
        ifCondition.setExecutableList(ListUtil.toList(node));
        ifCondition.setTrueCaseExecutableItem(trueCaseExecutableItem);
        ifCondition.setFalseCaseExecutableItem(falseCaseExecutableItem);
        return ifCondition;
    }
}
