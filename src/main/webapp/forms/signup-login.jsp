<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page isELIgnored="false" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %><!DOCTYPE html>
<html>
  <head>
    <title>Đăng nhập - Đăng ký</title>
    <link
      rel="stylesheet"
      href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.0/css/all.min.css"
      integrity="sha512-9xKTRVabjVeZmc+GUW8GgSmcREDunMM+Dt/GrzchfN8tkwHizc5RP4Ok/MXFFy5rIjJjzhndFScTceq5e6GvVQ=="
      crossorigin="anonymous"
      referrerpolicy="no-referrer"
    />

    <script
      src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.7.1/jquery.min.js"
      integrity="sha512-v2CJ7UaYy4JwqLDIrZUI/4hqeoQieOmAZNXBeQyjo21dadnwR+8ZaIJVT8EE2iyI61OV8e6M8PP2/4hpQINQ/g=="
      crossorigin="anonymous"
      referrerpolicy="no-referrer"
    ></script>
    <link rel="stylesheet" href="../assets/css/styleFormSignup-login.css">
  </head>
  <body>
    <div class="login-signup-container">
      <main class="login-signup">
        <input type="checkbox" id="chk" aria-hidden="true" />
        <div class="signup">
          <form>
            <div class="form-group">
              <label for="chk" class="label-Signup" aria-hidden="true"
                >Đăng ký</label
              >
            </div>
            <div class="form-group">
              <label for="username" class="form-label"> </label>
              <input
                id="username"
                name="username"
                type="text"
                placeholder="Tên đăng nhập"
                class="form-control"
                required=""
              />
              <span class="form-message"></span>
            </div>
            <div class="form-group">
              <label for="email" class="form-label"> </label>
              <input
                id="email"
                name="email"
                type="email"
                placeholder="Email của bạn"
                class="form-control"
                required=""
              />
              <span class="form-message"></span>
            </div>
            <div class="form-group">
              <label for="password" class="form-label"> </label>
              <input
                id="password"
                name="password"
                type="password"
                placeholder="Mật khẩu"
                class="form-control"
                required=""
              />
              <span class="form-message"></span>
            </div>
            <div class="form-group">
              <label for="rePassword" class="form-label"> </label>
              <input
                id="rePassword"
                name="rePassword"
                type="password"
                placeholder="Nhập lại mật khẩu"
                class="form-control"
                required=""
              />

              <span class="form-message"></span>
            </div>
            <div class="btn-blue">
              <button>Đăng Kí</button>
            </div>
          </form>
        </div>

        <div class="login">
          <form>
            <label class="label-Login" for="chk" aria-hidden="true"
              >Đăng Nhập</label
            >
            <input type="email" name="email" placeholder="Email" required="" />
            <input
              type="password"
              name="pswd"
              placeholder="Password"
              required=""
            />
            <i class="fa-regular fa-eye icon-show-hide"></i>

            <a class="signup-image-link" href="/pages/forms/changePassword.jsp"
              >Quên mật khẩu?</a
            >
            <button>Login</button>
          </form>
        </div>
      </main>
    </div>
    <script>
      // ẩn hiện mật khẩu
      const password = $(".login input[type='password']");
      const password_signup = $(".signup input[type='password']");

      $(".icon-show-hide").on("click", function () {
        // Kiểm tra nếu trường mật khẩu hiện tại là 'password'
        if (password.attr("type") === "password") {
          password.attr("type", "text"); // Đổi thành text
          $(this).removeClass("fa-regular fa-eye");
          $(this).addClass("fa-regular fa-eye-slash");
        } else {
          password.attr("type", "password"); // Đổi lại thành password
          $(this).removeClass("fa-regular fa-eye-slash");
          $(this).addClass("fa-regular fa-eye");
        }
      });
    </script>
  </body>
</html>