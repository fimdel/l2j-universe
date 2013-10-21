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

import instances.KamalokaNightmare;
import lineage2.gameserver.Config;
import lineage2.gameserver.instancemanager.MapRegionManager;
import lineage2.gameserver.instancemanager.ReflectionManager;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.templates.InstantZone;
import lineage2.gameserver.templates.StatsSet;
import lineage2.gameserver.templates.mapregion.DomainArea;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.ReflectionUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class PathfinderInstance extends NpcInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field INSTANCE_75LVL_ID. (value is 56)
	 */
	private static final int INSTANCE_75LVL_ID = 56;
	/**
	 * Field _rank.
	 */
	private int _rank = -1;
	/**
	 * Field _rewarded.
	 */
	private boolean _rewarded = false;
	
	/**
	 * Constructor for PathfinderInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public PathfinderInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
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
			pom = "" + npcId;
		}
		else
		{
			pom = npcId + "-" + val;
		}
		return "instance/soloKamaloka/" + pom + ".htm";
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
		if (command.startsWith("ExitSoloKama"))
		{
			Reflection r = getReflection();
			if (r.getReturnLoc() != null)
			{
				player.teleToLocation(r.getReturnLoc(), ReflectionManager.DEFAULT);
			}
			else
			{
				player.setReflection(ReflectionManager.DEFAULT);
			}
			player.unsetVar("backCoords");
			r.startCollapseTimer(1000);
		}
		else if (command.startsWith("ListPossible"))
		{
			if ((getNpcId() == 32484) && Config.ALT_KAMALOKA_NIGHTMARES_PREMIUM_ONLY && !player.hasBonus())
			{
				showChatWindow(player, "instance/soloKamaloka/32484-no.htm");
				return;
			}
			DomainArea domain = MapRegionManager.getInstance().getRegionData(DomainArea.class, this);
			String htmlpath = "instance/soloKamaloka/" + getNpcId();
			if (domain != null)
			{
				switch (domain.getId())
				{
					case 1:
						htmlpath += "-gludio";
						break;
					case 2:
						htmlpath += "-dion";
						break;
					case 4:
						htmlpath += "-oren";
						break;
					case 6:
						htmlpath += "-heine";
						break;
					case 8:
						htmlpath += "-rune";
						break;
					case 9:
						htmlpath += "-schuttgart";
						break;
				}
			}
			htmlpath += ".htm";
			showChatWindow(player, htmlpath);
		}
		else if (command.startsWith("ShowResults"))
		{
			String htmlpath = "instance/soloKamaloka/" + getNpcId();
			switch (getRewardRank())
			{
				case 0:
					htmlpath += "-F";
					break;
				case 1:
					htmlpath += "-D";
					break;
				case 2:
					htmlpath += "-C";
					break;
				case 3:
					htmlpath += "-B";
					break;
				case 4:
					htmlpath += "-A";
					break;
				case 5:
					htmlpath += "-S";
					break;
				case 6:
					if (getReflection().getInstancedZoneId() == INSTANCE_75LVL_ID)
					{
						htmlpath += "-G";
					}
					else
					{
						htmlpath += "-S";
					}
					break;
			}
			htmlpath += ".htm";
			showChatWindow(player, htmlpath);
		}
		else if (command.startsWith("SoloKamaReward"))
		{
			if (!_rewarded)
			{
				int[][] rewards = getRewardList(getRewardRank(), getReflection().getInstancedZone());
				if (rewards != null)
				{
					for (int[] item : rewards)
					{
						if (item != null)
						{
							int id = item[0];
							int count = item[1];
							if ((id > 0) && (count > 0))
							{
								Functions.addItem(player, id, count);
							}
						}
					}
				}
				_rewarded = true;
			}
			showChatWindow(player, 1);
		}
		else if (command.startsWith("Chat"))
		{
			try
			{
				int val = Integer.parseInt(command.substring(5));
				showChatWindow(player, val);
			}
			catch (NumberFormatException nfe)
			{
				String filename = command.substring(5).trim();
				if (filename.length() == 0)
				{
					showChatWindow(player, "npcdefault.htm");
				}
				else
				{
					showChatWindow(player, filename);
				}
			}
		}
		else if (command.startsWith("solo_kamaloka"))
		{
			int val = Integer.parseInt(command.substring(14));
			Reflection r = player.getActiveReflection();
			if (r != null)
			{
				if (player.canReenterInstance(val))
				{
					player.teleToLocation(r.getTeleportLoc(), r);
				}
			}
			else if (player.canEnterInstance(val))
			{
				ReflectionUtils.enterReflection(player, new KamalokaNightmare(player), val);
			}
		}
		else
		{
			super.onBypassFeedback(player, command);
		}
	}
	
	/**
	 * Method getRewardRank.
	 * @return int
	 */
	private int getRewardRank()
	{
		if (_rank >= 0)
		{
			return _rank;
		}
		Reflection r = getReflection();
		if (r instanceof KamalokaNightmare)
		{
			_rank = ((KamalokaNightmare) r).getRank();
		}
		else
		{
			_rank = 0;
		}
		return _rank;
	}
	
	/**
	 * Method getRewardList.
	 * @param rank int
	 * @param iz InstantZone
	 * @return int[][]
	 */
	private static int[][] getRewardList(int rank, InstantZone iz)
	{
		if (iz == null)
		{
			return null;
		}
		StatsSet params = iz.getAddParams();
		String rewards = null;
		for (int i = rank; i >= 0; i--)
		{
			rewards = params.getString("reward_lvl_" + i, null);
			if (rewards != null)
			{
				break;
			}
		}
		String[] items_list = rewards.split(";");
		int rewards_count = items_list.length;
		int[][] result = new int[rewards_count][];
		for (int i = 0; i < rewards_count; i++)
		{
			String[] item_s = items_list[i].split("-");
			if (item_s.length != 2)
			{
				continue;
			}
			int[] item = new int[2];
			item[0] = Integer.parseInt(item_s[0]);
			item[1] = Integer.parseInt(item_s[1]);
			result[i] = item;
		}
		return result;
	}
}
