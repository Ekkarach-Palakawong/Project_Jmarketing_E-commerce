package ku.cs.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import ku.cs.models.*;
import ku.cs.services.DataSource;
import ku.cs.services.Transitional;
import ku.cs.services.Writer.ReportedUserCsvWriter;
import ku.cs.services.Writer.UserCsvWriter;

import java.io.IOException;
import java.util.ArrayList;

public class UserStoreController {

    @FXML private Label itemLabel;
    @FXML private Label nameProductLabel;
    @FXML private Label nameShopLabel;
    @FXML private Label productPriceLabel;
    @FXML private Label productOfNumberLabel;
    @FXML private ListView<Product> productListView;
    @FXML private Label warningItemMoreThanNumberOfProduct, warningPriceRangeLessThanZero;
    @FXML private Label warningNotHaveProductInCheckout, warningThisProductHaveProductInUserProduct;
    @FXML private Label warningMotHaveThisProduct;
    @FXML private Label thisNameShopLabel;
    @FXML private Label topicNameShopLabel, topicProductPriceLabel, topicNumberOfProductLabel, topicAttributeLabel;
    @FXML private ChoiceBox<String> productTypeChoiceBox;
    @FXML private ChoiceBox<String> attributeChoiceBox;
    @FXML private TextField MinPrice, MaxPrice;
    @FXML private Button refreshSearch, reportButton, minusButton, plusButton, buyButton;
    @FXML private ImageView productImage;
    @FXML private AnchorPane anchorPane;

    private User user;
    private UserList userList;
    private Product product;
    private ProductsList productsList;
    private DataSource dataSource;

    private UserList reportedList;
    private User shopHolder;
    private CheckoutList checkoutList;
    private ProductTypeList productTypeList;
    private String productType = "ทั้งหมด";
    private ArrayList<Product> productsArrayList;
    private String check = "";
    private String nameShop ;
    private String data, userName;
    private Transitional transitional;
    private String attribute;
    private String productName;
    private double minPrice;
    private double maxPrice;


    @FXML
    public void initialize() {
        transitional = new Transitional(anchorPane);
        transitional.fadeIn();
        data = (String) com.github.saacsos.FXRouter.getData();
        String[] name = data.split(",");
        nameShop = name[0];
        userName = name[1];
        dataSource = new DataSource();
        productsList = dataSource.getProducts();
        productTypeList = dataSource.getProductType();

        productsArrayList = productsList.getUserProductWithDifferenceName(nameShop);

        userList = dataSource.getUserList();
        reportedList = dataSource.getReportedlist();
        user = userList.searchUserShopName(userName);
        checkoutList = new CheckoutList(user.getUserName());

        thisNameShopLabel.setText(nameShop);

        showData();
        showListView();
        handleSelectedListView();
        clearSelectedProduct();
        handleSelectedProductType();
        handleAttributeChoiceBox();
    }

//    method ทั่วไป

    private void showData() {
        thisNameShopLabel.setText(nameShop);
        warningItemMoreThanNumberOfProduct.setVisible(false);
        warningNotHaveProductInCheckout.setVisible(false);
        warningMotHaveThisProduct.setVisible(false);
        warningPriceRangeLessThanZero.setVisible(false);
        refreshSearch.setVisible(false);
        warningThisProductHaveProductInUserProduct.setVisible(false);
        topicNameShopLabel.setVisible(false);
        topicProductPriceLabel.setVisible(false);
        topicNumberOfProductLabel.setVisible(false);
        topicAttributeLabel.setVisible(false);
        itemLabel.setVisible(false);
        minusButton.setVisible(false);
        plusButton.setVisible(false);
        buyButton.setVisible(false);
        reportButton.setVisible(false);
        attributeChoiceBox.setVisible(false);
        productTypeChoiceBox.getItems().add("ทั้งหมด");
        productTypeChoiceBox.setValue("ทั้งหมด");
        productTypeChoiceBox.getItems().addAll(productTypeList.getAllKey());
    }

    private void showSelectedProduct(Product products) {
        buyButton.setVisible(true);
        topicNameShopLabel.setVisible(true);
        topicProductPriceLabel.setVisible(true);
        topicNumberOfProductLabel.setVisible(true);
        topicAttributeLabel.setVisible(true);
        itemLabel.setVisible(true);
        minusButton.setVisible(true);
        plusButton.setVisible(true);
        itemLabel.setText("0");
        nameProductLabel.setText(products.getNameProduct());
        nameShopLabel.setText(products.getNameShop());
        String productPrice = String.format("%.2f", products.getProductPrice());
        productPriceLabel.setText(productPrice);
        String productOfNumber = String.format("%d", products.getRemainProduct());
        productOfNumberLabel.setText(productOfNumber);
        productImage.setImage(new Image("file:" + products.getPathImage()));
        nameShop = products.getNameShop();
        productName = products.getNameProduct();
        setAttributeChoiceBox(products);
    }


