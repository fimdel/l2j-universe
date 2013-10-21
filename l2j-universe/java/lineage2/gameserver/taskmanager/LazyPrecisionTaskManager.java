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
package lineage2.gameserver.taskmanager;

import java.util.concurrent.Future;

import lineage2.commons.threading.RunnableImpl;
import lineage2.commons.threading.SteppingRunnableQueueManager;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.Config;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.dao.AccountBonusDAO;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.actor.instances.player.Bonus;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.ExBR_PremiumState;
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage;
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage.ScreenMessageAlign;
import lineage2.gameserver.network.serverpackets.components.CustomMessage;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class LazyPrecisionTaskManager extends SteppingRunnableQueueManager
{
	/**
	 * Field _instance.
	 */
	private static final LazyPrecisionTaskManager _instance = new LazyPrecisionTaskManager();
	
	/**
	 * Method getInstance.
	 * @return LazyPrecisionTaskManager
	 */
	public static final LazyPrecisionTaskManager getInstance()
	{
		return _instance;
	}
	
	/**
	 * Constructor for LazyPrecisionTaskManager.
	 */
	private LazyPrecisionTaskManager()
	{
		super(1000L);
		ThreadPoolManager.getInstance().scheduleAtFixedRate(this, 1000L, 1000L);
		ThreadPoolManager.getInstance().scheduleAtFixedRate(new RunnableImpl()
		{
			@Override
			public void runImpl()
			{
				LazyPrecisionTaskManager.this.purge();
			}
		}, 60000L, 60000L);
	}
	
	/**
	 * Method addPCCafePointsTask.
	 * @param player Player
	 * @return Future<?>
	 */
	public Future<?> addPCCafePointsTask(final Player player)
	{
		long delay = Config.ALT_PCBANG_POINTS_DELAY * 60000L;
		return scheduleAtFixedRate(new RunnableImpl()
		{
			@Override
			public void runImpl()
			{
				if (player.isInOfflineMode() || (player.getLevel() < Config.ALT_PCBANG_POINTS_MIN_LVL))
				{
					return;
				}
				player.addPcBangPoints(Config.ALT_PCBANG_POINTS_BONUS, (Config.ALT_PCBANG_POINTS_BONUS_DOUBLE_CHANCE > 0) && Rnd.chance(Config.ALT_PCBANG_POINTS_BONUS_DOUBLE_CHANCE));
			}
		}, delay, delay);
	}
	
	/**
	 * Method startBonusExpirationTask.
	 * @param player Player
	 * @return Future<?>
	 */
	public Future<?> startBonusExpirationTask(final Player player)
	{
		long delay = (player.getBonus().getBonusExpire() * 1000L) - System.currentTimeMillis();
		return schedule(new RunnableImpl()
		{
			@Override
			public void runImpl()
			{
				player.getBonus().setRateXp(1.);
				player.getBonus().setRateSp(1.);
				player.getBonus().setDropAdena(1.);
				player.getBonus().setDropItems(1.);
				player.getBonus().setDropSpoil(1.);
				if (player.getParty() != null)
				{
					player.getParty().recalculatePartyData();
				}
				String msg = new CustomMessage("scripts.services.RateBonus.LuckEnded", player).toString();
				player.sendPacket(new ExShowScreenMessage(msg, 10000, ScreenMessageAlign.TOP_CENTER, true), new ExBR_PremiumState(player, false));
				player.sendMessage(msg);
				if (Config.SERVICES_RATE_TYPE == Bonus.BONUS_GLOBAL_ON_GAMESERVER)
				{
					AccountBonusDAO.getInstance().delete(player.getAccountName());
				}
			}
		}, delay);
	}
	
	/**
	 * Method addNpcAnimationTask.
	 * @param npc NpcInstance
	 * @return Future<?>
	 */
	public Future<?> addNpcAnimationTask(final NpcInstance npc)
	{
		return scheduleAtFixedRate(new RunnableImpl()
		{
			@Override
			public void runImpl()
			{
				if (npc.isVisible() && !npc.isActionsDisabled() && !npc.isMoving && !npc.isInCombat())
				{
					npc.onRandomAnimation();
				}
			}
		}, 1000L, Rnd.get(Config.MIN_NPC_ANIMATION, Config.MAX_NPC_ANIMATION) * 1000L);
	}
}
