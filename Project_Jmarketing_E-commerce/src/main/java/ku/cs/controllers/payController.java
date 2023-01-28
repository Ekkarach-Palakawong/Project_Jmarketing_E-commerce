package ku.cs.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import ku.cs.models.CheckoutList;
import ku.cs.models.ProductsList;
import ku.cs.models.User;
import ku.cs.models.UserList;
import ku.cs.services.DataSource;
import ku.cs.services.Transitional;
import ku.cs.services.Writer.ProductCsvWriter;
import ku.cs.services.Writer.ReportedUserCsvWriter;
import ku.cs.services.Writer.UserCsvWriter;

import java.io.IOException;

public class payController {
    private CheckoutList checkoutList;
    private ProductsList productsList;
    private DataSource dataSource;
    private User user;
    private UserList userList;
    private Transitional transitional;
    private UserList reportedList;

    @FXML private Label TotalPrice;
    @FXML private AnchorPane anchorPane;


    @FXML
    public void initialize() {
        transitional = new Transitional(anchorPane);
        transitional.fadeIn();
        System.out.println("initialize PayDetailController");
        checkoutList = (CheckoutList) com.github.saacsos.FXRouter.getData();
        dataSource = new DataSource();
        productsList = dataSource.getProducts();
        userList = dataSource.getUserList();
        user = userList.searchUserName(checkoutList.getUsername());
        reportedList = dataSource.getReportedlist();

        showTotalPayData();

    }

//    method ทั่วไป

    @FXML
    private void showTotalPayData() {
        String price = String.format("%.2f",checkoutList.purchaseTotalPrice());
        TotalPrice.setText(price);
    }

//    method handle link ปุ่ม

    @FXML
    public void handleBackToCheckoutPageButton(ActionEvent actionEvent) {
        try {
            com.github.saacsos.FXRouter.goTo("checkout");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleToPayPageButton(ActionEvent actionEvent) {
        try {
            System.out.println("---------");
            ProductsList list = checkoutList.setValueOfProduct(productsList);
            dataSource.reWriteData(new ProductCsvWriter(),list);
            user.buyProduct(checkoutList.purchaseTotalPrice());
            dataSource.reWriteData(new UserCsvWriter(),userList);
            User reported = reportedList.isThisUserReported(user.getUserName());
            if(reported != null){reported.buyProduct(checkoutList.purchaseTotalPrice());dataSource.reWriteData(new ReportedUserCsvWriter(),reportedList);}

            com.github.saacsos.FXRouter.goTo("store");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
