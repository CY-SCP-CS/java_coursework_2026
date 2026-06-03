import model.*;
import service.*;
import util.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Honor of Kings Information Management System
 * 主入口类 — 菜单驱动控制台界面
 */
public class Main {
    private static GameDataManager dataManager;
    private static AuthenticationService authService;
    private static SearchService searchService;
    private static RankingService rankingService;

    public static void main(String[] args) {
        dataManager = DataInitializer.initData();
        authService = new AuthenticationService(dataManager);
        searchService = new SearchService(dataManager);
        rankingService = new RankingService(dataManager);

        System.out.println("=== Honor of Kings IMS ===");
        System.out.println("Welcome to the system!\n");
        showMainMenu();
    }

    // ========================================================================
    //  Main Menu
    // ========================================================================

    private static void showMainMenu() {
        while (true) {
            System.out.println("=== Main Menu ===");
            System.out.println("1. Login");
            System.out.println("2. Exit");
            int choice = InputHelper.readIntRange("Choose: ", 1, 2);

            switch (choice) {
                case 1 -> handleLogin();
                case 2 -> {
                    System.out.println("Goodbye!");
                    return;
                }
            }
        }
    }

    private static void handleLogin() {
        System.out.println("\n--- Login ---");
        String id = InputHelper.readString("User ID: ");
        String password = InputHelper.readString("Password: ");

        Person user = authService.login(id, password);
        if (user == null) {
            System.out.println("Login failed. Invalid ID or password.");
            InputHelper.pressEnterToContinue();
            return;
        }
        System.out.println("Welcome, " + user.getName() + " (" + user.getRole() + ")!");

        if (user.getRole() == Role.ADMIN) {
            showAdminMenu((Admin) user);
        } else {
            showPlayerMenu((Player) user);
        }
    }

    // ========================================================================
    //  Player Menu
    // ========================================================================

    private static void showPlayerMenu(Player player) {
        while (authService.isLoggedIn()) {
            System.out.println("\n=== Player Menu ===");
            System.out.println("1.  View My Profile");
            System.out.println("2.  View My Heroes");
            System.out.println("3.  View My Match History");
            System.out.println("4.  Search Player");
            System.out.println("5.  View Team");
            System.out.println("6.  View Hero Details");
            System.out.println("7.  Equipment Statistics");
            System.out.println("8.  Leaderboard");
            System.out.println("9.  Logout");
            int choice = InputHelper.readIntRange("Choose: ", 1, 9);

            switch (choice) {
                case 1 -> viewPlayerProfile(player);
                case 2 -> viewMyHeroes(player);
                case 3 -> viewMyMatchHistory(player);
                case 4 -> handlePlayerLookup();
                case 5 -> handleTeamOverview();
                case 6 -> handleHeroDetails();
                case 7 -> handleEquipmentStats();
                case 8 -> handleLeaderboard();
                case 9 -> {
                    authService.logout();
                    System.out.println("Logged out.");
                }
            }
        }
    }

    // ========================================================================
    //  Admin Menu
    // ========================================================================

    private static void showAdminMenu(Admin admin) {
        while (authService.isLoggedIn()) {
            System.out.println("\n=== Admin Menu ===");
            System.out.println("1.  Manage Players");
            System.out.println("2.  Manage Heroes");
            System.out.println("3.  Manage Equipment");
            System.out.println("4.  Manage Teams");
            System.out.println("5.  Manage Match Records");
            System.out.println("6.  View Player Lookup");
            System.out.println("7.  View Team Overview");
            System.out.println("8.  View Hero Details");
            System.out.println("9.  Equipment Statistics");
            System.out.println("10. Leaderboard");
            System.out.println("11. Logout");
            int choice = InputHelper.readIntRange("Choose: ", 1, 11);

            switch (choice) {
                case 1 -> adminManagePlayers();
                case 2 -> adminManageHeroes();
                case 3 -> adminManageEquipment();
                case 4 -> adminManageTeams();
                case 5 -> adminManageMatches();
                case 6 -> handlePlayerLookup();
                case 7 -> handleTeamOverview();
                case 8 -> handleHeroDetails();
                case 9 -> handleEquipmentStats();
                case 10 -> handleLeaderboard();
                case 11 -> {
                    authService.logout();
                    System.out.println("Logged out.");
                }
            }
        }
    }

