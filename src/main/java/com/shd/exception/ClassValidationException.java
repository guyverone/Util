package com.shd.exception;

/**
 * Created by suhd on 2016-09-01.
 */
public class ClassValidationException extends Exception {

    /**
     * @param
     */
    public ClassValidationException() {
        super();
    }

    /**
     *
     * @param message
     */
    public ClassValidationException(String message) {
        super(message);
    }
}
