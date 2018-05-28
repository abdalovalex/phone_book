package servlet;

import Bean.User;
import Model.UserModel;
import db.PostgresConnector;
import utils.StoreConnection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;

@WebServlet(urlPatterns = {"/login"})
public class LoginController extends HttpServlet
{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        req.getServletContext().getRequestDispatcher("/WEB-INF/view/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        String login = req.getParameter("login");
        char[] password = req.getParameter("password").toCharArray();
        String error = null;

        User user = null;
        Connection connection = StoreConnection.getStoreConnection(req);
        try
        {
            user = UserModel.find(connection, login, password);
            PostgresConnector.close(connection);
            if (user == null)
                error = "Неверные логин или пароль";
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        if (error == null)
        {
            // Сохраняем информацию о пользвоателе в куках на 1 час и в сессии.
            Cookie cookie = new Cookie("id", user.getId().toString());
            cookie.setMaxAge(3600);
            cookie.setHttpOnly(true);
            resp.addCookie(cookie);
            req.getSession().invalidate();
            // Таймаут сессии 15 минут.
            req.getSession().setMaxInactiveInterval(60 * 15);
            req.getSession().setAttribute("User", user);

            // "Удаляем" значение пароля
            Arrays.fill(password, ' ');
            resp.sendRedirect("/home");
        }
        else
        {
            req.setAttribute("error", error);
            req.getServletContext().getRequestDispatcher("/WEB-INF/view/login.jsp").forward(req, resp);
        }
    }
}
