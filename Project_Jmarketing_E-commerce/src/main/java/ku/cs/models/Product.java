package ku.cs.models;

import java.io.*;

public class Product {
    private String nameProduct;
    private String nameShop;
    private String productType;
    private int saleItem, totalItem, id, warningProduct;
    private double productPrice;
    private int remainProduct;
    private String pathImage;
    private String attribute = "ยังไม่มีคุณลักษณะ";

    public Product(int id, String productType, String nameShop, String nameProduct, double productPrice, int remainProduct, int warningProduct) {
        this.id = id;
        this.productType = productType;
        this.nameShop = nameShop;
        this.nameProduct = nameProduct;
        this.productPrice = productPrice;
        this.remainProduct = remainProduct;
        this.warningProduct = warningProduct;
        this.pathImage = "images/productimage/default-Product.jpg";
        this.saleItem = 0;
        this.totalItem = 0;
    }

    public String getProductType() { return productType; }
    public String getNameShop() { return nameShop; }
    public String getNameProduct() { return nameProduct; }
    public int getSaleItem() {
        return saleItem;
    }
    public double getProductPrice() { return productPrice; }
    public int getRemainProduct() { return remainProduct; }
    public int getTotalItem() { return totalItem; }
    public int getId() { return id; }
    public String getAttribute(){return attribute;}
    public int getWarningProduct() { return warningProduct; }
    public String getPathImage() { return pathImage; }

    public void setProductType(String productType) { this.productType = productType; }
    public void setNameShop(String nameShop) { this.nameShop = nameShop; }
    public void setNameProduct(String nameProduct) { this.nameProduct = nameProduct; }
    public void setProductPrice(double productPrice) { this.productPrice = productPrice; }
    public void setRemainProduct(int remainProduct) { this.remainProduct = remainProduct; }
    public void setAttribute(String attribute){this.attribute = attribute;}
    public void setWarningProduct(int warningProduct) { this.warningProduct = warningProduct; }
    public void setPathImage(String pathImage) { this.pathImage = pathImage; }

    public void setDefaultAttribute()
    { this.attribute = "ยังไม่มีคุณลักษณะ"; }

    public Product addProductDataInFile() throws Exception {
        String path = "csv/AllProduct.csv";
        FileWriter fileWriter = null;
        BufferedReader buffer = null;

        try {
            boolean temp = true;
            //read file
            FileReader fileReader = new FileReader(path);
            buffer = new BufferedReader(fileReader);
            String line = null;

            while ((line = buffer.readLine()) != null) {
                String[] data = line.split(",");
                String nameShop = data[2].trim();
                String nameProduct = data[3].trim();
                String attribute = data[6].trim();
                if (this.nameProduct.equals(nameProduct) && this.nameShop.equals(nameShop) && this.attribute.equals(attribute)) {
                    System.err.println("You are currently selling this item.");
                    temp = false;
                    break;
                }
            }
            //writer
            if (temp){
                fileWriter = new FileWriter(path, true);
                BufferedWriter out = new BufferedWriter(fileWriter);
                out.write(id + "," + productType + "," + nameShop + "," + nameProduct + "," + productPrice + "," + remainProduct + "," + warningProduct + "," + attribute + "," + pathImage);
                out.flush();
                out.newLine();
                out.close();
                fileWriter.close();
                fileReader.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addItem() {
        if (this.saleItem < remainProduct) {
            this.saleItem++;
        }
    }

    public void addItem(int number){
        totalItem += number;
    }

    public void deleteItem() {
        if (this.saleItem > 0) {
            this.saleItem--;
        }
    }

    public void IncreaseItem(int item) {
        remainProduct += item;
    }

    public String toCSV() {
        return id + "," + productType + "," + nameShop + "," + nameProduct + "," + productPrice + "," + remainProduct + "," + warningProduct + "," + attribute + "," + pathImage;
    }

    @Override
    public String toString()
    {
        return this.nameProduct + " ราคา " + this.productPrice;
    }

}