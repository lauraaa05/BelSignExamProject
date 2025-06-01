package be;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

    class OperatorTest {

        private Operator operator;

        @BeforeEach
        void setUp() {
            operator = new Operator(1, "username", "Operator", "token123", "Alice", "Smith");
        }

        @Test
        void testConstructorWithAllFields() {
            assertEquals(1, operator.getId());
            assertEquals("username", operator.getName());
            assertEquals("Operator", operator.getRole());
            assertEquals("token123", operator.getQrToken());
            assertEquals("Alice", operator.getFirstName());
            assertEquals("Smith", operator.getLastName());
        }

        @Test
        void testConstructorWithPartialFields() {
            Operator op = new Operator("John", "Doe", "jdoe", "pass123", "jdoe@example.com");

            assertEquals("John", op.getFirstName());
            assertEquals("Doe", op.getLastName());
            assertEquals("jdoe", op.getName());
            assertEquals("Operator", op.getRole());
            assertEquals("jdoe@example.com", op.getEmail());
            assertNull(op.getQrToken());
        }

        @Test
        void testDefaultConstructor() {
            Operator op = new Operator();
            assertEquals(0, op.getId());
            assertEquals("", op.getName());
            assertEquals("", op.getRole());
            assertEquals("", op.getFirstName());
            assertEquals("", op.getLastName());
            assertNull(op.getQrToken());
        }

        @Test
        void testSettersAndGetters() {
            operator.setId(10);
            operator.setName("newuser");
            operator.setRole("Admin");
            operator.setQrToken("newtoken");
            operator.setFirstName("Bob");
            operator.setLastName("Brown");
            operator.setEmail("bob@example.com");
            operator.setPassword("securepass");

            assertEquals(10, operator.getId());
            assertEquals("newuser", operator.getName());
            assertEquals("Admin", operator.getRole());
            assertEquals("newtoken", operator.getQrToken());
            assertEquals("Bob", operator.getFirstName());
            assertEquals("Brown", operator.getLastName());
            assertEquals("bob@example.com", operator.getEmail());
            assertEquals("securepass", operator.getPassword());
        }

        @Test
        void testToString() {
            String expected = "Alice Smith - username - Operator";
            assertEquals(expected, operator.toString());
        }
    }
