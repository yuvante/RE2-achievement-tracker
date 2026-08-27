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
import javafx.scene.image.Image;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javafx.geometry.Insets;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class Window extends Application {

    private static final int TOTAL_ACHIEVEMENTS = 44;

    private List<Achievement> allAchievements;
    private VBox achievementsBox;
    private ComboBox<String> categoryFilter;
    private ComboBox<String> difficultyFilter;
    private ComboBox<String> statusFilter;
    private TextField searchField;
    private MediaPlayer musicPlayer;
    private MediaPlayer rainPlayer;
    private MediaPlayer checkSound;
    private boolean soundEffectsEnabled = true;
    private IntegerProperty completed;
    private ProgressBar progressBar;
    private Label progressLabel;
    private AchievementRepository repository;

    private final Set<Integer> completedAchievements = new HashSet<>();

    @Override
    public void start(Stage stage) {

        Media music = new Media(
                getClass().getResource("re2 safe room music.mp3").toExternalForm()
        );
        Media rain = new Media(
                getClass().getResource("heavy storm rain loop.wav").toExternalForm()
        );

        musicPlayer = new MediaPlayer(music);
        rainPlayer = new MediaPlayer(rain);
        musicPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        rainPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        musicPlayer.setVolume(0.2);
        rainPlayer.setVolume(0.1);
        musicPlayer.play();
        rainPlayer.play();

        Media sfx = new Media(
                getClass().getResource("typewriter sound.wav").toExternalForm()
        );

        checkSound = new MediaPlayer(sfx);
        checkSound.setVolume(0.8);


        repository = new AchievementRepository();
        allAchievements = repository.getAllAchievements();
        completedAchievements.addAll(
                repository.getCompletedAchievementIds()
        );

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
        achievementsBox.setPadding(new Insets(0, 0, 45, 0));
        BorderPane root = new BorderPane();
        completed = new SimpleIntegerProperty(
                completedAchievements.size()
        );

        StackPane overlay = new StackPane();
        overlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.70);");
        overlay.setVisible(false);
        overlay.setManaged(false);

        Button menuButton = new Button();
        Button umbrellaButton = new Button();

        Image settingsImage = new Image(
                getClass().getResource("settings button.png").toExternalForm()
        );

        Image umbrellaImage = new Image(
                getClass().getResource("umbrella.png").toExternalForm()
        );

        ImageView settingsIcon = new ImageView(settingsImage);
        ImageView umbrellaIcon = new ImageView(umbrellaImage);

        settingsIcon.setFitWidth(37);
        settingsIcon.setFitHeight(37);
        settingsIcon.setPreserveRatio(true);

        umbrellaIcon.setFitWidth(37);
        umbrellaIcon.setFitHeight(37);
        umbrellaIcon.setPreserveRatio(true);

        menuButton.setGraphic(settingsIcon);
        umbrellaButton.setGraphic(umbrellaIcon);

        menuButton.getStyleClass().add("side-button");
        umbrellaButton.getStyleClass().add("side-button");

        VBox rightButtons = new VBox(8, menuButton, umbrellaButton);
        rightButtons.setAlignment(Pos.TOP_CENTER);
        rightButtons.setPadding(new Insets(10));

        root.setRight(rightButtons);

        VBox settingsBox = new VBox(10);
        settingsBox.setMaxSize(600, 360);
        settingsBox.setPadding(new Insets(20, 28, 20, 28));
        settingsBox.getStyleClass().add("settings-box");

        Label settingsTitle = new Label("SETTINGS");
        settingsTitle.getStyleClass().add("settings-title");

        Label musicLabel = new Label("Music");
        Label musicValue = new Label("[ ON ]");
        final boolean[] musicEnabled = {true};

        musicValue.setOnMouseClicked(event -> {
            musicEnabled[0] = !musicEnabled[0];

            if (musicEnabled[0]) {
                musicPlayer.play();
                musicValue.setText("[ ON ]");
            } else {
                musicPlayer.pause();
                musicValue.setText("[ OFF ]");
            }
        });

        Label sfxLabel = new Label("Sound Effects");
        Label sfxValue = new Label("[ ON ]");
        sfxValue.setOnMouseClicked(event -> {
            soundEffectsEnabled = !soundEffectsEnabled;

            sfxValue.setText(
                    soundEffectsEnabled ? "[ ON ]" : "[ OFF ]"
            );
        });

        Label rainLabel = new Label("Rain");
        Label rainValue = new Label("[ ON ]");
        final boolean[] rainEnabled = {true};

        rainValue.setOnMouseClicked(event -> {
            rainEnabled[0] = !rainEnabled[0];

            if (rainEnabled[0]) {
                rainPlayer.play();
                rainValue.setText("[ ON ]");
            } else {
                rainPlayer.pause();
                rainValue.setText("[ OFF ]");
            }
        });

        musicLabel.getStyleClass().add("settings-option");
        sfxLabel.getStyleClass().add("settings-option");
        rainLabel.getStyleClass().add("settings-option");

        musicValue.getStyleClass().add("settings-value");
        sfxValue.getStyleClass().add("settings-value");
        rainValue.getStyleClass().add("settings-value");

        HBox musicRow = new HBox(musicLabel, musicValue);
        HBox sfxRow = new HBox(sfxLabel, sfxValue);
        HBox rainRow = new HBox(rainLabel, rainValue);

        HBox.setHgrow(musicLabel, Priority.ALWAYS);
        HBox.setHgrow(sfxLabel, Priority.ALWAYS);
        HBox.setHgrow(rainLabel, Priority.ALWAYS);

        musicLabel.setMaxWidth(Double.MAX_VALUE);
        sfxLabel.setMaxWidth(Double.MAX_VALUE);
        rainLabel.setMaxWidth(Double.MAX_VALUE);

        Button resetButton = new Button("RESET ALL PROGRESS");
        resetButton.getStyleClass().add("settings-reset");
        resetButton.setOnAction(event -> {

            repository.resetAllProgress();

            completedAchievements.clear();

            completed.set(0);

            progressBar.setProgress(0);

            progressLabel.setText(
                    "0 out of " + TOTAL_ACHIEVEMENTS
            );

            applyFilters();
        });

        HBox resetRow = new HBox(resetButton);
        resetRow.setAlignment(Pos.CENTER);

        Button settingsCloseButton = new Button("Close");
        settingsCloseButton.getStyleClass().add("settings-close");

        HBox closeRow = new HBox(settingsCloseButton);
        closeRow.setAlignment(Pos.CENTER_RIGHT);

        settingsBox.getChildren().addAll(
                settingsTitle,
                musicRow,
                sfxRow,
                rainRow,
                resetRow,
                closeRow
        );

        Image cardImage = new Image(
                getClass().getResource("umbrella card.png").toExternalForm()
        );

        ImageView cardView = new ImageView(cardImage);
        cardView.setFitWidth(700);
        cardView.setPreserveRatio(true);

        Hyperlink researchLink = new Hyperlink(" ");
        Hyperlink portfolioLink = new Hyperlink(" ");
        Hyperlink githubLink = new Hyperlink(" ");

        researchLink.setStyle("-fx-background-color: transparent;");
        portfolioLink.setStyle("-fx-background-color: transparent;");
        githubLink.setStyle("-fx-background-color: transparent;");

        researchLink.setOnAction(event ->
                getHostServices().showDocument("https://app.notion.com/p/PROJECT-ACHIEVEMENT-TRACKER-3b3d434ac90480ccbe2ee13db35f312b?source=copy_link")
        );

        portfolioLink.setOnAction(event ->
                getHostServices().showDocument("https://github.com/yuvante")
        );

        githubLink.setOnAction(event ->
                getHostServices().showDocument("https://github.com/yuvante/RE2-achievement-tracker")
        );

        researchLink.setPrefSize(459, 18);
        portfolioLink.setPrefSize(459, 18);
        githubLink.setPrefSize(459, 18);

        researchLink.setLayoutX(25);
        researchLink.setLayoutY(294);

        portfolioLink.setLayoutX(25);
        portfolioLink.setLayoutY(318);

        githubLink.setLayoutX(25);
        githubLink.setLayoutY(342);


        researchLink.getStyleClass().add("card-hitbox");
        portfolioLink.getStyleClass().add("card-hitbox");
        githubLink.getStyleClass().add("card-hitbox");

        researchLink.setFocusTraversable(false);
        portfolioLink.setFocusTraversable(false);
        githubLink.setFocusTraversable(false);

        Pane cardPane = new Pane();
        cardPane.setPrefSize(700, 450);

        cardPane.getChildren().addAll(
                cardView,
                researchLink,
                portfolioLink,
                githubLink
        );

        VBox umbrellaBox = new VBox(15);
        umbrellaBox.setAlignment(Pos.CENTER);
        umbrellaBox.setMaxSize(750, 520);
        umbrellaBox.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-padding: 0;"
        );

        Button umbrellaCloseButton = new Button("Close");
        umbrellaCloseButton.getStyleClass().add("close-button");

        umbrellaBox.getChildren().addAll(
                cardPane,
                umbrellaCloseButton
        );

        overlay.getChildren().addAll(
                settingsBox,
                umbrellaBox
        );

        settingsBox.setVisible(false);
        settingsBox.setManaged(false);

        umbrellaBox.setVisible(false);
        umbrellaBox.setManaged(false);

        menuButton.setOnAction(event -> {

            umbrellaBox.setVisible(false);
            umbrellaBox.setManaged(false);

            settingsBox.setVisible(true);
            settingsBox.setManaged(true);

            overlay.setVisible(true);
            overlay.setManaged(true);
        });

        settingsCloseButton.setOnAction(event -> {
            overlay.setVisible(false);
            overlay.setManaged(false);

            settingsBox.setVisible(false);
            settingsBox.setManaged(false);
        });

        umbrellaButton.setOnAction(event -> {

            settingsBox.setVisible(false);
            settingsBox.setManaged(false);

            umbrellaBox.setVisible(true);
            umbrellaBox.setManaged(true);

            overlay.setVisible(true);
            overlay.setManaged(true);
        });

        umbrellaCloseButton.setOnAction(event -> {
            overlay.setVisible(false);
            overlay.setManaged(false);

            umbrellaBox.setVisible(false);
            umbrellaBox.setManaged(false);
        });

        progressBar = new ProgressBar(
                (double) completed.get() / TOTAL_ACHIEVEMENTS
        );
        progressBar.setMaxWidth(Double.MAX_VALUE);
        progressBar.setPrefWidth(1290);
        progressBar.setPrefHeight(20);
        progressBar.setStyle(
                "-fx-accent: #b00020;"
        );

        progressLabel = new Label(
                completed.get() + " out of " + TOTAL_ACHIEVEMENTS
        );
        Font requiemFont = Font.loadFont(getClass().getResourceAsStream("font.ttf"), 14);
        progressLabel.setFont(requiemFont);
        progressLabel.getStyleClass().add("progress-label");
        StackPane.setAlignment(progressLabel, Pos.TOP_CENTER);
        progressLabel.setPadding(new Insets(0, 0, 2, 0));

        StackPane progressPane = new StackPane(
                progressBar,
                progressLabel
        );
        progressPane.setPrefHeight(20);
        progressPane.setMaxHeight(20);
        progressBar.setMaxWidth(Double.MAX_VALUE);
        progressBar.setMaxHeight(Double.MAX_VALUE);

        HBox progressContainer = new HBox(progressPane);

        progressContainer.setAlignment(Pos.CENTER);
        progressContainer.setPadding(new Insets(0, 5, 2, 5));

        progressContainer.setMaxHeight(Region.USE_PREF_SIZE);
        progressContainer.setMouseTransparent(true);

        HBox.setHgrow(progressPane, Priority.ALWAYS);
        progressPane.setMaxWidth(Double.MAX_VALUE);

        HBox.setHgrow(progressPane, Priority.ALWAYS);
        progressPane.setMaxWidth(Double.MAX_VALUE);

        root.setStyle("-fx-background-color: transparent;");

        Image background1 = new Image(getClass().getResourceAsStream("re2 background2.png"));
        ImageView appBackground = new ImageView(background1);

        StackPane backgroundPane = new StackPane();

        backgroundPane.getChildren().addAll(
                appBackground,
                root,
                progressContainer,
                overlay
        );
        StackPane.setAlignment(progressContainer, Pos.BOTTOM_CENTER);
        StackPane.setMargin(progressContainer, new Insets(0, 12, 2, 12));
        backgroundPane.setStyle("-fx-background-color: black;");
        appBackground.fitWidthProperty().bind(backgroundPane.widthProperty());
        appBackground.fitHeightProperty().bind(backgroundPane.heightProperty());
        appBackground.setPreserveRatio(true);

        Image icon = new Image(getClass().getResource("/re2 icon.png").toExternalForm());
        stage.getIcons().add(icon);

        Image logo = new Image(getClass().getResource("/re2 logo.png").toExternalForm());
        ImageView logoView = new ImageView(logo);
        logoView.setFitWidth(400);
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
        searchField.setFont(Font.font("IBM Plex Sans", 16));
        topPanel.getChildren().add(searchField);
        searchField.setPrefWidth(390);
        searchField.setMaxWidth(390);
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

        Scene scene = new Scene(backgroundPane, 1290, 720);
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

        VBox achievementInfo = new VBox();
        achievementInfo.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(achievementInfo, Priority.ALWAYS);
        Label nameLabel = new Label(achievement.getName());

        Label categoryLabel = new Label(
                achievement.getCategory().toUpperCase()
        );
        Label descriptionLabel = new Label(
                achievement.getDescription()
        );

        VBox.setMargin(descriptionLabel, new Insets(5, 0, 0, 0));

        String stars =
                "★".repeat(achievement.getDifficulty())
                        + "☆".repeat(5 - achievement.getDifficulty());

        Label difficultyLabel = new Label(stars);

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
        achievementRow.setMaxWidth(Double.MAX_VALUE);
        checkBox.setTranslateY(6);

        nameLabel.setFont(Font.font("IBM Plex Sans",FontWeight.BOLD, 20));
        categoryLabel.setFont(Font.font("IBM Plex Sans",FontWeight.BOLD, 12));
        difficultyLabel.setFont(Font.font("IBM Plex Sans", FontWeight.BOLD, 11));
        descriptionLabel.setFont(Font.font("IBM Plex Sans", 16));

        nameLabel.getStyleClass().add("achievement-text");
        categoryLabel.getStyleClass().add("achievement-text");
        difficultyLabel.getStyleClass().add("achievement-text");
        descriptionLabel.getStyleClass().add("achievement-text");

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

                if (soundEffectsEnabled) {
                    checkSound.seek(Duration.ZERO);
                    checkSound.play();
                }

                completedAchievements.add(achievement.getId());
                achievementRow.getStyleClass().add("completed");

                repository.setAchievementCompleted(
                        achievement.getId(),
                        true
                );

            } else {

                completedAchievements.remove(achievement.getId());
                achievementRow.getStyleClass().remove("completed");

                repository.setAchievementCompleted(
                        achievement.getId(),
                        false
                );
            }

            completed.set(completedAchievements.size());

            progressBar.setProgress(
                    (double) completed.get() / TOTAL_ACHIEVEMENTS
            );

            progressLabel.setText(
                    completed.get() + " out of " + TOTAL_ACHIEVEMENTS
            );
        });


        if (achievement.getHint() != null) {

            Button hintButton = new Button("Hint");
            hintButton.getStyleClass().add("hint-button");

            Label hintLabel = new Label(achievement.getHint());
            hintLabel.getStyleClass().add("hint-text");
            hintLabel.setWrapText(true);
            hintLabel.setMaxWidth(380);

            Hyperlink guideLink = null;

            VBox hintContentBox = new VBox(3);
            hintContentBox.setAlignment(Pos.TOP_LEFT);

            hintLabel.setVisible(false);
            hintLabel.setManaged(false);

            hintContentBox.getChildren().add(hintLabel);

            if (achievement.getUrl() != null) {
                guideLink = new Hyperlink("[ Video Guide ]");
                guideLink.getStyleClass().add("guide-link");

                guideLink.setVisible(false);
                guideLink.setManaged(false);

                hintContentBox.getChildren().add(guideLink);

                guideLink.setOnAction(event -> {
                    getHostServices().showDocument(achievement.getUrl());
                });
            }

            VBox hintBox = new VBox(5);
            hintBox.setMaxWidth(Double.MAX_VALUE);

            HBox buttonBox = new HBox(hintButton);
            buttonBox.setAlignment(Pos.CENTER_RIGHT);

            hintBox.getChildren().addAll(
                    buttonBox,
                    hintContentBox
            );

            VBox.setMargin(hintBox, new Insets(8, 0, 0, 0));
            achievementInfo.getChildren().add(hintBox);

            Hyperlink finalGuideLink = guideLink;

            hintButton.setOnAction(event -> {

                boolean showHint = !hintLabel.isVisible();

                hintLabel.setVisible(showHint);
                hintLabel.setManaged(showHint);

                if (finalGuideLink != null) {
                    finalGuideLink.setVisible(showHint);
                    finalGuideLink.setManaged(showHint);
                }
            });
        }
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