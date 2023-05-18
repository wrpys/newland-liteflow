package com.yomahub.liteflow.builder.prop;

/**
 * 构建 fun 的中间属性
 */
public class FunPropBean {

    String contractId;

    /**
     * id
     */
    String id;

    /**
     * 名称
     */
    String name;

    /**
     * 版本
     */
    String version;

    /**
     * 类型
     */
    String type;

    public String getContractId() {
        return contractId;
    }

    public FunPropBean setContractId(String contractId) {
        this.contractId = contractId;
        return this;
    }

    public String getId() {
        return id;
    }

    public FunPropBean setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public FunPropBean setName(String name) {
        this.name = name;
        return this;
    }

    public String getVersion() {
        return version;
    }

    public FunPropBean setVersion(String version) {
        this.version = version;
        return this;
    }

    public String getType() {
        return type;
    }

    public FunPropBean setType(String type) {
        this.type = type;
        return this;
    }
}
