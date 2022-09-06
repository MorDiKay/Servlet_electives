package epam.db;

import epam.exceptions.DBException;
import org.apache.log4j.Logger;

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

    DBManager() throws DBException {

        /*try {
            Connection con = getConnection();
            String createDbScript = String.valueOf(getClass().getClassLoader().getResourceAsStream("dbcreate-mysql.sql"));
            con.createStatement().execute(createDbScript);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }*/

        /*try {
            driver = DriverManager.getDriver("com.mysql.cj.jdbc.Driver");

//            Context initContext = new InitialContext();
//            Context envContext = (Context) initContext.lookup("java:comp/env");
//            // Elective - the name of data source
//            driver = (DataSource) envContext.lookup("jdbc/Elective");
//            log.trace("Data source ==> " + driver);
        } catch (SQLException ex) {
            log.error("Cannot obtain the data source", ex);
            throw new DBException("Cannot obtain the data source", ex);
        }*/
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
            //con = driver.connect("jdbc:h2:~/final-project", new Properties());
        } catch (SQLException | ClassNotFoundException ex) {
            log.error("Cannot obtain a connection from the pool", ex);
            throw new DBException("Cannot obtain a connection from the pool", ex);
        }
        return con;
    }


    // //////////////////////////////////////////////////////////
    // DB util methods
    // //////////////////////////////////////////////////////////

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

/**************** THIS METHOD IS NOT USED IN THE PROJECT *******/
    /**
     * Returns a DB connection. This method is just for a example how to use the
     * DriverManager to obtain a DB connection. It does not use a pool
     * connections and not used in this project. It is preferable to use
     * {@link #getConnection()} method instead.
     *
     * @return A DB connection.
     */
    public Connection getConnectionWithDriverManager() throws SQLException {
        Connection connection = DriverManager
                .getConnection("jdbc:mysql://localhost:3306/client?serverTimezone=UTC", "root", "root");
        connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        connection.setAutoCommit(false);
        return connection;
    }
/**************************************************************/

}
