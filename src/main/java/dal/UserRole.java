package dal;

import java.util.HashMap;
import java.util.Map;

public enum UserRole {
    OPERATOR("Operator"),
    QUALITY_CONTROL("Quality Control"),
    ADMIN("Admin");

    private final String roleName;

    private static final Map<String, UserRole> ROLE_MAP = new HashMap<>();

    static {
        for (UserRole role : values()) {
            ROLE_MAP.put(role.roleName.toLowerCase(), role);
        }
    }

    UserRole(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }

    @Override
    public String toString() {
        return roleName;
    }

    public static UserRole fromString(String text) {
        if (text == null) {
            throw new IllegalArgumentException("Input cannot be null");
        }
        String key = text.trim().toLowerCase();
        UserRole role = ROLE_MAP.get(key);
        if (role == null) {
            throw new IllegalArgumentException("No enum constant with the role text: " + text);
        }
        return role;
    }

    public static boolean isValidRole(String text) {
        if (text == null) return false;
        return ROLE_MAP.containsKey(text.trim().toLowerCase());
    }
}