package controller.admincontrol.authorization;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dao.AuthorizationDAO;
import entity.Customer;
import entity.authorization.Permission;
import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.*;
import util.LogUtil;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@WebServlet(name = "AssignRolePermission", value = "/admin/assign-role-per")
public class AssignRolePermission extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String roleID = request.getParameter("roleID");
        String[] permissionID = request.getParameterValues("permissions");

        AuthorizationDAO auth = new AuthorizationDAO();
        boolean isSuccessDelete = false;

        // xóa
        Set<String> selectedSet = permissionID != null ? new HashSet<>(Arrays.asList(permissionID)) : new HashSet<>();

        List<Permission> current = auth.getPermissionsByRole(Integer.parseInt(roleID));



        // Xóa những cái không còn trong selectedSet
        for (Permission oldPerm : current) {
            String permIDStr = String.valueOf(oldPerm.getPermissionID());
            if (!selectedSet.contains(permIDStr)) {
                System.out.println("Xóa quyền có id là: " + permIDStr);
                auth.deleteRolePermission(roleID, permIDStr);
            }
        }
        boolean isSuccessAdd = auth.insertRolePermissions(roleID, permissionID);

        // Ghi log: Ai đã phân quyền gì cho role nào
        HttpSession session = request.getSession(false);
        Customer admin = (Customer) session.getAttribute("customer");

        if (admin != null) {
            String status = (isSuccessAdd || isSuccessDelete) ? "SUCCESS" : "FAILED";
            String msg = "Admin " + admin.getEmail() + " đã cập nhật quyền cho roleID = " + roleID
                    + ", thêm quyền: " + Arrays.toString(permissionID);
            LogUtil.warn("ASSIGN_ROLE_PERMISSION_" + status, msg, admin.getId(), admin.getRole(), request.getRemoteAddr());
        }

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("isSuccessAdd", isSuccessAdd);
        jsonObject.addProperty("isSuccessDelete", isSuccessDelete);

        Gson gson = new Gson();
        String json = gson.toJson(jsonObject);
        response.getWriter().write(json);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}