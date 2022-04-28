package dto;

public class ClientResponse extends DataTransferObject {
    private String word;

    public ClientResponse(String word) {
        super("clientResponse");
        this.word = word;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }
}
