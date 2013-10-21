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
package lineage2.gameserver.skills.skillclasses;

import java.util.List;

import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.Zone.ZoneType;
import lineage2.gameserver.model.base.TeamType;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.network.serverpackets.components.CustomMessage;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.templates.StatsSet;
import lineage2.gameserver.utils.Location;

/**
 * @author vegax
 * @version $Revision: 1.0 $
 */
public class Recall extends Skill
{
	/**
	 * Field _townId.
	 */
	private final int _townId;
	/**
	 * Field _clanhall.
	 */
	private final boolean _clanhall;
	/**
	 * Field _castle.
	 */
	private final boolean _castle;
	/**
	 * Field _fortress.
	 */
	private final boolean _fortress;
	/**
	 * Field _loc.
	 */
	private final Location _loc;
	
	/**
	 * Constructor for Recall.
	 * @param set StatsSet
	 */
	public Recall(StatsSet set)
	{
		super(set);
		_townId = set.getInteger("townId", 0);
		_clanhall = set.getBool("clanhall", false);
		_castle = set.getBool("castle", false);
		_fortress = set.getBool("fortress", false);
		String[] cords = set.getString("loc", "").split(";");
		if (cords.length == 3)
		{
			_loc = new Location(Integer.parseInt(cords[0]), Integer.parseInt(cords[1]), Integer.parseInt(cords[2]));
		}
		else
		{
			_loc = null;
		}
	}
	
	/**
	 * Method checkCondition.
	 * @param activeChar Creature
	 * @param target Creature
	 * @param forceUse boolean
	 * @param dontMove boolean
	 * @param first boolean
	 * @return boolean
	 */
	@Override
	public boolean checkCondition(Creature activeChar, Creature target, boolean forceUse, boolean dontMove, boolean first)
	{
		if (getHitTime() == 200)
		{
			Player player = activeChar.getPlayer();
			if (_clanhall)
			{
				if ((player.getClan() == null) || (player.getClan().getHasHideout() == 0))
				{
					activeChar.sendPacket(new SystemMessage(SystemMessage.S1_CANNOT_BE_USED_DUE_TO_UNSUITABLE_TERMS).addItemName(_itemConsumeId[0]));
					return false;
				}
			}
			else if (_castle)
			{
				if ((player.getClan() == null) || (player.getClan().getCastle() == 0))
				{
					activeChar.sendPacket(new SystemMessage(SystemMessage.S1_CANNOT_BE_USED_DUE_TO_UNSUITABLE_TERMS).addItemName(_itemConsumeId[0]));
					return false;
				}
			}
			else if (_fortress)
			{
				if ((player.getClan() == null) || (player.getClan().getHasFortress() == 0))
				{
					activeChar.sendPacket(new SystemMessage(SystemMessage.S1_CANNOT_BE_USED_DUE_TO_UNSUITABLE_TERMS).addItemName(_itemConsumeId[0]));
					return false;
				}
			}
		}
		if (activeChar.isPlayer())
		{
			Player p = (Player) activeChar;
			if (p.getActiveWeaponFlagAttachment() != null)
			{
				activeChar.sendPacket(SystemMsg.YOU_CANNOT_TELEPORT_WHILE_IN_POSSESSION_OF_A_WARD);
				return false;
			}
			if (p.isInDuel() || (p.getTeam() != TeamType.NONE))
			{
				activeChar.sendMessage(new CustomMessage("common.RecallInDuel", p));
				return false;
			}
			if (p.isInOlympiadMode())
			{
				activeChar.sendPacket(Msg.THIS_SKILL_IS_NOT_AVAILABLE_FOR_THE_OLYMPIAD_EVENT);
				return false;
			}
		}
		if (activeChar.isInZone(ZoneType.no_escape) || ((_townId > 0) && (activeChar.getReflection() != null) && (activeChar.getReflection().getCoreLoc() != null)))
		{
			if (activeChar.isPlayer())
			{
				activeChar.sendMessage(new CustomMessage("lineage2.gameserver.skills.skillclasses.Recall.Here", (Player) activeChar));
			}
			return false;
		}
		return super.checkCondition(activeChar, target, forceUse, dontMove, first);
	}
	
