import model.*;
import org.junit.jupiter.api.*;
import service.*;
import util.DataInitializer;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 认证服务单元测试
 */
public class AuthenticationServiceTest {

    private static GameDataManager dataManager;
    private AuthenticationService authService;

    @BeforeAll
    public static void setupClass() {
        dataManager = DataInitializer.initData();
    }

    @BeforeEach
    public void setup() {
        authService = new AuthenticationService(dataManager);
    }

    @Test
    public void testLoginAdminSuccess() {
        Person user = authService.login("admin", "admin123");
        assertNotNull(user);
        assertTrue(user instanceof Admin);
        assertTrue(authService.isLoggedIn());
        assertTrue(authService.isAdmin());
    }

    @Test
    public void testLoginPlayerSuccess() {
        Person user = authService.login("P001", "pass123");
        assertNotNull(user);
        assertTrue(user instanceof Player);
        assertTrue(authService.isLoggedIn());
        assertFalse(authService.isAdmin());
    }

    @Test
    public void testLoginWrongPassword() {
        Person user = authService.login("admin", "wrongpassword");
        assertNull(user);
        assertFalse(authService.isLoggedIn());
    }

    @Test
    public void testLoginNonExistentUser() {
        Person user = authService.login("NONEXIST", "pass123");
        assertNull(user);
        assertFalse(authService.isLoggedIn());
    }

    @Test
    public void testLoginWrongCase() {
        // User IDs should be case-sensitive
        Person user = authService.login("Admin", "admin123");
        assertNull(user);
    }

    @Test
    public void testLogout() {
        authService.login("admin", "admin123");
        assertTrue(authService.isLoggedIn());
        authService.logout();
        assertFalse(authService.isLoggedIn());
        assertNull(authService.getCurrentUser());
    }

    @Test
    public void testLoginReplacesPreviousSession() {
        authService.login("admin", "admin123");
        assertTrue(authService.isAdmin());
        authService.login("P001", "pass123");
        assertFalse(authService.isAdmin());
        assertTrue(authService.getCurrentUser() instanceof Player);
    }

    @Test
    public void testLogoutWithoutLogin() {
        authService.logout(); // should not throw
        assertFalse(authService.isLoggedIn());
    }

    @Test
    public void testGetCurrentUser() {
        assertNull(authService.getCurrentUser());
        authService.login("P001", "pass123");
        assertNotNull(authService.getCurrentUser());
        assertEquals("P001", authService.getCurrentUser().getId());
    }
}
