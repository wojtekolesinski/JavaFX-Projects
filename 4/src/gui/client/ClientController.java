package gui.client;

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
import javafx.scene.text.*;
import models.article.Article;
import models.article.Topic;
import server.Client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientController {

    public Client client;
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

    public void initialize() {
        client = new Client();
        client.registerUser();

        TableColumn<Topic, String> topicColumn = new TableColumn<>("Topic");
        TableColumn<Topic, BooleanProperty> subscribedColumn = new TableColumn<>("");

        topicColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        topicColumn.setMinWidth(86);
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
        client.subscribeTopics(
            topicsTable.getItems()
                    .stream()
                    .filter(Topic::isIsSubscribed )
                    .map(Topic::getName)
                    .toList()
        );
        updateArticles();
    }

    public void updateArticles() {
        List<Text> texts = new ArrayList<>();
        Map<String, List<String>> articles = new HashMap<>();
        for (Article article: client.getArticles()) {
            if (!articles.containsKey(article.getTopic())) {
                articles.put(article.getTopic(), new ArrayList<>());
            }
            articles.get(article.getTopic()).add(article.getText());
        }

        for (Map.Entry<String, List<String>> entry: articles.entrySet()) {
            Text title = new Text(entry.getKey() + "\n");
            title.setFont(Font.font("Helvetica", FontWeight.EXTRA_BOLD, 24));
            texts.add(title);

            for (String article: entry.getValue()) {
                Text text = new Text(article + "\n\n");
                text.setFont(Font.font("Helvetica", FontWeight.NORMAL, 16));
                texts.add(text);
            }
            texts.add(new Text("\n\n"));
        }
        articlesTextFlow.getChildren().clear();
        articlesTextFlow.getChildren().addAll(texts);
        articlesTextFlow.setLineSpacing(10);
        articlesTextFlow.setTextAlignment(TextAlignment.JUSTIFY);
    }

    public void refresh() {
        topics = Client.getTopics();
        ObservableList<Topic> data = FXCollections.observableArrayList(
                topics.stream().map(t -> new Topic(t, client.getUser().getTopics().contains(t))).toList()
        );
        topicsTable.setItems(data);

        updateArticles();
    }
}
