package util;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * 输入工具类
 * 封装 Scanner 操作，提供带验证的输入方法
 */
public class InputHelper {
    private static Scanner scanner = new Scanner(System.in);

    /**
     * 读取整数
     */
    public static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter an integer.");
                scanner.next(); // 清除错误输入
            }
        }
    }

    /**
     * 读取字符串（非空）
     */
    public static String readString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            // 处理 nextInt() 后残留的换行
            if (input.isEmpty()) {
                input = scanner.nextLine().trim();
            }
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("Input cannot be empty.");
        }
    }

    /**
     * 读取限定范围的整数
     */
    public static int readIntRange(String prompt, int min, int max) {
        while (true) {
            int value = readInt(prompt);
            if (value >= min && value <= max) {
                return value;
            }
            System.out.println("Please enter a value between " + min + " and " + max + ".");
        }
    }

    /**
     * 读取 Y/N 确认
     */
    public static boolean readYesNo(String prompt) {
        while (true) {
            System.out.print(prompt + " (Y/N): ");
            String input = scanner.next().trim().toUpperCase();
            if (input.equals("Y")) return true;
            if (input.equals("N")) return false;
            System.out.println("Please enter Y or N.");
        }
    }
}
