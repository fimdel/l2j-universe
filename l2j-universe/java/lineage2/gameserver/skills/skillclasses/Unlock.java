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

import lineage2.commons.util.Rnd;
import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.instances.ChestInstance;
import lineage2.gameserver.model.instances.DoorInstance;
import lineage2.gameserver.templates.StatsSet;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Unlock extends Skill
{
	/**
	 * Field _unlockPower.
	 */
	private final int _unlockPower;
	
	/**
	 * Constructor for Unlock.
	 * @param set StatsSet
	 */
	public Unlock(StatsSet set)
	{
		super(set);
		_unlockPower = set.getInteger("unlockPower", 0) + 100;
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
		if ((target == null) || ((target instanceof ChestInstance) && target.isDead()))
		{
			activeChar.sendPacket(Msg.INVALID_TARGET);
			return false;
		}
		if ((target instanceof ChestInstance) && activeChar.isPlayer())
		{
			return super.checkCondition(activeChar, target, forceUse, dontMove, first);
		}
		if (!target.isDoor() || (_unlockPower == 0))
		{
			activeChar.sendPacket(Msg.INVALID_TARGET);
			return false;
		}
		DoorInstance door = (DoorInstance) target;
		if (door.isOpen())
		{
			activeChar.sendPacket(Msg.IT_IS_NOT_LOCKED);
			return false;
		}
		if (!door.isUnlockable())
		{
			activeChar.sendPacket(Msg.YOU_ARE_UNABLE_TO_UNLOCK_THE_DOOR);
			return false;
		}
		if (door.getKey() > 0)
		{
			activeChar.sendPacket(Msg.YOU_ARE_UNABLE_TO_UNLOCK_THE_DOOR);
			return false;
		}
		if ((_unlockPower - (door.getLevel() * 100)) < 0)
		{
			activeChar.sendPacket(Msg.YOU_ARE_UNABLE_TO_UNLOCK_THE_DOOR);
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
		for (Creature targ : targets)
		{
			if (targ != null)
			{
				if (targ.isDoor())
				{
					DoorInstance target = (DoorInstance) targ;
					if (!target.isOpen() && ((target.getKey() > 0) || Rnd.chance(_unlockPower - (target.getLevel() * 100))))
					{
						target.openMe((Player) activeChar, true);
					}
					else
					{
						activeChar.sendPacket(Msg.YOU_HAVE_FAILED_TO_UNLOCK_THE_DOOR);
					}
				}
				else if (targ instanceof ChestInstance)
				{
					ChestInstance target = (ChestInstance) targ;
					if (!target.isDead())
					{
						target.tryOpen((Player) activeChar, this);
					}
				}
			}
		}
	}
}
