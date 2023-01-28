package ku.cs.controllers;

import javafx.animation.Transition;
import javafx.fxml.FXML;
import com.github.saacsos.FXRouter;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import ku.cs.services.Transitional;

import java.io.IOException;

public class HomeController {
    @FXML private AnchorPane anchorPane;
    @FXML private ImageView logoImageView;
    private Transitional transitional;

    @FXML
    public void initialize(){
        logoImageView.setImage(new Image("file:images/logo/home-logo.png"));
        transitional = new Transitional(anchorPane);
        transitional.fadeIn();
    }


    //    handle link ปุ่ม
    @FXML
    public void handleCreatorsButton(ActionEvent actionEvent){
        try {
            // เปลี่ยนการแสดงผลไปที่ route ที่ชื่อ creator
            FXRouter.goTo("creators");
        } catch (IOException e) {
            System.err.println("ไปที่หน้า creators ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
        }
    }

    @FXML
    public void handleRegisterButton(ActionEvent actionEvent){
        try {
            // เปลี่ยนการแสดงผลไปที่ route ที่ชื่อ register
            FXRouter.goTo("register");
        } catch (IOException e) {
            System.err.println("ไปที่หน้า register ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
        }
    }

    @FXML
    public void handleLoginButton(ActionEvent actionEvent){
        try {
            // เปลี่ยนการแสดงผลไปที่ route ที่ชื่อ login
            FXRouter.goTo("login");
        } catch (IOException e) {
            System.err.println("ไปที่หน้า login ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
        }
    }

    @FXML
    public void handleIntroductionButton(ActionEvent actionEvent){
        try {
            // เปลี่ยนการแสดงผลไปที่ route ที่ชื่อ introduction
            FXRouter.goTo("introduction");
        } catch (IOException e) {
            System.err.println("ไปที่หน้า introduction ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
        }
    }
}
