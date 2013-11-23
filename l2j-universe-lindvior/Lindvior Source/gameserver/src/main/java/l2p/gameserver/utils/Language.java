package l2p.gameserver.utils;

/**
 * @author VISTALL
 * @date 13:50/29.03.2011
 */
public enum Language {
    ENGLISH("en"),
    RUSSIAN("ru");

    public static final Language[] VALUES = Language.values();

    private String _shortName;

    Language(String shortName) {
        _shortName = shortName;
    }

    public String getShortName() {
        return _shortName;
    }
}
