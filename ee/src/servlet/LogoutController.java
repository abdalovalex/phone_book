package servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = {"/logout"})
public class LogoutController extends HttpServlet
{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        req.getSession().invalidate();

        Cookie[] cookies = req.getCookies();
        for (Cookie cookie: cookies)
        {
            if (cookie.getName().equals("id"))
            {
                cookie.setMaxAge(0);
                cookie.setValue(null);
                resp.addCookie(cookie);
                break;
            }
        }
        resp.sendRedirect("/login");
    }
}
