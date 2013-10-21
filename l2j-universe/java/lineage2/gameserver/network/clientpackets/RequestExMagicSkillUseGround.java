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

import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.tables.SkillTable;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestExMagicSkillUseGround extends L2GameClientPacket
{
	/**
	 * Field _loc.
	 */
	private final Location _loc = new Location();
	/**
	 * Field _skillId.
	 */
	private int _skillId;
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
		_loc.x = readD();
		_loc.y = readD();
		_loc.z = readD();
		_skillId = readD();
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
		if (activeChar.isOutOfControl())
		{
			activeChar.sendActionFailed();
			return;
		}
		Skill skill = SkillTable.getInstance().getInfo(_skillId, activeChar.getSkillLevel(_skillId));
		if (skill != null)
		{
			if (skill.getAddedSkills().length == 0)
			{
				return;
			}
			if ((activeChar.getTransformation() != 0) && !activeChar.getAllSkills().contains(skill))
			{
				return;
			}
			if (!activeChar.isInRange(_loc, skill.getCastRange()))
			{
				activeChar.sendPacket(Msg.YOUR_TARGET_IS_OUT_OF_RANGE);
				activeChar.sendActionFailed();
				return;
			}
			Creature target = skill.getAimingTarget(activeChar, activeChar.getTarget());
			if (skill.checkCondition(activeChar, target, _ctrlPressed, _shiftPressed, true))
			{
				activeChar.setGroundSkillLoc(_loc);
				activeChar.getAI().Cast(skill, target, _ctrlPressed, _shiftPressed);
			}
			else
			{
				activeChar.sendActionFailed();
			}
		}
		else
		{
			activeChar.sendActionFailed();
		}
	}
}
