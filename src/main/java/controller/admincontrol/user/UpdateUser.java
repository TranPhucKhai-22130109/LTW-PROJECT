package controller.admincontrol.user;

import dao.CustomerDAO;
import entity.Customer;
import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.*;
import org.json.JSONObject;
import util.LogUtil;

import java.io.IOException;

@WebServlet(name = "UpdateUser", value = "/admin/update-user")
public class UpdateUser extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String customerName = request.getParameter("username");
        String email = request.getParameter("email");
//        String pass = request.getParameter("password");
        String phone = request.getParameter("numberPhone");
        String address = request.getParameter("address");
        String addressShipping = request.getParameter("addressShipping");
        String role = request.getParameter("role");

        CustomerDAO cusDao = new CustomerDAO();

        boolean isSuccess = cusDao.updateUser(customerName, email, phone, address, addressShipping, role) > 0;

        // Logging
        HttpSession session = request.getSession(false);
        Object obj = (session != null) ? session.getAttribute("customer") : null;
        int userID = (obj instanceof Customer customer) ? customer.getId() : -1;

        if (isSuccess) {
            LogUtil.info("UPDATE_USER", "Cập nhật người dùng với email: " + email, userID, 1, request.getRemoteAddr());
        } else {
            LogUtil.error("UPDATE_USER_FAIL", "Cập nhật người dùng thất bại với email: " + email, userID, 1, request.getRemoteAddr(), "Lỗi cập nhật user");
        }


        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("isSuccess", isSuccess);

        response.setContentType("application/json");
        response.getWriter().write(jsonResponse.toString());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}