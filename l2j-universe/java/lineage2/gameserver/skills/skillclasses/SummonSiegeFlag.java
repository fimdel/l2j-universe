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

import lineage2.gameserver.data.xml.holder.NpcHolder;
import lineage2.gameserver.geodata.GeoEngine;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.Zone;
import lineage2.gameserver.model.entity.events.impl.SiegeEvent;
import lineage2.gameserver.model.entity.events.objects.SiegeClanObject;
import lineage2.gameserver.model.entity.events.objects.ZoneObject;
import lineage2.gameserver.model.instances.residences.SiegeFlagInstance;
import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.network.serverpackets.SystemMessage2;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.stats.Stats;
import lineage2.gameserver.stats.funcs.FuncMul;
import lineage2.gameserver.templates.StatsSet;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class SummonSiegeFlag extends Skill
{
	/**
	 * @author Mobius
	 */
	public static enum FlagType
	{
		/**
		 * Field DESTROY.
		 */
		DESTROY,
		/**
		 * Field NORMAL.
		 */
		NORMAL,
		/**
		 * Field ADVANCED.
		 */
		ADVANCED,
		/**
		 * Field OUTPOST.
		 */
		OUTPOST
	}
	
	/**
	 * Field _flagType.
	 */
	private final FlagType _flagType;
	/**
	 * Field _advancedMult.
	 */
	private final double _advancedMult;
	
	/**
	 * Constructor for SummonSiegeFlag.
	 * @param set StatsSet
	 */
	public SummonSiegeFlag(StatsSet set)
	{
		super(set);
		_flagType = set.getEnum("flagType", FlagType.class);
		_advancedMult = set.getDouble("advancedMultiplier", 1.);
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
		if (!activeChar.isPlayer())
		{
			return false;
		}
		if (!super.checkCondition(activeChar, target, forceUse, dontMove, first))
		{
			return false;
		}
		Player player = (Player) activeChar;
		if ((player.getClan() == null) || !player.isClanLeader())
		{
			return false;
		}
		switch (_flagType)
		{
			case DESTROY:
				break;
			case OUTPOST:
			case NORMAL:
			case ADVANCED:
				if (player.isInZone(Zone.ZoneType.RESIDENCE))
				{
					player.sendPacket(SystemMsg.YOU_CANNOT_SET_UP_A_BASE_HERE, new SystemMessage2(SystemMsg.S1_CANNOT_BE_USED_DUE_TO_UNSUITABLE_TERMS).addSkillName(this));
					return false;
				}
				SiegeEvent<?, ?> siegeEvent = activeChar.getEvent(SiegeEvent.class);
				if (siegeEvent == null)
				{
					player.sendPacket(SystemMsg.YOU_CANNOT_SET_UP_A_BASE_HERE, new SystemMessage2(SystemMsg.S1_CANNOT_BE_USED_DUE_TO_UNSUITABLE_TERMS).addSkillName(this));
					return false;
				}
				boolean inZone = false;
				List<ZoneObject> zones = siegeEvent.getObjects(SiegeEvent.FLAG_ZONES);
				for (ZoneObject zone : zones)
				{
					if (player.isInZone(zone.getZone()))
					{
						inZone = true;
					}
				}
				if (!inZone)
				{
					player.sendPacket(SystemMsg.YOU_CANNOT_SET_UP_A_BASE_HERE, new SystemMessage2(SystemMsg.S1_CANNOT_BE_USED_DUE_TO_UNSUITABLE_TERMS).addSkillName(this));
					return false;
				}
				SiegeClanObject siegeClan = siegeEvent.getSiegeClan(SiegeEvent.ATTACKERS, player.getClan());
				if (siegeClan == null)
				{
					player.sendPacket(SystemMsg.YOU_CANNOT_SUMMON_THE_ENCAMPMENT_BECAUSE_YOU_ARE_NOT_A_MEMBER_OF_THE_SIEGE_CLAN_INVOLVED_IN_THE_CASTLE__FORTRESS__HIDEOUT_SIEGE, new SystemMessage2(SystemMsg.S1_CANNOT_BE_USED_DUE_TO_UNSUITABLE_TERMS).addSkillName(this));
					return false;
				}
				if (siegeClan.getFlag() != null)
				{
					player.sendPacket(SystemMsg.AN_OUTPOST_OR_HEADQUARTERS_CANNOT_BE_BUILT_BECAUSE_ONE_ALREADY_EXISTS, new SystemMessage2(SystemMsg.S1_CANNOT_BE_USED_DUE_TO_UNSUITABLE_TERMS).addSkillName(this));
					return false;
				}
				break;
		}
		return true;
	}
	
	/**
	 * Method useSkill.
	 * @param activeChar Creature
	 * @param targets List<Creature>
	 */
	@Override
	public void useSkill(Creature activeChar, List<Creature> targets)
	{
		Player player = (Player) activeChar;
		Clan clan = player.getClan();
		if ((clan == null) || !player.isClanLeader())
		{
			return;
		}
		SiegeEvent<?, ?> siegeEvent = activeChar.getEvent(SiegeEvent.class);
		if (siegeEvent == null)
		{
			return;
		}
		SiegeClanObject siegeClan = siegeEvent.getSiegeClan(SiegeEvent.ATTACKERS, clan);
		if (siegeClan == null)
		{
			return;
		}
		switch (_flagType)
		{
			case DESTROY:
				siegeClan.deleteFlag();
				break;
			default:
				if (siegeClan.getFlag() != null)
				{
					return;
				}
				SiegeFlagInstance flag = (SiegeFlagInstance) NpcHolder.getInstance().getTemplate(_flagType == FlagType.OUTPOST ? 36590 : 35062).getNewInstance();
				flag.setClan(siegeClan);
				flag.addEvent(siegeEvent);
				if (_flagType == FlagType.ADVANCED)
				{
					flag.addStatFunc(new FuncMul(Stats.MAX_HP, 0x50, flag, _advancedMult));
				}
				flag.setCurrentHpMp(flag.getMaxHp(), flag.getMaxMp(), true);
				flag.setHeading(player.getHeading());
				int x = (int) (player.getX() + (100 * Math.cos(player.headingToRadians(player.getHeading() - 32768))));
				int y = (int) (player.getY() + (100 * Math.sin(player.headingToRadians(player.getHeading() - 32768))));
				flag.spawnMe(GeoEngine.moveCheck(player.getX(), player.getY(), player.getZ(), x, y, player.getGeoIndex()));
				siegeClan.setFlag(flag);
				break;
		}
	}
}
