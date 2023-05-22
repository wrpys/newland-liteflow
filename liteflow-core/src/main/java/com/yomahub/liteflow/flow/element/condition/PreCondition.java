///**
// * <p>Title: liteflow</p>
// * <p>Description: 轻量级的组件式流程框架</p>
// * @author Bryan.Zhang
// * @email weenyc31@163.com
// * @Date 2020/4/1
// */
//package com.yomahub.liteflow.flow.element.condition;
//
//import com.yomahub.liteflow.enums.CmpStepTypeEnum;
//import com.yomahub.liteflow.enums.ConditionTypeEnum;
//import com.yomahub.liteflow.flow.element.Executable;
//import com.yomahub.liteflow.flow.entity.CmpStep;
//import com.yomahub.liteflow.slot.DataBus;
//import com.yomahub.liteflow.slot.Slot;
//
///**
// * 前置Condition
// * @author Bryan.Zhang
// * @since 2.6.4
// */
//public class PreCondition extends Condition {
//
//	@Override
//	public void execute(Integer slotIndex) throws Exception {
//		//在元数据里加入step信息
//		Slot slot = DataBus.getSlot(slotIndex);
//		CmpStep cmpStep = new CmpStep(this.getId(), this.getId(), this.getRunId(), CmpStepTypeEnum.SINGLE);
//		slot.addStep(cmpStep);
//
//		for(Executable executableItem : this.getExecutableList()){
//			executableItem.setCurrChainId(this.getCurrChainId());
//			executableItem.execute(slotIndex);
//		}
//	}
//
//	@Override
//	public ConditionTypeEnum getConditionType() {
//		return ConditionTypeEnum.TYPE_PRE;
//	}
//}
