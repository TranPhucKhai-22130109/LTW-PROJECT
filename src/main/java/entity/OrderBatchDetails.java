package entity;

public class OrderBatchDetails {
    private int orderID;
    private int batchID;
    private int quantity;
    private int productID; // join bảng

    public OrderBatchDetails(int orderID, int batchID, int quantity, int productID) {
        this.orderID = orderID;
        this.batchID = batchID;
        this.quantity = quantity;
        this.productID = productID;
    }

    public OrderBatchDetails() {
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public int getBatchID() {
        return batchID;
    }

    public void setBatchID(int batchID) {
        this.batchID = batchID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }
}
