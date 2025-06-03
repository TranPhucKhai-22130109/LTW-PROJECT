package controller.usercontrol.order;

import dao.*;
import entity.*;
import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;

@WebServlet(name = "CreateOrder", value = "/create-order")
public class CreateOrder extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Customer customer = (Customer) session.getAttribute("customer");

        // info khách hàng
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String phone = request.getParameter("numberPhone");
        String addressShipping = request.getParameter("addressShipping");

        String payment = request.getParameter("payment");
        String statusOrder = "ready_to_pick";

        String oder_code_ghn = request.getParameter("oder_code_ghn");

        // lấy ra phương thức thanh toán
        if ("VNPay".equalsIgnoreCase(payment)) {
            payment = "VNPAY";
        } else {
            payment = "COD";
        }

        customer.setName(name);
        customer.setPhone(phone);
        customer.setAddressShipping(addressShipping);

        String role = String.valueOf(customer.getRole());
        CustomerDAO cusDao = new CustomerDAO();

        cusDao.updateUser(name, email, phone, customer.getAddress(), addressShipping, role);
        session.setAttribute("customer", customer);
        int id = customer.getId();

        Cart cart = (Cart) session.getAttribute("cart");

        // lấy tổng slg đơn
        int total = cart.getTotalQuantity();

        // lấy giá tổng đơn
        double totalPrice = 0;
        if (request.getParameter("vnp_Amount") != null) {
            totalPrice = Double.parseDouble(request.getParameter("vnp_Amount"));
        } else {
            totalPrice = Double.parseDouble(request.getParameter("finalPrice"));
        }

        Date date = Date.valueOf(LocalDate.now());

        // tạo order mới
        OrderDAO ordDao = new OrderDAO();
        int ordID = ordDao.createOrder(id, totalPrice, statusOrder, addressShipping, total, date, oder_code_ghn);

        // tạo trường cho bảng quá trình vận chuyển
        ShippingDAO shippingDAO = new ShippingDAO();
        Shipping shipping = new Shipping();
        shipping.setOrderID(ordID);
        shipping.setStatus("đang chờ");
        shippingDAO.insertShipping(shipping);

        // bảng thanh toán
        PaymentDAO paymentDAO = new PaymentDAO();
        paymentDAO.insertPayment(ordID, payment);


        System.out.println(ordID);
        request.setAttribute("ordID", ordID);
        response.sendRedirect("create-ord-detail?ordID=" + ordID); // Gửi qua URL


    }
}