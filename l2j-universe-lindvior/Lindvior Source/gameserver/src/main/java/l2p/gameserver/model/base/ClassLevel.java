/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.model.base;

/**
 * This class ...
 *
 * @version $Revision: 1.2 $ $Date: 2004/06/27 08:12:59 $
 */

public enum ClassLevel {
    NONE,    //чар только создали, т.е без профы
    FIRST,   //1
    SECOND, //2
    THIRD,   //3
    AWAKED; //4

    public static final ClassLevel[] VALUES = values();
}
