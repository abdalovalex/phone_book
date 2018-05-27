package db;

import Bean.ContactRecord;
import Bean.Phone;
import Bean.SocialLink;

import javax.servlet.http.HttpServletRequest;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class PhoneBookModel
{
    public static ArrayList<ContactRecord> find(Connection connection, Integer user_id) throws SQLException
    {
        String sql = "select contact.id, contact.name, contact.address, " +
                "(select array_agg(phone) from public.phone where contact.id = contact_id) as phone, " +
                "(select array_agg(link) from public.social_link where contact.id = contact_id) AS social_link " +
                "from public.contact " +
                "where contact.user_id = ?" +
                "group by contact.id, contact.name, contact.address";
        PreparedStatement prepared = connection.prepareStatement(sql);
        prepared.setInt(1, user_id);
        ResultSet rs = prepared.executeQuery();

        ArrayList<ContactRecord> list = new ArrayList<>();
        while (rs.next())
        {
            ContactRecord contactRecord = new ContactRecord();
            contactRecord.setId(rs.getInt("id"));
            contactRecord.setName(rs.getString("name"));
            contactRecord.setAddress(rs.getString("address"));

            ArrayList<Phone> phoneList = new ArrayList<>();
            Array phonesArray = rs.getArray("phone");
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
            contactRecord.setPhone(phoneList);

            ArrayList<SocialLink> socialLinkList = new ArrayList<>();
            Array linksArray = rs.getArray("social_link");
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
            contactRecord.setLink(socialLinkList);

            list.add(contactRecord);
        }

        return list;
    }

    public static boolean add(Connection connection, Integer user_id, HttpServletRequest req) throws SQLException
    {
        String contact = req.getParameter("contact");
        String address = req.getParameter("address");

        String sql = "insert into public.contact (user_id, name, address) values(?, ?, ?) RETURNING ID";
        PreparedStatement prepared = connection.prepareStatement(sql);
        prepared.setInt(1, user_id);
        prepared.setString(2, contact);
        prepared.setString(3, address);
        ResultSet rs = prepared.executeQuery();

        int contact_id;
        if (rs.next())
            contact_id = rs.getInt(1);
        else
            return false;

        int count_row = 0;

        String[] phone = req.getParameterValues("Phone[]");
        for (String value : phone)
        {
            sql = "insert into public.phone (contact_id, phone) values(?, ?)";
            prepared = connection.prepareStatement(sql);
            prepared.setInt(1, contact_id);
            prepared.setString(2, value);
            count_row = prepared.executeUpdate();

            if (count_row == 0)
                return false;
        }

        String[] socialLink = req.getParameterValues("SocialLink[]");
        for (String value : socialLink)
        {
            sql = "insert into public.social_link (contact_id, link) values(?, ?)";
            prepared = connection.prepareStatement(sql);
            prepared.setInt(1, contact_id);
            prepared.setString(2, value);
            count_row = prepared.executeUpdate();

            if (count_row == 0)
                return false;
        }

        return true;

    }
}
