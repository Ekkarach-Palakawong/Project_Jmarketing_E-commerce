package ku.cs.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import ku.cs.models.*;
import ku.cs.services.DataSource;
import ku.cs.services.Transitional;
import ku.cs.services.Writer.ProductTypeCsvWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class ProductTypeController {
    @FXML
    private Label productTypeLabel;
    @FXML
    private Label item1Label;
    @FXML
    private Label item2Label;
    @FXML
    private Label item3Label;
    @FXML
    private Label item4Label;

    @FXML
    private Label price1Label;
    @FXML
    private Label price2Label;
    @FXML
    private Label price3Label;
    @FXML
    private Label price4Label;

    @FXML
    private Label shop1Label;
    @FXML
    private Label shop2Label;
    @FXML
    private Label shop3Label;
    @FXML
    private Label shop4Label;

    @FXML
    private Button backBtn;
    @FXML
    private Button nextBtn;
    @FXML
    private Button addNewAttributeBtn;

    @FXML
    private TextField addProductTypeTextField;
    @FXML
    private TextField addNewAttributeTextField;
    @FXML
    private TableView<Map.Entry<String, ArrayList<String>>> productTypeTableView;
    @FXML
    private ChoiceBox removeAttributeChoiceBox;
    @FXML
    private Button removeBtn;
    @FXML
    private AnchorPane anchorPane;

    private ProductsList productsList;
    private ProductTypeList productTypeList;
    private DataSource dataSource;
    private ArrayList<Product> productsArrayList;
    private Admin admin;
    private String productType = " ";
    private String attribute = " ";
    private ObservableList<Map.Entry<String, ArrayList<String>>> productTypeObservableValue;
    private int i = 0;
    private Transitional transitional;

    @FXML
    public void initialize() {
        transitional = new Transitional(anchorPane);
        transitional.fadeIn();
        admin = (Admin) com.github.saacsos.FXRouter.getData();
        dataSource = new DataSource();
        productsList = dataSource.getProducts();
        productTypeList = dataSource.getProductType();
        productsArrayList = new ArrayList<>();
        showData();
        handleSelectedTableView();
        handleRemoveAttributeChoiceBox();

    }

//    method ทั่วๆไป

    @FXML
    private void showData() {
        clearItemLabel();
        nextBtn.setVisible(false);
        backBtn.setVisible(false);
        removeBtn.setVisible(false);
        removeAttributeChoiceBox.setVisible(false);
        addNewAttributeBtn.setVisible(false);
        addNewAttributeBtn.setText("เพิ่ม");
        addNewAttributeTextField.setVisible(false);
        productTypeLabel.setText("");


        productTypeObservableValue = FXCollections.observableArrayList(productTypeList.getAllProductType().entrySet());
        productTypeTableView.setItems(productTypeObservableValue);
        TableColumn<Map.Entry<String, ArrayList<String>>, String> productTypeCol = new TableColumn<>("ประเภทสินค้า");
        productTypeCol.setCellValueFactory(mapStringCellDataFeatures -> new SimpleStringProperty(mapStringCellDataFeatures.getValue().getKey()));
        productTypeTableView.getColumns().add(productTypeCol);
        for (int i = 0; i < 4; i++) {
            TableColumn<Map.Entry<String, ArrayList<String>>, String> attributeCol = new TableColumn<>("คุณลักษณะ : " + (i + 1));
            int num = i;
            attributeCol.setCellValueFactory(entryStringCellDataFeatures -> new SimpleStringProperty(entryStringCellDataFeatures.getValue().getValue().get(num)));
            productTypeTableView.getColumns().add(attributeCol);
        }

    }

    @FXML
    private  void clearItemLabel() {
        item1Label.setText("");
        item2Label.setText("");
        item3Label.setText("");
        item4Label.setText("");
        price1Label.setText("");
        price2Label.setText("");
        price3Label.setText("");
        price4Label.setText("");
        shop1Label.setText("");
        shop2Label.setText("");
        shop3Label.setText("");
        shop4Label.setText("");
    }

    @FXML
    private void showSelectedType(Map.Entry<String, ArrayList<String>> type) {
        i = 0;
        setupRemoveAttributeChoiceBox(type.getKey());
        productTypeLabel.setText(type.getKey());
        addNewAttributeTextField.setVisible(true);
        addNewAttributeBtn.setVisible(true);
        productsArrayList = productsList.getProductListByType(type.getKey());
        setProductLabel(productsArrayList, i);
        ArrayList<String> typeArray = productTypeList.getProductTypeAttribute(type.getKey());
        if (!typeArray.get(3).equals(" ")) {
            addNewAttributeTextField.setDisable(true);
            addNewAttributeBtn.setDisable(true);
            addNewAttributeTextField.setText("คุณลักษณะครบแล้ว");
        } else {
            addNewAttributeTextField.setDisable(false);
            addNewAttributeBtn.setDisable(false);
            addNewAttributeTextField.clear();
        }
    }

    @FXML
    private void setupRemoveAttributeChoiceBox(String productType) {
        removeBtn.setVisible(true);
        removeBtn.setDisable(false);
        removeAttributeChoiceBox.setVisible(true);
        removeAttributeChoiceBox.getItems().clear();
        removeAttributeChoiceBox.setDisable(false);
        if (productTypeList.isHasAttribute(productType)) {
            removeAttributeChoiceBox.getItems().add("คุณลักษณะ");
            removeAttributeChoiceBox.setValue("คุณลักษณะ");
            removeAttributeChoiceBox.getItems().addAll(productTypeList.getAttributeSpaceFilter(productType));
        } else {
            removeAttributeChoiceBox.getItems().add("สินค้าประเภทนี้ไม่มีคุณลักษณะ");
            removeAttributeChoiceBox.setValue("สินค้าประเภทนี้ไม่มีคุณลักษณะ");
            removeAttributeChoiceBox.setDisable(true);
            removeBtn.setDisable(true);
        }
    }

    @FXML
    private void setItemLabel(Label itemlabel,Label priceLabel,String productName,String price)
    {
        itemlabel.setText(productName);
        priceLabel.setText(price);
    }

    //method handle
    @FXML
    private void handleSelectedTableView() {
        productTypeTableView.getSelectionModel().selectedItemProperty().addListener((observableValue, stringArrayListEntry, t1) -> {
            if (t1 != null) {
                showSelectedType(t1);
                productType = t1.getKey();
            }
        });
    }


    @FXML
    private void handleRemoveAttributeChoiceBox() {
        removeAttributeChoiceBox.getSelectionModel().selectedItemProperty().addListener((observableValue, o, t1) ->
        {
            attribute = String.valueOf(t1);
        });
    }


    @FXML
    private void setProductLabel(ArrayList<Product> productsArrayList, int i)
    {
        Product products1,products2,products3,products4;
        clearItemLabel();
        String price;
        if(productsArrayList.size() -i > 0){
            products1 = productsArrayList.get(i);
            price = String.format("%.2f",products1.getProductPrice()) + " บาท";
            setItemLabel(item1Label,price1Label,products1.getNameProduct(),price);
            shop1Label.setText(products1.getNameShop()); }

        if(productsArrayList.size() -i > 1) {
            products2 = productsArrayList.get(i+1);
            price = String.format("%.2f",products2.getProductPrice()) + " บาท";
            setItemLabel(item2Label,price2Label,products2.getNameProduct(),price);
            shop2Label.setText(products2.getNameShop());
        }


        if(productsArrayList.size() -i > 2) {
            products3 = productsArrayList.get(i + 2);
            price = String.format("%.2f",products3.getProductPrice()) + " บาท";
            setItemLabel(item3Label,price3Label,products3.getNameProduct(),price);
            shop3Label.setText(products3.getNameShop());
        }


        if(productsArrayList.size() -i > 3){
            products4 = productsArrayList.get(i+3);
            price = String.format("%.2f",products4.getProductPrice()) + " บาท";
            setItemLabel(item4Label,price4Label,products4.getNameProduct(),price);
            shop4Label.setText(products4.getNameShop());
        }

        if(productsArrayList.size() -i >4){nextBtn.setVisible(true);}
        else {nextBtn.setVisible(false);}
        if(i != 0){backBtn.setVisible(true);}
        else {backBtn.setVisible(false);}

    }

// handle link ปุ่ม

    @FXML
    public void handleAddProductTypeButton(ActionEvent actionEvent)
    {
        String type = addProductTypeTextField.getText();
        if(!type.isBlank())
        {
            admin.addProductType(productTypeList,type);
            dataSource.reWriteData(new ProductTypeCsvWriter(), productTypeList);
            productTypeTableView.getColumns().clear();
            showData();
        }
    }

    @FXML
    public void handleRemoveAttributeBtn(ActionEvent actionEvent) {
        admin.removeAttribute(productTypeList, productType, attribute);
        productsList.removeAttribute(attribute);

        dataSource.reWriteData(new ProductTypeCsvWriter(), productTypeList);
        setupRemoveAttributeChoiceBox(productType);
        productTypeTableView.getColumns().clear();
        showData();
    }

    @FXML
    public void handleNextButton(ActionEvent actionEvent)
    {
        i += 4;
        setProductLabel(productsArrayList,i);
    }

    @FXML
    public void handleBackButton(ActionEvent actionEvent)
    {
        i-= 4;
        if (i >= 0) {
            setProductLabel(productsArrayList,i);
        }
        else{System.err.println("Error");}
    }

    @FXML
    public void handleAddNewAttributeButton(ActionEvent actionEvent)
    {
        if(!addNewAttributeTextField.getText().isBlank())
        {
            admin.addNewAttribute(productTypeList, productType,addNewAttributeTextField.getText());
            dataSource.reWriteData(new ProductTypeCsvWriter(), productTypeList);
            addNewAttributeTextField.clear();
            productTypeTableView.getColumns().clear();
            showData();
        }
    }

    @FXML
    public void handleAdminButton(ActionEvent actionEvent)
    {
        try {
            com.github.saacsos.FXRouter.goTo("admin");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

