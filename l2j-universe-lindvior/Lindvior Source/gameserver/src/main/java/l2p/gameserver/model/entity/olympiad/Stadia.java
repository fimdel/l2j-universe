/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.model.entity.olympiad;

public class Stadia {
    private boolean _freeToUse = true;

    public boolean isFreeToUse() {
        return _freeToUse;
    }

    public void setStadiaBusy() {
        _freeToUse = false;
    }

    public void setStadiaFree() {
        _freeToUse = true;
    }
}