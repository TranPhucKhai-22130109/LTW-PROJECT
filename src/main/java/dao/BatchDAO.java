package dao;

import entity.Batch;
import context.JDBIContext;
import entity.OrderBatchDetails;
import org.jdbi.v3.core.Handle;

import java.util.*;
import java.util.stream.Collectors;

public class BatchDAO {

    // Lấy tất cả lô hàng
    public List<Batch> getAllBatches() {
        return JDBIContext.getJdbi().withHandle(handle ->
                handle.createQuery("""
                                SELECT b.*, p.productName, s.supplierName
                                FROM batches b
                                JOIN products p ON b.productID = p.productID
                                LEFT JOIN suppliers s ON b.supplierID = s.supplierID
                                WHERE b.isDeleted = 0
                                ORDER BY b.createdAt DESC
                                """)
                        .mapToBean(Batch.class)
                        .list()
        );

    }

    // Lấy lô hàng theo ID
    public Optional<Batch> getBatchByID(int batchID) {
        return JDBIContext.getJdbi().withHandle(handle ->
                handle.createQuery("""
                                SELECT b.*, p.productName, s.supplierName
                                FROM batches b
                                JOIN products p ON b.productID = p.productID
                                LEFT JOIN suppliers s ON b.supplierID = s.supplierID
                                WHERE b.batchID = :batchID
                                """)
                        .bind("batchID", batchID)
                        .mapToBean(Batch.class)
                        .findOne()
        );
    }

    // Lấy lô hàng bị lưu trũ (isDeleted=0)
    public List<Batch> getArchiveBatch() {
        return JDBIContext.getJdbi().withHandle(handle ->
                handle.createQuery("""
                                SELECT b.*, p.productName
                                FROM batches b
                                JOIN products p ON b.productID = p.productID
                                WHERE b.isDeleted = 1
                                """)
                        .mapToBean(Batch.class)
                        .list()
        );
    }

    // Thêm lô hàng mới
    public boolean addBatch(Batch batch) {
        int rowsAffected = JDBIContext.getJdbi().withHandle(handle ->
                handle.createUpdate("""
                                INSERT INTO batches (productID, batchNumber, quantity, price, importDate, supplierID, isDeleted, isUsed)
                                VALUES (:productID, :batchNumber, :quantity, :price, :importDate, :supplierID, :isDeleted, :isUsed)
                                """)
                        .bind("productID", batch.getProductID())
                        .bind("batchNumber", batch.getBatchNumber())
                        .bind("quantity", batch.getQuantity())
                        .bind("price", batch.getPrice())
                        .bind("importDate", batch.getImportDate())
                        .bind("supplierID", batch.getSupplierID())
                        .bind("isDeleted", batch.getIsDeleted())
                        .bind("isUsed", batch.getIsUsed())
                        .execute()
        );
        syncInventoryForProductIDs(JDBIContext.getJdbi().open(), List.of(batch.getProductID()));
        return rowsAffected > 0;
    }

    // Update batches
    public boolean updateBatch(Batch batch) {
        return JDBIContext.getJdbi().withHandle(handle -> {
            // Bắt đầu transaction
            handle.begin();
            try {
                // Bước 1: Lấy productID cũ trước khi cập nhật
                Integer oldProductID = handle.createQuery("SELECT productID FROM batches WHERE batchID = :batchID")
                        .bind("batchID", batch.getBatchID())
                        .mapTo(Integer.class)
                        .findOne()
                        .orElse(null);

                if (oldProductID == null) {
                    handle.rollback(); // Rollback nếu batch không tồn tại
                    return false;
                }

                // Bước 2: Cập nhật batch trong bảng batches
                int rowsAffected = handle.createUpdate("""
                                UPDATE batches
                                SET productID = :productID, quantity = :quantity, price = :price, supplierID = :supplierID
                                WHERE batchID = :batchID
                                """)
                        .bind("batchID", batch.getBatchID())
                        .bind("productID", batch.getProductID())
                        .bind("quantity", batch.getQuantity())
                        .bind("price", batch.getPrice())
                        .bind("supplierID", batch.getSupplierID())
                        .execute();

                // Bước 3: Xác định các productID bị ảnh hưởng
                List<Integer> affectedProductIDs = new ArrayList<>();
                affectedProductIDs.add(oldProductID); // productID cũ
                affectedProductIDs.add(batch.getProductID()); // productID mới

                // Loại bỏ trùng lặp
                affectedProductIDs = affectedProductIDs.stream().distinct().collect(Collectors.toList());

                // Bước 4: Đồng bộ inventory cho các productID bị ảnh hưởng
                syncInventoryForProductIDs(handle, affectedProductIDs);

                // Commit transaction nếu thành công
                handle.commit();
                return rowsAffected > 0;

            } catch (Exception e) {
                // Rollback nếu có lỗi
                handle.rollback();
                return false;
            }
        });
    }

