package models.request.user;

import models.request.Request;

public class RegisterUserRequest extends Request {

    private int id;

    public RegisterUserRequest() {
        this(-1);
    }

    public RegisterUserRequest(int id) {
        super(Request.REGISTER_REQUEST);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
