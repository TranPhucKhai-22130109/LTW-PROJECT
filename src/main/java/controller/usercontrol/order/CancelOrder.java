package controller.usercontrol.order;

import dao.BatchDAO;
import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet(name = "CancelOrder", value = "/admin/cancel-order")
public class CancelOrder extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String orderID = request.getParameter("order_id");
        System.out.println("Success:" + orderID);
        new BatchDAO().cancelOrder(orderID);
        response.sendRedirect(request.getContextPath() + "/load-profile");

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}