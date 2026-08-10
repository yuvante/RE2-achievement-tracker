import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javafx.geometry.Insets;
import javafx.scene.text.Font;

public class Window extends Application {

    private static final int TOTAL_ACHIEVEMENTS = 44;

    private List<Achievement> allAchievements;
    private VBox achievementsBox;
    private ComboBox<String> categoryFilter;
    private ComboBox<String> difficultyFilter;
    private ComboBox<String> statusFilter;

    private IntegerProperty completed;
    private ProgressBar progressBar;
    private Label progressLabel;

    private final Set<Integer> completedAchievements = new HashSet<>();

    @Override
    public void start(Stage stage) {

        AchievementRepository repository = new AchievementRepository();
        allAchievements = repository.getAllAchievements();
        achievementsBox = new VBox();

        ScrollPane scrollPane = new ScrollPane(achievementsBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefWidth(400);
        scrollPane.getStyleClass().add("achievement-scroll");
        scrollPane.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-background: transparent;"
        );
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setPannable(false);



        achievementsBox.setStyle("-fx-background-color: transparent;");

        BorderPane root = new BorderPane();

        completed = new SimpleIntegerProperty(0);

        progressBar = new ProgressBar(0);
        progressBar.setMaxWidth(Double.MAX_VALUE);
        progressBar.setPrefWidth(1550);
        progressBar.setPrefHeight(35);
        progressBar.setStyle(
                "-fx-accent: #b00020;"
        );

        progressLabel = new Label("Completed: 0/" + TOTAL_ACHIEVEMENTS);
        progressLabel.setPadding(new Insets(5));
        Font requiemFont = Font.loadFont(getClass().getResourceAsStream("/font.ttf"), 17);
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

        Label completionLabel = new Label();
        completionLabel.textProperty().bind(
                Bindings.createStringBinding(
                        () -> Math.round(
                                (double) completed.get() / TOTAL_ACHIEVEMENTS * 100
                        ) + "% OVERALL COMPLETION",
                        completed
                )
        );
        completionLabel.setMaxWidth(Double.MAX_VALUE);
        completionLabel.setAlignment(Pos.CENTER);
        completionLabel.setPadding(new Insets(10));
        completionLabel.setStyle(
                "-fx-text-fill: #b00020;"
        );
        completionLabel.setFont(
                Font.font(requiemFont.getFamily(), 26)
        );


        VBox topPanel = new VBox();
        VBox leftPanel = new VBox();
        leftPanel.getChildren().addAll(
                topPanel,
                scrollPane
        );
        root.setLeft(leftPanel);
        leftPanel.setPrefWidth(450);
        leftPanel.setSpacing(10);
        leftPanel.setPadding(new Insets(10));
        leftPanel.getStyleClass().add("left-panel");
        topPanel.prefHeightProperty()
                .bind(leftPanel.heightProperty().multiply(0.25));
        scrollPane.prefHeightProperty()
                .bind(leftPanel.heightProperty().multiply(0.75));
        topPanel.getChildren().addAll(
                logoView,
                completionLabel
        );
        topPanel.setAlignment(Pos.TOP_CENTER);

        Label categoryLabel = new Label("CATEGORY");
        categoryFilter = new ComboBox<>();
        categoryFilter.getItems().addAll(
                "All",
                "Story",
                "Equipment",
                "Combat",
                "Exploration",
                "Easter Egg"
        );
        categoryFilter.setValue("All");
        VBox categoryBox = new VBox(3);

        categoryBox.getChildren().addAll(
                categoryLabel,
                categoryFilter
        );
        HBox filterRow = new HBox(15);
        filterRow.getChildren().add(categoryBox);
        topPanel.getChildren().add(filterRow);
        categoryLabel.setStyle(
                "-fx-text-fill: white;"
        );
        categoryLabel.setFont(
                Font.font(requiemFont.getFamily(), 17)
        );

        Label difficultyLabel = new Label("DIFFICULTY");
        difficultyFilter = new ComboBox<>();
        difficultyFilter.getItems().addAll( "All",
                "★☆☆☆☆",
                "★★☆☆☆",
                "★★★☆☆",
                "★★★★☆",
                "★★★★★"
        );
        difficultyFilter.setValue("All");
        VBox difficultyBox = new VBox(5);
        difficultyBox.getChildren().addAll(
                difficultyLabel,
                difficultyFilter
        );
        filterRow.getChildren().add(difficultyBox);
        difficultyLabel.setStyle(
                "-fx-text-fill: white;"
        );
        difficultyLabel.setFont(
                Font.font(requiemFont.getFamily(), 17)
        );

        Label statusLabel = new Label("STATUS");
        statusFilter = new ComboBox<>();
        statusFilter.getItems().addAll("All","Completed", "Missing");
        statusFilter.setValue("All");
        VBox statusBox = new VBox(5);
        statusBox.getChildren().addAll(statusLabel, statusFilter);
        filterRow.getChildren().add(statusBox);
        statusLabel.setStyle(
                "-fx-text-fill: white;"
        );
        statusLabel.setFont(
                Font.font(requiemFont.getFamily(), 17)
        );

        allAchievements = repository.getAllAchievements();

        categoryFilter.setOnAction(event -> applyFilters());
        difficultyFilter.setOnAction(event -> applyFilters());
        statusFilter.setOnAction(event -> applyFilters());

        filterRow.setAlignment(Pos.CENTER);

        for (Achievement achievement : allAchievements) {
                displayAchievement(achievement);
        }

        Scene scene = new Scene(backgroundPane, 1550, 800);
        stage.setResizable(false);
        scene.getStylesheets().add(
                getClass().getResource("/style.css").toExternalForm());
        stage.setTitle("Resident Evil 2 Achievement Tracker");
        stage.setScene(scene);
        stage.show();
    }

