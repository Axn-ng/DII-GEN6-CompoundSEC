class AuditTrail {
    // บันทึกการเข้าถึง
    public void logAccessAttempt(String userId, String floor, boolean accessGranted) {
        String status = accessGranted ? "granted" : "denied";
        System.out.println("Audit Log: User: " + userId + ", Floor: " + floor + ", Access " + status);
    }

    // บันทึกการกระทำการจัดการบัตร
    public void logCardAction(String action, String cardId, String reason) {
        System.out.println("Audit Log: Action: " + action + ", Card: " + cardId + ", Reason: " + reason);
    }
}
