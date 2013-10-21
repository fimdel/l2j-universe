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
package ai;

import java.util.List;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.ai.Mystic;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Effect;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.tables.SkillTable;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class HotSpringsMob extends Mystic
{
	/**
	 * Field DeBuffs.
	 */
	private static final int[] DeBuffs =
	{
		4554,
		4552
	};
	
	/**
	 * Constructor for HotSpringsMob.
	 * @param actor NpcInstance
	 */
	public HotSpringsMob(NpcInstance actor)
	{
		super(actor);
	}
	
	/**
	 * Method onEvtAttacked.
	 * @param attacker Creature
	 * @param damage int
	 */
	@Override
	protected void onEvtAttacked(Creature attacker, int damage)
	{
		final NpcInstance actor = getActor();
		if ((attacker != null) && Rnd.chance(5))
		{
			final int DeBuff = DeBuffs[Rnd.get(DeBuffs.length)];
			final List<Effect> effect = attacker.getEffectList().getEffectsBySkillId(DeBuff);
			if (effect != null)
			{
				final int level = effect.get(0).getSkill().getLevel();
				if (level < 10)
				{
					effect.get(0).exit();
					final Skill skill = SkillTable.getInstance().getInfo(DeBuff, level + 1);
					skill.getEffects(actor, attacker, false, false);
				}
			}
			else
			{
				final Skill skill = SkillTable.getInstance().getInfo(DeBuff, 1);
				if (skill != null)
				{
					skill.getEffects(actor, attacker, false, false);
				}
				else
				{
					System.out.println("Skill " + DeBuff + " is null, fix it.");
				}
			}
		}
		super.onEvtAttacked(attacker, damage);
	}
}
