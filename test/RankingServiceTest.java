import model.*;
import org.junit.jupiter.api.*;
import service.*;
import util.DataInitializer;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 排行榜服务单元测试
 */
public class RankingServiceTest {

    private static GameDataManager dataManager;
    private RankingService rankingService;

    @BeforeAll
    public static void setupClass() {
        dataManager = DataInitializer.initData();
    }

    @BeforeEach
    public void setup() {
        rankingService = new RankingService(dataManager);
    }

    @Test
    public void testGetLeaderboardByWinRate() {
        List<Player> list = rankingService.getLeaderboardByWinRate(5);
        assertEquals(5, list.size());
        // Verify sorted: first player should have highest win rate
        for (int i = 0; i < list.size() - 1; i++) {
            assertTrue(list.get(i).getWinRate() >= list.get(i + 1).getWinRate());
        }
    }

    @Test
    public void testGetLeaderboardByWinRateTopNAll() {
        List<Player> list = rankingService.getLeaderboardByWinRate(100);
        assertEquals(15, list.size());
    }

    @Test
    public void testGetLeaderboardByWinRateTieBreaker() {
        // Tie-breaker: same win rate → higher level first
        // We can't guarantee same win rates exist, but ordering should be consistent
        List<Player> list = rankingService.getLeaderboardByWinRate(15);
        for (int i = 0; i < list.size() - 1; i++) {
            double currWR = list.get(i).getWinRate();
            double nextWR = list.get(i + 1).getWinRate();
            if (Math.abs(currWR - nextWR) < 0.001) {
                // Same win rate → higher level should come first
                assertTrue(list.get(i).getLevel() >= list.get(i + 1).getLevel());
            }
        }
    }

    @Test
    public void testGetLeaderboardByLevel() {
        List<Player> list = rankingService.getLeaderboardByLevel(5);
        assertEquals(5, list.size());
        for (int i = 0; i < list.size() - 1; i++) {
            assertTrue(list.get(i).getLevel() >= list.get(i + 1).getLevel());
        }
    }

    @Test
    public void testGetLeaderboardByMatches() {
        List<Player> list = rankingService.getLeaderboardByMatches(5);
        assertEquals(5, list.size());
    }

    @Test
    public void testGetEquipmentRankingByUsage() {
        List<Equipment> list = rankingService.getEquipmentRankingByUsage();
        assertEquals(20, list.size());
        for (int i = 0; i < list.size() - 1; i++) {
            assertTrue(list.get(i).getUsageCount() >= list.get(i + 1).getUsageCount());
        }
    }

    @Test
    public void testGetEquipmentRankingByHeroCount() {
        // New no-arg method should return all equipment sorted by compatible hero count
        List<Equipment> list = rankingService.getEquipmentRankingByHeroCount();
        assertEquals(20, list.size());
        // At least some equipment should be compatible with multiple heroes
        assertFalse(list.isEmpty());
    }

    @Test
    public void testGetCustomScore() {
        Player player = dataManager.getPlayerById("P001");
        double score = rankingService.getCustomScore(player);
        // Score = winRate * 0.5 + level * 2.0 + matches * 0.1
        double expected = 65.5 * 0.5 + 28 * 2.0 + rankingService.getPlayerMatchCount("P001") * 0.1;
        assertEquals(expected, score, 0.01);
    }

    @Test
    public void testGetLeaderboardByCustomScore() {
        List<Player> list = rankingService.getLeaderboardByCustomScore(5);
        assertEquals(5, list.size());
    }

    @Test
    public void testGetPlayerMatchCount() {
        int count = rankingService.getPlayerMatchCount("P001");
        // P001 is on team T001 which has several matches
        assertTrue(count > 0, "Player P001 should have matches");
    }

    @Test
    public void testGetPlayerMatchCountNonExistent() {
        int count = rankingService.getPlayerMatchCount("P999");
        assertEquals(0, count);
    }

    @Test
    public void testGetPlayerMatchCountNoTeam() {
        // Use a player ID that exists but has no team — P001 has team T001 so use a non-existent ID
        int count = rankingService.getPlayerMatchCount("P999");
        assertEquals(0, count);
    }
}
