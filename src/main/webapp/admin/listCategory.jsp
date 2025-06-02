<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page isELIgnored="false" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Danh sách danh mục</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous"/>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css"/>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.1/css/all.min.css"
          integrity="sha512-5Hs3dF2AEPkpNAR7UiOHba+lRSJNeM2ECkwxUIxC1Q/FLycGTbNapWXB4tP889k5T5Ju8fs4b1P5z/iB4nMfSQ=="
          crossorigin="anonymous" referrerpolicy="no-referrer"/>
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@24,400,0,0"/>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/admin/styleListCate.css"/>
</head>
<body class="dark-theme">
<jsp:include page="header-admin.jsp"></jsp:include>
<div id="main-content">
    <div class="main-container">
        <div class="header">
            <h1>Hiển thị danh mục</h1>
        </div>
        <div class="add-voucher" style="margin-bottom: 1rem">
            <form id="myForm">
                <div class="row">
                    <p class="text-danger"><%= request.getAttribute("msg") != null ? request.getAttribute("msg") : "" %></p>
                    <div class="col col-2">
                        <label class="pb-2" for="nameCate">Tên danh mục</label>
                        <input id="nameCate" name="nameCate" type="text" class="form-control" placeholder="Tên danh mục"
                               aria-label="Last name">
                    </div>
                    <div class="col col-2">
                        <label class="pb-2" for="imgCate">Ảnh danh mục</label>
                        <input id="imgCate" name="imgCate" type="file" class="form-control" placeholder="Ảnh danh mục"
                               aria-label="First name">
                    </div>
                </div>
                <button style="margin-top: 1rem" type="submit" class="btn btn-primary btn-sm">Thêm danh mục</button>
            </form>
        </div>
        <div id="list-cate-container">
            <div class="cate-list">
                <table id="myTable" class="display" style="width:100%">
                    <thead>
                    <tr>
                        <th>Ảnh</th>
                        <th>Danh mục</th>
                        <th>ID danh mục</th>
                        <th>Hành động</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${listC}" var="o">
                        <tr data-id="${o.id}">
                            <td><img src="<%=request.getContextPath()%>/assets/pic/products/${o.cateImg}" alt=""></td>
                            <td>${o.name}</td>
                            <td>${o.id}</td>
                            <td>
                                <a class="btn btn-success btn-customize"
                                   href="<%=request.getContextPath()%>/admin/get-cate?cID=${o.id}" role="button">Chỉnh sửa</a>
                                <a class="btn btn-danger btn-customize"
                                   href="javascript:void(0)"
                                   onclick="confirmDelete(this, ${o.id})" role="button">Xóa</a>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
<script>
    // add
    document.getElementById("myForm").addEventListener("submit", async function (event) {
        event.preventDefault();
        let url = "${pageContext.request.contextPath}/add-newCate";
        let url_custom = "${pageContext.request.contextPath}/admin/get-cate?cID=";
        let formData = new URLSearchParams(new FormData(this));
        let fileName = document.querySelector("#imgCate").files[0].name;
        formData.delete("imgCate");
        formData.append("imgCate", fileName);
        let src_img = "${pageContext.request.contextPath}/assets/pic/products/" + fileName;
        try {
            let response = await fetch(url, {
                method: 'POST',
                headers: { "Content-Type": "application/x-www-form-urlencoded" },
                body: formData
            });
            let result = await response.json();
            if (result.isSuccess) {
                alert('Thêm thành công');
                let cID = result.cID;
                let table = $("#myTable").DataTable();
                let actionButtons =
                    "<a class='btn btn-success btn-customize' href='" + url_custom + cID + "' role='button'>Chỉnh sửa</a> " +
                    "<a class='btn btn-danger btn-customize' onclick='confirmDelete(this, " + cID + ")' role='button'>Xóa</a>";
                let newData = [
                    "<img alt='' src='" + src_img + "'></img>",
                    document.getElementById("nameCate").value,
                    cID,
                    actionButtons
                ];
                table.row.add(newData).draw(false);
            } else {
                alert('Thêm thất bại');
            }
        } catch (error) {
            console.log(error);
        }
    });

    // delete
    async function confirmDelete(button, cid) {
        if (!confirm("Bạn có chắc chắn muốn thực hiện hành động này?\nĐiều này sẽ xóa danh mục này")) return;
        let url = "${pageContext.request.contextPath}/admin/delete-cate";
        let target = button.parentElement.parentElement;
        try {
            let response = await fetch(url, {
                method: 'POST',
                headers: { "Content-Type": "application/x-www-form-urlencoded" },
                body: "cID=" + encodeURIComponent(cid)
            });
            if (!response.ok) {
                throw new Error("HTTP error, status = " + response.status);
            }
            let rs = await response.json();
            if (rs.isSuccess) {
                let table = $("#myTable").DataTable();
                table.row(target).remove().draw(false);
                alert("Xóa danh mục thành công!");
            } else {
                alert("Xóa danh mục thất bại! Lý do: " + (rs.error || "Không xác định"));
            }
        } catch (error) {
            console.log("Error: ", error);
            alert("Lỗi khi xóa danh mục! " + error.message);
        }
    }
</script>
</body>
</html>