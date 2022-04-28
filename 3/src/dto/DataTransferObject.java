package dto;

public class DataTransferObject {
    private String header;

    public DataTransferObject(String header) {
        this.header = header;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }
}
