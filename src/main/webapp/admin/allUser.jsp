<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page isELIgnored="false" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %><!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Header Example</title>
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


    <link rel="stylesheet" href="<%= request.getContextPath()%>/assets/css/admin/styleAllUser.css" />
  </head>
  <body class="dark-theme">
  <jsp:include page="header-admin.jsp"></jsp:include>
    <div id="main-content">
      <div class="main-container">
        <div class="header">
          <h1>Tất cả người dùng</h1>

        </div>
        <div id="all-user-container">
          <div class="all-user">
            <!-- Header -->
            <div class="header-show-cate">
              <div class="row align-items-center flex-wrap">
                <div class="col-8">
                  <div class="row row-cols-2 align-items-center">
                    <div class="col" style="position: relative">
                      <input
                        id="input-search"
                        class="mini-search"
                        type="text"
                        placeholder="Tìm kiếm..."
                      />
                      <div class="btn-submit">
                        <button>
                          <i class="fa-solid fa-magnifying-glass"></i>
                        </button>
                      </div>
                    </div>
                  </div>
                </div>
                <div class="col-4 text-end">
                  <a
                    class="add-btn d-inline-block bg-white text-center text-decoration-none"
                    ><i class="fa-solid fa-plus" style="padding-right: 4px"></i>
                    Add new</a
                  >
                </div>
              </div>
            </div>
            <!-- Content -->
            <div class="content-show-cate">
              <div class="header-title">
                <div class="row">
                  <div class="col-2 text-break">Người dùng</div>
                  <div class="col">ID người dùng</div>
                  <div class="col">Số điện thoại</div>
                  <div class="col-2 text-break">Email</div>
                  <div class="col-4">Địa chỉ</div>
                  <div class="col">Hoạt động</div>
                </div>
              </div>
              <ul class="list-cate list">
               <c:forEach items="listC" var="o">
                 <li>
                   <div class="row align-items-center">
                     <div
                             class="col-2 text-break d-flex align-items-center gap-2"
                     >
                       <img src="<%= request.getContextPath()%>/assets/pic/capy.jpg" alt="" />
                       <div class="name">
                         <p class="text-cate">${o.name}</p>
                         <div class="text-tiny">Product name</div>
                       </div>
                     </div>
                     <div class="col">${o.id}</div>
                     <div class="col">${o.phone}</div>
                     <div class="col-2 text-break">${o.email}</div>
                     <div class="col-4">
                       ${o.address}
                     </div>
                     <div class="col">
                       <div class="icon-action d-flex justify-content-around">
                         <i
                                 class="fa-regular fa-trash-can"
                                 style="color: #ff5200"
                         ></i>
                       </div>
                     </div>
                   </div>
                 </li>

               </c:forEach>

              </ul>
            </div>
          </div>
          <div class="row">
            <div class="col-6">Hiển thị 10 mục</div>
            <div class="col-6">
              <div class="page">
                <nav aria-label="Page navigation example">
                  <ul class="pagination justify-content-end">
                    <li class="page-item">
                      <a class="page-link arrow" href="#" aria-label="Previous">
                        <span aria-hidden="true"
                          ><i class="fa-solid fa-chevron-left"></i
                        ></span>
                      </a>
                    </li>
                    <li class="page-item">
                      <a class="page-link" href="#">1</a>
                    </li>
                    <li class="page-item active">
                      <a class="page-link" href="#">2</a>
                    </li>
                    <li class="page-item">
                      <a class="page-link" href="#">3</a>
                    </li>
                    <li class="page-item">
                      <a class="page-link arrow" href="#" aria-label="Next">
                        <span aria-hidden="true"
                          ><i class="fa-solid fa-chevron-right"></i
                        ></span>
                      </a>
                    </li>
                  </ul>
                </nav>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <script src="../assets/js/search.js"></script>
  </body>
</html>
