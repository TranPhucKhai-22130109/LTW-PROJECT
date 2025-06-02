package controller.admincontrol.inventory;

import dao.InventoryDAO;
import entity.Inventory;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import util.LogUtil;

import java.io.IOException;

@WebServlet(name = "UpdateInventory", value = "/admin/UpdateInventory")
public class UpdateInventory extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int productID = Integer.parseInt(request.getParameter("productID"));
        int quantityInStock = Integer.parseInt(request.getParameter("quantityInStock"));
        int quantitySold = Integer.parseInt(request.getParameter("quantitySold"));
        int quantityReserved = Integer.parseInt(request.getParameter("quantityReserved"));
        int reorderLevel = Integer.parseInt(request.getParameter("reorderLevel"));
        String productName = request.getParameter("productName");

        Inventory updatedInventory = new Inventory(productID, quantityInStock, quantitySold, quantityReserved, reorderLevel, null, productName);

        // Gọi DAO để cập nhật cơ sở dữ liệu
        InventoryDAO inventoryDAO = new InventoryDAO();
        boolean success = inventoryDAO.updateInventory(updatedInventory);

        // Lấy thông tin admin từ session
        HttpSession session = request.getSession(false);
        Object user = (session != null) ? session.getAttribute("customer") : null;
        int customerID = -1;
        if (user instanceof entity.Customer customer) {
            customerID = customer.getId();
        }

        if (success) {
            LogUtil.info(
                    "UPDATE_INVENTORY",
                    "Cập nhật tồn kho cho sản phẩm ID: " + productID
                            + ", Tên sản phẩm: " + productName
                            + ", Số lượng tồn: " + quantityInStock
                            + ", Đã bán: " + quantitySold
                            + ", Đã đặt: " + quantityReserved
                            + ", Mức đặt hàng lại: " + reorderLevel,
                    customerID,
                    1, // role admin
                    request.getRemoteAddr()
            );
        } else {
            LogUtil.error(
                    "UPDATE_INVENTORY_FAILED",
                    "Cập nhật tồn kho thất bại cho sản phẩm ID: " + productID,
                    customerID,
                    1,
                    request.getRemoteAddr(),""
            );
        }


        if (success) {
            // Trả về phản hồi JSON khi cập nhật thành công
            response.setContentType("application/json");
            response.getWriter().write("{\"isSuccess\": true}");
        } else {
            // Trả về phản hồi lỗi nếu không thành công
            response.setContentType("application/json");
            response.getWriter().write("{\"isSuccess\": false, \"msg\": \"Cập nhật thất bại\"}");
        }
    }
}

