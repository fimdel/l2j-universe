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
package npc.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.model.GameObjectsStorage;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.network.serverpackets.NpcHtmlMessage;
import lineage2.gameserver.network.serverpackets.Say2;
import lineage2.gameserver.network.serverpackets.components.ChatType;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.ItemFunctions;
import lineage2.gameserver.utils.PositionUtils;
import bosses.FourSepulchersManager;
import bosses.FourSepulchersSpawn;
import bosses.FourSepulchersSpawn.GateKeeper;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class SepulcherNpcInstance extends NpcInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field _hallGateKeepers.
	 */
	protected static Map<Integer, Integer> _hallGateKeepers = new HashMap<>();
	/**
	 * Field _spawnMonsterTask. Field _closeTask.
	 */
	protected Future<?> _closeTask = null, _spawnMonsterTask = null;
	/**
	 * Field HTML_FILE_PATH. (value is ""SepulcherNpc/"")
	 */
	private final static String HTML_FILE_PATH = "SepulcherNpc/";
	/**
	 * Field HALLS_KEY. (value is 7260)
	 */
	private final static int HALLS_KEY = 7260;
	
	/**
	 * Constructor for SepulcherNpcInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public SepulcherNpcInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	/**
	 * Method onDelete.
	 */
	@Override
	protected void onDelete()
	{
		if (_closeTask != null)
		{
			_closeTask.cancel(false);
			_closeTask = null;
		}
		if (_spawnMonsterTask != null)
		{
			_spawnMonsterTask.cancel(false);
			_spawnMonsterTask = null;
		}
		super.onDelete();
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
		if (isDead())
		{
			player.sendActionFailed();
			return;
		}
		switch (getNpcId())
		{
			case 31468:
			case 31469:
			case 31470:
			case 31471:
			case 31472:
			case 31473:
			case 31474:
			case 31475:
			case 31476:
			case 31477:
			case 31478:
			case 31479:
			case 31480:
			case 31481:
			case 31482:
			case 31483:
			case 31484:
			case 31485:
			case 31486:
			case 31487:
				doDie(player);
				if (_spawnMonsterTask != null)
				{
					_spawnMonsterTask.cancel(false);
				}
				_spawnMonsterTask = ThreadPoolManager.getInstance().schedule(new SpawnMonster(getNpcId()), 3500);
				return;
			case 31455:
			case 31456:
			case 31457:
			case 31458:
			case 31459:
			case 31460:
			case 31461:
			case 31462:
			case 31463:
			case 31464:
			case 31465:
			case 31466:
			case 31467:
				if (player.isInParty() && !hasPartyAKey(player) && (player.getParty().getPartyLeader() == player))
				{
					Functions.addItem(player.getParty().getPartyLeader(), HALLS_KEY, 1);
					doDie(player);
				}
				return;
		}
		super.showChatWindow(player, val);
	}
	
	/**
	 * Method getHtmlPath.
	 * @param npcId int
	 * @param val int
	 * @param player Player
	 * @return String
	 */
	@Override
	public String getHtmlPath(int npcId, int val, Player player)
	{
		String pom;
		if (val == 0)
		{
			pom = String.valueOf(npcId);
		}
		else
		{
			pom = npcId + "-" + val;
		}
		return HTML_FILE_PATH + pom + ".htm";
	}
	
	/**
	 * Method onBypassFeedback.
	 * @param player Player
	 * @param command String
	 */
	@Override
	public void onBypassFeedback(Player player, String command)
	{
		if (command.startsWith("open_gate"))
		{
			ItemInstance hallsKey = player.getInventory().getItemByItemId(HALLS_KEY);
			if (hallsKey == null)
			{
				showHtmlFile(player, "Gatekeeper-no.htm");
			}
			else if (FourSepulchersManager.isAttackTime())
			{
				switch (getNpcId())
				{
					case 31929:
					case 31934:
					case 31939:
					case 31944:
						if (!FourSepulchersSpawn.isShadowAlive(getNpcId()))
						{
							FourSepulchersSpawn.spawnShadow(getNpcId());
						}
				}
				openNextDoor(getNpcId());
				if (player.getParty() != null)
				{
					for (Player mem : player.getParty().getPartyMembers())
					{
						hallsKey = mem.getInventory().getItemByItemId(HALLS_KEY);
						if (hallsKey != null)
						{
							Functions.removeItem(mem, HALLS_KEY, hallsKey.getCount());
						}
					}
				}
				else
				{
					Functions.removeItem(player, HALLS_KEY, hallsKey.getCount());
				}
			}
		}
		else
		{
			super.onBypassFeedback(player, command);
		}
	}
	
	/**
	 * Method openNextDoor.
	 * @param npcId int
	 */
	public void openNextDoor(int npcId)
	{
		GateKeeper gk = FourSepulchersManager.getHallGateKeeper(npcId);
		gk.door.openMe();
		if (_closeTask != null)
		{
			_closeTask.cancel(false);
		}
		_closeTask = ThreadPoolManager.getInstance().schedule(new CloseNextDoor(gk), 10000);
	}
	
	/**
	 * @author Mobius
	 */
	private class CloseNextDoor extends RunnableImpl
	{
		/**
		 * Field _gk.
		 */
		private final GateKeeper _gk;
		/**
		 * Field state.
		 */
		private int state = 0;
		
		/**
		 * Constructor for CloseNextDoor.
		 * @param gk GateKeeper
		 */
		public CloseNextDoor(GateKeeper gk)
		{
			_gk = gk;
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			if (state == 0)
			{
				try
				{
					_gk.door.closeMe();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				state++;
				_closeTask = ThreadPoolManager.getInstance().schedule(this, 10000);
			}
			else if (state == 1)
			{
				FourSepulchersSpawn.spawnMysteriousBox(_gk.template.npcId);
				_closeTask = null;
			}
		}
	}
	
	/**
	 * @author Mobius
	 */
	private class SpawnMonster extends RunnableImpl
	{
		/**
		 * Field _NpcId.
		 */
		private final int _NpcId;
		
		/**
		 * Constructor for SpawnMonster.
		 * @param npcId int
		 */
		public SpawnMonster(int npcId)
		{
			_NpcId = npcId;
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			FourSepulchersSpawn.spawnMonster(_NpcId);
		}
	}
	
	/**
	 * Method sayInShout.
	 * @param msg String
	 */
	public void sayInShout(String msg)
	{
		if ((msg == null) || msg.isEmpty())
		{
			return;
		}
		List<Player> knownPlayers = GameObjectsStorage.getAllPlayers();
		if ((knownPlayers == null) || knownPlayers.isEmpty())
		{
			return;
		}
		Say2 sm = new Say2(0, ChatType.SHOUT, getName(), msg);
		for (Player player : knownPlayers)
		{
			if (player == null)
			{
				continue;
			}
			if (PositionUtils.checkIfInRange(15000, player, this, true))
			{
				player.sendPacket(sm);
			}
		}
	}
	
	/**
	 * Method showHtmlFile.
	 * @param player Player
	 * @param file String
	 */
	public void showHtmlFile(Player player, String file)
	{
		NpcHtmlMessage html = new NpcHtmlMessage(player, this);
		html.setFile("SepulcherNpc/" + file);
		html.replace("%npcname%", getName());
		player.sendPacket(html);
	}
	
	/**
	 * Method hasPartyAKey.
	 * @param player Player
	 * @return boolean
	 */
	private boolean hasPartyAKey(Player player)
	{
		for (Player m : player.getParty().getPartyMembers())
		{
			if (ItemFunctions.getItemCount(m, HALLS_KEY) > 0)
			{
				return true;
			}
		}
		return false;
	}
}
