package controller.admincontrol.image;

import dao.HomePictureDAO;
import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.*;
import util.LogUtil;

import java.io.IOException;

@WebServlet(name = "UpdateHomeImg", value = "/admin/update-homePic")
public class UpdateHomeImg extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String target = request.getParameter("target");
        String value = request.getParameter("value");
        String choice = request.getParameter("choice");

        if ("1".equals(choice)) {
            request.setAttribute("target", target);
            request.setAttribute("value", value);
            request.getRequestDispatcher("editHomeImg.jsp").forward(request, response);
        } else {
            // lấy thông tin để edit
            HomePictureDAO dao = new HomePictureDAO();
            dao.updateImg(target,value);
            response.sendRedirect("all-homePic");
        }
        // Lấy thông tin admin từ session
        HttpSession session = request.getSession(false);
        Object user = (session != null) ? session.getAttribute("customer") : null;
        int customerID = -1;
        if (user instanceof entity.Customer customer) {
            customerID = customer.getId();
        }

// Ghi log
        LogUtil.info(
                "UPDATE_HOME_IMAGE",
                "Cập nhật ảnh trang chủ cho target: " + target + ", giá trị mới: " + value,
                customerID,
                1, // role admin
                request.getRemoteAddr()
        );

    }
}