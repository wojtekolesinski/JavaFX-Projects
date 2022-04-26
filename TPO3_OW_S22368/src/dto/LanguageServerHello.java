package dto;

import java.net.InetAddress;

public class LanguageServerHello extends DataTransferObject {

    private String language;
    private InetAddress address;
    private int port;

    public LanguageServerHello(String language, InetAddress address, int port) {
        super("hello");
        this.language = language;
        this.address = address;
        this.port = port;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public InetAddress getAddress() {
        return address;
    }

    public void setAddress(InetAddress address) {
        this.address = address;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
