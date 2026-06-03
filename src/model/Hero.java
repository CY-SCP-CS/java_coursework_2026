package model;

import java.util.*;

/**
 * 英雄类
 * 代表一个可玩英雄，包含类型、属性和装备兼容性
 * 关联：Hero 可使用多个 Equipment 对象
 */
public class Hero implements Reportable {
    private String heroId;
    private String name;
    private HeroType heroType;
    private Map<String, Integer> baseStats;        // 基础属性，如 {"hp": 3000, "attack": 200}
    private List<Equipment> compatibleEquipment;    // 兼容的装备列表

    public Hero() {
        this.baseStats = new HashMap<>();
        this.compatibleEquipment = new ArrayList<>();
    }

    public Hero(String heroId, String name, HeroType heroType) {
        this.heroId = heroId;
        this.name = name;
        this.heroType = heroType;
        this.baseStats = new HashMap<>();
        this.compatibleEquipment = new ArrayList<>();
    }

    // === Getters and Setters ===

    public String getHeroId() {
        return heroId;
    }

    public void setHeroId(String heroId) {
        this.heroId = heroId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HeroType getHeroType() {
        return heroType;
    }

    public void setHeroType(HeroType heroType) {
        this.heroType = heroType;
    }

    public Map<String, Integer> getBaseStats() {
        return new HashMap<>(baseStats);
    }

    public void setBaseStats(Map<String, Integer> baseStats) {
        this.baseStats = (baseStats == null) ? new HashMap<>() : new HashMap<>(baseStats);
    }

    /**
     * 添加一条基础属性
     */
    public void addStat(String key, int value) {
        this.baseStats.put(key, value);
    }

    /**
     * 获取某项属性值
     */
    public int getStat(String key) {
        return this.baseStats.getOrDefault(key, 0);
    }

    public List<Equipment> getCompatibleEquipment() {
        return new ArrayList<>(compatibleEquipment);
    }

    public void setCompatibleEquipment(List<Equipment> compatibleEquipment) {
        this.compatibleEquipment = (compatibleEquipment == null) ? new ArrayList<>() : new ArrayList<>(compatibleEquipment);
    }

    /**
     * 添加一件兼容装备
     */
    public void addEquipment(Equipment equipment) {
        if (equipment != null && !compatibleEquipment.contains(equipment)) {
            this.compatibleEquipment.add(equipment);
        }
    }

    // === equals, hashCode ===

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hero hero = (Hero) o;
        return Objects.equals(heroId, hero.heroId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(heroId);
    }

    @Override
    public String getInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Hero Info ===\n");
        sb.append("ID: ").append(heroId).append("\n");
        sb.append("Name: ").append(name).append("\n");
        sb.append("Type: ").append(heroType).append("\n");
        sb.append("Base Stats:\n");
        for (Map.Entry<String, Integer> entry : baseStats.entrySet()) {
            sb.append("  ").append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        sb.append("Compatible Equipment (").append(compatibleEquipment.size()).append("):\n");
        for (Equipment eq : compatibleEquipment) {
            sb.append("  - ").append(eq.getName()).append(" (").append(eq.getEquipmentType()).append(")\n");
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return "Hero{id='" + heroId + "', name='" + name + "', type=" + heroType + "}";
    }
}
