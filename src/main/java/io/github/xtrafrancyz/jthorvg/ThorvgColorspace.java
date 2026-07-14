package io.github.xtrafrancyz.jthorvg;

public enum ThorvgColorspace {
    ABGR8888(0),
    ARGB8888(1),
    ABGR8888S(2),
    ARGB8888S(3),
    GRAYSCALE8(4),
    UNKNOWN(255);

    private final int code;

    ThorvgColorspace(int code) {
        this.code = code;
    }

    int code() {
        return code;
    }
}
