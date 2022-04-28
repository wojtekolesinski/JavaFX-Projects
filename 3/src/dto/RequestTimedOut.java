package dto;

public class RequestTimedOut extends DataTransferObject {
    private String language;

    public RequestTimedOut(String language) {
        super("TimedOut");
        this.language = language;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
