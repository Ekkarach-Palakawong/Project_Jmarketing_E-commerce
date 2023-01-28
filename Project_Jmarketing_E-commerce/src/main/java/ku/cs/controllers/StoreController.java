package ku.cs.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import ku.cs.models.*;
import ku.cs.services.DataSource;
import ku.cs.services.Transitional;
import ku.cs.services.Writer.ReportedUserCsvWriter;
import ku.cs.services.Writer.UserCsvWriter;

import java.io.IOException;
import java.util.ArrayList;


public class StoreController {
    private Product products;
    private User user;
    @FXML private Label itemLabel;
    @FXML private Label nameProductLabel;
    @FXML private Label nameShopLabel;
    @FXML private Label productPriceLabel;
    @FXML private Label productOfNumberLabel;
    @FXML private ListView<Product> productListView;
    @FXML private Label warningItemMoreThanNumberOfProduct, warningPriceRangeLessThanZero;
    @FXML private Label warningNotHaveProductInCheckout, warningThisProductHaveProductInUserProduct;
    @FXML private Label warningNotHaveThisProduct;
    @FXML private Label topicNameShopLabel, topicProductPriceLabel, topicNumberOfProductLabel, topicAttributeLabel;
    @FXML private ChoiceBox<String> productTypeChoiceBox;
    @FXML private ChoiceBox<String> attributeChoiceBox;
    @FXML private TextField MinPrice, MaxPrice, searchProduct;
    @FXML private Button refreshSearch, reportButton, minusButton, plusButton, buyButton;
    @FXML private ImageView productImage;
    @FXML private AnchorPane anchorPane;

    private ProductsList productsList;
    private UserList userList;
    private DataSource dataSource;
    private String username;
    private UserList reportedList;
    private User shopHolder;
    private CheckoutList checkoutList;
    private ProductTypeList productTypeList;
    private String productType = "ทั้งหมด";
    private ArrayList<Product> productsArrayList;
    private String scopePriceRangeCheck = "";
    private Transitional transitional;
    private String attribute;
    private String nameShop;
    private String productName;
    private double minPrice;
    private double maxPrice;


    @FXML
    public void initialize() {
        transitional = new Transitional(anchorPane);
        transitional.fadeIn();
        System.out.println("initialize ProductsDetailController");
        dataSource = new DataSource();
        productsList = dataSource.getProducts();
        productsList.sortLatestProduct();
        productsArrayList = productsList.getAllProductWIthDifferentNameInSameShop();
        productTypeList = dataSource.getProductType();

        username = (String) com.github.saacsos.FXRouter.getData();
        userList = dataSource.getUserList();
        reportedList = dataSource.getReportedlist();
        user = userList.searchUserName(username);
        checkoutList = new CheckoutList(username);

        showData();
        showListView();
        handleSelectedListView();
        clearSelectedProduct();
        handleSelectedProductType();
        handleAttributeChoiceBox();
    }

    @FXML
    private void showData() {
        warningItemMoreThanNumberOfProduct.setVisible(false);
        warningNotHaveProductInCheckout.setVisible(false);
        warningNotHaveThisProduct.setVisible(false);
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
        reportButton.setVisible(false);
        buyButton.setVisible(false);
        attributeChoiceBox.setVisible(false);
        productTypeChoiceBox.getItems().add("ทั้งหมด");
        productTypeChoiceBox.setValue("ทั้งหมด");
        productTypeChoiceBox.getItems().addAll(productTypeList.getAllKey());
    }

    @FXML
    private void showListView() {
        productListView.getItems().clear();
        productListView.getItems().addAll(productsArrayList);
        productListView.refresh();
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
        {   attributeChoiceBox.getItems().add(products.getAttribute());
            attributeChoiceBox.setValue(products.getAttribute());
        }

    }

    @FXML
    private void showSelectedProduct(Product products)
    {
        topicNameShopLabel.setVisible(true);
        topicProductPriceLabel.setVisible(true);
        topicNumberOfProductLabel.setVisible(true);
        topicAttributeLabel.setVisible(true);
        itemLabel.setVisible(true);
        minusButton.setVisible(true);
        plusButton.setVisible(true);
        buyButton.setVisible(true);
        itemLabel.setText("0");
        nameProductLabel.setText(products.getNameProduct());
        nameShopLabel.setText(products.getNameShop());
        String productPrice = String.format("%.2f",products.getProductPrice());
        productPriceLabel.setText(productPrice);
        String productOfNumber = String.format("%d",products.getRemainProduct());
        productOfNumberLabel.setText(productOfNumber);
        productImage.setImage(new Image("file:" + products.getPathImage()));
        nameShop = products.getNameShop();
        productName = products.getNameProduct();
        setAttributeChoiceBox(products);
    }


