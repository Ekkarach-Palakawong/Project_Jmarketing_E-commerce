package ku.cs.models;

import java.util.ArrayList;
import java.util.Comparator;

public class AdminList
{
    private ArrayList<Admin> adminList;
    public AdminList() {
        adminList = new ArrayList<>();
    }

    public void addAdmin(Admin admin) {
        adminList.add(admin);
    }


    public Admin login(String name)
    {
        for(Admin admin : adminList)
        {
            if(admin.getUserName().equals(name)){return admin;}
        }
        return null;
    }

    public String toCsvPattern() {
        String temp = "";
        for (Admin admin : adminList)
        {
            temp += admin.toCsvPattern();
        }
        return  temp;
    }
}

