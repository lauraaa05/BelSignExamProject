package be;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LoginTest {

    @Test
    void testConstructorAndGetters() {
        Login login = new Login(1, "admin", "user1", "pass123");

        assertEquals(1, login.getOperatorId());
        assertEquals("admin", login.getRole());
        assertEquals("user1", login.getUsername());
        assertEquals("pass123", login.getPassword());
    }

    @Test
    void testSetters() {
        Login login = new Login(0, "", "", "");

        login.setOperatorId(2);
        login.setRole("user");
        login.setUsername("user2");
        login.setPassword("secure456");

        assertEquals(2, login.getOperatorId());
        assertEquals("user", login.getRole());
        assertEquals("user2", login.getUsername());
        assertEquals("secure456", login.getPassword());
    }
}