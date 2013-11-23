package l2p.gameserver.skills.skillclasses;

import l2p.gameserver.cache.Msg;
import l2p.gameserver.model.Creature;
import l2p.gameserver.model.Fishing;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.Skill;
import l2p.gameserver.templates.StatsSet;
import l2p.gameserver.templates.item.WeaponTemplate;

import java.util.List;

public class ReelingPumping extends Skill {

    public ReelingPumping(StatsSet set) {
        super(set);
    }

    @Override
    public boolean checkCondition(Creature activeChar, Creature target, boolean forceUse, boolean dontMove, boolean first) {
        if (!((Player) activeChar).isFishing()) {
            activeChar.sendPacket(getSkillType() == SkillType.PUMPING ? Msg.PUMPING_SKILL_IS_AVAILABLE_ONLY_WHILE_FISHING : Msg.REELING_SKILL_IS_AVAILABLE_ONLY_WHILE_FISHING);
            activeChar.sendActionFailed();
            return false;
        }
        return super.checkCondition(activeChar, target, forceUse, dontMove, first);
    }

    @Override
    public void useSkill(Creature caster, List<Creature> targets) {
        if (caster == null || !caster.isPlayer())
            return;

        Player player = caster.getPlayer();
        Fishing fishing = player.getFishing();
        if (fishing == null || !fishing.isInCombat())
            return;

        WeaponTemplate weaponItem = player.getActiveWeaponItem();
        int SS = player.getChargedFishShot() ? 2 : 1;
        int pen = 0;
        double gradebonus = 1 + weaponItem.getCrystalType().ordinal() * 0.1;
        int dmg = (int) (getPower() * gradebonus * SS);

        if (player.getSkillLevel(1315) < getLevel() - 2) // 1315 - Fish Expertise
        {
            // Penalty
            player.sendPacket(Msg.SINCE_THE_SKILL_LEVEL_OF_REELING_PUMPING_IS_HIGHER_THAN_THE_LEVEL_OF_YOUR_FISHING_MASTERY_A_PENALTY_OF_S1_WILL_BE_APPLIED);
            pen = 50;
            int penatlydmg = dmg - pen;
            dmg = penatlydmg;
        }

        if (SS == 2)
            player.unChargeFishShot();

        fishing.useFishingSkill(dmg, pen, getSkillType());
    }
}