    @FXML
    private void clearSelectedProduct()
    {
        nameProductLabel.setText("");
        nameShopLabel.setText("");
        productPriceLabel.setText("");
        productOfNumberLabel.setText("");
        searchProduct.setText("");
    }

    private void setupProduct()
    {
        if (scopePriceRangeCheck.equals("check") ) {
            minPrice = Double.parseDouble(MinPrice.getText());
            maxPrice = Double.parseDouble(MaxPrice.getText());
            productsArrayList = productsList.scopeProductInStorePriceRange(productType, minPrice, maxPrice);
        }
        else if(!productType.equals("ทั้งหมด") && !scopePriceRangeCheck.equals("check")) { productsArrayList = productsList.getProductArrayListByType(productType); }
        else{productsArrayList = productsList.getAllProductWIthDifferentNameInSameShop();}
    }

    //  method handle
    @FXML
    private void handleAttributeChoiceBox()
    {
        attributeChoiceBox.getSelectionModel().selectedItemProperty().addListener(
                (observableValue, s, t1) -> {
                    if(t1 != null)
                    {
                        warningNotHaveThisProduct.setVisible(false);
                        attribute = t1;
                        if(t1.equals("คุณลักษณะ"))
                        { productPriceLabel.setText("โปรดเลือกคุณลักษณะ");
                            productOfNumberLabel.setText("โปรดเลือกคุณลักษณะ");}
                        else
                        {
                            attributeChoiceBox.getItems().remove("คุณลักษณะ");
                            products = productsList.getAProductByAttribute(t1,productName,nameShop);
                            productPriceLabel.setText(String.format("%.2f",products.getProductPrice()));
                            productOfNumberLabel.setText(String.format("%d",products.getRemainProduct()));
                            productImage.setImage(new Image("file:" + products.getPathImage()));
                            if (products.getRemainProduct() == 0 ) {
                                warningNotHaveThisProduct.setVisible(true);
                            }
                        }
                    } }
        );
    }


    @FXML
    private void handleSelectedProductType()
    {
        productTypeChoiceBox.getSelectionModel().selectedItemProperty().addListener(
                (observableValue, s, t1) -> {
                    if(t1 != null)
                    {
                        productType = t1;
                        productListView.getItems().clear();
                        if(t1.equals("ทั้งหมด")&& !scopePriceRangeCheck.equals("check"))
                        {   productsArrayList = productsList.getAllProductWIthDifferentNameInSameShop();
                            attributeChoiceBox.setVisible(false); }
                        else if(productsList.getProductArrayListByType(t1) != null && !scopePriceRangeCheck.equals("check"))
                        { productsArrayList = productsList.getProductArrayListByType(productType); }
                        else
                        {
                            Double LowPrice = Double.parseDouble(MinPrice.getText());
                            Double HighPrice = Double.parseDouble(MaxPrice.getText());
                            productsArrayList = productsList.scopeProductInStorePriceRange(productType,LowPrice,HighPrice);
                        }
                        showListView();
                    }}
        );
    }

