package dao;


import context.JDBIContext;
import entity.HomePicture;
import entity.Product;
import entity.SubImgProduct;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.mapper.reflect.BeanMapper;

import java.util.ArrayList;
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

    public List<Product> getProductByCateLimit(int CateID) {
        return JDBIContext.getJdbi().withHandle(handle ->
                (handle.createQuery("select * from products where cateID = :cateID  order by productOrder desc limit 8")
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
                        .executeAndReturnGeneratedKeys("id") // Trả về khóa tự động tăng của cột `id`
                        .mapTo(int.class) // Map giá trị `id` sang kiểu `int`
                        .one()
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

    public List<Product> getProByPriceRange(int min, int max) {
        return JDBIContext.getJdbi().withHandle(handle ->
                handle.createQuery("SELECT * FROM products WHERE productPrice BETWEEN :min AND :max")
                        .bind("min", min)
                        .bind("max", max)
                        .map(BeanMapper.of(Product.class))
                        .list()
        );

    }

    public List<Product> getProductsByPriceAndCategory(int minPrice, int maxPrice, int cateID) {
        return JDBIContext.getJdbi().withHandle(handle ->
                handle.createQuery("SELECT * FROM products WHERE productPrice BETWEEN :min AND :max AND cateID = :cateID")
                        .bind("min", minPrice)
                        .bind("max", maxPrice)
                        .bind("cateID", cateID)
                        .mapToBean(Product.class)
                        .list()
        );
    }

    public int UpdateQuantity(int productID, int quantity) {
        String sql = "UPDATE products SET productStock = productStock - ?, productOrder=productOrder + ? WHERE productID = ?";
        return JDBIContext.getJdbi().withHandle(handle ->
                handle.createUpdate(sql)
                        .bind(0, quantity)
                        .bind(1, quantity)
                        .bind(2, productID)
                        .execute());
    }


    // lấy ra ds ảnh phụ
    public SubImgProduct getListSubImg(int pid) {
        String sql = "Select * from productsubimages WHERE productID = ?";
        return JDBIContext.getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .bind(0, pid)
                        .mapToBean(SubImgProduct.class)
                        .findOne().orElse(null));
    }


    public int insertSubImg(int pid, SubImgProduct subImgProduct) {
        String sql = "INSERT INTO productsubimages ( productID, subImg1, subImg2, subImg3, subImg4, subImg5, " +
                "subImg6, subImg7, subImg8, subImg9, subImg10) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        return JDBIContext.getJdbi().withHandle(handle ->
                handle.createUpdate(sql)
                        .bind(0, pid)
                        .bind(1, subImgProduct.getSubImg1())
                        .bind(2, subImgProduct.getSubImg2())
                        .bind(3, subImgProduct.getSubImg3())
                        .bind(4, subImgProduct.getSubImg4())
                        .bind(5, subImgProduct.getSubImg5())
                        .bind(6, subImgProduct.getSubImg6())
                        .bind(7, subImgProduct.getSubImg7())
                        .bind(8, subImgProduct.getSubImg8())
                        .bind(9, subImgProduct.getSubImg9())
                        .bind(10, subImgProduct.getSubImg10())
                        .execute());
    }

    public int updateSubImg(int pid, SubImgProduct subImgProduct) {
        String sql = "UPDATE productsubimages SET " +
                "subImg1 = ?, subImg2 = ?, subImg3 = ?, subImg4 = ?, subImg5 = ?, " +
                "subImg6 = ?, subImg7 = ?, subImg8 = ?, subImg9 = ?, subImg10 = ? " +
                "WHERE productID = ?";

        return JDBIContext.getJdbi().withHandle(handle ->
                handle.createUpdate(sql)
                        .bind(0, subImgProduct.getSubImg1())
                        .bind(1, subImgProduct.getSubImg2())
                        .bind(2, subImgProduct.getSubImg3())
                        .bind(3, subImgProduct.getSubImg4())
                        .bind(4, subImgProduct.getSubImg5())
                        .bind(5, subImgProduct.getSubImg6())
                        .bind(6, subImgProduct.getSubImg7())
                        .bind(7, subImgProduct.getSubImg8())
                        .bind(8, subImgProduct.getSubImg9())
                        .bind(9, subImgProduct.getSubImg10())
                        .bind(10, pid)
                        .execute());
    }

    public List<Product> getNewPro() {
        String sql = "SELECT * FROM products ORDER BY productID DESC LIMIT 10;\n";

        return JDBIContext.getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .mapToBean(Product.class)
                        .list()
        );
    }

    public static void main(String[] args) {
        ProductDAO dao= new ProductDAO();
        System.out.println(dao.getNewPro());
    }
}


