<%@ page import="entity.Cart" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="f" uri="jakarta.tags.fmt" %>
<%@ page isELIgnored="false" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Payment</title>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
    <link rel="stylesheet" href="assets/css/stylePayment.css"/>
    <link
            rel="stylesheet"
            href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css"
    />
</head>
<body style="background-color: #faf7f0">
<!-- Phần Thông tin giao hàng -->
<main id="checkout-container">
    <div class="col-right-payment">
        <!-- Navigation -->
        <div class="breadcrumb-frame">
            <nav class="breadcrumb">
                <h1>TÊN SHOP</h1>
                <a href="show-cart" style="color: #338dbc">Giỏ hàng</a>
                &gt;
                <span>Thanh toán</span>
            </nav>
        </div>
        <form action="create-order"
              method="post"
              class="column shipping-info"
              id="createOrder"
        >
            <div class="info-shipping">
                <!-- Phần Thông tin giao hàng -->
                <h2>THÔNG TIN GIAO HÀNG</h2>
                <h2 style="color:#dc3545"><%=request.getAttribute("error") != null ? request.getAttribute("error") : ""  %>
                </h2>
                <div class="form-group">
                    <label for="name">Họ và Tên</label>
                    <input type="text" id="name" name="name" placeholder="Họ và Tên" value="${customer.name}" required/>
                </div>
                <div class="email-phone">
                    <div class="form-group">
                        <label for="email">Email</label>
                        <input
                                readonly
                                id="email"
                                class="email"
                                type="email"
                                name="email"
                                placeholder="Email"
                                value="${customer.email}"
                                required
                        />
                    </div>
                    <div class="form-group">
                        <label for="numberPhone">Số điện thoại</label>
                        <input
                                class="numberPhone"
                                id="numberPhone"
                                type="tel"
                                name="numberPhone"
                                placeholder="Số Điện Thoại"
                                value="${customer.phone}"
                                required
                        />
                    </div>
                </div>

                <%-- Gọi api để lấy list quận, huyện --%>
                <div class="form-group">

                    <label for="province">Tỉnh / Thành phố</label>
                    <select type="text" name="province" id="province"
                            required="required">
                        <option value="" disabled selected>Chọn tỉnh / thành phố</option>
                    </select>

                    <label for="district">Quận / huyện</label>
                    <select type="text" name="district" id="district"
                            required="required">
                        <option value="" disabled selected>Chọn quận / huyện</option>
                    </select>

                    <label for="ward">Phường / Xã</label>
                    <select type="text" name="ward" id="ward"
                            required="required">
                        <option value="" disabled selected>Chọn phường / xã</option>
                    </select>

                    <label for="addressShipping">Địa chỉ giao hàng cụ thể </label>
                    <input type="text" name="addressShipping" id="addressShipping" placeholder="Số Nhà, Tên Đường"
                           required="required"/>
                </div>

                <%--         Order code GHN       --%>
                <input type="hidden" name="oder_code_ghn" id="oder_code_ghn"
                       value=""/>

                <div class="column payment-method">
                    <!-- Phần Phương thức thanh toán -->
                    <h2>PHƯƠNG THỨC THANH TOÁN</h2>
                    <div class="method">
                        <input type="radio" id="VNPay" name="payment" value="VNPay"/>
                        <div class="icon-pay">
                            <img src="assets/pic/vnpay.jpg" alt="Vnpay icon"/>
                        </div>
                        <label for="VNPay"> Thanh toán bằng VNPay </label>
                    </div>
                    <div class="method">
                        <input type="radio" id="cod" name="payment" value="COD"/>
                        <div class="icon-pay">
                            <img src="assets/pic/truck.svg" alt="COD icon"/>
                        </div>
                        <label for="cod"> Thanh toán khi nhận hàng (COD) </label>
                    </div>
                </div>
            </div>
            <div class="order-summary">

                <!-- Danh sách sản phẩm -->
                <table class="product-list">
                    <tbody class="body-list">
                    <c:forEach items="${sessionScope.cart.list}" var="cp">
                        <tr data-title="${cp.title}" data-quantity="${cp.quantity}" class="product-item">
                            <td class="item-img">
                                <div class="pic-img">
                                    <img
                                            src="<%=request.getContextPath()%>/assets/pic/products/${cp.img}"
                                            alt="Product Image"
                                            class="img-product"
                                    />
                                    <span class="quantity">${cp.quantity}</span>
                                </div>
                                <h2>${cp.title}</h2>
                            </td>

                            <td class="item-price">
                                <h2>

                                    <f:setLocale value="vi_VN"/>
                                    <f:formatNumber value="${cp.totalCt}" type="currency"/>
                                </h2>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>

                <!-- Voucher Section -->
                <div class="voucher-section">

                    <input
                            class="id-voucher"
                            name="voucher"
                            id="voucher"
                            type="text"
                            placeholder="Mã giảm giá"
                    />
                    <button id="voucher-btn" type="button" class="apply-voucher-btn"><p>Sử
                        dụng</p></button>

                </div>

                <!-- Tổng tiền -->
                <div class="provisional">
                    <div class="row">
                        <span>Tạm tính</span>
                        <% Cart c = (Cart) session.getAttribute("cart");%>
                        <input type="text" class="prePrice" name="prePrice" value="<%= c == null ? 0 : c.getTotal() %>"
                               readonly>
                    </div>
                    <div class="row">
                        <span>Phí vận chuyển</span>
                        <input class="fee" value="" readonly>
                    </div>
                </div>
                <div class="total-section">
                    <span>Tổng cộng</span>
                    <input type="text" class="finalPrice" name="finalPrice" readonly>
                </div>

            </div>
            <div class="finish">
                <a
                        href="show-cart"
                        class=""
                        style="color: #338dbc; text-decoration: none"
                >Giỏ hàng</a
                >
                <%--                <button type="submit" class="btn-total">Hoàn tất đơn hàng</button>--%>
                <%--                <button--%>
                <%--                        onclick="createOrderGHN()"--%>
                <%--                        type="button" class="btn-total" style="background-color: #00B58D">Test API tạo đơn--%>
                <%--                </button>--%>

                <button type="button" class="btn-total" onclick="handleSubmit()">Hoàn tất đơn hàng
                </button>

            </div>
        </form>
    </div>

