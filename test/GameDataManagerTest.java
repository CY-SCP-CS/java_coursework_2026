import model.*;
import org.junit.jupiter.api.*;
import service.GameDataManager;
import static org.junit.jupiter.api.Assertions.*;

/**
 * GameDataManager 单元测试
 */
public class GameDataManagerTest {

    private GameDataManager data;

    @BeforeEach
    public void setup() {
        data = new GameDataManager();
    }

    @Test
    public void testInitialState() {
        assertNotNull(data.getAllPlayers());
        assertTrue(data.getAllPlayers().isEmpty());
        assertNotNull(data.getAllHeroes());
        assertTrue(data.getAllHeroes().isEmpty());
        assertNotNull(data.getAllEquipment());
        assertTrue(data.getAllEquipment().isEmpty());
        assertNotNull(data.getAllTeams());
        assertTrue(data.getAllTeams().isEmpty());
        assertNotNull(data.getAllMatchRecords());
        assertTrue(data.getAllMatchRecords().isEmpty());
    }

    @Test
    public void testAddAndGetPlayer() {
        Player player = new Player("P001", "Test", "T001", 15, 50.0);
        data.addPlayer(player, "pass123");
        assertEquals(player, data.getPlayerById("P001"));
        assertEquals("pass123", data.getPassword("P001"));
    }

    @Test
    public void testAddDuplicatePlayerThrows() {
        data.addPlayer(new Player("P001", "A", "T001", 10, 50.0), "pass");
        assertThrows(IllegalArgumentException.class, () -> {
            data.addPlayer(new Player("P001", "B", "T001", 20, 60.0), "newpass");
        });
    }

    @Test
    public void testRemovePlayer() {
        data.addPlayer(new Player("P001", "Test", "T001", 15, 50.0), "pass");
        data.removePlayer("P001");
        assertNull(data.getPlayerById("P001"));
        assertNull(data.getPassword("P001"));
    }

    @Test
    public void testUpdatePlayer() {
        Player player = new Player("P001", "Original", "T001", 10, 50.0);
        data.addPlayer(player, "pass");
        Player updated = new Player("P001", "Updated", "T001", 20, 70.0);
        data.updatePlayer(updated);
        assertEquals("Updated", data.getPlayerById("P001").getName());
        assertEquals(20, data.getPlayerById("P001").getLevel());
    }

    @Test
    public void testUpdateNonExistentPlayerThrows() {
        assertThrows(IllegalArgumentException.class, () -> {
            data.updatePlayer(new Player("P999", "Ghost", "T001", 10, 50.0));
        });
    }

    @Test
    public void testAddTeam() {
        Team team = new Team("T001", "TestTeam");
        data.addTeam(team);
        assertEquals(team, data.getTeamById("T001"));
    }

    @Test
    public void testAddDuplicateTeamThrows() {
        data.addTeam(new Team("T001", "TeamA"));
        assertThrows(IllegalArgumentException.class, () -> {
            data.addTeam(new Team("T001", "TeamB"));
        });
    }

    @Test
    public void testRemoveTeam() {
        data.addTeam(new Team("T001", "TestTeam"));
        data.removeTeam("T001");
        assertNull(data.getTeamById("T001"));
    }

    @Test
    public void testRemoveTeamNonExistent() {
        data.removeTeam("T999"); // should not throw
    }

    @Test
    public void testAddMatchRecord() {
        MatchRecord match = new MatchRecord("M001", java.time.LocalDate.parse("2026-06-01"),
                "T001", "T002", MatchResult.WIN);
        data.addMatchRecord(match);
        assertEquals(1, data.getAllMatchRecords().size());
    }

    @Test
    public void testRemoveMatchRecordExisting() {
        data.addMatchRecord(new MatchRecord("M001", java.time.LocalDate.parse("2026-06-01"),
                "T001", "T002", MatchResult.WIN));
        assertTrue(data.removeMatchRecord("M001"));
        assertTrue(data.getAllMatchRecords().isEmpty());
    }

    @Test
    public void testRemoveMatchRecordNonExistent() {
        assertFalse(data.removeMatchRecord("M999"));
    }

    @Test
    public void testFindPersonByIdPlayer() {
        data.addPlayer(new Player("P001", "Test", "T001", 15, 50.0), "pass");
        Person found = data.findPersonById("P001");
        assertNotNull(found);
        assertTrue(found instanceof Player);
    }

    @Test
    public void testFindPersonByIdAdmin() {
        data.addAdmin(new Admin("admin", "Admin", 1), "admin");
        Person found = data.findPersonById("admin");
        assertNotNull(found);
        assertTrue(found instanceof Admin);
    }

    @Test
    public void testFindPersonByIdNotFound() {
        assertNull(data.findPersonById("NONEXIST"));
    }

    @Test
    public void testGetAllMatchRecordsDefensiveCopy() {
        data.addMatchRecord(new MatchRecord("M001", java.time.LocalDate.parse("2026-06-01"),
                "T001", "T002", MatchResult.WIN));
        data.getAllMatchRecords().clear();
        assertEquals(1, data.getAllMatchRecords().size());
    }

    @Test
    public void testAddEquipment() {
        Equipment eq = new Equipment("EQ001", "TestEq", EquipmentType.OFFENSIVE);
        data.addEquipment(eq);
        assertEquals(eq, data.getEquipmentById("EQ001"));
    }

    @Test
    public void testUpdateEquipment() {
        Equipment eq = new Equipment("EQ001", "Original", EquipmentType.OFFENSIVE);
        data.addEquipment(eq);
        Equipment updated = new Equipment("EQ001", "Updated", EquipmentType.DEFENSIVE);
        data.updateEquipment(updated);
        assertEquals(EquipmentType.DEFENSIVE, data.getEquipmentById("EQ001").getEquipmentType());
    }

    @Test
    public void testUpdateHero() {
        Hero hero = new Hero("H001", "Original", HeroType.WARRIOR);
        data.addHero(hero);
        Hero updated = new Hero("H001", "Updated", HeroType.MAGE);
        data.updateHero(updated);
        assertEquals(HeroType.MAGE, data.getHeroById("H001").getHeroType());
    }
}
