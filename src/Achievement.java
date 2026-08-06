public class Achievement {
    private int id;
    private String name;
    private String description;
    private int difficulty;
    private String hint;

    public Achievement(int id, String name, String description, int difficulty, String hint) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.difficulty = difficulty;
        this.hint = hint;

    }

    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public int getDifficulty() {
        return difficulty;
    }
    public String getHint() {
        return hint;
    }

}
