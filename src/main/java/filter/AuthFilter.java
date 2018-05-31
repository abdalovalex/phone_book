package filter;

import Bean.User;
import Model.UserModel;
import utils.StoreConnection;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebFilter(filterName = "AuthFilter", urlPatterns = {"/*"})
public class AuthFilter implements Filter
{
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException
    {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String servletPath = request.getServletPath();
        Pattern regexp = Pattern.compile(".*(.css)|(.js)$");
        Matcher matched = regexp.matcher(servletPath);

        // Если это файлы css/js то пропускаем
        if (matched.find())
        {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        // Если урл login, то пропускаем
        if (servletPath.equals("/login"))
        {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        // Если атрибут сессии User не пустой, значит пользователь все еще авторизован
        if (request.getSession().getAttribute("User") != null)
        {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        Cookie[] cookies = request.getCookies();

        // Если куки пустые перенаправляем на login
        if (cookies == null)
        {
            response.sendRedirect("/login");
            return;
        }

        for (Cookie cookie : cookies)
        {
            if (cookie.getName().equals("id"))
            {
                Connection connection = StoreConnection.getStoreConnection(servletRequest);
                try
                {
                    User user = UserModel.findById(connection, Integer.parseInt(cookie.getValue()));
                    // Если пользователь не найден, отправляем на страницу с авторизацией
                    if (user == null)
                    {
                        response.sendRedirect("/login");
                        return;
                    }

                    request.getSession().invalidate();
                    // Таймаут сессии 15 минут.
                    request.getSession().setMaxInactiveInterval(60 * 15);
                    request.getSession().setAttribute("User", user);
                }
                catch (SQLException e)
                {
                    response.sendRedirect("/login");
                    return;
                }

                filterChain.doFilter(request, servletResponse);
                return;
            }
        }

        response.sendRedirect("/login");
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException
    {

    }

    @Override
    public void destroy()
    {

    }
}
