package models.request.admin;

import models.request.Request;

import java.util.List;

public class RemoveTopicsRequest extends Request {
    private final List<String> topics;

    public RemoveTopicsRequest(List<String> topics) {
        super(Request.REMOVE_TOPICS_REQUEST);
        this.topics = topics;
    }

    public List<String> getTopics() {
        return topics;
    }
}
