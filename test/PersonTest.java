import model.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Person 抽象类的基本功能测试
 */
public class PersonTest {

    @Test
    public void testPlayerIsInstanceOfPerson() {
        Player player = new Player("P001", "Test", "T001", 15, 50.0);
        assertTrue(player instanceof Person);
        assertTrue(player instanceof Reportable);
    }

    @Test
    public void testAdminIsInstanceOfPerson() {
        Admin admin = new Admin("admin", "Admin", 1);
        assertTrue(admin instanceof Person);
        assertTrue(admin instanceof Reportable);
    }

    @Test
    public void testRoleAssignment() {
        Player player = new Player("P001", "Test", "T001", 15, 50.0);
        Admin admin = new Admin("admin", "Admin", 1);
        assertEquals(Role.PLAYER, player.getRole());
        assertEquals(Role.ADMIN, admin.getRole());
    }

    @Test
    public void testPersonToString() {
        Player player = new Player("P001", "TestPlayer", "T001", 15, 50.0);
        String str = player.toString();
        assertTrue(str.contains("P001"));
        assertTrue(str.contains("TestPlayer"));
    }

    @Test
    public void testRoleValues() {
        assertEquals(2, Role.values().length);
        assertEquals(Role.ADMIN, Role.valueOf("ADMIN"));
        assertEquals(Role.PLAYER, Role.valueOf("PLAYER"));
    }

    @Test
    public void testHeroTypeValues() {
        assertEquals(7, HeroType.values().length);
    }

    @Test
    public void testEquipmentTypeValues() {
        assertEquals(5, EquipmentType.values().length);
    }

    @Test
    public void testMatchResultValues() {
        assertEquals(3, MatchResult.values().length);
    }
}
