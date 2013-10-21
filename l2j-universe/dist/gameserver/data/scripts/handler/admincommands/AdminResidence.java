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
package handler.admincommands;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import lineage2.commons.dao.JdbcEntityState;
import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.data.xml.holder.ResidenceHolder;
import lineage2.gameserver.handler.admincommands.AdminCommandHandler;
import lineage2.gameserver.model.GameObject;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.events.impl.FortressSiegeEvent;
import lineage2.gameserver.model.entity.events.impl.SiegeEvent;
import lineage2.gameserver.model.entity.events.objects.SiegeClanObject;
import lineage2.gameserver.model.entity.residence.Fortress;
import lineage2.gameserver.model.entity.residence.Residence;
import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.network.serverpackets.NpcHtmlMessage;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.tables.ClanTable;
import lineage2.gameserver.utils.HtmlUtils;
import npc.model.residences.fortress.siege.BackupPowerUnitInstance;
import npc.model.residences.fortress.siege.PowerControlUnitInstance;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class AdminResidence extends ScriptAdminCommand
{
	/**
	 * @author Mobius
	 */
	private static enum Commands
	{
		/**
		 * Field admin_residence_list.
		 */
		admin_residence_list,
		/**
		 * Field admin_residence.
		 */
		admin_residence,
		/**
		 * Field admin_set_owner.
		 */
		admin_set_owner,
		/**
		 * Field admin_set_siege_time.
		 */
		admin_set_siege_time,
		/**
		 * Field admin_quick_siege_start.
		 */
		admin_quick_siege_start,
		/**
		 * Field admin_quick_siege_stop.
		 */
		admin_quick_siege_stop,
		/**
		 * Field admin_backup_unit_info.
		 */
		admin_backup_unit_info,
		/**
		 * Field admin_fortress_spawn_flags.
		 */
		admin_fortress_spawn_flags
	}
	
	/**
	 * Method useAdminCommand.
	 * @param comm Enum<?>
	 * @param wordList String[]
	 * @param fullString String
	 * @param activeChar Player
	 * @return boolean * @see lineage2.gameserver.handler.admincommands.IAdminCommandHandler#useAdminCommand(Enum<?>, String[], String, Player)
	 */
	@Override
	public boolean useAdminCommand(Enum<?> comm, String[] wordList, String fullString, Player activeChar)
	{
		final Commands command = (Commands) comm;
		if (!activeChar.getPlayerAccess().CanEditNPC)
		{
			return false;
		}
		final Residence r;
		final SiegeEvent<?, ?> event;
		Calendar calendar;
		NpcHtmlMessage msg;
		switch (command)
		{
			case admin_residence_list:
				msg = new NpcHtmlMessage(5);
				msg.setFile("admin/residence/residence_list.htm");
				final StringBuilder replyMSG = new StringBuilder(200);
				for (Residence residence : ResidenceHolder.getInstance().getResidences())
				{
					if (residence != null)
					{
						replyMSG.append("<tr><td>");
						replyMSG.append("<a action=\"bypass -h admin_residence ").append(residence.getId()).append("\">").append(HtmlUtils.htmlResidenceName(residence.getId())).append("</a>");
						replyMSG.append("</td><td>");
						Clan owner = residence.getOwner();
						if (owner == null)
						{
							replyMSG.append("NPC");
						}
						else
						{
							replyMSG.append(owner.getName());
						}
						replyMSG.append("</td></tr>");
					}
				}
				msg.replace("%residence_list%", replyMSG.toString());
				activeChar.sendPacket(msg);
				break;
			case admin_residence:
				if (wordList.length != 2)
				{
					return false;
				}
				r = ResidenceHolder.getInstance().getResidence(Integer.parseInt(wordList[1]));
				if (r == null)
				{
					return false;
				}
				event = r.getSiegeEvent();
				msg = new NpcHtmlMessage(5);
				msg.setFile("admin/residence/siege_info.htm");
				msg.replace("%residence%", HtmlUtils.htmlResidenceName(r.getId()));
				msg.replace("%id%", String.valueOf(r.getId()));
				msg.replace("%owner%", (r.getOwner() == null) ? "NPC" : r.getOwner().getName());
				msg.replace("%cycle%", String.valueOf(r.getCycle()));
				msg.replace("%paid_cycle%", String.valueOf(r.getPaidCycle()));
				msg.replace("%reward_count%", String.valueOf(r.getRewardCount()));
				msg.replace("%left_time%", String.valueOf(r.getCycleDelay()));
				final StringBuilder clans = new StringBuilder(100);
				for (Map.Entry<String, List<Serializable>> entry : event.getObjects().entrySet())
				{
					for (Serializable o : entry.getValue())
					{
						if (o instanceof SiegeClanObject)
						{
							SiegeClanObject siegeClanObject = (SiegeClanObject) o;
							clans.append("<tr>").append("<td>").append(siegeClanObject.getClan().getName()).append("</td>").append("<td>").append(siegeClanObject.getClan().getLeaderName()).append("</td>").append("<td>").append(siegeClanObject.getType()).append("</td>").append("</tr>");
						}
					}
				}
				msg.replace("%clans%", clans.toString());
				msg.replace("%hour%", String.valueOf(r.getSiegeDate().get(Calendar.HOUR_OF_DAY)));
				msg.replace("%minute%", String.valueOf(r.getSiegeDate().get(Calendar.MINUTE)));
				msg.replace("%day%", String.valueOf(r.getSiegeDate().get(Calendar.DAY_OF_MONTH)));
				msg.replace("%month%", String.valueOf(r.getSiegeDate().get(Calendar.MONTH) + 1));
				msg.replace("%year%", String.valueOf(r.getSiegeDate().get(Calendar.YEAR)));
				activeChar.sendPacket(msg);
				break;
			case admin_set_owner:
				if (wordList.length != 3)
				{
					return false;
				}
				r = ResidenceHolder.getInstance().getResidence(Integer.parseInt(wordList[1]));
				if (r == null)
				{
					return false;
				}
				Clan clan = null;
				final String clanName = wordList[2];
				if (!clanName.equalsIgnoreCase("npc"))
				{
					clan = ClanTable.getInstance().getClanByName(clanName);
					if (clan == null)
					{
						activeChar.sendPacket(SystemMsg.INCORRECT_NAME);
						AdminCommandHandler.getInstance().useAdminCommandHandler(activeChar, "admin_residence " + r.getId());
						return false;
					}
				}
				event = r.getSiegeEvent();
				event.clearActions();
				r.getLastSiegeDate().setTimeInMillis((clan == null) ? 0 : System.currentTimeMillis());
				r.getOwnDate().setTimeInMillis((clan == null) ? 0 : System.currentTimeMillis());
				r.changeOwner(clan);
				event.reCalcNextTime(false);
				break;
			case admin_set_siege_time:
				r = ResidenceHolder.getInstance().getResidence(Integer.parseInt(wordList[1]));
				if (r == null)
				{
					return false;
				}
				calendar = (Calendar) r.getSiegeDate().clone();
				for (int i = 2; i < wordList.length; i++)
				{
					int type;
					int val = Integer.parseInt(wordList[i]);
					switch (i)
					{
						case 2:
							type = Calendar.HOUR_OF_DAY;
							break;
						case 3:
							type = Calendar.MINUTE;
							break;
						case 4:
							type = Calendar.DAY_OF_MONTH;
							break;
						case 5:
							type = Calendar.MONTH;
							val -= 1;
							break;
						case 6:
							type = Calendar.YEAR;
							break;
						default:
							continue;
					}
					calendar.set(type, val);
				}
				event = r.getSiegeEvent();
				event.clearActions();
				r.getSiegeDate().setTimeInMillis(calendar.getTimeInMillis());
				event.registerActions();
				r.setJdbcState(JdbcEntityState.UPDATED);
				r.update();
				AdminCommandHandler.getInstance().useAdminCommandHandler(activeChar, "admin_residence " + r.getId());
				break;
			case admin_quick_siege_start:
				r = ResidenceHolder.getInstance().getResidence(Integer.parseInt(wordList[1]));
				if (r == null)
				{
					return false;
				}
				calendar = Calendar.getInstance();
				if (wordList.length >= 3)
				{
					calendar.set(Calendar.SECOND, -Integer.parseInt(wordList[2]));
				}
				event = r.getSiegeEvent();
				event.clearActions();
				r.getSiegeDate().setTimeInMillis(calendar.getTimeInMillis());
				event.registerActions();
				r.setJdbcState(JdbcEntityState.UPDATED);
				r.update();
				AdminCommandHandler.getInstance().useAdminCommandHandler(activeChar, "admin_residence " + r.getId());
				break;
			case admin_quick_siege_stop:
				r = ResidenceHolder.getInstance().getResidence(Integer.parseInt(wordList[1]));
				if (r == null)
				{
					return false;
				}
				event = r.getSiegeEvent();
				event.clearActions();
				ThreadPoolManager.getInstance().execute(new RunnableImpl()
				{
					@Override
					public void runImpl()
					{
						event.stopEvent();
					}
				});
				AdminCommandHandler.getInstance().useAdminCommandHandler(activeChar, "admin_residence " + r.getId());
				break;
			case admin_backup_unit_info:
				final GameObject target = activeChar.getTarget();
				if (!(target instanceof PowerControlUnitInstance) && !(target instanceof BackupPowerUnitInstance))
				{
					return false;
				}
				final List<String> t = new ArrayList<>(3);
				if (target instanceof PowerControlUnitInstance)
				{
					for (int i : ((PowerControlUnitInstance) target).getGenerated())
					{
						t.add(String.valueOf(i));
					}
				}
				else
				{
					for (int i : ((BackupPowerUnitInstance) target).getGenerated())
					{
						t.add((i == 0) ? "A" : (i == 1) ? "B" : (i == 2) ? "C" : "D");
					}
				}
				activeChar.sendMessage("Password: " + t.toString());
				return true;
			case admin_fortress_spawn_flags:
				if (wordList.length != 2)
				{
					return false;
				}
				final Fortress fortress = ResidenceHolder.getInstance().getResidence(Fortress.class, Integer.parseInt(wordList[1]));
				if (fortress == null)
				{
					return false;
				}
				final FortressSiegeEvent siegeEvent = fortress.getSiegeEvent();
				if (!siegeEvent.isInProgress())
				{
					return false;
				}
				final boolean[] f = siegeEvent.getBarrackStatus();
				for (int i = 0; i < f.length; i++)
				{
					siegeEvent.barrackAction(i, true);
				}
				siegeEvent.spawnFlags();
				return true;
		}
		return true;
	}
	
	/**
	 * Method getAdminCommandEnum.
	 * @return Enum[] * @see lineage2.gameserver.handler.admincommands.IAdminCommandHandler#getAdminCommandEnum()
	 */
	@Override
	public Enum<?>[] getAdminCommandEnum()
	{
		return Commands.values();
	}
}
