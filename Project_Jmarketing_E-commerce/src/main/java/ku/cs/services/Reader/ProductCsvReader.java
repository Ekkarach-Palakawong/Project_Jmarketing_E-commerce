package ku.cs.services.Reader;

import ku.cs.models.Product;
import ku.cs.models.ProductsList;
import ku.cs.services.CsvReader;

public class ProductCsvReader implements CsvReader<ProductsList> {

    private String path = "csv/AllProduct.csv";
    @Override
    public void readData(String[] data, ProductsList productsList) {
        Product products = new Product(Integer.parseInt(data[0]),data[1],data[2],data[3],Double.parseDouble(data[4]), Integer.parseInt(data[5]), Integer.parseInt(data[6]));
        products.setAttribute(data[7]);
        products.setPathImage(data[8]);
        productsList.addProduct(products);
    }

    @Override
    public String getPath() {
        return path;
    }
}
