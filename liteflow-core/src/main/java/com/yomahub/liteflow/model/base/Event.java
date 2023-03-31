package com.yomahub.liteflow.model.base;

/**
 * @author WRP
 * @since 2023/3/28
 */
public class Event {

    private String eventId;

    private String eventType;

    private String contractId;

    private String funName;

    private String funVersion;

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

    public String getFunName() {
        return funName;
    }

    public void setFunName(String funName) {
        this.funName = funName;
    }

    public String getFunVersion() {
        return funVersion;
    }

    public void setFunVersion(String funVersion) {
        this.funVersion = funVersion;
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
