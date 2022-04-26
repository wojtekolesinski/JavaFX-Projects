package dto;

import java.net.InetAddress;

public class LanguageServerRequest extends DataTransferObject {
    private String word;
    private InetAddress clientAddress;
    private int clientPort;

    public LanguageServerRequest(String word, InetAddress clientAddress, int clientPort) {
        super("LanguageServerRequest");
        this.word = word;
        this.clientAddress = clientAddress;
        this.clientPort = clientPort;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public InetAddress getClientAddress() {
        return clientAddress;
    }

    public void setClientAddress(InetAddress clientAddress) {
        this.clientAddress = clientAddress;
    }

    public int getClientPort() {
        return clientPort;
    }

    public void setClientPort(int clientPort) {
        this.clientPort = clientPort;
    }
}
