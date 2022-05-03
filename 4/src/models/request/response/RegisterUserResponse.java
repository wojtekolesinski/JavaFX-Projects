package models.request.response;

import models.request.Request;

public class RegisterUserResponse extends Request {

    private int id;

    public RegisterUserResponse(int id) {
        super(Request.REGISTER_RESPONSE);
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
