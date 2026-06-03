import model.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 战队模型类单元测试
 */
public class TeamTest {

    @Test
    public void testConstructorAndGetters() {
        Team team = new Team("T001", "TestTeam");
        assertEquals("T001", team.getTeamId());
        assertEquals("TestTeam", team.getName());
        assertEquals(0, team.getMemberCount());
    }

    @Test
    public void testAddMember() {
        Team team = new Team("T001", "TestTeam");
        Player player = new Player("P001", "Member1", "T001", 15, 50.0);
        team.addMember(player);
        assertEquals(1, team.getMemberCount());
    }

    @Test
    public void testAddDuplicateMember() {
        Team team = new Team("T001", "TestTeam");
        Player player = new Player("P001", "Member1", "T001", 15, 50.0);
        team.addMember(player);
        team.addMember(player);
        assertEquals(1, team.getMemberCount());
    }

    @Test
    public void testAddNullMember() {
        Team team = new Team("T001", "TestTeam");
        team.addMember(null);
        assertEquals(0, team.getMemberCount());
    }

    @Test
    public void testRemoveMember() {
        Team team = new Team("T001", "TestTeam");
        Player player = new Player("P001", "Member1", "T001", 15, 50.0);
        team.addMember(player);
        assertTrue(team.removeMember("P001"));
        assertEquals(0, team.getMemberCount());
    }

    @Test
    public void testRemoveNonExistentMember() {
        Team team = new Team("T001", "TestTeam");
        assertFalse(team.removeMember("P999"));
    }

    @Test
    public void testMaxMembers() {
        assertEquals(5, Team.getMaxMembers());
    }

    @Test
    public void testCapacityExceeded() {
        Team team = new Team("T001", "FullTeam");
        for (int i = 1; i <= 5; i++) {
            team.addMember(new Player("P" + String.format("%03d", i), "M" + i, "T001", 10, 50.0));
        }
        assertEquals(5, team.getMemberCount());
        assertThrows(IllegalStateException.class, () -> {
            team.addMember(new Player("P006", "M6", "T001", 10, 50.0));
        });
    }

    @Test
    public void testAverageLevelEmpty() {
        Team team = new Team("T001", "EmptyTeam");
        assertEquals(0.0, team.getAverageLevel(), 0.001);
    }

    @Test
    public void testAverageLevel() {
        Team team = new Team("T001", "LevelTeam");
        team.addMember(new Player("P001", "A", "T001", 10, 50.0));
        team.addMember(new Player("P002", "B", "T001", 20, 50.0));
        assertEquals(15.0, team.getAverageLevel(), 0.001);
    }

    @Test
    public void testWinRateAverage() {
        Team team = new Team("T001", "WRTeam");
        team.addMember(new Player("P001", "A", "T001", 10, 60.0));
        team.addMember(new Player("P002", "B", "T001", 20, 40.0));
        assertEquals(50.0, team.getWinRate(), 0.001);
    }

    @Test
    public void testGetTopPlayer() {
        Team team = new Team("T001", "TopTeam");
        team.addMember(new Player("P001", "Low", "T001", 10, 50.0));
        team.addMember(new Player("P002", "High", "T001", 30, 50.0));
        team.addMember(new Player("P003", "Mid", "T001", 20, 50.0));
        Player top = team.getTopPlayer();
        assertNotNull(top);
        assertEquals("High", top.getName());
        assertEquals(30, top.getLevel());
    }

    @Test
    public void testGetTopPlayerEmpty() {
        Team team = new Team("T001", "Empty");
        assertNull(team.getTopPlayer());
    }

    @Test
    public void testGetMembersDefensiveCopy() {
        Team team = new Team("T001", "CopyTeam");
        team.addMember(new Player("P001", "A", "T001", 10, 50.0));
        team.getMembers().clear();
        assertEquals(1, team.getMemberCount());
    }

    @Test
    public void testEqualsAndHashCode() {
        Team t1 = new Team("T001", "Alpha");
        Team t2 = new Team("T001", "Alpha Clone");
        Team t3 = new Team("T002", "Beta");
        assertEquals(t1, t2);
        assertEquals(t1.hashCode(), t2.hashCode());
        assertNotEquals(t1, t3);
    }

    @Test
    public void testToCSVString() {
        Team team = new Team("T001", "CSVTeam");
        team.addMember(new Player("P001", "A", "T001", 10, 50.0));
        team.addMember(new Player("P002", "B", "T001", 10, 50.0));
        String csv = team.toCSVString();
        assertEquals("T001,CSVTeam,P001|P002", csv);
    }

    @Test
    public void testGetInfo() {
        Team team = new Team("T001", "InfoTeam");
        team.addMember(new Player("P001", "InfoP", "T001", 25, 70.0));
        String info = team.getInfo();
        assertTrue(info.contains("T001"));
        assertTrue(info.contains("InfoTeam"));
        assertTrue(info.contains("InfoP"));
    }
}
