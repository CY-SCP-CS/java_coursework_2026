import model.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 管理员模型类单元测试
 */
public class AdminTest {

    @Test
    public void testConstructor() {
        Admin admin = new Admin("admin", "System Admin", 1);
        assertEquals("admin", admin.getId());
        assertEquals("System Admin", admin.getName());
        assertEquals(Role.ADMIN, admin.getRole());
        assertEquals(1, admin.getAdminLevel());
    }

    @Test
    public void testDefaultConstructor() {
        Admin admin = new Admin();
        assertEquals(1, admin.getAdminLevel());
    }

    @Test
    public void testSetAdminLevel() {
        Admin admin = new Admin("admin", "Admin", 1);
        admin.setAdminLevel(3);
        assertEquals(3, admin.getAdminLevel());
    }

    @Test
    public void testGetDescription() {
        Admin admin = new Admin("admin", "Root", 2);
        String desc = admin.getDescription();
        assertTrue(desc.contains("Admin"));
        assertTrue(desc.contains("Root"));
        assertTrue(desc.contains("2"));
    }

    @Test
    public void testGetInfo() {
        Admin admin = new Admin("admin", "SuperAdmin", 1);
        String info = admin.getInfo();
        assertTrue(info.contains("admin"));
        assertTrue(info.contains("SuperAdmin"));
        assertTrue(info.contains("Full data management"));
    }
}
