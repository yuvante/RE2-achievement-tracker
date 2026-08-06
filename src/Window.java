import javafx.application.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.*;

import java.io.IOException;

public class Window extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        AchievementRepository repository = new AchievementRepository();

        VBox achievementsBox = new VBox();

        for (Achievement achievement : repository.getAllAchievements()) {


        Label label = new Label(
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
            achievementsBox.getChildren().add(label);
    }
        Scene scene = new Scene(achievementsBox, 1000, 800);
        stage.setScene(scene);
        stage.setTitle("Resident Evil 2 | Achievement Tracker");
        stage.show();

    }
}
