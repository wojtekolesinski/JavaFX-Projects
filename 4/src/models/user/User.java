package models.user;

import java.util.ArrayList;
import java.util.List;

public class User {
    private int id;
    private List<String> topics;

    public User(int id) {
        this.id = id;
        this.topics = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<String> getTopics() {
        return topics;
    }

    public void setTopics(List<String> topics) {
        this.topics = topics;
    }
}
