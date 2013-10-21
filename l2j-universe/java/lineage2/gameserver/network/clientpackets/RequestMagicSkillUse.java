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
package lineage2.gameserver.network.clientpackets;

import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.Skill.SkillType;
import lineage2.gameserver.model.items.attachment.FlagItemAttachment;
import lineage2.gameserver.tables.SkillTable;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestMagicSkillUse extends L2GameClientPacket
{
	/**
	 * Field _magicId.
	 */
	private Integer _magicId;
	/**
	 * Field _ctrlPressed.
	 */
	private boolean _ctrlPressed;
	/**
	 * Field _shiftPressed.
	 */
	private boolean _shiftPressed;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_magicId = readD();
		_ctrlPressed = readD() != 0;
		_shiftPressed = readC() != 0;
	}
	
	/**
	 * Method runImpl.
	 */
	@Override
	protected void runImpl()
	{
		Player activeChar = getClient().getActiveChar();
		if (activeChar == null)
		{
			return;
		}
		activeChar.setActive();
		if (activeChar.isOutOfControl())
		{
			activeChar.sendActionFailed();
			return;
		}
		Skill skill = SkillTable.getInstance().getInfo(_magicId, activeChar.getSkillLevel(_magicId));
		if (skill != null)
		{
			if (!(skill.isActive() || skill.isToggle()))
			{
				return;
			}
			FlagItemAttachment attachment = activeChar.getActiveWeaponFlagAttachment();
			if ((attachment != null) && !attachment.canCast(activeChar, skill))
			{
				activeChar.sendActionFailed();
				return;
			}
			if ((activeChar.getTransformation() != 0) && !activeChar.getAllSkills().contains(skill))
			{
				return;
			}
			if (skill.isToggle())
			{
				if (activeChar.getEffectList().getEffectsBySkill(skill) != null)
				{
					activeChar.getEffectList().stopEffect(skill.getId());
					activeChar.sendActionFailed();
					return;
				}
			}
			if (skill.getSkillType() == SkillType.EMDAM)
			{
				int inc = 0;
				if (activeChar.getEffectList().getEffectsBySkillId(Skill.SKILL_DUAL_CAST) != null)
				{
					inc = 5;
				}
				else if (activeChar.getEffectList().getEffectsBySkillId(Skill.SKILL_TRUE_FIRE) != null)
				{
					inc = 1;
				}
				else if (activeChar.getEffectList().getEffectsBySkillId(Skill.SKILL_TRUE_WATER) != null)
				{
					inc = 2;
				}
				else if (activeChar.getEffectList().getEffectsBySkillId(Skill.SKILL_TRUE_WIND) != null)
				{
					inc = 3;
				}
				else if (activeChar.getEffectList().getEffectsBySkillId(Skill.SKILL_TRUE_EARTH) != null)
				{
					inc = 4;
				}
				skill = SkillTable.getInstance().getInfo(skill.getId() + inc, skill.getLevel());
			}
			Creature target = skill.getAimingTarget(activeChar, activeChar.getTarget());
			activeChar.setGroundSkillLoc(null);
			if(activeChar.isStunned() && skill.isCastOverStun())
			{
				activeChar.doCast(skill, target, _ctrlPressed);
				return;
			}
			activeChar.getAI().Cast(skill, target, _ctrlPressed, _shiftPressed);
		}
		else
		{
			activeChar.sendActionFailed();
		}
	}
}
