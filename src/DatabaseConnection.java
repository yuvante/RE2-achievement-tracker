import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final Path DATABASE_PATH = Path.of(
            System.getenv("LOCALAPPDATA"),
            "RE2AchievementTracker",
            "re2_achievements.sqlite"
    );

    public static Connection connect() throws SQLException {
        prepareDatabase();

        return DriverManager.getConnection(
                "jdbc:sqlite:" + DATABASE_PATH
        );
    }

    private static void prepareDatabase() {

        if (Files.exists(DATABASE_PATH)) {
            return;
        }

        try {
            Files.createDirectories(DATABASE_PATH.getParent());

            InputStream databaseFile =
                    DatabaseConnection.class.getResourceAsStream(
                            "/re2_achievements.sqlite"
                    );

            Files.copy(
                    databaseFile,
                    DATABASE_PATH,
                    StandardCopyOption.REPLACE_EXISTING
            );

        } catch (IOException exception) {
            throw new RuntimeException(
                    "Could not create database",
                    exception
            );
        }
    }
}