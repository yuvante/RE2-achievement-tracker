import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

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
            a.Guide_URL,
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
                        resultSet.getString("Guide_URL"),
                        resultSet.getString("CategoryName")
                );

                achievements.add(achievement);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return achievements;

    }
    public Set<Integer> getCompletedAchievementIds() {

        Set<Integer> completedIds = new HashSet<>();

        String sql = """
            SELECT Achievement_ID
            FROM Progress
            WHERE Completed = 1
            """;

        try (
                Connection connection = DatabaseConnection.connect();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()
        ) {

            while (resultSet.next()) {
                completedIds.add(
                        resultSet.getInt("Achievement_ID")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return completedIds;
    }

    public void setAchievementCompleted(int achievementId, boolean completed) {

        String sql = """
            UPDATE Progress
            SET Completed = ?
            WHERE Achievement_ID = ?
            """;

        try (
                Connection connection = DatabaseConnection.connect();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setInt(1, completed ? 1 : 0);
            statement.setInt(2, achievementId);

            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void resetAllProgress() {

        String sql = """
            UPDATE Progress
            SET Completed = 0
            """;

        try (
                Connection connection = DatabaseConnection.connect();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
