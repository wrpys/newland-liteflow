package com.newland.sf.model;

/**
 * @author WRP
 * @since 2023/3/15
 * {"type":"event","funcName":"chachong","version":"1.0.0"}
 */
public class FuncParam {

    private String type;

    private String funcName;

    private String version;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFuncName() {
        return funcName;
    }

    public void setFuncName(String funcName) {
        this.funcName = funcName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
