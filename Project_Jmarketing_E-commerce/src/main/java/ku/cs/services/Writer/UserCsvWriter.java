package ku.cs.services.Writer;

import ku.cs.models.AdminList;
import ku.cs.models.UserList;
import ku.cs.services.CsvWriter;
import ku.cs.services.DataSource;

public class UserCsvWriter implements CsvWriter<UserList> {

    private String path = "csv/AllUser.csv";
    private DataSource dataSource = new DataSource();
    AdminList adminList = dataSource.getAdminList();
    @Override
    public String reWriteData(UserList data) {
        return adminList.toCsvPattern() +data.toCsvPattern();
    }

    @Override
    public String getPath() {
        return path;
    }
}
