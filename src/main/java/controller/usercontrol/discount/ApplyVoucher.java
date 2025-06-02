package controller.usercontrol.discount;

import dao.CouponDAO;
import entity.Cart;
import entity.Coupon;
import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet(name = "ApplyVoucher", value = "/apply-voucher")
public class ApplyVoucher extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String voucher = request.getParameter("voucher");

        CouponDAO couponDAO = new CouponDAO();
        boolean isExits = couponDAO.isCouponExist(voucher);
        if (isExits) {
            Coupon coupon = couponDAO.getCouponByName(voucher);
            double price = Double.parseDouble(request.getParameter("finalPrice"));
            double priceAfterDiscount = price - (price * coupon.getDiscount());

            response.setContentType("application/json");
            response.getWriter().write("{\"finalPrice\":" + priceAfterDiscount + "}");
        }
    }
}