    // Soft Delete
    public boolean softDeleteBatch(String batchId) {
        return JDBIContext.getJdbi().withHandle(handle -> {
            handle.begin();

            try {
                // Bước 1: Lấy productID của lô cần soft delete
                Integer productID = handle.createQuery("SELECT productID FROM batches WHERE batchID = :batchID")
                        .bind("batchID", batchId)
                        .mapTo(Integer.class)
                        .findOne()
                        .orElse(null);

                if (productID == null) {
                    handle.rollback();
                    return false; // Batch không tồn tại
                }

                // Bước 2: Thực hiện soft delete
                int rowsAffected = handle.createUpdate("UPDATE batches SET isDeleted = 1 WHERE batchID = :batchID")
                        .bind("batchID", batchId)
                        .execute();

                if (rowsAffected == 0) {
                    handle.rollback();
                    return false; // Không cập nhật được
                }

                // Bước 3: Đồng bộ inventory cho productID bị ảnh hưởng
                syncInventoryForProductIDs(handle, List.of(productID));

                handle.commit();
                return true;

            } catch (Exception e) {
                handle.rollback();
                return false;
            }
        });
    }

    // Un Soft Delete
    public boolean unSoftDeleteBatch(String batchId) {
        return JDBIContext.getJdbi().withHandle(handle -> {
            handle.begin();

            try {
                // Bước 1: Lấy productID của lô cần soft delete
                Integer productID = handle.createQuery("SELECT productID FROM batches WHERE batchID = :batchID")
                        .bind("batchID", batchId)
                        .mapTo(Integer.class)
                        .findOne()
                        .orElse(null);

                if (productID == null) {
                    handle.rollback();
                    return false; // Batch không tồn tại
                }

                // Bước 2: Thực hiện unSoft delete
                int rowsAffected = handle.createUpdate("UPDATE batches SET isDeleted = 0 WHERE batchID = :batchID")
                        .bind("batchID", batchId)
                        .execute();

                if (rowsAffected == 0) {
                    handle.rollback();
                    return false; // Không cập nhật được
                }

                // Bước 3: Đồng bộ inventory cho productID bị ảnh hưởng
                syncInventoryForProductIDs(handle, List.of(Integer.valueOf(productID)));

                handle.commit();
                return true;

            } catch (Exception e) {
                handle.rollback();
                return false;
            }
        });
    }

    // update status (isUsed=0 || 1)
    public boolean updateUsedStatus(String batchId, Byte isUsed) {
        int rowsAffected = JDBIContext.getJdbi().withHandle(handle ->
                handle.createUpdate("UPDATE batches SET isUsed = :isUsed WHERE batchID = :batchID")
                        .bind("batchID", batchId)
                        .bind("isUsed", isUsed)
                        .execute()
        );
        return rowsAffected > 0;
    }

    // Sync batches and inventory
    private void syncInventoryForProductIDs(Handle handle, List<Integer> productIDs) {
        if (productIDs.isEmpty()) {
            return;
        }
        handle.createUpdate(
                        "INSERT INTO inventory (productID, quantityInStock, lastUpdated) " +
                                "SELECT productID, COALESCE(SUM(quantity), 0) AS totalQuantity, NOW() " +
                                "FROM batches " +
                                "WHERE isDeleted = 0 AND productID IN (<productIDs>) " +
                                "GROUP BY productID " +
                                "ON DUPLICATE KEY UPDATE " +
                                "quantityInStock = VALUES(quantityInStock), " +
                                "lastUpdated = NOW()"
                )
                .bindList("productIDs", productIDs)
                .execute();
    }

