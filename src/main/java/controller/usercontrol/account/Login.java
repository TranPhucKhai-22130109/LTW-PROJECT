package controller.usercontrol.account;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

import dao.CartDAO;
import dao.CustomerDAO;
import entity.*;
import helper.CartManagerDB;
import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.*;
import helper.MaHoaMK;
import org.json.JSONObject;
import util.LogUtil;


@WebServlet(name = "LoginControl", value = "/login")
public class Login extends HttpServlet {
    private static final String SECRET_KEY = "6LdjZ_wqAAAAAJn2ysFxobW5IL-0n7qcoVSWNRhg";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String code = request.getParameter("code");
        if (code != null) {
            String accessToken = GoogleLogin.getToken(code);
            GoogleAccount googleAcc = GoogleLogin.getUserInfo(accessToken);

            CustomerDAO dao = new CustomerDAO();
            Customer cus = dao.getUserByEmail(googleAcc.getEmail());

            if (cus == null) {
                cus = new Customer();
                cus.setEmail(googleAcc.getEmail());
                cus.setName(googleAcc.getName());
                dao.insertCustomerFromGoogle(cus);
                cus = dao.getUserByEmail(googleAcc.getEmail());
            }

            HttpSession session = request.getSession();
            session.setAttribute("customer", cus);
            response.sendRedirect("home");
        }


    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String emailLogin = request.getParameter("email-login");
        String passLogin = request.getParameter("password-login");

        String gRecaptchaResponse = request.getParameter("g-recaptcha-response");
        // Kiểm tra CAPTCHA
//        boolean isCaptchaValid = verifyRecaptcha(gRecaptchaResponse);
        boolean isCaptchaValid = true;

        if (isCaptchaValid) {
            // Xử lý đăng ký
            CustomerDAO cusDao = new CustomerDAO();
            Customer cus = new Customer();
            cus.setEmail(emailLogin);
            passLogin = MaHoaMK.toSHA1(passLogin);
            cus.setPass(passLogin);

            cus = cusDao.getUserByEmailPass(cus);

            if (cus == null) {
                request.setAttribute("error", "Sai tài khoản hoặc mật khẩu");
                request.getRequestDispatcher("forms/login.jsp").forward(request, response);
            } else {
                HttpSession session = request.getSession();
                session.setAttribute("customer", cus);
                cus.setPass("");
                //log đăng nhập
                LogUtil.info(
                        "LOGIN_SUCCESS",
                        "User " + cus.getEmail() + " logged in successfully",
                        cus.getId(),
                        cus.getRole(),
                        request.getRemoteAddr()
                );

                // LOAD USER CART || INSERT NEW CART
                CartDAO cartDao = new CartDAO();
                int cartId = cartDao.getCartIDByCusIDAndIsCheckout(cus.getId());
                Cart c;
                if (cartId != -1) {
                    c = new Cart();
                    List<CartItem> cartItem = cartDao.getCartItemByCartID(cartId);
                    for (CartItem item : cartItem) {
                        c.addCT(item);
                    }
                    session.setAttribute("cart", c);
                    response.sendRedirect("home");

                } else {
                    // ========= SAVE CART DB ========= //
                    /*
                        - Nếu trước đó mà user đã thêm sp vào cart mà không login
                        - Thì khi login sẽ lấy cart từ session của trình duyệt lúc đó
                          và lưu xuống db theo id của user
                     */
                    Cart cart = (Cart) session.getAttribute("cart");
                    if (cart == null) {
                        response.sendRedirect("home");
                        return;
                    }
                    CartManagerDB cartManagerDB = new CartManagerDB();
                    cartManagerDB.saveCartDB(request, cus);
                    response.sendRedirect("home");
                }
            }
        } else {
            request.setAttribute("error", "Chưa tích vào captcha");
            request.getRequestDispatcher("forms/login.jsp").forward(request, response);


        }

    }

    private boolean verifyRecaptcha(String gRecaptchaResponse) throws IOException {
        String url = "https://www.google.com/recaptcha/api/siteverify";
        String params = "secret=" + SECRET_KEY + "&response=" + gRecaptchaResponse;

        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);

        try (OutputStream out = conn.getOutputStream()) {
            out.write(params.getBytes());
            out.flush();
        }

        Scanner scanner = new Scanner(conn.getInputStream());
        String responseBody = scanner.useDelimiter("\\A").next();
        scanner.close();

        JSONObject json = new JSONObject(responseBody);
        return json.getBoolean("success");
    }


}