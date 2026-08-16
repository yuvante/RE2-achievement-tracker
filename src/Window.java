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
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.image.Image;
import javafx.scene.text.FontWeight;
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
    private TextField searchField;

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
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

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
        Font requiemFont = Font.loadFont(getClass().getResourceAsStream("/font.ttf"), 18);
        progressLabel.setFont(requiemFont);
        progressLabel.getStyleClass().add("progress-label");
        StackPane.setAlignment(progressLabel, Pos.TOP_CENTER);
        progressLabel.setPadding(new Insets(0, 0, 2, 0));

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
        leftPanel.setSpacing(5);
        leftPanel.setPadding(new Insets(5, 5, 5, 5));
        leftPanel.getStyleClass().add("left-panel");
        topPanel.getChildren().addAll(
                logoView,
                completionLabel
        );
        topPanel.setAlignment(Pos.TOP_CENTER);

        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        scrollPane.setMaxHeight(Double.MAX_VALUE);;

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

        allAchievements = repository.getAllAchievements();

        categoryFilter.setOnAction(event -> applyFilters());
        difficultyFilter.setOnAction(event -> applyFilters());
        statusFilter.setOnAction(event -> applyFilters());

        filterRow.setAlignment(Pos.CENTER);

        searchField = new TextField();
        searchField.setPromptText("Search achievements...");
        searchField.setFont(Font.font("Requiem 9", 16));
        topPanel.getChildren().add(searchField);
        VBox.setMargin(searchField, new Insets(10, 0, 0, 0));
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            applyFilters();
        });
        String searchText = searchField.getText().toLowerCase();
        searchField.getStyleClass().add("search-field");

        categoryFilter.getStyleClass().add("filter-combo");
        difficultyFilter.getStyleClass().add("filter-combo");
        statusFilter.getStyleClass().add("filter-combo");

        categoryLabel.setFont(Font.font(requiemFont.getFamily(), 16));
        difficultyLabel.setFont(Font.font(requiemFont.getFamily(), 16));
        statusLabel.setFont(Font.font(requiemFont.getFamily(), 16));
        categoryBox.setAlignment(Pos.CENTER);
        difficultyBox.setAlignment(Pos.CENTER);
        statusBox.setAlignment(Pos.CENTER);

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

        Label nameLabel = new Label(achievement.getName());

        Label categoryLabel = new Label(
                achievement.getCategory().toUpperCase()
        );
        Label descriptionLabel = new Label(
                achievement.getDescription()
        );

        VBox achievementInfo = new VBox();
        VBox.setMargin(descriptionLabel, new Insets(5, 0, 0, 0));

        String stars =
                "★".repeat(achievement.getDifficulty())
                        + "☆".repeat(5 - achievement.getDifficulty());

        Label difficultyLabel = new Label(stars);
        difficultyLabel.setStyle("-fx-text-fill: white;");

        achievementInfo.getChildren().addAll(
                nameLabel,
                categoryLabel,
                difficultyLabel,
                descriptionLabel
        );
        HBox achievementRow = new HBox(
                checkBox,
                achievementInfo
        );

        achievementInfo.setSpacing(1);
        achievementRow.setSpacing(10);
        achievementRow.setPadding(new Insets(7, 8, 7, 8));
        achievementRow.setAlignment(Pos.TOP_LEFT);
        checkBox.setTranslateY(6);

        nameLabel.setFont(Font.font("IBM Plex Sans",FontWeight.BOLD, 20));
        categoryLabel.setFont(Font.font("IBM Plex Sans",FontWeight.BOLD, 12));
        difficultyLabel.setFont(Font.font("IBM Plex Sans", FontWeight.BOLD, 11));
        descriptionLabel.setFont(Font.font("IBM Plex Sans", 16));

        nameLabel.setStyle("-fx-text-fill: white;");
        categoryLabel.setStyle("-fx-text-fill: white;");
        descriptionLabel.setStyle("-fx-text-fill: white;");
        descriptionLabel.setWrapText(true);
        descriptionLabel.setMaxWidth(360);
        checkBox.getStyleClass().add("achievement-checkbox");

        achievementsBox.getChildren().add(achievementRow);

        achievementRow.getStyleClass().add("achievement-row");

        if (completedAchievements.contains(achievement.getId())) {
            achievementRow.getStyleClass().add("completed");
        }

        checkBox.setOnAction(event -> {
            if (checkBox.isSelected()) {
                completedAchievements.add(achievement.getId());
                achievementRow.getStyleClass().add("completed");
            } else {
                completedAchievements.remove(achievement.getId());
                achievementRow.getStyleClass().remove("completed");
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
        String searchText = searchField.getText().toLowerCase();

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

            boolean searchMatches =
                    achievement.getName().toLowerCase().contains(searchText)
                            || achievement.getDescription().toLowerCase().contains(searchText);

            if (categoryMatches
                    && difficultyMatches
                    && statusMatches
                    && searchMatches) {

                displayAchievement(achievement);
            }
        }
    }
}