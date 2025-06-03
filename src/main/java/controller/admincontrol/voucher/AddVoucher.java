package controller.admincontrol.voucher;

import dao.CouponDAO;
import entity.Customer;
import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.*;
import org.json.JSONObject;
import util.LogUtil;

import java.io.IOException;

@WebServlet(name = "addCouponController", value = "/admin/add-coupon")
public class AddVoucher extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String code = request.getParameter("code");
        String txt_Discount = request.getParameter("discount");


        // Logging
        HttpSession session = request.getSession(false);
        Object obj = (session != null) ? session.getAttribute("customer") : null;
        int userID = (obj instanceof Customer customer) ? customer.getId() : -1;

        LogUtil.info("ADD_COUPON", "Admin ID " + userID + " bắt đầu thêm mã giảm giá: " + code, userID, 1, request.getRemoteAddr());

        String msg = "";
        int id = 0;
        boolean isSuccess = false;
        try {
            if (code.isBlank() || txt_Discount.isBlank()) {
                msg = "Thiếu mã giảm hoặc giá trị của mã";
            } else {
                code = code.toUpperCase();
                double discount = Double.parseDouble(txt_Discount.trim());
                CouponDAO couponDAO = new CouponDAO();

                if (couponDAO.isCouponExist(code)) {
                    msg = "Mã giảm giá đã tồn tại";
                } else {
                    id = couponDAO.addCoupon(code, discount);
                    if (id >= 1) {
                        msg = "Thêm mã thành công";
                        isSuccess = true;
                        LogUtil.info("ADD_COUPON", "Thêm mã giảm giá thành công: " + code + ", id: " + id, userID, 1, request.getRemoteAddr());
                    }
                }
            }
        } catch (Exception e) {
            msg = "Lỗi khi thêm mã giảm giá";
            LogUtil.error("ADD_COUPON", "Lỗi khi thêm mã giảm giá: " + e.getMessage(), userID,
                    1, request.getRemoteAddr(), e.toString());
        }
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("msg", msg);
            jsonResponse.put("vId", id);
            jsonResponse.put("isSuccess", isSuccess);

            response.setContentType("application/json");
            response.getWriter().write(jsonResponse.toString());
        }

        @Override
        protected void doPost (HttpServletRequest request, HttpServletResponse response) throws
        ServletException, IOException {
            doGet(request, response);
        }
    }