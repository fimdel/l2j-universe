/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.tables;

import gnu.trove.list.array.TIntArrayList;
import gnu.trove.map.hash.TIntObjectHashMap;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.SubClass;
import l2p.gameserver.model.base.ClassId;
import l2p.gameserver.model.base.ClassLevel;
import l2p.gameserver.model.base.Race;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 * User: Darvin
 * Date: 05.04.12
 * Time: 18:37
 */
public final class SubClassTable {
    private static final Logger _log = LoggerFactory.getLogger(SubClassTable.class);

    private static SubClassTable _instance;

    private TIntObjectHashMap<TIntArrayList> _subClasses;

    public SubClassTable() {
        init();
    }

    public static SubClassTable getInstance() {
        if (_instance == null)
            _instance = new SubClassTable();
        return _instance;
    }

    private void init() {
        _subClasses = new TIntObjectHashMap<TIntArrayList>();

        for (ClassId baseClassId : ClassId.VALUES) {
            if (baseClassId.isOfLevel(ClassLevel.FIRST))
                continue;

            TIntArrayList availSubs = new TIntArrayList();
            for (ClassId subClassId : ClassId.VALUES) {
                if (!subClassId.isOfLevel(ClassLevel.SECOND))
                    continue;

                if (!areClassesComportable(baseClassId, subClassId))
                    continue;

                availSubs.add(subClassId.getId());
            }
            availSubs.sort();
            _subClasses.put(baseClassId.getId(), availSubs);
        }
        _log.info("SubClassTable: Loaded " + _subClasses.size() + " sub-classes variations.");
    }

    public int[] getAvailableSubClasses(Player player, int classId) {
        TIntArrayList subClassesList = _subClasses.get(classId);
        if (subClassesList == null || subClassesList.isEmpty())
            return new int[0];

        //   ClassId currClassId = ClassId.VALUES[classId];
        loop:
        for (int clsId : subClassesList.toArray()) {
            ClassId subClassId = ClassId.VALUES[clsId];

            Collection<SubClass> playerSubClasses = player.getSubClassList().values();
            for (SubClass playerSubClass : playerSubClasses) {
                ClassId playerSubClassId = ClassId.VALUES[playerSubClass.getClassId()];
                if (!areClassesComportable(playerSubClassId, subClassId)) {
                    subClassesList.remove(clsId);
                    continue loop;
                }
            }

            if (subClassId.isOfRace(Race.kamael)) {
                if (player.getSex() == 1 && subClassId == ClassId.M_SOUL_BREAKER || player.getSex() == 0 && subClassId == ClassId.F_SOUL_BREAKER) {
                    subClassesList.remove(clsId);
                    continue;
                }

                if (subClassId == ClassId.INSPECTOR && player.getSubClassList().size() < 3) {
                    subClassesList.remove(clsId);
                    continue;
                }
            }
        }
        return subClassesList.toArray();
    }

    public static boolean areClassesComportable(ClassId baseClassId, ClassId subClassId) {
        if (baseClassId == subClassId)
            return false;

        if (baseClassId.getType2() == subClassId.getType2())
            return false;

        if (baseClassId.isOfRace(Race.kamael) != subClassId.isOfRace(Race.kamael))
            return false;

        if (baseClassId.isOfRace(Race.elf) && subClassId.isOfRace(Race.darkelf) || baseClassId.isOfRace(Race.darkelf) && subClassId.isOfRace(Race.elf))
            return false;

        if (subClassId == ClassId.OVERLORD || subClassId == ClassId.WARSMITH)
            return false;

        return true;
    }
}
