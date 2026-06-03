import model.*;
import org.junit.jupiter.api.*;
import service.GameDataManager;
import util.DataInitializer;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 数据初始化测试：验证所有数据量符合要求
 */
public class DataInitializerTest {

    private static GameDataManager data;

    @BeforeAll
    public static void setup() {
        data = DataInitializer.initData();
    }

    @Test
    public void testEquipmentCount() {
        assertEquals(20, data.getAllEquipment().size(), "Should have exactly 20 equipment items");
    }

    @Test
    public void testHeroCount() {
        assertEquals(15, data.getAllHeroes().size(), "Should have exactly 15 heroes");
    }

    @Test
    public void testPlayerCount() {
        assertEquals(15, data.getAllPlayers().size(), "Should have exactly 15 players");
    }

    @Test
    public void testTeamCount() {
        assertEquals(3, data.getAllTeams().size(), "Should have exactly 3 teams");
    }

    @Test
    public void testMatchCount() {
        assertEquals(10, data.getAllMatchRecords().size(), "Should have exactly 10 match records");
    }

    @Test
    public void testAdminExists() {
        Admin admin = data.getAdminById("admin");
        assertNotNull(admin, "Admin 'admin' should exist");
        assertEquals("admin123", data.getPassword("admin"));
    }

    @Test
    public void testEachPlayerHasAtLeast3Heroes() {
        for (Player p : data.getAllPlayers()) {
            assertTrue(p.getOwnedHeroes().size() >= 3,
                    "Player " + p.getId() + " should have at least 3 heroes, got " + p.getOwnedHeroes().size());
        }
    }

    @Test
    public void testEachTeamHas5Members() {
        for (Team t : data.getAllTeams()) {
            assertEquals(5, t.getMemberCount(),
                    "Team " + t.getTeamId() + " should have exactly 5 members");
        }
    }

    @Test
    public void testEachHeroHasCompatibleEquipment() {
        for (Hero h : data.getAllHeroes()) {
            assertTrue(h.getCompatibleEquipment().size() >= 3,
                    "Hero " + h.getHeroId() + " should have at least 3 compatible equipment");
        }
    }

    @Test
    public void testAllTeamIdsValid() {
        for (Player p : data.getAllPlayers()) {
            if (p.getTeamId() != null) {
                assertNotNull(data.getTeamById(p.getTeamId()),
                        "Player " + p.getId() + " references non-existent team " + p.getTeamId());
            }
        }
    }

    @Test
    public void testAllPlayerHeroReferencesValid() {
        for (Player p : data.getAllPlayers()) {
            for (Hero h : p.getOwnedHeroes()) {
                assertNotNull(data.getHeroById(h.getHeroId()),
                        "Player " + p.getId() + " has non-existent hero " + h.getHeroId());
            }
        }
    }

    @Test
    public void testAllMatchTeamsExist() {
        for (MatchRecord m : data.getAllMatchRecords()) {
            assertNotNull(data.getTeamById(m.getTeamA()), "Match " + m.getMatchId() + " references non-existent team A");
            assertNotNull(data.getTeamById(m.getTeamB()), "Match " + m.getMatchId() + " references non-existent team B");
        }
    }

    @Test
    public void testDefaultPasswords() {
        for (Player p : data.getAllPlayers()) {
            assertEquals("pass123", data.getPassword(p.getId()),
                    "Player " + p.getId() + " should have default password 'pass123'");
        }
    }
}
