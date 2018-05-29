package filter;

import db.PostgresConnector;
import utils.StoreConnection;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebFilter(filterName = "DBFilter", urlPatterns = {"/*"})
public class DBFilter implements Filter
{
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException
    {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String servletPath = request.getServletPath();
        if (servletPath.equals("/"))
        {
            response.sendRedirect("/home");
            return;
        }

        Pattern pattern = Pattern.compile(".*(.css)|(.js)$");
        Matcher matched = pattern.matcher(servletPath);

        /*
          Если это файлы css/js то пропускаем
          Иначе добавляем коннект в request
         */
        if (matched.find())
            filterChain.doFilter(servletRequest, servletResponse);
        else
        {
            Connection connection = null;
            try
            {
                connection = PostgresConnector.connection();
                if (connection == null)
                    throw new Exception("Не удалось связаться с БД!");

                // отключение автокоммита
                connection.setAutoCommit(false);
                StoreConnection.storeConnection(servletRequest, connection);
                filterChain.doFilter(servletRequest, servletResponse);

                PostgresConnector.close(connection);
            }
            catch (Exception e)
            {
                e.printStackTrace();
                if (connection != null)
                    PostgresConnector.close(connection);

                request.getSession().setAttribute("error_db", e.getMessage());
                request.getServletContext().getRequestDispatcher("/WEB-INF/view/error.jsp").forward(request, servletResponse);
            }
        }
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
