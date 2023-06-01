package com.yomahub.liteflow.model.base;

import java.io.Serializable;
import java.util.Map;

/**
 * 事件参数
 *
 * @author WRP
 * @since 2023/3/28
 */
public class Event implements Serializable {

    private String eventId;

    private String eventType;

    private String contractId;

    private String chainId;

    private String preRunId;

    private Map<String, Object> stepResultMap;

    private String code;

    private String msg;

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public String getChainId() {
        return chainId;
    }

    public void setChainId(String chainId) {
        this.chainId = chainId;
    }

    public String getPreRunId() {
        return preRunId;
    }

    public void setPreRunId(String preRunId) {
        this.preRunId = preRunId;
    }

    public Map<String, Object> getStepResultMap() {
        return stepResultMap;
    }

    public void setStepResultMap(Map<String, Object> stepResultMap) {
        this.stepResultMap = stepResultMap;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
