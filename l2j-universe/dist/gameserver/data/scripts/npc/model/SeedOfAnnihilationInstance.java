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

import lineage2.commons.util.Rnd;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.instances.MonsterInstance;
import lineage2.gameserver.templates.npc.MinionData;
import lineage2.gameserver.templates.npc.NpcTemplate;

import org.apache.commons.lang3.ArrayUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class SeedOfAnnihilationInstance extends MonsterInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field BISTAKON_MOBS.
	 */
	private static final int[] BISTAKON_MOBS = new int[]
	{
		22750,
		22751,
		22752,
		22753
	};
	/**
	 * Field COKRAKON_MOBS.
	 */
	private static final int[] COKRAKON_MOBS = new int[]
	{
		22763,
		22764,
		22765
	};
	/**
	 * Field BISTAKON_MINIONS.
	 */
	private static final int[][] BISTAKON_MINIONS = new int[][]
	{
		{
			22746,
			22746,
			22746
		},
		{
			22747,
			22747,
			22747
		},
		{
			22748,
			22748,
			22748
		},
		{
			22749,
			22749,
			22749
		}
	};
	/**
	 * Field COKRAKON_MINIONS.
	 */
	private static final int[][] COKRAKON_MINIONS = new int[][]
	{
		{
			22760,
			22760,
			22761
		},
		{
			22760,
			22760,
			22762
		},
		{
			22761,
			22761,
			22760
		},
		{
			22761,
			22761,
			22762
		},
		{
			22762,
			22762,
			22760
		},
		{
			22762,
			22762,
			22761
		}
	};
	
	/**
	 * Constructor for SeedOfAnnihilationInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public SeedOfAnnihilationInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
		if (ArrayUtils.contains(BISTAKON_MOBS, template.getNpcId()))
		{
			addMinions(BISTAKON_MINIONS[Rnd.get(BISTAKON_MINIONS.length)], template);
		}
		else if (ArrayUtils.contains(COKRAKON_MOBS, template.getNpcId()))
		{
			addMinions(COKRAKON_MINIONS[Rnd.get(COKRAKON_MINIONS.length)], template);
		}
	}
	
	/**
	 * Method addMinions.
	 * @param minions int[]
	 * @param template NpcTemplate
	 */
	private static void addMinions(int[] minions, NpcTemplate template)
	{
		if ((minions != null) && (minions.length > 0))
		{
			for (int id : minions)
			{
				template.addMinion(new MinionData(id, 1));
			}
		}
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
