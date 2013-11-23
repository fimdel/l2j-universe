package l2p.gameserver.skills.skillclasses;

import l2p.gameserver.cache.Msg;
import l2p.gameserver.data.xml.holder.NpcHolder;
import l2p.gameserver.model.*;
import l2p.gameserver.model.base.SummonType;
import l2p.gameserver.model.instances.PetInstance;
import l2p.gameserver.model.items.ItemInstance;
import l2p.gameserver.tables.PetDataTable;
import l2p.gameserver.templates.StatsSet;
import l2p.gameserver.templates.npc.NpcTemplate;
import l2p.gameserver.utils.Location;

import java.util.List;

public class PetSummon extends Skill {
    public PetSummon(StatsSet set) {
        super(set);
    }

    @Override
    public boolean checkCondition(Creature activeChar, Creature target, boolean forceUse, boolean dontMove, boolean first) {
        Player player = activeChar.getPlayer();
        if (player == null)
            return false;

        if (player.getPetControlItem() == null)
            return false;

        int npcId = PetDataTable.getSummonId(player.getPetControlItem());
        if (npcId == 0)
            return false;

        if (player.isInCombat() || player.getSummonList().isInCombat()) {
            player.sendPacket(Msg.YOU_CANNOT_SUMMON_DURING_COMBAT);
            return false;
        }

        if (player.isProcessingRequest()) {
            player.sendPacket(Msg.PETS_AND_SERVITORS_ARE_NOT_AVAILABLE_AT_THIS_TIME);
            return false;
        }

        if (player.isMounted() || !player.getSummonList().canSummon(SummonType.PET, 0)) {
            player.sendPacket(Msg.YOU_ALREADY_HAVE_A_PET);
            return false;
        }

        if (player.isInBoat()) {
            player.sendPacket(Msg.YOU_MAY_NOT_CALL_FORTH_A_PET_OR_SUMMONED_CREATURE_FROM_THIS_LOCATION);
            return false;
        }

        if (player.isInFlyingTransform())
            return false;

        if (player.isInOlympiadMode()) {
            player.sendPacket(Msg.THIS_ITEM_IS_NOT_AVAILABLE_FOR_THE_OLYMPIAD_EVENT);
            return false;
        }

        if (player.isCursedWeaponEquipped()) {
            player.sendPacket(Msg.YOU_MAY_NOT_USE_MULTIPLE_PETS_OR_SERVITORS_AT_THE_SAME_TIME);
            return false;
        }

        for (GameObject o : World.getAroundObjects(player, 120, 200))
            if (o.isDoor()) {
                player.sendPacket(Msg.YOU_MAY_NOT_SUMMON_FROM_YOUR_CURRENT_LOCATION);
                return false;
            }

        return super.checkCondition(activeChar, target, forceUse, dontMove, first);
    }

    @Override
    public void useSkill(Creature caster, List<Creature> targets) {
        Player activeChar = caster.getPlayer();
        ItemInstance controlItem = activeChar.getPetControlItem();
        if (controlItem == null)
            return;

        int npcId = PetDataTable.getSummonId(controlItem);
        if (npcId == 0)
            return;

        NpcTemplate petTemplate = NpcHolder.getInstance().getTemplate(npcId);
        if (petTemplate == null)
            return;

        PetInstance pet = PetInstance.restore(controlItem, petTemplate, activeChar);
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

        activeChar.getSummonList().addSummon(pet);
        pet.setHeading(activeChar.getHeading());
        pet.setReflection(activeChar.getReflection());
        pet.spawnMe(Location.findAroundPosition(activeChar, 50, 70));
        pet.setRunning();
        pet.setFollowMode(true);

        if (activeChar.isInOlympiadMode())
            pet.getEffectList().stopAllEffects();

        if (isSSPossible())
            caster.unChargeShots(isMagic());
    }
}
