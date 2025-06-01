<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page isELIgnored="false" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Quản lý lô hàng</title>
    <!-- Bootstrap  -->
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

    <!-- jQuery  -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>

    <!-- Select2 -->
    <link href="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/css/select2.min.css" rel="stylesheet"/>
    <script src="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/js/select2.min.js"></script>

    <!-- Font Awesome (cho các icon) -->
    <link
            rel="stylesheet"
            href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css"
    />
    <link
            rel="stylesheet"
            href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.1/css/all.min.css"
            integrity="sha512-5Hs3dF2AEPkpNAR7UiOHba+lRSJNeM2ECkwxUIxC1Q/FLycGTbNapWXB4tP889k5T5Ju8fs4b1P5z/iB4nMfSQ=="
            crossorigin="anonymous"
            referrerpolicy="no-referrer"
    />
    <!-- Google Icon -->
    <link
            rel="stylesheet"
            href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@24,400,0,0"
    />
    <link rel="stylesheet" href="<%= request.getContextPath()%>/assets/css/admin/styleAllInventory.css"/>
    <link rel="stylesheet" href="<%= request.getContextPath()%>/assets/css/admin/modalForm.css"/>
    <style>
        #uploadForm {
            display: none;
        }

        .modal-backdrop.fade.show {
            display: none;
        }

        .modal-content {
            color: #fff;
        }

        .note {
            font-size: 12px; /* Kích thước chữ dễ đọc */
            font-style: italic;
            flex-shrink: 0; /* Không co lại */
        }
    </style>
</head>
<body class="dark-theme">
<jsp:include page="header-admin.jsp"></jsp:include>

