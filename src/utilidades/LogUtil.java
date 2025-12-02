package utilidades;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogUtil {
    private static final Logger LOG = LoggerFactory.getLogger("app");

    public static void info(String msg) { LOG.info(msg); }
    public static void warn(String msg) { LOG.warn(msg); }
    public static void error(String msg, Throwable t) { LOG.error(msg, t); }
    public static void debug(String msg) { LOG.debug(msg); }
}

