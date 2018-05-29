package Bean;

import java.util.ArrayList;

/**
 * Bean контакт
 */
public class Contact
{
    private Integer id;
    private String name;
    private String address;
    // Список телефонов контакта
    private ArrayList<Phone> phone;
    // Список соц. сетей контакта
    private ArrayList<SocialLink> link;

    public ArrayList<SocialLink> getLink()
    {
        return link;
    }

    public void setLink(ArrayList<SocialLink> link)
    {
        this.link = link;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public ArrayList<Phone> getPhone()
    {
        return phone;
    }

    public void setPhone(ArrayList<Phone> phone)
    {
        this.phone = phone;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }
}