</main>

<script src="<%=request.getContextPath()%>/assets/js/GHN.js"></script>
<script>
    <%-- Tạo cart item để truyền vào param item    --%>
    const cartItems = [];
    <c:forEach items="${sessionScope.cart.list}" var="cp">
    cartItems.push({
        name: "${cp.title}",
        code: "${cp.productID}",
        quantity: ${cp.quantity},
        weight: 300
    });
    </c:forEach>

    <%-- Chọn pthuc thanh toán--%>

    function handleSubmit() {
        const selectedPayment = document.querySelector('input[name="payment"]:checked');
        if (!selectedPayment) {
            alert("Vui lòng chọn phương thức thanh toán.");
            event.preventDefault();
            return;
        }

        // Chuyển hướng action nếu chọn VNPay
        if (selectedPayment.value === "VNPay") {
            let form = document.querySelector("form");
            form.action = "create-vnpay-order";
            form.submit()
        } else {
            createOrderGHN()
        }
    }

    <%-- Áp mã giảm giá--%>
    $("#voucher-btn").on("click", function () {
        let data = $("#voucher").val()
        let finalPrice = $("input[name='finalPrice']").val()
        console.log(data)
        $.ajax({
            url: "apply-voucher",
            method: "POST",
            data: {
                "voucher": data,
                "finalPrice": finalPrice
            },
            success: function (response) {
                console.log(response);
                $("input[name='finalPrice']").val(response.finalPrice)

                // $(".prePrice").val(response.finalPrice);

                function formatCurrency(value) {
                    return new Intl.NumberFormat('vi-VN', {style: 'currency', currency: 'VND'}).format(value);
                }
            },
        })
    })
</script>

</body>
</html>




