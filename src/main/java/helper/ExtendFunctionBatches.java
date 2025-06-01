package helper;

import context.JDBIContext;
import dao.ProductDAO;
import entity.Batch;
import entity.Product;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class ExtendFunctionBatches {
    public String generateBatchesNumber() {
        String dateStr = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")); // VD: 20240527
        int countToday = JDBIContext.getJdbi().withHandle(handle ->
                handle.createQuery("SELECT COUNT(*) FROM batches WHERE DATE(createdAt) = CURDATE()")
                        .mapTo(int.class)
                        .one()
        );

        int nextSerial = countToday + 1;
        //Ví dụ: 001, 002, ...
        String serialStr = String.format("%03d", nextSerial);
        return "LO-" + dateStr + "-" + serialStr;
    }

    // Import excel
    public String generateBatchesNumberExcel(int serialNumber) {
        String dateStr = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")); // VD: 20250530
        // Sử dụng serialNumber từ ExcelReader thay vì đếm từ DB
        String serialStr = String.format("%03d", serialNumber); // Ví dụ: 001, 002, ...
        return "LO-" + dateStr + "-" + serialStr; // output sẽ là vd: LO-20250530-001
    }

    // check exist productID
    public void checkProductIds(List<Batch> list_batches) {
        // Bước 1: Lấy danh sách productID từ list_batches
        List<Integer> productIds = list_batches.stream()
                .map(Batch::getProductID)
                .collect(Collectors.toList());

        // Bước 2: Lấy danh sách productID từ bảng products
        List<Product> existingProductIds = new ProductDAO().getAllProductID();

        // Bước 3: Tìm các productID không tồn tại trong bảng products
        List<Integer> nonExistingProductIds = productIds.stream()
                .filter(id -> !existingProductIds.contains(id))
                .distinct() // Loại bỏ trùng lặp nếu có
                .collect(Collectors.toList());

        // Bước 4: In thông báo
        if (nonExistingProductIds.isEmpty()) {
            System.out.println("All productIDs exist in the products table.");
        } else {
            System.out.println("The following productIDs do not exist in the products table: " + nonExistingProductIds);
        }
    }

}
