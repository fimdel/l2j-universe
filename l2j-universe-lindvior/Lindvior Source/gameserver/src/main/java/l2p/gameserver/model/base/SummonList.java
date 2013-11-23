package l2p.gameserver.model.base;

import l2p.gameserver.Config;
import l2p.gameserver.dao.EffectsDAO;
import l2p.gameserver.dao.ServitorsDAO;
import l2p.gameserver.data.xml.holder.NpcHolder;
import l2p.gameserver.model.Creature;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.Skill;
import l2p.gameserver.model.Summon;
import l2p.gameserver.model.instances.PetBabyInstance;
import l2p.gameserver.model.instances.PetInstance;
import l2p.gameserver.model.items.ItemInstance;
import l2p.gameserver.skills.skillclasses.SummonServitor;
import l2p.gameserver.tables.PetDataTable;
import l2p.gameserver.tables.SkillTable;
import l2p.gameserver.templates.npc.NpcTemplate;
import l2p.gameserver.utils.Location;

import java.util.*;

public class SummonList implements Iterable<Summon> {
    public Player _owner;
    private PetInstance _pet;
    private final Map<Integer, Summon> _summonList;

    private int _usedPoints = 0;

    public SummonList(Player owner) {
        _owner = owner;
        _summonList = new HashMap<Integer, Summon>(2);
    }

    public boolean canSummon(SummonType summonType, int summonPoint) {
        if (_usedPoints + summonPoint > _owner.getSummonPointMax())
            return false;
        if (summonType == SummonType.PET) {
            return _pet == null;
        } else {
            synchronized (_summonList) {
                if (summonType == SummonType.SERVITOR && _summonList.size() > 0)
                    return false;
                if (summonType == SummonType.MULTI_SERVITOR) {
                    if (_summonList.size() == 2) {
                        return false;
                    }
                    Summon summon = getFirstServitor();
                    if (summon != null) {
                        Skill skill = SkillTable.getInstance().getInfo(summon.getSummonSkillId(), summon.getSummonSkillLvl());
                        if (skill != null)
                            return skill instanceof SummonServitor
                                    && ((SummonServitor) skill).getSummonType() == SummonType.MULTI_SERVITOR;
                    }
                }
            }
        }

        return true;
    }

    public void addSummon(Summon summon) {
        if (summon.isServitor()) {
            synchronized (_summonList) {
                _summonList.put(summon.getObjectId(), summon);
            }
            _usedPoints += summon.getSummonPoint();
        } else if (summon.isPet()) {
            _pet = (PetInstance) summon;
        }
        summon.setTitle(_owner.getName());
        _owner.autoShot();
        EffectsDAO.getInstance().insert(summon);
    }


    public void unsummonAll() {
        unsummonPet();
        unsummonAllServitors();
    }

    public void unsummonPet() {
        if (_pet != null) {
            _owner.unsetVar("pet");
            _pet.unSummon();
            _pet = null;
            _owner.setPetControlItem(null);
        }
    }

    public void unsummonAllServitors() {
        synchronized (_summonList) {
            for (Summon summon : _summonList.values()) {
                summon.unSummon();
            }
            _summonList.clear();
        }
        _usedPoints = 0;
    }

    public int getUsedPoints() {
        return _usedPoints;
    }

    public boolean isInCombat() {
        boolean isCombat = false;
        if (_summonList.size() > 0) {
            synchronized (_summonList) {
                for (Summon summon : _summonList.values()) {
                    if (summon.isInCombat()) {
                        isCombat = true;
                        break;
                    }
                }
            }
        }
        return isCombat || (_pet != null && _pet.isInCombat());
    }

    public void store(boolean storeServitors) {
        // Store servitors
        if (storeServitors) {
            if (_summonList.size() > 0) {
                synchronized (_summonList) {
                    for (Summon summon : _summonList.values()) {
                        ServitorsDAO.getInstance().store(summon);
                        summon.saveEffects();
                    }
                }
            }
        }

        // Store pet
        if (_pet != null) {
            _owner.setVar("pet", _owner.getPetControlItem().getObjectId(), -1);
            _pet.saveEffects();
            _pet.store();
        }
    }

