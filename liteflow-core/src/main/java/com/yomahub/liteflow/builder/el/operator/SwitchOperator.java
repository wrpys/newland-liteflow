//package com.yomahub.liteflow.builder.el.operator;
//
//import cn.hutool.core.collection.ListUtil;
//import com.ql.util.express.exception.QLException;
//import com.yomahub.liteflow.builder.el.operator.base.BaseOperator;
//import com.yomahub.liteflow.builder.el.operator.base.OperatorHelper;
//import com.yomahub.liteflow.enums.NodeTypeEnum;
//import com.yomahub.liteflow.flow.element.Node;
//import com.yomahub.liteflow.flow.element.condition.SwitchCondition;
//
///**
// * EL规则中的SWITCH的操作符
// *
// * @author Bryan.Zhang
// * @since 2.8.0
// */
//public class SwitchOperator extends BaseOperator<SwitchCondition> {
//
//	@Override
//	public SwitchCondition build(Object[] objects) throws Exception {
//		OperatorHelper.checkObjectSizeNeqOne(objects);
//
//		Node switchNode = OperatorHelper.convert(objects[0], Node.class);
////		if (!ListUtil.toList(NodeTypeEnum.SWITCH, NodeTypeEnum.SWITCH_SCRIPT).contains(switchNode.getType())) {
//		if (!ListUtil.toList(NodeTypeEnum.SWITCH).contains(switchNode.getType())) {
//			throw new QLException("The caller must be Switch item");
//		}
//
//		SwitchCondition switchCondition = new SwitchCondition();
//		switchCondition.setSwitchNode(switchNode);
//
//		return switchCondition;
//	}
//}
