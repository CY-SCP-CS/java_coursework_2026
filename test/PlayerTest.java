import model.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 玩家模型类单元测试
 */
public class PlayerTest {

    @Test
    public void testConstructorAndGetters() {
        Player player = new Player("P001", "TestPlayer", "T001", 15, 50.0);
        assertEquals("P001", player.getId());
        assertEquals("TestPlayer", player.getName());
        assertEquals("T001", player.getTeamId());
        assertEquals(15, player.getLevel());
        assertEquals(50.0, player.getWinRate(), 0.001);
        assertEquals(Role.PLAYER, player.getRole());
    }

    @Test
    public void testDefaultConstructorInitializesEmptyCollections() {
        Player player = new Player();
        assertNotNull(player.getOwnedHeroes());
        assertTrue(player.getOwnedHeroes().isEmpty());
        assertNotNull(player.getAllEquippedItems());
        assertTrue(player.getAllEquippedItems().isEmpty());
    }

    @Test
    public void testLevelBoundaryLow() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Player("P002", "Low", "T001", 0, 50.0);
        });
    }

    @Test
    public void testLevelBoundaryHigh() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Player("P003", "High", "T001", 31, 50.0);
        });
    }

    @Test
    public void testLevelBoundaryValidLowest() {
        Player player = new Player("P004", "Min", "T001", 1, 50.0);
        assertEquals(1, player.getLevel());
    }

    @Test
    public void testLevelBoundaryValidHighest() {
        Player player = new Player("P005", "Max", "T001", 30, 50.0);
        assertEquals(30, player.getLevel());
    }

    @Test
    public void testWinRateBoundaryLow() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Player("P006", "LowWR", "T001", 15, -0.1);
        });
    }

    @Test
    public void testWinRateBoundaryHigh() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Player("P007", "HighWR", "T001", 15, 100.1);
        });
    }

    @Test
    public void testWinRateBoundaryValid() {
        Player pLow = new Player("P008", "MinWR", "T001", 15, 0.0);
        Player pHigh = new Player("P009", "MaxWR", "T001", 15, 100.0);
        assertEquals(0.0, pLow.getWinRate(), 0.001);
        assertEquals(100.0, pHigh.getWinRate(), 0.001);
    }

    @Test
    public void testAddHero() {
        Player player = new Player("P010", "HeroOwner", "T001", 15, 50.0);
        Hero hero = new Hero("H001", "TestHero", HeroType.WARRIOR);
        player.addHero(hero);
        assertEquals(1, player.getOwnedHeroes().size());
        assertTrue(player.getOwnedHeroes().contains(hero));
    }

    @Test
    public void testAddDuplicateHero() {
        Player player = new Player("P011", "DupTest", "T001", 15, 50.0);
        Hero hero = new Hero("H002", "Same", HeroType.MAGE);
        player.addHero(hero);
        player.addHero(hero); // should not add duplicate
        assertEquals(1, player.getOwnedHeroes().size());
    }

    @Test
    public void testAddNullHero() {
        Player player = new Player("P012", "NullTest", "T001", 15, 50.0);
        player.addHero(null);
        assertTrue(player.getOwnedHeroes().isEmpty());
    }

    @Test
    public void testRemoveHero() {
        Player player = new Player("P013", "RemoveTest", "T001", 15, 50.0);
        Hero hero = new Hero("H003", "RemoveMe", HeroType.ASSASSIN);
        player.addHero(hero);
        assertTrue(player.removeHero("H003"));
        assertEquals(0, player.getOwnedHeroes().size());
    }

    @Test
    public void testRemoveHeroNotOwned() {
        Player player = new Player("P014", "NoOwn", "T001", 15, 50.0);
        assertFalse(player.removeHero("H999"));
    }

    @Test
    public void testEquipItem() {
        Player player = new Player("P015", "EquipTest", "T001", 15, 50.0);
        player.equipItem("H001", "EQ001");
        List<String> items = player.getEquippedItems("H001");
        assertEquals(1, items.size());
        assertEquals("EQ001", items.get(0));
    }

    @Test
    public void testEquippedItemsDefensiveCopy() {
        Player player = new Player("P016", "CopyTest", "T001", 15, 50.0);
        player.equipItem("H001", "EQ001");
        List<String> items = player.getEquippedItems("H001");
        items.add("EQ002"); // should not affect player
        assertEquals(1, player.getEquippedItems("H001").size());
    }

    @Test
    public void testOwnedHeroesDefensiveCopy() {
        Player player = new Player("P017", "CopyTest2", "T001", 15, 50.0);
        player.addHero(new Hero("H010", "H10", HeroType.TANK));
        List<Hero> heroes = player.getOwnedHeroes();
        heroes.clear();
        assertEquals(1, player.getOwnedHeroes().size());
    }

    @Test
    public void testSetOwnedHeroesNull() {
        Player player = new Player("P018", "NullSet", "T001", 15, 50.0);
        player.setOwnedHeroes(null);
        assertNotNull(player.getOwnedHeroes());
        assertTrue(player.getOwnedHeroes().isEmpty());
    }

    @Test
    public void testGetInfoContainsDetails() {
        Player player = new Player("P001", "DisplayTest", "T001", 20, 60.5);
        String info = player.getInfo();
        assertTrue(info.contains("P001"));
        assertTrue(info.contains("DisplayTest"));
        assertTrue(info.contains("T001"));
        assertTrue(info.contains("20"));
        assertTrue(info.contains("60.5"));
    }

    @Test
    public void testEqualsAndHashCode() {
        Player p1 = new Player("P100", "Alice", "T001", 10, 50.0);
        Player p2 = new Player("P100", "Alice Clone", "T001", 20, 70.0);
        Player p3 = new Player("P101", "Bob", "T001", 15, 55.0);

        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());
        assertNotEquals(p1, p3);
    }

    @Test
    public void testToCSVString() {
        Player player = new Player("P001", "CSVTest", "T001", 20, 65.5);
        Hero hero = new Hero("H001", "TestHero", HeroType.WARRIOR);
        player.addHero(hero);
        String csv = player.toCSVString();
        assertTrue(csv.startsWith("P001,CSVTest,T001,20,65.5"));
        assertTrue(csv.contains("H001"));
    }

    @Test
    public void testTeamIdCanBeNull() {
        Player player = new Player("P099", "NoTeam", null, 10, 40.0);
        assertNull(player.getTeamId());
        String csv = player.toCSVString();
        assertTrue(csv.contains("P099,NoTeam,,10,40.0") || csv.contains("P099,NoTeam,,10,40"));
    }
}
