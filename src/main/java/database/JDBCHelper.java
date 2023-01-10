package database;



import java.sql.*;


public class JDBCHelper {


    public JDBCHelper() {

    }

    /**
     * Returns Connection to the database
     *
     * @return Returns a Connection Object with connection with the database
     */
    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:oracle:thin:@rs03-db-inf-min.ad.fh-bielefeld.de:1521:ORCL",
                    "agerikhanov","orcPW2022");
        } catch (SQLException e) {
            System.out.println("Verbindung zur Datenbank konnte nicht hergestellt werden");
        }

        return connection;
    }

    /**
     * Closes Connection to database
     *
     * @param con Open Connection to the Database
     */
    public static void closeConnection(Connection con) {
        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {

            }
        }
    }

    /**
     * Closes given PreparedStatement object
     *
     * @param stmt given prepared statement
     */
    public static void closePreparedStatement(PreparedStatement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {

            }
        }
    }

    /**
     * Closes given ResultSet
     *
     * @param rs Given ResultSet
     */
    public static void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {

            }
        }
    }
}
