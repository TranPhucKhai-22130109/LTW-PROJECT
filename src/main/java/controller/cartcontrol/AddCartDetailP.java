package controller.cartcontrol;

import dao.ProductDAO;
import entity.Cart;
import entity.Customer;
import entity.Product;
import helper.CartManagerDB;
import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.*;
import util.LogUtil;

import java.io.IOException;

@WebServlet(name = "AddCartDetailP", value = "/add-card-dp")
public class AddCartDetailP extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Object obj = (session != null) ? session.getAttribute("customer") : null;
        int userID = (obj instanceof Customer customer) ? customer.getId() : -1;
        Customer customer = (Customer) session.getAttribute("customer");



        try {
            String pID = request.getParameter("pID");
            String quantityTxt = request.getParameter("quantity");

            LogUtil.info("ADD_TO_CART_DETAIL", "Yêu cầu thêm sản phẩm " + pID + " với số lượng " + quantityTxt, userID, 1, request.getRemoteAddr());

            int quantity = Integer.parseInt(quantityTxt.trim());
            if (quantity <= 0) {
                LogUtil.warn("ADD_TO_CART_DETAIL", "Số lượng không hợp lệ: " + quantityTxt, userID, 1, request.getRemoteAddr());
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            ProductDAO productDAO = new ProductDAO();
            Product product = productDAO.getProductByID(pID);
            if (product == null) {
                LogUtil.warn("ADD_TO_CART_DETAIL", "Không tìm thấy sản phẩm với ID: " + pID, userID, 1, request.getRemoteAddr());
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            if (product.getIsDiscount() == 1) {
                product.setProductPrice(product.getDiscountPrice());
            }

            Cart c = (Cart) session.getAttribute("cart");
            if (c == null) c = new Cart();

            c.addWithQuantity(product, quantity);
            session.setAttribute("cart", c);

            LogUtil.info("ADD_TO_CART_DETAIL", "Đã thêm sản phẩm: " + product.getProductName() + " với số lượng " + quantity, userID, 1, request.getRemoteAddr());

            // Lưu cart vào DB nếu đã đăng nhập
            if (customer != null) {
                CartManagerDB cartManagerDB = new CartManagerDB();
                cartManagerDB.saveCartDB(request, customer);
                LogUtil.info("ADD_TO_CART_DETAIL", "Đã lưu giỏ hàng vào DB cho userID: " + userID, userID, 1, request.getRemoteAddr());
            }

        } catch (Exception e) {
            LogUtil.error("ADD_TO_CART_DETAIL", "Lỗi khi thêm sản phẩm vào giỏ: " + e.getMessage(), userID, 1, request.getRemoteAddr(), e.toString());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}