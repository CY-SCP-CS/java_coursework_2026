package service;

import model.*;
import util.GameLogger;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.*;

/**
 * 文件存储服务
 * 负责从 CSV 文件保存和加载系统数据
 *
 * 文件清单（均在 data/ 目录下）：
 * - equipment.csv    装备数据
 * - heroes.csv       英雄数据
 * - players.csv      玩家数据
 * - teams.csv        战队数据
 * - matches.csv      比赛记录
 * - passwords.csv    账号密码
 * - equipped_items.csv 玩家英雄装备映射
 */
public class FileStorageService {
    private static final String DATA_DIR = "data";
    private static final String EQUIPMENT_FILE = DATA_DIR + "/equipment.csv";
    private static final String HEROES_FILE = DATA_DIR + "/heroes.csv";
    private static final String PLAYERS_FILE = DATA_DIR + "/players.csv";
    private static final String TEAMS_FILE = DATA_DIR + "/teams.csv";
    private static final String MATCHES_FILE = DATA_DIR + "/matches.csv";
    private static final String PASSWORDS_FILE = DATA_DIR + "/passwords.csv";
    private static final String EQUIPPED_ITEMS_FILE = DATA_DIR + "/equipped_items.csv";

    public FileStorageService() {
    }

    // ========================================================================
    //  Save All
    // ========================================================================

    /**
     * 保存所有数据到 CSV 文件
     */
    public void saveAllData(GameDataManager data) {
        ensureDataDir();
        saveEquipment(data.getAllEquipment());
        saveHeroes(data.getAllHeroes());
        savePlayers(data.getAllPlayers());
        saveTeams(data.getAllTeams());
        saveMatchRecords(data.getAllMatchRecords());
        savePasswords(data);
        saveEquippedItems(data.getAllPlayers());
        GameLogger.info("FileStorage", "All data saved to " + DATA_DIR + "/");
    }

