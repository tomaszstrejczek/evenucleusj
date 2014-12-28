package com.evenucleus.client;

/**
 * Created by tomeks on 2014-12-28.
 */
public class UserException extends Exception {
    public UserException() {
        super();
    }

    public UserException(String msg) {
        super(msg);
    }

    public UserException(Throwable cause) {
        super(cause);
    }

    public UserException(String message, Throwable cause) {
        super(message, cause);
    }
}
