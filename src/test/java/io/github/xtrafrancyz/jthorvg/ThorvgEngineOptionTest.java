package io.github.xtrafrancyz.jthorvg;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.EnumSet;

import org.junit.jupiter.api.Test;

class ThorvgEngineOptionTest {
    @Test
    void combinesOptionsIntoNativeBitmask() {
        assertEquals(
            ThorvgEngineOption.DEFAULT.mask() | ThorvgEngineOption.ALIASED.mask(),
            ThorvgEngineOption.toNativeMask(EnumSet.of(ThorvgEngineOption.DEFAULT, ThorvgEngineOption.ALIASED))
        );
    }
}
