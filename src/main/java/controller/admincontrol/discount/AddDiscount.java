package controller.admincontrol.discount;

import com.google.gson.JsonObject;
import dao.DiscountDAO;
import entity.Customer;
import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.*;
import util.LogUtil;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@WebServlet(name = "AddDiscount", value = "/admin/add-discount")
public class AddDiscount extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String type = request.getParameter("discountType");
        String discount = request.getParameter("discountValue");
        String startTime = request.getParameter("startDateTime");
        String endTime = request.getParameter("endDateTime");

        boolean isSuccess = false;

        if (startTime != null && endTime != null) {
            Timestamp start = Timestamp.valueOf(LocalDateTime.parse(startTime));
            Timestamp end = Timestamp.valueOf(LocalDateTime.parse(endTime));
            System.out.println(start + " " + end);
            DiscountDAO dao = new DiscountDAO();
            dao.addDiscount(type, discount, start, end);
            isSuccess = true;
        }

        // Lấy thông tin user đăng nhập admin (không cần check null nếu chắc chắn có)
        HttpSession session = request.getSession(false);
        Customer customer = (Customer) session.getAttribute("customer");
        int customerID = customer.getId();
        int role = 1; // admin

        // Ghi log thêm discount
        LogUtil.info(
                "ADD_DISCOUNT",
                "Thêm mã giảm giá " + (isSuccess ? "thành công" : "thất bại"),
                customerID,
                role,
                request.getRemoteAddr()
        );

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("isSuccess", isSuccess);
        response.setContentType("application/json");
        response.getWriter().print(jsonObject);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}