    @FXML
    private void clearSelectedProduct() {
        nameProductLabel.setText("");
        nameShopLabel.setText("");
        productPriceLabel.setText("");
        productOfNumberLabel.setText("");
    }

    @FXML
    private void showListView() {
        productListView.getItems().addAll(productsArrayList);
        productListView.refresh();
    }


    private void setupSortProduct()
    {
        if (check.equals("check") ) {
            minPrice = Double.parseDouble(MinPrice.getText());
            maxPrice = Double.parseDouble(MaxPrice.getText());
            productsArrayList = productsList.scopePriceRangeForUserStore(productType,nameShop, minPrice, maxPrice);
        }
        else if(!productType.equals("All") && !check.equals("check")) {  productsArrayList = productsList.getUserProductByProductType(nameShop, productType); }
        else{productsArrayList = productsList.getUserProductWithDifferenceName(nameShop);}
    }
    @FXML
    private void setAttributeChoiceBox(Product products)
    {
        attributeChoiceBox.setVisible(true);
        attributeChoiceBox.getItems().clear();
        if(productsList.countProductWithSameName(productsList.getUserProduct(products.getNameShop()),products.getNameProduct()) >= 2)
        {
            ArrayList<String> attributeArrayList = productsList.getProductAttributeSameShopSameName(products.getNameProduct(),products.getNameShop());
            attributeChoiceBox.getItems().addAll(attributeArrayList);
            attributeChoiceBox.getItems().add("คุณลักษณะ");
            attributeChoiceBox.setValue("คุณลักษณะ");
            productOfNumberLabel.setText("โปรดเลือกคุณลักษณะ");
        }
        else
        {
            attributeChoiceBox.getItems().add(products.getAttribute());
            attributeChoiceBox.setValue(products.getAttribute());

        }

    }

    //    method handle
    @FXML
    private void handleSelectedProductType()
    {
        productTypeChoiceBox.getSelectionModel().selectedItemProperty().addListener(
                (observableValue, s, t1) -> {
                    if (t1.equals("ทั้งหมด") && !check.equals("check")) {
                        productsArrayList = productsList.getUserProductWithDifferenceName(nameShop);
                        attributeChoiceBox.setVisible(false);
                    } else if (productsList.getUserProductByProductType(nameShop,t1) != null && !check.equals("check")) {
                        productsArrayList = productsList.getUserProductByProductType(nameShop,t1);
                    } else {
                        attributeChoiceBox.getItems().remove("คุณลักษณะ");
                        Double LowPrice = Double.parseDouble(MinPrice.getText());
                        Double HighPrice = Double.parseDouble(MaxPrice.getText());
                        productsArrayList = productsList.scopePriceRangeForUserStore(t1,nameShop,LowPrice,HighPrice);
                    }
                    productType = t1;
                    productListView.getItems().clear();
                    showListView();
                }
        );
    }

    @FXML
    private void handleAttributeChoiceBox()
    {
        attributeChoiceBox.getSelectionModel().selectedItemProperty().addListener(
                (observableValue, s, t1) -> {
                    if(t1 != null)
                    {
                        attribute = String.valueOf(t1);
                        if(String.valueOf(t1).equals("คุณลักษณะ"))
                        { productPriceLabel.setText("โปรดเลือกคุณลักษณะ");
                            productOfNumberLabel.setText("โปรดเลือกคุณลักษณะ");}
                        else
                        {
                            product = productsList.getAProductByAttribute(String.valueOf(t1),productName,nameShop);
                            productPriceLabel.setText(String.format("%.2f",product.getProductPrice()));
                            productOfNumberLabel.setText(String.format("%d",product.getRemainProduct()));
                            productImage.setImage(new Image("file:" + product.getPathImage()));

                        }
                    }
                }
        );
    }


