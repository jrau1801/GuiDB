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

}
