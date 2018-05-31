package servlet;

import Bean.Contact;
import Bean.User;
import Model.ContactModel;
import db.PostgresConnector;
import utils.Common;
import utils.StoreConnection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet(urlPatterns = {"/contact-update"})
public class ContactUpdateController extends HttpServlet
{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        User user = (User) req.getSession().getAttribute("User");
        Connection connection = StoreConnection.getStoreConnection(req);
        try
        {
            Contact contact = ContactModel.findById(connection, user.getId(), req);
            req.setAttribute("contact", contact);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            req.getSession().setAttribute("error_not_find_contact", "Контакт не найден!");
        }
        PostgresConnector.close(connection);

        req.getServletContext().getRequestDispatcher("/WEB-INF/view/contactUpdate.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        super.doPost(req, resp);
    }
}
