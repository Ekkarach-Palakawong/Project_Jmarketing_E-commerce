package ku.cs.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import com.github.saacsos.FXRouter;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import ku.cs.services.MapSetOfIntroduction;
import ku.cs.services.Transitional;

import java.io.IOException;

public class IntroductionController {
    @FXML private AnchorPane anchorPane;
    @FXML private ChoiceBox<String> pageChoiceBox;
    @FXML private ImageView pageImageView;
    private Transitional transitional;
    private MapSetOfIntroduction mapSet;

    @FXML
    public void initialize(){

        String path = "file:images/introduction/";
        mapSet = new MapSetOfIntroduction();
        transitional = new Transitional(anchorPane);
        transitional.fadeIn();

        mapSet.addPage("หน้าหลัก",path+"home.png");
        mapSet.addPage("การลงทะเบียน",path+"register.png");
        mapSet.addPage("การเข้าสู่ระบบ",path+"login.png");
        mapSet.addPage("การสินค้าทั้งหมด",path+"market.png");
        mapSet.addPage("การตรวจดูสินค้า",path+"checkout.png");
        mapSet.addPage("การชำระเงิน",path+"payment.png");
        mapSet.addPage("หน้าโปรไฟล์",path+"profile.png");
        mapSet.addPage("การเปลี่ยนรหัสผ่าน",path+"resetpassword.png");
        mapSet.addPage("การเปิดร้านค้า",path+"openshop.png");
        mapSet.addPage("การเพิ่มสินค้า",path+"addproduct.png");
        mapSet.addPage("การแก้ไขสินค้า",path+"editproduct.png");

        showData();
        handleSelectedChoiceBoxAndShowImage();
    }
    @FXML
    private void showData() {
        pageChoiceBox.getItems().addAll(mapSet.getAllKey());
        pageChoiceBox.setValue("หน้าหลัก");
        pageImageView.setImage(new Image(mapSet.getImagePath("หน้าหลัก")));
    }
    @FXML private void handleSelectedChoiceBoxAndShowImage(){
        pageChoiceBox.getSelectionModel().selectedItemProperty().addListener(
                (observableValue, old, current) -> {
                    if (current!=null){
                        pageImageView.setImage(new Image(mapSet.getImagePath(current)));
                    }
                }
        );
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
