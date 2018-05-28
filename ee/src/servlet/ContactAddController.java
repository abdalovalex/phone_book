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

@WebServlet(urlPatterns = {"/contact-add"})
public class ContactAddController extends HttpServlet
{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        req.getServletContext().getRequestDispatcher("/WEB-INF/view/contactAdd.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        Connection connection = StoreConnection.getStoreConnection(req);
        User user = (User) req.getSession().getAttribute("User");

        // Добавленгие контакта
        if (!ContactModel.add(connection, user.getId(), req))
            req.getSession().setAttribute("error_add", "Не удалось добавить контакт");

        PostgresConnector.close(connection);
        req.getServletContext().getRequestDispatcher("/WEB-INF/view/contactAdd.jsp").forward(req, resp);
    }
}
