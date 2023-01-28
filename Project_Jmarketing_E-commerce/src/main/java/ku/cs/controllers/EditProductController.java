package ku.cs.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import ku.cs.models.ProductTypeList;
import ku.cs.models.Product;
import ku.cs.models.ProductsList;
import ku.cs.services.DataSource;
import ku.cs.services.Transitional;
import ku.cs.services.Writer.ProductCsvWriter;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class EditProductController {

    @FXML private Label nameProductLabel;
    @FXML private Label productPriceLabel;
    @FXML private Label warningProductLabel;
    @FXML private Label numberOfProductLabel;
    @FXML private TextField changeNameProduct;
    @FXML private TextField changeProductPrice;
    @FXML private TextField changeLowProduct;
    @FXML private TextField increaseNumberOfProduct;
    @FXML private ImageView productImage;
    @FXML private Label currentProductTypeLabel;
    @FXML private Label currentAttributeLabel;
    @FXML private ChoiceBox productTypeChoiceBox;
    @FXML private ChoiceBox attributeChoiceBox;
    @FXML private AnchorPane anchorPane;
    private Product product;
    private ProductsList productsList;
    private ProductTypeList productTypeList;
    private DataSource dataSource;
    private String path;
    private String productType;
    private String attribute;
    private Transitional transitional;

    public void initialize() {
        transitional = new Transitional(anchorPane);
        transitional.fadeIn();
        product = (Product) com.github.saacsos.FXRouter.getData();
        dataSource = new DataSource();

        productsList = dataSource.getProducts();
        productTypeList = dataSource.getProductType();
        product = productsList.getAProductByAttribute(product.getAttribute(),product.getNameProduct(),product.getNameShop());
        path = product.getPathImage();
        productType = product.getProductType();
        attribute = product.getAttribute();
        showData();
        setupAttributeChoiceBox(productType);
        handleSelectedProductType();
        handleSelectedAttribute();
    }

//    method ทั่วไป

    private void showData() {
        nameProductLabel.setText(product.getNameProduct());
        String productPrice = String.format("%.2f", product.getProductPrice());
        productPriceLabel.setText(productPrice);
        String warningProduct = String.format("%d", product.getWarningProduct());
        warningProductLabel.setText(warningProduct);
        String numberOfProduct = String.format("%d", product.getRemainProduct());
        numberOfProductLabel.setText(numberOfProduct);
        productImage.setImage(new Image("file:" + product.getPathImage()));
        currentAttributeLabel.setText(product.getAttribute());
        currentProductTypeLabel.setText(product.getProductType());
        productTypeChoiceBox.getItems().add("ประเภทสินค้า");
        productTypeChoiceBox.getItems().addAll(productTypeList.getAllKey());
        productTypeChoiceBox.setValue("ประเภทสินค้า");

    }

//    method handle

    private void handleSelectedProductType()
    {
        productTypeChoiceBox.getSelectionModel().selectedItemProperty().addListener((observableValue, o, t1) ->
        { productType = String.valueOf(t1); });
    }

    private  void handleSelectedAttribute()
    {
        attributeChoiceBox.getSelectionModel().selectedItemProperty().addListener((observableValue, o, t1) ->
        { attribute = String.valueOf(t1); });
    }

    private void setupAttributeChoiceBox(String productType)
    {
        attributeChoiceBox.getItems().clear();
        attributeChoiceBox.getItems().add("คุณลักษณะ");
        attributeChoiceBox.getItems().addAll(productTypeList.getAttributeSpaceFilter(productType));
        attributeChoiceBox.getItems().add("ไม่มีคุณลักษณะ");
        attributeChoiceBox.setValue("คุณลักษณะ");

    }

    @FXML
    private void handleEditNameProduct() {
        if (!changeNameProduct.getText().isEmpty()) {
            product.setNameProduct(changeNameProduct.getText());
        }
        dataSource.reWriteData(new ProductCsvWriter(),productsList);
        nameProductLabel.setText(changeNameProduct.getText());
    }

    @FXML
    private void handleEditProductPrice() {
        if (!changeProductPrice.getText().isEmpty()) {
            product.setProductPrice(Double.parseDouble(changeProductPrice.getText()));
        }
        dataSource.reWriteData(new ProductCsvWriter(),productsList);
        String productPrice = String.format("%.2f", product.getProductPrice());
        productPriceLabel.setText(productPrice);
    }

    @FXML
    private void handleEditWarningProductButton() {
        if (!changeLowProduct.getText().isEmpty()) {
            product.setWarningProduct(Integer.parseInt(changeLowProduct.getText()));
        }
        dataSource.reWriteData(new ProductCsvWriter(),productsList);
        warningProductLabel.setText(changeLowProduct.getText());
    }

    @FXML
    private void handleIncreaseNumberOfProduct() {
        product.IncreaseItem(Integer.parseInt(increaseNumberOfProduct.getText()));
        dataSource.reWriteData(new ProductCsvWriter(),productsList);
        String numberOfProduct = String.format("%d", product.getRemainProduct());
        numberOfProductLabel.setText(numberOfProduct);
    }

//    handle link ปุ่ม

    @FXML
    public void handleBrowseButton(ActionEvent event){
        FileChooser chooser = new FileChooser();
        chooser.setInitialDirectory(new File(System.getProperty("user.home")));
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("images PNG JPG JPEG", "*.png", "*.jpg", "*.jpeg"));
        Node source = (Node) event.getSource();
        File file = chooser.showOpenDialog(source.getScene().getWindow());
        if (file != null){
            try {
                File destDir = new File("images/productimage");
                if (!destDir.exists()) destDir.mkdirs();
                String[] fileSplit = file.getName().split("\\.");
                String filename = product.getId() + product.getNameProduct() + product.getNameShop() + "."
                        + fileSplit[fileSplit.length - 1];
                Path target = FileSystems.getDefault().getPath(
                        destDir.getAbsolutePath()+System.getProperty("file.separator")+filename
                );
                Files.copy(file.toPath(), target, StandardCopyOption.REPLACE_EXISTING );
                productImage.setImage(new Image(target.toUri().toString()));
                path = destDir + "/" + filename;
                product.setPathImage(path);
                dataSource.reWriteData(new ProductCsvWriter(),productsList);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void handleChangeProductTypeBtn(ActionEvent actionEvent)
    {
        product.setProductType(productType);
        product.setDefaultAttribute();
        dataSource.reWriteData(new ProductCsvWriter(),productsList);
        currentProductTypeLabel.setText(productType);
        currentAttributeLabel.setText(product.getAttribute());
        setupAttributeChoiceBox(productType);
    }

    @FXML
    public void handleChangeAttribute(ActionEvent actionEvent)
    {
        product.setAttribute(attribute);
        dataSource.reWriteData(new ProductCsvWriter(),productsList);
        currentAttributeLabel.setText(attribute);
    }

    @FXML
    public void handleBackButton(ActionEvent actionEvent) {
        try {
            com.github.saacsos.FXRouter.goTo("profile");
        } catch (IOException e) {
            System.err.println("ไปที่หน้า profile ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
        }
    }

}
