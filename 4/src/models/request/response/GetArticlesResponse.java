package models.request.response;

import models.article.Article;
import models.request.Request;

import java.util.List;

public class GetArticlesResponse extends Request {
    private final List<Article> articles;

    public GetArticlesResponse(List<Article> articles) {
        super(Request.GET_ARTICLES_RESPONSE);
        this.articles = articles;
    }

    public List<Article> getArticles() {
        return articles;
    }
}
