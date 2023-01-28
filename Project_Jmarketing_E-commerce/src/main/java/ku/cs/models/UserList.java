package ku.cs.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class UserList {

    private ArrayList<User> userList;

    public UserList() {
        userList = new ArrayList<User>();
    }

    public void addUser(User user) {
        userList.add(user);
    }

    public ArrayList<User> getAllUser() {
        return userList;
    }

    public void sortList() {
        Comparator<User> dateComparator = (o1, o2) -> {
                if(o1.getLastDateLogin().compareTo(o2.getLastDateLogin()) > 0){
                    if(o1.isLogin() && o2.isLogin()) { return -1; }
                    return 1; }
                else if(o1.getLastDateLogin().compareTo(o2.getLastDateLogin()) < 0) {
                    if (o1.isLogin() && o2.isLogin()) { return 1; }
                    return -1; }
                return compareTime(o1,o2);
        };
        Collections.sort(this.userList, dateComparator);
    }

    public int compareTime(User o1,User o2)
    {
        if(o1.getLastTimeLogin().compareTo(o2.getLastTimeLogin()) > 0){return -1;}
        else if(o1.getLastTimeLogin().compareTo(o2.getLastDateLogin()) < 0){return 1;}
        return 0;
    }



    public User searchUserName(String name)
    {
        for(User user : userList) {
            if(user.getUserName().equals(name)) {
                return user;
            }
        }
        return null;
    }

    public User searchUserShopName(String userShopName) {
        for (User user : userList) {
            if (user.getUserShopName().equals(userShopName)) {
                return user;
            }
        }
        return null;
    }


    public User isThisUserReported(String name)
    {
        for(User users : userList)
        {
            if(users.getUserName().equals(name)){return users;}
        }
        return null;
    }


    public boolean searchUserOpenShopName(String userShopName) {
        for (User user : userList) {
            if (user.getUserShopName().equals(userShopName)) {
                System.out.println(user.getUserShopName());
                return true;
            }
        }
        return false;
    }

    public String toCsvPattern() {
        String temp = "";
        for (User user : userList)
        {
            temp += user.toCsvPattern();
        }
        return  temp;
    }

}
