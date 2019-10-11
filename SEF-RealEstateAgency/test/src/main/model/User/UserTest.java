package main.model.User;

import main.model.User.PropertyOwner.Landlord;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserTest {
    @Test
    public void itShouldThrowInvalidEmailExceptionWhenSettingInvalidEmail() {
        InvalidEmailException ex = assertThrows(InvalidEmailException.class, () ->
                new Landlord("Test", "This is an invalid email format"));
        assertEquals("Invalid email format given.", ex.getMessage());
    }
}
