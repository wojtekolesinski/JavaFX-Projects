package models.request.user;

import models.request.Request;

import java.util.List;

public class SubscribeTopicsRequest extends Request {
    private final int userId;
    private final List<String> topics;

    public SubscribeTopicsRequest(int userId, List<String> topics) {
        super(Request.SUBSCRIBE_TOPICS_REQUEST);
        this.userId = userId;
        this.topics = topics;
    }

    public int getUserId() {
        return userId;
    }

    public List<String> getTopics() {
        return topics;
    }
}
