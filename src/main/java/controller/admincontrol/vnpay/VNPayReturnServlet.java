
package controller.admincontrol.vnpay;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;

import java.io.IOException;
import java.util.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * @author CTT VNPAY
 */
@WebServlet(name = "VNPayReturnServlet", value = "/vnpay-return")
public class VNPayReturnServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Map<String, String> fields = new HashMap<>();
        for (Enumeration<String> params = request.getParameterNames(); params.hasMoreElements(); ) {
            String fieldName = params.nextElement();
            String fieldValue = request.getParameter(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                fields.put(fieldName, fieldValue);
            }
        }

        String responseCode = fields.get("vnp_ResponseCode");
        if ("00".equals(responseCode)) {
            System.out.println("Successsss");
            response.sendRedirect(request.getContextPath() + "/success.jsp");
        } else {
            // Thanh toán thất bại
            request.setAttribute("message", "Thanh toán thất bại. Mã lỗi: " + responseCode);
        }


        String vnp_SecureHash = fields.remove("vnp_SecureHash");
        String vnp_SecureHashType = fields.remove("vnp_SecureHashType");
        String signValue = VnPayConfig.hashAllFields(fields);

        //  CẦN XEM LẠI signValue và vnp_SecureHash khác nhau
//        if (signValue.equals(vnp_SecureHash)) {
//
//        } else {
//            request.setAttribute("message", "Dữ liệu không hợp lệ (sai chữ ký)");
//        }


    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        doPost(request, response);
    }
}




