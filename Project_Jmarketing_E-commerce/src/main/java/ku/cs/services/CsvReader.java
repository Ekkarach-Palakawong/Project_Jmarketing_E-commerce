package ku.cs.services;

public interface CsvReader<T> {
    public void readData(String[] data,T userlist);
    public String getPath();
}
