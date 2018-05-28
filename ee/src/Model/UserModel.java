package Model;

import Bean.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserModel
{
    /**
     * Поиск пользователя по логину паролю
     * @param connection соединение с БД
     * @param login логин
     * @param password пароль
     * @return объект User
     * @throws SQLException
     */
    public static User find(Connection connection, String login, char[] password) throws SQLException
    {
        String sql = "select * from public.user where username = ? and password = ?";
        PreparedStatement prepared = connection.prepareStatement(sql);
        prepared.setString(1, login);
        prepared.setString(2, String.valueOf(password));
        ResultSet rs = prepared.executeQuery();

        return fillUser(rs);
    }

    /**
     * Поиск пользователя по id
     * @param connection соединение с БД
     * @param id ид пользователя
     * @return объект User
     * @throws SQLException
     */
    public static User findById(Connection connection, int id) throws SQLException
    {
        String sql = "select * from public.user where id = ?";

        PreparedStatement prepared = connection.prepareStatement(sql);
        prepared.setInt(1, id);
        ResultSet rs = prepared.executeQuery();

        return fillUser(rs);
    }

    /**
     * Заполнение объекта пользователя по инормации из БД
     * @param rs результат запроса в БД
     * @return объект User
     * @throws SQLException
     */
    private static User fillUser(ResultSet rs) throws SQLException
    {
        User user = null;
        if (rs.next())
        {
            user = new User();
            user.setId(rs.getInt("id"));
            user.setName(rs.getString("username"));
        }

        return user;
    }
}
