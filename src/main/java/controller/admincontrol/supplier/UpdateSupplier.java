package controller.admincontrol.supplier;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dao.SupplierDAO;
import entity.Customer;
import entity.Supplier;
import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.*;
import util.LogUtil;

import java.io.IOException;
import java.time.LocalDateTime;

@WebServlet(name = "UpdateSupplier", value = "/admin/update-supplier")
public class UpdateSupplier extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Supplier supplier = new SupplierDAO().getSupplierByID(Integer.parseInt(request.getParameter("supplierId")));
        supplier.setSupplierName(request.getParameter("supplierName"));
        supplier.setContactInfo(request.getParameter("supplierInfo"));
        supplier.setAddress(request.getParameter("supplierAddress"));
        String supplierID = request.getParameter("supplierId");

        // Get date and time
        LocalDateTime dateTime = LocalDateTime.now();
        supplier.setCreatedAt(String.valueOf(dateTime));
        supplier.setUpdatedAt(String.valueOf(dateTime));

        boolean isSuccess = new SupplierDAO().updateSupplier(supplier);


        // Logging
        HttpSession session = request.getSession(false);
        Object obj = (session != null) ? session.getAttribute("customer") : null;
        int userID = (obj instanceof Customer customer) ? customer.getId() : -1;
        LogUtil.info("UPDATE_SUPPLIER", "Cập nhật nhà cung cấp với ID: " + supplierID, userID, 2, request.getRemoteAddr());

        // Trả kết quả

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("isSuccess", isSuccess);

        Gson gson = new Gson();
        String json = gson.toJson(jsonObject);
        response.getWriter().write(json);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}