package ku.cs.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import ku.cs.models.CheckoutList;
import ku.cs.models.Product;
import ku.cs.models.ProductsList;
import ku.cs.services.Transitional;

import java.io.IOException;


public class CheckoutController {
    private CheckoutList checkoutList;
    private Transitional transitional;



    @FXML private Label name1, nameSet, price1, priceSet, numItem1, numItemSet, totalPrice1;
    @FXML private ListView<Product> listCheckout;
    @FXML private AnchorPane anchorPane;



    @FXML
    public void initialize() {
        transitional = new Transitional(anchorPane);
        transitional.fadeIn();
        System.out.println("initialize CheckOutDetailController");
        checkoutList = (CheckoutList) com.github.saacsos.FXRouter.getData();
        handleSelectedListView();
        showListView();
        clearSelectedProduct();
        showCheckoutData();
    }

    // method ทั่วไป
    @FXML
    private void showListView() {
        listCheckout.getItems().addAll(checkoutList.getAllProduct());
        listCheckout.refresh();
    }

    @FXML
    private void showSelectedProduct(Product products)
    {
        name1.setText(products.getNameProduct());
        String item = String.format("%d",products.getTotalItem());
        numItem1.setText(item);
        String price = String.format("%.2f",products.getProductPrice());
        price1.setText(price);
        nameSet.setText("ชื่อสินค้า :");
        priceSet.setText("ราคา/ชื้น :");
        numItemSet.setText("จำนวนสินค้า :");


    }
    @FXML
    private void clearSelectedProduct()
    {
        name1.setText("");
        price1.setText("");
        numItem1.setText("");
        nameSet.setText("");
        priceSet.setText("");
        numItemSet.setText("");
    }



    @FXML
    private void showCheckoutData() {
        String totalPrice = String.format("%.2f",checkoutList.purchaseTotalPrice());
        totalPrice1.setText(totalPrice);
    }

//    method handle

    @FXML
    private void handleSelectedListView() {
        listCheckout.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<Product>() {
                    @Override
                    public void changed(ObservableValue<? extends Product> observable,
                                        Product oldValue, Product newValue) {
                        System.out.println(newValue + " is selected");
                        showSelectedProduct(newValue);

                    }
                });
    }


    //    handle link ปุ่ม
    @FXML
    public  void handleBackToShopButton(ActionEvent actionEvent) {
        try {
            com.github.saacsos.FXRouter.goTo("store");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void handleGoToPayPageButton(ActionEvent actionEvent) {
        try {
            com.github.saacsos.FXRouter.goTo("pay",checkoutList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
