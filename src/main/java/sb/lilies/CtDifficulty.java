package sb.lilies;

public final class CtDifficulty implements Difficulty {

    private final Difficulty diff;
    private final String name;

    public CtDifficulty(Difficulty diff, String name) {
        this.diff = diff;
        this.name = name;
    }

    @Override
    public int rating() {
        return this.diff.rating();
    }

    @Override
    public String name() {
        return this.name;
    }
}
