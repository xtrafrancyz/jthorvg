package io.github.xtrafrancyz.jthorvg;

import java.util.Collection;
import java.util.Objects;

public enum ThorvgEngineOption {
    NONE(0),
    DEFAULT(1 << 0),
    SMART_RENDER(1 << 1),
    ALIASED(1 << 2);

    private final int mask;

    ThorvgEngineOption(int mask) {
        this.mask = mask;
    }

    int mask() {
        return mask;
    }

    static int toNativeMask(Collection<ThorvgEngineOption> options) {
        Objects.requireNonNull(options, "options");
        return options.stream().mapToInt(ThorvgEngineOption::mask).reduce(0, (left, right) -> left | right);
    }
}
