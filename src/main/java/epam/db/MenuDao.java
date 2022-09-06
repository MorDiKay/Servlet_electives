package epam.db;

import epam.db.bean.UserOrderBean;
import epam.db.entities.Elective;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import epam.db.entities.ElectivesHaveUsers;
import epam.db.entities.Temp;
import epam.db.entities.Topic;
import epam.exceptions.DBException;
import org.apache.log4j.Logger;

/**
 * Data access object for menu related entities.
 */
public class MenuDao {

    static Logger log = Logger.getLogger(MenuDao.class);

    private static final String SQL__FIND_ALL_TOPICS =
            "SELECT * FROM topics";

    private static final String SQL__FIND_ALL_ELECTIVES =
            "SELECT * FROM electives";

    private static final String SQL__FIND_ELECTIVE_BY_NAME =
            "SELECT * FROM electives WHERE name=?";

    private static final String SQL__FIND_SPORT_ELECTIVES =
            "SELECT * FROM electives WHERE topic_id = 0";

    private static final String SQL__FIND_PROGRAMMING_ELECTIVES =
            "SELECT * FROM electives WHERE topic_id = 1";

    private static final String SQL_UPDATE_ELECTIVES_BY_LECTURER =
            "UPDATE electives SET lecturer_id=? WHERE id=?";

    private static final String SQL_INCREASE_ELECTIVES_STUDENT_COUNT =
            "UPDATE electives SET student_count=student_count+1 WHERE id=?";

    private static final String SQL_DECREASE_ELECTIVES_STUDENT_COUNT =
            "UPDATE electives SET student_count=student_count-1 WHERE id=?";

    private static final String SQL__INSERT_ELECTIVES_HAVE_USERS =
            "INSERT INTO electives_have_users VALUES(?,?,?)";

    public static final String SQL__UPDATE_ELECTIVES_HAVE_USERS =
            "UPDATE electives_have_users SET mark=? WHERE user_id=? AND elective_id=?";

    private static final String SQL__FIND_ELECTIVE_ID =
            "SELECT elective_id FROM electives_have_users WHERE user_id=?";

    private static final String SQL__FIND_ELECTIVES_HAVE_USERS =
            "SELECT * FROM electives_have_users";

    private static final String SQL__DELETE_ELECTIVES_HAVE_USERS =
            "DELETE FROM electives_have_users WHERE user_id=? AND elective_id=?";

    private static final String SQL_FIND_USER_ORDER_BEANS =
            "SELECT  u.id, u.name AS username, e.elective_id, e.mark,"
                    + " el.name, el.end_date, el.topic_id, el.status, el.lecturer_id"
                    + "	FROM users u, electives_have_users e, electives el"
                    + "	WHERE e.user_id=u.id AND e.elective_id=el.id";

    private static final String SQL_FIND_USER_ORDER_BEANS_BY_LECTURER_ID =
            "SELECT  u.id, u.name AS username, e.elective_id, e.mark,"
                    + " el.name, el.end_date, el.topic_id, el.status, el.lecturer_id"
                    + "	FROM users u, electives_have_users e, electives el"
                    + "	WHERE e.user_id=u.id AND e.elective_id=el.id AND lecturer_id=?";

    private static final String SQL_FIND_ELECTIVES_BY_IDS_AND_USER_ID =
            "SELECT e.id, e.name, e.start_date, e.end_date, e.status, e.topic_id, e.lecturer_id, e.student_count, el.mark"
                    + " FROM electives e JOIN electives_have_users el where e.id=elective_id";

    private static final String SQL_Temp =
            "SELECT users.name AS login, roles.name AS rolename, COUNT(elective_id) AS elective_count FROM users\n" +
                    "JOIN electives_have_users ON user_id = users.id\n" +
                    "JOIN roles ON users.role_id = roles.id\n" +
                    "WHERE role_id = ?\n" +
                    "GROUP BY user_id";

