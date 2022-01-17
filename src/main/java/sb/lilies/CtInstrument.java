package sb.lilies;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class CtInstrument implements Instrument {

    private final Instrument origin;
    private final String name;
    private final String slug;

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
