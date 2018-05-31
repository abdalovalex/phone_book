package Model;

import Bean.Contact;
import Bean.Phone;
import Bean.SocialLink;
import utils.Common;

import javax.servlet.http.HttpServletRequest;
import java.sql.*;
import java.util.ArrayList;

public class ContactModel
{
    /**
     * Поиск контакта по ид пользователя
     *
     * @param connection соединение с БД
     * @param user_id    ид пользователя
     * @return ArrayList<Contact> Список объектов Contact
     * @throws SQLException
     */
    public static ArrayList<Contact> find(Connection connection, Integer user_id) throws SQLException
    {
        String sql = "SELECT contact.id, contact.name, contact.address, " +
                "(SELECT array_agg(phone) FROM public.phone WHERE contact.id = contact_id) AS phone, " +
                "(SELECT array_agg(link) FROM public.social_link WHERE contact.id = contact_id) AS social_link " +
                "FROM public.contact " +
                "WHERE contact.user_id = ? " +
                "ORDER BY contact.name";
        PreparedStatement prepared = connection.prepareStatement(sql);
        prepared.setInt(1, user_id);
        ResultSet rs = prepared.executeQuery();

        ArrayList<Contact> list = new ArrayList<>();
        while (rs.next())
        {
            Contact contact = new Contact();
            contact.setId(rs.getInt("id"));
            contact.setName(rs.getString("name"));
            contact.setAddress(rs.getString("address"));

            // Заполнение контакта телефонами, если есть
            ArrayList<Phone> phoneList = new ArrayList<>();
            Array phonesArray = rs.getArray("phone");
            fillPhone(phoneList, phonesArray);
            contact.setPhone(phoneList);

            // Заполнение контакта соц.сетями, если есть
            ArrayList<SocialLink> socialLinkList = new ArrayList<>();
            Array linksArray = rs.getArray("social_link");
            fillSocialLink(socialLinkList, linksArray);
            contact.setLink(socialLinkList);

            list.add(contact);
        }

        return list;
    }

    /**
     * Поиск контакта по ид
     *
     * @param connection соединение с БД
     * @param user_id    ид пользователя
     * @param req        request
     * @return Contact
     * @throws SQLException
     */
    public static Contact findById(Connection connection, Integer user_id, HttpServletRequest req) throws SQLException
    {
        int id = Common.stingToInt(req.getParameter("id"));

        String sql = "SELECT contact.id, contact.name, contact.address, " +
                "(SELECT array_agg(phone) FROM public.phone WHERE contact.id = contact_id) AS phone, " +
                "(SELECT array_agg(link) FROM public.social_link WHERE contact.id = contact_id) AS social_link " +
                "FROM public.contact " +
                "WHERE contact.id = ? " +
                "AND contact.user_id = ? ";
        PreparedStatement prepared = connection.prepareStatement(sql);
        prepared.setInt(1, id);
        prepared.setInt(2, user_id);
        ResultSet rs = prepared.executeQuery();

        Contact contact = new Contact();
        if (rs.next())
        {
            contact.setId(rs.getInt("id"));
            contact.setName(rs.getString("name"));
            contact.setAddress(rs.getString("address"));

            // Заполнение контакта телефонами, если есть
            ArrayList<Phone> phoneList = new ArrayList<>();
            Array phonesArray = rs.getArray("phone");
            fillPhone(phoneList, phonesArray);
            contact.setPhone(phoneList);

            // Заполнение контакта соц.сетями, если есть
            ArrayList<SocialLink> socialLinkList = new ArrayList<>();
            Array linksArray = rs.getArray("social_link");
            fillSocialLink(socialLinkList, linksArray);
            contact.setLink(socialLinkList);
        }

        return contact;
    }

    /**
     * Заполнения массива соц. сетями для одного контакта
     *
     * @param socialLinkList Список для заполнения объектами соц. сетей
     * @param linksArray     Массив соц. сетей из БД
     * @throws SQLException
     */
    private static void fillSocialLink(ArrayList<SocialLink> socialLinkList, Array linksArray) throws SQLException
    {
        if (linksArray != null)
        {
            String[] links = (String[]) linksArray.getArray();

            for (String value : links)
            {
                SocialLink socialLink = new SocialLink();
                socialLink.setLink(value);
                socialLinkList.add(socialLink);
            }
        }
    }

