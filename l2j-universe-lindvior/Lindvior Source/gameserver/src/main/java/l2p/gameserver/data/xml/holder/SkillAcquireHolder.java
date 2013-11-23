/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.data.xml.holder;

import gnu.trove.TIntObjectHashMap;
import gnu.trove.TIntObjectIterator;
import l2p.commons.data.xml.AbstractHolder;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.Skill;
import l2p.gameserver.model.SkillLearn;
import l2p.gameserver.model.base.AcquireType;
import l2p.gameserver.model.base.ClassId;
import l2p.gameserver.model.pledge.Clan;
import l2p.gameserver.model.pledge.SubUnit;

import java.util.*;


/**
 * @author: VISTALL
 * @date: 20:55/30.11.2010
 */
public final class SkillAcquireHolder extends AbstractHolder {
    private static final SkillAcquireHolder _instance = new SkillAcquireHolder();


    public static SkillAcquireHolder getInstance() {
        return _instance;
    }

    // классовые зависимости
    private TIntObjectHashMap<List<SkillLearn>> _normalSkillTree = new TIntObjectHashMap<List<SkillLearn>>();
    private TIntObjectHashMap<List<SkillLearn>> _transferSkillTree = new TIntObjectHashMap<List<SkillLearn>>();
    private TIntObjectHashMap<List<SkillLearn>> _spellBookSkillTree = new TIntObjectHashMap<List<SkillLearn>>();
    // расовые зависимости
    private TIntObjectHashMap<List<SkillLearn>> _fishingSkillTree = new TIntObjectHashMap<List<SkillLearn>>();
    private TIntObjectHashMap<List<SkillLearn>> _transformationSkillTree = new TIntObjectHashMap<List<SkillLearn>>();
    // без зависимостей
    private List<SkillLearn> _certificationSkillTree = new ArrayList<SkillLearn>();
    private List<SkillLearn> _dual_certificationSkillTree = new ArrayList<SkillLearn>();
    private List<SkillLearn> _collectionSkillTree = new ArrayList<SkillLearn>();
    private List<SkillLearn> _pledgeSkillTree = new ArrayList<SkillLearn>();
    private List<SkillLearn> _subUnitSkillTree = new ArrayList<SkillLearn>();
    private TIntObjectHashMap<TIntObjectHashMap<List<SkillLearn>>> _awakeParentSkillTree = new TIntObjectHashMap<TIntObjectHashMap<List<SkillLearn>>>();

    public int getMinLevelForNewSkill(Player player, AcquireType type) {
        List<SkillLearn> skills;
        switch (type) {
            case NORMAL:
                skills = _normalSkillTree.get(player.getActiveClassId());
                if (skills == null) {
                    info("skill tree for class " + player.getActiveClassId() + " is not defined !");
                    return 0;
                }
                skills.addAll(getAwakeParentSkillTree(player));
                break;
            case TRANSFORMATION:
                skills = _transformationSkillTree.get(player.getRace().ordinal());
                if (skills == null) {
                    info("skill tree for race " + player.getRace().ordinal() + " is not defined !");
                    return 0;
                }
                break;
            case FISHING:
                skills = _fishingSkillTree.get(player.getRace().ordinal());
                if (skills == null) {
                    info("skill tree for race " + player.getRace().ordinal() + " is not defined !");
                    return 0;
                }
                break;
            default:
                return 0;
        }
        int minlevel = 0;
        for (SkillLearn temp : skills)
            if (temp.getMinLevel() > player.getLevel())
                if (minlevel == 0 || temp.getMinLevel() < minlevel)
                    minlevel = temp.getMinLevel();
        return minlevel;
    }

    public Collection<SkillLearn> getAvailableSkills(Player player, AcquireType type, boolean ignoreLvl) {
        return getAvailableSkills(player, type, null, ignoreLvl);
    }

