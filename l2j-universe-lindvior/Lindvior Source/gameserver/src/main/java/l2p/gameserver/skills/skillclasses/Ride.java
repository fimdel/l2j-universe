package l2p.gameserver.skills.skillclasses;

import l2p.gameserver.cache.Msg;
import l2p.gameserver.model.Creature;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.Skill;
import l2p.gameserver.templates.StatsSet;

import java.util.List;

public class Ride extends Skill {
    public Ride(StatsSet set) {
        super(set);
    }

    @Override
    public boolean checkCondition(Creature activeChar, Creature target, boolean forceUse, boolean dontMove, boolean first) {
        if (!activeChar.isPlayer())
            return false;

        Player player = (Player) activeChar;
        if (getNpcId() != 0) {
            if (player.isInOlympiadMode()) {
                player.sendPacket(Msg.THIS_ITEM_IS_NOT_AVAILABLE_FOR_THE_OLYMPIAD_EVENT);
                return false;
            }
            if (player.isInDuel() || player.isSitting() || player.isInCombat() || player.isFishing() || player.isCursedWeaponEquipped() || player.getTransformation() != 0 || player.getSummonList().size() > 0 || player.isMounted() || player.isInBoat()) {
                player.sendPacket(Msg.YOU_CANNOT_MOUNT_BECAUSE_YOU_DO_NOT_MEET_THE_REQUIREMENTS);
                return false;
            }
        } else if (getNpcId() == 0 && !player.isMounted())
            return false;

        return super.checkCondition(activeChar, target, forceUse, dontMove, first);
    }

    @Override
    public void useSkill(Creature caster, List<Creature> targets) {
        if (!caster.isPlayer())
            return;

        Player activeChar = (Player) caster;
        activeChar.setMount(getNpcId(), 0, 0);
    }
}
