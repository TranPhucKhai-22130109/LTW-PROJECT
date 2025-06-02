package controller.admincontrol.category;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dao.CategoryDAO;
import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.*;
import util.LogUtil;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "DeleteCate", value = "/admin/delete-cate")
public class DeleteCate extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String cID = request.getParameter("cID");
        JsonObject jsonObject = new JsonObject();
        try {
            if (cID == null || cID.trim().isEmpty()) {
                jsonObject.addProperty("isSuccess", false);
                jsonObject.addProperty("error", "Invalid category ID");
                response.getWriter().write(new Gson().toJson(jsonObject));
                return;
            }

            CategoryDAO dao = new CategoryDAO();
            boolean isSuccess = dao.removeCate(cID) > 0;

            HttpSession session = request.getSession(false);
            Object user = session != null ? session.getAttribute("customer") : null;
            Integer customerID = null;
            int role = 1; // giả định admin = 1
            if (user instanceof entity.Customer customer) {
                customerID = customer.getId();
            }

            // Ghi log
            if (isSuccess) {
                LogUtil.info("DELETE_CATEGORY", "Xóa danh mục thành công", customerID, 1, request.getRemoteAddr());
            } else {

                LogUtil.error("DELETE_CATEGORY", "Xóa danh mục thất bại", customerID, 1, request.getRemoteAddr(),"");
            }

            // Trả về JSON
            jsonObject.addProperty("isSuccess", isSuccess);
            if (!isSuccess) jsonObject.addProperty("error", "Failed to delete category");
            response.getWriter().write(new Gson().toJson(jsonObject));
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.addProperty("isSuccess", false);
            jsonObject.addProperty("error", "Server error: " + e.getMessage());
            response.getWriter().write(new Gson().toJson(jsonObject));
        }
    }
}