    /**
     * Заполнения массива телефонами для одного контакта
     * @param phoneList Список для заполнения объектами телеон
     * @param phonesArray Массив телефонов из БД
     * @throws SQLException
     */
    private static void fillPhone(ArrayList<Phone> phoneList, Array phonesArray) throws SQLException
    {
        if (phonesArray != null)
        {
            String[] phones = (String[]) phonesArray.getArray();

            for (String value : phones)
            {
                Phone phone = new Phone();
                phone.setPhone(value);
                phoneList.add(phone);
            }
        }
    }

    /**
     * Добавление контакта
     *
     * @param connection соединение с БД
     * @param user_id    ид пользователя
     * @param req        request
     * @return boolean
     */
    public static boolean add(Connection connection, Integer user_id, HttpServletRequest req)
    {
        try
        {
            String name = req.getParameter("name");
            String address = req.getParameter("address");

            // Первый шаг, вставляем контакт
            String sql = "INSERT INTO public.contact (user_id, name, address) VALUES(?, ?, ?) RETURNING ID";
            PreparedStatement prepared = connection.prepareStatement(sql);
            prepared.setInt(1, user_id);
            prepared.setString(2, name);
            prepared.setString(3, address);
            ResultSet rs = prepared.executeQuery();

            // Вытаскиваем id контакта новой записи
            int contact_id;
            if (rs.next())
                contact_id = rs.getInt(1);
            else
            {
                connection.rollback();
                return false;
            }

            int count_row;

            // Добавление телефонов
            String[] phone = req.getParameterValues("Phone[]");
            for (String value : phone)
            {
                sql = "INSERT INTO public.phone (contact_id, phone) VALUES(?, ?)";
                prepared = connection.prepareStatement(sql);
                prepared.setInt(1, contact_id);
                prepared.setString(2, value);
                count_row = prepared.executeUpdate();

                if (count_row == 0)
                {
                    connection.rollback();
                    return false;
                }
            }

            // Добавление соц.сетей
            String[] socialLink = req.getParameterValues("SocialLink[]");
            for (String value : socialLink)
            {
                sql = "INSERT INTO public.social_link (contact_id, link) VALUES(?, ?)";
                prepared = connection.prepareStatement(sql);
                prepared.setInt(1, contact_id);
                prepared.setString(2, value);
                count_row = prepared.executeUpdate();

                if (count_row == 0)
                {
                    connection.rollback();
                    return false;
                }
            }

            connection.commit();
        }
        catch (SQLException ignored)
        {
            req.getSession().setAttribute("error_add", "Не удалось добавить контакт");
            try
            {
                connection.rollback();
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
            return false;
        }

        return true;
    }

    /**
     * Проверка, существует ли контакт с указаным id
     *
     * @param connection Соединение с БД
     * @param id         Идентифиактор
     * @return boolean
     */
    public static boolean isExistContactById(Connection connection, int id)
    {
        try
        {
            String sql = "SELECT EXISTS (SELECT id FROM public.contact WHERE id = ?) AS find";
            PreparedStatement prepared = connection.prepareStatement(sql);
            prepared.setInt(1, id);
            ResultSet rs = prepared.executeQuery();

            if (rs.next())
                return rs.getBoolean("find");
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Обновление контакта
     *
     * @param connection соединение с БД
     * @param user_id    ид пользователя
     * @param req        request
     * @return boolean
     */
    public static boolean update(Connection connection, Integer user_id, HttpServletRequest req)
    {
        // Валидация данных

        return true;
    }


    /**
     * Удаление контакта
     *
     * @param connection соединение с БД
     * @param user_id    ид пользователя
     * @param req        request
     * @return boolean
     */
    public static boolean delete(Connection connection, Integer user_id, HttpServletRequest req)
    {
        try
        {
            int id = Integer.parseInt(req.getParameter("id"));
            String sql = "DELETE FROM contact WHERE id = ? AND user_id = ?";

            PreparedStatement prepared = connection.prepareStatement(sql);
            prepared.setInt(1, id);
            prepared.setInt(2, user_id);

            int count = prepared.executeUpdate();
            // Если кол-во удаленых строк равно 0, то считаем, что это ошибка
            if (count == 0)
            {
                connection.rollback();
                return false;
            }
            connection.commit();
        }
        catch (SQLException ignored)
        {
            try
            {
                connection.rollback();
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
            return false;
        }

        return true;
    }
}
