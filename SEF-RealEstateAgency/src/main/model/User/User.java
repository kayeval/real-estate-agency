package main.model.User;

import java.util.regex.Pattern;

public abstract class User {
    private String userID;
    private String username;
    private String email;

    public User(String username, String email) throws InvalidEmailException {
        setUsername(username);
        setEmail(email);
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    //TODO REMOVE INVALID EMAIL EXCEPTION
    public void setEmail(String email) throws InvalidEmailException {
        if (!isValidEmailFormat(email))
            throw new InvalidEmailException();
        this.email = email;
    }

    public String getUserID() {
        return this.userID;
    }

    public String getUsername() {
        return this.username;
    }

    public String getEmail() {
        return this.email;
    }

    public boolean isValidUsernameFormat(String username) {
        final Pattern USERNAME_REGEX = Pattern.compile("^[A-Za-z0-9]+(?:[ _-][A-Za-z0-9]+)*$",
                Pattern.CASE_INSENSITIVE);
        return USERNAME_REGEX.matcher(username).matches();
    }

    public boolean isValidEmailFormat(String email) {
        final Pattern EMAIL_REGEX = Pattern.compile("[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?",
                Pattern.CASE_INSENSITIVE);
        return EMAIL_REGEX.matcher(email).matches();
    }
}
