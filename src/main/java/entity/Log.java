package entity;

import java.sql.Timestamp;

public class Log {
    private String level;
    private String action;
    private String message;
    private int customerID;
    private String role;
    private String ipAddress;
    private String extraData;

    private java.sql.Timestamp createdAt;

    public Log(String level, String action, String message, int customerID, String role, String ipAddress, String extraData, java.sql.Timestamp createdAt) {
        this.level = level;
        this.action = action;
        this.message = message;
        this.customerID = customerID;
        this.role = role;
        this.ipAddress = ipAddress;
        this.extraData = extraData;
        this.createdAt = createdAt;
    }

    public Log() {
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getExtraData() {
        return extraData;
    }

    public void setExtraData(String extraData) {
        this.extraData = extraData;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Log{" +
                "level='" + level + '\'' +
                ", action='" + action + '\'' +
                ", message='" + message + '\'' +
                ", customerID=" + customerID +
                ", role='" + role + '\'' +
                ", ipAddress='" + ipAddress + '\'' +
                ", extraData='" + extraData + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
