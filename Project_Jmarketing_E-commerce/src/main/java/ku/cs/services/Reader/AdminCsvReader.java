package ku.cs.services.Reader;

import ku.cs.models.Admin;
import ku.cs.models.AdminList;
import ku.cs.services.CsvReader;

public class AdminCsvReader implements CsvReader<AdminList> {
    private String path = "csv/AllUser.csv";
    @Override
    public void readData(String[] data, AdminList userList) {
        if(data[0].equals("Admin"))
        {
            Admin admin = new Admin(data[0],data[1],data[2],data[3]);
            admin.setImagePath(data[5]);
            userList.addAdmin(admin);
        }
    }

    @Override
    public String getPath() {
        return path;
    }
}
