<%@ page import="entity.AboutUsPic" %>
<%@ page import="java.util.List" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page isELIgnored="false" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>About Us</title>
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
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
    <link
      rel="stylesheet"
      href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css"
    />
    <link rel="stylesheet" href="assets/css/styleAboutUs.css" />
  </head>
  <body>
  <jsp:include page="header.jsp"></jsp:include>

  <% List<AboutUsPic> list = (List<AboutUsPic>) request.getAttribute("aboutuspictures");%>
  <main id="about-us-container" style="margin-top: 4rem">
      <div class="background-split back-1">
        <h1>Sứ Mệnh của Chúng Tôi</h1>
        <p>
          Với sứ mệnh mang đến những sản phẩm văn phòng phẩm chất lượng và tiện
          lợi, chúng tôi không ngừng tìm kiếm và cung cấp những mặt hàng tốt
          nhất phục vụ cho công việc, học tập và cuộc sống hằng ngày. Đội ngũ
          của chúng tôi luôn cam kết đảm bảo sự hài lòng của khách hàng, từ chất
          lượng sản phẩm đến dịch vụ giao hàng nhanh chóng, chu đáo.
        </p>
      </div>
      <div class="background-split back-2">
        <img src="assets/pic/Aboutus/<%= list.get(0).getImg1()%>" alt="Background Image 1" />
      </div>
      <div class="background-split back-3">
        <img src="assets/pic/Aboutus/<%= list.get(0).getImg4()%>" alt="Background Image 2" />
      </div>
      <div class="background-split back-4">
        <h1>Chất Lượng Là Trên Hết</h1>
        <p>
          Chúng tôi hiểu rằng chất lượng sản phẩm là điều mà khách hàng quan tâm
          hàng đầu. Vì vậy, mỗi sản phẩm văn phòng phẩm được lựa chọn kỹ lưỡng
          từ những nhà cung cấp uy tín, đảm bảo sự bền bỉ và tiện ích cho người
          dùng. Chúng tôi mong muốn khách hàng không chỉ nhận được sản phẩm tốt
          mà còn có trải nghiệm mua sắm đáng tin cậy và thoải mái.
        </p>
      </div>
      <div class="background-split back-5">
        <h1>Đội Ngũ Tận Tâm và Chuyên Nghiệp</h1>
        <p>
          Sự thành công của chúng tôi không chỉ đến từ sản phẩm, mà còn nhờ vào
          đội ngũ nhân viên giàu kinh nghiệm và nhiệt huyết. Chúng tôi luôn sẵn
          sàng lắng nghe, hỗ trợ và giải đáp mọi thắc mắc của khách hàng để mang
          lại trải nghiệm mua sắm tuyệt vời nhất. Đến với chúng tôi, bạn sẽ được
          phục vụ bởi những con người tận tâm và chu đáo.
        </p>
      </div>
      <div class="background-split back-6">
        <img src="assets/pic/Aboutus/<%= list.get(0).getImg2()%>" alt="Background Image 3" />
      </div>
      <div class="back_ground_aboutUS">
        <img src="assets/pic/Aboutus/<%= list.get(0).getImg3()%>" alt="">
        <div class="frame_element">
          <h2>Go further with a subscription</h2>
          <div class="icon-3-background">
            <div class="icon">
              <i class="fa-solid fa-percent"></i>
              <p>20% off every order</p>
            </div>
            <div class="icon">
              <i class="fa-solid fa-headphones-simple"></i>
              <p>Priority customer service</p>
            </div>
            <div class="icon">
              <i class="fa-solid fa-box"></i>
              <p>Free shipping</p>
            </div>
          </div>
          <p class="subscription-description">
            Check one more thing off your list when you subscribe. Seamless
            auto-shipments, white glove customer service, and the best price in
            town. Arrives at your door every month so you can keep on going.
          </p>
        </div>
      </div>
    </main>
    
    <section id="intro-team">
      <h2 style="text-align: center">Meet Our Team</h2>
      <div class="my-team">
        <div class="item">
          <img
            src="assets/pic/Aboutus/<%= list.get(0).getMember1()%>"
            alt="Image 1"
          />
          <h3>Tùng Đỗ</h3>
          <p>CEO</p>
        </div>
        <div class="item">
          <img src="assets/pic/Aboutus/<%= list.get(0).getMember3()%>" alt="Image 2" />
          <h3>Thảoo Vânn</h3>
          <p>Co.Founder</p>
        </div>
        <div class="item">
          <img src="assets/pic/Aboutus/<%= list.get(0).getMember2()%>" alt="Image 3" />
          <h3>Trần Khải</h3>
          <p>Co.Founder</p>
        </div>
      </div>
    </section>
  <jsp:include page="footer.jsp"></jsp:include>


  </body>
</html>
