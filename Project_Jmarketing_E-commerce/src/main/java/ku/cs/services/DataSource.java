package ku.cs.services;

import ku.cs.models.AdminList;
import ku.cs.models.ProductTypeList;
import ku.cs.models.ProductsList;
import ku.cs.models.UserList;
import ku.cs.services.Reader.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DataSource {
    private ProductsList product;
    private ProductTypeList productTypeList;
    private UserList userList;
    private AdminList adminList;
    private UserList reportedlist;

    public DataSource()
    {
        userList = new UserList();
        adminList = new AdminList();
        reportedlist = new UserList();
        product = new ProductsList();
        productTypeList = new ProductTypeList();
        readData(new AdminCsvReader(),adminList);
        readData(new UserCsvReader(),userList);
        readData(new ReportedUserCsvReader(),reportedlist);
        readData(new ProductCsvReader(),product);
        readData(new ProductTypeCsvReader(), productTypeList);
    }

    public <T> void readData(CsvReader<T> Csvreader,T list)
    {
        try{
            Path file = Paths.get(Csvreader.getPath());
            BufferedReader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8);
            String line = null;

            while ((line = reader.readLine()) != null)
            {
                String data[] = line.split(",");
                Csvreader.readData(data,list);


            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public <T> void reWriteData(CsvWriter<T> csvWriter, T list)
    {
        File file = new File(csvWriter.getPath());
        FileWriter writer = null;
        BufferedWriter buffer = null;
        try {
            writer = new FileWriter(file);
            buffer = new BufferedWriter(writer);
            buffer.write(csvWriter.reWriteData(list));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                buffer.close();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public ProductsList getProducts() {
        return product;
    }
    public ProductTypeList getProductType(){return productTypeList;}
    public UserList getUserList() {
        return userList;
    }
    public  AdminList getAdminList()
    {
        return adminList;
    }
    public UserList getReportedlist(){return reportedlist;}


}
