package util;

import model.*;
import service.GameDataManager;

import java.time.LocalDate;

/**
 * 数据初始化器
 * 创建系统所需的初始数据集（硬编码）
 *
 * 初始化内容：
 * - 20 件装备（覆盖所有 EquipmentType）
 * - 15 个英雄（覆盖所有 HeroType）
 * - 15 名玩家（每人至少 3 个英雄，含已装备物品）
 * - 3 支战队（每队 5 人）
 * - 10 条比赛记录
 * - 3 个默认账号（1 Admin + 2 Player）
 */
public class DataInitializer {

    /**
     * 初始化系统数据
     * @return 填充好初始数据的 GameDataManager
     */
    public static GameDataManager initData() {
        GameDataManager data = new GameDataManager();

        createEquipment(data);
        createHeroes(data);
        createUsers(data);
        createTeams(data);
        createMatchRecords(data);

        return data;
    }

    // ========================================================================
    //  1. 装备 (20 件)
    // ========================================================================

    private static void createEquipment(GameDataManager data) {
        // --- OFFENSIVE (6) ---
        addEquip(data, "EQ001", "无尽战刃",   EquipmentType.OFFENSIVE, "attack", 130, "crit_rate", 25);
        addEquip(data, "EQ002", "破军",       EquipmentType.OFFENSIVE, "attack", 180);
        addEquip(data, "EQ003", "宗师之力",   EquipmentType.OFFENSIVE, "attack", 80, "crit_rate", 20);
        addEquip(data, "EQ004", "碎星锤",     EquipmentType.OFFENSIVE, "attack", 80, "cd_reduction", 10);
        addEquip(data, "EQ005", "泣血之刃",   EquipmentType.OFFENSIVE, "attack", 100, "lifesteal", 25);
        addEquip(data, "EQ006", "暗影战斧",   EquipmentType.OFFENSIVE, "attack", 85, "cd_reduction", 15, "hp", 500);

        // --- DEFENSIVE (5) ---
        addEquip(data, "EQ007", "不祥征兆",   EquipmentType.DEFENSIVE, "defense", 270, "hp", 1200);
        addEquip(data, "EQ008", "魔女斗篷",   EquipmentType.DEFENSIVE, "magic_defense", 360, "hp", 1000);
        addEquip(data, "EQ009", "极寒风暴",   EquipmentType.DEFENSIVE, "defense", 360, "cd_reduction", 20);
        addEquip(data, "EQ010", "红莲斗篷",   EquipmentType.DEFENSIVE, "defense", 240, "hp", 1000);
        addEquip(data, "EQ011", "霸者重装",   EquipmentType.DEFENSIVE, "hp", 2000);

        // --- MOVEMENT (3) ---
        addEquip(data, "EQ012", "抵抗之靴",   EquipmentType.MOVEMENT,  "defense", 110, "magic_defense", 110);
        addEquip(data, "EQ013", "影忍之足",   EquipmentType.MOVEMENT,  "defense", 110, "block", 15);
        addEquip(data, "EQ014", "急速战靴",   EquipmentType.MOVEMENT,  "attack_speed", 30);

        // --- MAGIC (4) ---
        addEquip(data, "EQ015", "回响之杖",   EquipmentType.MAGIC,     "magic_attack", 240, "move_speed", 7);
        addEquip(data, "EQ016", "博学者之怒", EquipmentType.MAGIC,     "magic_attack", 400);
        addEquip(data, "EQ017", "虚无法杖",   EquipmentType.MAGIC,     "magic_attack", 240, "magic_pierce", 45);
        addEquip(data, "EQ018", "噬神之书",   EquipmentType.MAGIC,     "magic_attack", 180, "hp", 800, "spell_vamp", 25);

        // --- JUNGLE (2) ---
        addEquip(data, "EQ019", "贪婪之噬",   EquipmentType.JUNGLE,    "attack", 60, "cd_reduction", 8);
        addEquip(data, "EQ020", "符文大剑",   EquipmentType.JUNGLE,    "magic_attack", 120, "hp", 600);
    }

    private static void addEquip(GameDataManager data, String id, String name,
                                  EquipmentType type, Object... stats) {
        Equipment eq = new Equipment(id, name, type);
        for (int i = 0; i < stats.length; i += 2) {
            eq.addStat((String) stats[i], (Integer) stats[i + 1]);
        }
        data.addEquipment(eq);
    }

