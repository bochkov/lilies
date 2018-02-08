package sb.lilies;

public final class CtInstrument implements Instrument {

    private final Instrument origin;
    private final String name;
    private final String slug;

    public CtInstrument(Instrument origin, String name, String slug) {
        this.origin = origin;
        this.name = name;
        this.slug = slug;
    }

    @Override
    public Long id() {
        return this.origin.id();
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public String slug() {
        return this.slug;
    }
}
