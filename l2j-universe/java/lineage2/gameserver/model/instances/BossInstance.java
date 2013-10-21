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
package lineage2.gameserver.model.instances;

import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.Hero;
import lineage2.gameserver.model.entity.HeroDiary;
import lineage2.gameserver.templates.npc.NpcTemplate;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class BossInstance extends RaidBossInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field _teleportedToNest.
	 */
	private boolean _teleportedToNest;
	
	/**
	 * Constructor for BossInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public BossInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	/**
	 * Method isBoss.
	 * @return boolean
	 */
	@Override
	public boolean isBoss()
	{
		return true;
	}
	
	/**
	 * Method isMovementDisabled.
	 * @return boolean
	 */
	@Override
	public final boolean isMovementDisabled()
	{
		return (getNpcId() == 29006) || super.isMovementDisabled();
	}
	
	/**
	 * Method onDeath.
	 * @param killer Creature
	 */
	@Override
	protected void onDeath(Creature killer)
	{
		if (killer.isPlayable())
		{
			Player player = killer.getPlayer();
			if (player.isInParty())
			{
				for (Player member : player.getParty().getPartyMembers())
				{
					if (member.isNoble())
					{
						Hero.getInstance().addHeroDiary(member.getObjectId(), HeroDiary.ACTION_RAID_KILLED, getNpcId());
					}
				}
			}
			else if (player.isNoble())
			{
				Hero.getInstance().addHeroDiary(player.getObjectId(), HeroDiary.ACTION_RAID_KILLED, getNpcId());
			}
		}
		super.onDeath(killer);
	}
	
	/**
	 * Method setTeleported.
	 * @param flag boolean
	 */
	public void setTeleported(boolean flag)
	{
		_teleportedToNest = flag;
	}
	
	/**
	 * Method isTeleported.
	 * @return boolean
	 */
	public boolean isTeleported()
	{
		return _teleportedToNest;
	}
	
	/**
	 * Method hasRandomAnimation.
	 * @return boolean
	 */
	@Override
	public boolean hasRandomAnimation()
	{
		return false;
	}
}
