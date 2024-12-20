package controller;

import dao.CustomerDAO;
import entity.Customer;
import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet(name = "LoginControl", value = "/login")
public class Login extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String emailLogin = request.getParameter("email-login");
        String passLogin = request.getParameter("password-login");


        CustomerDAO cusDao = new CustomerDAO();
        Customer cus = cusDao.getUserByEmailPass(emailLogin, passLogin);
        System.out.println(cus);

        if (cus == null) {
//          response.sendRedirect("forms/signup-login.jsp");
            request.setAttribute("error", "Sai tài khoản hoặc mật khẩu");
            request.getRequestDispatcher("forms/signup-login.jsp").forward(request, response);
        } else {
            HttpSession session = request.getSession();
            session.setAttribute("customer", cus);
            request.getRequestDispatcher("home").forward(request, response);
        }
    }

}