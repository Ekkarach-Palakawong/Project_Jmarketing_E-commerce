package ku.cs.models;

import java.util.ArrayList;

public class CheckoutList {


    private ArrayList<Product> checkout;
    private String username;
    private double totalPrice;


    public CheckoutList(String username) {
        this.checkout = new ArrayList<>();
        totalPrice = 0.0;
        this.username = username;

    }


    public void addProductToCheckout(Product product, int numberItem) {
        Product proName = checkName(product.getNameProduct());
        if(proName == null){
            product.addItem(numberItem);
            this.checkout.add(product);
            return;
        }
        proName.addItem(numberItem);
        System.out.println("Same Name");
    }



    public Product checkName(String namePro) {
        for (Product pro : checkout) {
            if (pro.getNameProduct().equals(namePro)) {
                return pro;
            }
        }
        return null;
    }



    public int checkSize(){
        return checkout.size();
    }

    public double purchaseTotalPrice(){
        totalPrice = 0;
        for (Product pro : checkout) {
            totalPrice += (pro.getProductPrice() * pro.getTotalItem());
        }
        return totalPrice;
    }

    public ArrayList<Product> getAllProduct() {
        return checkout;
    }


    public ProductsList setValueOfProduct(ProductsList productsList) {
        for (Product products : checkout){
            for (Product AllProducts : productsList.getAllProductWIthDifferentNameInSameShop()) {
                if (products.getNameProduct().equals(AllProducts.getNameProduct()))         // check ชื่อว่าใน product ที่อยู่ใน productList และ checkoutList ว่าตรงกัน แล้ว เปลี่ยนแปลงค่า
                    AllProducts.setRemainProduct(AllProducts.getRemainProduct() - products.getTotalItem());
            }
        }
        return productsList;
    }

    public String getUsername(){return username;}





}

