/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package quests;

import l2p.gameserver.model.quest.Quest;
import l2p.gameserver.scripts.ScriptFile;

public class _10369_NoblesseTheTestOfSoul extends Quest implements ScriptFile {
    private static final int CON1 = 31281;

    @Override
    public void onLoad() {
    }

    @Override
    public void onReload() {
    }

    @Override
    public void onShutdown() {
    }

    public _10369_NoblesseTheTestOfSoul() {
        super(false);
        addStartNpc(CON1);
        addTalkId(CON1);
        addLevelCheck(75, 99);
        //addSubClassCheck();
    }
}
