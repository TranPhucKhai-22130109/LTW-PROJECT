package dao;

import context.JDBIContext;
import entity.Log;
import java.io.Serializable;

public class LogDAO implements Serializable {
    public int insertLog(Log log) {
        if (log == null || log.getLevel() == null || log.getAction() == null || log.getMessage() == null) {
            throw new IllegalArgumentException("Log object or required fields (level, action, message) cannot be null");
        }
        try {
            return JDBIContext.getJdbi().withHandle(handle ->
                    handle.createUpdate("INSERT INTO logs (level, action, message, customerID, role, ip_address, extra_data, created_at) " +
                                    "VALUES (:level, :action, :message, :customerID, :role, :ip, :extraData, NOW())")
                            .bind("level", log.getLevel())
                            .bind("action", log.getAction())
                            .bind("message", log.getMessage())
                            .bind("customerID", log.getCustomerID())
                            .bind("role", log.getRole())
                            .bind("ip", log.getIpAddress())
                            .bind("extraData", log.getExtraData() != null ? log.getExtraData() : "{}")
                            .execute()
            );
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error inserting log: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        LogDAO dao = new LogDAO();
        Log log = new Log();
        log.setLevel("INFO");
        log.setAction("TEST_INSERT");
        log.setMessage("Testing insert log");
        log.setCustomerID(19);
        log.setRole("1");
        log.setIpAddress("127.0.0.1");
        log.setExtraData("{\"error\":\"Invalid password\"}");
        try {
            int result = dao.insertLog(log);
            System.out.println("Insert result: " + result);
        } catch (Exception e) {
            System.out.println("Test failed: " + e.getMessage());
        }
    }
}