    // ========================================================================
    //  Player Features
    // ========================================================================

    private static void viewPlayerProfile(Player player) {
        System.out.println("\n" + player.getInfo());
        InputHelper.pressEnterToContinue();
    }

    private static void viewMyHeroes(Player player) {
        System.out.println("\n=== My Heroes ===");
        List<Hero> heroes = player.getOwnedHeroes();
        if (heroes.isEmpty()) {
            System.out.println("You don't own any heroes.");
        } else {
            for (Hero hero : heroes) {
                System.out.println(hero.getInfo());
                List<String> equipped = player.getEquippedItems(hero.getHeroId());
                if (!equipped.isEmpty()) {
                    System.out.println("  Equipped Items:");
                    for (String eqId : equipped) {
                        Equipment eq = dataManager.getEquipmentById(eqId);
                        if (eq != null) {
                            System.out.println("    - " + eq.getName());
                        }
                    }
                }
                System.out.println();
            }
        }
        InputHelper.pressEnterToContinue();
    }

    private static void viewMyMatchHistory(Player player) {
        System.out.println("\n=== My Match History ===");
        List<MatchRecord> allMatches = dataManager.getAllMatchRecords();
        List<MatchRecord> myMatches = allMatches.stream()
                .filter(m -> playerBelongsToMatch(player, m))
                .collect(Collectors.toList());

        if (myMatches.isEmpty()) {
            System.out.println("No match records found for you.");
        } else {
            for (MatchRecord m : myMatches) {
                System.out.println(m.getInfo());
                System.out.println();
            }
        }
        InputHelper.pressEnterToContinue();
    }

    /** 判断玩家是否参与某场比赛（通过战队ID或英雄选择） */
    private static boolean playerBelongsToMatch(Player player, MatchRecord match) {
        if (player.getTeamId() == null) return false;
        return match.getTeamA().equals(player.getTeamId())
            || match.getTeamB().equals(player.getTeamId())
            || match.getHeroPicks().stream().anyMatch(
                   hid -> player.getOwnedHeroes().stream().anyMatch(h -> h.getHeroId().equals(hid)));
    }

    // ========================================================================
    //  Player Lookup (F1)
    // ========================================================================

    private static void handlePlayerLookup() {
        System.out.println("\n--- Player Lookup ---");
        System.out.println("1. Search by ID");
        System.out.println("2. Search by Name");
        int choice = InputHelper.readIntRange("Choose: ", 1, 2);

        if (choice == 1) {
            String id = InputHelper.readString("Player ID: ");
            Player player = searchService.searchPlayerById(id);
            if (player == null) {
                System.out.println("Player not found.");
            } else {
                System.out.println("\n" + player.getInfo());
            }
        } else {
            String name = InputHelper.readString("Player Name: ");
            List<Player> results = searchService.searchPlayerByName(name);
            if (results.isEmpty()) {
                System.out.println("No players found matching \"" + name + "\".");
            } else {
                for (Player p : results) {
                    System.out.println(p.getInfo());
                }
            }
        }
        InputHelper.pressEnterToContinue();
    }

    // ========================================================================
    //  Team Overview (F2)
    // ========================================================================

    private static void handleTeamOverview() {
        System.out.println("\n--- Team Overview ---");
        System.out.println("1. Search by ID");
        System.out.println("2. Search by Name");
        int choice = InputHelper.readIntRange("Choose: ", 1, 2);

        Team team = null;
        if (choice == 1) {
            String id = InputHelper.readString("Team ID: ");
            team = searchService.searchTeamById(id);
        } else {
            String name = InputHelper.readString("Team Name: ");
            team = searchService.searchTeamByName(name);
        }

        if (team == null) {
            System.out.println("Team not found.");
        } else {
            System.out.println("\n" + team.getInfo());
        }
        InputHelper.pressEnterToContinue();
    }

    // ========================================================================
    //  Hero Details (F3)
    // ========================================================================

