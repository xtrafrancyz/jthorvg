package io.github.xtrafrancyz.jthorvg;

public final class ThorvgException extends IllegalStateException {
    private final ThorvgResult result;

    public ThorvgException(String operation, ThorvgResult result) {
        super(operation + " failed with result " + result + " (code=" + result.code() + ")");
        this.result = result;
    }

    public ThorvgResult result() {
        return result;
    }
}
