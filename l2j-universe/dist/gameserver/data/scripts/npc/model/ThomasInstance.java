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

import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.instances.MonsterInstance;
import lineage2.gameserver.templates.npc.NpcTemplate;
import events.SavingSnowman.SavingSnowman;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ThomasInstance extends MonsterInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor for ThomasInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public ThomasInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	/**
	 * Method reduceCurrentHp.
	 * @param i double
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
	public void reduceCurrentHp(double i, double reflectableDamage, Creature attacker, Skill skill, boolean awake, boolean standUp, boolean directHp, boolean canReflect, boolean transferDamage, boolean isDot, boolean sendMessage)
	{
		i = 10;
		if (attacker.getActiveWeaponInstance() != null)
		{
			switch (attacker.getActiveWeaponInstance().getItemId())
			{
				case 4202:
				case 5133:
				case 5817:
				case 7058:
				case 8350:
					i = 100;
					break;
				default:
					i = 10;
			}
		}
		super.reduceCurrentHp(i, reflectableDamage, attacker, skill, awake, standUp, directHp, canReflect, transferDamage, isDot, sendMessage);
	}
	
	/**
	 * Method onDeath.
	 * @param killer Creature
	 */
	@Override
	protected void onDeath(Creature killer)
	{
		Creature topdam = getAggroList().getTopDamager();
		if (topdam == null)
		{
			topdam = killer;
		}
		SavingSnowman.freeSnowman(topdam);
		super.onDeath(killer);
	}
	
	/**
	 * Method canChampion.
	 * @return boolean
	 */
	@Override
	public boolean canChampion()
	{
		return false;
	}
}