    private static void handleHeroDetails() {
        System.out.println("\n--- Hero Details ---");
        String name = InputHelper.readString("Hero Name: ");
        List<Hero> results = searchService.searchHeroByName(name);

        if (results.isEmpty()) {
            System.out.println("No heroes found matching \"" + name + "\".");
        } else {
            for (Hero hero : results) {
                System.out.println(hero.getInfo());
                // Show which players own this hero
                System.out.println("Owned by players:");
                boolean found = false;
                for (Player p : dataManager.getAllPlayers()) {
                    if (p.getOwnedHeroes().stream().anyMatch(h -> h.getHeroId().equals(hero.getHeroId()))) {
                        System.out.println("  - " + p.getName() + " (ID: " + p.getId() + ")");
                        found = true;
                    }
                }
                if (!found) {
                    System.out.println("  (none)");
                }
                System.out.println();
            }
        }
        InputHelper.pressEnterToContinue();
    }

    // ========================================================================
    //  Equipment Statistics (F4)
    // ========================================================================

    private static void handleEquipmentStats() {
        System.out.println("\n--- Equipment Statistics ---");
        System.out.println("1. Rank by Usage Count");
        System.out.println("2. Rank by Hero Count");
        int choice = InputHelper.readIntRange("Choose: ", 1, 2);

        Map<String, Integer> heroCount = new HashMap<>();
        List<Equipment> ranking;
        if (choice == 1) {
            ranking = rankingService.getEquipmentRankingByUsage();
            System.out.println("\n=== Equipment Ranking by Usage ===");
        } else {
            // Calculate hero count for each equipment
            for (Hero hero : dataManager.getAllHeroes()) {
                for (Equipment eq : hero.getCompatibleEquipment()) {
                    heroCount.merge(eq.getEquipmentId(), 1, Integer::sum);
                }
            }
            ranking = dataManager.getAllEquipment().stream()
                    .sorted(Comparator.<Equipment, Integer>comparing(
                            eq -> heroCount.getOrDefault(eq.getEquipmentId(), 0),
                            Comparator.reverseOrder()))
                    .collect(Collectors.toList());
            System.out.println("\n=== Equipment Ranking by Compatible Hero Count ===");
        }

        System.out.printf("%-4s %-12s %-12s %s\n", "Rank", "ID", "Name", "Metric");
        System.out.println("----------------------------------------");
        for (int i = 0; i < ranking.size(); i++) {
            Equipment eq = ranking.get(i);
            int metric = (choice == 1) ? eq.getUsageCount()
                                       : heroCount.getOrDefault(eq.getEquipmentId(), 0);
            System.out.printf("%-4d %-12s %-12s %d\n", i + 1, eq.getEquipmentId(), eq.getName(), metric);
        }
        InputHelper.pressEnterToContinue();
    }

    // ========================================================================
    //  Match History (F5)
    // ========================================================================

    private static void handleMatchHistory() {
        System.out.println("\n--- Match History ---");
        System.out.println("1. For a Player");
        System.out.println("2. For a Team");
        int choice = InputHelper.readIntRange("Choose: ", 1, 2);

        String id = InputHelper.readString("ID: ");
        int n = InputHelper.readInt("Last N matches (enter 0 for all): ");

        List<MatchRecord> allMatches = dataManager.getAllMatchRecords();
        List<MatchRecord> filtered;

        if (choice == 1) {
            Player player = dataManager.getPlayerById(id);
            if (player == null) {
                System.out.println("Player not found.");
                InputHelper.pressEnterToContinue();
                return;
            }
            String teamId = player.getTeamId();
            filtered = allMatches.stream()
                    .filter(m -> m.getTeamA().equals(teamId) || m.getTeamB().equals(teamId))
                    .collect(Collectors.toList());
        } else {
            filtered = allMatches.stream()
                    .filter(m -> m.getTeamA().equals(id) || m.getTeamB().equals(id))
                    .collect(Collectors.toList());
        }

        if (n > 0 && filtered.size() > n) {
            filtered = filtered.subList(filtered.size() - n, filtered.size());
        }

        if (filtered.isEmpty()) {
            System.out.println("No matches found.");
        } else {
            int wins = 0, losses = 0, draws = 0;
            for (MatchRecord m : filtered) {
                System.out.println(m.getInfo());
                switch (m.getResult()) {
                    case WIN  -> wins++;
                    case LOSE -> losses++;
                    case DRAW -> draws++;
                }
            }
            System.out.printf("\nSummary: %dW / %dL / %dD (Win Rate: %.1f%%)\n",
                    wins, losses, draws,
                    (wins + losses > 0) ? (double) wins / (wins + losses) * 100 : 0);
        }
        InputHelper.pressEnterToContinue();
    }

