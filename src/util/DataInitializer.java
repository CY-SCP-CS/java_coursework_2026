package util;

import model.*;
import service.GameDataManager;

/**
 * 数据初始化器
 * 创建系统所需的初始数据集（硬编码）
 */
public class DataInitializer {

    /**
     * 初始化系统数据
     * @return 填充好初始数据的 GameDataManager
     */
    public static GameDataManager initData() {
        GameDataManager data = new GameDataManager();

        // 1. 创建装备（20 件）
        createEquipment(data);

        // 2. 创建英雄（15 个）
        createHeroes(data);

        // 3. 创建管理员和玩家
        createUsers(data);

        // 4. 创建战队（3 支）
        createTeams(data);

        // 5. 创建比赛记录（10 条）
        createMatchRecords(data);

        return data;
    }

    private static void createEquipment(GameDataManager data) {
        // 将在后续实现中完善
    }

    private static void createHeroes(GameDataManager data) {
        // 将在后续实现中完善
    }

    private static void createUsers(GameDataManager data) {
        // 将在后续实现中完善
    }

    private static void createTeams(GameDataManager data) {
        // 将在后续实现中完善
    }

    private static void createMatchRecords(GameDataManager data) {
        // 将在后续实现中完善
    }
}
