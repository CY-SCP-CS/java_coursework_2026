import service.*;
import util.*;

/**
 * Honor of Kings Information Management System
 * 主入口类
 */
public class Main {
    private static GameDataManager dataManager;
    private static AuthenticationService authService;
    private static SearchService searchService;
    private static RankingService rankingService;

    public static void main(String[] args) {
        // 初始化数据
        dataManager = DataInitializer.initData();
        authService = new AuthenticationService(dataManager);
        searchService = new SearchService(dataManager);
        rankingService = new RankingService(dataManager);

        System.out.println("=== Honor of Kings IMS ===");
        System.out.println("Welcome to the system!");
        printDataSummary();

        // 菜单循环将在后续实现
    }

    /**
     * 打印初始数据摘要用于验证
     */
    private static void printDataSummary() {
        System.out.println("\n--- Data Summary ---");
        System.out.println("Equipment: " + dataManager.getAllEquipment().size());
        System.out.println("Heroes:    " + dataManager.getAllHeroes().size());
        System.out.println("Players:   " + dataManager.getAllPlayers().size());
        System.out.println("Teams:     " + dataManager.getAllTeams().size());
        System.out.println("Matches:   " + dataManager.getAllMatchRecords().size());
        System.out.println("Accounts:  admin / player1 / player2 ...");
        System.out.println("--------------------\n");
    }
}
