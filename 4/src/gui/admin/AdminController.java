package gui.admin;

import javafx.beans.property.BooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.DefaultStringConverter;
import models.article.Article;
import models.article.Topic;
import server.Admin;

import java.util.List;

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
    public Button refreshButton;
    public Button addTopicButton;
    public TextArea articleInputTextArea;
    public ComboBox<String> topicsComboBox;
    public boolean currentArticleIsSaved = false;

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
    public void save(ActionEvent actionEvent) {
        admin.updateTopics(
            topicsTable.getItems()
                    .stream()
                    .map(Topic::getName)
                    .filter(t -> !t.isEmpty())
                    .toList()
        );
        if (topicsComboBox.getValue() != null) {
            admin.addArticle(new Article(topicsComboBox.getValue(), articleInputTextArea.getText()));
            currentArticleIsSaved = true;
        }

    }

    public void refresh() {
        topics = Admin.getTopics();
        ObservableList<Topic> data = FXCollections.observableArrayList(
                topics.stream().map(t -> new Topic(t, false)).toList()
        );
        topicsTable.setItems(data);

        topicsComboBox.setItems(FXCollections.observableArrayList(topics));

        if (currentArticleIsSaved) {
            topicsComboBox.setValue(null);
            articleInputTextArea.setText("");
            currentArticleIsSaved = false;
        }

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