    public Collection<SkillLearn> getAvailableSkills(Player player, AcquireType type, SubUnit subUnit,
                                                     boolean ignoreLvl) {
        if (player.getActiveSubClass() == null) {
            return Collections.emptyList();
        }
        Collection<SkillLearn> skills;
        switch (type) {
            case NORMAL:
                skills = _normalSkillTree.get(player.getActiveClassId());
                if (skills == null) {
                    info("skill tree for class " + player.getActiveClassId() + " is not defined !");
                    return Collections.emptyList();
                }
                skills.addAll(getAwakeParentSkillTree(player));
                return getAvaliableList(skills, player.getAllSkillsArray(), player.getLevel(), ignoreLvl);
            case COLLECTION:
                skills = _collectionSkillTree;
                if (skills == null) {
                    info("skill tree for class " + player.getActiveClassId() + " is not defined !");
                    return Collections.emptyList();
                }
                return getAvaliableList(skills, player.getAllSkillsArray(), player.getLevel(), ignoreLvl);
            case TRANSFORMATION:
                skills = _transformationSkillTree.get(player.getRace().ordinal());
                if (skills == null) {
                    info("skill tree for race " + player.getRace().ordinal() + " is not defined !");
                    return Collections.emptyList();
                }
                return getAvaliableList(skills, player.getAllSkillsArray(), player.getLevel(), ignoreLvl);
            case TRANSFER_EVA_SAINTS:
            case TRANSFER_SHILLIEN_SAINTS:
            case TRANSFER_CARDINAL:
                skills = _transferSkillTree.get(type.transferClassId());
                if (skills == null) {
                    info("skill tree for class " + type.transferClassId() + " is not defined !");
                    return Collections.emptyList();
                }
                Map<Integer, SkillLearn> skillLearnMap = new TreeMap<Integer, SkillLearn>();
                for (SkillLearn temp : skills)
                    if (temp.getMinLevel() <= player.getLevel() || ignoreLvl) {
                        int knownLevel = player.getSkillLevel(temp.getId());
                        if (knownLevel == -1)
                            skillLearnMap.put(temp.getId(), temp);
                    }

                return skillLearnMap.values();
            case FISHING:
                skills = _fishingSkillTree.get(player.getRace().ordinal());
                if (skills == null) {
                    info("skill tree for race " + player.getRace().ordinal() + " is not defined !");
                    return Collections.emptyList();
                }
                return getAvaliableList(skills, player.getAllSkillsArray(), player.getLevel(), ignoreLvl);
            case CLAN:
                skills = _pledgeSkillTree;
                Collection<Skill> skls = player.getClan().getSkills(); //TODO [VISTALL] придумать другой способ

                return getAvaliableList(skills, skls.toArray(new Skill[skls.size()]), player.getClan().getLevel(), ignoreLvl);
            case SUB_UNIT:
                skills = _subUnitSkillTree;
                Collection<Skill> st = subUnit.getSkills(); //TODO [VISTALL] придумать другой способ

                return getAvaliableList(skills, st.toArray(new Skill[st.size()]), player.getClan().getLevel(), ignoreLvl);
            case CERTIFICATION:
                skills = _certificationSkillTree;
                return getAvaliableList(skills, player.getAllSkillsArray(), player.getLevel(), ignoreLvl);
            case DUALCERTIFICATION:
                skills = _dual_certificationSkillTree;
                return getAvaliableList(skills, player.getAllSkillsArray(), player.getLevel(), ignoreLvl);

            default:
                return Collections.emptyList();
        }
    }

    private Collection<SkillLearn> getAvaliableList(Collection<SkillLearn> skillLearns, Skill[] skills, int level,
                                                    boolean ignoreLvl) {
        Map<Integer, SkillLearn> skillLearnMap = new TreeMap<Integer, SkillLearn>();
        for (SkillLearn temp : skillLearns)
            if (temp.getMinLevel() <= level || ignoreLvl) {
                boolean knownSkill = false;
                for (int j = 0; j < skills.length && !knownSkill; j++)
                    if (skills[j].getId() == temp.getId()) {
                        knownSkill = true;
                        if (skills[j].getLevel() == temp.getLevel() - 1)
                            skillLearnMap.put(temp.getId(), temp);
                    }
                if (!knownSkill && temp.getLevel() == 1)
                    skillLearnMap.put(temp.getId(), temp);
            }

        return skillLearnMap.values();
    }

    public SkillLearn getSkillLearn(Player player, int id, int level, AcquireType type) {
        List<SkillLearn> skills;
        switch (type) {
            case NORMAL:
                skills = _normalSkillTree.get(player.getActiveClassId());
                skills.addAll(getAwakeParentSkillTree(player));
                break;
            case COLLECTION:
                skills = _collectionSkillTree;
                break;
            case TRANSFORMATION:
                skills = _transformationSkillTree.get(player.getRace().ordinal());
                break;
            case TRANSFER_CARDINAL:
            case TRANSFER_SHILLIEN_SAINTS:
            case TRANSFER_EVA_SAINTS:
                skills = _transferSkillTree.get(player.getActiveClassId());
                break;
            case FISHING:
                skills = _fishingSkillTree.get(player.getRace().ordinal());
                break;
            case CLAN:
                skills = _pledgeSkillTree;
                break;
            case SUB_UNIT:
                skills = _subUnitSkillTree;
                break;
            case CERTIFICATION:
                skills = _certificationSkillTree;
                break;
            case DUALCERTIFICATION:
                skills = _dual_certificationSkillTree;
                break;
            default:
                return null;
        }

        if (skills == null)
            return null;

        for (SkillLearn temp : skills)
            if (temp.getLevel() == level && temp.getId() == id)
                return temp;

        return null;
    }

