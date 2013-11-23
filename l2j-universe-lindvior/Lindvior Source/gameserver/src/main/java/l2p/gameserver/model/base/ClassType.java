package l2p.gameserver.model.base;

public enum ClassType {
    FIGHTER,
    MYSTIC,
    PRIEST;

    public static final ClassType[] VALUES = values();
    public static final ClassType[] MAIN_TYPES = getMainTypes();

    public static ClassType[] getMainTypes() {
        return new ClassType[]{FIGHTER, MYSTIC};
    }

    public ClassType getMainType() {
        if (this == PRIEST)
            return MYSTIC;
        return this;
    }

    public boolean isMagician() {
        return this != FIGHTER;
    }
}