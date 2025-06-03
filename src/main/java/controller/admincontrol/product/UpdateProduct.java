package controller.admincontrol.product;

import dao.ProductDAO;
import entity.Product;
import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.*;
import util.LogUtil;

import java.io.IOException;

@WebServlet(name = "UpdateProduct", value = "/admin/update-product")
public class UpdateProduct extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String productID = request.getParameter("id");
        String productName = request.getParameter("productName");
        String productDes = request.getParameter("productDes");
        double productPrice = Double.parseDouble(request.getParameter("productPrice").replaceAll("[^0-9]", ""));
        int productInventory = Integer.parseInt(request.getParameter("productInventory").replaceAll("[^0-9]", ""));
        int cateID = Integer.parseInt(request.getParameter("cateID"));
        String shortDes = request.getParameter("shortDes");

        String productImage = request.getParameter("productImage");
        String oldImg = request.getParameter("oldImg");

        int productOrder = Integer.parseInt(request.getParameter("productOrder").replaceAll("[^0-9]", ""));
        int productStock = Integer.parseInt(request.getParameter("productStock").replaceAll("[^0-9]", ""));

        ProductDAO productDAO = new ProductDAO();
        String imgUpdate;
        if (!"".equals(productImage)) {
            imgUpdate = productImage;
        } else {
            imgUpdate = oldImg;
        }

        // ktra slg san pham
        String msg = "";
        if (productOrder > productInventory || productStock > productInventory) {
            msg = "Số hàng đặt hoặc số hàng bán không được lớn hơn tổng hàng";
            request.setAttribute("msg", msg);
            request.getRequestDispatcher("EditProduct.jsp").forward(request, response);
            return;
        } else if (productOrder > productStock) {
            msg = "Số hàng đặt không được lớn hơn tổng hàng";
            request.setAttribute("msg", msg);
            request.getRequestDispatcher("EditProduct.jsp").forward(request, response);
            return;
        }

        // Lấy thông tin customer từ session
        HttpSession session = request.getSession(false);
        Object user = (session != null) ? session.getAttribute("customer") : null;
        int customerID = -1;
        if (user instanceof entity.Customer customer) {
            customerID = customer.getId();
        }

        // Ghi log trước khi cập nhật
        LogUtil.info(
                "UPDATE_PRODUCT",
                "Cập nhật sản phẩm có ID = " + productID,
                customerID,
                1, // role admin
                request.getRemoteAddr()
        );

        Product product = productDAO.getProductByID(productID);
        product.setProductName(productName);
        product.setProductDes(productDes);
        product.setProductPrice(productPrice);
        product.setCateID(cateID);
        product.setShortDes(shortDes);
        product.setProductImage(imgUpdate);

        System.out.println();


        productDAO.updateProduct(product);

        response.sendRedirect("load-pAdmin");
    }
}