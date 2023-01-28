package ku.cs.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import com.github.saacsos.FXRouter;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import ku.cs.models.User;
import ku.cs.models.UserList;
import ku.cs.services.DataSource;
import ku.cs.services.Transitional;
import ku.cs.services.Writer.ReportedUserCsvWriter;
import ku.cs.services.Writer.UserCsvWriter;

import java.io.IOException;

public class LoginController {
    private int tryToLogin = 0;
    private DataSource dataSource;
    private UserList userList;
    private UserList reportedList;
    private User reported;
    private User users;
    private Transitional transitional;

    @FXML private TextField loginUserName,loginPassword;
    @FXML private Label userNameReport,passwordReport;
    @FXML private AnchorPane anchorPane;

    @FXML
    public void initialize() {
        transitional = new Transitional(anchorPane);
        transitional.fadeIn();
        dataSource = new DataSource();
        userList = dataSource.getUserList();
        reportedList = dataSource.getReportedlist();
        setDataAndShow();
    }

//method ทั่วๆไป
    @FXML
    private void setDataAndShow() {
        userNameReport.setText("");
        passwordReport.setText("");
    }
//    method handle link ปุ่ม
    @FXML
    public void handleBackButton(ActionEvent actionEvent){
        try {
            // เปลี่ยนการแสดงผลไปที่ route ที่ชื่อ home
            FXRouter.goTo("home");
        } catch (IOException e) {
            System.err.println("ไปที่หน้า home ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
        }
    }

    public void handleLoginButton(ActionEvent actionEvent) {
        try {
            String username = loginUserName.getText();
            User user = new User(username,loginPassword.getText());
            if (username.equals("") || loginPassword.getText().equals("")) {
                //แสดงผลไปที่หน้าผู้ใช้ว่า "โปรดใส่ข้อมูลให้ครบท่วน"
                setDataAndShow();
                if (loginUserName.getText().equals("")){
                    userNameReport.setText("โปรดใส่ข้อมูลให้ครบท่วน");
                }else {passwordReport.setText("โปรดใส่ข้อมูลให้ครบท่วน");}

            } else if (user.checkPasswordAndStatus()==0) {
                //แสดงผลไปที่หน้าผู้ใช้ว่า "ชื่อผู้ใช้ผิด"
                setDataAndShow();
                userNameReport.setText("ชื่อผู้ใช้ผิด");
                System.err.println("ชื่อผู้ใช้ผิด");

            } else if (user.checkPasswordAndStatus()==1) {
                //แสดงผลไปที่หน้าผู้ใช้ว่า "รหัสผ่านผิด"
                setDataAndShow();
                passwordReport.setText("รหัสผ่านผิด");
                System.err.println("รหัสผ่านผิด");

            } else if(user.checkPasswordAndStatus()==2){
                setDataAndShow();
                //แสดงผลไปที่หน้าผู้ใช้ว่า "โดนระงับบัญชี"
                userNameReport.setText("โดนระงับบัญชี");
                System.err.println("โดนระงับบัญชี");
                tryToLogin++;
                users = userList.searchUserName(loginUserName.getText());
                users.setTotalContinueLogin(tryToLogin);
                dataSource.reWriteData(new UserCsvWriter(),userList);

                reported = reportedList.searchUserName(loginUserName.getText());
                if(reported != null)
                { reported.setTotalContinueLogin(tryToLogin);
                    dataSource.reWriteData(new ReportedUserCsvWriter(), reportedList); }
            } else {
                User loginUser = user.login();
                if (loginUser.getType().equals("Admin")) {
                    FXRouter.goTo("admin",username);
                } else if (loginUser.getType().equals("User")) {
                    FXRouter.goTo("store",username);
                }
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }
}