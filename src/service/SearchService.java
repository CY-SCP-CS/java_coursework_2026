package service;

import model.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 搜索服务
 * 提供玩家、战队、英雄的查询功能
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
}
