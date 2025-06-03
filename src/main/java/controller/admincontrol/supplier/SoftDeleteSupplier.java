package controller.admincontrol.supplier;

import dao.SupplierDAO;
import entity.Customer;
import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.*;
import util.LogUtil;

import java.io.IOException;

@WebServlet(name = "SoftDeleteSupplier", value = "/admin/soft-delete")
public class SoftDeleteSupplier extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String supplierID = request.getParameter("supplierId");
        String option = request.getParameter("option");
        HttpSession session = request.getSession(false);
        Object obj = (session != null) ? session.getAttribute("customer") : null;
        int userID = (obj instanceof Customer customer) ? customer.getId() : -1;

        if (option != null) {
            new SupplierDAO().unSoftDeleteSupplier(Integer.parseInt(supplierID));
            LogUtil.info("RESTORE_SUPPLIER", "Khôi phục nhà cung cấp với ID: " + supplierID, userID, 1, request.getRemoteAddr());
        } else {
            boolean deleted = new SupplierDAO().softDeleteSupplier(Integer.parseInt(supplierID));
            if (!deleted) {
                // Xóa thất bại, hiển thị lỗi
                request.setAttribute("error", "Xóa thất bại.");
                request.getRequestDispatcher("/admin/supplier/jsp").forward(request, response);
            }
            LogUtil.info("DELETE_SUPPLIER", "Xóa mềm nhà cung cấp với ID: " + supplierID, userID, 1, request.getRemoteAddr());
        }

        response.sendRedirect(request.getContextPath() + "/admin/list-supplier");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}