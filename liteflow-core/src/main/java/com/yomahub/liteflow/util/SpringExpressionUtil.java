package com.yomahub.liteflow.util;

import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * SpringEL解析器
 *
 * @author WRP
 * @since 2023/3/29
 */
public class SpringExpressionUtil {

    private SpringExpressionUtil() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 表达式是否成立
     *
     * @param expr
     * @param data
     * @return
     */
    public static boolean parseExpression(String expr, Object data) {
        Boolean result = parseExpression(expr, data, Boolean.class);
        return result == null ? false : result.booleanValue();
    }

    /**
     * 根据表达式取值
     *
     * @param expr
     * @param data
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T parseExpression(String expr, Object data, Class<T> clazz) {
        //创建表达式解析器
        ExpressionParser parser = new SpelExpressionParser();
        EvaluationContext context = new StandardEvaluationContext(data);
        //解析表达式，如果表达式是一个模板表达式，需要为解析传入模板解析器上下文。
        Expression expression = parser.parseExpression(expr);
        //使用Expression.getValue()获取表达式的值，这里传入了Evalution上下文，第二个参数是类型参数，表示返回值的类型。
        return expression.getValue(context, clazz);
    }

}
