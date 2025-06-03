package controller.admincontrol.user;

import dao.CustomerDAO;
import entity.Customer;
import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.*;
import util.LogUtil;

import java.io.IOException;

@WebServlet(name = "DeleteUser", value = "/admin/delete-user")
public class DeleteUser extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int customerID = Integer.parseInt(request.getParameter("cID").trim());
        CustomerDAO cusDao = new CustomerDAO();
        int result = cusDao.deleteUser(customerID);
        boolean isDeleted = (result > 0);



        // Logging
        HttpSession session = request.getSession(false);
        Object obj = (session != null) ? session.getAttribute("customer") : null;
        int userID = (obj instanceof Customer customer) ? customer.getId() : -1;

        if (isDeleted) {
            LogUtil.info("DELETE_USER", "Xóa người dùng với ID: " + customerID, userID, 1, request.getRemoteAddr());
        } else {
            LogUtil.error("DELETE_USER_FAIL", "Xóa người dùng thất bại với ID: " + customerID, userID, 1, request.getRemoteAddr(),"Lỗi trong quá trình xóa");
        }

        response.sendRedirect("all-user");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}