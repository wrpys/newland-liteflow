package com.yomahub.liteflow.builder.el.operator.base;

import com.ql.util.express.ArraySwap;
import com.ql.util.express.ExpressUtil;
import com.ql.util.express.IExpressContext;
import com.ql.util.express.InstructionSetContext;
import com.ql.util.express.OperateData;
import com.ql.util.express.config.QLExpressRunStrategy;
import com.ql.util.express.exception.QLException;
import com.ql.util.express.instruction.OperateDataCacheManager;
import com.ql.util.express.instruction.op.OperatorBase;
import com.yomahub.liteflow.exception.ELParseException;
import com.yomahub.liteflow.flow.element.Executable;

/**
 * BaseOperator 为了强化 executeInner 方法，会捕获抛出的 QLException 错误，输出友好的错误提示
 *
 * @author gaibu
 * @since 2.8.6
 */
public abstract class BaseOperator<T extends Executable> extends OperatorBase {

    private IExpressContext<String, Object> context;

    @Override
    public OperateData executeInner(InstructionSetContext parent, ArraySwap list) throws Exception {
        context = parent.getParent();
        Object[] parameters = new Object[list.length];
        for (int i = 0; i < list.length; i++) {
            if (list.get(i) == null && QLExpressRunStrategy.isAvoidNullPointer()) {
                parameters[i] = null;
            } else {
                parameters[i] = list.get(i).getObject(parent);
            }
        }
        Object result = this.executeInner(parameters);
        if (result != null && result.getClass().equals(OperateData.class)) {
            throw new QLException("操作符号定义的返回类型错误：" + this.getAliasName());
        }
        if (result == null) {
            //return new OperateData(null,null);
            return OperateDataCacheManager.fetchOperateData(null, null);
        } else {
            //return new OperateData(result,ExpressUtil.getSimpleDataType(result.getClass()));
            return OperateDataCacheManager.fetchOperateData(result, ExpressUtil.getSimpleDataType(result.getClass()));
        }
    }

    public T executeInner(Object[] objects) throws Exception {
        try {
            // 检查 node 和 chain 是否已经注册
            OperatorHelper.checkNodeAndChainExist(objects);
            return build(objects);
        } catch (QLException e) {
            throw e;
        } catch (Exception e) {
            throw new ELParseException("errors occurred in EL parsing");
        }
    }

    public IExpressContext<String, Object> getContext() {
        return context;
    }

    public String getContractId() {
        return (String) context.get("CONTRACT_ID");
    }

    /**
     * 构建 EL 条件
     *
     * @param objects objects
     * @return Condition
     * @throws Exception Exception
     */
    public abstract T build(Object[] objects) throws Exception;
}
