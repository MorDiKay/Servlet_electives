package epam.db;

import epam.exceptions.DBException;
import org.apache.log4j.Logger;

import java.io.Closeable;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;


/**
 * DB manager. Works with MySQL DB.
 * Only the required DAO methods are defined!
 */
public class DBManager {

    private static final Logger log = Logger.getLogger(DBManager.class);
    private Driver driver;
    // //////////////////////////////////////////////////////////
    // singleton
    // //////////////////////////////////////////////////////////

    private static DBManager instance;

    public static synchronized DBManager getInstance() throws DBException {
        if (instance == null)
            instance = new DBManager();
        return instance;
    }

    DBManager() {

    }

    /**
     * Returns a DB connection from the Pool Connections. Before using this
     * method you must configure the Date Source and the Connections Pool in your
     * WEB_APP_ROOT/META-INF/context.xml file.
     *
     * @return A DB connection.
     */
    public Connection getConnection() throws DBException {
        Connection con = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager
                    .getConnection("jdbc:mysql://localhost:3306/client?serverTimezone=UTC", "root", "root");
            con.setAutoCommit(false);
        } catch (SQLException | ClassNotFoundException ex) {
            log.error("Cannot obtain a connection from the pool", ex);
            throw new DBException("Cannot obtain a connection from the pool", ex);
        }
        return con;
    }


    /**
     * Commits and close the given connection.
     *
     * @param con Connection to be committed and closed.
     */
    public void commitAndClose(Connection con) {
        try {
            if (!con.isClosed()) {
                con.commit();
                con.close();
            }
        } catch (SQLException ex) {
            log.error("Cannot commit and close a connection" + ex);
        }
    }

    /**
     * Rollbacks and close the given connection.
     *
     * @param con Connection to be rollbacked and closed.
     */
    public void rollbackAndClose(Connection con) {
        try {
            if (!con.isClosed()) {
                con.rollback();
                con.close();
            }
        } catch (SQLException ex) {
            log.error("Cannot rollback and close a connection" + ex);
        }
    }

}
