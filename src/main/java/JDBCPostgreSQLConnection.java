import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCPostgreSQLConnection {

    private final String jdbcURL = "jdbc:postgresql://localhost:5432/WestKanyeRest";
    private final String userName = "postgres";
    private final String password = "1234";


    public Connection connect()  {

        try {
            return DriverManager.getConnection(jdbcURL, userName, password);

        } catch (SQLException e) {
            System.out.println("SQLException occur: " + e);
        }
        return null;
    }
}
