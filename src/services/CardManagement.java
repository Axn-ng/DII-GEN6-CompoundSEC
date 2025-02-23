package services;

import models.AccessCard;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CardManagement {
    private static Map<String, AccessCard> cardDatabase = new HashMap<>();

    public static void addCard(String cardId, LocalDateTime validUntil, List<String> accessLevels) {
        AccessCard newCard = new AccessCard(cardId, validUntil, accessLevels);
        cardDatabase.put(cardId, newCard);
        AuditTrail.logModification("created", cardId);
    }

    public static void modifyCard(String cardId, LocalDateTime newValidUntil, List<String> newAccessLevels) {
        if (cardDatabase.containsKey(cardId)) {
            cardDatabase.put(cardId, new AccessCard(cardId, newValidUntil, newAccessLevels));
            AuditTrail.logModification("modified", cardId);
        }
    }

    public static void revokeCard(String cardId) {
        if (cardDatabase.remove(cardId) != null) {
            AuditTrail.logModification("revoked", cardId);
        }
    }

    public static Map<String, AccessCard> getAllCards() {
        return cardDatabase;
    }

    public static void showAuditLogs() {
        AuditTrail.showLogs();
    }

    public static AccessCard getCard(String cardId) {
        return null;
    }
}
