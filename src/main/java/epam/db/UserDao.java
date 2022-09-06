package epam.db;

import epam.exceptions.DBException;
import org.apache.log4j.Logger;

import epam.db.entities.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 * Data access object for User entity.
 */
public class UserDao {

    static Logger log = Logger.getLogger(UserDao.class);

    private static final String SQL_FIND_USERS_BY_LOGIN_AND_PASSWORD_MD_5 =
            "SELECT * FROM users WHERE name=? AND password=?";

    private static final String SQL_FIND_USERS_BY_LOGIN =
            "SELECT * FROM users WHERE name=?";

    private static final String SQL_FIND_USER_BY_ID =
            "SELECT * FROM users WHERE id=?";

    private static final String SQL_FIND_USER_BY_ROLE_ID =
            "SELECT * FROM users WHERE role_id=?";

    private static final String SQL_UPDATE_USER =
            "UPDATE users SET name=?, password=?" +
                    "	WHERE id=?";
    private static final String SQL_UPDATE_USER_BY_IS_BLOCKED =
            "UPDATE users SET is_blocked=? WHERE name=?";

    /**
     * Returns a user with the given identifier.
     *
     * @param id User identifier.
     * @return User entity.
     * @throws DBException
     */
    public User findUserById(Long id) throws DBException {
        User user = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        Connection connection = null;
        try {
            connection = DBManager.getInstance().getConnection();
            statement = connection.prepareStatement(SQL_FIND_USER_BY_ID);
            statement.setLong(1, id);
            rs = statement.executeQuery();
            if (rs.next()) {
                user = new User();
                user.setLogin(rs.getString("name"));
                user.setPassword(rs.getString("password"));
            }
            rs.close();
            statement.close();
        } catch (SQLException ex) {
            if (connection != null)
                DBManager.getInstance().rollbackAndClose(connection);
            log.error("Cannot rollback and close: ", ex);
            throw new DBException("Cannot obtain user with a given identifier", ex);
        } finally {
            if (connection != null)
                DBManager.getInstance().commitAndClose(connection);
            else
                log.error("Cannot commit and close at findUserById()");
        }
        return user;
    }

    /**
     * Returns a user with the given login.
     *
     * @param name    User login.
     * @param password User password
     * @throws DBException
     */
    public User findUserByLoginAndPassword(String name, String password) throws DBException {
        User user = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        Connection connection = null;
        try {
            connection = DBManager.getInstance().getConnection();
            statement = connection.prepareStatement(SQL_FIND_USERS_BY_LOGIN_AND_PASSWORD_MD_5);
            statement.setString(1, name);
            statement.setString(2, password);
            rs = statement.executeQuery();
            if (rs.next()) {
                user = new User();
                user.setId(rs.getInt("id"));
                user.setLogin(rs.getString("name"));
                user.setPassword(rs.getString("password"));
                user.setRoleId(rs.getInt("role_id"));
                user.setBlocked(rs.getBoolean("is_blocked"));
            }
            rs.close();
            statement.close();
        } catch (SQLException ex) {
            if (connection != null)
                DBManager.getInstance().rollbackAndClose(connection);
            else log.error("Cannot rollback and close: ", ex);
            throw new DBException("Cannot obtain a user with a given login and password" + ex.getMessage(), ex);

        } finally {
            if (connection != null)
                DBManager.getInstance().commitAndClose(connection);
            else log.error("Cannot commit and close at findUserByLogin()");
        }
        return user;
    }

    /**
     * Returns a user with the given login.
     *
     * @param name User login.
     * @throws DBException
     */
    public User findUserByLogin(String name) throws DBException {
        User user = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        Connection connection = null;
        try {
            connection = DBManager.getInstance().getConnection();
            statement = connection.prepareStatement(SQL_FIND_USERS_BY_LOGIN);
            statement.setString(1, name);
            rs = statement.executeQuery();
            if (rs.next()) {
                user = new User();
                user.setId(rs.getInt("id"));
                user.setLogin(rs.getString("name"));
                user.setPassword(rs.getString("password"));
                user.setRoleId(rs.getInt("role_id"));
                user.setBlocked(rs.getBoolean("is_blocked"));
            }
            rs.close();
            statement.close();
        } catch (SQLException ex) {
            if (connection != null)
                DBManager.getInstance().rollbackAndClose(connection);
            else log.error("Cannot rollback and close: ", ex);
            throw new DBException("Cannot obtain user with a given login", ex);
        } finally {
            if (connection != null)
                DBManager.getInstance().commitAndClose(connection);
            else log.error("Cannot commit and close at findUserByLogin()");
        }
        return user;
    }

