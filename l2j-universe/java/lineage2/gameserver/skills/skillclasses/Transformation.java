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

import lineage2.gameserver.Config;
import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.instancemanager.ReflectionManager;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.templates.StatsSet;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Transformation extends Skill
{
	/**
	 * Field useSummon.
	 */
	public final boolean useSummon;
	/**
	 * Field isDisguise.
	 */
	public final boolean isDisguise;
	/**
	 * Field transformationName.
	 */
	public final String transformationName;
	
	/**
	 * Constructor for Transformation.
	 * @param set StatsSet
	 */
	public Transformation(StatsSet set)
	{
		super(set);
		useSummon = set.getBool("useSummon", false);
		isDisguise = set.getBool("isDisguise", false);
		transformationName = set.getString("transformationName", null);
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
	public boolean checkCondition(final Creature activeChar, final Creature target, boolean forceUse, boolean dontMove, boolean first)
	{
		Player player = target.getPlayer();
		if ((player == null) || (player.getActiveWeaponFlagAttachment() != null))
		{
			return false;
		}
		if ((player.getTransformation() != 0) && (getId() != SKILL_TRANSFORM_DISPEL))
		{
			activeChar.sendPacket(Msg.YOU_ALREADY_POLYMORPHED_AND_CANNOT_POLYMORPH_AGAIN);
			return false;
		}
		if (((getId() == SKILL_FINAL_FLYING_FORM) || (getId() == SKILL_AURA_BIRD_FALCON) || (getId() == SKILL_AURA_BIRD_OWL)) && ((player.getX() > -166168) || (player.getZ() <= 0) || (player.getZ() >= 6000) || (player.getSummonList().size() > 0) || (player.getReflection() != ReflectionManager.DEFAULT)))
		{
			activeChar.sendPacket(new SystemMessage(SystemMessage.S1_CANNOT_BE_USED_DUE_TO_UNSUITABLE_TERMS).addSkillName(_id, _level));
			return false;
		}
		if (player.isInFlyingTransform() && (getId() == SKILL_TRANSFORM_DISPEL) && (Math.abs(player.getZ() - player.getLoc().correctGeoZ().z) > 333))
		{
			activeChar.sendPacket(new SystemMessage(SystemMessage.S1_CANNOT_BE_USED_DUE_TO_UNSUITABLE_TERMS).addSkillName(_id, _level));
			return false;
		}
		if (player.isInWater())
		{
			activeChar.sendPacket(Msg.YOU_CANNOT_POLYMORPH_INTO_THE_DESIRED_FORM_IN_WATER);
			return false;
		}
		if (player.isRiding() || (player.getMountType() == 2))
		{
			activeChar.sendPacket(Msg.YOU_CANNOT_POLYMORPH_WHILE_RIDING_A_PET);
			return false;
		}
		if (player.getEffectList().getEffectsBySkillId(Skill.SKILL_MYSTIC_IMMUNITY) != null)
		{
			activeChar.sendPacket(Msg.YOU_CANNOT_POLYMORPH_WHILE_UNDER_THE_EFFECT_OF_A_SPECIAL_SKILL);
			return false;
		}
		if (player.isInBoat())
		{
			activeChar.sendPacket(Msg.YOU_CANNOT_POLYMORPH_WHILE_RIDING_A_BOAT);
			return false;
		}
		if ((player.getSummonList().getPet() != null) && (getId() != SKILL_TRANSFORM_DISPEL) && !isBaseTransformation())
		{
			activeChar.sendPacket(Msg.YOU_CANNOT_POLYMORPH_WHEN_YOU_HAVE_SUMMONED_A_SERVITOR_PET);
			return false;
		}
		return super.checkCondition(activeChar, target, forceUse, dontMove, first);
	}
	
	/**
	 * Method useSkill.
	 * @param activeChar Creature
	 * @param targets List<Creature>
	 */
	@Override
	public void useSkill(Creature activeChar, List<Creature> targets)
	{
		if (isSummonerTransformation() && activeChar.isPlayer())
		{
			activeChar.getPlayer().getSummonList().unsummonAllServitors();
		}
		for (Creature target : targets)
		{
			if ((target != null) && target.isPlayer())
			{
				getEffects(activeChar, target, false, false);
			}
		}
		if (isSSPossible())
		{
			if (!(Config.SAVING_SPS && (_skillType == SkillType.BUFF)))
			{
				activeChar.unChargeShots(isMagic());
			}
		}
	}
}
