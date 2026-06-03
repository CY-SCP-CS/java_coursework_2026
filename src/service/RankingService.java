package service;

import model.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 排行榜服务
 * 提供玩家排名和装备统计功能
 */
public class RankingService {
    private GameDataManager dataManager;

    public RankingService(GameDataManager dataManager) {
        this.dataManager = dataManager;
    }

    /**
     * 按胜率排名
     * 平局处理：胜率相同按等级降序
     */
    public List<Player> getLeaderboardByWinRate(int topN) {
        return dataManager.getAllPlayers().stream()
                .sorted(Comparator.comparing(Player::getWinRate)
                        .reversed()
                        .thenComparing(Comparator.comparing(Player::getLevel).reversed()))
                .limit(topN)
                .collect(Collectors.toList());
    }

    /**
     * 按等级排名
     * 平局处理：等级相同按胜率降序
     */
    public List<Player> getLeaderboardByLevel(int topN) {
        return dataManager.getAllPlayers().stream()
                .sorted(Comparator.comparing(Player::getLevel)
                        .reversed()
                        .thenComparing(Comparator.comparing(Player::getWinRate).reversed()))
                .limit(topN)
                .collect(Collectors.toList());
    }

    /**
     * 按比赛场次排名
     * 平局处理：场次相同按胜率降序
     */
    public List<Player> getLeaderboardByMatches(int topN) {
        return dataManager.getAllPlayers().stream()
                .sorted(Comparator.<Player, Integer>comparing(
                                p -> getPlayerMatchCount(p.getId()),
                                Comparator.reverseOrder())
                        .thenComparing(Comparator.comparing(Player::getWinRate).reversed()))
                .limit(topN)
                .collect(Collectors.toList());
    }

    /**
     * 按装备使用次数排名
     */
    public List<Equipment> getEquipmentRankingByUsage() {
        return dataManager.getAllEquipment().stream()
                .sorted(Comparator.comparing(Equipment::getUsageCount).reversed())
                .collect(Collectors.toList());
    }

    /**
     * 按使用该装备的英雄数量降序排列
     */
    public List<Equipment> getEquipmentRankingByHeroCount() {
        // Build hero count map for each equipment
        Map<String, Integer> heroCountMap = new HashMap<>();
        for (Hero hero : dataManager.getAllHeroes()) {
            for (Equipment eq : hero.getCompatibleEquipment()) {
                heroCountMap.merge(eq.getEquipmentId(), 1, Integer::sum);
            }
        }
        return getEquipmentRankingByHeroCount(heroCountMap);
    }

    /**
     * 按使用该装备的英雄数量降序排列（使用外部 Map）
     */
    public List<Equipment> getEquipmentRankingByHeroCount(Map<String, Integer> heroCountMap) {
        return dataManager.getAllEquipment().stream()
                .sorted(Comparator.<Equipment, Integer>comparing(
                                eq -> heroCountMap.getOrDefault(eq.getEquipmentId(), 0),
                                Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }

    /**
     * 自定义综合评分
     * 公式：customScore = winRate * 0.5 + level * 2.0 + matchesCount * 0.1
     */
    public double getCustomScore(Player player) {
        int matchesCount = getPlayerMatchCount(player.getId());
        return player.getWinRate() * 0.5 + player.getLevel() * 2.0 + matchesCount * 0.1;
    }

    /**
     * 按自定义评分排名
     */
    public List<Player> getLeaderboardByCustomScore(int topN) {
        return dataManager.getAllPlayers().stream()
                .sorted(Comparator.comparing(this::getCustomScore)
                        .reversed()
                        .thenComparing(Comparator.comparing(Player::getWinRate).reversed()))
                .limit(topN)
                .collect(Collectors.toList());
    }

    /**
     * 统计玩家参与的比赛场次
     */
    public int getPlayerMatchCount(String playerId) {
        Player player = dataManager.getPlayerById(playerId);
        if (player == null || player.getTeamId() == null) return 0;

        String teamId = player.getTeamId();
        return (int) dataManager.getAllMatchRecords().stream()
                .filter(m -> m.getTeamA().equals(teamId) || m.getTeamB().equals(teamId))
                .count();
    }
}
