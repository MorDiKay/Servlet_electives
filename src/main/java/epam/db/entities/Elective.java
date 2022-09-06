package epam.db.entities;

import java.sql.Date;

/**
 * electives entity.
 *
 */
public class Elective  extends Entity {

    private String name;

    private Date startDate;

    private Date endDate;

    private String status;

    private int topicId;

    private int lecturerId;

    private String lecturerLogin;

    private int userMark;

    private byte studentCount;

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public Date getStartDate() { return startDate; }

    public void setStartDate(Date start_date) { this.startDate = start_date; }

    public Date getEndDate() { return endDate; }

    public void setEndDate(Date end_date) { this.endDate = end_date; }

    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }

    public int getTopicId() { return topicId; }

    public void setTopicId(int topic_id) { this.topicId = topic_id; }

    public int getLecturerId() { return lecturerId; }

    public void setLecturerId(int lecturer_id) { this.lecturerId = lecturer_id; }

    public String getLecturerLogin() { return lecturerLogin; }

    public void setLecturerLogin(String lecturerLogin) { this.lecturerLogin = lecturerLogin; }

    public int getUserMark() { return userMark; }

    public void setUserMark(int userMark) { this.userMark = userMark; }

    public byte getStudentCount() { return studentCount; }

    public void setStudentCount(byte studentCount) { this.studentCount = studentCount; }

    @Override
    public String toString() {
        return "Elective{" +
                "name='" + name + '\'' +
                ", start_date=" + startDate +
                ", end_date=" + endDate +
                ", status='" + status + '\'' +
                ", topic_id=" + topicId +
                ", lecturer_id=" + lecturerId +
                '}';
    }
}
