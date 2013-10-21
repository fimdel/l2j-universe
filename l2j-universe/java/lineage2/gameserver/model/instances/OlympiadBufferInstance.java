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
package lineage2.gameserver.model.instances;

import gnu.trove.set.hash.TIntHashSet;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.network.serverpackets.MagicSkillUse;
import lineage2.gameserver.tables.SkillTable;
import lineage2.gameserver.templates.npc.NpcTemplate;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class OlympiadBufferInstance extends NpcInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field buffs.
	 */
	private final TIntHashSet buffs = new TIntHashSet();
	
	/**
	 * Constructor for OlympiadBufferInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public OlympiadBufferInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	@Override
	public void onInteract(final Player player)
	{
		if(buffs.size() > 4)
		{
			showChatWindow(player, 1);
		}
		else
		{
			showChatWindow(player, 0);
		}
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
		if (buffs.size() > 4)
		{
			showChatWindow(player, 1);
		}
		if (command.startsWith("Buff"))
		{
			int id = 0;
			int lvl = 0;
			StringTokenizer st = new StringTokenizer(command, " ");
			st.nextToken();
			id = Integer.parseInt(st.nextToken());
			lvl = Integer.parseInt(st.nextToken());
			Skill skill = SkillTable.getInstance().getInfo(id, lvl);
			List<Creature> target = new ArrayList<>();
			target.add(player);
			broadcastPacket(new MagicSkillUse(this, player, id, lvl, 0, 0));
			callSkill(skill, target, true);
			buffs.add(id);
			if (buffs.size() > 4)
			{
				showChatWindow(player, 1);
			}
			else
			{
				showChatWindow(player, 0);
			}
		}
		else
		{
			showChatWindow(player, 0);
		}
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
			pom = "buffer";
		}
		else
		{
			pom = "buffer-" + val;
		}
		return "olympiad/" + pom + ".htm";
	}
}
