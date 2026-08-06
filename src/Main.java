public class Main {

    public static void main(String[] args) {

                AchievementRepository repository = new AchievementRepository();

                for (Achievement achievement : repository.getAllAchievements()) {
                    System.out.println(
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
                }

    }
}
