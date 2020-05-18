package com.accommodation.system.logs;

import com.accommodation.system.utils2.Utils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WriteLog {
    private static Logger logger = LogManager.getLogger(WriteLog.class);

    public static void info(Object... objects) {
        logger.info(Utils.concatString(objects));
    }

    public static void info(String referenceId, String uri, Object... objects) {
        logger.info(String.format("[%s][%s]", Utils.nullToEmpty(referenceId), Utils.trimWithMask(uri,35)),Utils.concatString(objects));
    }

    public static void debug(String referenceId, String uri, Object... objects) {
        logger.debug(String.format("[%s][%s]", Utils.nullToEmpty(referenceId), Utils.trimWithMask(uri,35)),Utils.concatString(objects));
    }

    public static void error(Throwable throwable) {
        logger.error(throwable);
    }

    public static void error(String referenceId, String uri, Throwable throwable) {
        logger.error(String.format("[%s][%s]", Utils.nullToEmpty(referenceId), Utils.trimWithMask(uri,35)),throwable);
    }
}
