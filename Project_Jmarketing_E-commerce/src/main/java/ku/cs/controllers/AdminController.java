package ku.cs.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import ku.cs.models.Admin;
import ku.cs.models.AdminList;
import ku.cs.services.DataSource;
import ku.cs.services.Transitional;
import ku.cs.services.Writer.AdminCsvWriter;

import java.io.IOException;

public class AdminController
{
    @FXML private Label adminUserName;
    @FXML private ImageView adminPicture;
    @FXML private Label newPasswordLabel;
    @FXML private Label confirmPasswordLabel;
    @FXML private TextField newPasswordFieldText;
    @FXML private TextField confirmPasswordFieldText;
    @FXML private Button resetButton;
    @FXML private Label oldPasswordLabel;
    @FXML private Label differentPasswordLabel;
    @FXML private Label changeSuccessLabel;
    @FXML private AnchorPane anchorPane;
    private String username;
    private Admin admin;
    private AdminList adminList;
    private DataSource dataSource;
    private Transitional transitional;


    @FXML
    public void initialize()
    {
        transitional = new Transitional(anchorPane);
        transitional.fadeIn();
        username = (String) com.github.saacsos.FXRouter.getData();
        System.out.println(username);
        dataSource = new DataSource();
        adminList = dataSource.getAdminList();
        admin = adminList.login(username);
        showAdminData();
    }

    @FXML
    private void showAdminData()
    {
        String path = admin.getPathImage();
        adminPicture.setImage(new Image("file:"+path));
        adminUserName.setText(admin.getUserName());
        newPasswordLabel.setVisible(false);
        confirmPasswordLabel.setVisible(false);
        resetButton.setVisible(false);
        newPasswordFieldText.setVisible(false);
        confirmPasswordFieldText.setVisible(false);
        oldPasswordLabel.setVisible(false);
        differentPasswordLabel.setVisible(false);
        changeSuccessLabel.setVisible(false);
    }

//    handle link ปุ่ม

    @FXML
    public void handleResetButton(ActionEvent actionEvent)
    {
        oldPasswordLabel.setVisible(false);
        differentPasswordLabel.setVisible(false);
        changeSuccessLabel.setVisible(false);
        String pass = newPasswordFieldText.getText();
        String confirm = confirmPasswordFieldText.getText();
        if(pass.isBlank() || confirm.isBlank()) {return; }
        System.out.println(admin.toString());

        if(pass.equals(confirm))
        {
            if(admin.isUsedPassword(pass)){oldPasswordLabel.setVisible(true);}
            else{admin.resetPassword(pass);
                changeSuccessLabel.setVisible(true);dataSource.reWriteData(new AdminCsvWriter(),adminList);}
        }
        else{differentPasswordLabel.setVisible(true);}
        System.out.println( "New Pass : " +pass);
        System.out.println("Confirm Pass : " +confirm);
        System.out.println("-----------------");
    }

    @FXML
    public  void handleResetPasswordButton(ActionEvent actionEvent)
    {
        newPasswordLabel.setVisible(true);
        confirmPasswordLabel.setVisible(true);
        resetButton.setVisible(true);
        newPasswordFieldText.setVisible(true);
        confirmPasswordFieldText.setVisible(true);
    }


    @FXML
    public  void handleUserManageButton(ActionEvent actionEvent)
    {
        try {
            com.github.saacsos.FXRouter.goTo("usermanage",admin);
        } catch (IOException e) {
            System.err.println("ไปที่หน้า usermanage ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
        }
    }

    @FXML
    public  void handleLogoutButton(ActionEvent actionEvent)
    {
        try{
            com.github.saacsos.FXRouter.goTo("home");
        }catch(IOException e)
        {
            System.err.println("ไปที่หน้า home ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
        }
    }

    @FXML
    public  void handleProductTypeButton(ActionEvent actionEvent) {
        try {
            com.github.saacsos.FXRouter.goTo("producttype", admin);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    }