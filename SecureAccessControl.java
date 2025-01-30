class SecureAccessControl {
    // การตรวจสอบการเข้าถึงตามชั้น
    public boolean hasAccessToFloor(String userRole, String floor) {
        if (floor.equals("ground")) {
            return userRole.equals("Year1");
        } else if (floor.equals("middle")) {
            return userRole.equals("Year2");
        } else if (floor.equals("top")) {
            return userRole.equals("Year3") || userRole.equals("Year4");
        }
        return false;
    }
}