    /**
     * Returns a user with the given role_id.
     *
     * @param role_id User role identifier
     * @return User entity.
     * @throws DBException
     */
    public User findUserByRole(int role_id) throws DBException {
        User user = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        Connection connection = null;
        try {
            connection = DBManager.getInstance().getConnection();
            statement = connection.prepareStatement(SQL_FIND_USER_BY_ROLE_ID);
            statement.setInt(1, role_id);
            rs = statement.executeQuery();
            if (rs.next()) {
                user = new User();
                user.setId(rs.getInt("id"));
                user.setLogin(rs.getString("name"));
                user.setPassword(rs.getString("password"));
                user.setRoleId(rs.getInt("role_id"));
                user.setBlocked(rs.getBoolean("is_blocked"));
            }
            rs.close();
            statement.close();
        } catch (SQLException ex) {
            if (connection != null)
                DBManager.getInstance().rollbackAndClose(connection);
            else log.error("Cannot rollback and close: ", ex);
            throw new DBException("Cannot obtain user with a given role identifier", ex);
        } finally {
            if (connection != null)
                DBManager.getInstance().commitAndClose(connection);
            else log.error("Cannot commit and close at findUserByLogin()");
        }
        return user;
    }

    /**
     * Returns a list of users with the given role_id.
     *
     * @param role_id User role identifier
     * @return User list entity.
     * @throws DBException
     */
    public List<User> findUsersByRole(int role_id) throws DBException {
        List<User> users = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet rs = null;
        Connection connection = null;
        try {
            connection = DBManager.getInstance().getConnection();
            statement = connection.prepareStatement(SQL_FIND_USER_BY_ROLE_ID);
            statement.setInt(1, role_id);
            rs = statement.executeQuery();
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setLogin(rs.getString("name"));
                user.setPassword(rs.getString("password"));
                user.setRoleId(rs.getInt("role_id"));
                user.setBlocked(rs.getBoolean("is_blocked"));
                users.add(user);
            }
            rs.close();
            statement.close();
        } catch (SQLException ex) {
            if (connection != null)
                DBManager.getInstance().rollbackAndClose(connection);
            else log.error("Cannot rollback and close: ", ex);
            throw new DBException("Cannot obtain user list with a given role identifier", ex);
        } finally {
            if (connection != null)
                DBManager.getInstance().commitAndClose(connection);
            else log.error("Cannot commit and close at findUsersByLogin()");
        }
        return users;
    }

    /**
     * Update users.
     *
     * @param userLogin Login of user to update.
     * @param isBlocked is the user blocked
     * @throws DBException
     */
    public static void updateUserByIsBlocked(String userLogin, boolean isBlocked) throws DBException {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = DBManager.getInstance().getConnection();
            statement = connection.prepareStatement(SQL_UPDATE_USER_BY_IS_BLOCKED);
            statement.setBoolean(1, isBlocked);
            statement.setString(2, userLogin);
            statement.executeUpdate();
            statement.close();
        } catch (SQLException ex) {
            if (connection != null)
                DBManager.getInstance().rollbackAndClose(connection);
            log.error("Cannot rollback and close: ", ex);
            throw new DBException("Cannot update user with a given login and isBlocked", ex);
        } finally {
            if (connection != null)
                DBManager.getInstance().commitAndClose(connection);
            else log.error("Cannot commit and close at updateUser()");
        }
    }

    /**
     * Updates users.
     *
     * @param user user to update.
     * @throws DBException
     */
    public void updateUser(User user) throws DBException {
        Connection connection = null;
        try {
            connection = DBManager.getInstance().getConnection();
            updateUser(connection, user);
        } catch (SQLException ex) {
            if (connection != null)
                DBManager.getInstance().rollbackAndClose(connection);
            log.error("Cannot rollback and close: ", ex);
            throw new DBException("Cannot update user", ex);
        } finally {
            if (connection != null)
                DBManager.getInstance().commitAndClose(connection);
            else log.error("Cannot commit and close at updateUser()");
        }
    }

    /**
     * Update users.
     *
     * @param user user to update.
     * @throws SQLException
     */
    public void updateUser(Connection con, User user) throws SQLException {
        PreparedStatement statement = con.prepareStatement(SQL_UPDATE_USER);
        int k = 1;
        statement.setString(k++, user.getLogin());
        statement.setString(k++, user.getPassword());
        statement.setLong(k, user.getId());
        statement.executeUpdate();
        statement.close();
    }
}