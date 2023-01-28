package ku.cs.services.Reader;

import ku.cs.models.User;
import ku.cs.models.UserList;
import ku.cs.services.CsvReader;

public class ReportedUserCsvReader implements CsvReader<UserList> {
    private String path = "csv/reported-list.csv";
    @Override
    public void readData(String[] data, UserList userList) {
        User reportedUser = new User(data[0], data[1], data[2], data[3], Boolean.valueOf(data[4]), data[5], Integer.parseInt(data[6]), data[8]);
        reportedUser.setTotalPay(Double.parseDouble(data[7]));
        reportedUser.setImagePath(data[9]);
        userList.addUser(reportedUser);
    }

    @Override
    public String getPath() {
        return path;
    }
}
