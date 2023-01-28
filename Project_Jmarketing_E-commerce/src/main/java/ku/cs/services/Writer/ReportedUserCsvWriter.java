package ku.cs.services.Writer;

import ku.cs.models.UserList;
import ku.cs.services.CsvWriter;

public class ReportedUserCsvWriter implements CsvWriter<UserList> {
    private String path = "csv/reported-list.csv";
    @Override
    public String reWriteData(UserList data) {
        return data.toCsvPattern();
    }

    @Override
    public String getPath() {
        return path;
    }
}
