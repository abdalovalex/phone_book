package servlet;

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

@WebServlet(urlPatterns = {"/contact-delete"})
public class ContactDeleteController extends HttpServlet
{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        Connection connection = StoreConnection.getStoreConnection(req);
        User user = (User) req.getSession().getAttribute("User");

        // Удаление контакта
        if (!ContactModel.delete(connection, user.getId(), req))
            req.getSession().setAttribute("error_delete", "Не удалось удалить контакт");

        PostgresConnector.close(connection);
        resp.sendRedirect("/home");
    }
}
