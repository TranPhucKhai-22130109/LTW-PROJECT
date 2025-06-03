package dao;

import context.JDBIContext;
import entity.Coupon;
import entity.Customer;
import entity.Order;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class OrderDAO {

    // hiển thị tất cả đơn hàng
    public List<Order> getAllOrder() {
        return JDBIContext.getJdbi().withHandle(handle ->
                (handle.createQuery("select * from orders ").mapToBean(Order.class).list())
        );
    }

    public Order getOrderById(String id) {
        return JDBIContext.getJdbi().withHandle(handle ->
                handle.createQuery("select * from orders where orderID= :orderID")
                        .bind("orderID", id)
                        .mapToBean(Order.class).findOne().orElse(null)
        );
    }

    public Order getOrderByCusId(int id) {
        return JDBIContext.getJdbi().withHandle(handle ->
                handle.createQuery("select * from orders where cusID= :cusID")
                        .bind("cusID", id)
                        .mapToBean(Order.class).findOne().orElse(null)
        );
    }

    public List<Order> getListOrderByCusId(int id) {
        return JDBIContext.getJdbi().withHandle(handle ->
                handle.createQuery("select * from orders where cusID= :cusID")
                        .bind("cusID", id)
                        .mapToBean(Order.class).list());

    }


    public int createOrder(int id, double totalPrice, String status, String address, int quantity, Date date, String orderCodeGHN) {
        return JDBIContext.getJdbi().withHandle(handle ->
                handle.createUpdate("INSERT INTO orders (cusID, totalPrice, status, address, quantity, date, oder_code_ghn)\n" +
                                "VALUES (:cusID, :totalPrice, :status, :address, :quantity,  :date, :oder_code_ghn);")
                        .bind("cusID", id)
                        .bind("totalPrice", totalPrice)
                        .bind("status", status)
                        .bind("address", address)
                        .bind("quantity", quantity)
                        .bind("date", date)
                        .bind("oder_code_ghn", orderCodeGHN)
                        .executeAndReturnGeneratedKeys("id") // Trả về khóa tự động tăng của cột `id`
                        .mapTo(int.class) // Map giá trị `id` sang kiểu `int`
                        .one() // Lấy giá trị duy nhất (ID)

        );
    }

    public Double totalOrderPrice() {
        String sql = "SELECT SUM(orders.totalPrice) FROM `orders`";
        return JDBIContext.getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .mapTo(Double.class) // Ánh xạ kết quả thành Double
                        .findOnly()          // Lấy giá trị duy nhất
        );

    }

    // cập nhật trạng thái đơn
    public boolean updateStatus(String orderID, String status) {
        int rowsAffected = JDBIContext.getJdbi().withHandle(handle ->
                handle.createUpdate("UPDATE orders SET status = :status WHERE orderID = :orderID")
                        .bind("status", status)
                        .bind("orderID", orderID)
                        .execute()
        );
        return rowsAffected > 0;
    }


    public static void main(String[] args) {
        OrderDAO dao = new OrderDAO();
        System.out.println(dao.totalOrderPrice());
    }
}
