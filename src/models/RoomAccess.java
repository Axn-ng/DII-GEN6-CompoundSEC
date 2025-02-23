package models;
public class RoomAccess {
    public static boolean checkRoomAccess(AccessCard card, String room) {
        return card.hasAccess(room);
    }
}
