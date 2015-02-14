package com.evenucleus.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by tomeks on 2014-12-28.
 */
public class UserException extends Exception {
    final Logger logger = LoggerFactory.getLogger(UserException.class);

    public UserException() {
        super();
        logger.debug("UserException()");
    }

    public UserException(String msg) {
        super(msg);
        logger.debug("UserException({})", msg);
    }

    public UserException(Throwable cause)
    {
        super(cause);
        logger.debug("UserException({})", cause);
    }

    public UserException(String message, Throwable cause) {
        super(message, cause);
        logger.debug("UserException({},{})", message, cause);
    }
}
