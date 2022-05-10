package models.request.response;

import models.request.Request;

public class EmptyResponse extends Request {
    public EmptyResponse() {
        super(Request.EMPTY_RESPONSE);
    }
}
