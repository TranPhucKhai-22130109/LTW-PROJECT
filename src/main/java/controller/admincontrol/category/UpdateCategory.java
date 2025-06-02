package controller.admincontrol.category;

import dao.CategoryDAO;
import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.*;
import util.LogUtil;

import java.io.IOException;

@WebServlet(name = "UpdateCategory", value = "/admin/update-cate")
public class UpdateCategory extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        String img = request.getParameter("img");

        CategoryDAO cdao = new CategoryDAO();
        int row = cdao.updateCate(id, name, img);
        boolean isSuccess = row > 0;

        // Lấy session info
        HttpSession session = request.getSession(false);
        Object user = session != null ? session.getAttribute("customer") : null;
        Integer customerID = null;
        int role = 1; // giả định admin = 1
        if (user instanceof entity.Customer customer) {
            customerID = customer.getId();
        }

        // Tạo chuỗi extra dạng string
        String extraData = "categoryId=" + id
                + "; newName=" + name
                + "; newImg=" + img
                + "; result=" + (isSuccess ? "success" : "fail");

        // Ghi log update category
        LogUtil.info(
                "UPDATE_CATEGORY",
                "Cập nhật danh mục " + (isSuccess ? "thành công" : "thất bại"),
                customerID, // customerID nếu có thì truyền vào
                1,    // role = admin
                request.getRemoteAddr()
        );

        response.setContentType("application/json");
        response.getWriter().write("{\"nameCate\":\"" + name + "\", \"img\":\"" + img + "\"}");



    }
}