<div id="main-content">
    <div class="main-container">
        <div class="header">
            <h1>Danh sách lô hàng</h1>
        </div>
        <div id="all-user-container">
            <a class="btn btn-primary btn-customize py-2 mb-2"
            <%--  Todo: Thêm lô hàng  --%>
               href="#" role="button" onclick="showBatchesAddForm()">Thêm lô hàng</a>

            <%--  Todo: Danh sách các lô hàng đã lưu trữ --%>
            <a class="btn btn-primary btn-customize py-2 mb-2"
               href="#" role="button" onclick="openTableArchive()">Các lô hàng lưu trữ</a>

            <table class="myTable display">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Mã lô</th>
                    <th>Sản phẩm</th>
                    <th>Số lượng</th>
                    <th>Giá (giá/1sp)</th>
                    <th>Ngày nhập</th>
                    <th>Nhà cung cấp</th>
                    <th>Thời gian tạo</th>
                    <th>Trạng thái</th>
                    <th>Thao tác</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${batches}" var="batch" varStatus="i">
                    <tr>
                        <td>${batch.batchID}</td>
                        <td>${batch.batchNumber}</td>
                        <td>${batch.productName}</td>
                        <td data-id="batch-quantity">${batch.quantity}</td>
                        <td data-id="batch-price">${batch.price}</td>
                        <td>${batch.importDate}</td>
                        <td>${batch.supplierName != null ? batch.supplierName : 'N/A'}</td>
                        <td>${batch.createdAt}</td>
                        <td>
                            <label>
                                <select
                                        data-original="${batch.isUsed}"
                                        onchange="changeUsedStatus(${batch.batchID},this)" name="usedStatus"
                                        style="background-color: white !important;">
                                    <option value="0" <c:if test="${batch.isUsed == 0}">selected</c:if>>Không sử
                                        dụng
                                    </option>
                                    <option value="1" <c:if test="${batch.isUsed == 1}">selected</c:if>>Sử dụng
                                    </option>
                                </select>
                            </label>

                        </td>
                        <td>
                            <c:if test="${batch.isUsed == 0}">
                                <button
                                        class="btn btn-success btn-sm"
                                        onclick="showBatchesEditForm('${batch.batchID}', '${batch.batchNumber}','${batch.productID}',
                                                '${batch.quantity}', '${batch.price}','${batch.importDate}',
                                                '${batch.supplierID}', '${batch.createdAt}')">
                                    Chỉnh Sửa
                                </button>
                            </c:if>
                            <a class="text-white btn btn-danger btn-sm"
                               onclick="return confirmAction('Bạn có muốn lưu trữ lô hàng này')"
                               href="<%=request.getContextPath()%>/admin/deleteBatch?batchID=${batch.batchID}&&option=1">
                                Lưu trữ</a>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>

        <!-- Batches Edit Modal -->
        <div class="modal" id="batchesEditModal">
            <div class="modal-content">
                <h3>Sửa lô hàng</h3>
                <form id="batchesEditForm">
                    <label for="batchesId">Mã lô hàng</label>
                    <input type="text" id="batchesId" name="batchesId" readonly/>

                    <label for="batchesNumber">Số lô hàng</label>
                    <input type="text" id="batchesNumber" name="batchesNumber" readonly/>

                    <label for="productId">Sản phẩm</label>
                    <select id="productId" name="productId">
                        <c:forEach items="${products}" var="product">
                            <option value="${product.productID}">${product.productID} - ${product.productName}</option>
                        </c:forEach>
                    </select>

                    <label for="quantity">Số lượng</label>
                    <input type="text" id="quantity" name="quantity" required/>

                    <label for="price">Giá</label>
                    <input type="number" id="price" name="price" required/>

                    <label for="importDate">Ngày nhập</label>
                    <input type="text" id="importDate" name="importDate" readonly>

                    <label for="createAt">Ngày tạo</label>
                    <input type="text" id="createAt" name="createAt" readonly/>

                    <label for="supplierID">Nhà cung cấp</label>
                    <select id="supplierID" name="supplierID">
                        <c:forEach items="${suppliers}" var="sup">
                            <option value="${sup.supplierID}">${sup.supplierName}</option>
                        </c:forEach>
                    </select>

                    <button type="submit" class="btn btn-success btn-sm">Lưu</button>
                    <button type="button" class="btn btn-danger btn-sm" onclick="closeModals(this)">
                        Hủy
                    </button>
                </form>
            </div>
        </div>
        <!-- Batches Edit Modal -->

        <!-- Batches Add Modal -->
        <div class="modal" id="batchesAddModal">
            <div class="modal-content">
                <h3>Nhập lô hàng</h3>
                <form id="batchesAddForm">

                    <label for="addProductId">Sản phẩm</label>
                    <select id="addProductId" name="productId">
                        <c:forEach items="${products}" var="product">
                            <option value="${product.productID}">${product.productID} - ${product.productName}</option>
                        </c:forEach>
                        required
                    </select>

                    <label for="addQuantity">Số lượng</label>
                    <input type="text" id="addQuantity" name="quantity" required/>

                    <label for="addPrice">Giá</label>
                    <input type="text" id="addPrice" name="price" required/>

                    <label for="addSupplierID">Nhà cung cấp</label>
                    <select id="addSupplierID" name="supplierID">
                        <c:forEach items="${suppliers}" var="sup">
                            <option value="${sup.supplierID}">${sup.supplierName}</option>
                        </c:forEach>
                        required
                    </select>

                    <button type="submit" class="btn btn-success btn-sm">Lưu</button>
                    <button type="button" class="btn btn-danger btn-sm" onclick="closeModals(this)">
                        Hủy
                    </button>
                    <button type="button" class="float-end btn btn-success btn-sm m-0" onclick="openFileInput()">
                        Import file
                    </button>
                </form>
                <div id="uploadForm" class="my-3">
                    <input class="form-control" type="file" id="formFile" name="fileName">
                    <button type="button" class="btn btn-info btn-sm m-0" onclick="uploadFile()">Upload</button>
                </div>
            </div>
        </div>
        <!-- Batches Add Modal -->

        <!-- Archive Batches -->
        <div id="archive-batches">
            <div class="header">
                <h1>Danh sách các lô hàng đang lưu trữ</h1>
            </div>
            <table class="myTable display">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Mã lô</th>
                    <th>Sản phẩm</th>
                    <th>Số lượng</th>
                    <th>Tình trạng</th>
                    <th>Thao tác</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${archiveBatches}" var="ar">
                    <tr>
                        <td>${ar.batchID}</td>
                        <td>${ar.batchNumber}</td>
                        <td>${ar.productName}</td>
                        <td>${ar.quantity}</td>
                        <td>
                            <p class="text-danger m-0">Đang lưu trữ</p>
                        </td>
                        <td>
                            <button class="btn btn-success btn-sm">
                                <a class="text-white"
                                   href="<%=request.getContextPath()%>/admin/deleteBatch?batchID=${ar.batchID}&&option=0">
                                    Bỏ lưu trữ </a>
                            </button>
                                <%--   <button class="btn btn-danger btn-sm">--%>
                                <%--   Xóa vĩnh viễn--%>
                                <%--   </button>--%>
                        </td>

                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
        <!-- Archive Batches -->


    </div>
</div>

<!-- Loading Overlay -->
<div id="loadingOverlay" style="display:none;">
    <div class="spinner-border text-white" role="status">
        <span class="visually-hidden">Loading...</span>
    </div>