    // ========================================================================
    //  Leaderboard (F6)
    // ========================================================================

    private static void handleLeaderboard() {
        System.out.println("\n--- Leaderboard ---");
        System.out.println("1. By Win Rate");
        System.out.println("2. By Level");
        System.out.println("3. By Equipment Usage (Equipment Ranking)");
        int choice = InputHelper.readIntRange("Choose: ", 1, 3);
        int topN = InputHelper.readInt("Top N: ");

        System.out.println();
        switch (choice) {
            case 1 -> {
                List<Player> list = rankingService.getLeaderboardByWinRate(topN);
                System.out.println("=== Leaderboard by Win Rate ===");
                System.out.printf("%-4s %-10s %-8s %-8s %s\n", "Rank", "Name", "Level", "WinRate", "Team");
                for (int i = 0; i < list.size(); i++) {
                    Player p = list.get(i);
                    System.out.printf("%-4d %-10s Lv.%-5d %-7.1f%% %s\n",
                            i + 1, p.getName(), p.getLevel(), p.getWinRate(),
                            p.getTeamId() != null ? p.getTeamId() : "N/A");
                }
                System.out.println("\nTie-breaker: same win rate → higher level first.");
            }
            case 2 -> {
                List<Player> list = rankingService.getLeaderboardByLevel(topN);
                System.out.println("=== Leaderboard by Level ===");
                System.out.printf("%-4s %-10s %-8s %-8s %s\n", "Rank", "Name", "Level", "WinRate", "Team");
                for (int i = 0; i < list.size(); i++) {
                    Player p = list.get(i);
                    System.out.printf("%-4d %-10s Lv.%-5d %-7.1f%% %s\n",
                            i + 1, p.getName(), p.getLevel(), p.getWinRate(),
                            p.getTeamId() != null ? p.getTeamId() : "N/A");
                }
                System.out.println("\nTie-breaker: same level → higher win rate first.");
            }
            case 3 -> {
                List<Equipment> list = rankingService.getEquipmentRankingByUsage();
                if (list.size() > topN) list = list.subList(0, topN);
                System.out.println("=== Equipment Ranking by Usage ===");
                System.out.printf("%-4s %-12s %-10s %s\n", "Rank", "ID", "Name", "Usage Count");
                for (int i = 0; i < list.size(); i++) {
                    Equipment eq = list.get(i);
                    System.out.printf("%-4d %-12s %-10s %d\n",
                            i + 1, eq.getEquipmentId(), eq.getName(), eq.getUsageCount());
                }
            }
        }
        InputHelper.pressEnterToContinue();
    }

    // ========================================================================
    //  Admin: Manage Players
    // ========================================================================

