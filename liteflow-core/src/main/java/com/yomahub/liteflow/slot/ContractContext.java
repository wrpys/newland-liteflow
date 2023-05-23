package com.yomahub.liteflow.slot;

import com.yomahub.liteflow.model.base.Event;

/**
 * 契约上下文
 *
 * @author WRP
 * @since 2023/3/30
 */
public class ContractContext<T extends Event> extends DefaultContext {

    private T data;

    private Class<T> clazz;

    public ContractContext(Class<T> clazz) {
        this.clazz = clazz;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Class<T> getClazz() {
        return clazz;
    }

    public void setClazz(Class<T> clazz) {
        this.clazz = clazz;
    }
}
