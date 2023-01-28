package ku.cs.services.Writer;

import ku.cs.models.ProductTypeList;
import ku.cs.services.CsvWriter;

public class ProductTypeCsvWriter implements CsvWriter<ProductTypeList> {
    private String path = "csv/product-type.csv";
    @Override
    public String reWriteData(ProductTypeList data) {
        return data.toCsvPattern();
    }

    @Override
    public String getPath() {
        return path;
    }
}
