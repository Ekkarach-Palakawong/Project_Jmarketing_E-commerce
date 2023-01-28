package ku.cs.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import ku.cs.models.*;
import ku.cs.services.DataSource;
import ku.cs.services.Transitional;


public class AddProductController {
    private ProductsList productsList;
    private DataSource dataSource;
    private Transitional transitional;

    @FXML private TextField userProductName;
    @FXML private TextField userProductPrice;
    @FXML private TextField userNumberOfProduct;
    @FXML private TextField userWarningNumberOfProduct;
    @FXML private Label warningNameProductLabel;
    @FXML private Label warningProductPriceLabel;
    @FXML private Label warningNumberOfProductLabel;
    @FXML private Label warningLowProductLabel;
    @FXML private Label warningUserHaveProduct;
    @FXML private ImageView productImage;

    @FXML private ChoiceBox typeChoiceBox;
    @FXML private  ChoiceBox attributeChoiceBox;
    @FXML private AnchorPane warningNameLabel;

    private String username;
    private UserList userList;
    private  User user;
    private String productType = "Choose Product type";
    private ProductTypeList productTypeList;
    private String attribute = "Not assigned";
    private String path = "images/productimage/default-Product.jpg";

    @FXML
    public void initialize() {
        transitional = new Transitional(warningNameLabel);
        transitional.fadeIn();
        setWarningDataAndShow();
        username = (String)com.github.saacsos.FXRouter.getData();;
        dataSource = new DataSource();
        userList = dataSource.getUserList();
        user = userList.searchUserName(username);
        productsList = dataSource.getProducts();
        productTypeList = dataSource.getProductType();

        typeChoiceBox.getItems().add("Choose Product type");
        typeChoiceBox.setValue("Choose Product type");
        typeChoiceBox.getItems().addAll(productTypeList.getAllKey());
        attributeChoiceBox.setDisable(true);

        handleSelectedProductType();
        handleAttributeChoiceBox();
    }

    //    method ทั่วๆไป
    @FXML
    private void setupAttributeBox(String productType)
    {
        attributeChoiceBox.getItems().clear();
        attributeChoiceBox.getItems().add("Choose attribute");
        attributeChoiceBox.setValue("Choose attribute");
        attributeChoiceBox.getItems().addAll(productTypeList.getAttributeSpaceFilter(productType));
        attributeChoiceBox.getItems().add("Not assigned");

    }


    @FXML
    private void setWarningDataAndShow() {
        warningNameProductLabel.setVisible(false);
        warningProductPriceLabel.setVisible(false);
        warningNumberOfProductLabel.setVisible(false);
        warningLowProductLabel.setVisible(false);
        warningUserHaveProduct.setVisible(false);
    }

    //    method handle
    @FXML
    private void handleSelectedProductType()
    {
        typeChoiceBox.getSelectionModel().selectedItemProperty().addListener(
                (observableValue, s, t1) -> {
                    if(t1 != null)
                    {
                        productType = String.valueOf(t1);
                        attributeChoiceBox.getItems().clear();
                        if(t1.equals("Choose Product type"))
                        { attributeChoiceBox.setDisable(true); }
                        else
                        { attributeChoiceBox.setDisable(false); setupAttributeBox(productType);}

                    }}
        );
    }

    @FXML
    private void handleAttributeChoiceBox()
    {
        attributeChoiceBox.getSelectionModel().selectedItemProperty().addListener((observableValue, o, t1) ->
        {attribute = String.valueOf(t1); });
    }

//    handle link ปุ่ม

    @FXML
    public  void handleBrowseButton(ActionEvent event){
        FileChooser chooser = new FileChooser();
        // SET FILECHOOSER INITIAL DIRECTORY
        chooser.setInitialDirectory(new File(System.getProperty("user.home")));
        // DEFINE ACCEPTABLE FILE EXTENSION
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("images PNG JPG JPEG", "*.png", "*.jpg", "*.jpeg"));
        // GET FILE FROM FILECHOOSER WITH JAVAFX COMPONENT WINDOW
        Node source = (Node) event.getSource();
        File file = chooser.showOpenDialog(source.getScene().getWindow());
        if (file != null){
            try {
                // CREATE FOLDER IF NOT EXIST
                File destDir = new File("images/productimage");
                if (!destDir.exists()) destDir.mkdirs();
                // RENAME FILE
                String[] fileSplit = file.getName().split("\\.");
                String filename = productsList.getNewID() + userProductName.getText() + user.getUserShopName() + "."
                        + fileSplit[fileSplit.length - 1];
                Path target = FileSystems.getDefault().getPath(
                        destDir.getAbsolutePath()+System.getProperty("file.separator")+filename
                );
                // COPY WITH FLAG REPLACE FILE IF FILE IS EXIST
                Files.copy(file.toPath(), target, StandardCopyOption.REPLACE_EXISTING );
                productImage.setImage(new Image(target.toUri().toString()));
                path = destDir + "/" + filename;

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public  void handleSubmitButton(ActionEvent actionEvent){
        setWarningDataAndShow();
        try {
            if (userProductName.getText().isEmpty()) {
                warningNameProductLabel.setVisible(true);
            } if (userProductPrice.getText().isEmpty()) {
                warningProductPriceLabel.setVisible(true);
            } if (userNumberOfProduct.getText().isEmpty()) {
                warningNumberOfProductLabel.setVisible(true);
            } if (userWarningNumberOfProduct.getText().isEmpty()) {
                warningLowProductLabel.setVisible(true);
            }if (productsList.isProductAlreadySell(userProductName.getText(), user.getUserShopName(),attribute)) {
                warningUserHaveProduct.setVisible(true);
            } else if(!userProductName.getText().isEmpty() && !userProductPrice.getText().isEmpty() && !userNumberOfProduct.getText().isEmpty() && !userWarningNumberOfProduct.getText().isEmpty()) {
                Product data = new Product(productsList.getNewID(),productType, user.getUserShopName(), userProductName.getText(),Double.parseDouble(userProductPrice.getText()),Integer.parseInt(userNumberOfProduct.getText()), Integer.parseInt(userWarningNumberOfProduct.getText()));
                if(path.equals("images/productimage/default-Product.jpg") && productsList.isSameProductNameSameShop(productsList.getUserProduct(data.getNameShop()),data.getNameProduct(),data.getNameShop()))
                {
                    data.setPathImage(productsList.getProductPathSameNameSameShop(data.getNameShop(),data.getNameProduct()));
                }
                else {
                    data.setPathImage(path);
                }
                data.setAttribute(attribute);
                data.addProductDataInFile();
                com.github.saacsos.FXRouter.goTo("profile");
            }
        } catch (IOException e) {
            System.err.println("ไปที่หน้า profile ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public  void handleBackButton(ActionEvent actionEvent){
        try {
            // เปลี่ยนการแสดงผลไปที่ route ที่ชื่อ profile
            com.github.saacsos.FXRouter.goTo("profile");
        } catch (IOException e) {
            System.err.println("ไปที่หน้า profile ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
        }
    }
}
