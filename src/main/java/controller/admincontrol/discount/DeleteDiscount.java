package controller.admincontrol.discount;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dao.DiscountDAO;
import entity.Customer;
import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.*;
import util.LogUtil;

import java.io.IOException;

@WebServlet(name = "DeleteDiscount", value = "/admin/delete-discount")
public class DeleteDiscount extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String discountId = request.getParameter("discountId");
        DiscountDAO dao = new DiscountDAO();
        dao.deleteDiscountAndUpdateProducts(discountId);


        boolean isSuccess = false;
        try {
            dao.deleteDiscountAndUpdateProducts(discountId);
            isSuccess = true;
        } catch (Exception e) {
            isSuccess = false;
        }
        // Lấy thông tin user admin từ session
        HttpSession session = request.getSession(false);
        Customer customer = (Customer) session.getAttribute("customer");
        int customerID = customer.getId();
        int role = 1; // admin

        // Ghi log
        LogUtil.info(
                "DELETE_DISCOUNT",
                "Xóa mã giảm giá " + discountId + " " + (isSuccess ? "thành công" : "thất bại"),
                customerID,
                role,
                request.getRemoteAddr()
        );
        // Phản hồi về client
        JsonObject result = new JsonObject();
        result.addProperty("isSuccess", true);
        response.setContentType("application/json");

        Gson gson = new Gson();
        response.getWriter().write(gson.toJson(result));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}