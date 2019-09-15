package main.model.User;

import java.util.regex.Pattern;

public abstract class User {
    private String userID;
    private String name;
    private String email;

    public User(String name, String email) throws InvalidEmailException {
        setName(name);
        setEmail(email);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) throws InvalidEmailException {
        if (!isValidEmailFormat(email))
            throw new InvalidEmailException();
        this.email = email;
    }

    public String getUserID() {
        return this.userID;
    }

    public String getName() {
        return this.name;
    }

    public String getEmail() {
        return this.email;
    }

    public boolean isValidEmailFormat(String email) {
        final Pattern EMAIL_REGEX = Pattern.compile("[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?",
                Pattern.CASE_INSENSITIVE);
        return EMAIL_REGEX.matcher(email).matches();
    }
}
