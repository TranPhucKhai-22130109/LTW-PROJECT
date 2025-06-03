package controller.admincontrol.manufacturer;

import dao.ManufacturerDAO;
import entity.Manufacturer;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import util.LogUtil;

import java.io.IOException;

@WebServlet(name = "UpdateManufacturer", value = "/admin/update-manufacturer")
public class UpdateManu extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int manuID = Integer.parseInt(request.getParameter("manuID"));
        String manuName = request.getParameter("manuName");
        String brandOrigin = request.getParameter("brandOrigin");
        String manufactureLocation = request.getParameter("manufactureLocation");


        // Lấy admin từ session
        HttpSession session = request.getSession(false);
        Object user = (session != null) ? session.getAttribute("customer") : null;
        int customerID = -1;
        if (user instanceof entity.Customer customer) {
            customerID = customer.getId();
        }

        // Ghi log trước khi cập nhật
        LogUtil.info(
                "UPDATE_MANUFACTURER",
                "Cập nhật nhà sản xuất ID = " + manuID + " thành: Name = " + manuName + ", Origin = " + brandOrigin + ", Location = " + manufactureLocation,
                customerID,
                1, // role admin
                request.getRemoteAddr()
        );


        Manufacturer m = new Manufacturer();
        m.setManuID(manuID);
        m.setManuName(manuName);
        m.setBrandOrigin(brandOrigin);
        m.setManufactureLocation(manufactureLocation);

        ManufacturerDAO dao = new ManufacturerDAO();
        dao.updateManufacturer(m);

        response.sendRedirect(request.getContextPath() + "/admin/all-manufacturer");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

}
