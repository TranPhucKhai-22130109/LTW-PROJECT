package dao;


import context.JDBIContext;
import entity.HomePicture;
import entity.Product;
import org.jdbi.v3.core.Handle;

import java.util.List;

public class ProductDAO {
    public List<Product> getProduct() {
        return JDBIContext.getJdbi().withHandle(handle ->
                (handle.createQuery("select * from products").mapToBean(Product.class).list())
        );
    }

    public Product getProductByID(String ProID) {
        return JDBIContext.getJdbi().withHandle(handle ->
                (handle.createQuery("select * from products where productID = :productID")
                        .bind("productID", ProID)
                        .mapToBean(Product.class).findOne().orElse(null))
        );

    }

    public List<Product> getProductByCate(int CateID) {
        return JDBIContext.getJdbi().withHandle(handle ->
                (handle.createQuery("select * from products where cateID = :cateID")
                        .bind("cateID", CateID)
                        .mapToBean(Product.class).list())
        );
    }


    public List<Product> getProductSort(String choice) {
        return switch (choice) {
            case "1" -> JDBIContext.getJdbi().withHandle(handle ->
                    handle.createQuery("SELECT * FROM products ORDER BY productPrice ASC")
                            .mapToBean(Product.class)
                            .list()
            );
            case "2" -> JDBIContext.getJdbi().withHandle(handle ->
                    handle.createQuery("SELECT * FROM products ORDER BY productPrice DESC")
                            .mapToBean(Product.class)
                            .list()
            );
            case "3" -> JDBIContext.getJdbi().withHandle(handle ->
                    handle.createQuery("SELECT * FROM products ORDER BY productName ASC")
                            .mapToBean(Product.class)
                            .list()
            );
            case "4" -> JDBIContext.getJdbi().withHandle(handle ->
                    handle.createQuery("SELECT * FROM products ORDER BY productName DESC")
                            .mapToBean(Product.class)
                            .list()
            );
            default -> null;
        };
    }

    public List<Product> getBestSeller() {
        try (Handle handle = JDBIContext.getJdbi().open()) {
            return handle.createQuery("SELECT * FROM products ORDER BY productOrder DESC LIMIT 10;")
                    .mapToBean(Product.class).list();
        }
    }


    public List<Product> searchProductsByName(String nameP) {
        return JDBIContext.getJdbi().withHandle(handle ->
                handle.createQuery("SELECT * FROM products WHERE productName LIKE :name")
                        .bind("name", "%" + nameP + "%")
                        .mapToBean(Product.class)
                        .list());
    }

    // xóa sản phẩm dựa vào ID
    public int deleteProductById(int productID) {
        return JDBIContext.getJdbi().withHandle(handle -> (
                handle.createUpdate("DELETE FROM products WHERE productID =:productID")
                        .bind("productID", productID)
                        .execute())
        );
    }

    public int addProduct(Product product) {
        return JDBIContext.getJdbi().withHandle(handle ->
                handle.createUpdate("INSERT INTO products (productName, productDes, productPrice, productInventory, productOrder, " +
                                "productStock, productImage, cateID, shortDes) " +
                                "VALUES (:productName, :productDes, :productPrice, :productInventory, :productOrder, " +
                                ":productStock, :productImage, :cateID, :shortDes)")
                        .bind("productName", product.getProductName())
                        .bind("productDes", product.getProductDes())
                        .bind("productPrice", product.getProductPrice())
                        .bind("productInventory", product.getProductInventory())
                        .bind("productOrder", product.getProductOrder())
                        .bind("productStock", product.getProductStock())
                        .bind("productImage", product.getProductImage())
                        .bind("cateID", product.getCateID())
                        .bind("shortDes", product.getShortDes())
                        .execute()
        );
    }
    public int updateProduct(Product product) {
        return JDBIContext.getJdbi().withHandle(handle ->
                handle.createUpdate("Update products SET productName = :productName,\n" +
                                "                                productDes = :productDes,\n" +
                                "                                productPrice = :productPrice,\n" +
                                "                                productInventory = :productInventory,\n" +
                                "                                productOrder = :productOrder, \n" +
                                "                                productStock = :productStock, \n" +
                                "                               productImage = :productImage,\n" +
                                "                                cateID = :cateID,\n" +
                                "                               shortDes = :shortDes\n" +
                                "                                WHERE productID = :productID")
                        .bind("productID", product.getProductID()) // ID để xác định sản phẩm cần cập nhật
                        .bind("productName", product.getProductName())
                        .bind("productDes", product.getProductDes())
                        .bind("productPrice", product.getProductPrice())
                        .bind("productInventory", product.getProductInventory())
                        .bind("productOrder", product.getProductOrder())
                        .bind("productStock", product.getProductStock())
                        .bind("productImage", product.getProductImage())
                        .bind("cateID", product.getCateID())
                        .bind("shortDes", product.getShortDes())
                        .execute()
        );
    }



}
