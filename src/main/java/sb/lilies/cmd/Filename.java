package sb.lilies.cmd;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class Filename {

    private final String fn;

    public String name() {
        return fn.substring(0, fn.lastIndexOf('.'));
    }
}
