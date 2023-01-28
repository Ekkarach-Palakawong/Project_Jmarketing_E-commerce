package ku.cs.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import ku.cs.models.Product;
import ku.cs.models.ProductsList;
import ku.cs.models.User;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

import com.github.saacsos.FXRouter;
import ku.cs.models.UserList;
import ku.cs.services.DataSource;
import ku.cs.services.Transitional;
import ku.cs.services.Writer.UserCsvWriter;

public class ProfileController {
    private User user;
    private Product products;
    @FXML private Label profileName, profileUserName, profileStatus, userType;
    @FXML private Label nameProductLabel;
    @FXML private Label nameShopLabel;
    @FXML private Label productPriceLabel;
    @FXML private Label productOfNumberLabel;
    @FXML private Label topicNameShopLabel, topicProductPriceLabel, topicNumberOfProductLabel, topicAttributeLabel;
    @FXML private ListView<Product> productListView;
    @FXML private Button openShopButton;
    @FXML private Label warningLabel;
    @FXML private ImageView userImage;
    @FXML private ImageView productImage;
    @FXML private Button editProductButton;
    @FXML private AnchorPane profileImage;
    @FXML private ChoiceBox attributeChoiceBox;


    private ProductsList productsList;
    private DataSource dataSource;
    private UserList userList;
    private String productName;
    private String nameShop;
    private Transitional transitional;
    private String attribute;
    private ArrayList<Product> productArrayList;

    @FXML
    public void initialize() {
        transitional = new Transitional(profileImage);
        transitional.fadeIn();
        user = (User)com.github.saacsos.FXRouter.getData() ;
        user = new User("", "");
        dataSource = new DataSource();

        user = user.readLoginReport();
        productsList = dataSource.getProducts();
        userList = dataSource.getUserList();
        user = userList.searchUserName(user.getUserName());
        productArrayList = productsList.getUserProductWithDifferenceName(user.getUserShopName());

        showData();
        showListView();
        handleSelectedListView();
        clearSelectedProduct();
        handleSelectedChoiceBox();

    }
//method ทั่วไป

    @FXML
    private void showListView() {
        productListView.getItems().addAll(productArrayList);
        productListView.refresh();
    }
    @FXML
    private void clearSelectedProduct() {
        nameProductLabel.setText("");
        nameShopLabel.setText("");
        productPriceLabel.setText("");
        productOfNumberLabel.setText("");
    }
    @FXML
    private void showData() {
        profileName.setText(user.getName());
        profileUserName.setText(user.getUserName());
        userType.setText(user.getType());
        topicNameShopLabel.setVisible(false);
        topicProductPriceLabel.setVisible(false);
        topicNumberOfProductLabel.setVisible(false);
        topicAttributeLabel.setVisible(false);
        attributeChoiceBox.setVisible(false);
        warningLabel.setVisible(false);
        editProductButton.setVisible(false);
        userImage.setImage(new Image("file:" + user.getPathImage()));
        if (!user.getUserShopName().equals("No shop")) {
            openShopButton.setVisible(false);
        }
        if (user.getStatus()) {
            profileStatus.setText("Available");
        } else {
            profileStatus.setText("Disable");
        }
    }


    private void showSelectedProduct(Product product) {
        productName = product.getNameProduct();
        nameShop = product.getNameShop();
        nameProductLabel.setText(product.getNameProduct());
        nameShopLabel.setText(product.getNameShop());
        productImage.setImage(new Image("file:" + product.getPathImage()));
        topicNameShopLabel.setVisible(true);
        topicProductPriceLabel.setVisible(true);
        topicNumberOfProductLabel.setVisible(true);
        topicAttributeLabel.setVisible(true);
        attributeChoiceBox.setVisible(true);
        editProductButton.setVisible(true);
        attributeChoiceBox.getItems().clear();
        if(productsList.countProductWithSameName(productsList.getUserProduct(product.getNameShop()),product.getNameProduct()) >= 2)
        {
            ArrayList<String> attributeArrayList = productsList.getProductAttributeSameShopSameName(product.getNameProduct(),product.getNameShop());
            attributeChoiceBox.getItems().addAll(attributeArrayList);
            attributeChoiceBox.getItems().add("คุณลักษณะ");
            attributeChoiceBox.setValue("คุณลักษณะ");
            productOfNumberLabel.setText("โปรดเลือกคุณลักษณะ");
        }
        else
        {
            attributeChoiceBox.getItems().add(product.getAttribute());
            attributeChoiceBox.setValue(product.getAttribute());
            if (product.getRemainProduct() < products.getWarningProduct()) {
                warningLabel.setVisible(true);
            }
            if (product.getRemainProduct() >= products.getWarningProduct()) {
                warningLabel.setVisible(false);
            }
        }
    }

