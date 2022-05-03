package models.request.admin;

import models.article.Article;
import models.request.Request;

import java.util.List;

public class AddArticlesRequest extends Request {
    private final List<Article> articles;

    public AddArticlesRequest(List<Article> articles) {
        super(Request.ADD_ARTICLES_REQUEST);
        this.articles = articles;
    }

    public List<Article> getArticles() {
        return articles;
    }
}
