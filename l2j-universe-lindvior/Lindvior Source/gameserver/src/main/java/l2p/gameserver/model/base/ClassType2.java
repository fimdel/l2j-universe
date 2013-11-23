/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.model.base;

/**
 * @author VISTALL
 * @date 22:48/08.12.2010
 */
public enum ClassType2 {
    KNIGHT(10282, 10288),
    WARRIOR(10281, 10289),
    ROGUE(10283, 10290),
    ARCHER(10280, 10612),
    WIZARD(10284, 10292),
    ENCHANTER(10287, 10293),
    SUMMONER(10286, 10294),
    HEALER(10285, 10291);

    public static final ClassType2[] VALUES = values();
    private final int _certificate;
    private final int _transformation;

    private ClassType2(int cer, int tra) {
        _certificate = cer;
        _transformation = tra;
    }

    public int getCertificateId() {
        return _certificate;
    }

    public int getTransformationId() {
        return _transformation;
    }

}
