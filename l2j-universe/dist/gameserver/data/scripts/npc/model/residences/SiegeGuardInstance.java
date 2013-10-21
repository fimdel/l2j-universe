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
package npc.model.residences;

import java.util.List;
import java.util.Map;

import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.base.Experience;
import lineage2.gameserver.model.entity.events.impl.SiegeEvent;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.model.reward.RewardItem;
import lineage2.gameserver.model.reward.RewardList;
import lineage2.gameserver.model.reward.RewardType;
import lineage2.gameserver.stats.Stats;
import lineage2.gameserver.templates.npc.NpcTemplate;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class SiegeGuardInstance extends NpcInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor for SiegeGuardInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public SiegeGuardInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
		setHasChatWindow(false);
	}
	
	/**
	 * Method isSiegeGuard.
	 * @return boolean
	 */
	@Override
	public boolean isSiegeGuard()
	{
		return true;
	}
	
	/**
	 * Method getAggroRange.
	 * @return int
	 */
	@Override
	public int getAggroRange()
	{
		return 1200;
	}
	
	/**
	 * Method isAutoAttackable.
	 * @param attacker Creature
	 * @return boolean
	 */
	@Override
	public boolean isAutoAttackable(Creature attacker)
	{
		Player player = attacker.getPlayer();
		if (player == null)
		{
			return false;
		}
		SiegeEvent<?, ?> siegeEvent = getEvent(SiegeEvent.class);
		SiegeEvent<?, ?> siegeEvent2 = attacker.getEvent(SiegeEvent.class);
		Clan clan = player.getClan();
		if (siegeEvent == null)
		{
			return false;
		}
		if ((clan != null) && (siegeEvent == siegeEvent2) && (siegeEvent.getSiegeClan(SiegeEvent.DEFENDERS, clan) != null))
		{
			return false;
		}
		return true;
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
	
	/**
	 * Method isInvul.
	 * @return boolean
	 */
	@Override
	public boolean isInvul()
	{
		return false;
	}
	
	/**
	 * Method onDeath.
	 * @param killer Creature
	 */
	@Override
	protected void onDeath(Creature killer)
	{
		SiegeEvent<?, ?> siegeEvent = getEvent(SiegeEvent.class);
		if (killer != null)
		{
			Player player = killer.getPlayer();
			if ((siegeEvent != null) && (player != null))
			{
				Clan clan = player.getClan();
				SiegeEvent<?, ?> siegeEvent2 = killer.getEvent(SiegeEvent.class);
				if ((clan != null) && (siegeEvent == siegeEvent2) && (siegeEvent.getSiegeClan(SiegeEvent.DEFENDERS, clan) == null))
				{
					Creature topdam = getAggroList().getTopDamager();
					if (topdam == null)
					{
						topdam = killer;
					}
					for (Map.Entry<RewardType, RewardList> entry : getTemplate().getRewards().entrySet())
					{
						rollRewards(entry, killer, topdam);
					}
				}
			}
		}
		super.onDeath(killer);
	}
	
	/**
	 * Method rollRewards.
	 * @param entry Map.Entry<RewardType,RewardList>
	 * @param lastAttacker Creature
	 * @param topDamager Creature
	 */
	public void rollRewards(Map.Entry<RewardType, RewardList> entry, final Creature lastAttacker, Creature topDamager)
	{
		RewardList list = entry.getValue();
		final Player activePlayer = topDamager.getPlayer();
		if (activePlayer == null)
		{
			return;
		}
		final int diff = calculateLevelDiffForDrop(topDamager.getLevel());
		double mod = calcStat(Stats.REWARD_MULTIPLIER, 1., topDamager, null);
		mod *= Experience.penaltyModifier(diff, 9);
		List<RewardItem> rewardItems = list.roll(activePlayer, mod, false, true);
		for (RewardItem drop : rewardItems)
		{
			dropItem(activePlayer, drop.itemId, drop.count);
		}
	}
	
	/**
	 * Method isFearImmune.
	 * @return boolean
	 */
	@Override
	public boolean isFearImmune()
	{
		return true;
	}
	
	/**
	 * Method isParalyzeImmune.
	 * @return boolean
	 */
	@Override
	public boolean isParalyzeImmune()
	{
		return true;
	}
	
	/**
	 * Method getClan.
	 * @return Clan
	 */
	@Override
	public Clan getClan()
	{
		return null;
	}
}