    public boolean isSkillPossible(Player player, Skill skill, AcquireType type) {
        Clan clan;
        List<SkillLearn> skills;
        switch (type) {
            case NORMAL:
                skills = _normalSkillTree.get(player.getActiveClassId());
                skills.addAll(getAwakeParentSkillTree(player));
                break;
            case COLLECTION:
                skills = _collectionSkillTree;
                break;
            case TRANSFORMATION:
                skills = _transformationSkillTree.get(player.getRace().ordinal());
                break;
            case FISHING:
                skills = _fishingSkillTree.get(player.getRace().ordinal());
                break;
            case TRANSFER_CARDINAL:
            case TRANSFER_EVA_SAINTS:
            case TRANSFER_SHILLIEN_SAINTS:
                int transferId = type.transferClassId();
                if (player.getActiveClassId() != transferId)
                    return false;

                skills = _transferSkillTree.get(transferId);
                break;
            case CLAN:
                clan = player.getClan();
                if (clan == null)
                    return false;
                skills = _pledgeSkillTree;
                break;
            case SUB_UNIT:
                clan = player.getClan();
                if (clan == null)
                    return false;

                skills = _subUnitSkillTree;
                break;
            case CERTIFICATION:
                skills = _certificationSkillTree;
                break;
            case DUALCERTIFICATION:
                skills = _dual_certificationSkillTree;
                break;
            case SPELLBOOK:
                skills = _spellBookSkillTree.get(player.getActiveClassId());
                break;
            default:
                return false;
        }

        return isSkillPossible(skills, skill);
    }

    private boolean isSkillPossible(Collection<SkillLearn> skills, Skill skill) {
        if (skills != null)
            for (SkillLearn learn : skills)
                if (learn.getId() == skill.getId() && learn.getLevel() <= skill.getLevel())
                    return true;
        return false;
    }

    public boolean isSkillPossible(Player player, Skill skill) {
        for (AcquireType aq : AcquireType.VALUES)
            if (isSkillPossible(player, skill, aq))
                return true;

        return false;
    }

    public List<SkillLearn> getSkillLearnListByItemId(Player player, int itemId) {
        List<SkillLearn> learns = _normalSkillTree.get(player.getActiveClassId());
        if (learns == null)
            return Collections.emptyList();

        List<SkillLearn> l = new ArrayList<SkillLearn>(1);
        for (SkillLearn i : learns)
            if (i.getItemId() == itemId)
                l.add(i);

        return l;
    }

    public List<SkillLearn> getSkillLearnForSpellBook(Player player, int itemId) {
        List<SkillLearn> learns = _spellBookSkillTree.get(player.getActiveClassId());
        if (learns == null)
            return Collections.emptyList();

        List<SkillLearn> l = new ArrayList<SkillLearn>(1);
        for (SkillLearn i : learns)
            if (i.getItemId() == itemId)
                l.add(i);

        return l;
    }

    public List<SkillLearn> getAllSpellbookSkillTree() {
        List<SkillLearn> a = new ArrayList<SkillLearn>();
        for (TIntObjectIterator<List<SkillLearn>> i = _spellBookSkillTree.iterator(); i.hasNext(); ) {
            i.advance();
            a.addAll(i.value());
        }

        return a;
    }

    public void addAllNormalSkillLearns(TIntObjectHashMap<List<SkillLearn>> map) {
        int classID;

        for (ClassId classId : ClassId.VALUES) {
            if (!classId.isDummy()) {

                classID = classId.getId();

                List<SkillLearn> temp;

                temp = map.get(classID);
                if (temp == null) {
                    info("Not found NORMAL skill learn for class " + classID);
                    continue;
                }

                _normalSkillTree.put(classId.getId(), temp);

                ClassId secondparent = classId.getParent(1);
                if (secondparent == classId.getParent(0))
                    secondparent = null;

                classId = classId.getParent(0);

                while (classId != null) {
                    List<SkillLearn> parentList = _normalSkillTree.get(classId.getId());
                    temp.addAll(parentList);

                    classId = classId.getParent(0);
                    if (classId == null && secondparent != null) {
                        classId = secondparent;
                        secondparent = secondparent.getParent(1);
                    }
                }
            }
        }
    }

    public Collection<SkillLearn> getAwakeParentSkillTree(Player player) {
        ClassId classId = player.getClassId();
        return getAwakeParentSkillTree(classId, ClassId.VALUES[player.getActiveDefaultClassId()]);
    }

    public Collection<SkillLearn> getAwakeParentSkillTree(ClassId classId, ClassId parentClassId) {
        TIntObjectHashMap<List<SkillLearn>> awakeParentSkillTree = (TIntObjectHashMap<List<SkillLearn>>) this._awakeParentSkillTree.get(classId.getId());
        if ((awakeParentSkillTree == null) || (awakeParentSkillTree.isEmpty())) {
            return Collections.emptyList();
        }
        int awakeParentId = classId.getAwakeParentId(parentClassId).getId();
        if (!awakeParentSkillTree.containsKey(awakeParentId)) {
            return Collections.emptyList();
        }
        return awakeParentSkillTree.get(awakeParentId);
    }

