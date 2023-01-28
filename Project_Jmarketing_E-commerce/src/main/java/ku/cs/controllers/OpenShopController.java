package ku.cs.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import ku.cs.models.User;
import ku.cs.models.UserList;
import ku.cs.services.DataSource;
import ku.cs.services.Transitional;
import ku.cs.services.Writer.UserCsvWriter;

import java.io.IOException;

public class OpenShopController {
    private User user;
    private String username;
    private UserList userList;
    private DataSource dataSource;
    private Transitional transitional;

    @FXML private TextField userShopName;
    @FXML private Label warningDuplicateNameShop;
    @FXML private Label warningEnterNameShop;
    @FXML private AnchorPane anchorPane;

    @FXML
    public void initialize() {
        transitional = new Transitional(anchorPane);
        transitional.fadeIn();
        user = new User("","","","",false,"",0,"");
        user = user.readLoginReport();
        username = (String) com.github.saacsos.FXRouter.getData();
        dataSource = new DataSource();
        userList = dataSource.getUserList();
        user = userList.searchUserName(username);
        warningDuplicateNameShop.setVisible(false);
        warningEnterNameShop.setVisible(false);

    }

//method handle link ปุ่ม

    @FXML
    public void handleSummitOpenShop(ActionEvent actionEvent) {
        warningDuplicateNameShop.setVisible(false);
        warningEnterNameShop.setVisible(false);
        try {
            if (userShopName.getText().isEmpty()) {
                warningEnterNameShop.setVisible(true);
            } else if (userList.searchUserOpenShopName(userShopName.getText())) {
                warningDuplicateNameShop.setVisible(true);
            } else {
                user.setUserShopName(userShopName.getText());
                dataSource.reWriteData(new UserCsvWriter(),userList);
                com.github.saacsos.FXRouter.goTo("profile");
            }
        } catch (IOException e) {
            System.err.println("ไปที่หน้า profile ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
        }
    }

    @FXML
    public void handleCancelOpenShop(ActionEvent actionEvent) {
        try {
            com.github.saacsos.FXRouter.goTo("profile");
        } catch (IOException e) {
            System.err.println("ไปที่หน้า profile ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
        }
    }

}