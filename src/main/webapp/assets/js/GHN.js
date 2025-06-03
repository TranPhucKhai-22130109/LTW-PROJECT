const token_api = '463a453e-3eeb-11f0-9b81-222185cb68c8'
const shop_id = '196749'

// Chọn tỉnh/tp
async function getProvinces() {
    let url = 'https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/province';
    // Lấy thẻ province
    let provinceSelect = document.getElementById('province');


    try {
        const response = await fetch(url, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Token': token_api
            }
        });

        // Kiểm tra xem request có thành công không
        if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
        }

        // Chuyển response thành JSON
        let data = await response.json();
        let list_province = data.data

        // Xử lý dữ liệu trả về
        console.log('Danh sách tỉnh/thành phố:');
        list_province.forEach(province => {
            if (!province.ProvinceName.toLowerCase().includes('test') && !/\d/.test(province.ProvinceName)) {
                let option = document.createElement('option');
                option.value = province.ProvinceID; // Gán ProvinceID làm giá trị
                option.textContent = province.ProvinceName; // Hiển thị tên tỉnh
                provinceSelect.appendChild(option);
            }
        });

        // Thêm sự kiện change để lấy ProvinceID
        provinceSelect.addEventListener('change', (event) => {
            let province_id = event.target.value;
            if (province_id) {
                getDistrict(province_id); // Gọi hàm khác với ProvinceID
            }
        });
    } catch (error) {
        console.error('Lỗi khi gọi API:', error.message);
    }
}

// Chọn quận/huyện
async function getDistrict(province_id) {
    console.log("getDistrict: " + province_id)
    let url = 'https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/district';

    // Lấy thẻ district
    let districtSelect = document.getElementById('district');
    districtSelect.innerHTML = '<option value="" disabled selected>Chọn quận / huyện</option>';

    try {
        const response = await fetch(url, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Token': token_api
            },
            body: JSON.stringify({province_id: parseInt(province_id)})
        });

        // Kiểm tra xem request có thành công không
        if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
        }

        // Chuyển response thành JSON
        let data = await response.json();
        let list_district = data.data

        // Xử lý dữ liệu trả về
        console.log('Danh sách quận/huyện:');
        list_district.forEach(district => {
            let option = document.createElement('option');
            option.value = district.DistrictID; // Gán DistrictID làm giá trị
            option.textContent = district.DistrictName; // Hiển thị tên quận/huyện
            districtSelect.appendChild(option);

        });

        // Thêm sự kiện change để lấy WardID
        districtSelect.addEventListener('change', (event) => {
            let district_id = event.target.value;
            if (district_id) {
                getWard(district_id); // Gọi hàm khác với ProvinceID
            }
        });

    } catch (error) {
        console.error('Lỗi khi gọi API:', error.message);
    }
}

// Show phường/xã dựa trên district_id
async function getWard(district_id) {
    let url = 'https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/ward?district_id'

    let wardSelect = document.getElementById('ward');
    // Clear sạch dữ liệu trong select trước khi thêm mới
    wardSelect.innerHTML = '<option value="" disabled selected>Chọn phường / xã</option>';

    try {
        const response = await fetch(url, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Token': token_api
            },
            body: JSON.stringify({district_id: parseInt(district_id)})
        });

        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(`HTTP error! Status: ${response.status}, Message: ${errorData.message || 'Unknown error'}`);
        }

        const data = await response.json();
        const list_ward = data.data;

        console.log('Danh sách phường/xã:', list_ward);
        list_ward.forEach(ward => {
            const option = document.createElement('option');
            option.value = ward.WardCode;
            option.textContent = ward.WardName;
            wardSelect.appendChild(option);
        });

        // Thêm sự kiện change để 'tính ship'
        wardSelect.addEventListener('change', (event) => {
            updateAddress()
            calculateShip();
        });

    } catch (error) {
        console.error('Lỗi khi gọi API:', error.message);
    }
}

