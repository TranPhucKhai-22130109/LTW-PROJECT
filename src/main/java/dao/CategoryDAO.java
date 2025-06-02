package dao;

import context.JDBIContext;
import entity.Category;
import java.util.List;

public class CategoryDAO {

    // Lấy hết danh mục
    public List<Category> getAllCate() {
        try {
            return JDBIContext.getJdbi().withHandle(handle ->
                    handle.createQuery("SELECT * FROM categories ORDER BY cateID DESC")
                            .mapToBean(Category.class)
                            .list()
            );
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error fetching all categories: " + e.getMessage());
        }
    }

    // Thêm danh mục
    public int insertCate(Category category) {
        try {
            if (category == null || category.getName() == null || category.getCateImg() == null) {
                throw new IllegalArgumentException("Category or its fields cannot be null");
            }
            return JDBIContext.getJdbi().withHandle(handle ->
                    handle.createUpdate("INSERT INTO categories (cateName, cateImg) VALUES (:cateName, :cateImg)")
                            .bind("cateName", category.getName())
                            .bind("cateImg", category.getCateImg())
                            .executeAndReturnGeneratedKeys("id")
                            .mapTo(int.class)
                            .one()
            );
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error inserting category: " + e.getMessage());
        }
    }

    // Xóa danh mục
    public int removeCate(String cID) {
        try {
            if (cID == null || cID.trim().isEmpty()) {
                throw new IllegalArgumentException("Category ID cannot be null or empty");
            }
            return JDBIContext.getJdbi().withHandle(handle ->
                    handle.createUpdate("DELETE FROM categories WHERE cateID = :cateID")
                            .bind("cateID", cID)
                            .execute()
            );
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error deleting category: " + e.getMessage());
        }
    }

    // Lấy 1 danh mục dựa vào ID
    public Category getCateByID(int cateID) {
        try {
            return JDBIContext.getJdbi().withHandle(handle ->
                    handle.createQuery("SELECT * FROM categories WHERE cateID = :cateID")
                            .bind("cateID", cateID)
                            .mapToBean(Category.class)
                            .findOne()
                            .orElse(null)
            );
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error fetching category by ID: " + e.getMessage());
        }
    }

    // Cập nhật danh mục dựa vào ID
    public int updateCate(int cateID, String cateName, String cateImg) {
        try {
            if (cateName == null || cateImg == null) {
                throw new IllegalArgumentException("Category name or image cannot be null");
            }
            return JDBIContext.getJdbi().withHandle(handle ->
                    handle.createUpdate("UPDATE categories SET cateName = :cateName, cateImg = :cateImg WHERE cateID = :cateID")
                            .bind("cateID", cateID)
                            .bind("cateName", cateName)
                            .bind("cateImg", cateImg)
                            .execute()
            );
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error updating category: " + e.getMessage());
        }
    }
}