    // get batch by id (FIFO)
    public boolean deductBatchesForOrder(String orderID, int productID, int orderQuantity) {
        return JDBIContext.getJdbi().withHandle(handle -> {
            handle.begin();

            try {
                // 1. Lấy danh sách batch theo productID còn tồn kho (FIFO)
                List<Batch> batches = handle.createQuery("""
                                SELECT * FROM batches
                                WHERE productID = :productID AND isDeleted = 0 AND isUsed = 1 AND quantity > 0
                                ORDER BY importDate ASC
                                """)
                        .bind("productID", productID)
                        .mapToBean(Batch.class)
                        .list();


                // Nếu không tìm thấy batch phù hợp
                if (batches.isEmpty()) {
                    handle.rollback();
                    System.err.println("Không tìm thấy lô hàng phù hợp cho sản phẩm ID: " + productID);
                    return false;
                }


                int remainingQty = orderQuantity;

                for (Batch batch : batches) {
                    if (remainingQty <= 0) break;

                    int batchQty = batch.getQuantity();
                    int deductQty = Math.min(batchQty, remainingQty);
                    int newQty = batchQty - deductQty;

                    // 2. Cập nhật số lượng lô
                    handle.createUpdate("""
                                    UPDATE batches
                                    SET quantity = :newQty
                                    WHERE batchID = :batchID
                                    """)
                            .bind("newQty", newQty)
                            .bind("batchID", batch.getBatchID())
                            .execute();


                    handle.createUpdate("""
                                        INSERT INTO order_batch_details (orderID, batchID, quantity)
                                        VALUES (:orderID, :batchID, :quantity)
                                    """)
                            .bind("orderID", orderID)
                            .bind("batchID", batch.getBatchID())
                            .bind("quantity", deductQty)
                            .execute();

                    remainingQty -= deductQty;
                }

                if (remainingQty > 0) {
                    handle.rollback();
                    System.out.println("Không đủ hàng tồn cho productID: " + productID);
                    return false;
                }

                // 3. Đồng bộ inventory
                syncInventoryForProductIDs(handle, List.of(productID));

                handle.commit();
                return true;

            } catch (Exception e) {
                handle.rollback();
                e.printStackTrace();
                return false;
            }
        });
    }

    public boolean cancelOrder(String orderID) {
        return JDBIContext.getJdbi().withHandle(handle -> {
            handle.begin();

            try {
                // 1. Lấy thông tin các batch đã bị trừ trong đơn hàng
                List<OrderBatchDetails> batchDetails = handle.createQuery("""
                                    SELECT obd.batchID, obd.quantity, b.productID
                                    FROM order_batch_details obd
                                    JOIN batches b ON obd.batchID = b.batchID
                                    WHERE obd.orderID = :orderID
                                """)
                        .bind("orderID", orderID)
                        .mapToBean(OrderBatchDetails.class)
                        .list();

                if (batchDetails.isEmpty()) {
                    handle.rollback();
                    System.out.println("Không tìm thấy chi tiết đơn hàng để hủy.");
                    return false;
                }

                Set<Integer> affectedProductIDs = new HashSet<>();

                for (OrderBatchDetails row : batchDetails) {
                    int batchID = row.getBatchID();
                    int quantity = row.getQuantity();
                    int productID = row.getProductID();

                    // Cập nhật lại số lượng cho batch
                    handle.createUpdate("""
                                        UPDATE batches
                                        SET quantity = quantity + :quantity
                                        WHERE batchID = :batchID
                                    """)
                            .bind("quantity", quantity)
                            .bind("batchID", batchID)
                            .execute();

                    affectedProductIDs.add(productID);
                }

                // 2. Xoá dữ liệu trong bảng chi tiết đơn hàng (nếu không muốn giữ lại)
                handle.createUpdate("""
                                    DELETE FROM order_batch_details
                                    WHERE orderID = :orderID
                                """)
                        .bind("orderID", orderID)
                        .execute();

                // 3. (Tuỳ chọn) Đánh dấu đơn hàng đã bị huỷ nếu có trạng thái
                handle.createUpdate("""
                                    UPDATE orders
                                    SET status = 'cancel'
                                    WHERE orderID = :orderID
                                """)
                        .bind("orderID", orderID)
                        .execute();

                // 4. Đồng bộ lại inventory
                syncInventoryForProductIDs(handle, new ArrayList<>(affectedProductIDs));

                handle.commit();
                return true;

            } catch (Exception e) {
                handle.rollback();
                e.printStackTrace();
                return false;
            }
        });
    }


    public static void main(String[] args) {
        BatchDAO dao = new BatchDAO();
//        System.out.println(dao.deductBatchesForOrder(144, 440));
        System.out.println(dao.cancelOrder("7"));
    }

}

