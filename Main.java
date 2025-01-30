import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // สร้างอินสแตนซ์ของคลาสที่เกี่ยวข้อง
        CardManagement cardManagement = new CardManagement();
        AuditTrail auditLog = new AuditTrail();
        SecureAccessControl accessControl = new SecureAccessControl();

        // ระบบการใช้งาน
        while (true) {
            System.out.println("=== Dormitory Access Control System ===");
            System.out.println("1. Add Card");
            System.out.println("2. Revoke Card");
            System.out.println("3. Check Access");
            System.out.println("4. Check ID, Role, and Floor Access");
            System.out.println("5. Exit");
            System.out.print("Enter choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // consume the newline

            switch (choice) {
                case 1:
                    System.out.print("Enter card ID: ");
                    String cardId = scanner.nextLine();
                    System.out.print("Enter user role (e.g., Year1, Year2): ");
                    String userRole = scanner.nextLine();
                    cardManagement.addCard(cardId, userRole);
                    break;
                case 2:
                    System.out.print("Enter card ID to revoke: ");
                    cardId = scanner.nextLine();
                    cardManagement.revokeCard(cardId);
                    break;
                case 3:
                    System.out.print("Enter card ID to check access: ");
                    cardId = scanner.nextLine();
                    System.out.print("Enter floor (ground, middle, top): ");
                    String floor = scanner.nextLine();
                    if (accessControl.hasAccessToFloor(cardManagement.getCardAccessLevel(cardId), floor)) {
                        System.out.println("Access granted to floor " + floor);
                    } else {
                        System.out.println("Access denied to floor " + floor);
                    }
                    break;
                case 4:
                    System.out.print("Enter card ID to check role and floor access: ");
                    cardId = scanner.nextLine();
                    System.out.print("Enter floor (ground, middle, top): ");
                    floor = scanner.nextLine();
                    if (cardManagement.checkIdRoleFloor(cardId, floor)) {
                        System.out.println("Access granted to floor " + floor);
                    } else {
                        System.out.println("Access denied to floor " + floor);
                    }
                    break;
                case 5:
                    System.out.println("Exiting system...");
                    scanner.close();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid option, please try again.");
                    break;
            }
        }
    }
}
