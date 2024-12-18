<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page isELIgnored="false" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %><!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Sản phẩm bán chạy</title>
    <!-- Link FONTAWSOME -->
    <link
      rel="stylesheet"
      href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css"
    />

    <!-- Link JQUERY-->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>

    <!-- Link BOOTSTRAP -->
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
    <!-- Link BOOTSTRAP -->

    <!-- Link my CSS -->
    <link rel="stylesheet" href="assets/css/styleProduct.css" />
  </head>
  <body class="bg-body px-0 overflow-visible">
  <jsp:include page="header.jsp"></jsp:include>


  <main id="product-container" style="margin: 12rem 0px">
      <div class="container">
        <!-- Filter and Sort -->
        <div class="fs_bar py-3">
          <div class="row">
            <!-- Filter -->
            <div class="col-6">
              <button
                class="btn_filter"
                type="button"
                data-bs-toggle="offcanvas"
                data-bs-target="#offcanvasWithBothOptions"
                aria-controls="offcanvasWithBothOptions"
              >
                Filter
                <i class="fa-solid fa-caret-down"></i>
              </button>

              <div
                class="offcanvas offcanvas-start"
                data-bs-scroll="true"
                tabindex="-1"
                id="offcanvasWithBothOptions"
                aria-labelledby="offcanvasWithBothOptionsLabel"
                style="background-color: #fffefa"
              >
                <div class="offcanvas-header">
                  <button
                    type="button"
                    class="btn-close btn-close-custom"
                    data-bs-dismiss="offcanvas"
                    aria-label="Close"
                  >
                    <svg
                      width="25"
                      height="25"
                      viewBox="0 0 25 25"
                      fill="none"
                      xmlns="http://www.w3.org/2000/svg"
                      class="papier-icon icon-close-display fill-current cursor-pointer"
                      style="height: 24px; width: 24px"
                    >
                      <path
                        fill-rule="evenodd"
                        clip-rule="evenodd"
                        d="M13.4119 12.001L24.0416 21.9203L21.9203 24.0416L12.0208 13.4331L2.1213 24.0416L-2.36034e-05 21.9203L10.6297 12.001L-3.05176e-05 2.12132L2.12129 2.14577e-06L12.0208 10.6511L21.9203 1.52588e-05L24.0416 2.12134L13.4119 12.001Z"
                        fill="#1E2525"
                        style="background: red"
                      ></path>
                    </svg>
                  </button>
                </div>
                <h5 class="offcanvas-title" id="offcanvasWithBothOptionsLabel">
                  ALL FILTER
                </h5>
                <div class="offcanvas-body">
                  <!-- Lọc giá -->
                  <div class="filter-container">
                    <div class="filter-title">Filter by</div>
                    <div class="filter-category">Giá +</div>
                    <ul class="filter-options">
                      <li>
                        <div class="checkbox-circle">
                          <i class="fa-solid fa-check fa-xl"></i>
                        </div>
                        <span class="filter-option-text">Nhỏ hơn 50,000đ</span>
                      </li>
                      <li>
                        <div class="checkbox-circle">
                          <i class="fa-solid fa-check fa-xl"></i>
                        </div>
                        <span class="filter-option-text"
                          >50,000đ - 100,000đ</span
                        >
                      </li>
                      <li>
                        <div class="checkbox-circle">
                          <i class="fa-solid fa-check fa-xl"></i>
                        </div>
                        <span class="filter-option-text"
                          >100,000đ - 200,000đ</span
                        >
                      </li>
                      <li>
                        <div class="checkbox-circle">
                          <i class="fa-solid fa-check fa-xl"></i>
                        </div>
                        <span class="filter-option-text"
                          >200,000đ - 500,000đ</span
                        >
                      </li>
                      <li>
                        <div class="checkbox-circle">
                          <i class="fa-solid fa-check fa-xl"></i>
                        </div>
                        <span class="filter-option-text">Lớn hơn 500,000đ</span>
                      </li>
                    </ul>
                  </div>
                  <!-- Lọc thể loại -->
                  <div class="filter-container">
                    <div class="filter-category">Sản phẩm +</div>
                    <ul class="filter-options">
                      <li>
                        <div class="checkbox-circle">
                          <i class="fa-solid fa-check fa-xl"></i>
                        </div>
                        <span class="filter-option-text">Bút</span>
                      </li>
                      <li>
                        <div class="checkbox-circle">
                          <i class="fa-solid fa-check fa-xl"></i>
                        </div>
                        <span class="filter-option-text">Gôm, thước</span>
                      </li>
                      <li>
                        <div class="checkbox-circle">
                          <i class="fa-solid fa-check fa-xl"></i>
                        </div>
                        <span class="filter-option-text">Sổ tay, tập</span>
                      </li>
                      <li>
                        <div class="checkbox-circle">
                          <i class="fa-solid fa-check fa-xl"></i>
                        </div>
                        <span class="filter-option-text">Bìa còng</span>
                      </li>
                      <li>
                        <div class="checkbox-circle">
                          <i class="fa-solid fa-check fa-xl"></i>
                        </div>
                        <span class="filter-option-text"
                          >Bút lông bản, lông dầu</span
                        >
                      </li>
                    </ul>
                  </div>
                  <div class="filter-btn-group">
                    <a class="filter-items delete-btn">Xóa</a>
                    <a
                      class="filter-items"
                      data-bs-toggle="offcanvas"
                      data-bs-target="#offcanvasWithBothOptions"
                      aria-controls="offcanvasWithBothOptions"
                      >Lọc</a
                    >
                  </div>
                </div>
              </div>
            </div>

            <!-- Sort -->
            <div class="col">
              <div class="sort" style="display: inline-block">
                <span>Sắp xếp:</span>
                <div class="custom-select">
                  <div class="select-selected">Giá: Thấp đến cao</div>
                  <div class="select-options">
                    <div class="select-option" data-value="1">
                      Giá: Thấp đến cao
                    </div>
                    <div class="select-option" data-value="2">
                      Giá: Cao đến thấp
                    </div>
                    <div class="select-option" data-value="3">Tên: A-Z</div>
                    <div class="select-option" data-value="4">Tên: Z-A</div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <!-- Filter and Sort -->
        <div
          class="row row-cols-1 row-cols-sm-2 row-cols-md-3 row-cols-lg-4 g-3"
        >
          <div class="col">
            <div class="card border border-0">
              <a href="product-detail.jsp" class="forward-img">
                <!-- ảnh sản phẩm -->
                <div class="wrapper-img">
                  <img
                    src="./assets/pic/sample_pic_bestseller"
                    class="card-img-top"
                    style="background-color: #f4eee0"
                    alt="..."
                  />
                </div>
                <!-- nút thêm nhanh vào giỏ hàng -->
                <div class="add-to-cart">
                  <span data-bs-target="#exampleModal" data-bs-toggle="modal">
                    <button
                      class="icon-p"
                      type="button"
                      data-bs-toggle="tooltip"
                      data-bs-placement="top"
                      data-bs-custom-class="custom-tooltip"
                      data-bs-title="Thêm vào giỏ hàng."
                    >
                      <img
                        src="./assets/pic/shopping_cart_icon.svg"
                        alt="ảnh"
                      />
                    </button>
                  </span>
                </div>
              </a>

              <div class="card-body bg-body ms--15">
                <div class="card-header-cus">
                  <h5 class="card-title fw-semibold">Card title</h5>
                  <h5 class="price me--15 fw-semibold">
                    300.000<span class="currency">đ</span>
                  </h5>
                </div>
                <p class="card-text fs-7 fw-medium text-justify">
                  Rollerball Pen
                </p>
              </div>
            </div>
          </div>
          <div class="col">
            <div class="card border border-0">
              <a href="product-detail.jsp" class="forward-img">
                <div class="wrapper-img">
                  <img
                    src="./assets/pic/sample_pic_bestseller"
                    class="card-img-top"
                    style="background-color: #f4eee0"
                    alt="..."
                  />
                </div>
                <div class="add-to-cart">
                  <span data-bs-target="#exampleModal" data-bs-toggle="modal">
                    <button
                      class="icon-p"
                      type="button"
                      data-bs-toggle="tooltip"
                      data-bs-placement="top"
                      data-bs-custom-class="custom-tooltip"
                      data-bs-title="Thêm vào giỏ hàng."
                    >
                      <img
                        src="./assets/pic/shopping_cart_icon.svg"
                        alt="ảnh"
                      />
                    </button>
                  </span>
                </div>
              </a>
              <div class="card-body bg-body ms--15">
                <div class="card-header-cus">
                  <h5 class="card-title fw-semibold">Card title</h5>
                  <h5 class="price me--15 fw-semibold">
                    300.000<span class="currency">đ</span>
                  </h5>
                </div>
                <p class="card-text fs-7 fw-medium text-justify">
                  Rollerball Pen
                </p>
              </div>
            </div>
          </div>
          <div class="col">
            <div class="card border border-0">
              <a href="product-detail.jsp" class="forward-img">
                <div class="wrapper-img">
                  <img
                    src="./assets/pic/sample_pic_bestseller"
                    class="card-img-top"
                    style="background-color: #f4eee0"
                    alt="..."
                  />
                </div>
                <div class="add-to-cart">
                  <span data-bs-target="#exampleModal" data-bs-toggle="modal">
                    <button
                      class="icon-p"
                      type="button"
                      data-bs-toggle="tooltip"
                      data-bs-placement="top"
                      data-bs-custom-class="custom-tooltip"
                      data-bs-title="Thêm vào giỏ hàng."
                    >
                      <img
                        src="./assets/pic/shopping_cart_icon.svg"
                        alt="ảnh"
                      />
                    </button>
                  </span>
                </div>
              </a>
              <div class="card-body bg-body ms--15">
                <div class="card-header-cus">
                  <h5 class="card-title fw-semibold">Card title</h5>
                  <h5 class="price me--15 fw-semibold">
                    300.000<span class="currency">đ</span>
                  </h5>
                </div>
                <p class="card-text fs-7 fw-medium text-justify">
                  Rollerball Pen
                </p>
              </div>
            </div>
          </div>
          <div class="col">
            <div class="card border border-0">
              <a href="product-detail.jsp" class="forward-img">
                <div class="wrapper-img">
                  <img
                    src="./assets/pic/sample_pic_bestseller"
                    class="card-img-top"
                    style="background-color: #f4eee0"
                    alt="..."
                  />
                </div>
                <div class="add-to-cart">
                  <span data-bs-target="#exampleModal" data-bs-toggle="modal">
                    <button
                      class="icon-p"
                      type="button"
                      data-bs-toggle="tooltip"
                      data-bs-placement="top"
                      data-bs-custom-class="custom-tooltip"
                      data-bs-title="Thêm vào giỏ hàng."
                    >
                      <img
                        src="./assets/pic/shopping_cart_icon.svg"
                        alt="ảnh"
                      />
                    </button>
                  </span>
                </div>
              </a>
              <div class="card-body bg-body ms--15">
                <div class="card-header-cus">
                  <h5 class="card-title fw-semibold">Card title</h5>
                  <h5 class="price me--15 fw-semibold">
                    300.000<span class="currency">đ</span>
                  </h5>
                </div>
                <p class="card-text fs-7 fw-medium text-justify">
                  Rollerball Pen
                </p>
              </div>
            </div>
          </div>
        </div>
        <div class="page">
          <nav aria-label="Page navigation example">
            <ul class="pagination justify-content-center">
              <li class="page-item">
                <a class="page-link" href="#" aria-label="Previous">
                  <span aria-hidden="true"
                    ><i class="fa-solid fa-chevron-left"></i
                  ></span>
                </a>
              </li>
              <li class="page-item active">
                <a class="page-link" href="#" style="">1</a>
              </li>
              <li class="page-item"><a class="page-link" href="#">2</a></li>
              <li class="page-item"><a class="page-link" href="#">3</a></li>
              <li class="page-item">
                <a class="page-link" href="#" aria-label="Next">
                  <span aria-hidden="true"
                    ><i class="fa-solid fa-chevron-right"></i
                  ></span>
                </a>
              </li>
            </ul>
          </nav>
        </div>
      </div>

      <!-- Modal -->
      <div
        class="modal fade"
        id="exampleModal"
        tabindex="-1"
        aria-labelledby="exampleModalLabel"
        aria-hidden="true"
      >
        <div class="modal-dialog modal-sm" style="margin-top: 10rem">
          <div class="modal-content rounded-0">
            <div
              class="modal-header bg-dark border border-0 rounded-0"
              style="height: 25px; padding: 20px 0px"
            >
              <h1
                class="modal-title fs-6 fw- text-light text-center w-100 ps-2"
                id="exampleModalLabel"
              >
                THÔNG BÁO
              </h1>
              <button
                type="button"
                class="btn-close btn-custom"
                data-bs-dismiss="modal"
                aria-label="Close"
              ></button>
            </div>
            <div
              class="modal-body text-center text-secondary-emphasis fst-italic"
              style="height: 20px"
            >
              Thêm vào giỏ hàng thành công...
            </div>
            <div class="modal-footer border border-0">
              <button
                type="button"
                class="btn btn-secondary w-50 mx-auto bg-dark rounded-0"
                data-bs-dismiss="modal"
              >
                <a
                  href="../shopping-cart.html"
                  style="color: #fff; font-size: 14px; font-weight: 600"
                  >XEM GIỎ HÀNG</a
                >
              </button>
            </div>
          </div>
        </div>
      </div>
    </main>

  <jsp:include page="footer.jsp"></jsp:include>

    <!-- Link CUSTOM JS -->
    <script src="assets/js/main.js"></script>
    <script>
      //// JS cho phần hiện tooltip khi hover vào icon giỏ hàng (bstrap) ////
      const tooltipTriggerList = document.querySelectorAll(
        '[data-bs-toggle="tooltip"]'
      );

      const tooltipList = [...tooltipTriggerList].map(
        (tooltipTriggerEl) => new bootstrap.Tooltip(tooltipTriggerEl)
      );

      //// JS cho phần check filter ////
      $(".filter-options li").click(function () {
        // Toggle trạng thái dấu tích cho từng tùy chọn
        const checkboxCircle = $(this).find(".checkbox-circle");
        checkboxCircle.toggleClass("checked");
      });

      $(".delete-btn").click(function () {
        console.log(this);

        // Tìm và xóa class "checked" của tất cả các dấu tích trong checkbox-circle
        const checkboxCircle = $(".checkbox-circle");
        checkboxCircle.removeClass("checked");
      });

      // Ngăn chặn sự kiện click của thẻ span không thực hiện logic của thẻ a
      $(".add-to-cart span").click(function (event) {
        event.preventDefault(); // Ngừng hành động mặc định của thẻ a (theo href)
        event.stopPropagation(); // Ngừng sự kiện nổi bọt tới thẻ a
      });

      //// JS cho phần phân trang ////
      const preButton = $(".page-link[aria-label='Previous']");
      const nextButton = $(".page-link[aria-label='Next']");
      const currentPos = $(".page-item.active").text().trim();

      if (currentPos === "1") {
        preButton.addClass("disabled");
      }

      $(".page-link").click(function () {
        let text = $(this).text();

        $(".page-item").removeClass("active"); // Loại bỏ class 'active' khỏi tất cả các .page-item

        $(".page-link").removeClass("focused"); // Loại bỏ class 'focused' khỏi tất cả các .page-link

        $(this).addClass("focused"); // Thêm class 'focused' vào thẻ được click

        // Kiểm tra nếu là trang đầu tiên hoặc cuối cùng
        if (text === "1") {
          preButton.addClass("disabled");
          nextButton.removeClass("disabled");
        } else if (text === "3") {
          nextButton.addClass("disabled");
          preButton.removeClass("disabled");
        } else {
          preButton.removeClass("disabled");
          nextButton.removeClass("disabled");
        }
      });
    </script>
  </body>
</html>
