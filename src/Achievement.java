public class Achievement {
    private int id;
    private String name;
    private String description;
    private int difficulty;
    private String hint;
    private String category;

    public Achievement(int id, String name, String description, int difficulty, String hint, String category) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.difficulty = difficulty;
        this.hint = hint;
        this.category = category;

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
    public String getCategory() {return category; }
}
