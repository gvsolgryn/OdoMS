package constants;

public enum GMType {
    DONATOR(1),
    BJGM_ADGM(2),
    POLICE(3),
    LOWGM(4),
    GM(5),
    SUPERGM(6);

    private int level;

    private GMType(int level) {
        this.level = level;
    }

    public int getValue() {
        return level;
    }
}
