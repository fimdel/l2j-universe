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
import lineage2.gameserver.model.instances.MonsterInstance;
import lineage2.gameserver.templates.npc.NpcTemplate;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class PassagewayMobWithHerbInstance extends MonsterInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor for PassagewayMobWithHerbInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public PassagewayMobWithHerbInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	/**
	 * Field FieryDemonBloodHerb. (value is 9849)
	 */
	public static final int FieryDemonBloodHerb = 9849;
	
	/**
	 * Method calculateRewards.
	 * @param lastAttacker Creature
	 */
	@Override
	public void calculateRewards(Creature lastAttacker)
	{
		if (lastAttacker == null)
		{
			return;
		}
		super.calculateRewards(lastAttacker);
		if (lastAttacker.isPlayable())
		{
			dropItem(lastAttacker.getPlayer(), FieryDemonBloodHerb, 1);
		}
	}
}
