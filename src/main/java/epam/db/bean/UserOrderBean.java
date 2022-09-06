package epam.db.bean;

import java.util.Date;

/**
 * |user.id|elective.id|user.login|elective.topic_id|elective.name|
 * |elective.status|elective.lecturer_id|electives_have_users.mark|elective.end_date|
 */
public class UserOrderBean {

    private int userId;

    private int electiveId;

    private String userLogin;

    private int topicId;

    private String name;

    private String status;

    private int lecturerId;

    private String lecturerLogin;

    private int userMark;

    private Date endDate;

    public int getUserId() { return userId; }

    public void setUserId(int userId) { this.userId = userId; }

    public int getElectiveId() { return electiveId; }

    public void setElectiveId(int electiveId) { this.electiveId = electiveId; }

    public String getUserLogin() { return userLogin; }

    public void setUserLogin(String userLogin) { this.userLogin = userLogin; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }

    public int getTopicId() { return topicId; }

    public void setTopicId(int topicId) { this.topicId = topicId; }

    public int getLecturerId() { return lecturerId; }

    public void setLecturerId(int lecturerId) { this.lecturerId = lecturerId; }

    public String getLecturerLogin() { return lecturerLogin; }

    public void setLecturerLogin(String lecturerLogin) { this.lecturerLogin = lecturerLogin; }

    public int getUserMark() { return userMark; }

    public void setUserMark(int userMark) { this.userMark = userMark; }

    public Date getEndDate() { return endDate; }

    public void setEndDate(Date endDate) { this.endDate = endDate; }


    @Override
    public String toString() {
        return "UserOrderBean{" +
                "electiveId=" + electiveId +
                ", userLogin='" + userLogin + '\'' +
                ", topicId=" + topicId +
                ", name='" + name + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
