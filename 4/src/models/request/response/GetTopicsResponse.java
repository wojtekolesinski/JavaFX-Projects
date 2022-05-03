package models.request.response;

import models.request.Request;

import java.util.List;

public class GetTopicsResponse extends Request {
    private final List<String> topics;

    public GetTopicsResponse(List<String> topics) {
        super(Request.GET_TOPICS_RESPONSE);
        this.topics = topics;
    }

    public List<String> getTopics() {
        return topics;
    }
}
