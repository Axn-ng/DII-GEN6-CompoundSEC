package models;

import java.time.LocalDateTime;
import java.util.List;

public class AccessCard {
    private String cardId;
    private LocalDateTime validUntil;
    private List<String> accessLevels;

    public AccessCard(String cardId, LocalDateTime validUntil, List<String> accessLevels) {
        this.cardId = cardId;
        this.validUntil = validUntil;
        this.accessLevels = accessLevels;
    }

    public boolean isValid() {
        return LocalDateTime.now().isBefore(validUntil);
    }

    public List<String> getAccessLevels() {
        return accessLevels;
    }

    public String getCardId() {
        return cardId;
    }

    public LocalDateTime getValidUntil() {
        return validUntil;
    }

    public boolean hasAccess(String room) {

        return false;
    }
}