    @FXML
    private void handleSelectedListView() {
        productListView.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<Product>() {
                    @Override
                    public void changed(ObservableValue<? extends Product> observable,
                                        Product oldValue, Product newValue) {
                        warningThisProductHaveProductInUserProduct.setVisible(false);
                        // ให้ products เท่ากับค่าที่ชี้ใน Listview
                        if (newValue != null){
                            product = newValue;
                            showSelectedProduct(newValue);
                            if (newValue.getRemainProduct() == 0 ) {
                                warningMotHaveThisProduct.setVisible(true);
                            }
                            if (newValue.getRemainProduct() > product.getTotalItem()) {
                                warningItemMoreThanNumberOfProduct.setVisible(false);
                            }
                            if (user.getUserShopName().equals(nameShop)) {
                                reportButton.setVisible(false);
                            } else if (!user.getUserShopName().equals(nameShop)) {
                                reportButton.setVisible(true);
                            }
                            if (checkoutList.checkSize() > 0) {
                                warningNotHaveProductInCheckout.setVisible(false);
                            }
                        }
                    }
                });
    }

    //    method handle link กับปุ่ม
    @FXML
    public void handleBuyButton(ActionEvent actionEvent){      //ให้มันกดเอาค่านั้นมาใส่ checkoutlist
        int numberItem = Integer.parseInt(itemLabel.getText());

        if (numberItem > 0 && numberItem <= product.getRemainProduct()){
            if (product.getNameShop().equals(userName)) {
                System.out.println("---" + product.getNameShop());
                System.out.println(userName);
                warningThisProductHaveProductInUserProduct.setVisible(true);
                warningItemMoreThanNumberOfProduct.setVisible(false);
                System.err.println("----สินค้านี้เป็นสินค้าของคุณ----");
            }
            else {
                System.out.println("---" + product.getNameShop());
                System.out.println(userName);
                checkoutList.addProductToCheckout(product, numberItem);
                checkoutList.purchaseTotalPrice();
            }

            return;
        }
        warningThisProductHaveProductInUserProduct.setVisible(false);
        warningItemMoreThanNumberOfProduct.setVisible(true);
        System.err.println("Cannot Add Product");
    }

    @FXML public void handleSortExpensivePriceToLowButton(ActionEvent actionEvent) {
        productsList.sortExpensivePrice();
        setupSortProduct();
        productListView.getItems().clear();
        showListView();
    }

    @FXML public void handleSortLowPriceToExpensiveButton(ActionEvent actionEvent) {
        productsList.sortCheapPrice();
        setupSortProduct();
        productListView.getItems().clear();
        showListView();
    }

    @FXML public void handleSortLastedProductButton(ActionEvent actionEvent) {
        productsList.sortLatestProduct();
        setupSortProduct();
        productListView.getItems().clear();
        showListView();
    }

    @FXML public void handlePriceRangeButton(ActionEvent actionEvent) {
        Double LowPrice = Double.parseDouble(MinPrice.getText());
        Double HighPrice = Double.parseDouble(MaxPrice.getText());
        if ((LowPrice < 0 && HighPrice < 0) || (LowPrice == 0 && HighPrice == 0) || (LowPrice < 0 && HighPrice > 0) || (LowPrice > 0 && HighPrice < 0) || (LowPrice > HighPrice)) {
            warningPriceRangeLessThanZero.setVisible(true);
        }
        else {
            productsArrayList = productsList.scopePriceRangeForUserStore(productType,nameShop,LowPrice,HighPrice);
            warningPriceRangeLessThanZero.setVisible(false);
            check = "check";
        }
        productListView.getItems().clear();
        showListView();
        refreshSearch.setVisible(true);
    }

    @FXML public void handleRefresh(ActionEvent actionEvent) {
        check = "";
        MinPrice.setText("");
        MaxPrice.setText("");
        warningPriceRangeLessThanZero.setVisible(false);
        productsArrayList = productsList.getUserProductWithDifferenceName(nameShop);
        productListView.getItems().clear();
        showListView();
        refreshSearch.setVisible(false);

    }

    @FXML public void handleAddItemButton(ActionEvent actionEvent) {
        product.addItem();
        String item = String.format("%d", product.getSaleItem());
        itemLabel.setText(item);
    }

    @FXML public void handleDeleteItemButton(ActionEvent actionEvent) {
        product.deleteItem();
        String item = String.format("%d", product.getSaleItem());
        itemLabel.setText(item);
    }


    @FXML
    public void handleReportButton(ActionEvent actionEvent)
    {
        shopHolder = userList.searchUserShopName(product.getNameShop());
        if(reportedList.searchUserName(shopHolder.getUserName()) == null) { reportedList.addUser(shopHolder); }
        shopHolder.report();
        dataSource.reWriteData(new ReportedUserCsvWriter(),reportedList);
        dataSource.reWriteData(new UserCsvWriter(),userList);
    }
    @FXML
    public void handleGoToCheckoutPageButton(ActionEvent actionEvent) {
        try {
            if (checkoutList.checkSize() > 0 && !attribute.equals("คุณลักษณะ")) {
                com.github.saacsos.FXRouter.goTo("checkout", checkoutList);
            }
            warningNotHaveProductInCheckout.setVisible(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleBackButton(ActionEvent actionEvent) {
        try {
            com.github.saacsos.FXRouter.goTo("store");
        } catch (IOException e) {
            System.err.println("ไปที่หน้า store ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
        }
    }

    @FXML
    public void handleProfileButton(ActionEvent actionEvent){
        try {
            com.github.saacsos.FXRouter.goTo("profile", user);
        } catch (IOException e) {
            System.err.println("ไปที่หน้า profile ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
        }
    }
}
