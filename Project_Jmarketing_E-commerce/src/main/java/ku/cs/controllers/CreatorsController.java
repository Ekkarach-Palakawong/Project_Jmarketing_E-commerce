package ku.cs.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import com.github.saacsos.FXRouter;
import javafx.scene.layout.AnchorPane;
import ku.cs.services.Transitional;

import java.io.IOException;
import java.util.concurrent.TransferQueue;

public class CreatorsController {

    @FXML private ImageView peachImage,fifaImage, manImage,pongImage;
    @FXML private AnchorPane anchorPane;
    private Transitional transitional;

    @FXML
    public void initialize() {
        transitional = new Transitional(anchorPane);
        transitional.fadeIn();
        fifaImage.setImage(new Image("file:images/adminimage/fifa.jpg"));
        pongImage.setImage(new Image("file:images/adminimage/pong.jpg"));
        manImage.setImage(new Image("file:images/adminimage/man.jpg"));
        peachImage.setImage(new Image("file:images/adminimage/peach.jpg"));
    }
    //    handle link ปุ่ม
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
