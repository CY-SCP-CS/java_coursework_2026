import model.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 英雄和装备模型类单元测试
 */
public class HeroEquipmentTest {

    // ==================== Hero Tests ====================

    @Test
    public void testHeroConstructorAndGetters() {
        Hero hero = new Hero("H001", "TestHero", HeroType.WARRIOR);
        assertEquals("H001", hero.getHeroId());
        assertEquals("TestHero", hero.getName());
        assertEquals(HeroType.WARRIOR, hero.getHeroType());
    }

    @Test
    public void testHeroDefaultConstructor() {
        Hero hero = new Hero();
        assertNotNull(hero.getBaseStats());
        assertTrue(hero.getBaseStats().isEmpty());
        assertNotNull(hero.getCompatibleEquipment());
        assertTrue(hero.getCompatibleEquipment().isEmpty());
    }

    @Test
    public void testHeroAddStat() {
        Hero hero = new Hero("H001", "StatHero", HeroType.WARRIOR);
        hero.addStat("hp", 3000);
        hero.addStat("attack", 200);
        assertEquals(3000, hero.getStat("hp"));
        assertEquals(200, hero.getStat("attack"));
        assertEquals(0, hero.getStat("nonexistent"));
    }

    @Test
    public void testHeroBaseStatsDefensiveCopy() {
        Hero hero = new Hero("H001", "CopyHero", HeroType.MAGE);
        hero.addStat("hp", 3000);
        hero.getBaseStats().clear();
        assertEquals(1, hero.getBaseStats().size());
    }

    @Test
    public void testHeroAddEquipment() {
        Hero hero = new Hero("H001", "EqHero", HeroType.WARRIOR);
        Equipment eq = new Equipment("EQ001", "TestEq", EquipmentType.OFFENSIVE);
        hero.addEquipment(eq);
        assertEquals(1, hero.getCompatibleEquipment().size());
    }

    @Test
    public void testHeroAddNullEquipment() {
        Hero hero = new Hero("H001", "NullEq", HeroType.WARRIOR);
        hero.addEquipment(null);
        assertTrue(hero.getCompatibleEquipment().isEmpty());
    }

    @Test
    public void testHeroCompatibleEquipmentDefensiveCopy() {
        Hero hero = new Hero("H001", "DefenseCopy", HeroType.TANK);
        hero.addEquipment(new Equipment("EQ001", "E1", EquipmentType.DEFENSIVE));
        hero.getCompatibleEquipment().clear();
        assertEquals(1, hero.getCompatibleEquipment().size());
    }

    @Test
    public void testHeroSetBaseStatsNull() {
        Hero hero = new Hero("H001", "NullStats", HeroType.SUPPORT);
        hero.setBaseStats(null);
        assertNotNull(hero.getBaseStats());
        assertTrue(hero.getBaseStats().isEmpty());
    }

    @Test
    public void testHeroEqualsAndHashCode() {
        Hero h1 = new Hero("H001", "Alpha", HeroType.WARRIOR);
        Hero h2 = new Hero("H001", "Alpha Clone", HeroType.MAGE); // different name/type but same ID
        Hero h3 = new Hero("H002", "Beta", HeroType.WARRIOR);
        assertEquals(h1, h2);
        assertEquals(h1.hashCode(), h2.hashCode());
        assertNotEquals(h1, h3);
    }

    @Test
    public void testHeroGetInfo() {
        Hero hero = new Hero("H001", "InfoHero", HeroType.MAGE);
        hero.addStat("hp", 3000);
        hero.addStat("attack", 150);
        String info = hero.getInfo();
        assertTrue(info.contains("H001"));
        assertTrue(info.contains("InfoHero"));
        assertTrue(info.contains("MAGE"));
        assertTrue(info.contains("hp"));
        assertTrue(info.contains("3000"));
    }

