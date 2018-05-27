package db;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class PostgresConnector
{
    public static Connection connection()
    {
        try
        {
            Context context = (Context) new InitialContext().lookup("java:comp/env");
            DataSource dataSource = (DataSource) context.lookup("jdbc/app");

            return dataSource.getConnection();
        }
        catch (NamingException | SQLException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public static void close(Connection connection)
    {
        try
        {
            connection.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
}
