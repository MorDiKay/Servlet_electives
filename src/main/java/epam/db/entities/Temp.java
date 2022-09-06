package epam.db.entities;

public class Temp {

    private String login;

    private int userId;

    private String roleName;

    private int electiveId;

    private boolean isBlocked;

   public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public int getUserId() { return userId; }

    public void setUserId(int userId) { this.userId = userId; }

    public String getRoleName() { return roleName; }

    public void setRoleName(String roleName) { this.roleName = roleName; }

    public int getElectiveId() { return electiveId; }

    public void setElectiveId(int electiveId) { this.electiveId = electiveId; }

    public boolean isBlocked() { return isBlocked; }

    public void setBlocked(boolean blocked) { isBlocked = blocked; }
}
