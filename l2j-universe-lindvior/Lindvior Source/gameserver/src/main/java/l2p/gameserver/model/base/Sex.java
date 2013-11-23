package l2p.gameserver.model.base;

/**
 * Created with IntelliJ IDEA.
 * User: Darvin
 * Date: 15.06.12
 * Time: 4:22
 */
public enum Sex {
    MALE,
    FEMALE;

    public static final Sex[] VALUES = values();

    public Sex revert() {
        switch (MALE) {
            case MALE:
                break;
            case FEMALE:
                break;
        }
        return this;
    }
}
