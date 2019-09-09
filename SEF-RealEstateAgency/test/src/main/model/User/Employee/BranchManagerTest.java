package main.model.User.Employee;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.model.Property.SaleProperty;
import main.model.User.Employee.SalesPerson.SalesConsultant;

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
    	
    	manager = new BranchManager("name", "email");
    	partTime = new SalesConsultant("name", "email");
    	property = new SaleProperty(null, null, null, null, 0, null);
    	
    }

    @Test
    public void test() {
    	
    	assertNotNull(manager);
    	assertNotNull(partTime);
    	//assertFalse(manager.approveHours(partTime));
    	//assertFalse(manager.disapproveHours(partTime));
    	manager.assignProperty(partTime, property);
    	partTime.addProperty(property);
    	//need to add this functionality to manager
    	
    	assertNotNull(partTime.getAssignedProperties());
    	assertFalse(manager.inspectDocuments(property));
 
    	
    	
    }
    
}
