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
package lineage2.gameserver.stats.conditions;

import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.scripts.Scripts;
import lineage2.gameserver.stats.Env;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
@SuppressWarnings("unchecked")
public class ConditionTargetNpcClass extends Condition
{
	/**
	 * Field _npcClass.
	 */
	private final Class<NpcInstance> _npcClass;
	
	/**
	 * Constructor for ConditionTargetNpcClass.
	 * @param name String
	 */
	public ConditionTargetNpcClass(String name)
	{
		Class<NpcInstance> classType = null;
		try
		{
			classType = (Class<NpcInstance>) Class.forName("lineage2.gameserver.model.instances." + name + "Instance");
		}
		catch (ClassNotFoundException e)
		{
			classType = (Class<NpcInstance>) Scripts.getInstance().getClasses().get("npc.model." + name + "Instance");
		}
		if (classType == null)
		{
			throw new IllegalArgumentException("Not found type class for type: " + name + ".");
		}
		_npcClass = classType;
	}
	
	/**
	 * Method testImpl.
	 * @param env Env
	 * @return boolean
	 */
	@Override
	protected boolean testImpl(Env env)
	{
		return (env.target != null) && (env.target.getClass() == _npcClass);
	}
}
