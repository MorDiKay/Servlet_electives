package epam.db.entities;

/**
 * Role entity (roles).
 *
 */
public enum Role {
    ADMIN, CLIENT, LECTURER;

    public static Role getRole(User user) {
        int roleId = user.getRoleId();
        return Role.values()[roleId];
    }

    public String getName() { return name().toLowerCase(); }
}
