<%@ page import="jakarta.servlet.http.HttpSession" %>
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
    <title>Giỏ hàng</title>
    <!-- Link BOOTSRAP -->
    <link
            href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
            rel="stylesheet"
            integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
            crossorigin="anonymous"
    />
    <script
            src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
            crossorigin="anonymous"
    ></script>
    <!-- Link BOOTSRAP -->

    <!-- Link jQuery -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
    <link
            rel="stylesheet"
            href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css"
    />
    <link rel="stylesheet" href="assets/css/styleShoppingCart.css"/>
</head>
<body>

<jsp:include page="header.jsp"></jsp:include>

<main id="shopping-cart-container">

    <!-- Shopping Cart Table -->
    <div class="main-cart">

        <div class="cart-table left">
            <div class="cart-header">
                <div class="d-flex gap-1">
                    <input type="checkbox" onclick="checkAll(this)">Chọn tất cả
                </div>
                <span>Sản phẩm</span>
                <span>Số lượng</span>
                <span>Tổng tiền (VND)</span>
            </div>
            <c:forEach items="${sessionScope.cart.list}" var="cp">
                <!-- Product Rows -->

                <div class="cart-item" data-id="${cp.productID}">
                    <div>
                        <input type="checkbox" class="check-item"
                               value="${cp.productID}"
                               data-quantity="${cp.quantity}"
                               data-price="${cp.totalCt}"
                        >
                    </div>
                    <div class="product-info">
                        <a href="detail?pid=${cp.productID}">
                            <img src="<%=request.getContextPath()%>/assets/pic/products/${cp.img}" alt="Sản phẩm 1"></a>
                        <div>
                            <p>${cp.title}</p>
                            <p class="price number-format">
                                    <f:setLocale value="vi_VN"/>
                                    <f:formatNumber value="${cp.price}" type="currency"/>

                        </div>
                    </div>

                    <div class="quantity" style="width: 100px">
                        <input onblur="updateCart(this)" name="quantity" id="" class="p-quantity"
                               data-id="${cp.productID}" value="${cp.quantity}" autocomplete="off"/>
                    </div>

                    <div class="total-price number-format" id="total-price-${cp.productID}">
                        <f:setLocale value="vi_VN"/>
                        <f:formatNumber value="${cp.totalCt}" type="currency"/>

                    </div>

                    <div class="remove-item">
                        <div class="i-container">
                            <a id="remove-cart" data-id="${cp.productID}" onclick="removeCart(this)">
                                <i class="fa fa-times"></i>
                            </a>
                        </div>
                    </div>
                </div>
            </c:forEach>
            <!-- Cart Controls -->
            <div class="cart-controls">
                <a class="continue-shopping" href="products">Tiếp tục mua sắm</a>
                <!-- <button class="update-cart">Update Cart</button> -->
            </div>
        </div>

        <%--Tổng giỏ hàng (số lượng + giá)--%>
        <form action="check-out.jsp" class="form-checkout">
            <div class="cart-summary">
                <h2>Tổng đơn</h2>

                <div class="cart-total-row">
                    <span>Tổng sản phẩm:</span>
                    <span class="subtotalQuantity">
                         <% Cart c = (Cart) session.getAttribute("cart");%>
                         <%= c == null ? "" : c.getTotalQuantity()%>
                    </span>
                </div>

                <div class="cart-total-row">
                    <span>Tổng tiền:</span>
                    <span class="total-cart">
<%--                         <c:set var="balance" value="<%= c == null ? 0 : c.getTotal() %>"/>--%>
<%--                         <f:setLocale value="vi_VN"/>--%>
<%--                         <f:formatNumber value="${balance}" type="currency"/>--%>
                    </span>
                </div>
                <%String msgP = (String) request.getAttribute("msgP");%>
                <%String msgA = (String) request.getAttribute("msgA");%>
                <h6 class="text-danger fw-bold"><%= msgP == null ? "" : msgP%>
                </h6>
                <a href="forms/signup-login.jsp" class="text-danger fw-bold"><%= msgA == null ? "" : msgA%>
                </a>
                <a type="button" href="show-pre-order" class="checkout-button text-center">
                    Tiến hành thanh toán
                </a>
            </div>
        </form>
    </div>


</main>
<jsp:include page="footer.jsp"></jsp:include>

<%--AJAX CART--%>
<script src="assets/js/cart.js"></script>
<script>
    function updateSummary() {
        const checkboxes = document.querySelectorAll('.check-item');
        let totalQuantity = 0;
        let totalPrice = 0;

        checkboxes.forEach(cb => {
            if (cb.checked) {
                const quantity = parseInt(cb.getAttribute('data-quantity'));
                const price = parseFloat(cb.getAttribute('data-price'));
                totalQuantity += quantity;
                totalPrice += price;
            }
        });

        // Gán lại vào thẻ hiển thị
        document.querySelector('.subtotalQuantity').innerText = totalQuantity;
        document.querySelector('.total-cart').innerText = totalPrice.toLocaleString('vi-VN') + " ₫";
    }

    // Gắn sự kiện change cho tất cả checkbox
    document.querySelectorAll('.check-item').forEach(cb => {
        cb.addEventListener('change', updateSummary);
    });

    // Gắn cho "chọn tất cả"
    function checkAll(source) {
        const checkboxes = document.querySelectorAll('.check-item');
        checkboxes.forEach(cb => {
            cb.checked = source.checked;
        });
        updateSummary();
    }

    // Khởi động tính tổng ban đầu (nếu cần)
    updateSummary();
</script>

</body>
</html>