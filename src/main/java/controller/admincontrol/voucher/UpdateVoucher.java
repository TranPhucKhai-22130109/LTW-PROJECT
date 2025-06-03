package controller.admincontrol.voucher;

import dao.CouponDAO;
import entity.Customer;
import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.*;
import util.LogUtil;

import java.io.IOException;

@WebServlet(name = "updateVoucher", value = "/admin/update-voucher")
public class UpdateVoucher extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Object obj = (session != null) ? session.getAttribute("customer") : null;
        int userID = (obj instanceof Customer customer) ? customer.getId() : -1;

        String ip = request.getRemoteAddr();
        boolean isSuccess = false;
        try {
        int id = Integer.parseInt(request.getParameter("id"));
        String code = request.getParameter("code").toUpperCase();
        String discount = request.getParameter("discount");
            LogUtil.info("UPDATE_COUPON", "Admin ID " + userID + " yêu cầu cập nhật mã ID: " + id + " thành " + code + " - " + discount, userID, 1, ip);
        CouponDAO cdao = new CouponDAO();
        isSuccess = cdao.updateCoupon(id, code, discount) > 0;
            if (isSuccess) {
                LogUtil.info("UPDATE_COUPON", "Cập nhật thành công mã giảm giá ID: " + id, userID, 1, ip);
            } else {
                LogUtil.warn("UPDATE_COUPON", "Không thể cập nhật mã giảm giá ID: " + id, userID, 1, ip);
            }

        } catch (Exception e) {
            LogUtil.error("UPDATE_COUPON", "Lỗi khi cập nhật mã giảm giá: " + e.getMessage(), userID, 1, ip, e.toString());
        }

        response.setContentType("application/json");
        response.getWriter().write("{\"isSuccess\":" + isSuccess + "}");


    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}