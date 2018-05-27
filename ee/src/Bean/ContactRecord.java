package Bean;

import java.util.ArrayList;

public class ContactRecord
{
    private Integer id;
    private String name;
    private String address;
    private ArrayList<Phone> phone;
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
