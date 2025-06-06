package dao;

import entity.InventoryTransaction;
import context.JDBIContext;

import java.util.List;
import java.util.Optional;

public class InventoryTransactionDAO {

    // Lấy tất cả giao dịch kho
    public List<InventoryTransaction> getAllTransactions() {
        return JDBIContext.getJdbi().withHandle(handle ->
                handle.createQuery("""
                                SELECT t.*, p.productName, u.username AS createdByName
                                FROM inventory_transactions t
                                JOIN products p ON t.productID = p.productID
                                LEFT JOIN users u ON t.createdBy = u.userID
                                ORDER BY t.createdAt DESC
                                """)
                        .mapToBean(InventoryTransaction.class)
                        .list()
        );
    }

    // Lấy giao dịch theo ID
    public Optional<InventoryTransaction> getTransactionByID(int transactionID) {
        return JDBIContext.getJdbi().withHandle(handle ->
                handle.createQuery("""
                                SELECT t.*, p.productName, u.username AS createdByName
                                FROM inventory_transactions t
                                JOIN products p ON t.productID = p.productID
                                LEFT JOIN users u ON t.createdBy = u.userID
                                WHERE t.transactionID = :transactionID
                                """)
                        .bind("transactionID", transactionID)
                        .mapToBean(InventoryTransaction.class)
                        .findOne()
        );
    }

    // Lấy transaction theo id lô
    public Optional<InventoryTransaction> getTransactionByBatchID(String batchID) {
        return JDBIContext.getJdbi().withHandle(handle ->
                handle.createQuery("""
                                 SELECT *
                                FROM inventory_transactions t
                                 WHERE t.batchID = :batchID
                                """)
                        .bind("batchID", batchID)
                        .mapToBean(InventoryTransaction.class)
                        .findOne()
        );
    }

    // Thêm giao dịch mới
    public boolean addTransaction(InventoryTransaction transaction) {
        int rowsAffected = JDBIContext.getJdbi().withHandle(handle ->
                handle.createUpdate("""
                                INSERT INTO inventory_transactions (productID, batchID, transactionType, quantity, orderDetailID, description, createdBy)
                                VALUES (:productID, :batchID, :transactionType, :quantity, :orderDetailID, :description, :createdBy)
                                """)
                        .bind("productID", transaction.getProductID())
                        .bind("batchID", transaction.getBatchID())
                        .bind("transactionType", transaction.getTransactionType())
                        .bind("quantity", transaction.getQuantity())
                        .bind("orderDetailID", transaction.getOrderDetailID())
                        .bind("description", transaction.getDescription())
                        .bind("createdBy", transaction.getCreatedBy())
                        .execute()
        );
        return rowsAffected > 0;
    }

    // Cập nhật giao dịch (CẦN XEM LẠI)
    public boolean updateTransaction(InventoryTransaction transaction) {
        int rowsAffected = JDBIContext.getJdbi().withHandle(handle ->
                handle.createUpdate("""
                                UPDATE inventory_transactions
                                SET productID = :productID, transactionType = :transactionType, quantity = :quantity,
                                    orderDetailID = :orderDetailID, description = :description, createdBy = :createdBy
                                WHERE transactionID = :transactionID
                                """)
                        .bind("transactionID", transaction.getTransactionID())
                        .bind("productID", transaction.getProductID())
                        .bind("transactionType", transaction.getTransactionType())
                        .bind("quantity", transaction.getQuantity())
                        .bind("orderDetailID", transaction.getOrderDetailID())
                        .bind("description", transaction.getDescription())
                        .bind("createdBy", transaction.getCreatedBy())
                        .execute()
        );
        return rowsAffected > 0;
    }

}