package ku.cs.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import com.github.saacsos.FXRouter;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import ku.cs.models.User;
import ku.cs.services.Transitional;

import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;

public class RegisterController {
    @FXML private TextField userNameTxt,nameSignature,createPassword,createPasswordAgain;
    @FXML private Label nullError,userNameError,passError;
    @FXML private AnchorPane anchorPane;

    private String path;
    private Transitional transitional;

    @FXML
    public void initialize() {
        transitional = new Transitional(anchorPane);
        transitional.fadeIn();
        setDataAndShow();
    }

//  method ทั่วไป

    @FXML
    private void setDataAndShow() {
        nullError.setText("");
        userNameError.setText("");
        passError.setText("");
    }

    //    method handle link ปุ่ม
    @FXML
    public void handleBrowseButton(ActionEvent event){
        FileChooser chooser = new FileChooser();
        chooser.setInitialDirectory(new File(System.getProperty("user.home")));
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("images PNG JPG", "*.png", "*.jpg", "*.jpeg"));
        Node source = (Node) event.getSource();
        File file = chooser.showOpenDialog(source.getScene().getWindow());
        if (file != null){
            try {
                File destDir = new File("images/userimage");
                if (!destDir.exists()) destDir.mkdirs();
                String[] fileSplit = file.getName().split("\\.");
                String filename = userNameTxt.getText() + "."
                        + fileSplit[fileSplit.length - 1];
                Path target = FileSystems.getDefault().getPath(
                        destDir.getAbsolutePath()+System.getProperty("file.separator")+filename
                );
                Files.copy(file.toPath(), target, StandardCopyOption.REPLACE_EXISTING );
                path = destDir + "/" + filename;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @FXML
    public void handleCreateButton(ActionEvent actionEvent){
        try {
            User data = new User(nameSignature.getText(),userNameTxt.getText(),createPassword.getText());
            if (data.checkUserName()){
                setDataAndShow();
                userNameError.setText("ชื่อนี้ใช้ไปแล้ว");
                System.err.println("this user is already used");

            } else if (!createPassword.getText().equals(createPasswordAgain.getText())){
                setDataAndShow();
                passError.setText("รหัสผ่านไม่เหมือนกัน");
                System.err.println("password didn't match");

            } else if(data.checkEmpty()|| createPasswordAgain.getText().equals("")) {
                setDataAndShow();
                nullError.setText("โปรดใส่ข้อมูลให้ครบถ้วน");
                System.err.println("data must not be empty.");

            } else {
                data.registerDataInFile(path);
                FXRouter.goTo("login");
            }
        } catch (IOException e) {
            System.err.println("ไปที่หน้า login ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleBackButton(ActionEvent actionEvent){
        try {

            FXRouter.goTo("home");
        } catch (IOException e) {
            System.err.println("ไปที่หน้า home ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
        }
    }
}