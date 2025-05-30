package dao;

import context.JDBIContext;
import entity.Discount;
import entity.Product;
import org.jdbi.v3.core.Handle;

import java.sql.Timestamp;
import java.util.List;

public class DiscountDAO {

    // lấy hết các giảm giá
    public List<Discount> getAllDiscount() {
        return JDBIContext.getJdbi().withHandle(handle ->
                (handle.createQuery("select * from discounts order by discountType desc")
                        .mapToBean(Discount.class)
                        .list())
        );
    }

    // Lấy sản phẩm có giảm giá
    public List<Product> getDiscountedProducts() {
        return JDBIContext.getJdbi().withHandle(handle ->
                handle.createQuery("SELECT p.productID, p.productName, p.productPrice, d.discountID, d.discountType, d.discountValue\n" +
                                "FROM products p\n" +
                                " JOIN productdiscount pd ON p.productID = pd.productID\n" +
                                " JOIN discounts d ON pd.discountID = d.discountID;\n")
                        .mapToBean(Product.class)
                        .list()
        );
    }

    // Lấy sản phẩm không có giảm giá
    public List<Product> getNonDiscountedProducts() {
        return JDBIContext.getJdbi().withHandle(handle ->
                handle.createQuery("SELECT p.productID, p.productName, p.productPrice, p.cateID " +
                                "FROM products p " +
                                "WHERE p.productID NOT IN (" +
                                "SELECT pd.productID " +
                                "FROM productDiscount pd " +
                                " JOIN discounts d ON pd.discountID = d.discountID )")
                        .mapToBean(Product.class)
                        .list()
        );
    }

    // thêm các sản phẩm và mã giảm tương ứng
    public void addProductAndDiscount(String pId, String discountID) {
        String sql = "INSERT INTO productdiscount (productID, discountID) VALUES (:productID, :discountID)";
        JDBIContext.getJdbi().withHandle(handle ->
                handle.createUpdate(sql)
                        .bind("productID", pId)
                        .bind("discountID", discountID)
                        .execute()
        );
    }

    // lấy object discount từ id
    public Discount getDiscount(String discountId) {
        return JDBIContext.getJdbi().withHandle(handle ->
                (handle.createQuery("select * from discounts where discountID = :discountID ")
                        .bind("discountID", discountId)
                        .mapToBean(Discount.class)
                        .one())
        );
    }

    // thêm mới discount
    public void addDiscount(String discountType, String discountValue, Timestamp startDate, Timestamp endDate) {
        String sql = "INSERT INTO discounts (discountType, discountValue, startDate, endDate) " +
                "VALUES (:discountType, :discountValue, :startDate, :endDate)";
        JDBIContext.getJdbi().withHandle(handle -> (
                handle.createUpdate(sql)
                        .bind("discountType", discountType)
                        .bind("discountValue", discountValue)
                        .bind("startDate", startDate)
                        .bind("endDate", endDate)
                        .execute()
        ));
    }

    // cập nhật lại giá sản phẩm khi 1 mã giảm giá bất kì bị xóa và xóa nó
    public void deleteDiscountAndUpdateProducts(String discountID) {
        JDBIContext.getJdbi().useTransaction(handle -> {
            // 1. Cập nhật bảng products
            String updateProductsSql = """
                        UPDATE products
                        SET discountPrice = NULL,
                            isDiscount = 0
                        WHERE productID IN (
                            SELECT productID FROM productdiscount WHERE discountID = :discountID
                        )
                    """;
            handle.createUpdate(updateProductsSql)
                    .bind("discountID", discountID)
                    .execute();

            // 2. Xóa discount (cascade sẽ lo productdiscount)
            String deleteDiscountSql = "DELETE FROM discounts WHERE discountID = :discountID";
            handle.createUpdate(deleteDiscountSql)
                    .bind("discountID", discountID)
                    .execute();
        });
    }

    // Hủy giảm giá dựa vào id sản phẩm
    public boolean UnDiscount(List<String> productIds) {
        String deleteSql = "DELETE FROM productdiscount WHERE productID IN (<ids>)";
        String updateSql = "UPDATE products SET discountPrice = NULL, isDiscount = 0 WHERE productID IN (<ids>)";

        try (Handle handle = JDBIContext.getJdbi().open()) {
            handle.begin();

            // Xóa liên kết
            handle.createUpdate(deleteSql)
                    .bindList("ids", productIds)
                    .execute();

            // Cập nhật bảng products
            handle.createUpdate(updateSql)
                    .bindList("ids", productIds)
                    .execute();

            handle.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // lấy id các sp có mã giảm giá hết hạn
    public List<String> getExpiredDiscountProductIds() {
        String sql = """
                    SELECT p.productID FROM products p
                    JOIN productdiscount pd ON p.productID = pd.productID
                    JOIN discounts d ON pd.discountID = d.discountID
                    WHERE p.isDiscount = 1 AND d.endDate < NOW()
                """;

        return JDBIContext.getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .mapTo(String.class)
                        .list()
        );
    }

    public static void main(String[] args) {
        DiscountDAO dao = new DiscountDAO();
        System.out.println(dao.getExpiredDiscountProductIds());
    }
}
