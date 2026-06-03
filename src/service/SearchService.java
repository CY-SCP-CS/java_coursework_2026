package service;

import model.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 搜索服务
 * 提供玩家、战队、英雄的查询功能，以及格式化信息展示
 */
public class SearchService {
    private GameDataManager dataManager;

    public SearchService(GameDataManager dataManager) {
        this.dataManager = dataManager;
    }

    /**
     * 按 ID 搜索玩家（精确匹配）
     */
    public Player searchPlayerById(String id) {
        return dataManager.getPlayerById(id);
    }

    /**
     * 按名称搜索玩家（模糊匹配，大小写不敏感）
     */
    public List<Player> searchPlayerByName(String name) {
        String lowerName = name.toLowerCase();
        return dataManager.getAllPlayers().stream()
                .filter(p -> p.getName().toLowerCase().contains(lowerName))
                .collect(Collectors.toList());
    }

    /**
     * 按 ID 搜索战队
     */
    public Team searchTeamById(String id) {
        return dataManager.getTeamById(id);
    }

    /**
     * 按名称搜索战队（精确匹配）
     */
    public Team searchTeamByName(String name) {
        return dataManager.getAllTeams().stream()
                .filter(t -> t.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    /**
     * 按名称搜索英雄（模糊匹配）
     */
    public List<Hero> searchHeroByName(String name) {
        String lowerName = name.toLowerCase();
        return dataManager.getAllHeroes().stream()
                .filter(h -> h.getName().toLowerCase().contains(lowerName))
                .collect(Collectors.toList());
    }

    // ========================================================================
    //  Display Methods
    // ========================================================================

    /**
     * 格式化显示玩家详情
     * 包含：ID、名称、战队、等级、胜率、拥有英雄（含装备）
     */
    public String displayPlayerDetail(Player player) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Player Detail ===\n");
        sb.append("ID: ").append(player.getId()).append("\n");
        sb.append("Name: ").append(player.getName()).append("\n");
        sb.append("Team: ").append(player.getTeamId() != null ? player.getTeamId() : "N/A").append("\n");
        sb.append("Level: ").append(player.getLevel()).append("\n");
        sb.append("Win Rate: ").append(String.format("%.1f%%", player.getWinRate())).append("\n");
        sb.append("Heroes:\n");
        List<Hero> heroes = player.getOwnedHeroes();
        if (heroes.isEmpty()) {
            sb.append("  (none)\n");
        } else {
            for (Hero hero : heroes) {
                sb.append("  - ").append(hero.getName()).append(" (").append(hero.getHeroType()).append(")\n");
                List<String> equipped = player.getEquippedItems(hero.getHeroId());
                if (!equipped.isEmpty()) {
                    sb.append("    Equipment:");
                    for (String eqId : equipped) {
                        Equipment eq = dataManager.getEquipmentById(eqId);
                        sb.append(" ").append(eq != null ? eq.getName() : eqId);
                    }
                    sb.append("\n");
                }
            }
        }
        return sb.toString();
    }

    /**
     * 格式化显示战队详情
     * 包含：战队名、所有成员、平均等级、胜率、最佳队员
     */
    public String displayTeamDetail(Team team) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Team Detail ===\n");
        sb.append("ID: ").append(team.getTeamId()).append("\n");
        sb.append("Name: ").append(team.getName()).append("\n");
        sb.append("Members (").append(team.getMembers().size()).append("):\n");
        for (Player p : team.getMembers()) {
            sb.append("  - ").append(p.getName())
              .append(" (Lv.").append(p.getLevel())
              .append(", WR: ").append(String.format("%.1f%%", p.getWinRate()))
              .append(")\n");
        }
        sb.append("Average Level: ").append(String.format("%.1f", team.getAverageLevel())).append("\n");
        sb.append("Average Win Rate: ").append(String.format("%.1f%%", team.getWinRate())).append("\n");
        Player top = team.getTopPlayer();
        if (top != null) {
            sb.append("Best Player: ").append(top.getName())
              .append(" (Lv.").append(top.getLevel())
              .append(", WR: ").append(String.format("%.1f%%", top.getWinRate()))
              .append(")\n");
        }
        return sb.toString();
    }

    /**
     * 格式化显示英雄详情
     * 包含：英雄名、类型、基础属性、兼容装备、拥有该英雄的玩家
     */
    public String displayHeroDetail(Hero hero) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Hero Detail ===\n");
        sb.append("ID: ").append(hero.getHeroId()).append("\n");
        sb.append("Name: ").append(hero.getName()).append("\n");
        sb.append("Type: ").append(hero.getHeroType()).append("\n");
        sb.append("Base Stats:\n");
        Map<String, Integer> stats = hero.getBaseStats();
        if (stats.isEmpty()) {
            sb.append("  (none)\n");
        } else {
            for (Map.Entry<String, Integer> entry : stats.entrySet()) {
                sb.append("  ").append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
            }
        }
        sb.append("Compatible Equipment:\n");
        List<Equipment> eqList = hero.getCompatibleEquipment();
        if (eqList.isEmpty()) {
            sb.append("  (none)\n");
        } else {
            for (Equipment eq : eqList) {
                sb.append("  - ").append(eq.getName()).append(" (").append(eq.getEquipmentType()).append(")\n");
            }
        }
        return sb.toString();
    }

    /**
     * 获取玩家或战队的最近比赛历史
     * @param playerOrTeamId 玩家 ID 或战队 ID
     * @param n 最近 N 场（0 表示全部）
     * @return 匹配的比赛记录列表
     */
    public List<MatchRecord> getMatchHistory(String playerOrTeamId, int n) {
        List<MatchRecord> allMatches = dataManager.getAllMatchRecords();

        // Try as player first
        Player player = dataManager.getPlayerById(playerOrTeamId);
        String teamId = (player != null && player.getTeamId() != null)
                ? player.getTeamId() : playerOrTeamId;

        List<MatchRecord> filtered = allMatches.stream()
                .filter(m -> m.getTeamA().equals(teamId) || m.getTeamB().equals(teamId))
                .collect(Collectors.toList());

        if (n > 0 && filtered.size() > n) {
            filtered = filtered.subList(filtered.size() - n, filtered.size());
        }
        return filtered;
    }

    /**
     * 格式化显示比赛历史
     * 包含：对手、日期、结果
     */
    public String displayMatchHistory(List<MatchRecord> records) {
        if (records.isEmpty()) {
            return "No match records found.";
        }

        StringBuilder sb = new StringBuilder();
        int wins = 0, losses = 0, draws = 0;
        for (int i = 0; i < records.size(); i++) {
            MatchRecord m = records.get(i);
            sb.append(i + 1).append(". ").append(m.getInfo()).append("\n");
            switch (m.getResult()) {
                case WIN  -> wins++;
                case LOSE -> losses++;
                case DRAW -> draws++;
            }
        }
        sb.append("\nSummary: ").append(wins).append("W / ").append(losses).append("L / ").append(draws).append("D");
        if (wins + losses > 0) {
            sb.append(" (Win Rate: ").append(String.format("%.1f%%", (double) wins / (wins + losses) * 100)).append(")");
        }
        sb.append("\n");
        return sb.toString();
    }
}
