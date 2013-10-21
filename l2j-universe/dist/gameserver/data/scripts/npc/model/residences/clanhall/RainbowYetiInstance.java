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
package npc.model.residences.clanhall;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import lineage2.commons.threading.RunnableImpl;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.model.GameObject;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.World;
import lineage2.gameserver.model.entity.events.impl.ClanHallMiniGameEvent;
import lineage2.gameserver.model.entity.events.impl.SiegeEvent;
import lineage2.gameserver.model.entity.events.objects.CMGSiegeClanObject;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage;
import lineage2.gameserver.network.serverpackets.NpcHtmlMessage;
import lineage2.gameserver.network.serverpackets.SystemMessage2;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.ItemFunctions;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RainbowYetiInstance extends NpcInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field ItemA. (value is 8035)
	 */
	private static final int ItemA = 8035;
	/**
	 * Field ItemB. (value is 8036)
	 */
	private static final int ItemB = 8036;
	/**
	 * Field ItemC. (value is 8037)
	 */
	private static final int ItemC = 8037;
	/**
	 * Field ItemD. (value is 8038)
	 */
	private static final int ItemD = 8038;
	/**
	 * Field ItemE. (value is 8039)
	 */
	private static final int ItemE = 8039;
	/**
	 * Field ItemF. (value is 8040)
	 */
	private static final int ItemF = 8040;
	/**
	 * Field ItemG. (value is 8041)
	 */
	private static final int ItemG = 8041;
	/**
	 * Field ItemH. (value is 8042)
	 */
	private static final int ItemH = 8042;
	/**
	 * Field ItemI. (value is 8043)
	 */
	private static final int ItemI = 8043;
	/**
	 * Field ItemK. (value is 8045)
	 */
	private static final int ItemK = 8045;
	/**
	 * Field ItemL. (value is 8046)
	 */
	private static final int ItemL = 8046;
	/**
	 * Field ItemN. (value is 8047)
	 */
	private static final int ItemN = 8047;
	/**
	 * Field ItemO. (value is 8048)
	 */
	private static final int ItemO = 8048;
	/**
	 * Field ItemP. (value is 8049)
	 */
	private static final int ItemP = 8049;
	/**
	 * Field ItemR. (value is 8050)
	 */
	private static final int ItemR = 8050;
	/**
	 * Field ItemS. (value is 8051)
	 */
	private static final int ItemS = 8051;
	/**
	 * Field ItemT. (value is 8052)
	 */
	private static final int ItemT = 8052;
	/**
	 * Field ItemU. (value is 8053)
	 */
	private static final int ItemU = 8053;
	/**
	 * Field ItemW. (value is 8054)
	 */
	private static final int ItemW = 8054;
	/**
	 * Field ItemY. (value is 8055)
	 */
	private static final int ItemY = 8055;
	
	/**
	 * @author Mobius
	 */
	private static class Word
	{
		/**
		 * Field _name.
		 */
		private final String _name;
		/**
		 * Field _items.
		 */
		private final int[][] _items;
		
		/**
		 * Constructor for Word.
		 * @param name String
		 * @param items int[][]
		 */
		public Word(String name, int[]... items)
		{
			_name = name;
			_items = items;
		}
		
		/**
		 * Method getName.
		 * @return String
		 */
		public String getName()
		{
			return _name;
		}
		
		/**
		 * Method getItems.
		 * @return int[][]
		 */
		public int[][] getItems()
		{
			return _items;
		}
	}
	
	/**
	 * @author Mobius
	 */
	private class GenerateTask extends RunnableImpl
	{
		/**
		 * Constructor for GenerateTask.
		 */
		public GenerateTask()
		{
			// TODO Auto-generated constructor stub
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			_generated = Rnd.get(WORLD_LIST.length);
			Word word = WORLD_LIST[_generated];
			List<Player> around = World.getAroundPlayers(RainbowYetiInstance.this, 750, 100);
			ExShowScreenMessage msg = new ExShowScreenMessage(NpcString.NONE, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, word.getName());
			for (Player player : around)
			{
				player.sendPacket(msg);
			}
		}
	}
	
	/**
	 * Field WORLD_LIST.
	 */
	static final Word[] WORLD_LIST = new Word[8];
	static
	{
		WORLD_LIST[0] = new Word("BABYDUCK", new int[]
		{
			ItemB,
			2
		}, new int[]
		{
			ItemA,
			1
		}, new int[]
		{
			ItemY,
			1
		}, new int[]
		{
			ItemD,
			1
		}, new int[]
		{
			ItemU,
			1
		}, new int[]
		{
			ItemC,
			1
		}, new int[]
		{
			ItemK,
			1
		});
		WORLD_LIST[1] = new Word("ALBATROS", new int[]
		{
			ItemA,
			2
		}, new int[]
		{
			ItemL,
			1
		}, new int[]
		{
			ItemB,
			1
		}, new int[]
		{
			ItemT,
			1
		}, new int[]
		{
			ItemR,
			1
		}, new int[]
		{
			ItemO,
			1
		}, new int[]
		{
			ItemS,
			1
		});
		WORLD_LIST[2] = new Word("PELICAN", new int[]
		{
			ItemP,
			1
		}, new int[]
		{
			ItemE,
			1
		}, new int[]
		{
			ItemL,
			1
		}, new int[]
		{
			ItemI,
			1
		}, new int[]
		{
			ItemC,
			1
		}, new int[]
		{
			ItemA,
			1
		}, new int[]
		{
			ItemN,
			1
		});
		WORLD_LIST[3] = new Word("KINGFISHER", new int[]
		{
			ItemK,
			1
		}, new int[]
		{
			ItemI,
			1
		}, new int[]
		{
			ItemN,
			1
		}, new int[]
		{
			ItemG,
			1
		}, new int[]
		{
			ItemF,
			1
		}, new int[]
		{
			ItemI,
			1
		}, new int[]
		{
			ItemS,
			1
		}, new int[]
		{
			ItemH,
			1
		}, new int[]
		{
			ItemE,
			1
		}, new int[]
		{
			ItemR,
			1
		});
		WORLD_LIST[4] = new Word("CYGNUS", new int[]
		{
			ItemC,
			1
		}, new int[]
		{
			ItemY,
			1
		}, new int[]
		{
			ItemG,
			1
		}, new int[]
		{
			ItemN,
			1
		}, new int[]
		{
			ItemU,
			1
		}, new int[]
		{
			ItemS,
			1
		});
		WORLD_LIST[5] = new Word("TRITON", new int[]
		{
			ItemT,
			2
		}, new int[]
		{
			ItemR,
			1
		}, new int[]
		{
			ItemI,
			1
		}, new int[]
		{
			ItemN,
			1
		});
		WORLD_LIST[6] = new Word("RAINBOW", new int[]
		{
			ItemR,
			1
		}, new int[]
		{
			ItemA,
			1
		}, new int[]
		{
			ItemI,
			1
		}, new int[]
		{
			ItemN,
			1
		}, new int[]
		{
			ItemB,
			1
		}, new int[]
		{
			ItemO,
			1
		}, new int[]
		{
			ItemW,
			1
		});
		WORLD_LIST[7] = new Word("SPRING", new int[]
		{
			ItemS,
			1
		}, new int[]
		{
			ItemP,
			1
		}, new int[]
		{
			ItemR,
			1
		}, new int[]
		{
			ItemI,
			1
		}, new int[]
		{
			ItemN,
			1
		}, new int[]
		{
			ItemG,
			1
		});
	}
	/**
	 * Field _mobs.
	 */
	private final List<GameObject> _mobs = new ArrayList<>();
	/**
	 * Field _generated.
	 */
	int _generated = -1;
	/**
	 * Field _task.
	 */
	private Future<?> _task = null;
	
	/**
	 * Constructor for RainbowYetiInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public RainbowYetiInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
		_hasRandomWalk = false;
	}
	
	/**
	 * Method onSpawn.
	 */
	@Override
	public void onSpawn()
	{
		super.onSpawn();
		ClanHallMiniGameEvent event = getEvent(ClanHallMiniGameEvent.class);
		if (event == null)
		{
			return;
		}
		List<Player> around = World.getAroundPlayers(this, 750, 100);
		for (Player player : around)
		{
			CMGSiegeClanObject siegeClanObject = event.getSiegeClan(SiegeEvent.ATTACKERS, player.getClan());
			if ((siegeClanObject == null) || !siegeClanObject.getPlayers().contains(player.getObjectId()))
			{
				player.teleToLocation(event.getResidence().getOtherRestartPoint());
			}
		}
		_task = ThreadPoolManager.getInstance().scheduleAtFixedRate(new GenerateTask(), 10000L, 300000L);
	}
	
	/**
	 * Method onDelete.
	 */
	@Override
	public void onDelete()
	{
		super.onDelete();
		if (_task != null)
		{
			_task.cancel(false);
			_task = null;
		}
		for (GameObject object : _mobs)
		{
			object.deleteMe();
		}
		_mobs.clear();
	}
	
	/**
	 * Method teleportFromArena.
	 */
	public void teleportFromArena()
	{
		ClanHallMiniGameEvent event = getEvent(ClanHallMiniGameEvent.class);
		if (event == null)
		{
			return;
		}
		List<Player> around = World.getAroundPlayers(this, 750, 100);
		for (Player player : around)
		{
			player.teleToLocation(event.getResidence().getOtherRestartPoint());
		}
	}
	
	/**
	 * Method onBypassFeedback.
	 * @param player Player
	 * @param command String
	 */
	@Override
	public void onBypassFeedback(Player player, String command)
	{
		if (!canBypassCheck(player, this))
		{
			return;
		}
		if (command.equalsIgnoreCase("get"))
		{
			NpcHtmlMessage msg = new NpcHtmlMessage(player, this);
			boolean has = true;
			if (_generated == -1)
			{
				has = false;
			}
			else
			{
				Word word = WORLD_LIST[_generated];
				for (int[] itemInfo : word.getItems())
				{
					if (player.getInventory().getCountOf(itemInfo[0]) < itemInfo[1])
					{
						has = false;
					}
				}
				if (has)
				{
					for (int[] itemInfo : word.getItems())
					{
						if (!player.consumeItem(itemInfo[0], itemInfo[1]))
						{
							return;
						}
					}
					int rnd = Rnd.get(100);
					if ((_generated >= 0) && (_generated <= 5))
					{
						if (rnd < 70)
						{
							addItem(player, 8030);
						}
						else if (rnd < 80)
						{
							addItem(player, 8031);
						}
						else if (rnd < 90)
						{
							addItem(player, 8032);
						}
						else
						{
							addItem(player, 8033);
						}
					}
					else
					{
						if (rnd < 10)
						{
							addItem(player, 8030);
						}
						else if (rnd < 40)
						{
							addItem(player, 8031);
						}
						else if (rnd < 70)
						{
							addItem(player, 8032);
						}
						else
						{
							addItem(player, 8033);
						}
					}
				}
			}
			if (!has)
			{
				msg.setFile("residence2/clanhall/watering_manager002.htm");
			}
			else
			{
				msg.setFile("residence2/clanhall/watering_manager004.htm");
			}
			player.sendPacket(msg);
		}
		else if (command.equalsIgnoreCase("see"))
		{
			NpcHtmlMessage msg = new NpcHtmlMessage(player, this);
			msg.setFile("residence2/clanhall/watering_manager005.htm");
			if (_generated == -1)
			{
				msg.replaceNpcString("%word%", NpcString.UNDECIDED);
			}
			else
			{
				msg.replace("%word%", WORLD_LIST[_generated].getName());
			}
			player.sendPacket(msg);
		}
		else
		{
			super.onBypassFeedback(player, command);
		}
	}
	
	/**
	 * Method addItem.
	 * @param player Player
	 * @param itemId int
	 */
	private void addItem(Player player, int itemId)
	{
		ClanHallMiniGameEvent event = getEvent(ClanHallMiniGameEvent.class);
		if (event == null)
		{
			return;
		}
		ItemInstance item = ItemFunctions.createItem(itemId);
		item.addEvent(event);
		player.getInventory().addItem(item);
		player.sendPacket(SystemMessage2.obtainItems(item));
	}
	
	/**
	 * Method showChatWindow.
	 * @param player Player
	 * @param val int
	 * @param arg Object[]
	 */
	@Override
	public void showChatWindow(Player player, int val, Object... arg)
	{
		showChatWindow(player, "residence2/clanhall/watering_manager001.htm");
	}
	
	/**
	 * Method addMob.
	 * @param object GameObject
	 */
	public void addMob(GameObject object)
	{
		_mobs.add(object);
	}
}
