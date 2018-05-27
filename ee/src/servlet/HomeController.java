package servlet;

import Bean.ContactRecord;
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
            ArrayList<ContactRecord> phoneBook = PhoneBookModel.find(connection, 1);
            connection.close();

            req.setAttribute("phoneBook", phoneBook);

        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        req.getServletContext().getRequestDispatcher("/WEB-INF/view/home.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        this.doGet(req, resp);
    }
}
