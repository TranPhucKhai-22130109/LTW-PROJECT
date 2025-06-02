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

@WebServlet(name = "AddUserRole", value = "/admin/add-user-role")
public class AddUserRole extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String roleIDs = request.getParameter("roleIDs");
        String customerID = request.getParameter("customerID");

        AuthorizationDAO auth = new AuthorizationDAO();
        boolean isSuccess = auth.insertUserRole(customerID, roleIDs);

        // Ghi log sau khi gán quyền
        HttpSession session = request.getSession(false);
        Customer admin = (Customer) session.getAttribute("customer");

        if (admin != null) {
            String status = isSuccess ? "SUCCESS" : "FAILED";
            LogUtil.warn(  // <-- dùng WARN vì đây là hành động phân quyền
                    "ASSIGN_ROLE_" + status,
                    "Admin" + admin.getEmail() + " gán quyền [" + roleIDs + "] cho người dùng ID = " + customerID + " -> " + status,
                    admin.getId(),
                    admin.getRole(),
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