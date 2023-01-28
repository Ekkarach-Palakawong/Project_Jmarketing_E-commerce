package ku.cs.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

// concept = "//"

public class UserManageController
{
    //    ItemName
    @FXML private Label itemLabel1;
    @FXML private Label itemLabel2;
    @FXML private Label itemLabel3;
    @FXML private Label itemLabel4;

    //    ItemPrice
    @FXML private Label priceLabel1;
    @FXML private Label priceLabel2;
    @FXML private Label priceLabel3;
    @FXML private Label priceLabel4;

    //Other
    @FXML private Label userNameLabel;
    @FXML private Button disableButton;
    @FXML private Button reactivateButton;
    @FXML private Label totalPayLabel1;
    @FXML private Label totalPayLabel2;
    @FXML private Label shopLabel;
    @FXML private Label userWarningLabel;
    @FXML private  TextField userIDSearchTextField;
    @FXML private Button backButton;
    @FXML private Button nextButton;
    @FXML private  Label shopName1Label;
    @FXML private ImageView userImage;
    @FXML private TableView<User> userListTable;
    @FXML private AnchorPane shopNameLabel;

    private UserList userList;
    private User userCheck;
    private DataSource dataSource;
    private Admin admin;
    private  UserList reportList;
    private  ProductsList productsList;
    private User user;
    private  ArrayList<Product> productsArrayList;
    private ObservableList<User> userObservableList;
    private int i;
    private Transitional transitional;


    @FXML
    public void initialize() {
        transitional = new Transitional(shopNameLabel);
        transitional.fadeIn();
        dataSource= new DataSource();
        userList = dataSource.getUserList();
        productsList = dataSource.getProducts();
        reportList = dataSource.getReportedlist();
        productsArrayList = new ArrayList<>();

        admin = (Admin) com.github.saacsos.FXRouter.getData();
        userList.sortList();
        showListView();
        clearSelectedUser();
        handleSelectedUser();
    }
    //method ทั่วๆไป
    @FXML
    private void showListView() {
        userWarningLabel.setVisible(false);
        backButton.setVisible(false);
        nextButton.setVisible(false);
        //        เพิ่มข้อมูลลง table view
        userObservableList = FXCollections.observableArrayList(userList.getAllUser());
        userListTable.setItems(userObservableList);
        TableColumn<User,String> userNameCol = new TableColumn("userName");
        userNameCol.setCellValueFactory(features -> features.getValue().nameProperty());
        userNameCol.setMinWidth(100);
        TableColumn<User,String> dateCol = new TableColumn("last Date");
        dateCol.setCellValueFactory(features -> features.getValue().dateProperty());
        dateCol.setMinWidth(135);
        TableColumn<User,String> timeCol = new TableColumn("last Time");
        timeCol.setCellValueFactory(features -> features.getValue().timeProperty());
        dateCol.setMinWidth(135);
        userListTable.getColumns().addAll(userNameCol, dateCol, timeCol);

    }


    @FXML
    private void clearSelectedUser()
    {
        userWarningLabel.setVisible(false);
        userNameLabel.setText("");
        totalPayLabel1.setText("");
        totalPayLabel2.setText("");
        shopLabel.setText("");
        shopName1Label.setVisible(false);
        disableButton.setVisible(false);
        reactivateButton.setVisible(false);
        backButton.setVisible(false);
        nextButton.setVisible(false);
        clearItemLabel();
    }
    @FXML
    private  void clearItemLabel()
    {
        itemLabel1.setText("");
        itemLabel2.setText("");
        itemLabel3.setText("");
        itemLabel4.setText("");
        priceLabel1.setText("");
        priceLabel2.setText("");
        priceLabel3.setText("");
        priceLabel4.setText("");
    }

    @FXML
    private void showSelectedUser(User user)
    {
        clearItemLabel();
        i = 0;
        String totalPay = String.format("%.2f",user.getTotalPay());
        productsArrayList.clear();
        userImage.setImage(new Image("file:"+user.getPathImage()));
        userNameLabel.setText(user.getUserName());
        totalPayLabel1.setText("totalPay :");
        totalPayLabel2.setText(totalPay);
        shopLabel.setText(user.getUserShopName());
        shopName1Label.setVisible(true);
        nextButton.setVisible(false);
        backButton.setVisible(false);
        if(user.getStatus()){disableButton.setVisible(true);reactivateButton.setVisible(false);}
        else{disableButton.setVisible(false);reactivateButton.setVisible(true);}

        productsArrayList = productsList.getUserProduct(user.getUserShopName());

        if(user.isOpenShop()) { setupUserProductLabel(productsArrayList,i);return; }
    }



