/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package lineage2.gameserver.skills.skillclasses;

import java.util.List;

import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Fishing;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.templates.StatsSet;
import lineage2.gameserver.templates.item.WeaponTemplate;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ReelingPumping extends Skill
{
	/**
	 * Constructor for ReelingPumping.
	 * @param set StatsSet
	 */
	public ReelingPumping(StatsSet set)
	{
		super(set);
	}
	
	/**
	 * Method checkCondition.
	 * @param activeChar Creature
	 * @param target Creature
	 * @param forceUse boolean
	 * @param dontMove boolean
	 * @param first boolean
	 * @return boolean
	 */
	@Override
	public boolean checkCondition(Creature activeChar, Creature target, boolean forceUse, boolean dontMove, boolean first)
	{
		if (!((Player) activeChar).isFishing())
		{
			activeChar.sendPacket(getSkillType() == SkillType.PUMPING ? Msg.PUMPING_SKILL_IS_AVAILABLE_ONLY_WHILE_FISHING : Msg.REELING_SKILL_IS_AVAILABLE_ONLY_WHILE_FISHING);
			activeChar.sendActionFailed();
			return false;
		}
		return super.checkCondition(activeChar, target, forceUse, dontMove, first);
	}
	
	/**
	 * Method useSkill.
	 * @param caster Creature
	 * @param targets List<Creature>
	 */
	@Override
	public void useSkill(Creature caster, List<Creature> targets)
	{
		if ((caster == null) || !caster.isPlayer())
		{
			return;
		}
		Player player = caster.getPlayer();
		Fishing fishing = player.getFishing();
		if ((fishing == null) || !fishing.isInCombat())
		{
			return;
		}
		WeaponTemplate weaponItem = player.getActiveWeaponItem();
		int SS = player.getChargedFishShot() ? 2 : 1;
		int pen = 0;
		double gradebonus = 1 + (weaponItem.getCrystalType().ordinal() * 0.1);
		int dmg = (int) (getPower() * gradebonus * SS);
		if (player.getSkillLevel(1315) < (getLevel() - 2))
		{
			player.sendPacket(Msg.SINCE_THE_SKILL_LEVEL_OF_REELING_PUMPING_IS_HIGHER_THAN_THE_LEVEL_OF_YOUR_FISHING_MASTERY_A_PENALTY_OF_S1_WILL_BE_APPLIED);
			pen = 50;
			int penatlydmg = dmg - pen;
			dmg = penatlydmg;
		}
		if (SS == 2)
		{
			player.unChargeFishShot();
		}
		fishing.useFishingSkill(dmg, pen, getSkillType());
	}
}
