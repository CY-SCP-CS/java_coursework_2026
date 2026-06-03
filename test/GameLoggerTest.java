import org.junit.jupiter.api.*;
import util.GameLogger;
import static org.junit.jupiter.api.Assertions.*;

/**
 * GameLogger 日志工具单元测试
 */
public class GameLoggerTest {

    @BeforeEach
    @AfterEach
    public void resetLogger() {
        GameLogger.setMinLevel(GameLogger.Level.INFO);
        GameLogger.disableFileLogging();
    }

    @Test
    public void testLevelPriorities() {
        assertEquals(0, GameLogger.Level.DEBUG.getPriority());
        assertEquals(1, GameLogger.Level.INFO.getPriority());
        assertEquals(2, GameLogger.Level.WARN.getPriority());
        assertEquals(3, GameLogger.Level.ERROR.getPriority());
    }

    @Test
    public void testSetMinLevel() {
        GameLogger.setMinLevel(GameLogger.Level.DEBUG);
        // Should not throw — just verifies the level is accepted
        GameLogger.debug("Test", "debug message");
        GameLogger.info("Test", "info message");
    }

    @Test
    public void testSetMinLevelWarn() {
        GameLogger.setMinLevel(GameLogger.Level.WARN);
        // INFO should be suppressed
        GameLogger.info("Test", "this should be suppressed");
        // WARN and ERROR should pass
        GameLogger.warn("Test", "warn message");
        GameLogger.error("Test", "error message");
    }

    @Test
    public void testLogMethods() {
        // These should all work without throwing
        GameLogger.debug("TestSource", "debug msg");
        GameLogger.info("TestSource", "info msg");
        GameLogger.warn("TestSource", "warn msg");
        GameLogger.error("TestSource", "error msg");
    }

    @Test
    public void testErrorWithException() {
        Exception ex = new RuntimeException("test error");
        GameLogger.error("TestSource", "Something failed", ex);
        // Should not throw when given a null exception
        GameLogger.error("TestSource", "Null exception", null);
    }

    @Test
    public void testFileLogging() {
        GameLogger.enableFileLogging();
        GameLogger.info("Test", "File log test");
    }

    @Test
    public void testDisableFileLogging() {
        GameLogger.enableFileLogging();
        GameLogger.disableFileLogging();
        GameLogger.info("Test", "After disabling file log");
    }

    @Test
    public void testReadLogFileNonExistent() {
        String result = GameLogger.readLogFile("nonexistent.log");
        assertTrue(result.contains("No log file found"));
    }

    @Test
    public void testAnalyzeLogsNonExistent() {
        String result = GameLogger.analyzeLogs("nonexistent.log");
        assertTrue(result.contains("No log file found"));
    }

    @Test
    public void testEnableFileLoggingCustomPath() {
        GameLogger.enableFileLogging("logs/test_custom.log");
        GameLogger.info("Test", "Custom path test");
    }
}