    @Test
    public void testHeroToCSVString() {
        Hero hero = new Hero("H001", "CSVHero", HeroType.MARKSMAN);
        hero.addStat("hp", 2800);
        hero.addStat("attack", 190);
        Equipment eq = new Equipment("EQ001", "Eq1", EquipmentType.OFFENSIVE);
        hero.addEquipment(eq);
        String csv = hero.toCSVString();
        assertTrue(csv.startsWith("H001,CSVHero,MARKSMAN"));
        assertTrue(csv.contains("hp:2800"));
        assertTrue(csv.contains("EQ001"));
    }

    // ==================== Equipment Tests ====================

    @Test
    public void testEquipmentConstructorAndGetters() {
        Equipment eq = new Equipment("EQ001", "TestEq", EquipmentType.OFFENSIVE);
        assertEquals("EQ001", eq.getEquipmentId());
        assertEquals("TestEq", eq.getName());
        assertEquals(EquipmentType.OFFENSIVE, eq.getEquipmentType());
        assertEquals(0, eq.getUsageCount());
    }

    @Test
    public void testEquipmentDefaultConstructor() {
        Equipment eq = new Equipment();
        assertNotNull(eq.getStats());
        assertTrue(eq.getStats().isEmpty());
        assertEquals(0, eq.getUsageCount());
    }

    @Test
    public void testEquipmentAddStat() {
        Equipment eq = new Equipment("EQ001", "StatEq", EquipmentType.MAGIC);
        eq.addStat("magic_attack", 240);
        eq.addStat("hp", 500);
        assertEquals(240, eq.getStats().get("magic_attack").intValue());
        assertEquals(500, eq.getStats().get("hp").intValue());
    }

    @Test
    public void testEquipmentStatsDefensiveCopy() {
        Equipment eq = new Equipment("EQ001", "CopyEq", EquipmentType.DEFENSIVE);
        eq.addStat("defense", 100);
        eq.getStats().clear();
        assertEquals(1, eq.getStats().size());
    }

    @Test
    public void testSetStatsNull() {
        Equipment eq = new Equipment("EQ001", "NullStats", EquipmentType.MOVEMENT);
        eq.setStats(null);
        assertNotNull(eq.getStats());
        assertTrue(eq.getStats().isEmpty());
    }

    @Test
    public void testIncrementUsage() {
        Equipment eq = new Equipment("EQ001", "UsageEq", EquipmentType.OFFENSIVE);
        assertEquals(0, eq.getUsageCount());
        eq.incrementUsage();
        assertEquals(1, eq.getUsageCount());
        eq.incrementUsage();
        assertEquals(2, eq.getUsageCount());
    }

    @Test
    public void testEquipmentEqualsAndHashCode() {
        Equipment e1 = new Equipment("EQ001", "Alpha", EquipmentType.OFFENSIVE);
        Equipment e2 = new Equipment("EQ001", "Alpha Clone", EquipmentType.DEFENSIVE);
        Equipment e3 = new Equipment("EQ002", "Beta", EquipmentType.OFFENSIVE);
        assertEquals(e1, e2);
        assertEquals(e1.hashCode(), e2.hashCode());
        assertNotEquals(e1, e3);
    }

    @Test
    public void testEquipmentGetInfo() {
        Equipment eq = new Equipment("EQ001", "InfoEq", EquipmentType.OFFENSIVE);
        eq.addStat("attack", 100);
        eq.incrementUsage();
        String info = eq.getInfo();
        assertTrue(info.contains("EQ001"));
        assertTrue(info.contains("InfoEq"));
        assertTrue(info.contains("OFFENSIVE"));
        assertTrue(info.contains("1"));
    }

    @Test
    public void testEquipmentToCSVString() {
        Equipment eq = new Equipment("EQ001", "CSVEq", EquipmentType.OFFENSIVE);
        eq.addStat("attack", 100);
        eq.addStat("crit_rate", 25);
        eq.setUsageCount(5);
        String csv = eq.toCSVString();
        assertTrue(csv.contains("EQ001"));
        assertTrue(csv.contains("CSVEq"));
        assertTrue(csv.contains("OFFENSIVE"));
        assertTrue(csv.contains("attack:100"));
        assertTrue(csv.endsWith(",5") || csv.endsWith(",5\r"));
    }
}