    public void restore() {
        // Restore servitors
        List<Summon> summons = ServitorsDAO.getInstance().restore(_owner);
        if (summons.size() == 0)
            return;
        synchronized (_summonList) {
            for (Summon summon : summons) {
                addSummon(summon);
            }
        }

        // Restore pet
        int controlItemId = _owner.getVarInt("pet");
        if (controlItemId > 0) {
            ItemInstance controlItem = _owner.getInventory().getItemByObjectId(controlItemId);
            if (controlItem == null)
                return;
            _owner.setPetControlItem(controlItem);

            int npcId = PetDataTable.getSummonId(controlItem);
            if (npcId == 0)
                return;

            NpcTemplate petTemplate = NpcHolder.getInstance().getTemplate(npcId);
            if (petTemplate == null)
                return;

            PetInstance pet = PetInstance.restore(controlItem, petTemplate, _owner);
            if (pet == null)
                return;

            if (!pet.isRespawned()) {
                pet.setCurrentHp(pet.getMaxHp(), false);
                pet.setCurrentMp(pet.getMaxMp());
                pet.setCurrentFed(pet.getMaxFed());
                pet.updateControlItem();
                pet.store();
            }
            pet.getInventory().restore();

            addSummon(pet);
        }
    }

    /**
     * Вызывается только при первом onActive игрока после входа в мир
     */
    public void summonAll() {
        // Summon servitors
        if (_summonList.size() > 0) {
            synchronized (_summonList) {
                for (Summon summon : _summonList.values()) {
                    EffectsDAO.getInstance().restoreEffects(summon);
                    summon.setNonAggroTime(System.currentTimeMillis() + Config.NONAGGRO_TIME_ONTELEPORT);
                    summon.setReflection(_owner.getReflection());
                    summon.spawnMe(Location.findPointToStay(_owner, 50, 70));
                    summon.setRunning();
                    summon.setFollowMode(true);
                }
            }
        }

        // Summon pet
        if (_pet != null) {
            if (!_pet.isRespawned()) {
                _pet.setCurrentHp(_pet.getMaxHp(), false);
                _pet.setCurrentMp(_pet.getMaxMp());
                _pet.setCurrentFed(_pet.getMaxFed());
                _pet.updateControlItem();
                _pet.store();
            }
            _pet.getInventory().restore();

            _pet.setNonAggroTime(System.currentTimeMillis() + Config.NONAGGRO_TIME_ONTELEPORT);
            _pet.setReflection(_owner.getReflection());
            _pet.spawnMe(Location.findPointToStay(_owner, 50, 70));
            _pet.setRunning();
            _pet.setFollowMode(true);
            _pet.getInventory().validateItems();

            if (_pet instanceof PetBabyInstance)
                ((PetBabyInstance) _pet).startBuffTask();
        }
    }


    /**
     * Возвращает питомца
     *
     * @return
     */
    public PetInstance getPet() {
        return _pet;
    }

    /**
     * Возвращает слугу, если он один (для всех слуг, исключая четверопрофных слуг у Wynn Summoner)
     *
     * @return слуга, либо null
     */
    public Summon getFirstServitor() {
        if (_summonList.size() == 1) {
            Summon summon = _summonList.values().iterator().next();
            Skill skill = SkillTable.getInstance().getInfo(summon.getSummonSkillId(), summon.getSummonSkillLvl());
            if (skill == null || skill.getSkillType() != Skill.SkillType.SUMMON
                    || ((SummonServitor) skill).getSummonType() == SummonType.MULTI_SERVITOR)
                return null;
            return summon;
        }
        return null;
    }

    /**
     * Возвращает всех слуг
     *
     * @return
     */
    public List<Summon> getServitors() {
        if (_summonList.size() > 0) {
            List<Summon> servitors = new ArrayList<Summon>();
            synchronized (_summonList) {
                for (Summon summon : _summonList.values())
                    servitors.add(summon);
            }
            return servitors;
        }

        return Collections.emptyList();
    }

    public boolean contains(Creature creature) {
        if (creature == null)
            return false;
        if (_pet == creature)
            return true;

        if (_summonList.size() == 0)
            return false;
        synchronized (_summonList) {
            for (Summon summon : _summonList.values()) {
                if (summon == creature)
                    return true;
            }
        }

        return false;
    }

    @Override
    public Iterator<Summon> iterator() {
        List<Summon> summons = new ArrayList<Summon>(2);
        if (_pet != null)
            summons.add(_pet);
        if (_summonList.size() > 0) {
            synchronized (_summonList) {
                summons.addAll(_summonList.values());
            }
        }
        return Collections.unmodifiableList(summons).iterator();
    }

    public int size() {
        return _summonList.size() + (_pet != null ? 1 : 0);
    }
}
