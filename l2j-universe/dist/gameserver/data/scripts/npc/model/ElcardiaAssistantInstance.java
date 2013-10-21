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
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.tables.SkillTable;
import lineage2.gameserver.templates.npc.NpcTemplate;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class ElcardiaAssistantInstance extends NpcInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field _elcardiaBuff.
	 */
	private final static int[][] _elcardiaBuff = new int[][]
	{
		{
			6714,
			2
		},
		{
			6719,
			1
		},
		{
			6720,
			2
		},
		{
			6721,
			0
		},
		{
			6722,
			0
		},
		{
			6723,
			0
		},
	};
	
	/**
	 * Constructor for ElcardiaAssistantInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public ElcardiaAssistantInstance(int objectId, NpcTemplate template)
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
		if (command.equalsIgnoreCase("request_blessing"))
		{
			List<Creature> target = new ArrayList<>();
			target.add(player);
			for (int[] buff : _elcardiaBuff)
			{
				callSkill(SkillTable.getInstance().getInfo(buff[0], 1), target, true);
			}
		}
		else
		{
			super.onBypassFeedback(player, command);
		}
	}
}
