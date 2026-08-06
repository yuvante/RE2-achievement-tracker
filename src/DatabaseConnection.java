import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL = "jdbc:sqlite:C:/Users/kinga/DataGripProjects/achievements database/dupa_leona.sqlite";

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL);
    }
}