    /**
     * Returns elective with a given name.
     *
     * @param name Name of Elective entity
     * @return Elective entity
     * @throws DBException
     */
    public Elective findElectiveByName(String name) throws DBException {
        Elective elective = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        Connection connection = null;
        try {
            connection = DBManager.getInstance().getConnection();
            statement = connection.prepareStatement(SQL__FIND_ELECTIVE_BY_NAME);
            statement.setString(1, name);
            rs = statement.executeQuery();
            if (rs.next()) {
                elective = new Elective();
                elective.setId(rs.getInt("id"));
                elective.setName(rs.getString("name"));
                elective.setStartDate(rs.getDate("start_date"));
                elective.setEndDate(rs.getDate("end_date"));
                elective.setStatus(rs.getString("status"));
                elective.setTopicId(rs.getInt("topic_id"));
                elective.setLecturerId(rs.getInt("lecturer_id"));
                elective.setStudentCount(rs.getByte("student_count"));
            }
            rs.close();
            statement.close();
        } catch (SQLException ex) {
            if (connection != null)
                DBManager.getInstance().rollbackAndClose(connection);
            log.error("Exception in findElectiveByName(): ", ex);
            throw new DBException("Cannot obtain elective with a given name", ex);
        } finally {
            if (connection != null)
                DBManager.getInstance().commitAndClose(connection);
            log.error("Exception in findElectiveByName()");
        }
        return elective;
    }

