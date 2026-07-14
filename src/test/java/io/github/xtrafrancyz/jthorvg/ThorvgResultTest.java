package io.github.xtrafrancyz.jthorvg;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class ThorvgResultTest {
    @Test
    void mapsKnownResultCodes() {
        assertEquals(ThorvgResult.SUCCESS, ThorvgResult.fromCode(0));
        assertEquals(ThorvgResult.NOT_SUPPORTED, ThorvgResult.fromCode(5));
    }

    @Test
    void mapsUnknownResultCodesToUnknown() {
        assertEquals(ThorvgResult.UNKNOWN, ThorvgResult.fromCode(1234));
    }

    @Test
    void throwsHelpfulExceptionForFailures() {
        ThorvgException exception = assertThrows(
            ThorvgException.class,
            () -> ThorvgResult.INVALID_ARGUMENT.throwIfError("tvg_example")
        );

        assertEquals(ThorvgResult.INVALID_ARGUMENT, exception.result());
    }
}
