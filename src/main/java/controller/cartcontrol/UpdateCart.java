package controller.cartcontrol;

import entity.Cart;
import entity.CartItem;
import entity.Customer;
import helper.CartManagerDB;
import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.*;
import util.LogUtil;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "updateCart", value = "/update-cart")
public class UpdateCart extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String ip = request.getRemoteAddr();
        HttpSession session = request.getSession(false);

        int userID = -1;
        int role = 0;
        Customer customer = (Customer) (session != null ? session.getAttribute("customer") : null);
        if (customer != null) {
            userID = customer.getId();
            role = 1;
        }

        try {
            if (session == null) {
                LogUtil.warn("UPDATE_CART", "Session không tồn tại", userID, role, ip);
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            Cart c = (Cart) session.getAttribute("cart");
            if (c == null) {
                LogUtil.warn("UPDATE_CART", "Cart không tồn tại trong session", userID, role, ip);
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            String txt_id = request.getParameter("pID");
            int id = Integer.parseInt(txt_id);
            int quantity = Integer.parseInt(request.getParameter("quantity"));

            LogUtil.info("UPDATE_CART", "Yêu cầu cập nhật sản phẩm ID: " + id + ", quantity: " + quantity, userID, role, ip);

            c.update(id, quantity);
            session.setAttribute("cart", c);
            LogUtil.info("UPDATE_CART", "Đã cập nhật session cart với sản phẩm ID: " + id, userID, role, ip);

            // lấy ra cụ thể 1 cart item
            CartItem ct = c.getData().get(id);

            // ========= SAVE CART DB ========= //
            if (customer != null) {
                CartManagerDB cartManagerDB = new CartManagerDB();
                cartManagerDB.saveCartDB(request, customer);
                LogUtil.info("UPDATE_CART", "Đã lưu cart vào DB cho userID: " + userID, userID, role, ip);
            }

            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.write("{\"TotalQuantity\":" + c.getTotalQuantity()
                    + ", \"Total\":" + c.getTotal()
                    + ", \"TotalCt\":" + ct.getTotalCt() + "}");

        } catch (Exception e) {
            LogUtil.error("UPDATE_CART", "Lỗi khi cập nhật cart: " + e.getMessage(), userID, role, ip, e.toString());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}