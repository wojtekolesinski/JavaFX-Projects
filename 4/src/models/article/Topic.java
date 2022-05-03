package models.article;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Topic {
    private StringProperty name;
    private BooleanProperty isSubscribed;

    public Topic(String name, boolean isSubscribed) {
        this.name = new SimpleStringProperty();
        this.isSubscribed = new SimpleBooleanProperty();
        this.name.set(name);
        this.isSubscribed.set(isSubscribed);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public boolean isIsSubscribed() {
        return isSubscribed.get();
    }

    public BooleanProperty isSubscribedProperty() {
        return isSubscribed;
    }

    public void setIsSubscribed(boolean isSubscribed) {
        this.isSubscribed.set(isSubscribed);
    }
}
