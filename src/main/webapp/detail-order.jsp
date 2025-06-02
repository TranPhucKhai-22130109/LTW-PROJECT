<%@ page import="entity.Order" %>
<%@ page import="entity.OrderDetail" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="entity.Payment" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="f" uri="jakarta.tags.fmt" %>
<%@ page isELIgnored="false" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Sản Phẩm</title>
    <!-- Link FONTAWSOME -->
    <link
            rel="stylesheet"
            href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css"
    />

    <!-- Link my CSS -->
    <link rel="stylesheet" href="assets/css/styleProfile.css"/>
</head>
<body>
<span><%String orderCodeGHN = (String) request.getAttribute("orderCodeGHN"); %></span>
<input id="order_code" value="<%=orderCodeGHN%>">

<div class="Page-profile">
    <main class="container-profile">
        <div class="sidebar-profile">
            <nav class="breadcrumb" style="width: 500px">
                <a href="home" style="color: #338dbc">Trang chủ</a>
                &gt;
                <a href="load-profile" style="color: #338dbc; text-decoration: none">Thông tin cá nhân</a>
                &gt;
                <span>Chi tiết đơn hàng</span>
            </nav>
            <div class="user-info">

                <div class="username">
                    <span> Tài khoản của </span>
                    <h3 style="margin-right: 8px"><Strong>${cus.name}</Strong></h3>
                </div>
            </div>
            <ul class="menu">

                <li data-frame="frame3" class="menu-item active">Đơn hàng của tôi</li>
            </ul>
        </div>
        <div class="content">


            <!-- Order Details and Tracking Section -->
            <div id="frame3" class="frame order active">
                <div class="container-frameOrder">
                    <!-- Order Details and Summary -->
                    <div class="order-container" style="display: flex">
                        <!-- Order Details -->
                        <div class="order-details">
                            <div class="order-header">
                                <h2>Mã đơn hàng: ${order.orderID}</h2>
                                <p>Ngày tạo đơn: ${order.date} <span class="status">${order.status} </span></p>

                            </div>
                            <div class="product-list">
                                <table>
                                    <thead>
                                    <tr>
                                        <th>Sản phẩm</th>
                                        <th>Số lượng</th>
                                        <th>Giá</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:forEach items="${listOrd}" var="o">
                                        <tr>

                                            <td>
                                                <a href="detail?pid=${o.productID}" class="forward-img">
                                                    <img src="assets/pic/products/${o.productImage}"
                                                         alt="Women Shoes">${o.productName}
                                                </a>
                                            </td>
                                            <td>${o.quantity}</td>
                                            <td>
                                                <f:setLocale value="vi_VN"/>
                                                <f:formatNumber value="  ${o.price}" type="currency"/>

                                            </td>
                                        </tr>
                                    </c:forEach>

                                    </tbody>
                                </table>
                            </div>
                        </div>

                        <div class="order-summary">
                            <h3>Đơn hàng và Thanh toán</h3>
                            <div class="invoice-payment">
                                <!-- Hóa đơn -->
                                <div class="invoice">
                                    <h4>Hóa đơn</h4>
                                    <ul>
                                        <c:if test="${order != null}">
                                            <%--    <li>Tạm tính: <span> <f:setLocale value="vi_VN"/>--%>
                                            <%--   <f:formatNumber value=" ${order.totalPrice}" type="currency"/></span></li>--%>

                                            <li>Phí giao hàng: <span class="fee"></span></li>
                                            <li><strong>Tổng tiền:</strong>
                                                <span><strong>
                                                  <f:setLocale value="vi_VN"/>
                                            <f:formatNumber value=" ${order.totalPrice}" type="currency"/>
                                            </strong></span></li>
                                        </c:if>
                                    </ul>
                                </div>


                                <!-- Phương thức thanh toán -->
                                <div class="payment-method">
                                    <h4>Trạng thái đơn hàng</h4>
                                    <ul>
                                        <li>Phương thức: <span>${pay.payMethods}</span></li>
                                        <li>Khách hàng: <span>${sessionScope.customer.name}</span></li>
                                        <li>Trạng thái: <span class="status_ghn"></span></li>
                                    </ul>
                                </div>
                            </div>
                        </div>

                    </div>


                </div>
            </div>
        </div>
    </main>
</div>

<%--Biến toàn cục--%>
<script src="<%=request.getContextPath()%>/assets/js/GLOBAL_VAR.js"></script>

<script>
    document.addEventListener("DOMContentLoaded", () => {
            const menuItems = document.querySelectorAll(".menu-item");
            const frames = document.querySelectorAll(".frame");

            menuItems.forEach((item) => {
                item.addEventListener("click", () => {
                    // Xóa class active khỏi tất cả menu items
                    menuItems.forEach((i) => i.classList.remove("active"));

                    // Xóa class active khỏi tất cả frames
                    frames.forEach((frame) => frame.classList.remove("active"));

                    // Thêm class active vào menu item được click
                    item.classList.add("active");

                    // Hiển thị frame tương ứng
                    const targetFrame = document.getElementById(
                        item.getAttribute("data-frame")
                    );
                    if (targetFrame) {
                        targetFrame.classList.add("active");
                    }
                });
            });
        }
    );
</script>

<%--Fetch--%>
<script>
    // Fetch lấy trạng thái đơn
    async function getDetail() {
        let order_code = document.getElementById('order_code').value
        try {

            // Lấy thông trạng thái đơn
            let response = await fetch(`https://dev-online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/detail`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Token': token_api,
                },
                body: JSON.stringify({
                    'order_code': order_code
                })
            });
            let status = await response.json();
            let target_val = status.data.status;
            if (target_val == 'cancel') {
                target_val = 'Đã hủy'
            } else {
                target_val = 'Chờ lấy hàng'
            }

            document.querySelector('.status_ghn').textContent = target_val

        } catch (error) {
            console.error('Lỗi khi gọi API:', error);
        }
    }


    // Fetch lấy giá ship của đơn
    async function getCostShip() {
        let order_code = document.getElementById('order_code').value
        try {

            // Lấy thông trạng thái đơn
            let response = await fetch(`https://dev-online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/soc`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Token': token_api,
                },
                body: JSON.stringify({
                    'order_code': order_code
                })
            });
            let fee = await response.json();
            const mainService = fee.data.detail.main_service;
            console.log('Main service:', mainService);
            document.querySelector('.fee').textContent = mainService.toLocaleString('vi-VN') + '₫';

        } catch (error) {
            console.error('Lỗi khi gọi API:', error);
        }
    }

    getDetail()
    getCostShip()
</script>
</body>
</html>