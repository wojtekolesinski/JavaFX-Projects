package dto;

public class ClientRequest extends DataTransferObject {
    private String word;
    private String language;
    private int port;

    public ClientRequest(String word, String language, int port) {
        super("ClientRequest");
        this.word = word;
        this.language = language;
        this.port = port;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
