package controller.admincontrol.voucher;

import dao.CouponDAO;
import entity.Customer;
import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.*;
import util.LogUtil;

import java.io.IOException;

@WebServlet(name = "deleteVoucher", value = "/admin/delete-voucher")
public class DeleteVoucher extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int voucher_id = Integer.parseInt(request.getParameter("vID").trim());



        // Logging
        HttpSession session = request.getSession(false);
        Object obj = (session != null) ? session.getAttribute("customer") : null;
        int userID = (obj instanceof Customer customer) ? customer.getId() : -1;
        boolean isSuccess = false;
        LogUtil.info("DELETE_COUPON", "Admin ID " + userID + " yêu cầu xóa mã giảm giá ID: " + voucher_id, userID, 1, request.getRemoteAddr());
        try {
        CouponDAO dao = new CouponDAO();

        isSuccess = dao.deleteCoupon(voucher_id) > 0;

        if (isSuccess) {
            LogUtil.info("DELETE_COUPON", "Xóa thành công mã giảm giá ID: " + voucher_id, userID, 1, request.getRemoteAddr());
        } else {
            LogUtil.warn("DELETE_COUPON", "Không thể xóa mã giảm giá ID: " + voucher_id, userID, 1, request.getRemoteAddr());
        }
    } catch (Exception e) {
        LogUtil.error("DELETE_COUPON", "Lỗi khi xóa mã giảm giá: " + e.getMessage(), userID, 1, request.getRemoteAddr(), e.toString());
    }

        response.setContentType("application/json");
        response.getWriter().write("{\"isSuccess\":" + isSuccess + "}");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}