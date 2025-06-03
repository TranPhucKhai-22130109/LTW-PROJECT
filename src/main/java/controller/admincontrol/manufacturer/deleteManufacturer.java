package controller.admincontrol.manufacturer;

import dao.ManufacturerDAO;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import util.LogUtil;

import java.io.IOException;

@WebServlet(name = "DeleteManufacturer", value = "/admin/delete-manufacturer")
public class deleteManufacturer extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("manuID"));

        // Lấy thông tin admin từ session
        HttpSession session = request.getSession(false);
        Object user = (session != null) ? session.getAttribute("customer") : null;
        int customerID = -1;
        if (user instanceof entity.Customer customer) {
            customerID = customer.getId();
        }

        // Ghi log trước khi xóa
        LogUtil.info(
                "DELETE_MANUFACTURER",
                "Xóa nhà sản xuất có ID = " + id,
                customerID,
                1, // role admin
                request.getRemoteAddr()
        );


        ManufacturerDAO dao = new ManufacturerDAO();
        dao.deleteManufacturer(id);

        response.sendRedirect(request.getContextPath() + "/admin/all-manufacturer");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
