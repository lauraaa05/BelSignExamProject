package dal;

public enum UserRole {
    OPERATOR("Operator"),
    QUALITY_CONTROL("Quality Control");

    private final String roleName;

    UserRole(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }

    public static UserRole fromString(String text) {
        for (UserRole role : UserRole.values()) {
            if (role.roleName.equalsIgnoreCase(text)) {
                return role;
            }
        }
        throw new IllegalArgumentException("No enum constant with the role text: " + text);
    }
}
