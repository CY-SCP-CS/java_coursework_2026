package model;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 玩家类
 * 继承 Person，代表一名游戏玩家
 * 关联：Player 拥有多个 Hero 对象
 */
public class Player extends Person implements Persistable {
    private String teamId;
    private int level;        // 1-30
    private double winRate;   // 0.0-100.0
    private List<Hero> ownedHeroes;
    private Map<String, List<String>> equippedItemIds; // heroId -> equipped equipment IDs

    public Player() {
        super();
        this.ownedHeroes = new ArrayList<>();
        this.equippedItemIds = new HashMap<>();
    }

    public Player(String id, String name, String teamId, int level, double winRate) {
        super(id, name, Role.PLAYER);
        this.teamId = teamId;
        this.level = level;
        this.winRate = winRate;
        this.ownedHeroes = new ArrayList<>();
        this.equippedItemIds = new HashMap<>();
    }

    // === Getters and Setters ===

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        if (level < 1 || level > 30) {
            throw new IllegalArgumentException("Level must be between 1 and 30, got: " + level);
        }
        this.level = level;
    }

    public double getWinRate() {
        return winRate;
    }

    public void setWinRate(double winRate) {
        if (winRate < 0.0 || winRate > 100.0) {
            throw new IllegalArgumentException("Win rate must be between 0.0 and 100.0, got: " + winRate);
        }
        this.winRate = winRate;
    }

    /**
     * 返回拥有的英雄列表（防御性拷贝）
     */
    public List<Hero> getOwnedHeroes() {
        return new ArrayList<>(ownedHeroes);
    }

    /**
     * 设置拥有的英雄列表
     */
    public void setOwnedHeroes(List<Hero> ownedHeroes) {
        this.ownedHeroes = (ownedHeroes == null) ? new ArrayList<>() : new ArrayList<>(ownedHeroes);
    }

    /**
     * 添加一个英雄到玩家
     */
    public void addHero(Hero hero) {
        if (hero != null && !ownedHeroes.contains(hero)) {
            this.ownedHeroes.add(hero);
        }
    }

    /**
     * 从玩家移除一个英雄
     */
    public boolean removeHero(String heroId) {
        equippedItemIds.remove(heroId);
        return ownedHeroes.removeIf(h -> h.getHeroId().equals(heroId));
    }

    // === Equipped Items Management ===

    /**
     * 为指定英雄装备一件物品
     */
    public void equipItem(String heroId, String equipmentId) {
        equippedItemIds.computeIfAbsent(heroId, k -> new ArrayList<>()).add(equipmentId);
    }

    /**
     * 获取指定英雄已装备的物品 ID 列表
     */
    public List<String> getEquippedItems(String heroId) {
        List<String> items = equippedItemIds.get(heroId);
        return items == null ? new ArrayList<>() : new ArrayList<>(items);
    }

    /**
     * 获取所有已装备映射（heroId -> equipmentId列表）
     */
    public Map<String, List<String>> getAllEquippedItems() {
        Map<String, List<String>> copy = new HashMap<>();
        for (Map.Entry<String, List<String>> entry : equippedItemIds.entrySet()) {
            copy.put(entry.getKey(), new ArrayList<>(entry.getValue()));
        }
        return copy;
    }

    @Override
    public String getDescription() {
        return "Player: " + getName() + " (Level " + level + ", Win Rate " + winRate + "%)";
    }

    // === Persistable ===

    @Override
    public String toCSVString() {
        // format: id,name,teamId,level,winRate,heroId1|heroId2|...
        String heroIds = ownedHeroes.stream()
                .map(Hero::getHeroId)
                .collect(Collectors.joining("|"));
        return getId() + "," + getName() + "," +
               (teamId != null ? teamId : "") + "," +
               level + "," + winRate + "," +
               heroIds;
    }

    @Override
    public void fromCSVString(String csvLine) {
        // to be implemented in FileStorageService stage
    }

    @Override
    public String getInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Player Info ===\n");
        sb.append("ID: ").append(getId()).append("\n");
        sb.append("Name: ").append(getName()).append("\n");
        sb.append("Team ID: ").append(teamId != null ? teamId : "N/A").append("\n");
        sb.append("Level: ").append(level).append("\n");
        sb.append("Win Rate: ").append(winRate).append("%\n");
        sb.append("Owned Heroes (").append(ownedHeroes.size()).append("):\n");
        for (Hero hero : ownedHeroes) {
            sb.append("  - ").append(hero.getName()).append(" (").append(hero.getHeroType()).append(")");
            List<String> equipped = getEquippedItems(hero.getHeroId());
            if (!equipped.isEmpty()) {
                sb.append(" [装备: ").append(String.join(", ", equipped)).append("]");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
