package controller.admincontrol.util;

import dao.CustomerDAO;
import entity.Customer;
import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.*;
import util.LogUtil;

import java.io.IOException;

@WebServlet(name = "ResetPassword", value = "/admin/reset-password")
public class ResetPassword extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String cID = request.getParameter("cID");
        CustomerDAO cDAO = new CustomerDAO();

        // Logging
        HttpSession session = request.getSession(false);
        Object obj = (session != null) ? session.getAttribute("customer") : null;
        int userID = (obj instanceof Customer customer) ? customer.getId() : -1;

        cDAO.resetPassword(cID);

        LogUtil.info("RESET_PASSWORD",
                "Admin ID " + userID + " reset mật khẩu cho Customer ID " + cID,
                userID, 1, request.getRemoteAddr());


        response.sendRedirect("all-user");

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}