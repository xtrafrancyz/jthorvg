package io.github.xtrafrancyz.jthorvg;

public class EngineVersion {
    private final int major;
    private final int minor;
    private final int micro;
    private final String version;

    public EngineVersion(int major, int minor, int micro, String version) {
        this.major = major;
        this.minor = minor;
        this.micro = micro;
        this.version = version;
    }

    public int major() {
        return major;
    }

    public int minor() {
        return minor;
    }

    public int micro() {
        return micro;
    }

    public String version() {
        return version;
    }

    @Override
    public String toString() {
        return String.format("%d.%d.%d (%s)", major, minor, micro, version);
    }
}
