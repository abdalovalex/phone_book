package filter;

import Model.ContactModel;
import db.PostgresConnector;
import utils.Common;
import utils.StoreConnection;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;

@WebFilter(filterName = "ContactIUpdateFilter", urlPatterns = {"/contact-update"})
public class ContactIUpdateFilter implements Filter
{
    @Override
    public void init(FilterConfig filterConfig) throws ServletException
    {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException
    {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // Проверка, что пришедший ид является целочисленым типом
        if (!Common.isId(request.getParameter("id")))
        {
            request.getSession().setAttribute("error_not_find_contact", "Контакт не найден!");
            response.sendRedirect("/home");
            return;
        }

        int id = Common.stingToInt(request.getParameter("id"));
        Connection connection = StoreConnection.getStoreConnection(request);
        // Проверка, что запись контакта существует по пришедшему ид
        if (!ContactModel.isExistContactById(connection, id))
        {
            request.getSession().setAttribute("error_not_find_contact", "Контакт не найден!");
            PostgresConnector.close(connection);
            response.sendRedirect("/home");
            return;
        }

        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy()
    {

    }
}
