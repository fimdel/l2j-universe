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
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.World;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.MagicSkillUse;
import lineage2.gameserver.tables.SkillTable;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class SwampSpirit extends DefaultAI
{
	private static final Skill healSkill = SkillTable.getInstance().getInfo(14064, 1);
	private static final Skill healSkillBig = SkillTable.getInstance().getInfo(14065, 1);
	private static final Skill blindlightSkill = SkillTable.getInstance().getInfo(14066, 1);
	
	
	
	/**
	 * Constructor for SwampSpirit.
	 * @param actor NpcInstance
	 */
	public SwampSpirit(NpcInstance actor)
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
				switch (npc.getNpcId())
				{
					case 32915:
						if (player.getEffectList().getEffectsBySkillId(healSkill.getId()) == null)
						{
							List<Creature> target = new ArrayList<>();
							target.add(player);
							npc.broadcastPacket(new MagicSkillUse(npc, player, healSkill.getId(), healSkill.getLevel(), 0, 0));
							npc.callSkill(SkillTable.getInstance().getInfo(healSkill.getId(), healSkill.getLevel()), target, true);
						}
						break;
					case 32916:
						if (player.getEffectList().getEffectsBySkillId(healSkillBig.getId()) == null)
						{
							List<Creature> target = new ArrayList<>();
							target.add(player);
							npc.broadcastPacket(new MagicSkillUse(npc, player, healSkillBig.getId(), healSkillBig.getLevel(), 0, 0));
							npc.callSkill(SkillTable.getInstance().getInfo(healSkillBig.getId(), healSkillBig.getLevel()), target, true);
						}
						break;
					case 32938:
						List<Creature> target = new ArrayList<>();
						target.add(npc);
						npc.broadcastPacket(new MagicSkillUse(npc, player, blindlightSkill.getId(), blindlightSkill.getLevel(), 0, 0));
						npc.callSkill(SkillTable.getInstance().getInfo(blindlightSkill.getId(), blindlightSkill.getLevel()), target, true);
						break;
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
