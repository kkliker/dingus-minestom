package ru.sheep.dingus.api;

import org.slf4j.LoggerFactory;

public class DebugLogger {

    static org.slf4j.Logger logger = LoggerFactory.getLogger(DebugLogger.class);

    public static void debug(String info){
        logger.debug(info);
    }
    public static void info(String info){
        logger.info(info);}
}
