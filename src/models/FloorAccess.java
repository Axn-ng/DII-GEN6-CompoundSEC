package models;
public class FloorAccess {
    public static boolean checkFloorAccess(AccessCard card, String floor) {
        return card.hasAccess(floor);
    }
}
