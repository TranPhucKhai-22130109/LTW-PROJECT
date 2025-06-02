package controller.admincontrol.batches;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dao.BatchDAO;
import entity.Batch;
import helper.ExtendFunctionBatches;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDateTime;

@WebServlet(name = "AddBatchController", value = "/admin/add-batches")
public class AddBatchController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Batch batch = new Batch();

        // tạo số lô (example: LO-20250529)
        ExtendFunctionBatches gen = new ExtendFunctionBatches();
        String batchesNumber = gen.generateBatchesNumber();

        batch.setProductID(Integer.parseInt(request.getParameter("productId")));
        batch.setBatchNumber(batchesNumber);
        batch.setQuantity(Integer.parseInt(request.getParameter("quantity")));
        batch.setPrice(Double.parseDouble(request.getParameter("price")));
        batch.setSupplierID(Integer.parseInt(request.getParameter("supplierID")));
        batch.setIsUsed((byte) 1);

        // Get date and time
        LocalDateTime dateTime = LocalDateTime.now();
        batch.setImportDate(String.valueOf(dateTime.toLocalDate()));

        // Call addBatch function
        boolean isSuccess = new BatchDAO().addBatch(batch);


        // Ghi log: thêm một batch mới
        HttpSession session = request.getSession(false);
        Object user = session != null ? session.getAttribute("customer") : null;
        Integer customerID = null;
        int role = 1; // Admin mặc định
        if (user instanceof entity.Customer customer) {
            customerID = customer.getId();
        }
        String ip = request.getRemoteAddr();

        util.LogUtil.info(
                "ADD_BATCH_MANUAL",
                "Thêm batch thủ công. Số lô: " + batch.getBatchNumber()
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
