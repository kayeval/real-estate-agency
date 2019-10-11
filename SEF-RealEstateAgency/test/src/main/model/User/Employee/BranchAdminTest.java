package main.model.User.Employee;

import main.model.BankAccount;
import main.model.Property.DeactivatedPropertyException;
import main.model.Property.RentalProperty;
import main.model.User.InvalidEmailException;
import main.model.User.PropertyOwner.Vendor;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.*;

public class BranchAdminTest {
    BranchAdmin admin = new BranchAdmin("name", "email@email.com", LocalDate.now(ZoneId.systemDefault()), 50000);
    RentalProperty property = new RentalProperty(null, null, null, null, 0, new Vendor("Aaa", "test@test.com"), null);
    BankAccount account = new BankAccount();

    public BranchAdminTest() throws DeactivatedPropertyException, InvalidEmailException {
    }

    @BeforeAll
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterAll
    public static void tearDownAfterClass() throws Exception {
    }

    @BeforeEach
    public void setUp() throws Exception {

    }

    @Test
    public void test() {
        assertNotNull(admin);
        assertEquals(admin.getUsername(), "name");
        assertEquals(admin.getEmail(), "email@email.com");
        assertFalse(admin.receiveDocuments("document"));
        assertFalse(admin.collectRent(property, account));
    }
}
