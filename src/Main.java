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

        // 菜单循环将在后续实现
    }
}
