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

import java.util.ArrayList;
import java.util.List;

import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Zone;
import lineage2.gameserver.model.base.Race;
import lineage2.gameserver.model.instances.WarehouseInstance;
import lineage2.gameserver.network.serverpackets.MagicSkillUse;
import lineage2.gameserver.network.serverpackets.SystemMessage2;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.tables.SkillTable;
import lineage2.gameserver.templates.npc.NpcTemplate;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ArenaManagerInstance extends WarehouseInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field _arenaBuff.
	 */
	private final static int[][] _arenaBuff = new int[][]
	{
		{
			6803,
			0
		},
		{
			6804,
			2
		},
		{
			6805,
			1
		},
		{
			6806,
			1
		},
		{
			6807,
			1
		},
		{
			6808,
			2
		},
		{
			6809,
			0
		},
		{
			6811,
			0
		},
		{
			6812,
			2
		},
	};
	
	/**
	 * Constructor for ArenaManagerInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public ArenaManagerInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
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
		if (command.equalsIgnoreCase("ArenaBuff"))
		{
			if (player.isCursedWeaponEquipped() || player.isInZone(Zone.ZoneType.battle_zone))
			{
				return;
			}
			int neededmoney = 2000;
			long currentmoney = player.getAdena();
			if (neededmoney > currentmoney)
			{
				player.sendPacket(SystemMsg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
				return;
			}
			player.reduceAdena(neededmoney, true);
			List<Creature> target = new ArrayList<>();
			target.add(player);
			for (int[] buff : _arenaBuff)
			{
				if (player.isMageClass() && (player.getTemplate().getRace() != Race.orc))
				{
					if ((buff[1] == 1) || (buff[1] == 2))
					{
						broadcastPacket(new MagicSkillUse(this, player, buff[0], 1, 0, 0));
						callSkill(SkillTable.getInstance().getInfo(buff[0], 1), target, true);
					}
				}
				else
				{
					if ((buff[1] == 0) || (buff[1] == 2))
					{
						broadcastPacket(new MagicSkillUse(this, player, buff[0], 1, 0, 0));
						callSkill(SkillTable.getInstance().getInfo(buff[0], 1), target, true);
					}
				}
			}
		}
		else if (command.equals("CPRecovery"))
		{
			if (player.isCursedWeaponEquipped() || player.isInZone(Zone.ZoneType.battle_zone))
			{
				return;
			}
			int neededmoney = 1000;
			long currentmoney = player.getAdena();
			if (neededmoney > currentmoney)
			{
				player.sendPacket(SystemMsg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
				return;
			}
			player.reduceAdena(neededmoney, true);
			player.setCurrentCp(player.getMaxCp());
			player.sendPacket(new SystemMessage2(SystemMsg.S1_CP_HAS_BEEN_RESTORED).addName(player));
		}
		else if (command.equals("HPRecovery"))
		{
			if (player.isCursedWeaponEquipped() || player.isInZone(Zone.ZoneType.battle_zone))
			{
				return;
			}
			int neededmoney = 1000;
			long currentmoney = player.getAdena();
			if (neededmoney > currentmoney)
			{
				player.sendPacket(SystemMsg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
				return;
			}
			player.reduceAdena(neededmoney, true);
			player.setCurrentHp(player.getMaxHp(), false);
			player.sendPacket(new SystemMessage2(SystemMsg.S1_HP_HAS_BEEN_RESTORED).addName(player));
		}
		else
		{
			super.onBypassFeedback(player, command);
		}
	}
}
