package filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

@WebFilter(filterName = "EncodingFilter", urlPatterns = {"/*"})
public class EncodingFilter implements Filter
{
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException
    {
        servletRequest.setCharacterEncoding("UTF-8");

        filterChain.doFilter(servletRequest, servletResponse);
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
