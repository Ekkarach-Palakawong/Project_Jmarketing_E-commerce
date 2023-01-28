package ku.cs.services.Reader;

import ku.cs.models.ProductTypeList;
import ku.cs.services.CsvReader;

import java.util.ArrayList;
import java.util.Collections;

public class ProductTypeCsvReader implements CsvReader<ProductTypeList> {
    private String path = "csv/product-type.csv";
    @Override
    public void readData(String[] data, ProductTypeList productTypeList) {
        ArrayList<String> attribute = new ArrayList<>();
        Collections.addAll(attribute,data[1],data[2],data[3],data[4]);
        productTypeList.addProductType(data[0],attribute);
    }

    @Override
    public String getPath() {
        return path;
    }
}
