package gui.admin;

import javafx.beans.property.BooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.text.*;
import javafx.util.converter.DefaultStringConverter;
import models.article.Article;
import models.article.Topic;
import server.Admin;
import server.Client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminController {

    public Admin admin;
    public List<String> topics;
    @FXML
    public TableView<Topic> topicsTable;
    @FXML
    public Button saveSubscriptionsButton;
    @FXML
    public ScrollPane articlesMainPane;
    @FXML
    public TextFlow articlesTextFlow;
    public Button refreshButton;
    public Button addTopicButton;

    public void initialize() {
        admin = new Admin();

        TableColumn<Topic, String> topicColumn = new TableColumn<>("Topic");
        TableColumn<Topic, BooleanProperty> subscribedColumn = new TableColumn<>("");

        topicColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        topicColumn.setMinWidth(86);
        topicColumn.setCellFactory(p -> new TextFieldTableCell<>(new DefaultStringConverter()));
        topicColumn.setOnEditCommit(e -> e.getRowValue().setName(e.getNewValue()));
        subscribedColumn.setCellValueFactory(new PropertyValueFactory<>("isSubscribed"));
        subscribedColumn.setCellFactory(p -> new CheckBoxTableCell<>());
        subscribedColumn.setMaxWidth(25);
        subscribedColumn.setOnEditCommit(e -> e.getRowValue().setIsSubscribed(e.getNewValue().get()));
        topicsTable.setEditable(true);
        topicsTable.getColumns().addAll(topicColumn, subscribedColumn);

        refresh();

    }

    @FXML
    public void saveSubscriptions(ActionEvent actionEvent) {
        admin.updateTopics(
            topicsTable.getItems()
                    .stream()
                    .map(Topic::getName)
                    .filter(t -> !t.isEmpty())
                    .toList()
        );
    }

    public void refresh() {
        topics = Admin.getTopics();
        ObservableList<Topic> data = FXCollections.observableArrayList(
                topics.stream().map(t -> new Topic(t, false)).toList()
        );
        topicsTable.setItems(data);
    }

    public void addTopic() {
        topics.add("");
        ObservableList<Topic> data = FXCollections.observableArrayList(
                topics.stream().map(t -> new Topic(t, false)).toList()
        );
        topicsTable.setItems(data);
//        refresh();
    }

    public void removeSelectedTopics() {
        admin.removeTopics(
            topicsTable.getItems()
                .stream()
                .filter(Topic::isIsSubscribed)
                .map(Topic::getName)
                .toList()
        );
        refresh();
    }
}
