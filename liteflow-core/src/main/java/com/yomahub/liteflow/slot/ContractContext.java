package com.yomahub.liteflow.slot;

import com.yomahub.liteflow.model.base.Event;

/**
 * 契约上下文
 *
 * @author WRP
 * @since 2023/3/30
 */
public class ContractContext<T extends Event> extends DefaultContext {

    private T input;

    private T output;



    public T getInput() {
        return input;
    }

    public void setInput(T input) {
        this.input = input;
    }

    public T getOutput() {
        return output;
    }

    public void setOutput(T output) {
        this.output = output;
    }
}
