package io.github.xtrafrancyz.jthorvg;

public interface LottieAudioResolver {
    void onAudio(String src, String mimeType, int size, float offset, float volume, boolean active, boolean embedded);
}
