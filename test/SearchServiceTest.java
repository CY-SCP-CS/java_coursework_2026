import model.*;
import org.junit.jupiter.api.*;
import service.*;
import util.DataInitializer;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 搜索服务单元测试
 */
public class SearchServiceTest {

    private static GameDataManager dataManager;
    private SearchService searchService;

    @BeforeAll
    public static void setupClass() {
        dataManager = DataInitializer.initData();
    }

    @BeforeEach
    public void setup() {
        searchService = new SearchService(dataManager);
    }

    @Test
    public void testSearchPlayerByIdFound() {
        Player player = searchService.searchPlayerById("P001");
        assertNotNull(player);
        assertEquals("P001", player.getId());
    }

    @Test
    public void testSearchPlayerByIdNotFound() {
        assertNull(searchService.searchPlayerById("P999"));
    }

    @Test
    public void testSearchPlayerByNameExact() {
        java.util.List<Player> results = searchService.searchPlayerByName("梦之泪伤");
        assertFalse(results.isEmpty());
        assertEquals("P001", results.get(0).getId());
    }

    @Test
    public void testSearchPlayerByNamePartial() {
        java.util.List<Player> results = searchService.searchPlayerByName("月"); // matches 月色如歌, 月下独酌, 明月
        assertFalse(results.isEmpty());
        assertTrue(results.size() >= 2);
    }

    @Test
    public void testSearchPlayerByNameNotFound() {
        java.util.List<Player> results = searchService.searchPlayerByName("NonExistentPlayer");
        assertTrue(results.isEmpty());
    }

    @Test
    public void testSearchTeamByIdFound() {
        Team team = searchService.searchTeamById("T001");
        assertNotNull(team);
        assertEquals("星辰战队", team.getName());
    }

    @Test
    public void testSearchTeamByIdNotFound() {
        assertNull(searchService.searchTeamById("T999"));
    }

    @Test
    public void testSearchTeamByNameFound() {
        Team team = searchService.searchTeamByName("雷霆战队");
        assertNotNull(team);
        assertEquals("T002", team.getTeamId());
    }

    @Test
    public void testSearchTeamByNameNotFound() {
        assertNull(searchService.searchTeamByName("NonExistent"));
    }

    @Test
    public void testSearchHeroByNameExact() {
        java.util.List<Hero> results = searchService.searchHeroByName("李白");
        assertFalse(results.isEmpty());
        assertTrue(results.stream().anyMatch(h -> h.getHeroId().equals("H007")));
    }

    @Test
    public void testSearchHeroByNamePartial() {
        java.util.List<Hero> results = searchService.searchHeroByName("貂");
        assertFalse(results.isEmpty());
    }

    @Test
    public void testSearchHeroByNameNotFound() {
        java.util.List<Hero> results = searchService.searchHeroByName("NonExistentHero");
        assertTrue(results.isEmpty());
    }

    @Test
    public void testDisplayPlayerDetail() {
        Player player = searchService.searchPlayerById("P001");
        String detail = searchService.displayPlayerDetail(player);
        assertTrue(detail.contains("P001"));
        assertTrue(detail.contains("梦之泪伤"));
        assertTrue(detail.contains("T001"));
    }

    @Test
    public void testDisplayTeamDetail() {
        Team team = searchService.searchTeamById("T001");
        String detail = searchService.displayTeamDetail(team);
        assertTrue(detail.contains("T001"));
        assertTrue(detail.contains("星辰战队"));
        assertTrue(detail.contains("Members"));
    }

    @Test
    public void testDisplayHeroDetail() {
        java.util.List<Hero> results = searchService.searchHeroByName("李白");
        assertFalse(results.isEmpty());
        String detail = searchService.displayHeroDetail(results.get(0));
        assertTrue(detail.contains("H007"));
        assertTrue(detail.contains("李白"));
    }

    @Test
    public void testGetMatchHistoryByPlayer() {
        java.util.List<MatchRecord> matches = searchService.getMatchHistory("P001", 0);
        assertFalse(matches.isEmpty());
    }

    @Test
    public void testGetMatchHistoryByTeam() {
        java.util.List<MatchRecord> matches = searchService.getMatchHistory("T001", 0);
        assertFalse(matches.isEmpty());
    }

    @Test
    public void testGetMatchHistoryLimit() {
        java.util.List<MatchRecord> matches = searchService.getMatchHistory("T001", 2);
        assertTrue(matches.size() <= 2);
    }

    @Test
    public void testGetMatchHistoryNonExistent() {
        java.util.List<MatchRecord> matches = searchService.getMatchHistory("T999", 0);
        assertTrue(matches.isEmpty());
    }

    @Test
    public void testDisplayMatchHistoryEmpty() {
        String display = searchService.displayMatchHistory(java.util.Collections.emptyList());
        assertEquals("No match records found.", display);
    }

    @Test
    public void testDisplayMatchHistorySummary() {
        java.util.List<MatchRecord> matches = searchService.getMatchHistory("T001", 0);
        String display = searchService.displayMatchHistory(matches);
        assertTrue(display.contains("W /"));
        assertTrue(display.contains("L /"));
        assertTrue(display.contains("D"));
    }
}
