/**
 * <p>Title: liteflow</p>
 * <p>Description: 轻量级的组件式流程框架</p>
 *
 * @author Bryan.Zhang
 * @email weenyc31@163.com
 * @Date 2020/4/1
 */
package com.yomahub.liteflow.flow.element;

import cn.hutool.core.collection.CollUtil;
import com.yomahub.liteflow.exception.ChainEndException;
import com.yomahub.liteflow.slot.DataBus;
import com.yomahub.liteflow.slot.Slot;
import com.yomahub.liteflow.enums.ExecuteTypeEnum;
import com.yomahub.liteflow.exception.FlowSystemException;
import com.yomahub.liteflow.flow.element.condition.Condition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.List;

/**
 * chain对象，实现可执行器
 *
 * @author Bryan.Zhang
 */
public class Chain implements Executable {

    private static final Logger LOG = LoggerFactory.getLogger(Chain.class);

    private String chainId;

    private List<Condition> conditionList = new ArrayList<>();

    public Chain(String chainName){
        this.chainId = chainName;
    }

    public Chain(){}

    public Chain(String chainName, List<Condition> conditionList) {
        this.chainId = chainName;
        this.conditionList = conditionList;
    }

    public List<Condition> getConditionList() {
        return conditionList;
    }

    public void setConditionList(List<Condition> conditionList) {
        this.conditionList = conditionList;
    }

    /**
	 * @deprecated 请使用{@link #getChainId()}
	 */
    @Deprecated
    public String getChainName() {
        return chainId;
    }

    /**
     * 
     * @param chainName
     * @deprecated 请使用 {@link #setChainId(String)}
     */
    public void setChainName(String chainName) {
        this.chainId = chainName;
    }
    
    public String getChainId() {
        return chainId;
    }

    public void setChainId(String chainId) {
        this.chainId = chainId;
    }

    //执行chain的主方法
    @Override
    public void execute(Integer slotIndex) throws Exception {
        if (CollUtil.isEmpty(conditionList)) {
            throw new FlowSystemException("no conditionList in this chain[" + chainId + "]");
        }
        Slot slot = DataBus.getSlot(slotIndex);
        try {
            //设置主ChainName
            slot.setChainId(chainId);
            //执行主体Condition
            for (Condition condition : conditionList) {
                condition.setCurrChainId(chainId);
                condition.execute(slotIndex);
            }
        }catch (ChainEndException e){
            //这里单独catch ChainEndException是因为ChainEndException是用户自己setIsEnd抛出的异常
            //是属于正常逻辑，所以会在FlowExecutor中判断。这里不作为异常处理
            throw e;
        }catch (Exception e){
            //这里事先取到exception set到slot里，为了方便finally取到exception
            if (slot.isSubChain(chainId)){
                slot.setSubException(chainId, e);
            }else{
                slot.setException(e);
            }
            throw e;
        }
    }

    @Override
    public ExecuteTypeEnum getExecuteType() {
        return ExecuteTypeEnum.CHAIN;
    }

    @Override
    public String getExecuteId() {
        return chainId;
    }
}
