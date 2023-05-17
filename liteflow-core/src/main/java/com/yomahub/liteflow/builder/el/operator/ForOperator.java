//package com.yomahub.liteflow.builder.el.operator;
//
//import cn.hutool.core.collection.ListUtil;
//import cn.hutool.core.util.IdUtil;
//import cn.hutool.core.util.StrUtil;
//import com.ql.util.express.exception.QLException;
//import com.yomahub.liteflow.builder.el.operator.base.BaseOperator;
//import com.yomahub.liteflow.builder.el.operator.base.OperatorHelper;
//import com.yomahub.liteflow.core.NodeComponent;
//import com.yomahub.liteflow.core.NodeForComponent;
//import com.yomahub.liteflow.enums.NodeTypeEnum;
//import com.yomahub.liteflow.flow.element.Node;
//import com.yomahub.liteflow.flow.element.condition.ForCondition;
//
///**
// * EL规则中的FOR的操作符
// *
// * @author Bryan.Zhang
// * @since 2.9.0
// */
//public class ForOperator extends BaseOperator<ForCondition> {
//    @Override
//    public ForCondition build(Object[] objects) throws Exception {
//        OperatorHelper.checkObjectSizeEq(objects, 1);
//
//        Node node;
//        if (objects[0] instanceof Node) {
//            node = OperatorHelper.convert(objects[0], Node.class);
////            if (!ListUtil.toList(NodeTypeEnum.FOR, NodeTypeEnum.FOR_SCRIPT).contains(node.getType())) {
//            if (!ListUtil.toList(NodeTypeEnum.FOR).contains(node.getType())) {
//                throw new QLException("The parameter must be for-node item");
//            }
//        }else if(objects[0] instanceof Integer){
//            Integer forCount = OperatorHelper.convert(objects[0], Integer.class);
//            node = new Node();
//            NodeForComponent nodeForComponent = new NodeForComponent() {
//                @Override
//                public int processFor() {
//                    return forCount;
//                }
//            };
//            nodeForComponent.setSelf(nodeForComponent);
//            nodeForComponent.setNodeId(StrUtil.format("LOOP_{}", forCount));
//            node.setInstance(nodeForComponent);
//        }else{
//            throw new QLException("The parameter must be Node item");
//        }
//
//        ForCondition forCondition = new ForCondition();
//        forCondition.setForNode(node);
//        return forCondition;
//    }
//}
