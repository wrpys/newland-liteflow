package com.yomahub.liteflow.flow.element.condition;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.yomahub.liteflow.common.ChainConstant;
import com.yomahub.liteflow.enums.CmpStepTypeEnum;
import com.yomahub.liteflow.enums.ConditionTypeEnum;
import com.yomahub.liteflow.exception.NoIfTrueNodeException;
import com.yomahub.liteflow.flow.FlowContent;
import com.yomahub.liteflow.flow.element.Executable;
import com.yomahub.liteflow.flow.entity.CmpStep;
import com.yomahub.liteflow.slot.ContractContext;
import com.yomahub.liteflow.slot.DataBus;
import com.yomahub.liteflow.slot.Slot;
import com.yomahub.liteflow.util.SpringExpressionUtil;

/**
 * 条件Condition
 *
 * @author Bryan.Zhang
 * @since 2.8.5
 */
public class IfCondition extends Condition {

    private String expr;

    private Executable trueCaseExecutableItem;

    private Executable falseCaseExecutableItem;

    @Override
    public void execute(Integer slotIndex) throws Exception {
        Slot slot = DataBus.getSlot(slotIndex);

        boolean ifResult;
        if (slot.isSkip()) {
            Object result = FlowContent.getStepResult(slot.getRequestId(), this.getRunId());
            ifResult = (Boolean) result;
        } else {
            //在元数据里加入step信息
            CmpStep cmpStep = new CmpStep(this.getId(), null, this.getRunId(), CmpStepTypeEnum.SINGLE);
            slot.addStep(cmpStep);

            //判断条件
            ContractContext context = slot.getContextBean(ContractContext.class);
            ifResult = SpringExpressionUtil.parseExpression(expr, context);

            // 保存执行结果
            FlowContent.addStepResult(slot.getRequestId(), this.getRunId(), ifResult);

            cmpStep.setNodeId(cmpStep.getNodeId() + "==" + ifResult);
        }

        if (ifResult) {
            //trueCaseExecutableItem这个不能为空，否则执行什么呢
            if (ObjectUtil.isNull(trueCaseExecutableItem)) {
                String errorInfo = StrUtil.format("[{}]:no if-true node found for the component[{}]", slot.getRequestId(), trueCaseExecutableItem.getExecuteId());
                throw new NoIfTrueNodeException(errorInfo);
            }

            //执行trueCaseExecutableItem
            trueCaseExecutableItem.setCurrChainName(this.getCurrChainName());
            trueCaseExecutableItem.execute(slotIndex);

            if (slot.isSkip()) {
                if (slot.getPreRunId().equals(trueCaseExecutableItem.getRunId())) {
                    slot.setIsSkip(false);
                }
            }

        } else {
            //falseCaseExecutableItem可以为null，但是不为null时就执行否的情况
            if (ObjectUtil.isNotNull(falseCaseExecutableItem)) {
                //执行falseCaseExecutableItem
                falseCaseExecutableItem.setCurrChainId(this.getCurrChainId());

                //添加 ELSE 步骤信息
                if (!(falseCaseExecutableItem instanceof IfCondition)) {
                    //在元数据里加入step信息
                    CmpStep cmpStep = new CmpStep(ChainConstant.ELSE, ChainConstant.ELSE, ChainConstant.ELSE, CmpStepTypeEnum.SINGLE);
                    slot.addStep(cmpStep);
                }

                falseCaseExecutableItem.execute(slotIndex);

                if (slot.isSkip()) {
                    if (slot.getPreRunId().equals(trueCaseExecutableItem.getRunId())) {
                        slot.setIsSkip(false);
                    }
                }

            }
        }
    }

    @Override
    public ConditionTypeEnum getConditionType() {
        return ConditionTypeEnum.TYPE_IF;
    }

    public Executable getTrueCaseExecutableItem() {
        return trueCaseExecutableItem;
    }

    public void setTrueCaseExecutableItem(Executable trueCaseExecutableItem) {
        this.trueCaseExecutableItem = trueCaseExecutableItem;
    }

    public Executable getFalseCaseExecutableItem() {
        return falseCaseExecutableItem;
    }

    public void setFalseCaseExecutableItem(Executable falseCaseExecutableItem) {
        this.falseCaseExecutableItem = falseCaseExecutableItem;
    }

    public String getExpr() {
        return expr;
    }

    public void setExpr(String expr) {
        this.expr = expr;
    }

}
