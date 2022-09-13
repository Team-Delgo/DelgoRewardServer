package com.delgo.reward.comm.log;

import ch.qos.logback.classic.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/*
 * 요청이 들어온 API의 URI와 parameter를 로그로 남김
 */
public class ERRLog {
    private static Logger logger = LoggerFactory.getLogger(ERRLog.class);

    public static void debug(String format, Object... objects) {
        logger.debug(format, objects);
    }

    public static void debug(Exception e) {
        if (logger.isDebugEnabled()) {
            StringBuffer sb = new StringBuffer();
            StackTraceElement[] elemList = e.getStackTrace();
            sb.append(e.getMessage());
            for (int i = 0; i < elemList.length; i++) {
                StackTraceElement elem = elemList[i];
                sb.append("\n\tat " + elem.getClassName() + "." + elem.getMethodName() + " (" + elem.getFileName() + ":" + elem.getLineNumber() + ")");
            }
            logger.debug("{}", sb.toString());
        }
    }

    public static void info(String format, Object... objects) {
        logger.info(format, objects);
    }

    public static void info(HttpServletRequest request, int code, String msg) {
        StringBuffer parameter = new StringBuffer();

        @SuppressWarnings("rawtypes")
        Enumeration enumValue = request.getParameterNames();
        while (enumValue.hasMoreElements()) {
            String name = (String) enumValue.nextElement();

            parameter.append("&" + name + "=");
            String[] valuesList = request.getParameterValues(name);
            if (valuesList.length > 1) {
                for (String value : valuesList) {
                    parameter.append(value + "|");
                }
            } else {
                parameter.append(valuesList[0]);
            }

        }
        logger.info("{} || Parameter : {} || Result : code = {} msg = {}", request.getRequestURI(), parameter.toString(), code, msg);
    }

    public static void warn(String format, Object... objects) {
        logger.warn(format, objects);
    }

    public static void error(String format, Object... objects) {
        logger.error(format, objects);
    }

    public static void error(Exception e) {
        if (logger.isErrorEnabled()) {
            StringBuffer sb = new StringBuffer();
            StackTraceElement[] elemList = e.getStackTrace();
            sb.append(e.getMessage());
            for (int i = 0; i < elemList.length; i++) {
                StackTraceElement elem = elemList[i];
                sb.append("\n\tat " + elem.getClassName() + "." + elem.getMethodName() + " (" + elem.getFileName() + ":" + elem.getLineNumber() + ")");
            }
            logger.error("{}", sb.toString());
        }
    }

    public static void trace(String format, Object... objects) {
        logger.trace(format, objects);
    }

    public static void trace(Exception e) {
        if (logger.isTraceEnabled()) {
            StringBuffer sb = new StringBuffer();
            StackTraceElement[] elemList = e.getStackTrace();
            sb.append(e.getMessage());
            for (int i = 0; i < elemList.length; i++) {
                StackTraceElement elem = elemList[i];
                sb.append("\n\tat " + elem.getClassName() + "." + elem.getMethodName() + " (" + elem.getFileName() + ":" + elem.getLineNumber() + ")");
            }
            logger.trace("{}", sb.toString());
        }
    }

    public static void setLoggerLevel(Level level) {
        ch.qos.logback.classic.Logger currentLogger = (ch.qos.logback.classic.Logger) logger;
        currentLogger.setLevel(level);
    }
}