    private static void adminManagePlayers() {
        while (true) {
            System.out.println("\n--- Manage Players ---");
            System.out.println("1. List All Players");
            System.out.println("2. Add Player");
            System.out.println("3. Remove Player");
            System.out.println("4. Back");
            int choice = InputHelper.readIntRange("Choose: ", 1, 4);

            switch (choice) {
                case 1 -> {
                    System.out.println("\nAll Players:");
                    for (Player p : dataManager.getAllPlayers()) {
                        System.out.println(p.getInfo());
                    }
                    InputHelper.pressEnterToContinue();
                }
                case 2 -> {
                    String id = InputHelper.readString("New Player ID: ");
                    if (dataManager.getPlayerById(id) != null) {
                        System.out.println("Player ID already exists!");
                        InputHelper.pressEnterToContinue();
                        continue;
                    }
                    String name = InputHelper.readString("Name: ");
                    String teamId = InputHelper.readString("Team ID: ");
                    if (dataManager.getTeamById(teamId) == null) {
                        System.out.println("Warning: Team " + teamId + " does not exist. Player added without team.");
                    }
                    int level = InputHelper.readIntRange("Level (1-30): ", 1, 30);
                    double winRate = InputHelper.readDouble("Win Rate (0-100): ", 0, 100);
                    String password = InputHelper.readString("Password: ");
                    Player newPlayer = new Player(id, name, teamId, level, winRate);
                    dataManager.addPlayer(newPlayer, password);
                    // Add to team
                    Team team = dataManager.getTeamById(teamId);
                    if (team != null) {
                        team.addMember(newPlayer);
                        System.out.println("Player added to team " + team.getName());
                    }
                    System.out.println("Player " + name + " added successfully.");
                    InputHelper.pressEnterToContinue();
                }
                case 3 -> {
                    String id = InputHelper.readString("Player ID to remove: ");
                    Player removed = dataManager.getPlayerById(id);
                    if (removed == null) {
                        System.out.println("Player not found.");
                    } else {
                        // Remove from team
                        Team team = dataManager.getTeamById(removed.getTeamId());
                        if (team != null) {
                            team.removeMember(id);
                        }
                        dataManager.removePlayer(id);
                        System.out.println("Player " + removed.getName() + " removed.");
                    }
                    InputHelper.pressEnterToContinue();
                }
                case 4 -> {
                    return;
                }
            }
        }
    }

    // ========================================================================
    //  Admin: Manage Heroes
    // ========================================================================

    private static void adminManageHeroes() {
        while (true) {
            System.out.println("\n--- Manage Heroes ---");
            System.out.println("1. List All Heroes");
            System.out.println("2. Add Hero");
            System.out.println("3. Remove Hero");
            System.out.println("4. Back");
            int choice = InputHelper.readIntRange("Choose: ", 1, 4);

            switch (choice) {
                case 1 -> {
                    System.out.println("\nAll Heroes:");
                    for (Hero h : dataManager.getAllHeroes()) {
                        System.out.println(h.getInfo());
                    }
                    InputHelper.pressEnterToContinue();
                }
                case 2 -> {
                    String id = InputHelper.readString("Hero ID: ");
                    String name = InputHelper.readString("Hero Name: ");
                    System.out.println("Hero Types:");
                    for (HeroType t : HeroType.values()) {
                        System.out.println("  " + t.ordinal() + ". " + t);
                    }
                    int typeIdx = InputHelper.readIntRange("Choose type: ", 0, HeroType.values().length - 1);
                    HeroType type = HeroType.values()[typeIdx];
                    int hp = InputHelper.readInt("Base HP: ");
                    int attack = InputHelper.readInt("Base Attack: ");
                    int defense = InputHelper.readInt("Base Defense: ");
                    Hero hero = new Hero(id, name, type);
                    hero.addStat("hp", hp);
                    hero.addStat("attack", attack);
                    hero.addStat("defense", defense);
                    dataManager.addHero(hero);
                    System.out.println("Hero " + name + " added.");
                    InputHelper.pressEnterToContinue();
                }
                case 3 -> {
                    String id = InputHelper.readString("Hero ID to remove: ");
                    if (dataManager.getHeroById(id) == null) {
                        System.out.println("Hero not found.");
                    } else {
                        // Remove hero from all players
                        for (Player p : dataManager.getAllPlayers()) {
                            p.removeHero(id);
                        }
                        dataManager.removeHero(id);
                        System.out.println("Hero removed.");
                    }
                    InputHelper.pressEnterToContinue();
                }
                case 4 -> {
                    return;
                }
            }
        }
    }

    // ========================================================================
    //  Admin: Manage Equipment
    // ========================================================================

