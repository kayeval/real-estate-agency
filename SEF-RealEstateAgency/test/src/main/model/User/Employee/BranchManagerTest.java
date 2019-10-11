package main.model.User.Employee;

import main.model.Property.SaleProperty;
import main.model.User.Employee.SalesPerson.SalesConsultant;
import main.model.User.PropertyOwner.Vendor;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;

public class BranchManagerTest {

    BranchManager manager;
    SalesConsultant partTime;
    SaleProperty property;

    @BeforeAll
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterAll
    public static void tearDownAfterClass() throws Exception {
    }

    @BeforeEach
    public void setUp() throws Exception {
//        manager = new BranchManager("name", "email@email.com");
//        partTime = new SalesConsultant("name", "email@email.com");
        property = new SaleProperty(null, null, null, null, 0, new Vendor("Test", "test@test.com"));

    }

    @Test
    public void test() {
        fail("Not yet implemented");
    }

}
