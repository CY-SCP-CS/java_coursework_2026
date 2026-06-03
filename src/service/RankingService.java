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
     * 按装备使用次数排名
     */
    public List<Equipment> getEquipmentRankingByUsage() {
        return dataManager.getAllEquipment().stream()
                .sorted(Comparator.comparing(Equipment::getUsageCount).reversed())
                .collect(Collectors.toList());
    }
}
