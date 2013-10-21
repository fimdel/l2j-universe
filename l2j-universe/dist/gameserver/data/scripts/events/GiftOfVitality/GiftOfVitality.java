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
package events.GiftOfVitality;

import java.util.ArrayList;
import java.util.List;

import lineage2.gameserver.Announcements;
import lineage2.gameserver.Config;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.SimpleSpawner;
import lineage2.gameserver.model.base.Race;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.MagicSkillUse;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.tables.SkillTable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class GiftOfVitality extends Functions implements ScriptFile
{
	/**
	 * Field EVENT_NAME. (value is ""GiftOfVitality"")
	 */
	private static final String EVENT_NAME = "GiftOfVitality";
	/**
	 * Field REUSE_HOURS. (value is 24)
	 */
	private static final int REUSE_HOURS = 24;
	/**
	 * Field EVENT_MANAGER_ID. (value is 109)
	 */
	private static final int EVENT_MANAGER_ID = 109;
	/**
	 * Field _spawns.
	 */
	private static final List<SimpleSpawner> _spawns = new ArrayList<>();
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(GiftOfVitality.class);
	/**
	 * Field _mageBuff.
	 */
	private final static int[][] _mageBuff = new int[][]
	{
		{
			5627,
			1
		},
		{
			5628,
			1
		},
		{
			5637,
			1
		},
		{
			5633,
			1
		},
		{
			5634,
			1
		},
		{
			5635,
			1
		},
		{
			5636,
			1
		},
	};
	/**
	 * Field _warrBuff.
	 */
	private final static int[][] _warrBuff = new int[][]
	{
		{
			5627,
			1
		},
		{
			5628,
			1
		},
		{
			5637,
			1
		},
		{
			5629,
			1
		},
		{
			5630,
			1
		},
		{
			5631,
			1
		},
		{
			5632,
			1
		},
	};
	
	/**
	 * @author Mobius
	 */
	public enum BuffType
	{
		/**
		 * Field PLAYER.
		 */
		PLAYER,
		/**
		 * Field SUMMON.
		 */
		SUMMON,
		/**
		 * Field VITALITY.
		 */
		VITALITY,
	}
	
	/**
	 * Method spawnEventManagers.
	 */
	private void spawnEventManagers()
	{
		final int[][] EVENT_MANAGERS =
		{
			{
				-119494,
				44882,
				360,
				24576
			},
			{
				-84023,
				243051,
				-3728,
				4096
			},
			{
				45538,
				48357,
				-3056,
				18000
			},
			{
				9929,
				16324,
				-4568,
				62999
			},
			{
				115096,
				-178370,
				-880,
				0
			},
			{
				-45372,
				-114104,
				-240,
				16384
			},
			{
				-83156,
				150994,
				-3120,
				0
			},
			{
				-13727,
				122117,
				-2984,
				16384
			},
			{
				16111,
				142850,
				-2696,
				16000
			},
			{
				111004,
				218928,
				-3536,
				16384
			},
			{
				82145,
				148609,
				-3464,
				0
			},
			{
				81083,
				56118,
				-1552,
				32768
			},
			{
				117356,
				76708,
				-2688,
				49151
			},
			{
				147200,
				25614,
				-2008,
				16384
			},
			{
				43966,
				-47709,
				-792,
				49999
			},
			{
				147421,
				-55435,
				-2728,
				49151
			},
			{
				85584,
				-142490,
				-1336,
				0
			},
		};
		SpawnNPCs(EVENT_MANAGER_ID, EVENT_MANAGERS, _spawns);
	}
	
	/**
	 * Method unSpawnEventManagers.
	 */
	private void unSpawnEventManagers()
	{
		deSpawnNPCs(_spawns);
	}
	
	/**
	 * Method isActive.
	 * @return boolean
	 */
	private static boolean isActive()
	{
		return IsActive(EVENT_NAME);
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
		if (SetActive(EVENT_NAME, true))
		{
			spawnEventManagers();
			System.out.println("Event: 'Gift Of Vitality' started.");
			Announcements.getInstance().announceByCustomMessage("scripts.events.GiftOfVitality.AnnounceEventStarted", null);
		}
		else
		{
			player.sendMessage("Event 'Gift Of Vitality' already started.");
		}
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
		if (SetActive(EVENT_NAME, false))
		{
			unSpawnEventManagers();
			System.out.println("Event: 'Gift Of Vitality' stopped.");
			Announcements.getInstance().announceByCustomMessage("scripts.events.GiftOfVitality.AnnounceEventStoped", null);
		}
		else
		{
			player.sendMessage("Event: 'Gift Of Vitality' not started.");
		}
		show("admin/events.htm", player);
	}
	
	/**
	 * Method onLoad.
	 * @see lineage2.gameserver.scripts.ScriptFile#onLoad()
	 */
	@Override
	public void onLoad()
	{
		if (isActive())
		{
			spawnEventManagers();
			_log.info("Loaded Event: Gift Of Vitality [state: activated]");
		}
		else
		{
			_log.info("Loaded Event: Gift Of Vitality [state: deactivated]");
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
	 * Method buffMe.
	 * @param type BuffType
	 */
	private void buffMe(BuffType type)
	{
		if ((getSelf() == null) || (getNpc() == null) || (getSelf().getPlayer() == null))
		{
			return;
		}
		String htmltext = null;
		final Player player = getSelf().getPlayer();
		final NpcInstance npc = getNpc();
		final String var = player.getVar("govEventTime");
		switch (type)
		{
			case VITALITY:
				if (((var != null) && (Long.parseLong(var) > System.currentTimeMillis())) || (player.getBaseClassId() != player.getActiveClassId()))
				{
					htmltext = "jack-notime.htm";
				}
				else
				{
					npc.broadcastPacket(new MagicSkillUse(npc, player, 23179, 1, 0, 0));
					player.altOnMagicUseTimer(player, SkillTable.getInstance().getInfo(23179, 1));
					player.setVar("govEventTime", String.valueOf(System.currentTimeMillis() + (REUSE_HOURS * 60 * 60 * 1000L)), -1);
					player.setVitality(Config.MAX_VITALITY);
					htmltext = "jack-okvitality.htm";
				}
				break;
			case PLAYER:
				if (player.getLevel() < 76)
				{
					htmltext = "jack-nolevel.htm";
				}
				else
				{
					if (!player.isMageClass() || (player.getTemplate().getRace() == Race.orc))
					{
						for (int[] buff : _warrBuff)
						{
							npc.broadcastPacket(new MagicSkillUse(npc, player, buff[0], buff[1], 0, 0));
							player.altOnMagicUseTimer(player, SkillTable.getInstance().getInfo(buff[0], buff[1]));
						}
					}
					else
					{
						for (int[] buff : _mageBuff)
						{
							npc.broadcastPacket(new MagicSkillUse(npc, player, buff[0], buff[1], 0, 0));
							player.altOnMagicUseTimer(player, SkillTable.getInstance().getInfo(buff[0], buff[1]));
						}
					}
					htmltext = "jack-okbuff.htm";
				}
				break;
			default:
				break;
		}
		show("scripts/events/GiftOfVitality/" + htmltext, getSelf().getPlayer());
	}
	
	/**
	 * Method buffVitality.
	 */
	public void buffVitality()
	{
		buffMe(BuffType.VITALITY);
	}
	
	/**
	 * Method buffSummon.
	 */
	public void buffSummon()
	{
		buffMe(BuffType.SUMMON);
	}
	
	/**
	 * Method buffPlayer.
	 */
	public void buffPlayer()
	{
		buffMe(BuffType.PLAYER);
	}
}