    @FXML
    private void setItemLabel(Label itemLabel,Label priceLabel,String productName,String price)
    {
        itemLabel.setText(productName);
        priceLabel.setText(price);
    }

    @FXML
    private void setupUserProductLabel(ArrayList<Product> productsArrayList, int i)
    {
        clearItemLabel();
        Product products1,products2,products3,products4;
        String price;
        if(productsArrayList.size() -i > 0) {
            products1 = productsArrayList.get(i);
            price = String.format("%.2f", products1.getProductPrice());
            setItemLabel(itemLabel1, priceLabel1, products1.getNameProduct(), price);
        }
        if(productsArrayList.size() -i > 1) {
            products2 = productsArrayList.get(i + 1);
            price = String.format("%.2f", products2.getProductPrice());
            setItemLabel(itemLabel2, priceLabel2, products2.getNameProduct(), price);
        }

        if(productsArrayList.size() -i > 2) {
            products3 = productsArrayList.get(i + 2);
            price = String.format("%.2f", products3.getProductPrice());
            setItemLabel(itemLabel3, priceLabel3, products3.getNameProduct(), price);
        }

        if(productsArrayList.size() -i > 3){products4 = productsArrayList.get(i+3);
            price = String.format("%.2f", products4.getProductPrice());
            setItemLabel(itemLabel4, priceLabel4, products4.getNameProduct(), price);}


        if(productsArrayList.size() -i >4){nextButton.setVisible(true);}
        else {nextButton.setVisible(false);}
        if(i != 0){backButton.setVisible(true);}
        else {backButton.setVisible(false);}

    }
    //    method handle
    @FXML
    private void handleSelectedUser() {
        userListTable.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<User>() {
                    @Override
                    public void changed(ObservableValue<? extends User> observable,
                                        User oldValue, User newValue) {
                        System.out.println(newValue );
                        userCheck = newValue;
                        showSelectedUser(newValue);
                    }
                });
    }

    //    method handle link ปุ่ม
    @FXML
    public void handleNextButton(ActionEvent actionEvent)
    {
        i += 4;
        setupUserProductLabel(productsArrayList,i);
    }

    @FXML
    public void handleBackButton(ActionEvent actionEvent)
    {
        i-= 4;
        if (i >= 0) {
            setupUserProductLabel(productsArrayList,i);
        }
        else{System.err.println("Error");}
    }
    @FXML
    public void handleDisableButton(ActionEvent actionEvent) {
        user = reportList.isThisUserReported(userCheck.getUserName());
        admin.disableUser(userCheck);
        if(user != null){admin.disableUser(user);}
        disableButton.setVisible(false);
        reactivateButton.setVisible(true);
        dataSource.reWriteData(new UserCsvWriter(),userList);
        dataSource.reWriteData(new ReportedUserCsvWriter(),reportList);
    }

    @FXML
    public void handleReactivateButton(ActionEvent actionevent)
    {
        user = reportList.isThisUserReported(userCheck.getUserName());
        if(user != null){admin.disableUser(user);}
        admin.reactivateUser(userCheck);
        disableButton.setVisible(true);
        reactivateButton.setVisible(false);
        dataSource.reWriteData(new UserCsvWriter(),userList);
        dataSource.reWriteData(new ReportedUserCsvWriter(),reportList);
    }

    @FXML
    public  void handleAdminButton(ActionEvent actionEvent)
    {
        try {
            com.github.saacsos.FXRouter.goTo("admin");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleSearchButton(ActionEvent actionEvent)
    {
        String s = userIDSearchTextField.getText();
        userCheck = userList.searchUserName(s);
        if(userCheck != null) { showSelectedUser(userCheck);userWarningLabel.setVisible(false);}
        else{clearSelectedUser();userWarningLabel.setVisible(true);}
    }

    @FXML
    public void handleReportedUser(ActionEvent actionEvent)
    {
        try {
            com.github.saacsos.FXRouter.goTo("reported",admin);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}