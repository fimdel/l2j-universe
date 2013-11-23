package l2p.gameserver.model.base;

/**
 * Author: VISTALL
 * Date:  11:53/01.12.2010
 */
public enum AcquireType {
    NORMAL,
    FISHING,
    CLAN,
    SUB_UNIT,
    TRANSFORMATION,
    DUALCERTIFICATION,
    CERTIFICATION,
    SPELLBOOK,
    COLLECTION,
    TRANSFER_CARDINAL,
    TRANSFER_EVA_SAINTS,
    TRANSFER_SHILLIEN_SAINTS;

    public static final AcquireType[] VALUES = AcquireType.values();

    public static AcquireType transferType(int classId) {
        switch (classId) {
            case 97:
                return TRANSFER_CARDINAL;
            case 105:
                return TRANSFER_EVA_SAINTS;
            case 112:
                return TRANSFER_SHILLIEN_SAINTS;
        }

        return null;
    }

    public int transferClassId() {
        switch (this) {
            case TRANSFER_CARDINAL:
                return 97;
            case TRANSFER_EVA_SAINTS:
                return 105;
            case TRANSFER_SHILLIEN_SAINTS:
                return 112;
            default:
                break;
        }

        return 0;
    }
}
