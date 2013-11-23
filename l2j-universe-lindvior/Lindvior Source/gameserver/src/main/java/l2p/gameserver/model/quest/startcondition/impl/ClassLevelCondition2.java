/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.model.quest.startcondition.impl;

import l2p.gameserver.model.Player;
import l2p.gameserver.model.base.ClassLevel;
import l2p.gameserver.model.quest.startcondition.ICheckStartCondition;
import org.apache.commons.lang3.ArrayUtils;

public class ClassLevelCondition2 implements ICheckStartCondition {

    private ClassLevel[] _classLevels;

    public ClassLevelCondition2(ClassLevel[] classLevels) {
        this._classLevels = classLevels;
    }

    @Override
    public boolean checkCondition(Player player) {
        return ArrayUtils.contains(_classLevels, player.getClassId().getClassLevel().ordinal());
    }
}
