package epam.db.entities;

/**
 * electives_have_users entity
 *
 */
public class ElectivesHaveUsers {

    private int user_id;

    private int elective_id;

    private int mark;

    public int getUserId() { return user_id; }

    public void setUserId(int user_id) { this.user_id = user_id; }

    public int getElectiveId() { return elective_id; }

    public void setElectiveId(int elective_id) { this.elective_id = elective_id; }

    public int getMark() { return mark; }

    public void setMark(int mark) { this.mark = mark; }

    @Override
    public String toString() {
        return "ElectivesHaveUsers{" +
                "user_id=" + user_id +
                ", elective_id=" + elective_id +
                '}';
    }
}
