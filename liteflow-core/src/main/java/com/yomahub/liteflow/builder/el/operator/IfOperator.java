package com.yomahub.liteflow.builder.el.operator;

import cn.hutool.core.util.StrUtil;
import com.yomahub.liteflow.builder.el.operator.base.BaseOperator;
import com.yomahub.liteflow.builder.el.operator.base.OperatorHelper;
import com.yomahub.liteflow.exception.ELParseException;
import com.yomahub.liteflow.flow.FlowBus;
import com.yomahub.liteflow.flow.element.Executable;
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
        OperatorHelper.checkObjectSizeEq(objects, 2);

        //解析第一个参数
        final String expr = OperatorHelper.convert(objects[0], String.class);
        if (!expr.startsWith("data")) {
            LOGGER.error("Spring EL表达式[{}]有误，使用不存在的变量！", expr);
            throw new ELParseException("Spring EL表达式[" + expr + "]有误，使用不存在的变量！");
        }

        IfCondition ifCondition = new IfCondition();
        ifCondition.setId(StrUtil.format("IF('{}')", expr));
        ifCondition.setRunId(FlowBus.getRunId(this.getContractId()));
        ifCondition.setExpr(expr);

        //解析第二个参数
        Executable trueCaseExecutableItem = buildExecutable(objects[1]);

        ifCondition.setTrueCaseExecutableItem(trueCaseExecutableItem);
        return ifCondition;
    }
}
