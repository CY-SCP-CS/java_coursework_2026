import model.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 比赛记录模型类单元测试
 */
public class MatchRecordTest {

    @Test
    public void testConstructorAndGetters() {
        MatchRecord match = new MatchRecord("M001", java.time.LocalDate.parse("2026-06-01"),
                "T001", "T002", MatchResult.WIN);
        assertEquals("M001", match.getMatchId());
        assertEquals(java.time.LocalDate.parse("2026-06-01"), match.getDate());
        assertEquals("T001", match.getTeamA());
        assertEquals("T002", match.getTeamB());
        assertEquals(MatchResult.WIN, match.getResult());
    }

    @Test
    public void testDefaultConstructor() {
        MatchRecord match = new MatchRecord();
        assertNotNull(match.getHeroPicks());
        assertTrue(match.getHeroPicks().isEmpty());
    }

    @Test
    public void testAddHeroPick() {
        MatchRecord match = new MatchRecord("M001", java.time.LocalDate.parse("2026-06-01"),
                "T001", "T002", MatchResult.WIN);
        match.addHeroPick("H001");
        match.addHeroPick("H002");
        assertEquals(2, match.getHeroPicks().size());
        assertTrue(match.getHeroPicks().contains("H001"));
    }

    @Test
    public void testAddNullHeroPick() {
        MatchRecord match = new MatchRecord("M001", java.time.LocalDate.parse("2026-06-01"),
                "T001", "T002", MatchResult.WIN);
        match.addHeroPick(null);
        assertTrue(match.getHeroPicks().isEmpty());
    }

    @Test
    public void testAddDuplicateHeroPick() {
        MatchRecord match = new MatchRecord("M001", java.time.LocalDate.parse("2026-06-01"),
                "T001", "T002", MatchResult.WIN);
        match.addHeroPick("H001");
        match.addHeroPick("H001");
        assertEquals(1, match.getHeroPicks().size());
    }

    @Test
    public void testHeroPicksDefensiveCopy() {
        MatchRecord match = new MatchRecord("M001", java.time.LocalDate.parse("2026-06-01"),
                "T001", "T002", MatchResult.WIN);
        match.addHeroPick("H001");
        match.getHeroPicks().clear();
        assertEquals(1, match.getHeroPicks().size());
    }

    @Test
    public void testSetHeroPicksNull() {
        MatchRecord match = new MatchRecord("M001", java.time.LocalDate.parse("2026-06-01"),
                "T001", "T002", MatchResult.WIN);
        match.setHeroPicks(null);
        assertNotNull(match.getHeroPicks());
        assertTrue(match.getHeroPicks().isEmpty());
    }

    @Test
    public void testEqualsAndHashCode() {
        MatchRecord m1 = new MatchRecord("M001", java.time.LocalDate.parse("2026-06-01"),
                "T001", "T002", MatchResult.WIN);
        MatchRecord m2 = new MatchRecord("M001", java.time.LocalDate.parse("2026-06-02"),
                "T003", "T004", MatchResult.LOSE);
        MatchRecord m3 = new MatchRecord("M002", java.time.LocalDate.parse("2026-06-01"),
                "T001", "T002", MatchResult.WIN);
        assertEquals(m1, m2);
        assertEquals(m1.hashCode(), m2.hashCode());
        assertNotEquals(m1, m3);
    }

    @Test
    public void testMatchResultValues() {
        assertEquals(3, MatchResult.values().length);
        assertTrue(MatchResult.valueOf("WIN") == MatchResult.WIN);
        assertTrue(MatchResult.valueOf("LOSE") == MatchResult.LOSE);
        assertTrue(MatchResult.valueOf("DRAW") == MatchResult.DRAW);
    }

    @Test
    public void testToCSVString() {
        MatchRecord match = new MatchRecord("M001", java.time.LocalDate.parse("2026-06-01"),
                "T001", "T002", MatchResult.WIN);
        match.addHeroPick("H001");
        match.addHeroPick("H007");
        String csv = match.toCSVString();
        assertTrue(csv.startsWith("M001,2026-06-01,T001,T002,WIN"));
        assertTrue(csv.contains("H001"));
        assertTrue(csv.contains("H007"));
    }

    @Test
    public void testGetInfo() {
        MatchRecord match = new MatchRecord("M001", java.time.LocalDate.parse("2026-06-01"),
                "T001", "T002", MatchResult.DRAW);
        String info = match.getInfo();
        assertTrue(info.contains("M001"));
        assertTrue(info.contains("2026-06-01"));
        assertTrue(info.contains("T001"));
        assertTrue(info.contains("T002"));
        assertTrue(info.contains("DRAW"));
    }
}
