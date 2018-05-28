package utils;

import javax.servlet.ServletRequest;
import java.sql.Connection;


public class StoreConnection
{
    private static final String DB_CONNECTION = "DB_CONNECTION";

    public static void storeConnection(ServletRequest request, Connection connection)
    {
        request.setAttribute(DB_CONNECTION, connection);
    }

    public static Connection getStoreConnection(ServletRequest request)
    {
        return (Connection) request.getAttribute(DB_CONNECTION);
    }
}
