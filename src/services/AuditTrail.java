package services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AuditTrail {
    private static List<String> logs = new ArrayList<>();

    public static void logAccess(String cardId, String location, boolean success) {
        String log = LocalDateTime.now() + " - Card: " + cardId + " tried to access " + location + " - " + (success ? "GRANTED" : "DENIED");
        logs.add(log);
        System.out.println(log);  // แสดงผลใน Console ด้วย
    }

    public static void logModification(String action, String cardId) {
        String log = LocalDateTime.now() + " - Card " + cardId + " was " + action;
        logs.add(log);
        System.out.println(log);  // แสดงผลใน Console ด้วย
    }

    public static List<String> getLogs() {
        return logs;
    }

    public static void showLogs() {
        if (logs.isEmpty()) {
            System.out.println("No audit logs available.");
        } else {
            System.out.println("=== Audit Logs ===");
            for (String log : logs) {
                System.out.println(log);
            }
        }
    }
}
