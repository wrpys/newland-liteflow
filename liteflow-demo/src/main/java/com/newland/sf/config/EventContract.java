package com.newland.sf.config;

/**
 * @author WRP
 * @since 2023/3/14
 */
public class EventContract {

    private String expression;

    private String contractName;

    private String version;

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
