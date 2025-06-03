package controller.admincontrol.review;

import dao.ReviewDAO;
import entity.Customer;
import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.*;
import util.LogUtil;

import java.io.IOException;

@WebServlet(name = "ModifyReview", value = "/admin/modify-review")
public class ModifyReview extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String rID = request.getParameter("rID");
        int choice = Integer.parseInt(request.getParameter("choice"));
        boolean isSuccess = true;
        System.out.println("ID review la:" + rID);
        System.out.println("lua chon cua ban la:" + choice);

        isSuccess = switch (choice) {
            case 0 -> hiddenR(rID, choice);
            case 1 -> accept(rID, choice);
            case -1 -> remove(rID);
            default -> false;
        };

        // Ghi log nếu thành công
        if (isSuccess) {
            HttpSession session = request.getSession(false);
            Object obj = (session != null) ? session.getAttribute("customer") : null;
            int userID = (obj instanceof Customer customer) ? customer.getId() : -1;

            String action = switch (choice) {
                case 0 -> "Ẩn đánh giá";
                case 1 -> "Duyệt đánh giá";
                case -1 -> "Xóa đánh giá";
                default -> "Không xác định";
            };

            LogUtil.info("MODIFY_REVIEW", action + " (Review ID = " + rID + ")", userID, 1, request.getRemoteAddr());
        }


        response.setContentType("application/json");
        response.getWriter().write("{\"isSuccess\":" + isSuccess + ", \"choose\":" + choice + "}");
        response.getWriter().flush();


    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }


    // Duyệt
    public boolean accept(String rID, int choice) {
        ReviewDAO rd = new ReviewDAO();
        int row = rd.modifyReview(rID, choice);
        if (row >= 1) {
            return true;
        } else {
            return false;
        }
    }

    // Ẩn
    public boolean hiddenR(String rID, int choice) {
        ReviewDAO rd = new ReviewDAO();
        int row = rd.modifyReview(rID, choice);
        return row >= 1;
    }

    // Xóa
    public boolean remove(String rID) {
        ReviewDAO rd = new ReviewDAO();
        int row = rd.removeReview(rID);
        if (row >= 1) {
            return true;
        } else {
            return false;
        }
    }

}