    // ========================================================================
    //  2. 英雄 (15 个) + 装备兼容性
    // ========================================================================

    private static void createHeroes(GameDataManager data) {
        // --- WARRIOR (3) ---
        addHero(data, "H001", "铠",     HeroType.WARRIOR,  3500, 200, 200,
                "EQ001", "EQ002", "EQ003", "EQ006", "EQ012", "EQ013");
        addHero(data, "H002", "吕布",   HeroType.WARRIOR,  3800, 220, 180,
                "EQ001", "EQ002", "EQ004", "EQ011", "EQ013");
        addHero(data, "H003", "赵云",   HeroType.WARRIOR,  3600, 190, 160,
                "EQ001", "EQ004", "EQ006", "EQ012", "EQ019");

        // --- MAGE (3) ---
        addHero(data, "H004", "诸葛亮", HeroType.MAGE,      3000, 150, 100,
                "EQ015", "EQ016", "EQ017", "EQ018", "EQ012");
        addHero(data, "H005", "安琪拉", HeroType.MAGE,      2800, 140, 80,
                "EQ015", "EQ016", "EQ017", "EQ018");
        addHero(data, "H006", "貂蝉",   HeroType.MAGE,      2900, 160, 90,
                "EQ015", "EQ016", "EQ018", "EQ012", "EQ009");

        // --- ASSASSIN (2) ---
        addHero(data, "H007", "李白",   HeroType.ASSASSIN,  3100, 210, 120,
                "EQ001", "EQ002", "EQ005", "EQ006", "EQ014", "EQ019");
        addHero(data, "H008", "兰陵王", HeroType.ASSASSIN,  3000, 200, 110,
                "EQ001", "EQ004", "EQ005", "EQ006", "EQ019");

        // --- TANK (2) ---
        addHero(data, "H009", "程咬金", HeroType.TANK,      4200, 170, 300,
                "EQ007", "EQ008", "EQ009", "EQ010", "EQ011", "EQ013");
        addHero(data, "H010", "廉颇",   HeroType.TANK,      4500, 160, 350,
                "EQ007", "EQ008", "EQ009", "EQ010", "EQ011", "EQ013");

        // --- MARKSMAN (2) ---
        addHero(data, "H011", "鲁班七号", HeroType.MARKSMAN, 2800, 190, 80,
                "EQ001", "EQ003", "EQ005", "EQ014");
        addHero(data, "H012", "后羿",   HeroType.MARKSMAN,  2900, 185, 85,
                "EQ001", "EQ003", "EQ005", "EQ014");

        // --- SUPPORT (2) ---
        addHero(data, "H013", "蔡文姬", HeroType.SUPPORT,   2700, 130, 70,
                "EQ008", "EQ012", "EQ015", "EQ018");
        addHero(data, "H014", "瑶",     HeroType.SUPPORT,   2600, 120, 60,
                "EQ008", "EQ012", "EQ015", "EQ018");

        // --- JUNGLER (1) ---
        addHero(data, "H015", "韩信",   HeroType.JUNGLER,   3300, 195, 130,
                "EQ001", "EQ005", "EQ006", "EQ014", "EQ019");
    }

    private static void addHero(GameDataManager data, String id, String name,
                                HeroType type, int hp, int attack, int defense,
                                String... compatibleEqIds) {
        Hero hero = new Hero(id, name, type);
        hero.addStat("hp", hp);
        hero.addStat("attack", attack);
        hero.addStat("defense", defense);
        // 添加兼容装备引用
        for (String eqId : compatibleEqIds) {
            Equipment eq = data.getEquipmentById(eqId);
            if (eq != null) {
                hero.addEquipment(eq);
            }
        }
        data.addHero(hero);
    }

    // ========================================================================
    //  3. 用户 (1 Admin + 15 名玩家)
    // ========================================================================

