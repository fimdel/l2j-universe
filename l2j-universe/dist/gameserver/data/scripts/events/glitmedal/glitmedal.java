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
package events.glitmedal;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.Announcements;
import lineage2.gameserver.Config;
import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.data.xml.holder.MultiSellHolder;
import lineage2.gameserver.listener.actor.OnDeathListener;
import lineage2.gameserver.listener.actor.player.OnPlayerEnterListener;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.SimpleSpawner;
import lineage2.gameserver.model.actor.listener.CharListenerList;
import lineage2.gameserver.model.instances.MonsterInstance;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.utils.Util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class glitmedal extends Functions implements ScriptFile, OnDeathListener, OnPlayerEnterListener
{
	/**
	 * Field EVENT_MANAGER_ID1.
	 */
	private static int EVENT_MANAGER_ID1 = 31228;
	/**
	 * Field EVENT_MANAGER_ID2.
	 */
	private static int EVENT_MANAGER_ID2 = 31229;
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(glitmedal.class);
	/**
	 * Field isTalker.
	 */
	private int isTalker;
	/**
	 * Field EVENT_MEDAL.
	 */
	private static int EVENT_MEDAL = 6392;
	/**
	 * Field EVENT_GLITTMEDAL.
	 */
	private static int EVENT_GLITTMEDAL = 6393;
	/**
	 * Field Badge_of_Rabbit.
	 */
	private static int Badge_of_Rabbit = 6399;
	/**
	 * Field Badge_of_Hyena.
	 */
	private static int Badge_of_Hyena = 6400;
	/**
	 * Field Badge_of_Fox.
	 */
	private static int Badge_of_Fox = 6401;
	/**
	 * Field Badge_of_Wolf.
	 */
	private static int Badge_of_Wolf = 6402;
	/**
	 * Field _spawns.
	 */
	private static List<SimpleSpawner> _spawns = new ArrayList<>();
	/**
	 * Field _active.
	 */
	private static boolean _active = false;
	/**
	 * Field MultiSellLoaded.
	 */
	private static boolean MultiSellLoaded = false;
	/**
	 * Field multiSellFiles.
	 */
	private static File[] multiSellFiles =
	{
		new File(Config.DATAPACK_ROOT, "data/xml/other/event/glitmedal/502.xml"),
		new File(Config.DATAPACK_ROOT, "data/xml/other/event/glitmedal/503.xml"),
		new File(Config.DATAPACK_ROOT, "data/xml/other/event/glitmedal/504.xml"),
		new File(Config.DATAPACK_ROOT, "data/xml/other/event/glitmedal/505.xml"),
		new File(Config.DATAPACK_ROOT, "data/xml/other/event/glitmedal/506.xml"),
	};
	
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
			loadMultiSell();
			spawnEventManagers();
			_log.info("Loaded Event: L2 Medal Collection Event [state: activated]");
		}
		else
		{
			_log.info("Loaded Event: L2 Medal Collection Event [state: deactivated]");
		}
	}
	
	/**
	 * Method isActive.
	 * @return boolean
	 */
	private static boolean isActive()
	{
		return IsActive("glitter");
	}
	
	/**
	 * Method startEvent.
	 */
	public void startEvent()
	{
		Player player = getSelf();
		if (!player.getPlayerAccess().IsEventGm)
		{
			return;
		}
		if (SetActive("glitter", true))
		{
			loadMultiSell();
			spawnEventManagers();
			System.out.println("Event 'L2 Medal Collection Event' started.");
			Announcements.getInstance().announceByCustomMessage("scripts.events.glitmedal.AnnounceEventStarted", null);
		}
		else
		{
			player.sendMessage("Event 'L2 Medal Collection Event' already started.");
		}
		_active = true;
		show("admin/events.htm", player);
	}
	
	/**
	 * Method stopEvent.
	 */
	public void stopEvent()
	{
		Player player = getSelf();
		if (!player.getPlayerAccess().IsEventGm)
		{
			return;
		}
		if (SetActive("glitter", false))
		{
			unSpawnEventManagers();
			System.out.println("Event 'L2 Medal Collection Event' stopped.");
			Announcements.getInstance().announceByCustomMessage("scripts.events.glitmedal.AnnounceEventStoped", null);
		}
		else
		{
			player.sendMessage("Event 'L2 Medal Collection Event' not started.");
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
	public void onPlayerEnter(Player player)
	{
		if (_active)
		{
			Announcements.getInstance().announceToPlayerByCustomMessage(player, "scripts.events.glitmedal.AnnounceEventStarted", null);
		}
	}
	
	/**
	 * Method spawnEventManagers.
	 */
	private void spawnEventManagers()
	{
		final int[][] EVENT_MANAGERS1 =
		{
			{
				147893,
				-56622,
				-2776,
				0
			},
			{
				-81070,
				149960,
				-3040,
				0
			},
			{
				82882,
				149332,
				-3464,
				49000
			},
			{
				44176,
				-48732,
				-800,
				33000
			},
			{
				147920,
				25664,
				-2000,
				16384
			},
			{
				117498,
				76630,
				-2695,
				38000
			},
			{
				111776,
				221104,
				-3543,
				16384
			},
			{
				-84516,
				242971,
				-3730,
				34000
			},
			{
				-13073,
				122801,
				-3117,
				0
			},
			{
				-44337,
				-113669,
				-224,
				0
			},
			{
				11281,
				15652,
				-4584,
				25000
			},
			{
				44122,
				50784,
				-3059,
				57344
			},
			{
				80986,
				54504,
				-1525,
				32768
			},
			{
				114733,
				-178691,
				-821,
				0
			},
			{
				18178,
				145149,
				-3054,
				7400
			},
		};
		final int[][] EVENT_MANAGERS2 =
		{
			{
				147960,
				-56584,
				-2776,
				0
			},
			{
				-81070,
				149860,
				-3040,
				0
			},
			{
				82798,
				149332,
				-3464,
				49000
			},
			{
				44176,
				-48688,
				-800,
				33000
			},
			{
				147985,
				25664,
				-2000,
				16384
			},
			{
				117459,
				76664,
				-2695,
				38000
			},
			{
				111724,
				221111,
				-3543,
				16384
			},
			{
				-84516,
				243015,
				-3730,
				34000
			},
			{
				-13073,
				122841,
				-3117,
				0
			},
			{
				-44342,
				-113726,
				-240,
				0
			},
			{
				11327,
				15682,
				-4584,
				25000
			},
			{
				44157,
				50827,
				-3059,
				57344
			},
			{
				80986,
				54452,
				-1525,
				32768
			},
			{
				114719,
				-178742,
				-821,
				0
			},
			{
				18154,
				145192,
				-3054,
				7400
			},
		};
		SpawnNPCs(EVENT_MANAGER_ID1, EVENT_MANAGERS1, _spawns);
		SpawnNPCs(EVENT_MANAGER_ID2, EVENT_MANAGERS2, _spawns);
	}
	
	/**
	 * Method unSpawnEventManagers.
	 */
	private void unSpawnEventManagers()
	{
		deSpawnNPCs(_spawns);
	}
	
	/**
	 * Method loadMultiSell.
	 */
	private static void loadMultiSell()
	{
		if (MultiSellLoaded)
		{
			return;
		}
		for (File f : multiSellFiles)
		{
			MultiSellHolder.getInstance().parseFile(f);
		}
		MultiSellLoaded = true;
	}
	
	/**
	 * Method onReload.
	 * @see lineage2.gameserver.scripts.ScriptFile#onReload()
	 */
	@Override
	public void onReload()
	{
		unSpawnEventManagers();
		if (MultiSellLoaded)
		{
			for (File f : multiSellFiles)
			{
				MultiSellHolder.getInstance().remove(f);
			}
			MultiSellLoaded = false;
		}
	}
	
	/**
	 * Method onShutdown.
	 * @see lineage2.gameserver.scripts.ScriptFile#onShutdown()
	 */
	@Override
	public void onShutdown()
	{
		// empty method
	}
	
	/**
	 * Method onDeath.
	 * @param cha Creature
	 * @param killer Creature
	 * @see lineage2.gameserver.listener.actor.OnDeathListener#onDeath(Creature, Creature)
	 */
	@Override
	public void onDeath(Creature cha, Creature killer)
	{
		if (_active && SimpleCheckDrop(cha, killer))
		{
			long count = Util.rollDrop(1, 1, Config.EVENT_GLITTMEDAL_NORMAL_CHANCE * killer.getPlayer().getRateItems() * ((MonsterInstance) cha).getTemplate().rateHp * 10000L, true);
			if (count > 0)
			{
				addItem(killer.getPlayer(), EVENT_MEDAL, count);
			}
			if ((killer.getPlayer().getInventory().getCountOf(Badge_of_Wolf) == 0) && Rnd.chance(Config.EVENT_GLITTMEDAL_GLIT_CHANCE * killer.getPlayer().getRateItems() * ((MonsterInstance) cha).getTemplate().rateHp))
			{
				addItem(killer.getPlayer(), EVENT_GLITTMEDAL, 1);
			}
		}
	}
	
	/**
	 * Method glitchang.
	 */
	public void glitchang()
	{
		Player player = getSelf();
		if (!player.isQuestContinuationPossible(true))
		{
			return;
		}
		if (getItemCount(player, EVENT_MEDAL) >= 1000)
		{
			removeItem(player, EVENT_MEDAL, 1000);
			addItem(player, EVENT_GLITTMEDAL, 10);
			return;
		}
		player.sendPacket(Msg.YOU_DO_NOT_HAVE_ENOUGH_REQUIRED_ITEMS);
	}
	
	/**
	 * Method medal.
	 */
	public void medal()
	{
		Player player = getSelf();
		if (!player.isQuestContinuationPossible(true))
		{
			return;
		}
		if (getItemCount(player, Badge_of_Wolf) >= 1)
		{
			show("scripts/events/glitmedal/event_col_agent1_q0996_05.htm", player);
			return;
		}
		else if (getItemCount(player, Badge_of_Fox) >= 1)
		{
			show("scripts/events/glitmedal/event_col_agent1_q0996_04.htm", player);
			return;
		}
		else if (getItemCount(player, Badge_of_Hyena) >= 1)
		{
			show("scripts/events/glitmedal/event_col_agent1_q0996_03.htm", player);
			return;
		}
		else if (getItemCount(player, Badge_of_Rabbit) >= 1)
		{
			show("scripts/events/glitmedal/event_col_agent1_q0996_02.htm", player);
			return;
		}
		show("scripts/events/glitmedal/event_col_agent1_q0996_01.htm", player);
	}
	
	/**
	 * Method medalb.
	 */
	public void medalb()
	{
		Player player = getSelf();
		if (!player.isQuestContinuationPossible(true))
		{
			return;
		}
		if (getItemCount(player, Badge_of_Wolf) >= 1)
		{
			show("scripts/events/glitmedal/event_col_agent2_q0996_05.htm", player);
			return;
		}
		else if (getItemCount(player, Badge_of_Fox) >= 1)
		{
			show("scripts/events/glitmedal/event_col_agent2_q0996_04.htm", player);
			return;
		}
		else if (getItemCount(player, Badge_of_Hyena) >= 1)
		{
			show("scripts/events/glitmedal/event_col_agent2_q0996_03.htm", player);
			return;
		}
		else if (getItemCount(player, Badge_of_Rabbit) >= 1)
		{
			show("scripts/events/glitmedal/event_col_agent2_q0996_02.htm", player);
			return;
		}
		show("scripts/events/glitmedal/event_col_agent2_q0996_01.htm", player);
		return;
	}
	
	/**
	 * Method game.
	 */
	public void game()
	{
		Player player = getSelf();
		if (!player.isQuestContinuationPossible(true))
		{
			return;
		}
		if (getItemCount(player, Badge_of_Fox) >= 1)
		{
			if (getItemCount(player, EVENT_GLITTMEDAL) >= 40)
			{
				show("scripts/events/glitmedal/event_col_agent2_q0996_11.htm", player);
				return;
			}
			show("scripts/events/glitmedal/event_col_agent2_q0996_12.htm", player);
			return;
		}
		else if (getItemCount(player, Badge_of_Hyena) >= 1)
		{
			if (getItemCount(player, EVENT_GLITTMEDAL) >= 20)
			{
				show("scripts/events/glitmedal/event_col_agent2_q0996_11.htm", player);
				return;
			}
			show("scripts/events/glitmedal/event_col_agent2_q0996_12.htm", player);
			return;
		}
		else if (getItemCount(player, Badge_of_Rabbit) >= 1)
		{
			if (getItemCount(player, EVENT_GLITTMEDAL) >= 10)
			{
				show("scripts/events/glitmedal/event_col_agent2_q0996_11.htm", player);
				return;
			}
			show("scripts/events/glitmedal/event_col_agent2_q0996_12.htm", player);
			return;
		}
		else if (getItemCount(player, EVENT_GLITTMEDAL) >= 5)
		{
			show("scripts/events/glitmedal/event_col_agent2_q0996_11.htm", player);
			return;
		}
		show("scripts/events/glitmedal/event_col_agent2_q0996_12.htm", player);
	}
	
	/**
	 * Method gamea.
	 */
	public void gamea()
	{
		Player player = getSelf();
		if (!player.isQuestContinuationPossible(true))
		{
			return;
		}
		isTalker = Rnd.get(2);
		if (getItemCount(player, Badge_of_Fox) >= 1)
		{
			if (getItemCount(player, EVENT_GLITTMEDAL) >= 40)
			{
				if (isTalker == 1)
				{
					removeItem(player, Badge_of_Fox, 1);
					removeItem(player, EVENT_GLITTMEDAL, getItemCount(player, EVENT_GLITTMEDAL));
					addItem(player, Badge_of_Wolf, 1);
					show("scripts/events/glitmedal/event_col_agent2_q0996_24.htm", player);
					return;
				}
				else if (isTalker == 0)
				{
					removeItem(player, EVENT_GLITTMEDAL, 40);
					show("scripts/events/glitmedal/event_col_agent2_q0996_25.htm", player);
					return;
				}
			}
			show("scripts/events/glitmedal/event_col_agent2_q0996_26.htm", player);
			return;
		}
		else if (getItemCount(player, Badge_of_Hyena) >= 1)
		{
			if (getItemCount(player, EVENT_GLITTMEDAL) >= 20)
			{
				if (isTalker == 1)
				{
					removeItem(player, Badge_of_Hyena, 1);
					removeItem(player, EVENT_GLITTMEDAL, 20);
					addItem(player, Badge_of_Fox, 1);
					show("scripts/events/glitmedal/event_col_agent2_q0996_23.htm", player);
					return;
				}
				else if (isTalker == 0)
				{
					removeItem(player, EVENT_GLITTMEDAL, 20);
					show("scripts/events/glitmedal/event_col_agent2_q0996_25.htm", player);
					return;
				}
			}
			show("scripts/events/glitmedal/event_col_agent2_q0996_26.htm", player);
			return;
		}
		else if (getItemCount(player, Badge_of_Rabbit) >= 1)
		{
			if (getItemCount(player, EVENT_GLITTMEDAL) >= 10)
			{
				if (isTalker == 1)
				{
					removeItem(player, Badge_of_Rabbit, 1);
					removeItem(player, EVENT_GLITTMEDAL, 10);
					addItem(player, Badge_of_Hyena, 1);
					show("scripts/events/glitmedal/event_col_agent2_q0996_22.htm", player);
					return;
				}
				else if (isTalker == 0)
				{
					removeItem(player, EVENT_GLITTMEDAL, 10);
					show("scripts/events/glitmedal/event_col_agent2_q0996_25.htm", player);
					return;
				}
			}
			show("scripts/events/glitmedal/event_col_agent2_q0996_26.htm", player);
			return;
		}
		if (getItemCount(player, EVENT_GLITTMEDAL) >= 5)
		{
			if (isTalker == 1)
			{
				removeItem(player, EVENT_GLITTMEDAL, 5);
				addItem(player, Badge_of_Rabbit, 1);
				show("scripts/events/glitmedal/event_col_agent2_q0996_21.htm", player);
				return;
			}
			else if (isTalker == 0)
			{
				removeItem(player, EVENT_GLITTMEDAL, 5);
				show("scripts/events/glitmedal/event_col_agent2_q0996_25.htm", player);
				return;
			}
		}
		show("scripts/events/glitmedal/event_col_agent2_q0996_26.htm", player);
	}
	
	/**
	 * Method gameb.
	 */
	public void gameb()
	{
		Player player = getSelf();
		if (!player.isQuestContinuationPossible(true))
		{
			return;
		}
		isTalker = Rnd.get(2);
		if (getItemCount(player, Badge_of_Fox) >= 1)
		{
			if (getItemCount(player, EVENT_GLITTMEDAL) >= 40)
			{
				if (isTalker == 1)
				{
					removeItem(player, Badge_of_Fox, 1);
					removeItem(player, EVENT_GLITTMEDAL, 40);
					addItem(player, Badge_of_Wolf, 1);
					show("scripts/events/glitmedal/event_col_agent2_q0996_34.htm", player);
					return;
				}
				else if (isTalker == 0)
				{
					removeItem(player, EVENT_GLITTMEDAL, 40);
					show("scripts/events/glitmedal/event_col_agent2_q0996_35.htm", player);
					return;
				}
			}
			show("scripts/events/glitmedal/event_col_agent2_q0996_26.htm", player);
			return;
		}
		else if (getItemCount(player, Badge_of_Hyena) >= 1)
		{
			if (getItemCount(player, EVENT_GLITTMEDAL) >= 20)
			{
				if (isTalker == 1)
				{
					removeItem(player, Badge_of_Hyena, 1);
					removeItem(player, EVENT_GLITTMEDAL, 20);
					addItem(player, Badge_of_Fox, 1);
					show("scripts/events/glitmedal/event_col_agent2_q0996_33.htm", player);
					return;
				}
				else if (isTalker == 0)
				{
					removeItem(player, EVENT_GLITTMEDAL, 20);
					show("scripts/events/glitmedal/event_col_agent2_q0996_35.htm", player);
					return;
				}
			}
			show("scripts/events/glitmedal/event_col_agent2_q0996_26.htm", player);
			return;
		}
		else if (getItemCount(player, Badge_of_Rabbit) >= 1)
		{
			if (getItemCount(player, EVENT_GLITTMEDAL) >= 10)
			{
				if (isTalker == 1)
				{
					removeItem(player, Badge_of_Rabbit, 1);
					removeItem(player, EVENT_GLITTMEDAL, 10);
					addItem(player, Badge_of_Hyena, 1);
					show("scripts/events/glitmedal/event_col_agent2_q0996_32.htm", player);
					return;
				}
				else if (isTalker == 0)
				{
					removeItem(player, EVENT_GLITTMEDAL, 10);
					show("scripts/events/glitmedal/event_col_agent2_q0996_35.htm", player);
					return;
				}
			}
			show("scripts/events/glitmedal/event_col_agent2_q0996_26.htm", player);
			return;
		}
		if (getItemCount(player, EVENT_GLITTMEDAL) >= 5)
		{
			if (isTalker == 1)
			{
				removeItem(player, EVENT_GLITTMEDAL, 5);
				addItem(player, Badge_of_Rabbit, 1);
				show("scripts/events/glitmedal/event_col_agent2_q0996_31.htm", player);
				return;
			}
			else if (isTalker == 0)
			{
				removeItem(player, EVENT_GLITTMEDAL, 5);
				show("scripts/events/glitmedal/event_col_agent2_q0996_35.htm", player);
				return;
			}
		}
		show("scripts/events/glitmedal/event_col_agent2_q0996_26.htm", player);
		return;
	}
}
