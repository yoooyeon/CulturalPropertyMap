package com.yooyeon.culturalpropertymap.exception;

public class NotFoundInformation extends RuntimeException {
    public NotFoundInformation() {
        super();
    }

    public NotFoundInformation(String message) {
        super(message);
    }

    public NotFoundInformation(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundInformation(Throwable cause) {
        super(cause);
    }

    protected NotFoundInformation(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

