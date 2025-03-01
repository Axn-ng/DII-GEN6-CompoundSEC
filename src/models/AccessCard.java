package models;

import java.time.LocalDateTime;
import java.util.List;
import java.time.LocalTime;

public class AccessCard {
    private String cardId;
    private LocalDateTime validUntil;
    private List<String> accessLevels;
    private LocalTime allowedStartTime;  // เวลาที่เริ่มใช้บัตรได้
    private LocalTime allowedEndTime;    // เวลาที่หมดสิทธิ์ใช้บัตร

    public AccessCard(String cardId, LocalDateTime validUntil, List<String> accessLevels,
                      LocalTime allowedStartTime, LocalTime allowedEndTime) {
        this.cardId = cardId;
        this.validUntil = validUntil;
        this.accessLevels = accessLevels;
        this.allowedStartTime = allowedStartTime;
        this.allowedEndTime = allowedEndTime;
    }

    public boolean isValid() {
        return LocalDateTime.now().isBefore(validUntil);
    }

    public boolean isWithinAllowedTime() {
        LocalTime now = LocalTime.now();
        return now.isAfter(allowedStartTime) && now.isBefore(allowedEndTime);
    }

    // Getter และ Setter
    public String getCardId() { return cardId; }
    public LocalDateTime getValidUntil() { return validUntil; }
    public List<String> getAccessLevels() { return accessLevels; }
    public LocalTime getAllowedStartTime() { return allowedStartTime; }
    public LocalTime getAllowedEndTime() { return allowedEndTime; }

    public boolean hasAccess(String room) {
        return false;
    }
}

