package l2p.gameserver.skills.skillclasses;

import l2p.gameserver.ThreadPoolManager;
import l2p.gameserver.cache.Msg;
import l2p.gameserver.data.xml.holder.NpcHolder;
import l2p.gameserver.idfactory.IdFactory;
import l2p.gameserver.model.*;
import l2p.gameserver.model.base.Experience;
import l2p.gameserver.model.base.SummonType;
import l2p.gameserver.model.entity.events.impl.SiegeEvent;
import l2p.gameserver.model.instances.*;
import l2p.gameserver.network.serverpackets.SystemMessage2;
import l2p.gameserver.network.serverpackets.components.SystemMsg;
import l2p.gameserver.stats.Stats;
import l2p.gameserver.stats.funcs.FuncAdd;
import l2p.gameserver.tables.SkillTable;
import l2p.gameserver.templates.StatsSet;
import l2p.gameserver.templates.npc.NpcTemplate;
import l2p.gameserver.utils.Location;

import java.util.List;

public class SummonServitor extends Skill {
    private final SummonType _summonType;

    private final double _expPenalty;
    private final int _lifeTime;
    private final int _summonPoint;

    public SummonServitor(StatsSet set) {
        super(set);

        _summonType = Enum.valueOf(SummonType.class, set.getString("summonType", "SERVITOR").toUpperCase());
        _expPenalty = set.getDouble("expPenalty", 0.f);
        _lifeTime = set.getInteger("lifeTime", 1200) * 1000;
        _summonPoint = set.getInteger("summonPoint", 0);
    }

