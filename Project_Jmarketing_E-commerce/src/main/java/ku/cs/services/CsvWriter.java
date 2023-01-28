package ku.cs.services;

public interface CsvWriter<T> {
    public String reWriteData(T data);
    public String getPath();
}