    private void handleSelectedChoiceBox()
    {
        attributeChoiceBox.getSelectionModel().selectedItemProperty().addListener((observableValue, o, t1) ->
        {
            if(t1 != null)
            {
                attribute = String.valueOf(t1);
                if (String.valueOf(t1).equals("คุณลักษณะ")) {
                    productPriceLabel.setText("โปรดเลือกคุณลักษณะ");
                    productOfNumberLabel.setText("โปรดเลือกคุณลักษณะ");
                } else {
                    attributeChoiceBox.getItems().remove("คุณลักษณะ");
                    products = productsList.getAProductByAttribute(String.valueOf(t1),productName,nameShop);
                    productPriceLabel.setText(String.format("%.2f",products.getProductPrice()));
                    productOfNumberLabel.setText(String.format("%d",products.getRemainProduct()));
                    productImage.setImage(new Image("file:" + products.getPathImage()));
                    if (products.getRemainProduct() < products.getWarningProduct()) {
                        warningLabel.setVisible(true);
                    }
                    if (products.getRemainProduct() >= products.getWarningProduct()) {
                        warningLabel.setVisible(false);
                    }
                }

            }
        });
    }

    @FXML
    private void handleSelectedListView() {
        productListView.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<Product>() {
                    @Override
                    public void changed(ObservableValue<? extends Product> observable,
                                        Product oldValue, Product newValue) {
                        showSelectedProduct(newValue);
                        products = newValue;        // ให้ products เท่ากับค่าที่ชี้ใน Listview
                    }
                });
    }


    //    method handle link ปุ่ม
    @FXML
    public void handleBackButton(ActionEvent actionEvent) {
        try {
            FXRouter.goTo("store");
        } catch (IOException e) {
            System.err.println("ไปที่หน้า store ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
        }
    }

    @FXML
    public void handleLogoutButton(ActionEvent actionEvent) {
        try {
            FXRouter.goTo("login");
        } catch (IOException e) {
            System.err.println("ไปที่หน้า login ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
        }
    }

    @FXML
    public void handleSellerButton(ActionEvent actionEvent) {
        try {
            FXRouter.goTo("addproduct", user.getUserName());
        } catch (IOException e) {
            System.err.println(e);
            System.err.println("ไปที่หน้า addproduct ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
        }
    }

    @FXML
    public void handleEditProductButton(ActionEvent actionEvent) {
        if(products!= null && !attribute.equals("คุณลักษณะ"))
        {
            try {
                FXRouter.goTo("editproduct", products);
            } catch (IOException e) {
                System.err.println("ไปที่หน้า editproduct ไม่ได้");
                System.err.println("ให้ตรวจสอบการกำหนด route");
                System.err.println(e);
            }
        }
    }

    @FXML
    public void handleOpenShopButton(ActionEvent actionEvent) {
        try {
            FXRouter.goTo("openshop", user.getUserName());
        } catch (IOException e) {
            System.err.println("ไปที่หน้า openshop ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
        }
    }
    @FXML
    public void handleResetPasswordButton(ActionEvent actionEvent){
        try {
            FXRouter.goTo("resetpassword", user.getUserName());
        } catch (IOException e) {
            System.err.println("ไปที่หน้า resetpassword ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
        }
    }
    @FXML
    public void handleBrowseButton(ActionEvent actionEvent){
        FileChooser chooser = new FileChooser();
        chooser.setInitialDirectory(new File(System.getProperty("user.home")));
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("images PNG JPG", "*.png", "*.jpg", "*.jpeg"));
        Node source = (Node) actionEvent.getSource();
        File file = chooser.showOpenDialog(source.getScene().getWindow());

        if (file != null){
            try {
                File destDir = new File("images/userimage");
                if (!destDir.exists()) destDir.mkdirs();
                String[] fileSplit = file.getName().split("\\.");
                String filename = user.getUserName() + "."
                        + fileSplit[fileSplit.length - 1];
                Path target = FileSystems.getDefault().getPath(
                        destDir.getAbsolutePath()+System.getProperty("file.separator")+filename
                );
                Files.copy(file.toPath(), target, StandardCopyOption.REPLACE_EXISTING );
                user = userList.searchUserName(user.getUserName());
                user.setImagePath(destDir+"/"+filename);
                userImage.setImage(new Image(target.toUri().toString()));
                dataSource.reWriteData(new UserCsvWriter(), userList);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}