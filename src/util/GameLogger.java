package util;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * 四级分层日志工具
 *
 * 日志级别（由低到高）：
 * - DEBUG: 调试信息，用于开发阶段追踪
 * - INFO:  常规信息，记录系统正常运行事件
 * - WARN:  警告信息，表示潜在问题但系统仍可运行
 * - ERROR: 错误信息，表示功能受阻或异常
 *
 * 可通过 setMinLevel() 控制输出级别，默认只输出 INFO 及以上。
 * 可通过 enableFileLogging() 启用文件输出。
 */
public class GameLogger {

    public enum Level {
        DEBUG(0),
        INFO(1),
        WARN(2),
        ERROR(3);

        private final int priority;
        Level(int priority) { this.priority = priority; }
        public int getPriority() { return priority; }
    }

    private static Level minLevel = Level.INFO;
    private static PrintWriter fileWriter = null;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    private static final String LOG_FILE = "logs/system.log";
    private static final Object LOCK = new Object();

    static {
        // Ensure log directory exists
        File logDir = new File("logs");
        if (!logDir.exists()) {
            logDir.mkdirs();
        }
    }

    /** 设置最低输出级别（默认 INFO） */
    public static void setMinLevel(Level level) {
        minLevel = level;
    }

    /** 启用文件日志输出 */
    public static void enableFileLogging() {
        enableFileLogging(LOG_FILE);
    }

    /** 启用文件日志输出到指定路径 */
    public static void enableFileLogging(String filePath) {
        try {
            FileOutputStream fos = new FileOutputStream(filePath, true);
            OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
            BufferedWriter bw = new BufferedWriter(osw);
            fileWriter = new PrintWriter(bw, true); // autoFlush
            info("GameLogger", "File logging enabled: " + filePath);
        } catch (IOException e) {
            System.err.println("[GameLogger] Failed to open log file: " + e.getMessage());
        }
    }

    /** 关闭文件日志 */
    public static void disableFileLogging() {
        if (fileWriter != null) {
            fileWriter.close();
            fileWriter = null;
        }
    }

    public static void debug(String source, String message) {
        log(Level.DEBUG, source, message);
    }

    public static void info(String source, String message) {
        log(Level.INFO, source, message);
    }

    public static void warn(String source, String message) {
        log(Level.WARN, source, message);
    }

    public static void error(String source, String message) {
        log(Level.ERROR, source, message);
    }

    public static void error(String source, String message, Throwable e) {
        if (e == null) {
            log(Level.ERROR, source, message + " | (no exception)");
            return;
        }
        String stackTrace = Arrays.stream(e.getStackTrace())
                .limit(10)
                .map(StackTraceElement::toString)
                .collect(Collectors.joining("\n  "));
        log(Level.ERROR, source, message + " | " + e.getClass().getSimpleName()
                + ": " + (e.getMessage() != null ? e.getMessage() : "null")
                + "\n  StackTrace (top 10):\n  " + stackTrace);
    }

    // ========================================================================
    //  Internal
    // ========================================================================

    private static void log(Level level, String source, String message) {
        if (level.getPriority() < minLevel.getPriority()) {
            return;
        }

        String timestamp = LocalDateTime.now().format(FORMATTER);
        String logLine = String.format("[%-5s] [%s] [%s] %s",
                level.name(), timestamp, source, message);

        synchronized (LOCK) {
            // Console output
            System.out.println(logLine);

            // File output
            if (fileWriter != null) {
                fileWriter.println(logLine);
            }
        }
    }

    /** 读取日志文件内容（用于日志分析） */
    public static String readLogFile() {
        return readLogFile(LOG_FILE);
    }

    /** 读取指定日志文件内容 */
    public static String readLogFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return "[GameLogger] No log file found at: " + filePath;
        }

        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                new FileInputStream(file), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (IOException e) {
            return "[GameLogger] Failed to read log file: " + e.getMessage();
        }
        return sb.toString();
    }

    /** 分析日志中的 ERROR 和 WARN 记录（供 Log Agent 使用） */
    public static String analyzeLogs() {
        return analyzeLogs(LOG_FILE);
    }

    /** 分析指定日志文件中的问题 */
    public static String analyzeLogs(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return "[Log Analysis] No log file found at: " + filePath;
        }

        int errorCount = 0, warnCount = 0;
        StringBuilder errorDetails = new StringBuilder();
        StringBuilder warnDetails = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                new FileInputStream(file), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("[ERROR]")) {
                    errorCount++;
                    errorDetails.append("  ").append(line).append("\n");
                } else if (line.contains("[WARN ]")) {
                    warnCount++;
                    if (warnCount <= 5) { // Limit warning details
                        warnDetails.append("  ").append(line).append("\n");
                    }
                }
            }
        } catch (IOException e) {
            return "[Log Analysis] Failed to read log file: " + e.getMessage();
        }

        StringBuilder report = new StringBuilder();
        report.append("=== Log Analysis Report ===\n");
        report.append("Total ERRORs: ").append(errorCount).append("\n");
        report.append("Total WARNs:  ").append(warnCount).append("\n");

        if (errorCount > 0) {
            report.append("\n--- ERROR Details ---\n").append(errorDetails);
            report.append("Recommendation: Review errors above and dispatch Fix Agent to resolve.\n");
        }
        if (warnCount > 0) {
            report.append("\n--- WARN Details (first 5) ---\n").append(warnDetails);
            if (warnCount > 5) {
                report.append("  ... and ").append(warnCount - 5).append(" more warnings\n");
            }
        }
        if (errorCount == 0 && warnCount == 0) {
            report.append("\nNo issues found. System is healthy.\n");
        }
        return report.toString();
    }
}
