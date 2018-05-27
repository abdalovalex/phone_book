package filter;

import db.PostgresConnector;
import utils.StoreConnection;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
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

        String servletPath = request.getServletPath();
        Pattern regexp = Pattern.compile(".*(.css)|(.js)$");
        Matcher m = regexp.matcher(servletPath);

        /*
          Если это файлы css/js то пропускаем
          Иначе добавляем коннект в request
         */
        if (m.find())
        {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        else
        {
            Connection connection = null;
            try
            {
                connection = PostgresConnector.connection();
                StoreConnection.storeConnection(servletRequest, connection);
                filterChain.doFilter(servletRequest, servletResponse);
                if (connection != null)
                {
                    System.out.println("Close connection");
                    PostgresConnector.close(connection);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
                if (connection != null)
                    PostgresConnector.close(connection);
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
