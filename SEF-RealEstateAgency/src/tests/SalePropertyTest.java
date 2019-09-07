package tests;


import model.Property.Property;
import model.Property.SaleProperty;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SalePropertyTest {
    Property test = new SaleProperty(123.0);

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
        assertEquals("Result", test.getPrice(), 123.0, 0.00001);
    }
}