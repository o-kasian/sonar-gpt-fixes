
package com.epam.java.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Logging {

    private static final Logger LOGGER = LoggerFactory.getLogger(Logging.class);
    private static final String PREFIX = " >> ";

    private Logging() {}

    public static Logger getLogger() {
        return LOGGER;
    }

    public static class Logger {

        private final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(Logger.class);

        public void info(String message) {
            LOGGER.info(PREFIX + message);
        }

        public void error(Throwable e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

}
