package controller.admincontrol.inventory;

import dao.InventoryDAO;
import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.*;
import util.LogUtil;

import java.io.IOException;

@WebServlet(name = "deleteInventoryItem", value = "/admin/deleteInventoryItem")
public class deleteInventoryItem extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Lấy productID từ tham số URL dưới dạng String
        String productID = request.getParameter("productID");

        // Gọi phương thức xóa từ InventoryDAO với productID là String
        InventoryDAO inventoryDAO = new InventoryDAO();
        boolean success = inventoryDAO.deleteInventoryItem(productID); // Truyền productID dạng String vào phương thức

        if (success) {
            // Nếu xóa thành công, chuyển hướng về trang danh sách sản phẩm kho
            response.sendRedirect(request.getContextPath() + "/admin/list-warehouse");

        } else {
            // Nếu xóa thất bại, trả về thông báo lỗi
            response.setContentType("application/json");
            response.getWriter().write("{\"isSuccess\": false, \"msg\": \"Xóa thất bại\"}");
        }
        // Lấy thông tin admin từ session
        HttpSession session = request.getSession(false);
        Object user = (session != null) ? session.getAttribute("customer") : null;
        int customerID = -1;
        if (user instanceof entity.Customer customer) {
            customerID = customer.getId();
        }

// Ghi log sau khi xóa thành công
        if (success) {
            LogUtil.info(
                    "DELETE_INVENTORY_ITEM",
                    "Xóa sản phẩm trong kho với productID: " + productID,
                    customerID,
                    1, // role admin
                    request.getRemoteAddr()
            );
        } else {
            // Log thất bại xóa chi tiết hơn (đã có System.out.println rồi)
            LogUtil.error(
                    "DELETE_INVENTORY_ITEM_FAILED",
                    "Xóa sản phẩm trong kho thất bại với productID: " + productID,
                    customerID,
                    1,
                    request.getRemoteAddr(),
                    ""
            );
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Không cần xử lý POST trong trường hợp này vì xóa là một hành động GET
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "POST method is not allowed");
    }

}
