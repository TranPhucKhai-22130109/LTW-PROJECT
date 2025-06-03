package controller.usercontrol.order;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dao.OrderDAO;
import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet(name = "UpdateStatusOrder", value = "/admin/update-status-order")
public class UpdateStatusOrder extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String orderID = request.getParameter("orderID");
        String status = request.getParameter("status");

        OrderDAO orderDAO = new OrderDAO();
        boolean isSuccess = orderDAO.updateStatus(orderID, status);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("isSuccess", isSuccess);

        Gson gson = new Gson();
        String json = gson.toJson(jsonObject);
        response.getWriter().write(json);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}