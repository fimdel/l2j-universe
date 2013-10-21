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
package npc.model.residences.clanhall;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.instances.residences.clanhall.CTBBossInstance;
import lineage2.gameserver.templates.npc.NpcTemplate;
import ai.residences.clanhall.MatchCleric;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class MatchClericInstance extends CTBBossInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field _massiveDamage.
	 */
	private long _massiveDamage;
	
	/**
	 * Constructor for MatchClericInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public MatchClericInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	/**
	 * Method reduceCurrentHp.
	 * @param damage double
	 * @param reflectableDamage double
	 * @param attacker Creature
	 * @param skill Skill
	 * @param awake boolean
	 * @param standUp boolean
	 * @param directHp boolean
	 * @param canReflect boolean
	 * @param transferDamage boolean
	 * @param isDot boolean
	 * @param sendMessage boolean
	 */
	@Override
	public void reduceCurrentHp(double damage, double reflectableDamage, Creature attacker, Skill skill, boolean awake, boolean standUp, boolean directHp, boolean canReflect, boolean transferDamage, boolean isDot, boolean sendMessage)
	{
		if (_massiveDamage > System.currentTimeMillis())
		{
			damage = 10000;
			if (Rnd.chance(10))
			{
				((MatchCleric) getAI()).heal();
			}
		}
		else if (getCurrentHpPercents() > 50)
		{
			if (attacker.isPlayer())
			{
				damage = ((damage / getMaxHp()) / 0.05) * 100;
			}
			else
			{
				damage = ((damage / getMaxHp()) / 0.05) * 10;
			}
		}
		else if (getCurrentHpPercents() > 30)
		{
			if (Rnd.chance(90))
			{
				if (attacker.isPlayer())
				{
					damage = ((damage / getMaxHp()) / 0.05) * 100;
				}
				else
				{
					damage = ((damage / getMaxHp()) / 0.05) * 10;
				}
			}
			else
			{
				_massiveDamage = System.currentTimeMillis() + 5000L;
			}
		}
		else
		{
			_massiveDamage = System.currentTimeMillis() + 5000L;
		}
		super.reduceCurrentHp(damage, reflectableDamage, attacker, skill, awake, standUp, directHp, canReflect, transferDamage, isDot, sendMessage);
	}
}
