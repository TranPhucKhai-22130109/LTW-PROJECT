package controller.cartcontrol;

import entity.Cart;
import entity.Customer;
import helper.CartManagerDB;
import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.*;
import util.LogUtil;

import java.io.IOException;

@WebServlet(name = "RemoveController", value = "/remove-cart")
public class RemoveCart extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String ip = request.getRemoteAddr();
        HttpSession session = request.getSession();

        int userID = -1;
        int role = 0; // 0: chưa đăng nhập, 1: đã đăng nhập
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer != null) {
            userID = customer.getId();
            role = 1;
        }

        try {
            String pID = request.getParameter("pID");
            int proID = Integer.parseInt(pID);
            LogUtil.info("REMOVE_FROM_CART", "Yêu cầu xóa sản phẩm ID: " + proID, userID, role, ip);

            Cart c = (Cart) session.getAttribute("cart");
            if (c == null) {
                LogUtil.warn("REMOVE_FROM_CART", "Không tìm thấy giỏ hàng trong session", userID, role, ip);
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            c.remove(proID);
            session.setAttribute("cart", c);
            LogUtil.info("REMOVE_FROM_CART", "Đã xóa sản phẩm ID: " + proID + " khỏi session", userID, role, ip);

            // ========= DELETE CART ITEM DB ========= //
            if (customer != null) {
                CartManagerDB cartManagerDB = new CartManagerDB();
                cartManagerDB.deleteCartItemDB(request, proID);
                LogUtil.info("REMOVE_FROM_CART", "Đã xóa sản phẩm ID " + proID + " khỏi DB cho userID: " + userID, userID, role, ip);
            }

            // Trả về JSON phản hồi
            response.setContentType("application/json");
            response.getWriter().write("{\"isSuccess\":true, \"TotalQuantity\":"
                    + c.getTotalQuantity() + ", \"Total\":" + c.getTotal() + "}");

        } catch (Exception e) {
            LogUtil.error("REMOVE_FROM_CART", "Lỗi khi xóa sản phẩm khỏi giỏ: " + e.getMessage(), userID, role, ip, e.toString());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
