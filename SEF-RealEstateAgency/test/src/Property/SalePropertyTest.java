package Property;

import main.model.Property.Property;
import main.model.Property.SaleProperty;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class SalePropertyTest {
    Property test = new SaleProperty(123.0);

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
        assertNotNull(test);
        assertEquals(test.getPrice(), 123.0);
    }
}