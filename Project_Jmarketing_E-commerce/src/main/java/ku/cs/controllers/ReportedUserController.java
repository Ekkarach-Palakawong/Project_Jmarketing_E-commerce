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

public class ReportedUserController
{
    //    ItemName
    @FXML
    private Label itemLabel1;
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
    @FXML private Label shopnameLabel;
    @FXML private Label userWarningLabel;
    @FXML private TextField userIDSearchTextField;
    @FXML private Button backButton;
    @FXML private Button nextButton;
    @FXML private Label shopLabel;
    @FXML private Label trytoLoginlabel;
    @FXML private Label timesLabel;
    @FXML private Label continueLoginLabel;
    @FXML private TableView<User> userListTable;
    @FXML private ImageView userImage;
    @FXML private AnchorPane anchorPane;


    private UserList reportList;
    private UserList userList;
    private  User user;
    private User userCheck;
    private DataSource dataSource;
    private Admin admin;
    private ProductsList productsList;
    private ArrayList<Product> productsArrayList;
    private int i;
    private Transitional transitional;

    @FXML
    public void initialize()
    {
        transitional = new Transitional(anchorPane);
        transitional.fadeIn();
        dataSource = new DataSource();
        user = new User("","","","");
        reportList = dataSource.getReportedlist();
        userList = dataSource.getUserList();
        productsList = dataSource.getProducts();
        productsArrayList = new ArrayList<>();
        admin = (Admin) com.github.saacsos.FXRouter.getData();
        reportList.sortList();
        showListView();
        clearSelectedUser();
        handleSelectedListView();

    }

//method ทั่วไป
    @FXML
    private void showListView() {
        userWarningLabel.setVisible(false);
        backButton.setVisible(false);
        nextButton.setVisible(false);

        //        เพิ่มข้อมูลลง table view
        ObservableList<User> userObservableList = FXCollections.observableArrayList(reportList.getAllUser());
        userListTable.setItems(userObservableList);
        TableColumn<User,String> userNameCol = new TableColumn("ชื่อผู้ใช้ระบบ");
        userNameCol.setCellValueFactory(features -> features.getValue().nameProperty());
        TableColumn<User,String> dateCol = new TableColumn("วันที่ลอคอินล่าสุด");
        dateCol.setCellValueFactory(features -> features.getValue().dateProperty());
        TableColumn<User,String> timeCol = new TableColumn("เวลาที่่ลอคอินล่าสุด");
        timeCol.setCellValueFactory(features -> features.getValue().timeProperty());

        userListTable.getColumns().addAll(userNameCol, dateCol, timeCol);
    }


    @FXML
    private void clearSelectedUser()
    {
        userWarningLabel.setVisible(false);
        userNameLabel.setText("");
        totalPayLabel1.setText("");
        totalPayLabel2.setText("");
        shopnameLabel.setText("");
        disableButton.setVisible(false);
        reactivateButton.setVisible(false);
        backButton.setVisible(false);
        nextButton.setVisible(false);
        shopLabel.setVisible(false);
        trytoLoginlabel.setVisible(false);
        timesLabel.setVisible(false);
        continueLoginLabel.setText("");
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
        productsArrayList.clear();
        shopLabel.setVisible(true);
        trytoLoginlabel.setVisible(true);
        timesLabel.setVisible(true);
        String tryToLogin = String.format("%d",user.getTotalContinueLogin());
        continueLoginLabel.setText(tryToLogin);
        userNameLabel.setText(user.getUserName());
        totalPayLabel1.setText("ยอดใช้จ่ายรวม");
        nextButton.setVisible(false);
        backButton.setVisible(false);
        String totalPay = String.format("%.2f",user.getTotalPay());
        totalPayLabel2.setText(totalPay);
        userImage.setImage(new Image("file:"+user.getPathImage()));
        if(user.getStatus()){disableButton.setVisible(true);reactivateButton.setVisible(false);}
        else{disableButton.setVisible(false);reactivateButton.setVisible(true);}
        shopnameLabel.setText(user.getUserShopName());
        productsArrayList = productsList.getUserProduct(user.getUserShopName());
        System.out.println(productsArrayList);
        if(user.isOpenShop()) { setupUserProductLabel(productsArrayList,i);return; }
        clearItemLabel();
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
            setItemLabel(itemLabel3, priceLabel3, products3.getNameProduct(), price);}

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
    private void handleSelectedListView() {
        userListTable.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<User>() {
                    @Override
                    public void changed(ObservableValue<? extends User> observable,
                                        User oldValue, User newValue) {
                        System.out.println(newValue);
                        showSelectedUser(newValue);
                        userCheck = newValue;
                    }
                });
    }

//    handle link ปุ่ม
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
        user = userList.isThisUserReported(userCheck.getUserName());
        admin.disableUser(userCheck);
        admin.disableUser(user);
        disableButton.setVisible(false);
        reactivateButton.setVisible(true);
        dataSource.reWriteData(new UserCsvWriter(),userList);
        dataSource.reWriteData(new ReportedUserCsvWriter(),reportList);
    }

    @FXML
    public void handleReactivateButton(ActionEvent actionevent)
    {
        user = userList.isThisUserReported(userCheck.getUserName());
        admin.reactivateUser(user);
        admin.reactivateUser(userCheck);
        disableButton.setVisible(true);
        reactivateButton.setVisible(false);
        user.setTotalContinueLogin(0);
        userCheck.setTotalContinueLogin(0);
        dataSource.reWriteData(new UserCsvWriter(),userList);
        dataSource.reWriteData(new ReportedUserCsvWriter(),reportList);
        continueLoginLabel.setText("0");

    }

    @FXML
    public void handleSearchButton(ActionEvent actionEvent)
    {
        String s = userIDSearchTextField.getText();
        userCheck = reportList.searchUserName(s);
        if(userCheck != null) { showSelectedUser(userCheck);userWarningLabel.setVisible(false);}
        else{clearSelectedUser();userWarningLabel.setVisible(true);}
    }

    @FXML
    public void handleBackPageButton(ActionEvent actionEvent)
    {
        try {
            com.github.saacsos.FXRouter.goTo("usermanage",admin);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
