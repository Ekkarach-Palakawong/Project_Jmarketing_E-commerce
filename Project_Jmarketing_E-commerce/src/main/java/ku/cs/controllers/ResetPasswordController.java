package ku.cs.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import com.github.saacsos.FXRouter;
import javafx.scene.layout.AnchorPane;
import ku.cs.models.User;
import ku.cs.models.UserList;
import ku.cs.services.DataSource;
import ku.cs.services.Transitional;
import ku.cs.services.Writer.UserCsvWriter;

import java.io.IOException;

public class ResetPasswordController {
    @FXML private TextField newPasswordTxt,confirmPasswordTxt;
    @FXML private Label reportTxt;
    @FXML private AnchorPane anchorPane;

    private User user;
    private DataSource dataSource;
    private UserList userList;
    private Transitional transitional;

    @FXML
    public void initialize() {
        transitional = new Transitional(anchorPane);
        transitional.fadeIn();
        String username = (String) FXRouter.getData();
        dataSource = new DataSource();
        userList = dataSource.getUserList();
        user = userList.searchUserName(username);
        setDataAndShow();
    }

    @FXML
    private void setDataAndShow() {
        reportTxt.setText("");
    }
//    method handle link ปุ่ม
    @FXML
    public void handleBackButton(ActionEvent actionEvent){
        try {

            FXRouter.goTo("profile");
        } catch (IOException e) {
            System.err.println("ไปที่หน้า profile ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
        }
    }
    @FXML
    public void handleOkButton(ActionEvent actionEvent){
        try {
            String newPass=newPasswordTxt.getText();
            String ConfirmPass=confirmPasswordTxt.getText();
            if (newPass.equals("") || ConfirmPass.equals("")){
                reportTxt.setText("โปรดใส่ข้อมูลให้ครบถ้วน");
                System.err.println("โปรดใส่ข้อมูลให้ครบถ้วน");
            }else if(!newPass.equals(ConfirmPass)){
                reportTxt.setText("รหัสผ่านไม่ตรงกัน");
                System.err.println("รหัสผ่านไม่ตรงกัน");
            }else if(user.getPassword().equals(newPasswordTxt.getText())){
                reportTxt.setText("คุณใส่รหัสผ่านเก่า");
                System.err.println("คุณใส่รหัสผ่านเก่า");
            }else{
                user.setPassword(newPass);
                dataSource.reWriteData(new UserCsvWriter(),userList);
                FXRouter.goTo("profile");
            }
        } catch (IOException e) {
            System.err.println("ไปที่หน้า profile ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
        }
    }

}
