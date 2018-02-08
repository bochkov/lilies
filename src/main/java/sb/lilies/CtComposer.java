package sb.lilies;

public final class CtComposer implements Author {

    private final Author origin;
    private final String name;

    public CtComposer(Author origin, String name) {
        this.origin = origin;
        this.name = name;
    }

    @Override
    public String name() {
        return name;
    }
}
