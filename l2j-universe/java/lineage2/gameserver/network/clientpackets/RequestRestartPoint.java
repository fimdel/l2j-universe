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
package lineage2.gameserver.network.clientpackets;

import lineage2.commons.lang.ArrayUtils;
import lineage2.gameserver.instancemanager.ReflectionManager;
import lineage2.gameserver.listener.actor.player.OnAnswerListener;
import lineage2.gameserver.listener.actor.player.impl.ReviveAnswerListener;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.base.RestartType;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.entity.events.GlobalEvent;
import lineage2.gameserver.model.entity.residence.Castle;
import lineage2.gameserver.model.entity.residence.ClanHall;
import lineage2.gameserver.model.entity.residence.Fortress;
import lineage2.gameserver.model.entity.residence.ResidenceFunction;
import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.network.serverpackets.ActionFail;
import lineage2.gameserver.network.serverpackets.Die;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.utils.ItemFunctions;
import lineage2.gameserver.utils.Location;
import lineage2.gameserver.utils.TeleportUtils;

import org.apache.commons.lang3.tuple.Pair;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestRestartPoint extends L2GameClientPacket
{
	/**
	 * Field _restartType.
	 */
	private RestartType _restartType;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_restartType = ArrayUtils.valid(RestartType.VALUES, readD());
	}
	
	/**
	 * Method runImpl.
	 */
	@Override
	protected void runImpl()
	{
		Player activeChar = getClient().getActiveChar();
		if ((_restartType == null) || (activeChar == null))
		{
			return;
		}
		if (activeChar.isFakeDeath())
		{
			activeChar.breakFakeDeath();
			return;
		}
		if (!activeChar.isDead() && !activeChar.isGM())
		{
			activeChar.sendActionFailed();
			return;
		}
		switch (_restartType)
		{
			case AGATHION:
				if (activeChar.isAgathionResAvailable())
				{
					activeChar.doRevive(100);
				}
				else
				{
					activeChar.sendPacket(ActionFail.STATIC, new Die(activeChar));
				}
				break;
			case FIXED:
				if (activeChar.getPlayerAccess().ResurectFixed)
				{
					activeChar.doRevive(100);
				}
				else if (ItemFunctions.removeItem(activeChar, 13300, 1, true) == 1)
				{
					activeChar.sendPacket(SystemMsg.YOU_HAVE_USED_THE_FEATHER_OF_BLESSING_TO_RESURRECT);
					activeChar.doRevive(100);
				}
				else if (ItemFunctions.removeItem(activeChar, 10649, 1, true) == 1)
				{
					activeChar.sendPacket(SystemMsg.YOU_HAVE_USED_THE_FEATHER_OF_BLESSING_TO_RESURRECT);
					activeChar.doRevive(100);
				}
				else
				{
					activeChar.sendPacket(ActionFail.STATIC, new Die(activeChar));
				}
				break;
			default:
				Location loc = null;
				Reflection ref = activeChar.getReflection();
				if (ref == ReflectionManager.DEFAULT)
				{
					for (GlobalEvent e : activeChar.getEvents())
					{
						loc = e.getRestartLoc(activeChar, _restartType);
					}
				}
				if (loc == null)
				{
					loc = defaultLoc(_restartType, activeChar);
				}
				if (loc != null)
				{
					Pair<Integer, OnAnswerListener> ask = activeChar.getAskListener(false);
					if ((ask != null) && (ask.getValue() instanceof ReviveAnswerListener) && !((ReviveAnswerListener) ask.getValue()).isForPet())
					{
						activeChar.getAskListener(true);
					}
					activeChar.setPendingRevive(true);
					activeChar.teleToLocation(loc, ReflectionManager.DEFAULT);
				}
				else
				{
					activeChar.sendPacket(ActionFail.STATIC, new Die(activeChar));
				}
				break;
		}
	}
	
	/**
	 * Method defaultLoc.
	 * @param restartType RestartType
	 * @param activeChar Player
	 * @return Location
	 */
	public static Location defaultLoc(RestartType restartType, Player activeChar)
	{
		Location loc = null;
		Clan clan = activeChar.getClan();
		switch (restartType)
		{
			case TO_CLANHALL:
				if ((clan != null) && (clan.getHasHideout() != 0))
				{
					ClanHall clanHall = activeChar.getClanHall();
					loc = TeleportUtils.getRestartLocation(activeChar, RestartType.TO_CLANHALL);
					if (clanHall.getFunction(ResidenceFunction.RESTORE_EXP) != null)
					{
						activeChar.restoreExp(clanHall.getFunction(ResidenceFunction.RESTORE_EXP).getLevel());
					}
				}
				break;
			case TO_CASTLE:
				if ((clan != null) && (clan.getCastle() != 0))
				{
					Castle castle = activeChar.getCastle();
					loc = TeleportUtils.getRestartLocation(activeChar, RestartType.TO_CASTLE);
					if (castle.getFunction(ResidenceFunction.RESTORE_EXP) != null)
					{
						activeChar.restoreExp(castle.getFunction(ResidenceFunction.RESTORE_EXP).getLevel());
					}
				}
				break;
			case TO_FORTRESS:
				if ((clan != null) && (clan.getHasFortress() != 0))
				{
					Fortress fort = activeChar.getFortress();
					loc = TeleportUtils.getRestartLocation(activeChar, RestartType.TO_FORTRESS);
					if (fort.getFunction(ResidenceFunction.RESTORE_EXP) != null)
					{
						activeChar.restoreExp(fort.getFunction(ResidenceFunction.RESTORE_EXP).getLevel());
					}
				}
				break;
			case TO_VILLAGE:
			default:
				loc = TeleportUtils.getRestartLocation(activeChar, RestartType.TO_VILLAGE);
				break;
		}
		return loc;
	}
}
