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

import static lineage2.gameserver.model.Zone.ZoneType.no_restart;
import static lineage2.gameserver.model.Zone.ZoneType.no_summon;

import java.util.List;

import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.instancemanager.ReflectionManager;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.Zone;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.templates.StatsSet;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Call extends Skill
{
	/**
	 * Field _party.
	 */
	final boolean _party;
	
	/**
	 * Constructor for Call.
	 * @param set StatsSet
	 */
	public Call(StatsSet set)
	{
		super(set);
		_party = set.getBool("party", false);
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
		if (activeChar.isPlayer())
		{
			if (_party && (((Player) activeChar).getParty() == null))
			{
				return false;
			}
			SystemMessage msg = canSummonHere((Player) activeChar);
			if (msg != null)
			{
				activeChar.sendPacket(msg);
				return false;
			}
			if (!_party)
			{
				if (activeChar == target)
				{
					return false;
				}
				msg = canBeSummoned(target);
				if (msg != null)
				{
					activeChar.sendPacket(msg);
					return false;
				}
			}
		}
		return super.checkCondition(activeChar, target, forceUse, dontMove, first);
	}
	
	/**
	 * Method useSkill.
	 * @param activeChar Creature
	 * @param targets List<Creature>
	 */
	@Override
	public void useSkill(Creature activeChar, List<Creature> targets)
	{
		if (!activeChar.isPlayer())
		{
			return;
		}
		SystemMessage msg = canSummonHere((Player) activeChar);
		if (msg != null)
		{
			activeChar.sendPacket(msg);
			return;
		}
		if (_party)
		{
			if (((Player) activeChar).getParty() != null)
			{
				for (Player target : ((Player) activeChar).getParty().getPartyMembers())
				{
					if (!target.equals(activeChar) && (canBeSummoned(target) == null) && !target.isTerritoryFlagEquipped())
					{
						target.stopMove();
						target.teleToLocation(Location.findPointToStay(activeChar, 100, 150), activeChar.getGeoIndex());
						getEffects(activeChar, target, getActivateRate() > 0, false);
					}
				}
			}
			if (isSSPossible())
			{
				activeChar.unChargeShots(isMagic());
			}
			return;
		}
		for (Creature target : targets)
		{
			if (target != null)
			{
				if (canBeSummoned(target) != null)
				{
					continue;
				}
				((Player) target).summonCharacterRequest(activeChar, Location.findAroundPosition(activeChar, 100, 150), (getId() == 1403) || (getId() == 1404) ? 1 : 0);
				getEffects(activeChar, target, getActivateRate() > 0, false);
			}
		}
		if (isSSPossible())
		{
			activeChar.unChargeShots(isMagic());
		}
	}
	
	/**
	 * Method canSummonHere.
	 * @param activeChar Player
	 * @return SystemMessage
	 */
	public static SystemMessage canSummonHere(Player activeChar)
	{
		if (activeChar.isAlikeDead() || activeChar.isInOlympiadMode() || activeChar.isInObserverMode() || activeChar.isFlying())
		{
			return Msg.NOTHING_HAPPENED;
		}
		if (activeChar.isInZoneBattle() || activeChar.isInZone(Zone.ZoneType.SIEGE) || activeChar.isInZone(no_restart) || activeChar.isInZone(no_summon) || activeChar.isInBoat() || (activeChar.getReflection() != ReflectionManager.DEFAULT))
		{
			return Msg.YOU_MAY_NOT_SUMMON_FROM_YOUR_CURRENT_LOCATION;
		}
		if (activeChar.isInStoreMode() || activeChar.isProcessingRequest())
		{
			return Msg.YOU_CANNOT_SUMMON_DURING_A_TRADE_OR_WHILE_USING_THE_PRIVATE_SHOPS;
		}
		return null;
	}
	
	/**
	 * Method canBeSummoned.
	 * @param target Creature
	 * @return SystemMessage
	 */
	public static SystemMessage canBeSummoned(Creature target)
	{
		if ((target == null) || !target.isPlayer() || target.getPlayer().isTerritoryFlagEquipped() || target.isFlying() || target.isInObserverMode() || !target.getPlayer().getPlayerAccess().UseTeleport)
		{
			return Msg.INVALID_TARGET;
		}
		if (target.isInOlympiadMode())
		{
			return Msg.YOU_CANNOT_SUMMON_PLAYERS_WHO_ARE_CURRENTLY_PARTICIPATING_IN_THE_GRAND_OLYMPIAD;
		}
		if (target.isInZoneBattle() || target.isInZone(Zone.ZoneType.SIEGE) || target.isInZone(no_restart) || target.isInZone(no_summon) || (target.getReflection() != ReflectionManager.DEFAULT) || target.isInBoat())
		{
			return Msg.YOUR_TARGET_IS_IN_AN_AREA_WHICH_BLOCKS_SUMMONING;
		}
		if (target.isAlikeDead())
		{
			return new SystemMessage(SystemMessage.S1_IS_DEAD_AT_THE_MOMENT_AND_CANNOT_BE_SUMMONED).addString(target.getName());
		}
		if ((target.getPvpFlag() != 0) || target.isInCombat())
		{
			return new SystemMessage(SystemMessage.S1_IS_ENGAGED_IN_COMBAT_AND_CANNOT_BE_SUMMONED).addString(target.getName());
		}
		Player pTarget = (Player) target;
		if ((pTarget.getPrivateStoreType() != Player.STORE_PRIVATE_NONE) || pTarget.isProcessingRequest())
		{
			return new SystemMessage(SystemMessage.S1_IS_CURRENTLY_TRADING_OR_OPERATING_A_PRIVATE_STORE_AND_CANNOT_BE_SUMMONED).addString(target.getName());
		}
		return null;
	}
}
