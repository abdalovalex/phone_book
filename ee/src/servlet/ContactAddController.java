package servlet;

import db.PhoneBookModel;
import utils.StoreConnection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

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
        try
        {
            System.out.println(PhoneBookModel.add(connection, 1, req));
            connection.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        req.getServletContext().getRequestDispatcher("/WEB-INF/view/contactAdd.jsp").forward(req, resp);
    }
}
