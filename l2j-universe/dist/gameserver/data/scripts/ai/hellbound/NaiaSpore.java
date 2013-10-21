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
package ai.hellbound;

import java.util.HashMap;
import java.util.Map;

import lineage2.gameserver.ai.Fighter;
import lineage2.gameserver.instancemanager.naia.NaiaCoreManager;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.instances.NpcInstance;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class NaiaSpore extends Fighter
{
	/**
	 * Field epidosIndex.
	 */
	private static final Map<Integer, Integer> epidosIndex = new HashMap<>();
	static
	{
		epidosIndex.put(1, 0);
		epidosIndex.put(2, 0);
		epidosIndex.put(3, 0);
		epidosIndex.put(4, 0);
	}
	
	/**
	 * Constructor for NaiaSpore.
	 * @param actor NpcInstance
	 */
	public NaiaSpore(NpcInstance actor)
	{
		super(actor);
	}
	
	/**
	 * Method onEvtDead.
	 * @param killer Creature
	 */
	@Override
	protected void onEvtDead(Creature killer)
	{
		final NpcInstance actor = getActor();
		final int npcId = actor.getNpcId();
		switch (npcId)
		{
			case 25605:
			{
				epidosIndex.put(1, epidosIndex.get(1) + 1);
				break;
			}
			case 25606:
			{
				epidosIndex.put(2, epidosIndex.get(2) + 1);
				break;
			}
			case 25607:
			{
				epidosIndex.put(3, epidosIndex.get(3) + 1);
				break;
			}
			case 25608:
			{
				epidosIndex.put(4, epidosIndex.get(4) + 1);
				break;
			}
			default:
				break;
		}
		if ((isBossSpawnCondMet() != 0) && !NaiaCoreManager.isBossSpawned())
		{
			NaiaCoreManager.spawnEpidos(isBossSpawnCondMet());
		}
		super.onEvtDead(killer);
	}
	
	/**
	 * Method isBossSpawnCondMet.
	 * @return int
	 */
	private int isBossSpawnCondMet()
	{
		for (int i = 1; i < 5; i++)
		{
			if (epidosIndex.get(i) >= 100)
			{
				return i;
			}
		}
		return 0;
	}
}
