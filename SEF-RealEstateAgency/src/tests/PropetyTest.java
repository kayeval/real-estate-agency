package tests;

import model.PropertyImpl;


import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import abstracts.Property;

public class PropetyTest {
	
	Property test = new PropertyImpl("b123", 123.0);
	

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		//fail("Not yet implemented");
		assertNotNull(test);
		assertEquals(test.getPropertyID(), "b123");
	}

}
