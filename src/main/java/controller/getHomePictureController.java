package controller;

import dao.HomePictureDAO;
import entity.HomePicture;
import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "getHomePictureController", value = "/HomePic")
public class getHomePictureController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HomePictureDAO homePicDAO = new HomePictureDAO();
        List<HomePicture> homePic= homePicDAO.getHomePic();
        for (HomePicture homePicture : homePic) {
            System.out.println(homePicture);

        }
        request.setAttribute("homepictures", homePic);
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}