    private static void adminManageEquipment() {
        while (true) {
            System.out.println("\n--- Manage Equipment ---");
            System.out.println("1. List All Equipment");
            System.out.println("2. Add Equipment");
            System.out.println("3. Remove Equipment");
            System.out.println("4. Back");
            int choice = InputHelper.readIntRange("Choose: ", 1, 4);

            switch (choice) {
                case 1 -> {
                    System.out.println("\nAll Equipment:");
                    for (Equipment eq : dataManager.getAllEquipment()) {
                        System.out.println(eq.getInfo());
                    }
                    InputHelper.pressEnterToContinue();
                }
                case 2 -> {
                    String id = InputHelper.readString("Equipment ID: ");
                    String name = InputHelper.readString("Equipment Name: ");
                    System.out.println("Equipment Types:");
                    for (EquipmentType t : EquipmentType.values()) {
                        System.out.println("  " + t.ordinal() + ". " + t);
                    }
                    int typeIdx = InputHelper.readIntRange("Choose type: ", 0, EquipmentType.values().length - 1);
                    EquipmentType type = EquipmentType.values()[typeIdx];
                    Equipment eq = new Equipment(id, name, type);
                    dataManager.addEquipment(eq);
                    System.out.println("Equipment " + name + " added.");
                    InputHelper.pressEnterToContinue();
                }
                case 3 -> {
                    String id = InputHelper.readString("Equipment ID to remove: ");
                    if (dataManager.getEquipmentById(id) == null) {
                        System.out.println("Equipment not found.");
                    } else {
                        dataManager.removeEquipment(id);
                        System.out.println("Equipment removed.");
                    }
                    InputHelper.pressEnterToContinue();
                }
                case 4 -> {
                    return;
                }
            }
        }
    }

    // ========================================================================
    //  Admin: Manage Teams
    // ========================================================================

    private static void adminManageTeams() {
        while (true) {
            System.out.println("\n--- Manage Teams ---");
            System.out.println("1. List All Teams");
            System.out.println("2. Add Team");
            System.out.println("3. Remove Team");
            System.out.println("4. Back");
            int choice = InputHelper.readIntRange("Choose: ", 1, 4);

            switch (choice) {
                case 1 -> {
                    for (Team t : dataManager.getAllTeams()) {
                        System.out.println(t.getInfo());
                    }
                    InputHelper.pressEnterToContinue();
                }
                case 2 -> {
                    String id = InputHelper.readString("Team ID: ");
                    String name = InputHelper.readString("Team Name: ");
                    Team team = new Team(id, name);
                    dataManager.addTeam(team);
                    System.out.println("Team " + name + " added.");
                    InputHelper.pressEnterToContinue();
                }
                case 3 -> {
                    String id = InputHelper.readString("Team ID to remove: ");
                    if (dataManager.getTeamById(id) == null) {
                        System.out.println("Team not found.");
                    } else {
                        dataManager.removeTeam(id);
                        System.out.println("Team removed.");
                    }
                    InputHelper.pressEnterToContinue();
                }
                case 4 -> {
                    return;
                }
            }
        }
    }

    // ========================================================================
    //  Admin: Manage Match Records
    // ========================================================================

    private static void adminManageMatches() {
        while (true) {
            System.out.println("\n--- Manage Match Records ---");
            System.out.println("1. List All Matches");
            System.out.println("2. Add Match Record");
            System.out.println("3. Remove Match Record");
            System.out.println("4. Back");
            int choice = InputHelper.readIntRange("Choose: ", 1, 4);

            switch (choice) {
                case 1 -> {
                    for (MatchRecord m : dataManager.getAllMatchRecords()) {
                        System.out.println(m.getInfo());
                    }
                    InputHelper.pressEnterToContinue();
                }
                case 2 -> {
                    String id = InputHelper.readString("Match ID: ");
                    String dateStr = InputHelper.readString("Date (YYYY-MM-DD): ");
                    String teamA = InputHelper.readString("Team A ID: ");
                    String teamB = InputHelper.readString("Team B ID: ");
                    System.out.println("Result: 0=WIN, 1=LOSE, 2=DRAW");
                    int resultIdx = InputHelper.readIntRange("Choose: ", 0, 2);
                    MatchResult result = MatchResult.values()[resultIdx];
                    MatchRecord match = new MatchRecord(id, LocalDate.parse(dateStr), teamA, teamB, result);
                    dataManager.addMatchRecord(match);
                    System.out.println("Match record added.");
                    InputHelper.pressEnterToContinue();
                }
                case 3 -> {
                    String id = InputHelper.readString("Match ID to remove: ");
                    dataManager.removeMatchRecord(id);
                    System.out.println("Match record removed (if existed).");
                    InputHelper.pressEnterToContinue();
                }
                case 4 -> {
                    return;
                }
            }
        }
    }
}
