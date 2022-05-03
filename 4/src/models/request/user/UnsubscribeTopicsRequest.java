package models.request.user;

import models.request.Request;

import java.util.List;

public class UnsubscribeTopicsRequest extends Request {
    private final List<String> topics;

    public UnsubscribeTopicsRequest(List<String> topics) {
        super(Request.UNSUBSCRIBE_TOPICS_REQUEST);
        this.topics = topics;
    }

    public List<String> getTopics() {
        return topics;
    }
}
