package com.yomahub.liteflow.model.base;

/**
 * 流程执行参数
 *
 * @author WRP
 * @since 2023/5/23
 */
public class FlowParam {

    private String requestId;

    private String chainId;

    private String preRunId;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
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
}