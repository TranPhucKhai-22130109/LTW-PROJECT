package controller.admincontrol.authorization;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dao.AuthorizationDAO;
import entity.Customer;
import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.*;

import util.LogUtil;
import java.io.IOException;

@WebServlet(name = "AddRole", value = "/admin/add-role")
public class AddRole extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String role_name = request.getParameter("role-name");
        AuthorizationDAO dao = new AuthorizationDAO();
        boolean isSuccess = dao.insertRole(role_name.toUpperCase());

        // Ghi log sau khi thêm
        HttpSession session = request.getSession(false);
        Customer cus = (Customer) session.getAttribute("customer");

        if (cus != null) {
            String status = isSuccess ? "SUCCESS" : "FAILED";
            LogUtil.info(
                    "ADD_ROLE_" + status,
                    "Admin " + cus.getEmail() + " tried to add role: '" + role_name + "' -> " + status,
                    cus.getId(),
                    cus.getRole(),
                    request.getRemoteAddr()
            );
        }

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