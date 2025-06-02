<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="f" uri="jakarta.tags.fmt" %>
<%@ page isELIgnored="false" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Thanh toán thành công</title>
    <!-- Nhúng Bootstrap CSS từ CDN -->
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">

    <script src="<%=request.getContextPath()%>/assets/js/GHN.js"></script>

    <!-- Custom CSS cho trang -->
    <style>
        body {
            background-color: #f8f9fa;
            font-family: 'Arial', sans-serif;
        }

        .payment-container {
            background-color: #ffffff;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            border-radius: 8px;
            padding: 40px;
            text-align: center;
            max-width: 600px;
            margin: 80px auto;
        }

        .payment-container h2 {
            color: #28a745;
            font-size: 30px;
            font-weight: bold;
        }

        .payment-container .btn {
            background-color: #007bff;
            border-color: #007bff;
        }

        .payment-container .btn:hover {
            background-color: #0056b3;
            border-color: #0056b3;
        }

        .payment-logo {
            width: 80px;
            margin-bottom: 20px;
        }
    </style>
</head>
<body>
<%--<span>${sessionScope.name}</span>--%>
<%--<span>${sessionScope.email}</span>--%>
<%--<span>${sessionScope.phone}</span>--%>
<%--<span>${sessionScope.addressShipping}</span>--%>
<%--<span>${sessionScope.paymentMethod}</span>--%>
<%--<span>${sessionScope.prePrice}</span>--%>
<%--<span>${sessionScope.vnp_Amount /100}</span>--%>

<%--<span>${sessionScope.province}</span>--%>
<%--<span>${sessionScope.district}</span>--%>
<%--<span>${sessionScope.ward}</span>--%>
<div class="container" style="display: none">
    <div class="payment-container">
        <!-- Logo VNPay -->
        <img src="<%=request.getContextPath()%>/assets/pic/vnpay.jpg" alt="VNPay Logo" class="payment-logo">

        <h2>✅ Thanh toán thành công!</h2>
        <p>Cảm ơn bạn! Đơn hàng của bạn đã được thanh toán thành công qua VNPay.</p>
        <div class="mt-4">
            <a href="home" class="btn btn-lg btn-block">Quay về trang chủ</a>
        </div>
    </div>
    <div style="display: none">


        <form action="create-order"
              method="post"
              class="column shipping-info"
              id="createOrder"
        >
            <div class="info-shipping">

                <div class="form-group">
                    <input type="text" id="name" name="name" placeholder="Họ và Tên" value="${sessionScope.name}"
                           required/>
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
                                value="${sessionScope.email}"
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
                                value="${sessionScope.phone}"
                                required
                        />
                    </div>
                </div>

                <%-- Gọi api để lấy list quận, huyện --%>
                <div class="form-group">

                    <label for="province">Tỉnh / Thành phố</label>
                    <select type="text" name="province" id="province"
                            required="required">
                        <option value="${sessionScope.province}" selected></option>
                    </select>

                    <label for="district">Quận / huyện</label>
                    <select type="text" name="district" id="district"
                            required="required">
                        <option value="${sessionScope.district}" selected></option>
                    </select>

                    <label for="ward">Phường / Xã</label>
                    <select type="text" name="ward" id="ward"
                            required="required">
                        <option value="${sessionScope.ward}" selected>Chọn phường / xã</option>
                    </select>

                    <label for="addressShipping">Địa chỉ giao hàng cụ thể </label>
                    <input type="text" name="addressShipping" id="addressShipping" placeholder="Số Nhà, Tên Đường"
                           required="required" value="${sessionScope.addressShipping}"/>
                </div>

                <%--         Order code GHN       --%>
                <input type="hidden" name="oder_code_ghn" id="oder_code_ghn"
                       value=""/>

                <div class="column payment-method">
                    <!-- Phần Phương thức thanh toán -->
                    <h2>PHƯƠNG THỨC THANH TOÁN</h2>
                    <div class="method">
                        <input type="radio" id="VNPay" name="payment" value="VNPay" checked/>
                        <div class="icon-pay">
                            <img src="assets/pic/vnpay.jpg" alt="Vnpay icon"/>
                        </div>
                        <label for="VNPay"> Thanh toán bằng VNPay </label>
                    </div>

                </div>
            </div>

            <div class="order-summary">

                <!-- Tổng tiền -->
                <div class="total-section">
                    <input type="text" class="prePrice" value="${sessionScope.prePrice}" readonly>
                    <input type="text" class="finalPrice" name="finalPrice" value="${sessionScope.vnp_Amount/100}"
                           readonly>
                </div>
            </div>
        </form>
    </div>
</div>


<%--Biến toàn cục--%>
<script src="<%=request.getContextPath()%>/assets/js/GLOBAL_VAR.js"></script>

<%--Set item--%>
<script>
    const cartItems = [];
    <c:forEach items="${sessionScope.cart.list}" var="cp">
    cartItems.push({
        name: "${cp.title}",
        code: "${cp.productID}",
        quantity: ${cp.quantity},
        weight: 300
    });
    </c:forEach>
</script>

<%--Cập nhật vị trí --> call API   --%>
<script>
    async function getProvinceDistrictWardNames(provinceId, districtId, wardCode) {
        try {
            // Lấy thông tin tỉnh
            const provinceResponse = await fetch('https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/province', {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Token': token_api
                }
            });
            const provinceData = await provinceResponse.json();
            const province = provinceData.data.find(p => p.ProvinceID == provinceId);
            const provinceName = province ? province.ProvinceName : 'Không tìm thấy';

            // Lấy thông tin quận/huyện
            const districtResponse = await fetch(`https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/district`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Token': token_api
                },
                body: JSON.stringify({
                    'province_id': parseInt(provinceId)
                })
            });
            const districtData = await districtResponse.json();
            const district = districtData.data.find(d => d.DistrictID == districtId);
            const districtName = district ? district.DistrictName : 'Không tìm thấy';

            // Lấy thông tin phường/xã
            const wardResponse = await fetch(`https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/ward?district_id`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Token': token_api
                },
                body: JSON.stringify({
                    'district_id': parseInt(districtId)
                })
            });
            const wardData = await wardResponse.json();
            const ward = wardData.data.find(w => w.WardCode == wardCode);
            const wardName = ward ? ward.WardName : 'Không tìm thấy';

            console.log('Tỉnh:', provinceName);
            console.log('Quận/Huyện:', districtName);
            console.log('Phường/Xã:', wardName);

            // return {provinceName, districtName, wardName};
            document.getElementById('province').selectedOptions[0].textContent = provinceName
            document.getElementById('district').selectedOptions[0].textContent = districtName
            document.getElementById('ward').selectedOptions[0].textContent = wardName
        } catch (error) {
            console.error('Lỗi hàm getProvinceDistrictWardNames ');
        }
    }
</script>

<script>

    async function init() {
        await getProvinceDistrictWardNames('${sessionScope.province}', '${sessionScope.district}', '${sessionScope.ward}');
        await createOrderGHN();
    }

    init(); // gọi khi trang khởi tạo


</script>

<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.4/dist/umd/popper.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>

</body>
</html>