    private static void createUsers(GameDataManager data) {
        // Admin
        Admin admin = new Admin("admin", "System Admin", 1);
        data.addAdmin(admin, "admin123");

        // 玩家英雄分配：每人至少 3 个英雄
        // P001 梦之泪伤 — 李白、韩信、诸葛亮
        createPlayer(data, "P001", "梦之泪伤", "T001", 28, 65.5,
                new String[]{"H007", "H015", "H004"},
                new String[][]{{"EQ001", "EQ005", "EQ014"}, {"EQ001", "EQ019", "EQ014"}, {"EQ015", "EQ016", "EQ012"}});

        // P002 月色如歌 — 貂蝉、铠、后羿
        createPlayer(data, "P002", "月色如歌", "T001", 25, 58.3,
                new String[]{"H006", "H001", "H012"},
                new String[][]{{"EQ015", "EQ016", "EQ018"}, {"EQ001", "EQ002", "EQ013"}, {"EQ001", "EQ005", "EQ014"}});

        // P003 剑如虹 — 赵云、吕布、鲁班七号
        createPlayer(data, "P003", "剑如虹", "T001", 22, 52.1,
                new String[]{"H003", "H002", "H011"},
                new String[][]{{"EQ006", "EQ012"}, {"EQ001", "EQ002", "EQ013"}, {"EQ001", "EQ003", "EQ014"}});

        // P004 影之舞 — 兰陵王、李白、安琪拉
        createPlayer(data, "P004", "影之舞", "T001", 20, 48.7,
                new String[]{"H008", "H007", "H005"},
                new String[][]{{"EQ001", "EQ004"}, {"EQ002", "EQ005"}, {"EQ015", "EQ016", "EQ017"}});

        // P005 星光璀璨 — 程咬金、蔡文姬、廉颇
        createPlayer(data, "P005", "星光璀璨", "T001", 26, 61.2,
                new String[]{"H009", "H013", "H010"},
                new String[][]{{"EQ007", "EQ011"}, {"EQ008", "EQ012"}, {"EQ007", "EQ008", "EQ010"}});

        // P006 不败战神 — 吕布、李白、程咬金、诸葛亮
        createPlayer(data, "P006", "不败战神", "T002", 30, 72.8,
                new String[]{"H002", "H007", "H009", "H004"},
                new String[][]{{"EQ001", "EQ002"}, {"EQ001", "EQ005"}, {"EQ007", "EQ011"}, {"EQ015", "EQ016"}});

        // P007 月下独酌 — 赵云、后羿、瑶
        createPlayer(data, "P007", "月下独酌", "T002", 24, 55.6,
                new String[]{"H003", "H012", "H014"},
                new String[][]{{"EQ001", "EQ006"}, {"EQ001", "EQ003"}, {"EQ008", "EQ012"}});

        // P008 风之痕 — 铠、兰陵王、鲁班七号
        createPlayer(data, "P008", "风之痕", "T002", 21, 50.2,
                new String[]{"H001", "H008", "H011"},
                new String[][]{{"EQ001", "EQ003"}, {"EQ001", "EQ005"}, {"EQ001", "EQ014"}});

        // P009 九天揽月 — 韩信、阿轲、安琪拉
        createPlayer(data, "P009", "九天揽月", "T002", 27, 63.4,
                new String[]{"H015", "H007", "H005"},
                new String[][]{{"EQ001", "EQ019"}, {"EQ002", "EQ005"}, {"EQ016", "EQ017"}});

        // P010 暖阳 — 貂蝉、蔡文姬、后羿、廉颇
        createPlayer(data, "P010", "暖阳", "T002", 23, 54.0,
                new String[]{"H006", "H013", "H012", "H010"},
                new String[][]{{"EQ015", "EQ018"}, {"EQ008", "EQ015"}, {"EQ003", "EQ005"}, {"EQ007", "EQ010"}});

        // P011 清风 — 赵云、鲁班七号、兰陵王
        createPlayer(data, "P011", "清风", "T003", 19, 45.3,
                new String[]{"H003", "H011", "H008"},
                new String[][]{{"EQ001", "EQ006"}, {"EQ001", "EQ005"}, {"EQ004", "EQ006"}});

        // P012 明月 — 铠、后羿、貂蝉
        createPlayer(data, "P012", "明月", "T003", 22, 51.8,
                new String[]{"H001", "H012", "H006"},
                new String[][]{{"EQ002", "EQ013"}, {"EQ001", "EQ003"}, {"EQ015", "EQ016"}});

        // P013 孤影 — 安琪拉、瑶、阿轲
        createPlayer(data, "P013", "孤影", "T003", 18, 42.5,
                new String[]{"H005", "H014", "H007"},
                new String[][]{{"EQ015", "EQ017"}, {"EQ008", "EQ018"}, {"EQ001", "EQ005"}});

        // P014 浅梦 — 蔡文姬、廉颇、韩信
        createPlayer(data, "P014", "浅梦", "T003", 20, 47.0,
                new String[]{"H013", "H010", "H015"},
                new String[][]{{"EQ008", "EQ012"}, {"EQ009", "EQ011"}, {"EQ005", "EQ019"}});

        // P015 听风 — 程咬金、诸葛亮、铠
        createPlayer(data, "P015", "听风", "T003", 21, 49.6,
                new String[]{"H009", "H004", "H001"},
                new String[][]{{"EQ007", "EQ011"}, {"EQ016", "EQ017"}, {"EQ002", "EQ006"}});
    }

