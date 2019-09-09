package main.model.User.Employee;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.model.BankAccount;
import main.model.Property.RentalProperty;

public class BranchAdminTest {
	BranchAdmin admin = new BranchAdmin("name", "email");
	RentalProperty property = new RentalProperty(null, null, null, null, 0, null);
	BankAccount account = new BankAccount();
	
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
    	fail();
    	

    	assertEquals(admin.getName(), "name");
    	assertEquals(admin.getEmail(), "email");
    	assertFalse(admin.receiveDocuments("document"));
    	assertFalse(admin.collectRent(property, account));

  
    }
}
