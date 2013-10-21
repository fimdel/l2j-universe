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
package events.TrickOfTrans;

import java.util.ArrayList;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.Announcements;
import lineage2.gameserver.Config;
import lineage2.gameserver.listener.actor.OnDeathListener;
import lineage2.gameserver.listener.actor.player.OnPlayerEnterListener;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.SimpleSpawner;
import lineage2.gameserver.model.actor.listener.CharListenerList;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.scripts.ScriptFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class TrickOfTrans extends Functions implements ScriptFile, OnDeathListener, OnPlayerEnterListener
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(TrickOfTrans.class);
	/**
	 * Field EVENT_MANAGER_ID.
	 */
	private static final int EVENT_MANAGER_ID = 32132;
	/**
	 * Field CHESTS_ID.
	 */
	private static final int CHESTS_ID = 13036;
	/**
	 * Field RED_PSTC.
	 */
	private static final int RED_PSTC = 9162;
	/**
	 * Field BLUE_PSTC.
	 */
	private static final int BLUE_PSTC = 9163;
	/**
	 * Field ORANGE_PSTC.
	 */
	private static final int ORANGE_PSTC = 9164;
	/**
	 * Field BLACK_PSTC.
	 */
	private static final int BLACK_PSTC = 9165;
	/**
	 * Field WHITE_PSTC.
	 */
	private static final int WHITE_PSTC = 9166;
	/**
	 * Field GREEN_PSTC.
	 */
	private static final int GREEN_PSTC = 9167;
	/**
	 * Field RED_PSTC_R.
	 */
	private static final int RED_PSTC_R = 9171;
	/**
	 * Field BLUE_PSTC_R.
	 */
	private static final int BLUE_PSTC_R = 9172;
	/**
	 * Field ORANGE_PSTC_R.
	 */
	private static final int ORANGE_PSTC_R = 9173;
	/**
	 * Field BLACK_PSTC_R.
	 */
	private static final int BLACK_PSTC_R = 9174;
	/**
	 * Field WHITE_PSTC_R.
	 */
	private static final int WHITE_PSTC_R = 9175;
	/**
	 * Field GREEN_PSTC_R.
	 */
	private static final int GREEN_PSTC_R = 9176;
	/**
	 * Field A_CHEST_KEY.
	 */
	private static final int A_CHEST_KEY = 9205;
	/**
	 * Field _active.
	 */
	private static boolean _active = false;
	/**
	 * Field _em_spawns.
	 */
	private static final ArrayList<SimpleSpawner> _em_spawns = new ArrayList<>();
	/**
	 * Field _ch_spawns.
	 */
	private static final ArrayList<SimpleSpawner> _ch_spawns = new ArrayList<>();
	/**
	 * Field PhilosophersStoneOre.
	 */
	private static final int PhilosophersStoneOre = 9168;
	/**
	 * Field PhilosophersStoneOreMax.
	 */
	private static final int PhilosophersStoneOreMax = 17;
	/**
	 * Field PhilosophersStoneConversionFormula.
	 */
	private static final int PhilosophersStoneConversionFormula = 9169;
	/**
	 * Field MagicReagents.
	 */
	private static final int MagicReagents = 9170;
	/**
	 * Field MagicReagentsMax.
	 */
	private static final int MagicReagentsMax = 30;
	
	/**
	 * Method onLoad.
	 * @see lineage2.gameserver.scripts.ScriptFile#onLoad()
	 */
	@Override
	public void onLoad()
	{
		CharListenerList.addGlobal(this);
		if (isActive())
		{
			_active = true;
			spawnEventManagers();
			_log.info("Loaded Event: Trick of Trnasmutation [state: activated]");
		}
		else
		{
			_log.info("Loaded Event: Trick of Trnasmutation [state: deactivated]");
		}
	}
	
	/**
	 * Method isActive.
	 * @return boolean
	 */
	private static boolean isActive()
	{
		return IsActive("trickoftrans");
	}
	
	/**
	 * Method startEvent.
	 */
	public void startEvent()
	{
		final Player player = getSelf();
		if (!player.getPlayerAccess().IsEventGm)
		{
			return;
		}
		if (SetActive("trickoftrans", true))
		{
			spawnEventManagers();
			System.out.println("Event 'Trick of Transmutation' started.");
			Announcements.getInstance().announceByCustomMessage("scripts.events.TrickOfTrans.AnnounceEventStarted", null);
		}
		else
		{
			player.sendMessage("Event 'Trick of Transmutation' already started.");
		}
		_active = true;
		show("admin/events.htm", player);
	}
	
	/**
	 * Method stopEvent.
	 */
	public void stopEvent()
	{
		final Player player = getSelf();
		if (!player.getPlayerAccess().IsEventGm)
		{
			return;
		}
		if (SetActive("trickoftrans", false))
		{
			unSpawnEventManagers();
			System.out.println("Event 'Trick of Transmutation' stopped.");
			Announcements.getInstance().announceByCustomMessage("scripts.events.TrickOfTrans.AnnounceEventStoped", null);
		}
		else
		{
			player.sendMessage("Event 'Trick of Transmutation' not started.");
		}
		_active = false;
		show("admin/events.htm", player);
	}
	
	/**
	 * Method onPlayerEnter.
	 * @param player Player
	 * @see lineage2.gameserver.listener.actor.player.OnPlayerEnterListener#onPlayerEnter(Player)
	 */
	@Override
	public void onPlayerEnter(final Player player)
	{
		if (_active)
		{
			Announcements.getInstance().announceToPlayerByCustomMessage(player, "scripts.events.TrickOfTrans.AnnounceEventStarted", null);
		}
	}
	
	/**
	 * Method onReload.
	 * @see lineage2.gameserver.scripts.ScriptFile#onReload()
	 */
	@Override
	public void onReload()
	{
		unSpawnEventManagers();
	}
	
	/**
	 * Method onShutdown.
	 * @see lineage2.gameserver.scripts.ScriptFile#onShutdown()
	 */
	@Override
	public void onShutdown()
	{
		unSpawnEventManagers();
	}
	
	/**
	 * Method spawnEventManagers.
	 */
	private void spawnEventManagers()
	{
		final int[][] EVENT_MANAGERS =
		{
			{
				147992,
				28616,
				-2295,
				0
			},
			{
				81919,
				148290,
				-3472,
				51432
			},
			{
				18293,
				145208,
				-3081,
				6470
			},
			{
				-14694,
				122699,
				-3122,
				0
			},
			{
				-81634,
				150275,
				-3155,
				15863
			},
		};
		final int[][] CHESTS =
		{
			{
				148081,
				28614,
				-2274,
				2059
			},
			{
				147918,
				28615,
				-2295,
				31471
			},
			{
				147998,
				28534,
				-2274,
				49152
			},
			{
				148053,
				28550,
				-2274,
				55621
			},
			{
				147945,
				28563,
				-2274,
				40159
			},
			{
				82012,
				148286,
				-3472,
				61567
			},
			{
				81822,
				148287,
				-3493,
				29413
			},
			{
				81917,
				148207,
				-3493,
				49152
			},
			{
				81978,
				148228,
				-3472,
				53988
			},
			{
				81851,
				148238,
				-3472,
				40960
			},
			{
				18343,
				145253,
				-3096,
				7449
			},
			{
				18284,
				145274,
				-3090,
				19740
			},
			{
				18351,
				145186,
				-3089,
				61312
			},
			{
				18228,
				145265,
				-3079,
				21674
			},
			{
				18317,
				145140,
				-3078,
				55285
			},
			{
				-14584,
				122694,
				-3122,
				65082
			},
			{
				-14610,
				122756,
				-3143,
				13029
			},
			{
				-14628,
				122627,
				-3122,
				50632
			},
			{
				-14697,
				122607,
				-3143,
				48408
			},
			{
				-14686,
				122787,
				-3122,
				12416
			},
			{
				-81745,
				150275,
				-3134,
				32768
			},
			{
				-81520,
				150275,
				-3134,
				0
			},
			{
				-81628,
				150379,
				-3134,
				16025
			},
			{
				-81696,
				150347,
				-3155,
				22854
			},
			{
				-81559,
				150332,
				-3134,
				3356
			},
		};
		SpawnNPCs(EVENT_MANAGER_ID, EVENT_MANAGERS, _em_spawns);
		SpawnNPCs(CHESTS_ID, CHESTS, _ch_spawns);
	}
	
	/**
	 * Method unSpawnEventManagers.
	 */
	private void unSpawnEventManagers()
	{
		deSpawnNPCs(_em_spawns);
		deSpawnNPCs(_ch_spawns);
	}
	
	/**
	 * Method onDeath.
	 * @param cha Creature
	 * @param killer Creature
	 * @see lineage2.gameserver.listener.actor.OnDeathListener#onDeath(Creature, Creature)
	 */
	@Override
	public void onDeath(final Creature cha, final Creature killer)
	{
		if (_active && SimpleCheckDrop(cha, killer) && Rnd.chance(Config.EVENT_TRICK_OF_TRANS_CHANCE * killer.getPlayer().getRateItems() * Config.RATE_DROP_ITEMS * ((NpcInstance) cha).getTemplate().rateHp))
		{
			((NpcInstance) cha).dropItem(killer.getPlayer(), A_CHEST_KEY, 1);
		}
	}
	
	/**
	 * Method accept.
	 */
	public void accept()
	{
		final Player player = getSelf();
		if (!player.isQuestContinuationPossible(true))
		{
			return;
		}
		if (!player.findRecipe(RED_PSTC_R))
		{
			addItem(player, RED_PSTC, 1);
		}
		if (!player.findRecipe(BLACK_PSTC_R))
		{
			addItem(player, BLACK_PSTC, 1);
		}
		if (!player.findRecipe(BLUE_PSTC_R))
		{
			addItem(player, BLUE_PSTC, 1);
		}
		if (!player.findRecipe(GREEN_PSTC_R))
		{
			addItem(player, GREEN_PSTC, 1);
		}
		if (!player.findRecipe(ORANGE_PSTC_R))
		{
			addItem(player, ORANGE_PSTC, 1);
		}
		if (!player.findRecipe(WHITE_PSTC_R))
		{
			addItem(player, WHITE_PSTC, 1);
		}
		show("scripts/events/TrickOfTrans/TrickOfTrans_01.htm", player);
	}
	
	/**
	 * Method open.
	 */
	public void open()
	{
		final Player player = getSelf();
		if (getItemCount(player, A_CHEST_KEY) > 0)
		{
			removeItem(player, A_CHEST_KEY, 1);
			addItem(player, PhilosophersStoneOre, Rnd.get(1, PhilosophersStoneOreMax));
			addItem(player, MagicReagents, Rnd.get(1, MagicReagentsMax));
			if (Rnd.chance(80))
			{
				addItem(player, PhilosophersStoneConversionFormula, 1);
			}
			show("scripts/events/TrickOfTrans/TrickOfTrans_02.htm", player);
		}
		else
		{
			show("scripts/events/TrickOfTrans/TrickOfTrans_03.htm", player);
		}
	}
}