</div>
<script>
    // Show form 'EDIT' batches
    function showBatchesEditForm(batchesId, batchNumber, productID, quantity, price, importDate, supplierID, createAt) {
        document.getElementById("batchesId").value = batchesId;
        document.getElementById("batchesNumber").value = batchNumber;
        document.getElementById("quantity").value = quantity;
        document.getElementById("price").value = price;
        document.getElementById("importDate").value = importDate;
        document.getElementById("createAt").value = createAt

        // Gán supplierID, productId vào select ==> set selected
        const supplierSelect = document.getElementById("supplierID");
        const productSelect = document.getElementById("productId");
        supplierSelect.value = supplierID; // Chọn option có value trùng với supplierID
        productSelect.value = productID; // Chọn option có value trùng với productId

        // Khởi tạo Select2
        createSelect2();
        // Show form
        document.getElementById("batchesEditModal").style.display = "block";
    }

    // Send request 'EDIT' batches
    document.getElementById('batchesEditForm').addEventListener('submit', async function (e) {
        e.preventDefault();
        let formData = new URLSearchParams(new FormData(this));
        let url = `${pageContext.request.contextPath}/admin/edit-batches`
        // if (!checkIsValidData(formData.get('dishName'), formData.get('dishPrice'))) return;

        try {
            let response = await fetch(url, {
                method: 'Post',
                headers: {
                    "Content-Type": "application/x-www-form-urlencoded"
                },
                body: formData,
            })

            let result = await response.json();
            if (result.isSuccess) {
                alert('Cập nhật thành công')
                window.location.reload()
            } else {
                alert('Cập nhật thất bại')
                window.location.reload()
            }
        } catch (error) {
        }
    })

    // Show form 'ADD' batches
    function showBatchesAddForm() {
        // Khởi tạo Select2
        createSelect2();
        // Show form
        document.getElementById("batchesAddModal").style.display = "block";
    }

    // Send request 'ADD' batches
    document.getElementById('batchesAddForm').addEventListener('submit', async function (e) {
        e.preventDefault();
        let formData = new URLSearchParams(new FormData(this));
        let url = `${pageContext.request.contextPath}/admin/add-batches`
        // if (!checkIsValidData(formData.get('dishName'), formData.get('dishPrice'))) return;

        try {
            let response = await fetch(url, {
                method: 'Post',
                headers: {
                    "Content-Type": "application/x-www-form-urlencoded"
                },
                body: formData
            })

            let result = await response.json();
            if (result.isSuccess) {
                alert('Thêm thành công')
                window.location.reload()
            } else {
                alert('Thêm thất bại')
                window.location.reload()
            }
        } catch (error) {
        }
    })

    // Change used status
    async function changeUsedStatus(batchID, selectBox) {
        const originalValue = selectBox.getAttribute('data-original'); // giữ trạng thái ban đầu
        if (!confirmAction('Bạn có muốn thay đổi trạng thái lô này')) {
            selectBox.value = originalValue;
            return;
        }
        const selectedValue = selectBox.value; // value status

        let url = `${pageContext.request.contextPath}/admin/status-used`
        try {
            let response = await fetch(url, {
                method: 'Post',
                headers: {
                    "Content-Type": "application/x-www-form-urlencoded"
                },
                body: "batchID=" + encodeURIComponent(batchID) + "&usedStatus=" + encodeURIComponent(selectedValue)
            })

            let result = await response.json();
            if (result.isSuccess) {
                alert('Cập nhật thành công');
                window.location.reload();
            } else {
                alert('Cập nhật thất bại');
                selectBox.value = originalValue;
                window.location.reload();
            }
        } catch (error) {
        }
    }

    // upload file to import batches
    async function uploadFile() {
        let fileName = document.getElementById("formFile").files[0].name;
        console.log(fileName);

        let formData = new URLSearchParams();
        formData.append("fileName", fileName);

        let url = `${pageContext.request.contextPath}/admin/add-batch-excel`;

        try {
            let response = await fetch(url, {
                method: 'Post',
                headers: {
                    "Content-Type": "application/x-www-form-urlencoded"
                },
                body: formData,
            })

            let result = await response.json();
            if (result.isSuccess) {
                alert('Thêm thành công')
                window.location.reload()
            } else {
                alert('Thêm thất bại')
                window.location.reload()
            }
        } catch (error) {
            console.log(error)
        }
    }

    // Helper function
    function confirmAction(text) {
        return confirm(text);
    }

    function openTableArchive() {
        let element = document.getElementById('archive-batches');
        let currentDisplay = element.style.display;

        if (currentDisplay === 'none' || currentDisplay === '') {
            element.style.display = 'block';
        } else {
            element.style.display = 'none';
        }
    }

    function openFileInput() {
        let element = document.getElementById('uploadForm');
        let currentDisplay = element.style.display;

        if (currentDisplay === 'none' || currentDisplay === '') {
            element.style.display = 'block';
        } else {
            element.style.display = 'none';
        }
    }

    function closeModals(btnCancel) {
        let target = btnCancel.form.parentElement.parentElement;
        target.style.display = "none";
    }

    function loading(reload) {
        const loading = document.getElementById("loadingOverlay");
        loading.style.display = "flex";

        if (reload) {
            setTimeout(() => {
                window.location.reload();
            }, 500); // 0.5 giây
        } else {
            setTimeout(() => {
                loading.style.display = "none";
            }, 500); // 0.5 giây
        }
    }

    function createSelect2() {
        // Khởi tạo Select2
        $('#productId').select2({
            allowClear: true,
            width: '100%'
        });
    }

</script>
</body>
</html>