    public void addAllAwakeParentSkillLearns(TIntObjectHashMap<TIntObjectHashMap<List<SkillLearn>>> map) {
        this._awakeParentSkillTree.putAll(map);
    }

    public void addAllFishingLearns(int race, List<SkillLearn> s) {
        _fishingSkillTree.put(race, s);
    }

    public void addAllTransferLearns(int classId, List<SkillLearn> s) {
        _transferSkillTree.put(classId, s);
    }

    public void addAllTransformationLearns(int race, List<SkillLearn> s) {
        _transformationSkillTree.put(race, s);
    }

    public void addAllSpellbookLearns(TIntObjectHashMap<List<SkillLearn>> map) {
        int classID;

        for (ClassId classId : ClassId.VALUES) {
            if (classId.name().startsWith("dummyEntry"))
                continue;

            classID = classId.getId();

            List<SkillLearn> temp;

            temp = map.get(classID);
            if (temp == null) {
                continue;
            }

            _spellBookSkillTree.put(classId.getId(), temp);

            ClassId secondparent = classId.getParent(1);
            if (secondparent == classId.getParent(0))
                secondparent = null;

            classId = classId.getParent(0);

            while (classId != null) {
                List<SkillLearn> parentList = _spellBookSkillTree.get(classId.getId());
                classId = classId.getParent(0);

                if (parentList == null)
                    continue;

                temp.addAll(parentList);
                if (classId == null && secondparent != null) {
                    classId = secondparent;
                    secondparent = secondparent.getParent(1);
                }
            }
        }
    }

    public void addAllCertificationLearns(List<SkillLearn> s) {
        _certificationSkillTree.addAll(s);
    }

    public void addAllDualCertificationLearns(List<SkillLearn> s) {
        _dual_certificationSkillTree.addAll(s);
    }

    public void addAllCollectionLearns(List<SkillLearn> s) {
        _collectionSkillTree.addAll(s);
    }

    public void addAllSubUnitLearns(List<SkillLearn> s) {
        _subUnitSkillTree.addAll(s);
    }

    public void addAllPledgeLearns(List<SkillLearn> s) {
        _pledgeSkillTree.addAll(s);
    }

    @Override
    public void log() {
        info("load " + sizeTroveMap(_normalSkillTree) + " normal learns for " + _normalSkillTree.size() + " classes.");
        info("load " + sizeTroveMap(_spellBookSkillTree) + " spellbook learns for " + _spellBookSkillTree.size() + " classes.");
        info("load " + sizeTroveMap(_transferSkillTree) + " transfer learns for " + _transferSkillTree.size() + " classes.");
        //
        info("load " + sizeTroveMap(_transformationSkillTree) + " transformation learns for " + _transformationSkillTree.size() + " races.");
        info("load " + sizeTroveMap(_fishingSkillTree) + " fishing learns for " + _fishingSkillTree.size() + " races.");
        //
        info("load " + _certificationSkillTree.size() + " certification learns.");
        info("load " + _dual_certificationSkillTree.size() + " dual certification learns.");
        info("load " + _collectionSkillTree.size() + " collection learns.");
        info("load " + _pledgeSkillTree.size() + " pledge learns.");
        info("load " + _subUnitSkillTree.size() + " sub unit learns.");

        info("load " + sizeTroveMapMap(this._awakeParentSkillTree) + " awake parent learns for " + this._awakeParentSkillTree.size() + " classes.");

    }

    @Deprecated
    @Override
    public int size() {
        return 0;
    }

    @Override
    public void clear() {
        _normalSkillTree.clear();
        _fishingSkillTree.clear();
        _transferSkillTree.clear();
        _certificationSkillTree.clear();
        _dual_certificationSkillTree.clear();
        _collectionSkillTree.clear();
        _pledgeSkillTree.clear();
        _subUnitSkillTree.clear();
        _spellBookSkillTree.clear();
    }

    private int sizeTroveMap(TIntObjectHashMap<List<SkillLearn>> a) {
        int i = 0;
        for (TIntObjectIterator<List<SkillLearn>> iterator = a.iterator(); iterator.hasNext(); ) {
            iterator.advance();
            i += iterator.value().size();
        }

        return i;
    }

    private int sizeTroveMapMap(TIntObjectHashMap<TIntObjectHashMap<List<SkillLearn>>> a) {
        int i = 0;
        for (TIntObjectIterator<TIntObjectHashMap<List<SkillLearn>>> iterator = a.iterator(); iterator.hasNext(); ) {
            iterator.advance();
            i += sizeTroveMap(iterator.value());
        }

        return i;
    }
}
