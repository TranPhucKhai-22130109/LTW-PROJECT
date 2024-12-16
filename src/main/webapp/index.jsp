<%@ page import="java.util.List" %>
<%@ page import="entity.HomePicture" %><%--
  Created by IntelliJ IDEA.
  User: ASUS
  Date: 12/3/2024
  Time: 3:44 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page isELIgnored="false" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Trang Chủ</title>
    <!-- Link jQuery -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>

    <!-- Swiper JS -->
    <link
            rel="stylesheet"
            href="https://cdn.jsdelivr.net/npm/swiper@11/swiper-bundle.min.css"
    />

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
    <link rel="stylesheet" href="assets/css/styleIndex.css">
</head>
<body>

<jsp:include page="header.jsp"></jsp:include>


<main id="home-container">
    <% HomePicture home = (HomePicture) request.getAttribute("homepictures"); %>
    <!-- Banner -->
    <section class="banner-hero">
        <!-- Banner bootstrap -->
        <div
                id="carouselExampleAutoplaying"
                class="carousel slide"
                data-bs-ride="carousel"
        >
            <div class="carousel-inner">
                <div class="carousel-item active">
                    <img
                            src="./assets/pic/<%= home.getBannerImg()%>"
                            class="d-block w-100"
                            alt="..."
                    />
                </div>
                <%--        <div class="carousel-item">--%>
                <%--          <img--%>
                <%--                  src="./assets/pic/new_banner_noel.jpg"--%>
                <%--                  class="d-block w-100"--%>
                <%--                  alt="..."--%>
                <%--          />--%>
                <%--        </div>--%>
                <%--        <div class="carousel-item">--%>
                <%--          <img--%>
                <%--                  src="./assets/pic/new_banner_noel.jpg"--%>
                <%--                  class="d-block w-100"--%>
                <%--                  alt="..."--%>
                <%--          />--%>
                <%--        </div>--%>
            </div>
            <%--      <button--%>
            <%--              class="carousel-control-prev"--%>
            <%--              type="button"--%>
            <%--              data-bs-target="#carouselExampleAutoplaying"--%>
            <%--              data-bs-slide="prev"--%>
            <%--      >--%>
            <%--        <span class="carousel-control-prev-icon" aria-hidden="true"></span>--%>
            <%--        <span class="visually-hidden">Previous</span>--%>
            <%--      </button>--%>
            <%--      <button--%>
            <%--              class="carousel-control-next"--%>
            <%--              type="button"--%>
            <%--              data-bs-target="#carouselExampleAutoplaying"--%>
            <%--              data-bs-slide="next"--%>
            <%--      >--%>
            <%--        <span class="carousel-control-next-icon" aria-hidden="true"></span>--%>
            <%--        <span class="visually-hidden">Next</span>--%>
            <%--      </button>--%>
        </div>
    </section>

    <!-- Banner -->

    <!-- Why Choose -->
    <section class="why-choose-section">
        <div class="why-choose-container">
            <div class="wh-col-left">

                 <!-- Assuming getTitle() exists in HomePicture -->

                <h2 class="wh-title">Những lí do bạn nên chọn chúng tôi</h2>
                <p>
                    Chúng tôi mang đến sự khác biệt trong từng sản phẩm, tạo niềm vui
                    , ý nghĩa và đặc biệt là đem đến sự thoải mái trong từng trải
                    nghiệm.
                </p>

                <div class="wh-col-left-content">
                    <div class="items-1 item-left-content">
                        <div class="feature">
                            <div class="icon">
                                <img class="icon-image" src="./assets/pic/aBook" alt=""/>
                            </div>

                            <h3>Sẵn sàng để cá nhân hóa bằng ngôn từ của bạn</h3>
                            <p>
                                Dễ dàng thêm lời chúc, thông điệp riêng của bạn vào sản
                                phẩm.
                            </p>
                        </div>
                    </div>
                    <div class="items-2 item-left-content">
                        <div class="feature">
                            <div class="icon">
                                <img
                                        class="icon-image"
                                        src="./assets/pic/note.avif"
                                        alt=""
                                />
                            </div>

                            <h3>Quà tặng ý nghĩa cho bạn và mọi người</h3>
                            <p>
                                Mang đến món quà ý nghĩa và độc đáo cho mọi dịp và mọi
                                người.
                            </p>
                        </div>
                    </div>

                    <div class="items-3 item-left-content">
                        <div class="feature">
                            <div class="icon">
                                <img
                                        class="icon-image"
                                        src="./assets/pic/hand_fly.avif"
                                        alt=""
                                />
                            </div>

                            <h3>Chúng tôi nổi tiếng với chất lượng sản phẩm</h3>
                            <p>
                                Sản phẩm với chất lượng sản phẩm tốt, mang đến sự khác biệt.
                            </p>
                        </div>
                    </div>
                    <div class="items-4 item-left-content">
                        <div class="feature">
                            <div class="icon">
                                <img
                                        class="icon-image"
                                        src="./assets/pic/hand_heart.avif"
                                        alt=""
                                />
                            </div>
                            <h3>Thiết kế được làm thủ công tại xưởng TP.HCM</h3>
                            <p>
                                Từng sản phẩm được tạo nên từ tâm huyết và sáng tạo độc đáo.
                            </p>
                        </div>
                    </div>
                </div>
            </div>
            <div class="wh-col-right">
                <div class="img-wrap">
                    <img
                            src="./assets/pic/<%= home.getImg1()%>"
                            alt="Image"
                            class="img-fluid"
                    />
                </div>
            </div>
        </div>
    </section>
    <!-- Why Choose -->

    <!-- We Help -->
    <section class="we-help-section">
        <div class="we-help-container">
            <div class="whp-left">
                <div class="imgs-grid">
                    <div class="grid grid-1">
                        <img src="./assets/pic/<%= home.getImg4()%>" alt=""/>
                    </div>
                    <div class="grid grid-2">
                        <img src="./assets/pic/<%= home.getImg2()%>" alt=""/>
                    </div>
                    <div class="grid grid-3">
                        <img src="./assets/pic/<%= home.getImg5()%>" alt=""/>
                    </div>
                </div>
            </div>
            <div class="whp-right">
                <h2 class="wh-title" style="margin-left: -6px">
                    Chúng tôi giúp bạn có những trải nghiệm tốt nhất
                </h2>
                <p>
                    Chúng tôi cam kết mang đến cho bạn trải nghiệm mua sắm hoàn hảo.
                    Từ quá trình lựa chọn sản phẩm đến dịch vụ hậu mãi, mọi khâu đều
                    được chăm chút để đảm bảo bạn luôn hài lòng. Đội ngũ của chúng tôi
                    sẵn sàng hỗ trợ bạn bất cứ lúc nào, giúp bạn yên tâm với những
                    dịch vụ như giao hàng nhanh, hỗ trợ tận tâm, và chính sách hoàn
                    trả dễ dàng. Với chúng tôi, bạn luôn là ưu tiên hàng đầu
                </p>
                <ul>
                    <li>Giao hàng siêu tốc, hoàn toàn miễn phí</li>
                    <li>Mua sắm dễ dàng, nhanh chóng mọi lúc</li>
                    <li>Chúng tôi hỗ trợ 24/7 để giải đáp mọi thắc mắc</li>
                    <li>Hoàn trả miễn phí nếu có lỗi từ sản phẩm</li>
                </ul>
            </div>
        </div>
    </section>

    <!-- We Help -->
    <section id="list-template">
        <div class="list-container">
            <div class="list-header container-full">
                <div class="section-header">
                    <h2 class="section-heading">Khám phá sản phẩm của chúng tôi</h2>
                    <h2 class="section-description">
                        Những sản phẩm với chất lượng tốt nhất, giá cả hợp lý
                    </h2>
                </div>
            </div>
            <div class="list-content container-full">
                <div class="colection-list">
                    <div class="layout">
                        <div class="row">
                            <div class="col-lg-2 col-md-3 col-sm-4 col-6">
                                <div class="card-inner">
                                    <a href="#">
                                        <div class="box">
                                            <img src="assets/pic/highlights_pen.avif"/>
                                        </div>
                                    </a>
                                    <div class="card-info">
                                        <div class="card-title">
                                            <a href="#">Thể loại</a>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="col-lg-2 col-md-3 col-sm-4 col-6">
                                <div class="card-inner">
                                    <a href="#">
                                        <div class="box">
                                            <img src="assets/pic/highlights_pen.avif"/>
                                        </div>
                                    </a>
                                    <div class="card-info">
                                        <div class="card-title">
                                            <a href="#">Thể loại</a>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="col-lg-2 col-md-3 col-sm-4 col-6">
                                <div class="card-inner">
                                    <a href="#">
                                        <div class="box">
                                            <img src="assets/pic/highlights_pen.avif"/>
                                        </div>
                                    </a>
                                    <div class="card-info">
                                        <div class="card-title">
                                            <a href="#">Thể loại</a>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="col-lg-2 col-md-3 col-sm-4 col-6">
                                <div class="card-inner">
                                    <a href="#">
                                        <div class="box">
                                            <img src="assets/pic/highlights_pen.avif"/>
                                        </div>
                                    </a>
                                    <div class="card-info">
                                        <div class="card-title">
                                            <a href="#">Thể loại</a>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="col-lg-2 col-md-3 col-sm-4 col-6">
                                <div class="card-inner">
                                    <a href="#">
                                        <div class="box">
                                            <img src="assets/pic/highlights_pen.avif"/>
                                        </div>
                                    </a>
                                    <div class="card-info">
                                        <div class="card-title">
                                            <a href="#">Thể loại</a>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="col-lg-2 col-md-3 col-sm-4 col-6">
                                <div class="card-inner">
                                    <a href="#">
                                        <div class="box">
                                            <img src="assets/pic/highlights_pen.avif"/>
                                        </div>
                                    </a>
                                    <div class="card-info">
                                        <div class="card-title">
                                            <a href="#">Thể loại</a>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <!-- Slider -->
    <h2 class="section-title">Sản phẩm nổi bật</h2>
    <div class="slider-container swiper">
        <div class="card-wrapper">
            <ul class="card-list swiper-wrapper">
                <li class="card-item swiper-slide">
                    <a href="product-detail.jsp" class="card-link">
                        <img
                                src="./assets/pic/sample_pic_bestseller"
                                alt=""
                                class="card-image"
                        />
                        <p class="badge">Sổ tay</p>
                        <h2 class="card-title">99.000đ</h2>
                        <button class="card-button">
                            <i
                                    class="fa-solid fa-arrow-right"
                                    style="font-size: 1rem"
                            ></i>
                        </button>
                    </a>
                </li>
                <li class="card-item swiper-slide">
                    <a href="product-detail.jsp" class="card-link">
                        <img src="./assets/pic/2pens.avif" alt="" class="card-image"/>
                        <p class="badge">Sổ tay</p>
                        <h2 class="card-title">99.000đ</h2>
                        <button class="card-button">
                            <i
                                    class="fa-solid fa-arrow-right"
                                    style="font-size: 1rem"
                            ></i>
                        </button>
                    </a>
                </li>
                <li class="card-item swiper-slide">
                    <a href="product-detail.jsp" class="card-link">
                        <img
                                src="./assets//pic/highlights_pen.avif"
                                alt=""
                                class="card-image"
                        />
                        <p class="badge">Sổ tay</p>
                        <h2 class="card-title">99.000đ</h2>
                        <button class="card-button">
                            <i
                                    class="fa-solid fa-arrow-right"
                                    style="font-size: 1rem"
                            ></i>
                        </button>
                    </a>
                </li>
                <li class="card-item swiper-slide">
                    <a href="product-detail.jsp" class="card-link">
                        <img
                                src="./assets/pic/notebook-flower.avif"
                                alt=""
                                class="card-image"
                        />
                        <p class="badge">Sổ tay</p>
                        <h2 class="card-title">99.000đ</h2>
                        <button class="card-button">
                            <i
                                    class="fa-solid fa-arrow-right"
                                    style="font-size: 1rem"
                            ></i>
                        </button>
                    </a>
                </li>
                <li class="card-item swiper-slide">
                    <a href="product-detail.jsp" class="card-link">
                        <img
                                src="./assets/pic/notebook-flower.avif"
                                alt=""
                                class="card-image"
                        />
                        <p class="badge">Sổ tay</p>
                        <h2 class="card-title">99.000đ</h2>
                        <button class="card-button">
                            <i
                                    class="fa-solid fa-arrow-right"
                                    style="font-size: 1rem"
                            ></i>
                        </button>
                    </a>
                </li>
                <li class="card-item swiper-slide">
                    <a href="product-detail.jsp" class="card-link">
                        <img
                                src="./assets/pic/notebook-flower.avif"
                                alt=""
                                class="card-image"
                        />
                        <p class="badge">Sổ tay</p>
                        <h2 class="card-title">99.000đ</h2>
                        <button class="card-button">
                            <i
                                    class="fa-solid fa-arrow-right"
                                    style="font-size: 1rem"
                            ></i>
                        </button>
                    </a>
                </li>
            </ul>

            <!-- If we need pagination -->
            <div class="swiper-pagination"></div>

            <!-- If we need navigation buttons -->
            <div class="swiper-slide-button swiper-button-prev"></div>
            <div class="swiper-slide-button swiper-button-next"></div>
        </div>
    </div>

    <!-- Slider -->
</main>
<jsp:include page="footer.jsp"></jsp:include>
<!-- Swpier JS script -->
<script src="https://cdn.jsdelivr.net/npm/swiper@11/swiper-bundle.min.js"></script>
<script src="assets/js/swpier.js"></script>
</body>
</html>
