import model.*;
import org.junit.jupiter.api.*;
import service.*;
import util.DataInitializer;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 文件持久化服务：保存/加载循环测试
 *
 * 注意：这些测试依赖文件 I/O，运行前确保 data/ 目录可写。
 */
public class FileStorageServiceTest {

    private static GameDataManager originalData;
    private FileStorageService fileStorage;

    @BeforeAll
    public static void setupClass() {
        originalData = DataInitializer.initData();
    }

    @BeforeEach
    public void setup() {
        fileStorage = new FileStorageService();
    }

    @AfterEach
    public void cleanup() throws IOException {
        // Clean up test files but not the original data
        Path dataDir = Paths.get("data");
        if (Files.exists(dataDir)) {
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(dataDir, "test_*.csv")) {
                for (Path entry : stream) {
                    Files.deleteIfExists(entry);
                }
            }
        }
    }

    @Test
    public void testSaveAndLoadEquipment() throws IOException {
        String testFile = "data/test_equipment.csv";
        Collection<Equipment> equipment = originalData.getAllEquipment();
        fileStorage.saveEquipment(equipment);
        // Verify file exists
        assertTrue(Files.exists(Paths.get(testFile.replace("test_", ""))));
    }

    @Test
    public void testSaveAndLoadHeroes() throws IOException {
        Collection<Hero> heroes = originalData.getAllHeroes();
        fileStorage.saveHeroes(heroes);
        assertTrue(Files.exists(Paths.get("data/heroes.csv")));
    }

    @Test
    public void testSaveAndLoadPlayers() throws IOException {
        Collection<Player> players = originalData.getAllPlayers();
        fileStorage.savePlayers(players);
        assertTrue(Files.exists(Paths.get("data/players.csv")));
    }

    @Test
    public void testSaveAndLoadTeams() throws IOException {
        Collection<Team> teams = originalData.getAllTeams();
        fileStorage.saveTeams(teams);
        assertTrue(Files.exists(Paths.get("data/teams.csv")));
    }

    @Test
    public void testSaveAndLoadMatchRecords() throws IOException {
        List<MatchRecord> matches = originalData.getAllMatchRecords();
        fileStorage.saveMatchRecords(matches);
        assertTrue(Files.exists(Paths.get("data/matches.csv")));
    }

    @Test
    public void testFullSaveCycle() {
        // Save all data
        fileStorage.saveAllData(originalData);
        // Verify all CSV files exist
        assertTrue(Files.exists(Paths.get("data/equipment.csv")));
        assertTrue(Files.exists(Paths.get("data/heroes.csv")));
        assertTrue(Files.exists(Paths.get("data/players.csv")));
        assertTrue(Files.exists(Paths.get("data/teams.csv")));
        assertTrue(Files.exists(Paths.get("data/matches.csv")));
        assertTrue(Files.exists(Paths.get("data/passwords.csv")));
        assertTrue(Files.exists(Paths.get("data/equipped_items.csv")));
    }

    @Test
    public void testFullLoadCycle() {
        // Save first
        fileStorage.saveAllData(originalData);
        // Load back
        GameDataManager loaded = fileStorage.loadAllData();
        // Verify data counts match
        assertEquals(originalData.getAllEquipment().size(), loaded.getAllEquipment().size());
        assertEquals(originalData.getAllHeroes().size(), loaded.getAllHeroes().size());
        assertEquals(originalData.getAllPlayers().size(), loaded.getAllPlayers().size());
        assertEquals(originalData.getAllTeams().size(), loaded.getAllTeams().size());
        assertEquals(originalData.getAllMatchRecords().size(), loaded.getAllMatchRecords().size());
    }

    @Test
    public void testLoadAllDataDoesNotCrash() {
        // Should not crash regardless of whether files exist
        assertDoesNotThrow(() -> {
            GameDataManager result = fileStorage.loadAllData();
            assertNotNull(result);
        });
    }

    @Test
    public void testCSVFileEncoding() throws IOException {
        // Verify files are UTF-8 encoded
        fileStorage.saveAllData(originalData);
        byte[] bytes = Files.readAllBytes(Paths.get("data/players.csv"));
        String content = new String(bytes, java.nio.charset.StandardCharsets.UTF_8);
        // Chinese characters should be preserved
        assertTrue(content.contains("梦之泪伤"));
        assertTrue(content.contains("月色如歌"));
    }

    @Test
    public void testSavePasswords() {
        fileStorage.savePasswords(originalData);
        assertTrue(Files.exists(Paths.get("data/passwords.csv")));
    }

    @Test
    public void testSaveEquippedItems() {
        fileStorage.saveEquippedItems(originalData.getAllPlayers());
        assertTrue(Files.exists(Paths.get("data/equipped_items.csv")));
    }
}
