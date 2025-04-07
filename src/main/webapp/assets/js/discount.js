// hiển thị sản phẩm theo danh mục
document.getElementById("categorySelect").addEventListener("change", async function () {
    let cateId = document.getElementById("categorySelect").value;
    let url = `${contextPath}/admin/load-cate-discount`;

    let response = await fetch(url, {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded"
        },
        body: "cateId=" + encodeURIComponent(cateId),
    });

    let result = await response.json();
    try {
        if (result.isSuccess) {
            // Gọi hàm cập nhật giao diện
            updateProductTable(result.products, cateId);
        } else {
            alert(result.error);
        }
    } catch (error) {
        alert(error)
    }
});

// cập nhật bảng
function updateProductTable(products, cateId) {
    let table = $('#myTable').DataTable();
    table.clear(); // Xóa dữ liệu cũ
    products.forEach(product => {
        console.log(product.isDiscount)
        let checkedAttr = product.isDiscount == 1 ? 'checked' : '';
        table.row.add([
            `<input type="checkbox" class="product-checkbox" ${checkedAttr} value="${product.productID}" >`,
            product.productID,
            product.productName,
            product.productPrice.toLocaleString('Vi') + " đ",
            product.discountPrice.toLocaleString('Vi') + " đ",
            // cateId
        ]);
    });

    table.draw(); // Cập nhật bảng
}

// Sự kiện khi click vào checkbox "Chọn tất cả"
document.getElementById('selectAll').addEventListener('click', function (e) {
    e.stopPropagation(); // Ngăn không cho sự kiện lan ra ngoài (ví dụ sorting table)
    let checked = this.checked; // Trạng thái hiện tại của checkbox
    let checkboxes = document.querySelectorAll('.product-checkbox');
    checkboxes.forEach(function (checkbox) {
        checkbox.checked = checked; // Set tất cả cùng trạng thái
    });
});

// Sự kiện khi người dùng click vào từng checkbox sản phẩm
document.getElementById('productTable').addEventListener('change', function (e) {
    if (e.target.classList.contains('product-checkbox')) {
        if (!e.target.checked) {
            document.getElementById('selectAll').checked = false;
        }
    }
});

// hiển thị giao diện thêm mã mới
document.getElementById("discountSelect").addEventListener("change", function () {
    document.getElementById("customDiscountDiv").classList.toggle("d-none", this.value !== "custom");
});

// hiển thị nút xóa mã giảm giá
document.getElementById("discountSelect").addEventListener("change", function () {
    document.getElementById("delete").classList.remove("d-none");
});


// Khi click vào nút "Áp dụng giảm giá"
document.getElementById("applyDiscountBtn").addEventListener('click', async function () {
    let selectedProducts = [];

    // lấy id sản phẩm cần giảm
    let checkedBoxes = document.querySelectorAll('.product-checkbox:checked');
    checkedBoxes.forEach(function (checkbox) {
        selectedProducts.push(checkbox.value);
    });

    // lấy id mã giảm
    let discount = document.getElementById('discountSelect').value

    // send data về servlet
    let formData = new URLSearchParams();
    selectedProducts.forEach(p => formData.append("productIds", p));
    formData.append("discountId", discount);
    let url = `${contextPath}/admin/apply-discount`;

    try {
        let response = await fetch(url, {
            method: 'POST',
            headers: {
                "Content-Type": "application/x-www-form-urlencoded"
            },
            body: formData
        });

        let result = await response.json();
        console.log(result);
    } catch (err) {
        console.error("Lỗi khi gửi dữ liệu:", err);
    }
});

// thêm mã giảm mới
document.getElementById('customDiscountDiv').addEventListener('submit', async function (e) {
    e.preventDefault();
    let formData = new URLSearchParams(new FormData(this));
    let url = `${contextPath}/admin/add-discount`;

    let value = formData.get('discountValue');
    let type = formData.get('discountType');

    if (type === 'Percent' && value.trim().length > 2) {
        console.log('Phần trăm không được lớn hơn 2 chữ số');
        return;
    }
    for (let value of formData) {
        console.log(value)
    }
    // return;

    let response = await fetch(url, {
        method: 'POST',
        header: {
            "Content-Type": "application/x-www-form-urlencoded"
        },
        body: formData
    })
    let result = await response.json();
    try {
        if (result.isSuccess) {
            alert("Thêm mục giảm giá mới Thành công")
        } else {
            alert("Thêm mục giảm giá mới thất bại")
        }
    } catch (error) {
        alert(error)
    }
})

// Hiển thị bảng để chỉnh sửa
document.getElementById("editDiscountBtn").addEventListener("click", function () {
    let table = document.getElementById('myTable2_wrapper')
    let btn = document.getElementById('editDiscountBtn')
    //  lấy  CSS
    let
        currentDisplay = window.getComputedStyle(table).display;
    if (currentDisplay === "none") {
        table.style.display = "block";  // Hiển thị bảng
        btn.textContent = 'Quay lại'
    } else {
        table.style.display = "none";  // Ẩn bảng
        btn.textContent = 'Chỉnh sửa'
    }

});

// Xóa mã giảm
async function deleteDiscount(dID) {
    let url = `${contextPath}/admin/delete-discount?discountId=${dID}`;

    let response = await fetch(url, {
        method: "POST",
    });

    let result = await response.json();
    try {
        if (result.isSuccess) {
            alert('Xóa thành công')
        } else {
            alert('Xóa thất bại')
        }
    } catch (error) {
        alert(error)
    }
}


