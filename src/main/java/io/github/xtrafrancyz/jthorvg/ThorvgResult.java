package io.github.xtrafrancyz.jthorvg;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum ThorvgResult {
    SUCCESS(0),
    INVALID_ARGUMENT(1),
    INSUFFICIENT_CONDITION(2),
    FAILED_ALLOCATION(3),
    MEMORY_CORRUPTION(4),
    NOT_SUPPORTED(5),
    SYSTEM_ERROR(6),
    UNKNOWN(255);

    private static final Map<Integer, ThorvgResult> BY_CODE = Arrays.stream(values())
        .collect(Collectors.toMap(ThorvgResult::code, Function.identity()));

    private final int code;

    ThorvgResult(int code) {
        this.code = code;
    }

    public int code() {
        return code;
    }

    public boolean isSuccess() {
        return this == SUCCESS;
    }

    public static ThorvgResult fromCode(int code) {
        return BY_CODE.getOrDefault(code, UNKNOWN);
    }

    void throwIfError(String operation) {
        if (!isSuccess()) {
            throw new ThorvgException(operation, this);
        }
    }
}
