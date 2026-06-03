package model;

import java.util.*;

/**
 * 装备类
 * 代表英雄可以装备的物品
 */
public class Equipment implements Reportable, Persistable {
    private String equipmentId;
    private String name;
    private EquipmentType equipmentType;
    private Map<String, Integer> stats;     // 属性加成，如 {"attack": 100, "defense": 50}
    private int usageCount;                  // 使用次数

    public Equipment() {
        this.stats = new HashMap<>();
        this.usageCount = 0;
    }

    public Equipment(String equipmentId, String name, EquipmentType equipmentType) {
        this.equipmentId = equipmentId;
        this.name = name;
        this.equipmentType = equipmentType;
        this.stats = new HashMap<>();
        this.usageCount = 0;
    }

    // === Getters and Setters ===

    public String getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(String equipmentId) {
        this.equipmentId = equipmentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EquipmentType getEquipmentType() {
        return equipmentType;
    }

    public void setEquipmentType(EquipmentType equipmentType) {
        this.equipmentType = equipmentType;
    }

    public Map<String, Integer> getStats() {
        return new HashMap<>(stats);
    }

    public void setStats(Map<String, Integer> stats) {
        this.stats = (stats == null) ? new HashMap<>() : new HashMap<>(stats);
    }

    public int getUsageCount() {
        return usageCount;
    }

    public void setUsageCount(int usageCount) {
        this.usageCount = usageCount;
    }

    /**
     * 增加使用次数
     */
    public void incrementUsage() {
        this.usageCount++;
    }

    /**
     * 添加一条属性加成
     */
    public void addStat(String key, int value) {
        this.stats.put(key, value);
    }

    // === equals, hashCode ===

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Equipment equipment = (Equipment) o;
        return Objects.equals(equipmentId, equipment.equipmentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(equipmentId);
    }

    @Override
    public String getInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Equipment Info ===\n");
        sb.append("ID: ").append(equipmentId).append("\n");
        sb.append("Name: ").append(name).append("\n");
        sb.append("Type: ").append(equipmentType).append("\n");
        sb.append("Usage Count: ").append(usageCount).append("\n");
        sb.append("Stats:\n");
        for (Map.Entry<String, Integer> entry : stats.entrySet()) {
            sb.append("  +").append(entry.getValue()).append(" ").append(entry.getKey()).append("\n");
        }
        return sb.toString();
    }

    // === Persistable ===

    @Override
    public String toCSVString() {
        // format: equipmentId,name,equipmentType,statKey:statValue|...,usageCount
        StringBuilder sb = new StringBuilder();
        sb.append(equipmentId).append(",");
        sb.append(name).append(",");
        sb.append(equipmentType).append(",");
        // serialize stats map
        List<String> statPairs = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : stats.entrySet()) {
            statPairs.add(entry.getKey() + ":" + entry.getValue());
        }
        sb.append(String.join("|", statPairs)).append(",");
        sb.append(usageCount);
        return sb.toString();
    }

    @Override
    public void fromCSVString(String csvLine) {
        // to be implemented in FileStorageService stage
    }

    @Override
    public String toString() {
        return "Equipment{id='" + equipmentId + "', name='" + name + "', type=" + equipmentType + "}";
    }
}