// Hàm tính ship
async function calculateShip() {


    let to_district_id = document.getElementById('district').value
    let to_ward_code = document.getElementById('ward').value

    if (to_district_id === '' || to_ward_code === '') {
        alert('bạn chưa nhập vị trí')
        return;
    } else {
        console.log(to_district_id + " " + to_ward_code)

    }
    let url = 'https://dev-online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/fee'
    // prepare raw
    const raw_data = {
        service_type_id: 2,
        to_district_id: parseInt(to_district_id),
        to_ward_code: to_ward_code,
        weight: 300,
        coupon: null,

    };
    try {
        const response = await fetch(url, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Token': token_api,
                'ShopID': shop_id
            },
            body: JSON.stringify(raw_data)
        });

        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(`HTTP error! Status: ${response.status}, Message: ${errorData.message || 'Unknown error'}`);
        }

        const data = await response.json();
        console.log(`Phí dịch vụ: ${data.data.service_fee} VNĐ`);
        document.querySelector(".fee").value = data.data.service_fee;
        document.querySelector(".finalPrice").value = parseInt(data.data.service_fee) +
            parseInt(document.querySelector(".prePrice").value);
    } catch (error) {
        console.error('Lỗi khi gọi API tính phí:', error.message);
        alert('Không thể tính phí vận chuyển. Vui lòng thử lại sau.');
    }
}

// Tạo đơn (GHN)
async function createOrderGHN(rs) {
    let url = 'https://dev-online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/create'
    let insurance_value = document.querySelector(".prePrice").value

    // Lấy nội dung của option (vị trí) được chọn
    let to_province_name = document.getElementById('province').selectedOptions[0].textContent;
    let to_district_name = document.getElementById('district').selectedOptions[0].textContent;
    let to_ward_name = document.getElementById('ward').selectedOptions[0].textContent;


    // lấy phương thức thanh toán
    const selectedPayment = document.querySelector('input[name="payment"]:checked');

    // check thanh toán (vnpay || cod) ==> set phí thu hộ, payment_type_id
    let cod_Amount, payment_type_id;
    if (selectedPayment.value === 'COD') {
        cod_Amount = document.querySelector(".finalPrice").value
        payment_type_id = 2
    } else {
        cod_Amount = 0;
        payment_type_id = 1
    }

    // prepare raw
    const raw_data = {
        payment_type_id: parseInt(payment_type_id), // VNpay = 1
        note: "",
        required_note: "KHONGCHOXEMHANG",
        to_name: "KH Test1", // lấy từ session.customer.name
        to_phone: "0987654321", // lấy từ session.customer.phone
        to_address: `TEST, ${to_ward_name}, ${to_district_name},${to_province_name}, Vietnam`, // lấy từ session.customer.phone
        to_ward_name: to_ward_name,
        to_district_name: to_district_name,
        to_province_name: to_province_name,
        cod_amount: parseInt(cod_Amount), // nếu Vnpay thì = 0
        content: "",
        length: 12,
        width: 12,
        height: 12,
        weight: 1200,
        insurance_value: parseInt(insurance_value),
        service_type_id: 2,
        coupon: null,
        items: cartItems
    };

    // console.log(raw_data)
    // return;

    // Fetch
    try {
        const response = await fetch(url, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'ShopID': shop_id,
                'Token': token_api,
            },
            body: JSON.stringify(raw_data)
        });

        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(`HTTP error! Status: ${response.status}, Message: ${errorData.message || 'Unknown error'}`);
        }

        const data = await response.json();
        console.log('Order code:\n', data.data.order_code);
        console.log('Tất cả giá trị trong data:\n', printObject(data.data));
        document.getElementById('oder_code_ghn').value = data.data.order_code
        document.getElementById('createOrder').submit()


    } catch (error) {
        console.error('Lỗi khi gọi API :', error.message);
        alert('Có lỗi. Vui lòng thử lại sau.');
    }
}

// Hàm đệ quy để in tất cả giá trị trong một đối tượng
function printObject(obj, prefix = '') {
    let output = '';
    for (const [key, value] of Object.entries(obj)) {
        if (typeof value === 'object' && value !== null) {
            // Nếu giá trị là một đối tượng, gọi đệ quy
            output += `${prefix}${key}:\n`;
            output += printObject(value, prefix + '  ');
        } else {
            // Nếu giá trị là giá trị đơn giản (string, number, v.v.), in ra
            output += `${prefix}${key}: ${value}\n`;
        }
    }
    return output;
}

// hàm cập nhật địa chỉ
function updateAddress() {
    let addressSHipping = document.getElementById('addressShipping');

    addressSHipping.value = '';
    let to_province_name = document.getElementById('province').selectedOptions[0].textContent;
    let to_district_name = document.getElementById('district').selectedOptions[0].textContent;
    let to_ward_name = document.getElementById('ward').selectedOptions[0].textContent;
    addressSHipping.value = `${to_ward_name}, ${to_district_name}, ${to_province_name}`;
}

// Gọi hàm để chọn địa chỉ nhận hàng
getProvinces();