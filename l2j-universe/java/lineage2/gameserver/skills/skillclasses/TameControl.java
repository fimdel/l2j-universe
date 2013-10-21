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
import lineage2.gameserver.ai.CtrlIntention;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.instances.TamedBeastInstance;
import lineage2.gameserver.templates.StatsSet;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class TameControl extends Skill
{
	/**
	 * Field _type.
	 */
	private final int _type;
	
	/**
	 * Constructor for TameControl.
	 * @param set StatsSet
	 */
	public TameControl(StatsSet set)
	{
		super(set);
		_type = set.getInteger("type", 0);
	}
	
	/**
	 * Method useSkill.
	 * @param activeChar Creature
	 * @param targets List<Creature>
	 */
	@Override
	public void useSkill(Creature activeChar, List<Creature> targets)
	{
		if (isSSPossible())
		{
			activeChar.unChargeShots(isMagic());
		}
		if (!activeChar.isPlayer())
		{
			return;
		}
		Player player = activeChar.getPlayer();
		if (player.getTrainedBeasts() == null)
		{
			return;
		}
		if (_type == 0)
		{
			for (Creature target : targets)
			{
				if ((target != null) && (target instanceof TamedBeastInstance))
				{
					if (player.getTrainedBeasts().get(target.getObjectId()) != null)
					{
						((TamedBeastInstance) target).despawnWithDelay(1000);
					}
				}
			}
		}
		else if (_type > 0)
		{
			if (_type == 1)
			{
				for (TamedBeastInstance tamedBeast : player.getTrainedBeasts().values())
				{
					tamedBeast.getAI().setIntention(CtrlIntention.AI_INTENTION_FOLLOW, player, Config.FOLLOW_RANGE);
				}
			}
			else if (_type == 3)
			{
				for (TamedBeastInstance tamedBeast : player.getTrainedBeasts().values())
				{
					tamedBeast.buffOwner();
				}
			}
			else if (_type == 4)
			{
				for (TamedBeastInstance tamedBeast : player.getTrainedBeasts().values())
				{
					tamedBeast.doDespawn();
				}
			}
		}
	}
}
