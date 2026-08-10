import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AchievementRepository {

    public List<Achievement> getAllAchievements() {
        List<Achievement> achievements = new ArrayList<>();

        String sql = """
        SELECT
            a.ID,
            a.Name,
            a.Description,
            a.Difficulty,
            a.Hint,
            c.Name AS CategoryName
        FROM Achievement a
        JOIN Category c
            ON a.Category_ID = c.ID
        ORDER BY a.ID
        """;

        try (
                Connection connection = DatabaseConnection.connect();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()
        ) {
            while (resultSet.next()) {
                Achievement achievement = new Achievement(
                        resultSet.getInt("ID"),
                        resultSet.getString("Name"),
                        resultSet.getString("Description"),
                        resultSet.getInt("Difficulty"),
                        resultSet.getString("Hint"),
                        resultSet.getString("CategoryName")
                );

                achievements.add(achievement);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return achievements;

    }
}
