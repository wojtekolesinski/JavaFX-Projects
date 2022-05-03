package models.request.user;

import models.request.Request;

public class GetArticlesRequest extends Request {
    private int userId;

    public GetArticlesRequest(int userId) {
        super(Request.GET_ARTICLES_REQUEST);
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