    private static void createPlayer(GameDataManager data, String id, String name,
                                     String teamId, int level, double winRate,
                                     String[] heroIds, String[][] equipmentIds) {
        Player player = new Player(id, name, teamId, level, winRate);
        for (int i = 0; i < heroIds.length; i++) {
            Hero hero = data.getHeroById(heroIds[i]);
            if (hero != null) {
                player.addHero(hero);
                // 为该英雄装备物品
                if (i < equipmentIds.length && equipmentIds[i] != null) {
                    for (String eqId : equipmentIds[i]) {
                        player.equipItem(hero.getHeroId(), eqId);
                    }
                }
            }
        }
        data.addPlayer(player, "pass123");
    }

    // ========================================================================
    //  4. 战队 (3 支，每队 5 人)
    // ========================================================================

    private static void createTeams(GameDataManager data) {
        addTeam(data, "T001", "星辰战队", "P001", "P002", "P003", "P004", "P005");
        addTeam(data, "T002", "雷霆战队", "P006", "P007", "P008", "P009", "P010");
        addTeam(data, "T003", "明月战队", "P011", "P012", "P013", "P014", "P015");
    }

    private static void addTeam(GameDataManager data, String id, String name,
                                String... playerIds) {
        Team team = new Team(id, name);
        for (String pid : playerIds) {
            Player player = data.getPlayerById(pid);
            if (player != null) {
                team.addMember(player);
            }
        }
        data.addTeam(team);
    }

    // ========================================================================
    //  5. 比赛记录 (10 条)
    // ========================================================================

    private static void createMatchRecords(GameDataManager data) {
        addMatch(data, "M001", "2026-05-01", "T001", "T002", MatchResult.WIN,
                "H007", "H015", "H004", "H001", "H006");
        addMatch(data, "M002", "2026-05-03", "T002", "T003", MatchResult.LOSE,
                "H002", "H007", "H001", "H003", "H012");
        addMatch(data, "M003", "2026-05-05", "T001", "T003", MatchResult.WIN,
                "H009", "H013", "H011", "H004", "H007");
        addMatch(data, "M004", "2026-05-08", "T002", "T001", MatchResult.WIN,
                "H002", "H009", "H015", "H005", "H012");
        addMatch(data, "M005", "2026-05-10", "T003", "T002", MatchResult.LOSE,
                "H003", "H011", "H005", "H001", "H006");
        addMatch(data, "M006", "2026-05-12", "T001", "T002", MatchResult.LOSE,
                "H001", "H007", "H009", "H015", "H004");
        addMatch(data, "M007", "2026-05-15", "T003", "T001", MatchResult.DRAW,
                "H010", "H008", "H006", "H013", "H005");
        addMatch(data, "M008", "2026-05-18", "T002", "T003", MatchResult.WIN,
                "H002", "H007", "H009", "H004", "H012");
        addMatch(data, "M009", "2026-05-20", "T001", "T003", MatchResult.WIN,
                "H007", "H015", "H009", "H013", "H011");
        addMatch(data, "M010", "2026-05-22", "T002", "T001", MatchResult.LOSE,
                "H001", "H002", "H015", "H012", "H005");
    }

    private static void addMatch(GameDataManager data, String id, String dateStr,
                                 String teamA, String teamB, MatchResult result,
                                 String... heroPickIds) {
        MatchRecord match = new MatchRecord(id, LocalDate.parse(dateStr), teamA, teamB, result);
        for (String hid : heroPickIds) {
            match.addHeroPick(hid);
        }
        data.addMatchRecord(match);
    }
}
