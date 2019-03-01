package com.lewisallen.rtdptiCache.logging;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ErrorHandler {

    private static Logger logger = Logger.getLogger("ErrorHandler");

    public static void handle(Throwable t, Level level, String message)
    {
        logger.log(level, String.format("%s\n%s", message, t.getMessage()));
    }
}