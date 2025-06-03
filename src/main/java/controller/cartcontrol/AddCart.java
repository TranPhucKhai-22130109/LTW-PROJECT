package controller.cartcontrol;

import dao.ProductDAO;
import entity.Cart;
import entity.Customer;
import entity.Product;
import helper.CartManagerDB;
import jakarta.servlet.annotation.*;

import java.io.IOException;

// Controller for handling Add to Cart functionality
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import util.LogUtil;

@WebServlet(name = "Add", value = "/add-cart")
public class AddCart extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Object obj = (session != null) ? session.getAttribute("customer") : null;
        int userID = (obj instanceof Customer customer) ? customer.getId() : -1;
        Customer customer = (Customer) session.getAttribute("customer");
        try {

            String pID = request.getParameter("pID");
            LogUtil.info("ADD_TO_CART", "Yêu cầu thêm sản phẩm vào giỏ: " + pID, userID, 1, request.getRemoteAddr());

            ProductDAO productDAO = new ProductDAO();
            Product product = productDAO.getProductByID(pID);

            if (product == null) {
                LogUtil.warn("ADD_TO_CART", "Sản phẩm không tồn tại với ID: " + pID, userID, 1, request.getRemoteAddr());
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            if (product.getIsDiscount() == 1) {
                product.setProductPrice(product.getDiscountPrice());
            }

            Cart c = (Cart) session.getAttribute("cart");
            if (c == null) c = new Cart();

            c.add(product);
            session.setAttribute("cart", c);

            LogUtil.info("ADD_TO_CART", "Đã thêm sản phẩm vào giỏ: " + product.getProductName(), userID, 1, request.getRemoteAddr());

            // Nếu đã đăng nhập thì lưu vào DB
            if (customer != null) {
                CartManagerDB cartManagerDB = new CartManagerDB();
                cartManagerDB.saveCartDB(request, customer);
                LogUtil.info("ADD_TO_CART", "Đã lưu giỏ hàng vào DB cho userID: " + userID, userID, 1, request.getRemoteAddr());
            }

        } catch (Exception e) {
            LogUtil.error("ADD_TO_CART", "Lỗi khi thêm sản phẩm vào giỏ: " + e.getMessage(), userID, 1, request.getRemoteAddr(), e.toString());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}




