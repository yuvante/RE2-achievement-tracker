import javafx.application.Application;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Window extends Application {

    private static final int TOTAL_ACHIEVEMENTS = 44;

    @Override
    public void start(Stage stage) {

        AchievementRepository repository = new AchievementRepository();

        VBox achievementsBox = new VBox();

        ScrollPane scrollPane = new ScrollPane(achievementsBox);
        scrollPane.setFitToWidth(true);

        BorderPane root = new BorderPane();
        root.setCenter(scrollPane);

        IntegerProperty completed = new SimpleIntegerProperty(0);

        ProgressBar progressBar = new ProgressBar(0);
        progressBar.setMaxWidth(Double.MAX_VALUE);

        Label progressLabel = new Label("0 / " + TOTAL_ACHIEVEMENTS);

        StackPane progressPane = new StackPane(
                progressBar,
                progressLabel
        );

        root.setBottom(progressPane);

        for (Achievement achievement : repository.getAllAchievements()) {

            CheckBox checkBox = new CheckBox();

            Label achievementLabel = new Label(
                    achievement.getId()
                            + " | "
                            + achievement.getName()
                            + " | Difficulty: "
                            + achievement.getDifficulty()
                            + " | "
                            + achievement.getDescription()
                            + " | "
                            + achievement.getHint()
            );

            HBox achievementRow = new HBox(
                    6,
                    checkBox,
                    achievementLabel
            );

            achievementsBox.getChildren().add(achievementRow);

            checkBox.setOnAction(event -> {

                if (checkBox.isSelected()) {
                    completed.set(completed.get() + 1);
                } else {
                    completed.set(completed.get() - 1);
                }

                progressBar.setProgress(
                        (double) completed.get() / TOTAL_ACHIEVEMENTS
                );

                progressLabel.setText(
                        completed.get() + " / " + TOTAL_ACHIEVEMENTS
                );
            });
        }

        Scene scene = new Scene(root, 700, 700);

        stage.setTitle("Resident Evil 2 | Achievement Tracker");
        stage.setScene(scene);
        stage.show();
    }
}