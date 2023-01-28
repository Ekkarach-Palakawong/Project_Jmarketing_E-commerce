package ku.cs.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class User {
    private String name;
    private String userName;
    private String password;
    private Boolean status;
    private String type;
    private String lastDateLogin;
    private String lastTimeLogin;
    private int totalContinueLogin;
    private String report;
    private double totalPay;//default จะเป็นผู้ซื้อสินค้า
    private String userShopName;
    private String pathImage;

    //ต้องหาตัวมาเก็บรูปจากหน้า register
    public User(String userName,String password){
        this.userName=userName;
        this.password=password;
        this.totalContinueLogin = 0;
        this.totalPay = 0.0;
        this.status= true;
        this.type = "User";
        this.report = "Not reported";
        this.userShopName = "No shop";
        this.pathImage= "images/userimage/default-User.png";
        this.lastDateLogin = "waiting for update";
        this.lastTimeLogin = "waiting for update";
        checkLastLogin();
    }
    public User(String name, String userName, String password){
        this(userName,password);
        this.name=name;
    }
    public User(String type,String name ,String userName ,String password){
        this(name,userName,password);
        this.type=type;
    }
    public User(String type, String name , String userName , boolean status){
        this.type=type;
        this.name=name;
        this.userName=userName;
        this.status=status;
        this.totalPay = 0.0;
        this.totalContinueLogin = 0;
        this.lastDateLogin = "waiting for update";
        this.lastTimeLogin = "waiting for update";
        this.report = "Not reported";
        this.pathImage= "images/default-User.png";
        checkLastLogin();
    }
    public User(String type, String name , String userName, String password, boolean status, String userShopName,int totalContinueLogin,String report) {
        this(type, name, userName, status);
        this.password = password;
        this.userShopName = userShopName;
        this.report = report;
        this.totalContinueLogin = totalContinueLogin;
    }

    public String getName(){ return name; }
    public String getUserName(){ return userName; }
    public Boolean getStatus(){ return status; }
    public Double getTotalPay() { return this.totalPay; }
    public String getLastDateLogin() { return lastDateLogin; }
    public  String getLastTimeLogin(){return  lastTimeLogin;}
    public String getPassword(){ return password; }
    public String getType() { return type; }
    public String getUserShopName() { return userShopName; }
    public  int getTotalContinueLogin(){return totalContinueLogin;}
    public String getReport(){return  report;}
    public String getPathImage(){return pathImage;}

    public void setImagePath(String pathImage) {
        this.pathImage = pathImage;
    }
    public void setName(String name) { this.name = name; }
    public void setUserName(String userName) { this.userName = userName; }
    public void setPassword(String password) { this.password = password; }
    public void setStatus(boolean status) { this.status = status; }//เอาไว้เปลี่ยนเวลาโดน admin disable
    public void setType(String type){ this.type=type; }
    public void setUserShopName(String userShopName) {
        this.userShopName = userShopName;
    }
    public void setTotalContinueLogin(int tryToLogin){totalContinueLogin = tryToLogin;}
    public void setTotalPay(double totalPay){this.totalPay = totalPay;}

    public StringProperty nameProperty() {
        StringProperty username = new SimpleStringProperty(this.userName);
        return username; }

    public  StringProperty dateProperty() {
        StringProperty date = new SimpleStringProperty(this.lastDateLogin);
        return  date; }

    public StringProperty timeProperty() {
        StringProperty time = new SimpleStringProperty(this.lastTimeLogin);
        return time; }

    public void report(){this.report = "reported";}
    public Boolean isOpenShop()
    {
        if(getUserShopName().equals("No shop")){return false;}
        return  true;
    }

    public Boolean isLogin()
    {
        if(this.lastTimeLogin.equals("waiting for update") || this.lastDateLogin.equals("waiting for update")) { return false; }
        return  true;
    }

    public void buyProduct(double price)
    { this.totalPay += price; }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name  +
                ", userName='" + userName +
                ", password='" + password +
                ", lastDateLogin='" + lastDateLogin +
                ", lastTimeLogin='" + lastTimeLogin +
                ", totalPay=" + totalPay +
                ", userShopName='" + userShopName +
                ", pathImage='" + pathImage +
                '}';
    }

    public String toCsvPattern()
    {
        return "User," + name + "," + userName +"," +password + "," + status + "," + userShopName
                + ","+totalContinueLogin+","+totalPay +"," +report+","+pathImage+"\n";
    }

    public boolean checkEmpty(){
        if (this.userName.equals("") ||
                this.name.equals("") ||
                this.password.equals("") )
        {
            return true;
        }
        return false;
    }
    public boolean checkUserName(){//for register
        String path = "csv/AllUser.csv";
        BufferedReader buffer = null;
        FileReader fileReader = null;
        try {
            //read file
            fileReader = new FileReader(path);
            buffer = new BufferedReader(fileReader);
            String line = null;

            while ((line = buffer.readLine()) != null) {
                String[] data = line.split(",");
                String userName = data[2].trim();
                if (this.userName.equals(userName)){
                    return true;
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Cannot read file " + path);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                buffer.close();
                fileReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public int checkPasswordAndStatus() {
        String path = "csv/AllUser.csv";
        BufferedReader buffer = null;
        FileReader fileReader = null;
        try {
            //read file
            fileReader = new FileReader(path);
            buffer = new BufferedReader(fileReader);
            String line = null;

            while ((line = buffer.readLine()) != null) {
                String[] data = line.split(",");
                String userName = data[2].trim();
                String password =data[3].trim();
                String status =data[4].trim();
                if (this.userName.equals(userName)) {
                    if (this.password.equals(password)){
                        if (Boolean.parseBoolean(status)){
                            return 3;
                        }
                        return 2;
                    }
                    return 1;
                }
            }
        } catch (FileNotFoundException e){
            System.err.println("Cannot read file " + path);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                buffer.close();
                fileReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }return 0;
    }
    public User readLoginReport(){
        String path = "csv/LoginReport.csv";
        BufferedReader buffer = null;
        FileReader fileReader = null;
        try {
            //read file
            fileReader = new FileReader(path);
            buffer = new BufferedReader(fileReader);
            String line = null;
            String str=null;

            line = buffer.readLine();
            while (line!= null) {
                if ((str = buffer.readLine()) == null) {
                    String[] data = line.split(",");
                    String type = data[0].trim();
                    String name = data[1].trim();
                    String userName = data[2].trim();
                    String status = data[3].trim();
                    return new User(type,name,userName,Boolean.parseBoolean(status));
                }
                line = str;
            }

        } catch (FileNotFoundException e){
            System.err.println("Cannot read file " + path);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                buffer.close();
                fileReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public User login() {
        String path = "csv/AllUser.csv";
        BufferedReader buffer = null;
        FileReader fileReader = null;

        FileWriter fileWriter = null;
        BufferedWriter out = null;
        try {
            //read file
            fileReader = new FileReader(path);
            buffer = new BufferedReader(fileReader);
            String line = null;

            fileWriter = new FileWriter("csv/LoginReport.csv", true);
            out = new BufferedWriter(fileWriter);

            while ((line = buffer.readLine()) != null) {
                String[] data = line.split(",");
                String type = data[0].trim();
                String name = data[1].trim();
                String userName = data[2].trim();
                String password =data[3].trim();
                String status =data[4].trim();
                if(this.userName.equals(userName)){
                    //SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    out.write(type +","+ name +","+ userName +","+ status +","+ java.time.LocalDateTime.now());
                    out.newLine();
                    return new User(type, name , userName , password);
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Cannot read file " + path);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
                buffer.close();
                fileReader.close();
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void registerDataInFile(String imagePath) {
        String path = "csv/AllUser.csv";
        FileWriter fileWriter = null;
        BufferedWriter out = null;
        try {
            //writer
            fileWriter = new FileWriter(path, true);
            out = new BufferedWriter(fileWriter);
            if (imagePath==null){
                out.write(type + "," + name + "," + userName
                        + "," + password + "," + status + "," + userShopName
                        + "," +totalContinueLogin +"," +totalPay+","+report+","+pathImage);
            }else {out.write(type + "," + name + "," + userName
                    + "," + password + "," + status + "," + userShopName
                    + "," +totalContinueLogin +"," +totalPay+","+report+","+imagePath); }
            out.newLine();
        } catch (Exception e){
            System.out.println(e.getMessage());
        }finally {
            try {
                out.close();
                fileWriter.close();
            } catch (IOException e) {
                System.err.println("Error closing files");
            }
        }
    }

    public void checkLastLogin()
    {
        try {
            Path file = Paths.get("csv/LoginReport.csv");
            BufferedReader reader = Files.newBufferedReader(file , StandardCharsets.UTF_8);
            String line = null;

            while ((line = reader.readLine()) != null) {
                String[] temp = line.split(",");
                String[] dateTime = temp[4].split("T");
                if(temp[2].equals(this.userName)) {
                    this.lastDateLogin = dateTime[0];
                    this.lastTimeLogin = dateTime[1];}
            }

            reader.close();
        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
        }
    }




}