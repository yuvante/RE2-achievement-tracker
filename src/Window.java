import javafx.application.Application;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.awt.*;
import javafx.geometry.Insets;
import javafx.scene.text.Font;

public class Window extends Application {

    private static final int TOTAL_ACHIEVEMENTS = 44;

    @Override
    public void start(Stage stage) {

        AchievementRepository repository = new AchievementRepository();

        VBox achievementsBox = new VBox();

        ScrollPane scrollPane = new ScrollPane(achievementsBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefWidth(400);
        scrollPane.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-background: transparent;"
        );


        achievementsBox.setStyle("-fx-background-color: transparent;");

        BorderPane root = new BorderPane();

        IntegerProperty completed = new SimpleIntegerProperty(0);

        ProgressBar progressBar = new ProgressBar(0);
        progressBar.setMaxWidth(Double.MAX_VALUE);
        progressBar.setPrefWidth(1550);
        progressBar.setPrefHeight(35);
        progressBar.setStyle(
                "-fx-accent: #b00020;"
        );

        Label progressLabel = new Label("0/" + TOTAL_ACHIEVEMENTS);
        Font requiemFont = Font.loadFont(getClass().getResourceAsStream("/font.ttf"), 20);
        progressLabel.setFont(requiemFont);
        progressLabel.getStyleClass().add("progress-label");

        StackPane progressPane = new StackPane(
                progressBar,
                progressLabel
        );
        progressPane.setPrefHeight(30);
        progressPane.setMaxHeight(30);
        progressBar.setMaxWidth(Double.MAX_VALUE);
        progressBar.setMaxHeight(Double.MAX_VALUE);

        root.setBottom(progressPane);
        root.setStyle("-fx-background-color: transparent;");

        Media background = new Media(getClass().getResource("/re2.mp4").toExternalForm());
        MediaPlayer  mediaPlayer = new MediaPlayer(background);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.play();
        MediaView  mediaView = new MediaView(mediaPlayer);

        StackPane backgroundPane = new StackPane();

        backgroundPane.getChildren().addAll(
                mediaView,
                root
        );

        backgroundPane.setStyle("-fx-background-color: black;");
        mediaView.fitWidthProperty().bind(backgroundPane.widthProperty());
        mediaView.fitHeightProperty().bind(backgroundPane.heightProperty());
        mediaView.setPreserveRatio(true);

        Image icon = new Image(getClass().getResource("/re2 icon.png").toExternalForm());
        stage.getIcons().add(icon);

        Image logo = new Image(getClass().getResource("/re2 logo.png").toExternalForm());
        ImageView logoView = new ImageView(logo);
        logoView.setFitWidth(450);
        logoView.setPreserveRatio(true);

        VBox leftPanel = new VBox();
        leftPanel.getChildren().addAll(
                logoView,
                scrollPane
        );
        root.setLeft(leftPanel);
        leftPanel.setPrefWidth(450);
        leftPanel.setSpacing(20);
        leftPanel.setPadding(new Insets(10));

        //will later add search bar, filtering and sorting (category and difficulty)

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
            achievementLabel.setFont(Font.font("Lucida Sans Typewriter", 13));

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

        Scene scene = new Scene(backgroundPane, 1550, 800);
        stage.setResizable(false);
        scene.getStylesheets().add(
                getClass().getResource("/style.css").toExternalForm());
        stage.setTitle("Resident Evil 2 | Achievement Tracker");
        stage.setScene(scene);
        stage.show();
    }

}