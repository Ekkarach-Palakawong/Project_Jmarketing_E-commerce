package ku.cs.models;

import java.util.ArrayList;
import java.util.Collections;

public class Admin extends User
{
    public Admin(String type,String name ,String userName ,String password){
        super(type,name ,userName,password);
        setImagePath("images/"+getUserName()+".jpg");
    }

    public void disableUser(User userID) { userID.setStatus(false); }
    public void reactivateUser(User userID){ userID.setStatus(true);}

    public Boolean isUsedPassword(String pass)
    {
        if(super.getPassword().equals(pass)){return true;}
        else{return false;}
    }

    public void resetPassword(String password){super.setPassword(password);}

    public void addProductType(ProductTypeList productTypeList, String type)
    { ArrayList<String> value = new ArrayList<>();
        Collections.addAll(value," "," "," "," ");
        productTypeList.addProductType(type, value); }

    public void addNewAttribute(ProductTypeList productTypeList, String key, String attribute)
    { productTypeList.addNewAttribute(key,attribute); }

    public void removeAttribute(ProductTypeList productTypeList, String type, String attribute)
    { productTypeList.removeAttribute(type,attribute); }


    @Override
    public String toString() {
        return "Admin User : " + getUserName() + " name : " + getName() + " Pass : " + getPassword() + " Last login : "  +" Status: "+ getStatus();
    }

    public String toCsvPattern()
    {
        return "Admin," + getName() + "," + getUserName() +"," +getPassword() + "," + getStatus() +","+getPathImage()+"\n";
    }






}