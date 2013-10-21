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

import lineage2.gameserver.model.Creature;
import lineage2.gameserver.templates.npc.NpcTemplate;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ReflectionBossInstance extends RaidBossInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field COLLAPSE_AFTER_DEATH_TIME. (value is 5)
	 */
	private final static int COLLAPSE_AFTER_DEATH_TIME = 5;
	
	/**
	 * Constructor for ReflectionBossInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public ReflectionBossInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	/**
	 * Method onDeath.
	 * @param killer Creature
	 */
	@Override
	protected void onDeath(Creature killer)
	{
		getMinionList().unspawnMinions();
		super.onDeath(killer);
		clearReflection();
	}
	
	/**
	 * Method clearReflection.
	 */
	protected void clearReflection()
	{
		getReflection().clearReflection(COLLAPSE_AFTER_DEATH_TIME, true);
	}
}
