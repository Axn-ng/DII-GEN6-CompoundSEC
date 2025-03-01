package services;

import models.AccessCard;
import java.util.List;

public class AccessControlSystem {
    public static void verifyAccess(AccessCard card, String location, boolean isRoom) {
        if (!card.isValid()) {
            System.out.println("Access Denied: Card Expired!");
            return;
        }

        if (!card.isWithinAllowedTime()) {
            System.out.println("Access Denied: Out of Allowed Hours!");
            return;
        }

        List<String> accessLevels = card.getAccessLevels();
        if (accessLevels.contains(location)) {
            System.out.println("Access Granted to " + location);
        } else {
            System.out.println("Access Denied to " + location);
        }
    }
}
