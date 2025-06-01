package util;

import dao.LogDAO;
import entity.Log;

public class LogUtil {

    public static void log(String level, String action, String message,
                           Integer customerID, String role, String ip, String extraData) {
        Log log = new Log();
        log.setLevel(level);
        log.setAction(action);
        log.setMessage(message);
        log.setCustomerID(customerID);
        log.setRole(role != null ? role : "guest");
        log.setIpAddress(ip != null ? ip : "unknown");
        log.setExtraData(extraData != null ? extraData : "{}");

        new LogDAO().insertLog(log);
    }

    public static void info(String action, String message, Integer customerID, String role, String ip) {
        log("INFO", action, message, customerID, role, ip, null);
    }

    public static void error(String action, String message, Integer customerID, String role, String ip, String extra) {
        log("ERROR", action, message, customerID, role, ip, extra);
    }

    public static void warn(String action, String message, Integer customerID, String role, String ip) {
        log("WARN", action, message, customerID, role, ip, null);
    }

    public static void debug(String action, String message, Integer customerID, String role, String ip) {
        log("DEBUG", action, message, customerID, role, ip, null);
    }
}
