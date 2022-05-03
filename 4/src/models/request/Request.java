package models.request;

public class Request {
    // user requests
    public final static String REGISTER_REQUEST = "register";
    public final static String GET_TOPICS_REQUEST = "getTopics";
    public final static String GET_ARTICLES_REQUEST = "getArticles";
    public final static String SUBSCRIBE_TOPICS_REQUEST = "subscribeTopics";
    public final static String UNSUBSCRIBE_TOPICS_REQUEST = "unsubscribeTopics";

    // admin requests
    public final static String ADD_TOPICS_REQUEST = "addTopics";
    public final static String REMOVE_TOPICS_REQUEST = "removeTopics";
    public final static String ADD_ARTICLES_REQUEST = "addArticles";

    // server responses
    public final static String REGISTER_RESPONSE = "register_r";
    public final static String GET_TOPICS_RESPONSE = "getTopics_r";
    public final static String GET_ARTICLES_RESPONSE = "getArticles_r";

    private String header;

    public Request(String header) {
        this.header = header;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }
}
