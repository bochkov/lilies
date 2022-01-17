package sb.lilies;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class CtWriter implements Author {

    private final Author origin;
    private final String name;

    @Override
    public String name() {
        return name;
    }
}
