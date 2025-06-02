package controller.admincontrol.image;

import dao.AboutUsPicDAO;
import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.*;
import util.LogUtil;

import java.io.IOException;

@WebServlet(name = "UpdateImgAbUs", value = "/admin/update-img-ab")
public class UpdateImgAbUs extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nameImg = request.getParameter("txt");
        String target = request.getParameter("txtVal");
        String choice = request.getParameter("choice");

        if ("1".equals(choice)) {
            request.setAttribute("nameImg", nameImg);
            request.setAttribute("target", target);
            request.getRequestDispatcher("editImg.jsp").forward(request, response);
        } else {
            // lấy thông tin để edit
            AboutUsPicDAO dao = new AboutUsPicDAO();
            dao.updateImg(target, nameImg);
            response.sendRedirect("all-aboutUs");
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
                "UPDATE_ABOUTUS_IMAGE",
                "Cập nhật ảnh About Us với target: " + target + ", tên ảnh mới: " + nameImg,
                customerID,
                1, // role admin
                request.getRemoteAddr()
        );


    }
}