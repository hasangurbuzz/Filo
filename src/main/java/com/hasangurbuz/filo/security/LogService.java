package com.hasangurbuz.filo.security;

import com.hasangurbuz.filo.api.ApiException;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LogService {
    @Autowired
    private Logger logger;

    public void E(String message) {
        logger.error(message);
    }

    public void E(String tag, String message) {
        logger.error(tag + " : " + message);
    }

    public void E(ApiException ex) {
        logger.error(ex.getCode(), ex);
    }

    public void D(String message) {
        logger.debug(message);
    }

    public void D(String tag, String message) {
        logger.debug(tag + " : " + message);
    }
}
