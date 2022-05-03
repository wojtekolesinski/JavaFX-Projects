package models.request.admin;

import models.request.Request;

import java.util.List;

public class AddTopicsRequest extends Request {
    private final List<String> topics;

    public AddTopicsRequest(List<String> topics) {
        super(Request.ADD_TOPICS_REQUEST);
        this.topics = topics;
    }

    public List<String> getTopics() {
        return topics;
    }
}
