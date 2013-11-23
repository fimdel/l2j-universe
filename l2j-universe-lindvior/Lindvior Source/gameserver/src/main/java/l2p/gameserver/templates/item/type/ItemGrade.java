/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.templates.item.type;

public enum ItemGrade {
    NONE(0, 0),
    D(1458, 1),
    C(1459, 2),
    B(1460, 3),
    A(1461, 4),
    S(1462, 5),
    S80(1462, 5),
    S84(1462, 5),
    R(17371, 6),
    R95(17371, 6),
    R99(17371, 6);

    public final int cry;
    public final int externalOrdinal;

    private ItemGrade(int crystal, int ext) {
        cry = crystal;
        externalOrdinal = ext;
    }

    public int getExtOrdinal() {
        return externalOrdinal;
    }

    public int getCrystalId() {
        return cry;
    }

    public ItemGrade getShotGrade() {
        switch (externalOrdinal) {
            case 1:
                return D;
            case 2:
                return C;
            case 3:
                return B;
            case 4:
                return A;
            case 5:
                return S;
            case 6:
                return R;
        }
        return NONE;
    }
}
