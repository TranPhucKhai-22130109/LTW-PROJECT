package controller.admincontrol.product;

import dao.ProductDAO;
import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.*;
import util.LogUtil;

import java.io.IOException;

@WebServlet(name = "DeleteProduct", value = "/admin/delete-pro")
public class DeleteProduct extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int pro_id = Integer.parseInt(request.getParameter("pID").trim());

        // Lấy admin từ session
        HttpSession session = request.getSession(false);
        Object user = (session != null) ? session.getAttribute("customer") : null;
        int customerID = -1;
        if (user instanceof entity.Customer customer) {
            customerID = customer.getId();
        }

        // Ghi log trước khi xóa
        LogUtil.info(
                "DELETE_PRODUCT",
                "Xóa sản phẩm có ID = " + pro_id,
                customerID,
                1, // role admin
                request.getRemoteAddr()
        );
        ProductDAO dao= new ProductDAO();
        dao.deleteProductById(pro_id);
        response.sendRedirect("load-pAdmin");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}