package mobile.fhi360.covid_19selfscreeningtool;

import com.google.gson.annotations.SerializedName;

import mobile.fhi360.covid_19selfscreeningtool.model.Users;

public class Result {
    @SerializedName("error")
    private Boolean error;

    @SerializedName("message")
    private String message;

    @SerializedName("users")
    private Users users;

    public Result(Boolean error, String message, Users users) {
        this.error = error;
        this.message = message;
        this.users = users;
    }

    public Boolean getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public Users getUsers() {
        return users;
    }
}