    private void displayAchievement(Achievement achievement) {
        CheckBox checkBox = new CheckBox();
        checkBox.setSelected(
                completedAchievements.contains(achievement.getId())
        );

        Label achievementLabel = new Label(
                achievement.getId()
                        + " "
                        + achievement.getName()
                        + " | "
                        + achievement.getDescription()
        );

        HBox achievementRow = new HBox(
                6,
                checkBox,
                achievementLabel
        );

        achievementsBox.getChildren().add(achievementRow);

        checkBox.setOnAction(event -> {
            if (checkBox.isSelected()) {
                completedAchievements.add(achievement.getId());
            } else {
                completedAchievements.remove(achievement.getId());
            }

            completed.set(completedAchievements.size());

            progressBar.setProgress(
                    (double) completed.get() / TOTAL_ACHIEVEMENTS
            );

            progressLabel.setText(
                    "Completed: " + completed.get() + "/" + TOTAL_ACHIEVEMENTS
            );
        });
    }

    private void applyFilters() {

        achievementsBox.getChildren().clear();

        String selectedCategory = categoryFilter.getValue();
        String selectedDifficulty = difficultyFilter.getValue();
        String selectedStatus = statusFilter.getValue();

        int selectedDifficultyNumber = switch (selectedDifficulty) {
            case "★☆☆☆☆" -> 1;
            case "★★☆☆☆" -> 2;
            case "★★★☆☆" -> 3;
            case "★★★★☆" -> 4;
            case "★★★★★" -> 5;
            default -> 0;
        };

        for (Achievement achievement : allAchievements) {

            boolean categoryMatches =
                    selectedCategory.equals("All")
                            || achievement.getCategory().equals(selectedCategory);

            boolean difficultyMatches =
                    selectedDifficulty.equals("All")
                            || achievement.getDifficulty() == selectedDifficultyNumber;

            boolean isCompleted =
                    completedAchievements.contains(achievement.getId());

            boolean statusMatches =
                    selectedStatus.equals("All")
                            || (selectedStatus.equals("Completed") && isCompleted)
                            || (selectedStatus.equals("Missing") && !isCompleted);

            if (categoryMatches && difficultyMatches && statusMatches) {
                displayAchievement(achievement);
            }
        }
    }
}