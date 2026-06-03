package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 战队类
 * 包含多个 Player 对象（聚合关系）
 */
public class Team implements Reportable, Persistable {
    private static final int MAX_MEMBERS = 5;

    private String teamId;
    private String name;
    private List<Player> members;   // 聚合：Team 包含多个 Player

    public Team() {
        this.members = new ArrayList<>();
    }

    public Team(String teamId, String name) {
        this.teamId = teamId;
        this.name = name;
        this.members = new ArrayList<>();
    }

    // === Getters and Setters ===

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Player> getMembers() {
        return new ArrayList<>(members);
    }

    public void setMembers(List<Player> members) {
        this.members = (members == null) ? new ArrayList<>() : new ArrayList<>(members);
    }

    /**
     * 添加队员
     */
    public void addMember(Player player) {
        if (members.size() >= MAX_MEMBERS) {
            throw new IllegalStateException("Team is full (max " + MAX_MEMBERS + " members)");
        }
        if (player != null && !members.contains(player)) {
            this.members.add(player);
        }
    }

    /** 获取最大队员数 */
    public static int getMaxMembers() {
        return MAX_MEMBERS;
    }

    /**
     * 移除队员
     */
    public boolean removeMember(String playerId) {
        return members.removeIf(p -> p.getId().equals(playerId));
    }

    /**
     * 获取队员人数
     */
    public int getMemberCount() {
        return members.size();
    }

    /**
     * 计算战队平均等级
     */
    public double getAverageLevel() {
        if (members.isEmpty()) return 0.0;
        int total = 0;
        for (Player p : members) {
            total += p.getLevel();
        }
        return (double) total / members.size();
    }

    /**
     * 计算战队平均胜率
     */
    public double getWinRate() {
        if (members.isEmpty()) return 0.0;
        double total = 0.0;
        for (Player p : members) {
            total += p.getWinRate();
        }
        return total / members.size();
    }

    /**
     * 获取战队中等级最高的玩家（"最佳队员"）
     */
    public Player getTopPlayer() {
        if (members.isEmpty()) return null;
        Player top = members.get(0);
        for (Player p : members) {
            if (p.getLevel() > top.getLevel()) {
                top = p;
            }
        }
        return top;
    }

    // === equals, hashCode ===

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Team team = (Team) o;
        return Objects.equals(teamId, team.teamId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamId);
    }

    // === Persistable ===

    @Override
    public String toCSVString() {
        // format: teamId,name,playerId1|playerId2|...
        String memberIds = members.stream()
                .map(Player::getId)
                .collect(Collectors.joining("|"));
        return teamId + "," + name + "," + memberIds;
    }

    @Override
    public void fromCSVString(String csvLine) {
        // to be implemented in FileStorageService stage
    }

    @Override
    public String getInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Team Info ===\n");
        sb.append("ID: ").append(teamId).append("\n");
        sb.append("Name: ").append(name).append("\n");
        sb.append("Members (").append(members.size()).append("):\n");
        for (Player p : members) {
            sb.append("  - ").append(p.getName()).append(" (Lv.").append(p.getLevel()).append(")\n");
        }
        sb.append("Average Level: ").append(String.format("%.1f", getAverageLevel())).append("\n");
        sb.append("Win Rate: ").append(String.format("%.1f", getWinRate())).append("%\n");
        Player top = getTopPlayer();
        if (top != null) {
            sb.append("Top Player: ").append(top.getName()).append(" (Lv.").append(top.getLevel()).append(")\n");
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return "Team{id='" + teamId + "', name='" + name + "', members=" + members.size() + "}";
    }
}
