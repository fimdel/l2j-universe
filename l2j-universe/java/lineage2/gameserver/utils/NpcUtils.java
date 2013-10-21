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
package lineage2.gameserver.utils;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.data.xml.holder.NpcHolder;
import lineage2.gameserver.instancemanager.ReflectionManager;
import lineage2.gameserver.model.GameObjectTasks;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.templates.npc.NpcTemplate;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class NpcUtils
{
	/**
	 * Method spawnSingle.
	 * @param npcId int
	 * @param x int
	 * @param y int
	 * @param z int
	 * @return NpcInstance
	 */
	public static NpcInstance spawnSingle(int npcId, int x, int y, int z)
	{
		return spawnSingle(npcId, new Location(x, y, z, -1), ReflectionManager.DEFAULT, 0);
	}
	
	/**
	 * Method spawnSingle.
	 * @param npcId int
	 * @param x int
	 * @param y int
	 * @param z int
	 * @param despawnTime long
	 * @return NpcInstance
	 */
	public static NpcInstance spawnSingle(int npcId, int x, int y, int z, long despawnTime)
	{
		return spawnSingle(npcId, new Location(x, y, z, -1), ReflectionManager.DEFAULT, despawnTime);
	}
	
	/**
	 * Method spawnSingle.
	 * @param npcId int
	 * @param x int
	 * @param y int
	 * @param z int
	 * @param h int
	 * @param despawnTime long
	 * @return NpcInstance
	 */
	public static NpcInstance spawnSingle(int npcId, int x, int y, int z, int h, long despawnTime)
	{
		return spawnSingle(npcId, new Location(x, y, z, h), ReflectionManager.DEFAULT, despawnTime);
	}
	
	/**
	 * Method spawnSingle.
	 * @param npcId int
	 * @param loc Location
	 * @return NpcInstance
	 */
	public static NpcInstance spawnSingle(int npcId, Location loc)
	{
		return spawnSingle(npcId, loc, ReflectionManager.DEFAULT, 0);
	}
	
	/**
	 * Method spawnSingle.
	 * @param npcId int
	 * @param loc Location
	 * @param despawnTime long
	 * @return NpcInstance
	 */
	public static NpcInstance spawnSingle(int npcId, Location loc, long despawnTime)
	{
		return spawnSingle(npcId, loc, ReflectionManager.DEFAULT, despawnTime);
	}
	
	/**
	 * Method spawnSingle.
	 * @param npcId int
	 * @param loc Location
	 * @param reflection Reflection
	 * @return NpcInstance
	 */
	public static NpcInstance spawnSingle(int npcId, Location loc, Reflection reflection)
	{
		return spawnSingle(npcId, loc, reflection, 0);
	}
	
	/**
	 * Method spawnSingle.
	 * @param npcId int
	 * @param loc Location
	 * @param reflection Reflection
	 * @param despawnTime long
	 * @return NpcInstance
	 */
	public static NpcInstance spawnSingle(int npcId, Location loc, Reflection reflection, long despawnTime)
	{
		NpcTemplate template = NpcHolder.getInstance().getTemplate(npcId);
		if (template == null)
		{
			throw new NullPointerException("Npc template id : " + npcId + " not found!");
		}
		NpcInstance npc = template.getNewInstance();
		npc.setHeading(loc.h < 0 ? Rnd.get(0xFFFF) : loc.h);
		npc.setSpawnedLoc(loc);
		npc.setReflection(reflection);
		npc.setCurrentHpMp(npc.getMaxHp(), npc.getMaxMp(), true);
		npc.spawnMe(npc.getSpawnedLoc());
		if (despawnTime > 0)
		{
			ThreadPoolManager.getInstance().schedule(new GameObjectTasks.DeleteTask(npc), despawnTime);
		}
		return npc;
	}
}
