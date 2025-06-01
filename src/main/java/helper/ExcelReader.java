package helper;

import context.JDBIContext;
import dao.ProductDAO;
import entity.Batch;
import entity.Product;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class ExcelReader {
    // ktra sp tồn tại chưa
    private boolean checkProductExists(int productID) {
        return JDBIContext.getJdbi().withHandle(handle ->
                handle.createQuery("SELECT productID FROM products WHERE productID = :productID")
                        .bind("productID", productID)
                        .mapTo(Integer.class)
                        .findOne()
                        .isPresent()
        );
    }

    // ĐỌC LÔ + SP
    public List<Batch> readBatchesFromExcel(String filePath) throws IOException {
        List<Batch> batches = new ArrayList<>();
        int serialCounter = 0;

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {
            // Đọc Sheet 1: Thông tin lô hàng
            Sheet batchSheet = workbook.getSheetAt(0);
            // Đọc Sheet 2: Thông tin sản phẩm
            Sheet productSheet = workbook.getSheetAt(1);
            boolean skipHeader = true;

            for (Row row : batchSheet) {
                if (skipHeader) {
                    skipHeader = false;
                    continue;
                }

                if (row.getCell(0) == null || row.getCell(0).getCellType() == CellType.BLANK) {
                    continue;
                }

                // Đọc productID
                int productID = 0;
                if (row.getCell(0) != null && row.getCell(0).getCellType() == CellType.NUMERIC) {
                    productID = (int) row.getCell(0).getNumericCellValue();
                }

                // Kiểm tra sản phẩm có tồn tại trong CSDL không
                if (!checkProductExists(productID)) {
                    // Nếu sản phẩm chưa tồn tại, đọc từ Sheet 2 và thêm vào CSDL
                    boolean productFound = false;
                    skipHeader = true; // Bỏ qua header của Sheet 2
                    for (Row productRow : productSheet) {
                        if (skipHeader) {
                            skipHeader = false;
                            continue;
                        }

                        // Đọc productID từ Sheet 2
                        int sheetProductID = 0;
                        if (productRow.getCell(0) != null && productRow.getCell(0).getCellType() == CellType.NUMERIC) {
                            sheetProductID = (int) productRow.getCell(0).getNumericCellValue();
                        }

                        // Nếu productID khớp, đọc thông tin sản phẩm
                        if (sheetProductID == productID) {
                            // Đọc productName
                            String productName = "";
                            if (productRow.getCell(1) != null && productRow.getCell(1).getCellType() == CellType.STRING) {
                                productName = productRow.getCell(1).getStringCellValue();
                            }

                            // Đọc productPrice
                            double productPrice = 0.0;
                            if (productRow.getCell(2) != null && productRow.getCell(2).getCellType() == CellType.NUMERIC) {
                                productPrice = productRow.getCell(2).getNumericCellValue();
                            }

                            // Đọc brand
                            String brand = "";
                            if (productRow.getCell(3) != null && productRow.getCell(3).getCellType() == CellType.STRING) {
                                brand = productRow.getCell(3).getStringCellValue();
                            }

                            // Đọc color
                            String color = "";
                            if (productRow.getCell(4) != null && productRow.getCell(4).getCellType() == CellType.STRING) {
                                color = productRow.getCell(4).getStringCellValue();
                            }

                            // Đọc material
                            String material = "";
                            if (productRow.getCell(5) != null && productRow.getCell(5).getCellType() == CellType.STRING) {
                                material = productRow.getCell(5).getStringCellValue();
                            }

                            // Đọc weight
                            double weight = 0.0;
                            if (productRow.getCell(6) != null && productRow.getCell(6).getCellType() == CellType.NUMERIC) {
                                weight = productRow.getCell(6).getNumericCellValue();
                            }

                            // Đọc dimensions
                            String dimensions = "";
                            if (productRow.getCell(7) != null && productRow.getCell(7).getCellType() == CellType.STRING) {
                                dimensions = productRow.getCell(7).getStringCellValue();
                            }


                            // Tạo đối tượng Product và thêm vào CSDL
                            Product product = new Product(productID, productName, productPrice, brand, color, material, weight, dimensions);
                            new ProductDAO().insertProduct(product);
                            productFound = true;
                            break; // Thoát vòng lặp khi tìm thấy sản phẩm
                        }
                    }

                    if (!productFound) {
                        System.err.println("ProductID " + productID + " not found in Sheet 2, skipping batch.");
                        continue; // Bỏ qua lô hàng nếu không tìm thấy sản phẩm
                    }
                }

                // Tiếp tục đọc thông tin lô hàng
                int quantity = 0;
                if (row.getCell(2) != null && row.getCell(2).getCellType() == CellType.NUMERIC) {
                    quantity = (int) row.getCell(2).getNumericCellValue();
                }

                double price = 0.0;
                if (row.getCell(3) != null && row.getCell(3).getCellType() == CellType.NUMERIC) {
                    price = row.getCell(3).getNumericCellValue();
                }

                LocalDateTime importDateExcel = LocalDateTime.now();
                if (row.getCell(4) != null && row.getCell(4).getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(row.getCell(4))) {
                    importDateExcel = row.getCell(4).getDateCellValue()
                            .toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                }

                int supplierID = 0;
                if (row.getCell(5) != null && row.getCell(5).getCellType() == CellType.NUMERIC) {
                    supplierID = (int) row.getCell(5).getNumericCellValue();
                }

                LocalDateTime createdAtExcel = LocalDateTime.now();
                if (row.getCell(6) != null && row.getCell(6).getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(row.getCell(6))) {
                    createdAtExcel = row.getCell(6).getDateCellValue()
                            .toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                }

                serialCounter++;
                String batchNumber = new ExtendFunctionBatches().generateBatchesNumberExcel(serialCounter);

                String importDate = String.valueOf(importDateExcel.toLocalDate());
                String createdAt = String.valueOf(createdAtExcel);

                byte isDeleted = 0;
                byte isUsed = 1;

                Batch batch = new Batch(productID, batchNumber, quantity, price, importDate, createdAt, supplierID, isDeleted, isUsed);
                batches.add(batch);
                System.out.println("Read batch: " + batch);
            }
        } catch (IOException e) {
            System.err.println("Error reading Excel file: " + e.getMessage());
            throw e;
        }
        return batches;
    }

    public static void main(String[] args) throws IOException {
        ExcelReader reader = new ExcelReader();
        System.out.println(reader.readBatchesFromExcel("D:\\WorkSpace_IJ\\Project-LTW\\Book1.xlsx"));
    }
}
