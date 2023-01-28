package ku.cs.services.Writer;

import ku.cs.models.ProductsList;
import ku.cs.services.CsvWriter;

public class ProductCsvWriter implements CsvWriter<ProductsList> {
    private String path = "csv/AllProduct.csv";
    @Override
    public String reWriteData(ProductsList data) {
        return data.writeCSVPattern();
    }

    @Override
    public String getPath() {
        return path;
    }
}
