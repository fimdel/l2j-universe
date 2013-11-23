/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2012.
 */

package l2p.gameserver.model.entity.events.impl;

import l2p.commons.collections.MultiValueSet;
import l2p.gameserver.model.entity.events.GlobalEvent;

/**
 * Created with IntelliJ IDEA.
 * User: Darvin
 * Date: 24.11.12
 * Time: 14:49
 */
public class MonasteryFurnaceEvent extends GlobalEvent {
    private long _startTime;
    private boolean _progress;
    public static final String FURNACE_ROOM = "furnace_room";
    public static final String PROTECTOR_ROOM = "Protector_Room";
    public static final String FIGHTER_ROOM = "Fighter_Room";
    public static final String MYSTIC_ROOM = "Mystic_Room";
    public static final String STANDART_ROOM = "Standart_Monster";

    public MonasteryFurnaceEvent(MultiValueSet<String> set) {
        super(set);
    }

    public void startEvent() {
        _progress = true;
        super.startEvent();
    }

    public void stopEvent() {
        _progress = false;
        super.stopEvent();
    }

    public boolean isInProgress() {
        return _progress;
    }

    public void reCalcNextTime(boolean onStart) {
        _startTime = System.currentTimeMillis();

        registerActions();
    }

    protected long startTimeMillis() {
        return _startTime;
    }
}
