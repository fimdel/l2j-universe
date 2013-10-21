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
package ai.TI;

import java.util.ArrayList;
import java.util.List;

import lineage2.gameserver.ai.DefaultAI;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.World;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage;
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage.ScreenMessageAlign;
import lineage2.gameserver.network.serverpackets.MagicSkillUse;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.tables.SkillTable;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Newbie extends DefaultAI
{
	/**
	 * Field _mageBuff.
	 */
	private final static int[][] _mageBuff = new int[][]
	{
		{
			1,
			75,
			4322,
			1
		},
		{
			1,
			75,
			4323,
			1
		},
		{
			1,
			75,
			5637,
			1
		},
		{
			1,
			75,
			4328,
			1
		},
		{
			1,
			75,
			4329,
			1
		},
		{
			1,
			75,
			4330,
			1
		},
		{
			1,
			75,
			4331,
			1
		},
		{
			16,
			34,
			4338,
			1
		},
	};
	/**
	 * Field _warrBuff.
	 */
	private final static int[][] _warrBuff = new int[][]
	{
		{
			1,
			75,
			4322,
			1
		},
		{
			1,
			75,
			4323,
			1
		},
		{
			1,
			75,
			5637,
			1
		},
		{
			1,
			75,
			4324,
			1
		},
		{
			1,
			75,
			4325,
			1
		},
		{
			1,
			75,
			4326,
			1
		},
		{
			1,
			39,
			4327,
			1
		},
		{
			40,
			75,
			5632,
			1
		},
		{
			16,
			34,
			4338,
			1
		},
	};
	
	/**
	 * Constructor for Newbie.
	 * @param actor NpcInstance
	 */
	public Newbie(NpcInstance actor)
	{
		super(actor);
		AI_TASK_ACTIVE_DELAY = 2000;
	}
	
	/**
	 * Method thinkActive.
	 * @return boolean
	 */
	@Override
	protected boolean thinkActive()
	{
		if (!_def_think)
		{
			final NpcInstance npc = getActor();
			if (npc == null)
			{
				return true;
			}
			for (Player player : World.getAroundPlayers(npc, 200, 200))
			{
				if ((player.getLevel() <= 40) && (player.getEffectList().getEffectsBySkillId(4322) == null))
				{
					List<Creature> target = new ArrayList<>();
					target.add(player);
					if (!player.isMageClass())
					{
						for (int[] buff : _warrBuff)
						{
							npc.broadcastPacket(new MagicSkillUse(npc, player, buff[2], buff[3], 0, 0));
							npc.callSkill(SkillTable.getInstance().getInfo(buff[2], buff[3]), target, true);
						}
					}
					else
					{
						for (int[] buff : _mageBuff)
						{
							npc.broadcastPacket(new MagicSkillUse(npc, player, buff[2], buff[3], 0, 0));
							npc.callSkill(SkillTable.getInstance().getInfo(buff[2], buff[3]), target, true);
						}
					}
					player.sendPacket(new ExShowScreenMessage(NpcString.NEWBIE_HELPER_HAS_CASTED_BUFFS_ON_$S1, 800, ScreenMessageAlign.TOP_CENTER, true, String.valueOf(player.getName())));
				}
			}
		}
		return true;
	}
	
	/**
	 * Method isGlobalAI.
	 * @return boolean
	 */
	@Override
	public boolean isGlobalAI()
	{
		return true;
	}
}