    @FXML
    private void handleSelectedListView() {
        productListView.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<Product>() {
                    @Override
                    public void changed(ObservableValue<? extends Product> observable,
                                        Product oldValue, Product newValue) {
                        System.out.println(newValue + " is selected");
                        warningThisProductHaveProductInUserProduct.setVisible(false);
                        warningNotHaveThisProduct.setVisible(false);
                        products = newValue;        // ให้ products เท่ากับค่าที่ชี้ใน Listview
                        if (newValue != null){
                            showSelectedProduct(newValue);
                            if (newValue.getRemainProduct() > products.getTotalItem()) {
                                warningItemMoreThanNumberOfProduct.setVisible(false);
                            }
                            if (user.getUserShopName().equals(products.getNameShop())) {
                                reportButton.setVisible(false);
                            } else if (!user.getUserShopName().equals(products.getNameShop())){
                                reportButton.setVisible(true);
                            }
                        }
                        if (checkoutList.checkSize() > 0) {
                            warningNotHaveProductInCheckout.setVisible(false);
                        }
                    }
                });
    }


    //    method handle link ปุ่ม
    @FXML
    public void handleReportButton(ActionEvent actionEvent)
    {
        shopHolder = userList.searchUserShopName(products.getNameShop());
        if(reportedList.searchUserName(shopHolder.getUserName()) == null) { reportedList.addUser(shopHolder); }
        shopHolder.report();
        dataSource.reWriteData(new ReportedUserCsvWriter(),reportedList);
        dataSource.reWriteData(new UserCsvWriter(),userList);
    }

    @FXML
    public void handleBuyButton(ActionEvent actionEvent){      //ให้มันกดเอาค่านั้นมาใส่ checkoutlist
        int numberItem = Integer.parseInt(itemLabel.getText());

        if (numberItem > 0 && numberItem <= products.getRemainProduct()){
            if (products.getNameShop().equals(user.getUserShopName())){
                warningThisProductHaveProductInUserProduct.setVisible(true);
                warningItemMoreThanNumberOfProduct.setVisible(false);
                System.err.println("----สินค้านี้เป็นสินค้าของคุณ----");
            }
            else{
                checkoutList.addProductToCheckout(products, numberItem);
                checkoutList.purchaseTotalPrice();
                System.out.println("products: " + products.toCSV());
            }
            return;
        }
        warningThisProductHaveProductInUserProduct.setVisible(false);
        warningItemMoreThanNumberOfProduct.setVisible(true);
        System.err.println("Cannot Add Product");
    }

    @FXML public void handleSortExpensivePriceToLowButton(ActionEvent actionEvent) {
        productsList.sortExpensivePrice();
        setupProduct();
        productListView.getItems().clear();
        showListView();
    }

    @FXML public void handleSortLowPriceToExpensiveButton(ActionEvent actionEvent) {
        productsList.sortCheapPrice();
        setupProduct();
        productListView.getItems().clear();
        showListView();
    }

    @FXML public void handleSortLastedProductButton(ActionEvent actionEvent) {
        productsList.sortLatestProduct();
        setupProduct();
        productListView.getItems().clear();
        showListView();
    }

    @FXML public void handleScopePriceRange(ActionEvent actionEvent) {
        Double LowPrice = Double.parseDouble(MinPrice.getText());
        Double HighPrice = Double.parseDouble(MaxPrice.getText());
        if ((LowPrice < 0 && HighPrice < 0) || (LowPrice == 0 && HighPrice == 0) || (LowPrice < 0 && HighPrice > 0) || (LowPrice > 0 && HighPrice < 0) || (LowPrice > HighPrice)) {
            warningPriceRangeLessThanZero.setVisible(true);
        }
        else
        {
            productsArrayList = productsList.scopeProductInStorePriceRange(productType,LowPrice,HighPrice);
            warningPriceRangeLessThanZero.setVisible(false);
            scopePriceRangeCheck = "check";
        }
        productListView.getItems().clear();
        showListView();
        refreshSearch.setVisible(true);
    }

    @FXML public void handleRefresh(ActionEvent actionEvent) {
        scopePriceRangeCheck = "";
        MinPrice.setText("");
        MaxPrice.setText("");
        warningPriceRangeLessThanZero.setVisible(false);
        if(productType.equals("ทั้งหมด"))
        { productsArrayList = productsList.getAllProductWIthDifferentNameInSameShop(); }
        else
        { productsArrayList = productsList.getProductArrayListByType(productType);}
        productListView.getItems().clear();
        showListView();
        refreshSearch.setVisible(false);

    }

    @FXML public void handleAddItemButton(ActionEvent actionEvent) {
        products.addItem();
        String item = String.format("%d", products.getSaleItem());
        itemLabel.setText(item);
    }

    @FXML public void handleDeleteItemButton(ActionEvent actionEvent) {
        products.deleteItem();
        String item = String.format("%d", products.getSaleItem());
        itemLabel.setText(item);
    }

    @FXML public void handleSearchProductButton(ActionEvent actionEvent) {
        productListView.getItems().clear();
        String search = searchProduct.getText();
        productsArrayList = productsList.getSearchProduct(search);
        if(productsArrayList.size() > 0) {
            showListView();
        }

    }


    @FXML public void handleClearSearchProductButton(ActionEvent actionEvent) {
        setupProduct();
        showListView();
    }

    @FXML
    public void handleGoToCheckoutPageButton(ActionEvent actionEvent) {
        try {
            System.out.println(checkoutList.toString());
            if (checkoutList.checkSize() > 0 && !attribute.equals("คุณลักษณะ")) {
                com.github.saacsos.FXRouter.goTo("checkout", checkoutList);
            }
            warningNotHaveProductInCheckout.setVisible(true);
        } catch (IOException e) {
            e.printStackTrace();
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

    //    handle link mouse click
    @FXML public void handleMouseClick(MouseEvent event) {
        System.out.println(products.getNameShop() + "," + username);
        try {
            com.github.saacsos.FXRouter.goTo("userstore", products.getNameShop() + "," + user.getUserShopName());
        } catch (IOException e) {
            System.err.println(e);
            System.err.println("ไปที่หน้า userstore ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
        }
    }

}