package controller.admincontrol.feedback;

import dao.FeedbackDAO;
import entity.Customer;
import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.*;
import util.LogUtil;

import java.io.IOException;

@WebServlet(name = "DeleteFeedback", value = "/admin/delete-feedback")
public class DeleteFeedback extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int feedback_id = Integer.parseInt(request.getParameter("fID"));
        FeedbackDAO dao = new FeedbackDAO();
        boolean isSuccess = dao.deleteFeedback(feedback_id) > 0;

        // Lấy thông tin user admin từ session (giả sử chắc chắn có)
        HttpSession session = request.getSession(false);
        Customer customer = (Customer) session.getAttribute("customer");
        int customerID = customer.getId();
        int role = 1; // admin

        // Ghi log thao tác xóa feedback
        LogUtil.info(
                "DELETE_FEEDBACK",
                "Xóa phản hồi id=" + feedback_id + " " + (isSuccess ? "thành công" : "thất bại"),
                customerID,
                role,
                request.getRemoteAddr()
        );

        //  response.sendRedirect("all-feedback");
        response.setContentType("application/json");
        response.getWriter().write("{\"isSuccess\":" + isSuccess + "}");


    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}