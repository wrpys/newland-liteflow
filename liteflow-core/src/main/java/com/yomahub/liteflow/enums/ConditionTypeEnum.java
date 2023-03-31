package com.yomahub.liteflow.enums;

/**
 * @author Yun
 */
public enum ConditionTypeEnum {
    TYPE_THEN("then","then"),
    TYPE_WHEN("when","when"),
    TYPE_SWITCH("switch", "switch"),

    TYPE_IF("if", "if"),

    TYPE_INVOKE("invoke", "invoke"),

    TYPE_END("end", "end"),

    TYPE_PRE("pre","pre"),
    TYPE_FINALLY("finally","finally"),

    TYPE_FOR("for", "for"),

    TYPE_WHILE("while", "while"),

    TYPE_ITERATOR("iterator", "iterator")
    ;
    private String type;
    private String name;

    ConditionTypeEnum(String type, String name) {
        this.type = type;
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static ConditionTypeEnum getEnumByCode(String code) {
        for (ConditionTypeEnum e : ConditionTypeEnum.values()) {
            if (e.getType().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
