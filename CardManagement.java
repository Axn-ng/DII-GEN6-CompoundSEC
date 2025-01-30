import java.util.HashMap;
import java.util.Map;

class CardManagement {
    private Map<String, String> cardDatabase = new HashMap<>();
    private Map<String, Boolean> cardStatus = new HashMap<>();
    private AuditTrail auditLog = new AuditTrail();

    // เพิ่มบัตรใหม่
    public void addCard(String cardId, String userRole) {
        cardDatabase.put(cardId, userRole);
        cardStatus.put(cardId, true); // บัตรเริ่มต้นจะถูกเปิดใช้งาน
        auditLog.logCardAction("ADD", cardId, "New card added with role: " + userRole);
    }

    // ยกเลิกการใช้งานบัตร
    public void revokeCard(String cardId) {
        if (cardDatabase.containsKey(cardId)) {
            cardStatus.put(cardId, false); // เปลี่ยนสถานะบัตรเป็นไม่ใช้งาน
            auditLog.logCardAction("REVOKE", cardId, "Card revoked");
        } else {
            System.out.println("Card not found.");
        }
    }

    // ตรวจสอบสถานะบัตร
    public boolean isCardValid(String cardId) {
        return cardStatus.getOrDefault(cardId, false); // หากไม่พบบัตรหรือบัตรถูกปิดใช้งาน
    }

    // ดึงข้อมูลการเข้าถึงของบัตร
    public String getCardAccessLevel(String cardId) {
        return cardDatabase.getOrDefault(cardId, "No role assigned");
    }

    // ฟังก์ชันตรวจสอบ ID, Role, และ Floor
    public boolean checkIdRoleFloor(String cardId, String floor) {
        String role = cardDatabase.get(cardId);
        if (role == null) {
            System.out.println("Card ID not found.");
            return false;
        }

        if (floor.equals("ground") && role.equals("Year1")) {
            return true; // นักศึกษาปีหนึ่งสามารถเข้าชั้นล่าง
        } else if (floor.equals("middle") && role.equals("Year2")) {
            return true; // นักศึกษาปีสองสามารถเข้าชั้นกลาง
        } else if (floor.equals("top") && (role.equals("Year3") || role.equals("Year4"))) {
            return true; // นักศึกษาปีสามหรือปีสี่สามารถเข้าชั้นบน
        }
        return false; // หากไม่ตรงกับเงื่อนไข
    }
}
