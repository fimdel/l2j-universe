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
import lineage2.gameserver.model.Effect;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill.SkillType;
import lineage2.gameserver.model.Summon;
import lineage2.gameserver.skills.EffectType;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestDispel extends L2GameClientPacket
{
	/**
	 * Field _level. Field _id. Field _objectId.
	 */
	private int _objectId, _id, _level;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_objectId = readD();
		_id = readD();
		_level = readD();
	}
	
	/**
	 * Method runImpl.
	 */
	@Override
	protected void runImpl()
	{
		Player activeChar = getClient().getActiveChar();
		Creature target = activeChar;
		if (activeChar == null)
		{
			return;
		}
		if (activeChar.getObjectId() != _objectId)
		{
			for (Summon summon : activeChar.getSummonList())
			{
				if (summon.getObjectId() == _objectId)
				{
					target = summon;
					break;
				}
			}
			if (target == null)
			{
				return;
			}
		}
		for (Effect e : target.getEffectList().getAllEffects())
		{
			if ((e.getDisplayId() == _id) && (e.getDisplayLevel() == _level))
			{
				if (!e.isOffensive() && !e.getSkill().isMusic() && e.getSkill().isSelfDispellable() && (e.getSkill().getSkillType() != SkillType.TRANSFORMATION) && (e.getTemplate().getEffectType() != EffectType.Hourglass))
				{
					e.exit();
				}
				else
				{
					return;
				}
			}
		}
	}
}
