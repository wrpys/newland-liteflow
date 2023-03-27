package com.yomahub.liteflow.exception;

/**
 * 节点不为真异常
 * @author Yun
 */
public class NoIfTrueNodeException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 异常信息
     */
    private String message;

    public NoIfTrueNodeException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
