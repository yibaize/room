package org.bql.utils.logger;


import java.util.concurrent.ConcurrentHashMap;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * 作者： 白泽
 * 时间： 2017/11/15.
 * 描述：
 */
public class LoggerUtils {
    private static Logger platformLog = null;
    private static Logger logicLog = null;
    private static Logger debugAll = null;
    private static final ConcurrentHashMap<String, Logger> logMap = new ConcurrentHashMap();

    public LoggerUtils() {
    }

    public static Logger getPlatformLog() {
        return platformLog;
    }

    public static Logger getLogicLog() {
        return logicLog;
    }

    public static Logger getLogByName(String logName) {
        Logger _logger = (Logger)logMap.get(logName);
        if (_logger == null) {
            _logger = LogManager.getLogger(logName);
            logMap.put(logName, _logger);
        }

        return _logger;
    }

    public static void openLog(Logger log) {
        log.setAdditivity(true);
    }

    public static void closeLog(Logger log) {
        log.setAdditivity(false);
    }

    public static void setDebugStatus(Logger log) {
        log.setLevel(Level.DEBUG);
    }

    public static void setOperateStatus(Logger log) {
        log.setLevel(Level.ERROR);
    }


    static {
        PropertyConfigurator.configure(Thread.currentThread().getContextClassLoader().getResourceAsStream("log4j.properties"));
        platformLog = LogManager.getLogger("platformlogs");
        logicLog = LogManager.getLogger("logiclogs");
        debugAll = LogManager.getLogger("debugalls");
    }

    public static void main(String[] args) {
        LoggerUtils.getLogicLog().debug("asdasdas");
    }
}
