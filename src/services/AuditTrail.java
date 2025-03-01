package services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AuditTrail {
    private static List<String> logs = new ArrayList<>();

    // ฟังก์ชันบันทึกการเข้าถึง
    public static void logAccess(String cardId, List<String> locations, boolean success) {
        String log = LocalDateTime.now() + " - Card: " + cardId + " tried to access " + String.join(", ", locations)
                + " - " + (success ? "GRANTED" : "DENIED");
        logs.add(log);
        System.out.println(log);  // แสดงผลใน Console ด้วย
    }

    // ฟังก์ชันบันทึกการเพิ่ม/แก้ไขบัตร
    public static void logModification(String action, String cardId) {
        String log = LocalDateTime.now() + " - Card " + cardId + " was " + action;
        logs.add(log);
        System.out.println(log);  // แสดงผลใน Console ด้วย
    }

    // ฟังก์ชันบันทึกการเพิกถอนบัตร
    public static void logAction(String action, String cardId) {
        String log = LocalDateTime.now() + " - Action: " + action + " performed on Card: " + cardId;
        logs.add(log);
        System.out.println(log);  // แสดงผลใน Console ด้วย
    }

    // ฟังก์ชันดึงข้อมูลบันทึก
    public static List<String> getLogs() {
        return logs;
    }
}
