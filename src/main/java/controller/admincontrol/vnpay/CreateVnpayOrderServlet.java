/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.admincontrol.vnpay;

import entity.Cart;
import entity.Customer;
import jakarta.servlet.annotation.WebServlet;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import util.LogUtil;


@WebServlet("/create-vnpay-order")
public class CreateVnpayOrderServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();

        Cart cart = (Cart) session.getAttribute("cart");
        // Logging
        Object obj = (session != null) ? session.getAttribute("customer") : null;
        int userID = (obj instanceof Customer customer) ? customer.getId() : -1;


        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String orderType = "other";
        String vnp_TxnRef = VnPayConfig.getRandomNumber(8);
        String vnp_IpAddr = VnPayConfig.getIpAddress(request);
        String vnp_TmnCode = VnPayConfig.vnp_TmnCode;
        String bankCode = request.getParameter("bankCode");

        // Lấy thông tin từ form checkout
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String phone = request.getParameter("numberPhone");
        String address = request.getParameter("addressShipping");
        String amountStr = request.getParameter("amount");
        String priceDiscountStr = request.getParameter("finalPrice");
        String paymentMethod = request.getParameter("payment");
        String prePrice = request.getParameter("prePrice");
        String province = request.getParameter("province");
        String ward = request.getParameter("ward");
        String district= request.getParameter("district");


        long amount = (long) Double.parseDouble(priceDiscountStr.equals("noV") ? amountStr : priceDiscountStr);

        // Log thông tin đơn hàng được tạo
        LogUtil.info("CREATE_VNPAY_ORDER",
                "Khởi tạo đơn hàng VNPAY: name=" + name + ", email=" + email + ", phone=" + phone + ", amount=" + amount,
                userID, 1, request.getRemoteAddr());


        String OrderID = UUID.randomUUID().toString();
        String vnp_ReturnUrl = "https://localhost:8088/Project-LTW/vnpay-return";

        // Lưu thông tin order vào session
        session.setAttribute("name", name);
        session.setAttribute("email", email);
        session.setAttribute("phone", phone);
        session.setAttribute("addressShipping", address);
        session.setAttribute("amount", amount);
        session.setAttribute("paymentMethod", paymentMethod);
        session.setAttribute("vnp_ReturnUrl", vnp_ReturnUrl);
        session.setAttribute("prePrice", prePrice);
        session.setAttribute("vnp_Amount", String.valueOf(amount * 100));
        session.setAttribute("province", province);
        session.setAttribute("ward", ward);
        session.setAttribute("district", district);

        // tổng giá cho đơn hàng

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount * 100)); // nhân 100 theo yêu cầu VNPay
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang: " + vnp_TxnRef);
        vnp_Params.put("vnp_OrderType", orderType);
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", VnPayConfig.vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        if (bankCode != null && !bankCode.isEmpty()) {
            vnp_Params.put("vnp_BankCode", bankCode);
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDateTime now = LocalDateTime.now();
        vnp_Params.put("vnp_CreateDate", now.format(formatter));

        // Log tham số VNPAY trước khi tạo hash và redirect
        LogUtil.debug("CREATE_VNPAY_ORDER",
                "Tham số VNPAY: " + vnp_Params.toString(),
                userID, 1, request.getRemoteAddr());


        // Build hash data
        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        for (String fieldName : fieldNames) {
            String fieldValue = vnp_Params.get(fieldName);
            if (fieldValue != null && !fieldValue.isEmpty()) {
                hashData.append(fieldName).append('=').append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII))
                        .append('=').append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                if (!fieldName.equals(fieldNames.get(fieldNames.size() - 1))) {
                    hashData.append('&');
                    query.append('&');
                }
            }
        }

        String vnp_SecureHash = VnPayConfig.hmacSHA512(VnPayConfig.vnp_HashSecret, hashData.toString());
        String paymentUrl = VnPayConfig.vnp_PayUrl + "?" + query + "&vnp_SecureHash=" + vnp_SecureHash;


        // Log URL thanh toán trước khi redirect
        LogUtil.info("CREATE_VNPAY_ORDER",
                "Redirect tới URL thanh toán: " + paymentUrl,
                userID, 1, request.getRemoteAddr());

        response.sendRedirect(paymentUrl);
    }
}