    /**
     * 从 CSV 文件加载所有数据
     * @return 填充好数据的 GameDataManager，文件不存在时返回空的 manager
     */
    public GameDataManager loadAllData() {
        GameDataManager data = new GameDataManager();

        // 1. Load equipment (no dependencies)
        List<Equipment> equipmentList = loadEquipment();
        Map<String, Equipment> equipMap = new HashMap<>();
        for (Equipment eq : equipmentList) {
            data.addEquipment(eq);
            equipMap.put(eq.getEquipmentId(), eq);
        }

        // 2. Load heroes (depends on equipment)
        List<String[]> heroLines = loadRawLines(HEROES_FILE);
        for (String[] parts : heroLines) {
            if (parts.length < 5) continue;
            Hero hero = new Hero(parts[0], parts[1], HeroType.valueOf(parts[2]));
            // Parse stats: hp:3000|attack:200
            if (!parts[3].isEmpty()) {
                for (String pair : parts[3].split("\\|")) {
                    String[] kv = pair.split(":", 2);
                    if (kv.length == 2) {
                        try {
                            hero.addStat(kv[0], Integer.parseInt(kv[1]));
                        } catch (NumberFormatException e) {
                            GameLogger.warn("FileStorage", "Skip hero stat: " + pair);
                        }
                    }
                }
            }
            // Parse compatible equipment IDs
            if (!parts[4].isEmpty()) {
                for (String eqId : parts[4].split("\\|")) {
                    Equipment eq = equipMap.get(eqId);
                    if (eq != null) {
                        hero.addEquipment(eq);
                    }
                }
            }
            data.addHero(hero);
        }

        // 3. Load passwords (no dependencies)
        List<String[]> pwdLines = loadRawLines(PASSWORDS_FILE);
        Map<String, String> pwdMap = new HashMap<>();
        for (String[] parts : pwdLines) {
            if (parts.length >= 2) {
                pwdMap.put(parts[0], parts[1]);
            }
        }

        // 4. Load players (depends on heroes)
        List<String[]> playerLines = loadRawLines(PLAYERS_FILE);
        Map<String, Player> playerMap = new HashMap<>();
        for (String[] parts : playerLines) {
            if (parts.length < 6) continue;
            String pid = parts[0];
            String name = parts[1];
            String teamId = parts[2].isEmpty() ? null : parts[2];
            int level = parseIntSafe(parts[3], 1);
            double winRate = parseDoubleSafe(parts[4], 50.0);
            Player player = new Player(pid, name, teamId, level, winRate);
            // Parse owned hero IDs
            if (!parts[5].isEmpty()) {
                for (String hid : parts[5].split("\\|")) {
                    Hero hero = data.getHeroById(hid);
                    if (hero != null) {
                        player.addHero(hero);
                    }
                }
            }
            String password = pwdMap.getOrDefault(pid, "pass123");
            data.addPlayer(player, password);
            playerMap.put(pid, player);
        }

        // 5. Load equipped items (depends on players)
        List<String[]> equipItemLines = loadRawLines(EQUIPPED_ITEMS_FILE);
        for (String[] parts : equipItemLines) {
            if (parts.length < 3) continue;
            Player player = playerMap.get(parts[0]);
            if (player != null) {
                String heroId = parts[1];
                if (!parts[2].isEmpty()) {
                    for (String eqId : parts[2].split("\\|")) {
                        player.equipItem(heroId, eqId);
                    }
                }
            }
        }

        // 6. Load teams (depends on players)
        List<String[]> teamLines = loadRawLines(TEAMS_FILE);
        for (String[] parts : teamLines) {
            if (parts.length < 3) continue;
            Team team = new Team(parts[0], parts[1]);
            if (!parts[2].isEmpty()) {
                for (String pid : parts[2].split("\\|")) {
                    Player player = playerMap.get(pid);
                    if (player != null) {
                        team.addMember(player);
                    }
                }
            }
            data.addTeam(team);
        }

        // 7. Load match records (no object dependencies)
        List<String[]> matchLines = loadRawLines(MATCHES_FILE);
        for (String[] parts : matchLines) {
            if (parts.length < 6) continue;
            try {
                MatchRecord match = new MatchRecord(
                        parts[0],
                        LocalDate.parse(parts[1]),
                        parts[2],
                        parts[3],
                        MatchResult.valueOf(parts[4]));
                if (!parts[5].isEmpty()) {
                    for (String hid : parts[5].split("\\|")) {
                        match.addHeroPick(hid);
                    }
                }
                data.addMatchRecord(match);
            } catch (Exception e) {
                GameLogger.warn("FileStorage", "Skip match: " + parts[0] + " - " + e.getMessage());
            }
        }

        GameLogger.info("FileStorage", "All data loaded from " + DATA_DIR + "/");
        return data;
    }

    // ========================================================================
    //  Equipment Save/Load
    // ========================================================================