    @Override
    public boolean checkCondition(Creature activeChar, Creature target, boolean forceUse, boolean dontMove, boolean first) {
        Player player = activeChar.getPlayer();
        if (player == null)
            return false;

        if (player.isProcessingRequest()) {
            player.sendPacket(Msg.PETS_AND_SERVITORS_ARE_NOT_AVAILABLE_AT_THIS_TIME);
            return false;
        }

        switch (_summonType) {
            case TRAP:
                if (player.isInZonePeace()) {
                    activeChar.sendPacket(Msg.A_MALICIOUS_SKILL_CANNOT_BE_USED_IN_A_PEACE_ZONE);
                    return false;
                }
                break;
            case CLONE:
                // TODO: Спавн клона чара
                break;
            case TREE:
                if (player.getTree()) {
                    player.sendMessage("You already have a tree.");
                    return false;
                }
                break;
            case SERVITOR:
            case MULTI_SERVITOR:
            case SIEGE_SUMMON:
                if (player.isMounted() || !player.getSummonList().canSummon(_summonType, _summonPoint)) {
                    player.sendPacket(new SystemMessage2(SystemMsg.S1_CANNOT_BE_USED_DUE_TO_UNSUITABLE_TERMS).addSkillName(this));
                    return false;
                }
                break;
            case AGATHION:
                if (player.getAgathionId() > 0 && _npcId != 0) {
                    player.sendPacket(SystemMsg.AN_AGATHION_HAS_ALREADY_BEEN_SUMMONED);
                    return false;
                }
            default:
                break;
        }

        return super.checkCondition(activeChar, target, forceUse, dontMove, first);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void useSkill(Creature caster, List<Creature> targets) {
        Player activeChar = caster.getPlayer();

        switch (_summonType) {
            case AGATHION:
                activeChar.setAgathion(getNpcId());
                break;
            case TRAP:
                Skill trapSkill = getFirstAddedSkill();

                if (activeChar.getTrapsCount() >= 5)
                    activeChar.destroyFirstTrap();
                TrapInstance trap = new TrapInstance(IdFactory.getInstance().getNextId(), NpcHolder.getInstance().getTemplate(getNpcId()), activeChar, trapSkill);
                activeChar.addTrap(trap);
                trap.spawnMe();
                break;
            case CLONE:
                FakePlayer fp = new FakePlayer(IdFactory.getInstance().getNextId(), activeChar.getTemplate(), activeChar);
                fp.spawnMe(Location.findAroundPosition(activeChar, 50, 70));
                fp = new FakePlayer(IdFactory.getInstance().getNextId(), activeChar.getTemplate(), activeChar);
                fp.spawnMe(Location.findAroundPosition(activeChar, 50, 70));
                break;
            case TREE:
                if (activeChar.getTree()) {
                    activeChar.sendMessage("You already have a tree.");
                    return;
                }
                Skill treeSkill = getFirstAddedSkill();
                TreeInstance tree = new TreeInstance(IdFactory.getInstance().getNextId(), NpcHolder.getInstance().getTemplate(getNpcId()), activeChar, _lifeTime, treeSkill);
                activeChar.setTree(true);
                tree.spawnMe(Location.findAroundPosition(activeChar, 50, 70));
                break;
            case SERVITOR:
            case MULTI_SERVITOR:
            case SIEGE_SUMMON:
                // Удаление трупа, если идет суммон из трупа.
                Location loc = null;
                if (_targetType == SkillTargetType.TARGET_CORPSE) {
                    for (Creature target : targets)
                        if (target != null && target.isDead()) {
                            activeChar.getAI().setAttackTarget(null);
                            loc = target.getLoc();
                            if (target.isNpc())
                                ((NpcInstance) target).endDecayTask();
                            else if (target.isServitor())
                                ((SummonInstance) target).endDecayTask();
                            else
                                return; // кто труп ?
                        }
                }
                if (activeChar.isMounted() || !activeChar.getSummonList().canSummon(_summonType, _summonPoint)) {
                    return;
                }
                NpcTemplate summonTemplate = NpcHolder.getInstance().getTemplate(getNpcId());
                SummonInstance summon = new SummonInstance(IdFactory.getInstance().getNextId(), summonTemplate, activeChar, _lifeTime, _summonPoint, this);
                activeChar.getSummonList().addSummon(summon);

                summon.setExpPenalty(_expPenalty);
                summon.setExp(Experience.LEVEL[Math.min(summon.getLevel(), Experience.LEVEL.length - 1)]);
                summon.setHeading(activeChar.getHeading());
                summon.setReflection(activeChar.getReflection());
                summon.spawnMe(loc == null ? Location.findAroundPosition(activeChar, 50, 70) : loc);
                summon.setRunning();
                summon.setFollowMode(true);

                if (summon.getSkillLevel(4140) > 0)
                    summon.altUseSkill(SkillTable.getInstance().getInfo(4140, summon.getSkillLevel(4140)), activeChar);

                if (summon.getName().equalsIgnoreCase("Shadow"))//FIXME [G1ta0] идиотский хардкод
                    summon.addStatFunc(new FuncAdd(Stats.ABSORB_DAMAGE_PERCENT, 0x40, this, 15));

                if (activeChar.isInOlympiadMode())
                    summon.getEffectList().stopAllEffects();

                summon.setCurrentHpMp(summon.getMaxHp(), summon.getMaxMp(), false);

                if (_summonType == SummonType.SIEGE_SUMMON) {
                    SiegeEvent siegeEvent = activeChar.getEvent(SiegeEvent.class);

                    siegeEvent.addSiegeSummon(summon);
                }
                break;
            case MERCHANT:
                if (activeChar.getSummonList().size() > 0 || activeChar.isMounted())
                    return;

                NpcTemplate merchantTemplate = NpcHolder.getInstance().getTemplate(getNpcId());
                MerchantInstance merchant = new MerchantInstance(IdFactory.getInstance().getNextId(), merchantTemplate);

                merchant.setCurrentHp(merchant.getMaxHp(), false);
                merchant.setCurrentMp(merchant.getMaxMp());
                merchant.setHeading(activeChar.getHeading());
                merchant.setReflection(activeChar.getReflection());
                merchant.spawnMe(activeChar.getLoc());

                ThreadPoolManager.getInstance().schedule(new GameObjectTasks.DeleteTask(merchant), _lifeTime);
                break;
            default:
                break;
        }


        if (isSSPossible())
            caster.unChargeShots(isMagic());
    }

    public final int getLifeTime() {
        return _lifeTime;
    }

    public final int getSummonPoint() {
        return _summonPoint;
    }

    public final SummonType getSummonType() {
        return _summonType;
    }

    @Override
    public boolean isOffensive() {
        return _targetType == SkillTargetType.TARGET_CORPSE;
    }
}
