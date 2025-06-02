package controller.admincontrol.batches;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dao.BatchDAO;
import entity.Batch;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Date;

@WebServlet(name = "EditBatchController", value = "/admin/edit-batches")
public class EditBatchController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Batch batch = new Batch();
        batch.setBatchID(Integer.parseInt(request.getParameter("batchesId")));
        batch.setProductID(Integer.parseInt(request.getParameter("productId")));
        batch.setQuantity(Integer.parseInt(request.getParameter("quantity")));
        batch.setPrice(Double.parseDouble(request.getParameter("price")));
        String supplierID = request.getParameter("supplierID");
        if (supplierID != null && !supplierID.trim().isEmpty()) {
            batch.setSupplierID(Integer.parseInt(supplierID));
        }

        boolean isSuccess = new BatchDAO().updateBatch(batch);

        // Ghi log chỉnh sửa batch
        HttpSession session = request.getSession(false);
        Object user = session != null ? session.getAttribute("customer") : null;
        Integer customerID = null;
        int role = 1; // Admin mặc định
        if (user instanceof entity.Customer customer) {
            customerID = customer.getId();
        }
        String ip = request.getRemoteAddr();

        util.LogUtil.info(
                "EDIT_BATCH",
                "Chỉnh sửa batch. ID: " + batch.getBatchID()
                        + ", Sản phẩm ID: " + batch.getProductID()
                        + ", Số lượng: " + batch.getQuantity()
                        + ", Giá: " + batch.getPrice()
                        + ", Nhà cung cấp ID: " + batch.getSupplierID()
                        + ", Thành công: " + isSuccess,
                customerID,
                role,
                ip
        );


        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("isSuccess", isSuccess);

        Gson gson = new Gson();
        String json = gson.toJson(jsonObject);
        response.getWriter().write(json);

    }
}
