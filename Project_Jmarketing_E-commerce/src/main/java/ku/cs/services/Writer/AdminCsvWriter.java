package ku.cs.services.Writer;

import ku.cs.models.AdminList;
import ku.cs.models.UserList;
import ku.cs.services.CsvWriter;
import ku.cs.services.DataSource;

public class AdminCsvWriter implements CsvWriter<AdminList> {

    private String path = "csv/AllUser.csv";
    private DataSource dataSource = new DataSource();
    UserList userList= dataSource.getUserList();
    @Override
    public String reWriteData(AdminList data) {
        return data.toCsvPattern()+userList.toCsvPattern();
    }

    @Override
    public String getPath() {
        return path;
    }
}
