package services;
import models.AccessCard;
import models.FloorAccess;
import models.RoomAccess;

public class AccessControlSystem {
    public static void verifyAccess(AccessCard card, String location, boolean isRoom) {
        boolean accessGranted = isRoom ? RoomAccess.checkRoomAccess(card, location)
                : FloorAccess.checkFloorAccess(card, location);
        System.out.println("Access Attempt: " + card.getCardId() + " to " + location + " - " + (accessGranted ? "GRANTED" : "DENIED"));
    }
}
