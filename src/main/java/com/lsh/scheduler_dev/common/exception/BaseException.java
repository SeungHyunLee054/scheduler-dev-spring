package com.lsh.scheduler_dev.common.exception;

public abstract class BaseException extends RuntimeException {
    public abstract Enum<?> getErrorCode();

    public abstract String getErrorMessage();
}
