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
package npc.model.residences.castle;

import java.util.HashSet;
import java.util.Set;

import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Spawner;
import lineage2.gameserver.model.instances.residences.SiegeToggleNpcInstance;
import lineage2.gameserver.templates.npc.NpcTemplate;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class CastleControlTowerInstance extends SiegeToggleNpcInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field _spawnList.
	 */
	private final Set<Spawner> _spawnList = new HashSet<>();
	
	/**
	 * Constructor for CastleControlTowerInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public CastleControlTowerInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	/**
	 * Method onDeathImpl.
	 * @param killer Creature
	 */
	@Override
	public void onDeathImpl(Creature killer)
	{
		for (Spawner spawn : _spawnList)
		{
			spawn.stopRespawn();
		}
		_spawnList.clear();
	}
	
	/**
	 * Method register.
	 * @param spawn Spawner
	 */
	@Override
	public void register(Spawner spawn)
	{
		_spawnList.add(spawn);
	}
}