	/**
	 * Method useSkill.
	 * @param activeChar Creature
	 * @param targets List<Creature>
	 */
	@Override
	public void useSkill(final Creature activeChar, List<Creature> targets)
	{
		for (final Creature target : targets)
		{
			if (target != null)
			{
				final Player pcTarget = target.getPlayer();
				if (pcTarget == null)
				{
					continue;
				}
				if (!pcTarget.getPlayerAccess().UseTeleport)
				{
					continue;
				}
				if (pcTarget.getActiveWeaponFlagAttachment() != null)
				{
					activeChar.sendPacket(Msg.YOU_CANNOT_TELEPORT_WHILE_IN_POSSESSION_OF_A_WARD);
					continue;
				}
				if (pcTarget.isInOlympiadMode())
				{
					activeChar.sendPacket(Msg.YOU_CANNOT_SUMMON_PLAYERS_WHO_ARE_CURRENTLY_PARTICIPATING_IN_THE_GRAND_OLYMPIAD);
					return;
				}
				if (pcTarget.isInObserverMode())
				{
					activeChar.sendPacket(new SystemMessage(SystemMessage.S1_CANNOT_BE_USED_DUE_TO_UNSUITABLE_TERMS).addSkillName(getId(), getLevel()));
					return;
				}
				if (pcTarget.isInDuel() || (pcTarget.getTeam() != TeamType.NONE))
				{
					activeChar.sendMessage(new CustomMessage("common.RecallInDuel", (Player) activeChar));
					return;
				}
				if (_isItemHandler)
				{
					if (_itemConsumeId[0] == 7125)
					{
						pcTarget.teleToLocation(17144, 170156, -3502, 0);
						return;
					}
					if (_itemConsumeId[0] == 7127)
					{
						pcTarget.teleToLocation(105918, 109759, -3207, 0);
						return;
					}
					if (_itemConsumeId[0] == 7130)
					{
						pcTarget.teleToLocation(85475, 16087, -3672, 0);
						return;
					}
					if (_itemConsumeId[0] == 9716)
					{
						pcTarget.teleToLocation(-120000, 44500, 352, 0);
						return;
					}
					if (_itemConsumeId[0] == 7618)
					{
						pcTarget.teleToLocation(149864, -81062, -5618, 0);
						return;
					}
					if (_itemConsumeId[0] == 7619)
					{
						pcTarget.teleToLocation(108275, -53785, -2524, 0);
						return;
					}
					
					if(_itemConsumeId[0] == 34978) //Scroll of Escape: Hot Springs
					{
						pcTarget.teleToLocation(155784, -105640, -2778, 0);
						return;
					}
					if(_itemConsumeId[0] == 34979) //Scroll of Escape: Forge of the Gods
					{
						pcTarget.teleToLocation(175992, -116088, -3798, 0);
						return;
}
					if(_itemConsumeId[0] == 34980) //Scroll of Escape: Isle of Prayer
					{
						pcTarget.teleToLocation(156984, 169736, -3514, 0);
						return;
					}
					if(_itemConsumeId[0] == 34981) //Scroll of Escape: Aden Castle(Siege)
					{
						pcTarget.teleToLocation(147432, 15640, -1448, 0);
						return;
					}
				}
				if (_loc != null)
				{
					pcTarget.teleToLocation(_loc);
					return;
				}
				switch (_townId)
				{
					case 1:
						pcTarget.teleToLocation(-114558, 253605, -1536, 0);
						return;
					case 2:
						pcTarget.teleToLocation(45576, 49412, -2950, 0);
						return;
					case 3:
						pcTarget.teleToLocation(12501, 16768, -4500, 0);
						return;
					case 4:
						pcTarget.teleToLocation(-44884, -115063, -80, 0);
						return;
					case 5:
						pcTarget.teleToLocation(115790, -179146, -890, 0);
						return;
					case 6:
						pcTarget.teleToLocation(-14279, 124446, -3000, 0);
						return;
					case 7:
						pcTarget.teleToLocation(-82909, 150357, -3000, 0);
						return;
					case 8:
						pcTarget.teleToLocation(19025, 145245, -3107, 0);
						return;
					case 9:
						pcTarget.teleToLocation(82272, 147801, -3350, 0);
						return;
					case 10:
						pcTarget.teleToLocation(82323, 55466, -1480, 0);
						return;
					case 11:
						pcTarget.teleToLocation(144526, 24661, -2100, 0);
						return;
					case 12:
						pcTarget.teleToLocation(117189, 78952, -2210, 0);
						return;
					case 13:
						pcTarget.teleToLocation(110768, 219824, -3624, 0);
						return;
					case 14:
						pcTarget.teleToLocation(43536, -50416, -800, 0);
						return;
					case 15:
						pcTarget.teleToLocation(148288, -58304, -2979, 0);
						return;
					case 16:
						pcTarget.teleToLocation(87776, -140384, -1536, 0);
						return;
					case 17:
						pcTarget.teleToLocation(-117081, 44171, 507, 0);
						return;
					case 18:
						pcTarget.teleToLocation(10568, -24600, -3648, 0);
						return;
					case 19:
						pcTarget.teleToLocation(19025, 145245, -3107, 0);
						return;
					case 20:
						pcTarget.teleToLocation(-16434, 208803, -3664, 0);
						return;
					case 21:
						pcTarget.teleToLocation(-184200, 243080, 1568, 0);
						return;
					case 22:
						pcTarget.teleToLocation(8976, 252416, -1928, 0);
						return;
				}
				if (_castle)
				{
					pcTarget.teleToCastle();
					return;
				}
				if (_clanhall)
				{
					pcTarget.teleToClanhall();
					return;
				}
				if (_fortress)
				{
					pcTarget.teleToFortress();
					return;
				}
				pcTarget.teleToClosestTown();
			}
		}
		if (isSSPossible())
		{
			activeChar.unChargeShots(isMagic());
		}
	}
}