    /**
     * Returns all electives with a given topic.
     *
     * @param topic Topic selected from the enum
     * @return List of Elective entities
     * @throws DBException
     */
    public List<Elective> findElectivesByTopic(Topic topic) throws DBException {
        List<Elective> electives = new ArrayList<>();
        Statement statement = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = DBManager.getInstance().getConnection();
            statement = con.createStatement();
            if (topic == Topic.SPORT)
                rs = statement.executeQuery(SQL__FIND_SPORT_ELECTIVES);
            else if (topic == Topic.PROGRAMMING)
                rs = statement.executeQuery(SQL__FIND_PROGRAMMING_ELECTIVES);
            while (rs.next()) {
                Elective elective = new Elective();
                elective.setId(rs.getInt("id"));
                elective.setName(rs.getString("name"));
                elective.setStartDate(rs.getDate("start_date"));
                elective.setEndDate(rs.getDate("end_date"));
                elective.setStatus(rs.getString("status"));
                elective.setTopicId(rs.getInt("topic_id"));
                elective.setLecturerId(rs.getInt("lecturer_id"));
                elective.setStudentCount(rs.getByte("student_count"));
                electives.add(elective);
            }
            rs.close();
            statement.close();
        } catch (SQLException ex) {
            if (con != null)
                DBManager.getInstance().rollbackAndClose(con);
            else log.error("Cannot rollback and close: ", ex);
            throw new DBException("Cannot obtain electives list with a given topic", ex);
        } finally {
            if (con != null)
                DBManager.getInstance().commitAndClose(con);
            else log.error("Cannot commit and close");
        }
        return electives;
    }

    /**
     * Returns all electives with a given identifiers list.
     *
     * @param ids Elective identifiers.
     * @return List of elective entities.
     * @throws DBException
     */
    public List<Elective> findElectivesByIds(String[] ids) throws DBException {
        List<Elective> electiveList = new ArrayList<>();
        Statement statement = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = DBManager.getInstance().getConnection();

            // create SQL query like "... id IN (1, 2, 7)"
            StringBuilder query = new StringBuilder(
                    "SELECT * FROM electives WHERE id IN (");
            for (String idAsString : ids) {
                query.append(idAsString).append(',');
            }
            query.deleteCharAt(query.length() - 1);
            query.append(')');

            statement = con.createStatement();
            rs = statement.executeQuery(query.toString());
            while (rs.next()) {
                Elective elective = new Elective();
                elective.setId(rs.getInt("id"));
                elective.setName(rs.getString("name"));
                elective.setStartDate(rs.getDate("start_date"));
                elective.setEndDate(rs.getDate("end_date"));
                elective.setStatus(rs.getString("status"));
                elective.setTopicId(rs.getInt("topic_id"));
                elective.setLecturerId(rs.getInt("lecturer_id"));
                elective.setStudentCount(rs.getByte("student_count"));
                electiveList.add(elective);
            }
            rs.close();
            statement.close();
        } catch (SQLException ex) {
            if (con != null)
                DBManager.getInstance().rollbackAndClose(con);
            else log.error("Cannot rollback and close: ", ex);
            throw new DBException("Cannot obtain electives list with a given identifiers list", ex);
        } finally {
            if (con != null)
                DBManager.getInstance().commitAndClose(con);
            else log.error("Cannot commit and close at findElectivesByIds");
        }
        return electiveList;
    }

    /**
     * Returns all electives.
     *
     * @return List of elective entities.
     * @throws DBException
     */
    public List<Elective> findElectivesByIdsAndUserId() throws DBException {
        List<Elective> electiveList = new ArrayList<>();
        Statement statement = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = DBManager.getInstance().getConnection();
            statement = con.createStatement();
            rs = statement.executeQuery(SQL_FIND_ELECTIVES_BY_IDS_AND_USER_ID);
            while (rs.next()) {
                Elective elective = new Elective();
                elective.setId(rs.getInt("id"));
                elective.setName(rs.getString("name"));
                elective.setStartDate(rs.getDate("start_date"));
                elective.setEndDate(rs.getDate("end_date"));
                elective.setStatus(rs.getString("status"));
                elective.setTopicId(rs.getInt("topic_id"));
                elective.setLecturerId(rs.getInt("lecturer_id"));
                elective.setUserMark(rs.getInt("mark"));
                elective.setStudentCount(rs.getByte("student_count"));
                electiveList.add(elective);
            }
            rs.close();
            statement.close();
        } catch (SQLException ex) {
            if (con != null)
                DBManager.getInstance().rollbackAndClose(con);
            else log.error("Cannot rollback and close: ", ex);
            throw new DBException("Cannot obtain elective list with join", ex);
        } finally {
            if (con != null)
                DBManager.getInstance().commitAndClose(con);
            else log.error("Cannot commit and close at findElectivesByIdsAndUserId");
        }
        return electiveList;
    }

    /**
     * Returns all electives with a given user identifier.
     *
     * @param user_id User entity identifier
     * @return List of electives entities.
     * @throws DBException
     */
    public List<Integer> findElectivesIdByUserId(int user_id) throws DBException {
        List<Integer> usersArrayList = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = DBManager.getInstance().getConnection();
            statement = con.prepareStatement(SQL__FIND_ELECTIVE_ID);
            statement.setInt(1, user_id);
            rs = statement.executeQuery();
            while (rs.next()) {
                Integer integer = rs.getInt("elective_id");
                usersArrayList.add(integer);
            }
            rs.close();
            statement.close();
        } catch (SQLException ex) {
            if (con != null)
                DBManager.getInstance().rollbackAndClose(con);
            else log.error("Cannot rollback and close: ", ex);
            throw new DBException("Cannot obtain integer list with a given user identifier", ex);
        } finally {
            if (con != null)
                DBManager.getInstance().commitAndClose(con);
            else log.error("Cannot commit and close at findElectivesIdByUserId()");
        }
        return usersArrayList;
    }


    /**
     * Returns all electives.
     *
     * @return List of electives entities.
     * @throws DBException
     */
    public List<Elective> findElectives() throws DBException {
        List<Elective> electives = new ArrayList<>();
        Statement statement = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = DBManager.getInstance().getConnection();
            statement = con.createStatement();
            rs = statement.executeQuery(SQL__FIND_ALL_ELECTIVES);
            while (rs.next()) {
                Elective elective = new Elective();
                elective.setId(rs.getInt("id"));
                elective.setName(rs.getString("name"));
                elective.setStartDate(rs.getDate("start_date"));
                elective.setEndDate(rs.getDate("end_date"));
                elective.setStatus(rs.getString("status"));
                elective.setTopicId(rs.getInt("topic_id"));
                elective.setLecturerId(rs.getInt("lecturer_id"));
                elective.setStudentCount(rs.getByte("student_count"));
                electives.add(elective);
            }
            rs.close();
            statement.close();
        } catch (SQLException ex) {
            if (con != null)
                DBManager.getInstance().rollbackAndClose(con);
            else log.error("Cannot rollback and close: ", ex);
            throw new DBException("Cannot obtain elective list", ex);
        } finally {
            if (con != null)
                DBManager.getInstance().commitAndClose(con);
            else log.error("Cannot commit and close at findElectives()");
        }
        return electives;
    }

    /**
     * Deletes electives with a given identifiers list.
     *
     * @param ids Elective identifiers.
     * @throws DBException
     */
    public static void deleteElectivesByIds(String[] ids) throws DBException {
        Statement statement = null;
        Connection con = null;
        try {
            con = DBManager.getInstance().getConnection();

            // create SQL query like "... id IN (1, 2, 7)"
            StringBuilder query = new StringBuilder(
                    "DELETE FROM electives WHERE id IN (");
            for (String idAsString : ids) {
                query.append(idAsString).append(',');
            }
            query.deleteCharAt(query.length() - 1);
            query.append(')');

            statement = con.createStatement();
            statement.execute(query.toString());
            statement.close();
        } catch (SQLException ex) {
            if (con != null)
                DBManager.getInstance().rollbackAndClose(con);
            else log.error("Cannot rollback and close: ", ex);
            throw new DBException("Cannot delete elective list by given electives identifiers", ex);
        } finally {
            if (con != null)
                DBManager.getInstance().commitAndClose(con);
            else log.error("Cannot commit and close at deleteElectivesByIds");
        }
    }

    /**
     * Updates elective.
     *
     * @param electiveId Elective identifier
     * @param lecturerId Lecturer identifier
     * @throws DBException
     */
    public static void updateElectiveByLecturer(int electiveId, int lecturerId) throws DBException {
        PreparedStatement statement = null;
        Connection con = null;
        try {
            con = DBManager.getInstance().getConnection();
            statement = con.prepareStatement(SQL_UPDATE_ELECTIVES_BY_LECTURER);
            int k = 0;
            statement.setInt(++k, lecturerId);
            statement.setInt(++k, electiveId);
            statement.executeUpdate();
            statement.close();
        } catch (SQLException ex) {
            if (con != null)
                DBManager.getInstance().rollbackAndClose(con);
            else log.error("Cannot rollback and close: ", ex);
            throw new DBException("Cannot update elective by lecturer identifier", ex);
        } finally {
            if (con != null)
                DBManager.getInstance().commitAndClose(con);
            else log.error("Cannot commit and close at updateElectiveByLecturer");
        }
    }

    /**
     * Updates elective.
     *
     * @param electiveId Elective identifier
     * @param isIncrease Increase or decrease student count of any elective
     * @throws DBException
     */
    public static void updateElectiveByStudentCount(int electiveId, boolean isIncrease) throws DBException {
        PreparedStatement statement = null;
        Connection con = null;
        try {
            con = DBManager.getInstance().getConnection();
            if (isIncrease)
                statement = con.prepareStatement(SQL_INCREASE_ELECTIVES_STUDENT_COUNT);
            else
                statement = con.prepareStatement(SQL_DECREASE_ELECTIVES_STUDENT_COUNT);
            statement.setInt(1, electiveId);
            statement.executeUpdate();
            statement.close();
        } catch (SQLException ex) {
            if (con != null)
                DBManager.getInstance().rollbackAndClose(con);
            else log.error("Cannot rollback and close: ", ex);
            throw new DBException("Cannot update elective with a given elective identifier", ex);
        } finally {
            if (con != null)
                DBManager.getInstance().commitAndClose(con);
            else log.error("Cannot commit and close at updateElectivesByLecturer");
        }
    }

    /**
     * Returns ElectivesHaveUsers list.
     *
     * @return List of ElectivesHaveUsers entities
     * @throws DBException
     */
    public List<ElectivesHaveUsers> findElectivesHaveUsers() throws DBException {
        List<ElectivesHaveUsers> electivesHaveUsers = new ArrayList<>();
        Statement statement = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = DBManager.getInstance().getConnection();
            statement = con.createStatement();
            rs = statement.executeQuery(SQL__FIND_ELECTIVES_HAVE_USERS);
            while (rs.next()) {
                ElectivesHaveUsers haveUsers = new ElectivesHaveUsers();
                haveUsers.setUserId(rs.getInt("user_id"));
                haveUsers.setElectiveId(rs.getInt("elective_id"));
                haveUsers.setMark(rs.getInt("mark"));
                electivesHaveUsers.add(haveUsers);
            }
            rs.close();
            statement.close();
        } catch (SQLException ex) {
            if (con != null)
                DBManager.getInstance().rollbackAndClose(con);
            else log.error("Cannot rollback and close: ", ex);
            throw new DBException("Cannot obtain electives have users", ex);
        } finally {
            if (con != null)
                DBManager.getInstance().commitAndClose(con);
            else log.error("Cannot commit and close at findElectivesByIds");
        }
        return electivesHaveUsers;
    }

    /**
     * Inserts user_id, elective_id into ElectiveHaveUsers in DB.
     *
     * @param user_id     User identifier
     * @param elective_id Elective identifier
     * @throws DBException
     */
    public static void insertElectivesHaveUsers(int user_id, int elective_id) throws DBException {
        PreparedStatement statement = null;
        Connection con = null;
        try {
            con = DBManager.getInstance().getConnection();
            statement = con.prepareStatement(SQL__INSERT_ELECTIVES_HAVE_USERS);
            int k = 0;
            statement.setInt(++k, user_id);
            statement.setInt(++k, elective_id);
            statement.setInt(++k, 0);
            statement.execute();
            statement.close();
        } catch (SQLException ex) {
            if (con != null)
                DBManager.getInstance().rollbackAndClose(con);
            else log.error("Cannot rollback and close: ", ex);
            throw new DBException("Cannot insert into ElectivesHaveUsers with given user identifier and elective identifier", ex);
        } finally {
            if (con != null)
                DBManager.getInstance().commitAndClose(con);
            else log.error("Cannot commit and close at insertElectivesHaveUsers()");
        }
    }

    /**
     * Updates mark into electives_have_users in DB.
     *
     * @param user_id     User identifier
     * @param elective_id Elective identifier
     * @param userMark    The mark given by the lecturer
     * @throws DBException
     */
    public static void updateElectivesHaveUsers(int user_id, int elective_id, int userMark) throws DBException {
        PreparedStatement statement = null;
        Connection con = null;
        try {
            con = DBManager.getInstance().getConnection();
            statement = con.prepareStatement(SQL__UPDATE_ELECTIVES_HAVE_USERS);
            int k = 0;
            statement.setInt(++k, userMark);
            statement.setInt(++k, user_id);
            statement.setInt(++k, elective_id);
            statement.executeUpdate();
            statement.close();
        } catch (SQLException ex) {
            if (con != null)
                DBManager.getInstance().rollbackAndClose(con);
            else log.error("Cannot rollback and close: ", ex);
            throw new DBException("Cannot update ElectivesHaveUsers ", ex);
        } finally {
            if (con != null)
                DBManager.getInstance().commitAndClose(con);
            else log.error("Cannot commit and close at updateElectivesHaveUsers()");
        }
    }

    /**
     * Deletes electives_have_users entity.
     *
     * @param user_id     User identifier
     * @param elective_id Elective identifier
     * @throws DBException
     */
    public static void deleteElectivesHaveUsers(int user_id, int elective_id) throws DBException {
        PreparedStatement statement = null;
        Connection con = null;
        try {
            con = DBManager.getInstance().getConnection();
            statement = con.prepareStatement(SQL__DELETE_ELECTIVES_HAVE_USERS);
            int k = 0;
            statement.setInt(++k, user_id);
            statement.setInt(++k, elective_id);
            statement.executeUpdate();
            statement.close();
        } catch (SQLException ex) {
            if (con != null)
                DBManager.getInstance().rollbackAndClose(con);
            else log.error("Cannot rollback and close: ", ex);
            throw new DBException("Cannot delete ElectivesHaveUsers ", ex);
        } finally {
            if (con != null)
                DBManager.getInstance().commitAndClose(con);
            else log.error("Cannot commit and close at deleteElectivesHaveUsers()");
        }
    }

    /**
     * Returns UserOrderBean list.
     *
     * @return List of UserOrderBean entities
     * @throws DBException
     */
    public List<UserOrderBean> findUserOrderBeans() throws DBException {
        List<UserOrderBean> orderUserBeanList = new ArrayList<>();
        Statement statement = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = DBManager.getInstance().getConnection();
            statement = con.createStatement();
            rs = statement.executeQuery(SQL_FIND_USER_ORDER_BEANS);
            while (rs.next()) {
                UserOrderBean userOrderBean = new UserOrderBean();
                userOrderBean.setUserId(rs.getInt("id"));
                userOrderBean.setUserLogin(rs.getString("username"));
                userOrderBean.setElectiveId(rs.getInt("elective_id"));
                userOrderBean.setUserMark(rs.getInt("mark"));
                userOrderBean.setName(rs.getString("name"));
                userOrderBean.setEndDate(rs.getDate("end_date"));
                userOrderBean.setTopicId(rs.getInt("topic_id"));
                userOrderBean.setStatus(rs.getString("status"));
                userOrderBean.setLecturerId(rs.getInt("lecturer_id"));
                orderUserBeanList.add(userOrderBean);
            }
            rs.close();
            statement.close();
        } catch (SQLException ex) {
            if (con != null)
                DBManager.getInstance().rollbackAndClose(con);
            else log.error("Cannot rollback and close: ", ex);
            throw new DBException("Cannot obtain user order bean ", ex);
        } finally {
            if (con != null)
                DBManager.getInstance().commitAndClose(con);
            else log.error("Cannot commit and close at findUserOrderBeans()");
        }
        return orderUserBeanList;
    }

    /**
     * Returns UserOrderBean with a given lecturer identifier.
     *
     * @param lecturerId Lecturer identifier
     * @return List of UserOrderBean entities
     * @throws DBException
     */
    public List<UserOrderBean> findUserOrderBeansByLecturerId(int lecturerId) throws DBException {
        List<UserOrderBean> orderUserBeanList = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = DBManager.getInstance().getConnection();
            statement = con.prepareStatement(SQL_FIND_USER_ORDER_BEANS_BY_LECTURER_ID);
            statement.setInt(1, lecturerId);
            rs = statement.executeQuery();
            while (rs.next()) {
                UserOrderBean userOrderBean = new UserOrderBean();
                userOrderBean.setUserId(rs.getInt("id"));
                userOrderBean.setUserLogin(rs.getString("username"));
                userOrderBean.setElectiveId(rs.getInt("elective_id"));
                userOrderBean.setUserMark(rs.getInt("mark"));
                userOrderBean.setName(rs.getString("name"));
                userOrderBean.setEndDate(rs.getDate("end_date"));
                userOrderBean.setTopicId(rs.getInt("topic_id"));
                userOrderBean.setStatus(rs.getString("status"));
                userOrderBean.setLecturerId(rs.getInt("lecturer_id"));
                orderUserBeanList.add(userOrderBean);
            }
            rs.close();
            statement.close();
        } catch (SQLException ex) {
            if (con != null)
                DBManager.getInstance().rollbackAndClose(con);
            else log.error("Cannot rollback and close: ", ex);
            throw new DBException("Cannot obtain user order bean with given lecturer identifier ", ex);
        } finally {
            if (con != null)
                DBManager.getInstance().commitAndClose(con);
            else log.error("Cannot commit and close at findUserOrderBeans()");
        }
        return orderUserBeanList;
    }

    public List<Temp> temp(int roleId) throws DBException {
        List<Temp> tempList = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = DBManager.getInstance().getConnection();
            statement = con.prepareStatement(SQL_Temp);
            statement.setInt(1, roleId);
            rs = statement.executeQuery();
            while (rs.next()) {
                Temp temp = new Temp();
                temp.setLogin(rs.getString("login"));
                temp.setRoleName(rs.getString("rolename"));
                temp.setElectiveId(rs.getInt("elective_count"));
                tempList.add(temp);
            }
            rs.close();
            statement.close();
        } catch (SQLException ex) {
            if (con != null)
                DBManager.getInstance().rollbackAndClose(con);
            else log.error("Cannot rollback and close: ", ex);
            throw new DBException("Cannot obtain user order bean with given lecturer identifier ", ex);
        } finally {
            if (con != null)
                DBManager.getInstance().commitAndClose(con);
            else log.error("Cannot commit and close at findUserOrderBeans()");
        }
        return tempList;
    }
}
