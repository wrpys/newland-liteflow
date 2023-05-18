//package com.yomahub.liteflow.builder.el.operator;
//
//import cn.hutool.core.util.ObjectUtil;
//import cn.hutool.core.util.StrUtil;
//import com.ql.util.express.exception.QLException;
//import com.yomahub.liteflow.builder.el.operator.base.BaseOperator;
//import com.yomahub.liteflow.builder.el.operator.base.OperatorHelper;
//import com.yomahub.liteflow.flow.FlowBus;
//import com.yomahub.liteflow.flow.element.Node;
//import com.yomahub.liteflow.property.LiteflowConfig;
//import com.yomahub.liteflow.property.LiteflowConfigGetter;
//
///**
// * EL规则中的node的操作符
// *
// * @author Bryan.Zhang
// * @since 2.8.3
// */
//public class NodeOperator extends BaseOperator<Node> {
//
//	@Override
//	public Node build(Object[] objects) throws Exception {
//		OperatorHelper.checkObjectSizeNeqOne(objects);
//
//		String nodeId = OperatorHelper.convert(objects[0], String.class);
//
//		if (FlowBus.containNode("TODO", nodeId)) {
//			return FlowBus.getNode("TODO", nodeId);
//		} else {
//			LiteflowConfig liteflowConfig = LiteflowConfigGetter.get();
//			if (StrUtil.isNotBlank(liteflowConfig.getSubstituteCmpClass())) {
//				Node substituteNode = FlowBus.getNodeMap().get("TODO").values().stream().filter(node
//						-> node.getInstance().getClass().getName().equals(liteflowConfig.getSubstituteCmpClass())).findFirst().orElse(null);
//				if (ObjectUtil.isNotNull(substituteNode)) {
//					return substituteNode;
//				} else {
//					String error = StrUtil.format("This node[{}] cannot be found", nodeId);
//					throw new QLException(error);
//				}
//			} else {
//				String error = StrUtil.format("This node[{}] cannot be found, or you can configure an substitute node", nodeId);
//				throw new QLException(error);
//			}
//		}
//	}
//}
