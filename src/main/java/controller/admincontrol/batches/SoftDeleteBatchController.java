package controller.admincontrol.batches;

import context.JDBIContext;
import dao.BatchDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "DeleteBatchController", value = "/admin/deleteBatch")
public class SoftDeleteBatchController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String option = request.getParameter("option");
        String batchID = request.getParameter("batchID");

        BatchDAO batchDAO = new BatchDAO();
        boolean deleted = false;
        boolean unDeleted = false;

        if ("1".equals(option)) {
            deleted = batchDAO.softDeleteBatch(batchID);
        } else {
            unDeleted = batchDAO.unSoftDeleteBatch(batchID);
        }
        // Ghi log xóa hoặc khôi phục lô hàng
        HttpSession session = request.getSession(false);
        Object user = session != null ? session.getAttribute("customer") : null;
        Integer customerID = null;
        int role = 1; // Admin mặc định
        if (user instanceof entity.Customer customer) {
            customerID = customer.getId();
        }
        String ip = request.getRemoteAddr();

        String action = "1".equals(option) ? "DELETE_BATCH" : "RESTORE_BATCH";
        String message = ("1".equals(option) ? "Xóa mềm" : "Khôi phục") + " batch với ID: " + batchID
                + ", Thành công: " + (deleted || unDeleted);

        util.LogUtil.dangerous(action, message, customerID, role, ip, null);


        if (deleted || unDeleted) {
            // Xóa (lưu trữ) thành công, redirect về danh sách lô hàng
            response.sendRedirect(request.getContextPath() + "/admin/list-batches");
        } else {
            // Xóa thất bại, hiển thị lỗi
            request.setAttribute("error", "Xóa lô hàng thất bại.");
            request.getRequestDispatcher("/admin/list-batches").forward(request, response);
        }

    }
}

