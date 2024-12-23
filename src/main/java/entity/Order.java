package entity;

import java.util.Date;

public class Order {
    private int orderID;
    private int cusID;
    private double totalPrice;
    private String status;
    private int quantity;
    private int couponID;
    private Date date;

    public Order(int orderID, int cusID, double totalPrice, String status, int quantity, int couponID, Date date) {
        this.orderID = orderID;
        this.cusID = cusID;
        this.totalPrice = totalPrice;
        this.status = status;
        this.quantity = quantity;
        this.couponID = couponID;
        this.date = date;
    }

    public Order() {

    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public int getCusID() {
        return cusID;
    }

    public void setCusID(int cusID) {
        this.cusID = cusID;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getCouponID() {
        return couponID;
    }

    public void setCouponID(int couponID) {
        this.couponID = couponID;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderID=" + orderID +
                ", cusID=" + cusID +
                ", totalPrice=" + totalPrice +
                ", status='" + status + '\'' +
                ", quantity=" + quantity +
                ", couponID=" + couponID +
                '}';
    }
}
