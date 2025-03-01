package services;

import models.AccessCard;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CardManagement {
    private static Map<String, AccessCard> cardDatabase = new HashMap<>();

    public static void addCard(String cardId, LocalDateTime validUntil, List<String> accessLevels,
                               LocalTime startTime, LocalTime endTime) {
        AccessCard newCard = new AccessCard(cardId, validUntil, accessLevels, startTime, endTime);
        cardDatabase.put(cardId, newCard);
        AuditTrail.logModification("Added", cardId);
        System.out.println("Card added successfully!");
    }

    public static void modifyCard(String cardId, LocalDateTime newValidUntil, List<String> newAccessLevels,
                                  LocalTime newStartTime, LocalTime newEndTime) {
        if (cardDatabase.containsKey(cardId)) {
            // ดึงข้อมูลบัตรเดิมมาใช้
            AccessCard existingCard = cardDatabase.get(cardId);

            // ใช้ค่าใหม่ที่ส่งมา ถ้าไม่มีให้ใช้ค่าจากเดิม
            LocalTime updatedStartTime = (newStartTime != null) ? newStartTime : existingCard.getAllowedStartTime();
            LocalTime updatedEndTime = (newEndTime != null) ? newEndTime : existingCard.getAllowedEndTime();

            // ✅ อัปเดตบัตรใหม่
            AccessCard updatedCard = new AccessCard(cardId, newValidUntil, newAccessLevels, updatedStartTime, updatedEndTime);
            cardDatabase.put(cardId, updatedCard);

            // ✅ บันทึก Log
            AuditTrail.logModification("Modified", cardId);

            System.out.println("Card modified successfully!");
        }
    }

    public static void revokeCard(String cardId) {
        cardDatabase.remove(cardId);
        AuditTrail.logModification("Revoked", cardId);
        System.out.println("Card revoked successfully!");
    }

    public static AccessCard getCard(String cardId) {
        return cardDatabase.get(cardId);
    }

    public static Map<String, AccessCard> getAllCards() {
        return cardDatabase;
    }
}
