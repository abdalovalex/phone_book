package servlet;

import Bean.Contact;
import Bean.User;
import Model.ContactModel;
import db.PostgresConnector;
import utils.StoreConnection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet(urlPatterns = {"/home"})
public class HomeController extends HttpServlet
{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        Connection connection = StoreConnection.getStoreConnection(req);
        try
        {
            User user = (User) req.getSession().getAttribute("User");
            // Ищем все контакты пользователя
            ArrayList<Contact> contactList = ContactModel.find(connection, user.getId());
            PostgresConnector.close(connection);

            req.setAttribute("contactList", contactList);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            req.getSession().setAttribute("error_contacts_find", "Не удалось найти контакты");
        }

        req.getServletContext().getRequestDispatcher("/WEB-INF/view/home.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        this.doGet(req, resp);
    }
}
