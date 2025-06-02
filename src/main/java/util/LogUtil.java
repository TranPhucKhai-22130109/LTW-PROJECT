package util;

import dao.LogDAO;
import entity.Log;

public class LogUtil {

    // Hàm parse role từ String sang int
    private static int parseRole(String role) {
        if (role == null) return 0;
        return switch (role.toLowerCase()) {
            case "admin" -> 1;
            case "customer" -> 0;
            default -> 0; // Mặc định là customer nếu không khớp
        };
    }

    public static void log(String level, String action, String message,
                           Integer customerID, int role, String ip, String extraData) {
        Log log = new Log();
        log.setLevel(level);
        log.setAction(action);
        log.setMessage(message);
        log.setCustomerID(customerID);
        log.setRole(String.valueOf(role));
        log.setIpAddress(ip != null ? ip : "unknown");
        log.setExtraData(extraData != null ? extraData : "{}");

        new LogDAO().insertLog(log);
    }

    public static void info(String action, String message, Integer customerID, int role, String ip) {
        log("INFO", action, message, customerID, role, ip, null);
    }

    public static void warn(String action, String message, Integer customerID, int role, String ip) {
        log("WARN", action, message, customerID, role, ip, null);
    }

    public static void error(String action, String message, Integer customerID, int role, String ip, String extra) {
        log("ERROR", action, message, customerID, role, ip, extra);
    }

    public static void debug(String action, String message, Integer customerID, int role, String ip) {
        log("DEBUG", action, message, customerID, role, ip, null);
    }

}