    public void saveEquipment(Collection<Equipment> equipment) {
        try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(
                new FileOutputStream(EQUIPMENT_FILE), StandardCharsets.UTF_8), true)) {
            for (Equipment eq : equipment) {
                pw.println(eq.toCSVString());
            }
        } catch (IOException e) {
            GameLogger.error("FileStorage", "Failed to save equipment", e);
        }
    }

    public List<Equipment> loadEquipment() {
        List<Equipment> list = new ArrayList<>();
        List<String[]> lines = loadRawLines(EQUIPMENT_FILE);
        for (String[] parts : lines) {
            if (parts.length < 5) continue;
            try {
                Equipment eq = new Equipment(parts[0], parts[1], EquipmentType.valueOf(parts[2]));
                // Parse stats
                if (!parts[3].isEmpty()) {
                    for (String pair : parts[3].split("\\|")) {
                        String[] kv = pair.split(":", 2);
                        if (kv.length == 2) {
                            eq.addStat(kv[0], Integer.parseInt(kv[1]));
                        }
                    }
                }
                eq.setUsageCount(parseIntSafe(parts[4], 0));
                list.add(eq);
            } catch (Exception e) {
                GameLogger.warn("FileStorage", "Skip equipment line: " + e.getMessage());
            }
        }
        return list;
    }

    // ========================================================================
    //  Heroes Save/Load
    // ========================================================================

    public void saveHeroes(Collection<Hero> heroes) {
        try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(
                new FileOutputStream(HEROES_FILE), StandardCharsets.UTF_8), true)) {
            for (Hero hero : heroes) {
                pw.println(hero.toCSVString());
            }
        } catch (IOException e) {
            GameLogger.error("FileStorage", "Failed to save heroes", e);
        }
    }

    // ========================================================================
    //  Players Save/Load
    // ========================================================================

    public void savePlayers(Collection<Player> players) {
        try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(
                new FileOutputStream(PLAYERS_FILE), StandardCharsets.UTF_8), true)) {
            for (Player player : players) {
                pw.println(player.toCSVString());
            }
        } catch (IOException e) {
            GameLogger.error("FileStorage", "Failed to save players", e);
        }
    }

    // ========================================================================
    //  Teams Save/Load
    // ========================================================================

    public void saveTeams(Collection<Team> teams) {
        try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(
                new FileOutputStream(TEAMS_FILE), StandardCharsets.UTF_8), true)) {
            for (Team team : teams) {
                pw.println(team.toCSVString());
            }
        } catch (IOException e) {
            GameLogger.error("FileStorage", "Failed to save teams", e);
        }
    }

    // ========================================================================
    //  MatchRecords Save/Load
    // ========================================================================

    public void saveMatchRecords(List<MatchRecord> records) {
        try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(
                new FileOutputStream(MATCHES_FILE), StandardCharsets.UTF_8), true)) {
            for (MatchRecord record : records) {
                pw.println(record.toCSVString());
            }
        } catch (IOException e) {
            GameLogger.error("FileStorage", "Failed to save match records", e);
        }
    }

    // ========================================================================
    //  Passwords Save/Load
    // ========================================================================

    public void savePasswords(GameDataManager data) {
        try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(
                new FileOutputStream(PASSWORDS_FILE), StandardCharsets.UTF_8), true)) {
            // Collect all user IDs and passwords
            Set<String> allIds = new HashSet<>();
            for (Player p : data.getAllPlayers()) allIds.add(p.getId());
            for (Admin a : Collections.singletonList(data.getAdminById("admin"))) {
                if (a != null) allIds.add(a.getId());
            }
            for (String id : allIds) {
                String pwd = data.getPassword(id);
                if (pwd != null) {
                    pw.println(id + "," + pwd);
                }
            }
        } catch (IOException e) {
            GameLogger.error("FileStorage", "Failed to save passwords", e);
        }
    }

    // ========================================================================
    //  Equipped Items Save/Load
    // ========================================================================

    public void saveEquippedItems(Collection<Player> players) {
        try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(
                new FileOutputStream(EQUIPPED_ITEMS_FILE), StandardCharsets.UTF_8), true)) {
            for (Player player : players) {
                Map<String, List<String>> allEquipped = player.getAllEquippedItems();
                for (Map.Entry<String, List<String>> entry : allEquipped.entrySet()) {
                    String heroId = entry.getKey();
                    String eqIds = String.join("|", entry.getValue());
                    pw.println(player.getId() + "," + heroId + "," + eqIds);
                }
            }
        } catch (IOException e) {
            GameLogger.error("FileStorage", "Failed to save equipped items", e);
        }
    }

    // ========================================================================
    //  Internal Helpers
    // ========================================================================

    /** 确保数据目录存在 */
    private void ensureDataDir() {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
            GameLogger.info("FileStorage", "Created data directory: " + DATA_DIR);
        }
    }

    /** 读取 CSV 文件，每行按逗号分割，跳过空行 */
    private List<String[]> loadRawLines(String filePath) {
        List<String[]> result = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) {
            GameLogger.warn("FileStorage", "File not found: " + filePath + " — skipping");
            return result;
        }
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                new FileInputStream(file), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                // Split on comma, but handle the case where name might contain commas
                // For this project, names don't contain commas so simple split works
                String[] parts = line.split(",", -1); // -1 to keep empty trailing fields
                result.add(parts);
            }
        } catch (IOException e) {
            GameLogger.error("FileStorage", "Failed to read file: " + filePath, e);
        }
        return result;
    }

    private int parseIntSafe(String s, int defaultValue) {
        try {
            return Integer.parseInt(s.trim());
        } catch (Exception e) {
            return defaultValue;
        }
    }

    private double parseDoubleSafe(String s, double defaultValue) {
        try {
            return Double.parseDouble(s.trim());
        } catch (Exception e) {
            return defaultValue;
        }
    }
}
