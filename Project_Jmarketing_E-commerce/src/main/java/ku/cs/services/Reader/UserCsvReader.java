package ku.cs.services.Reader;

import ku.cs.models.Admin;
import ku.cs.models.User;
import ku.cs.models.UserList;
import ku.cs.services.CsvReader;

public class UserCsvReader implements CsvReader<UserList> {
    private String path = "csv/AllUser.csv";
    @Override
    public void readData(String[] data,UserList userList) {
        if(data[0].equals("User"))
        {   User user = new User(data[0], data[1], data[2], data[3], Boolean.valueOf(data[4]), data[5], Integer.parseInt(data[6]), data[8]);
            user.setTotalPay(Double.parseDouble(data[7]));
            user.setImagePath(data[9]);
            userList.addUser(user);

        }
    }

    @Override
    public String getPath() {
        return path;
    }


}
