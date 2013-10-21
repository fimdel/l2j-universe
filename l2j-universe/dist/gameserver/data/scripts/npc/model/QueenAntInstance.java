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

import java.util.ArrayList;
import java.util.List;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.data.xml.holder.NpcHolder;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.SimpleSpawner;
import lineage2.gameserver.model.instances.BossInstance;
import lineage2.gameserver.model.instances.MinionInstance;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.PlaySound;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class QueenAntInstance extends BossInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field Queen_Ant_Larva. (value is 29002)
	 */
	private static final int Queen_Ant_Larva = 29002;
	/**
	 * Field _spawns.
	 */
	private final List<SimpleSpawner> _spawns = new ArrayList<>();
	/**
	 * Field Larva.
	 */
	private NpcInstance Larva = null;
	
	/**
	 * Constructor for QueenAntInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public QueenAntInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	/**
	 * Method getLarva.
	 * @return NpcInstance
	 */
	public NpcInstance getLarva()
	{
		if (Larva == null)
		{
			Larva = SpawnNPC(Queen_Ant_Larva, new Location(-21600, 179482, -5846, Rnd.get(0, 0xFFFF)));
		}
		return Larva;
	}
	
	/**
	 * Method getKilledInterval.
	 * @param minion MinionInstance
	 * @return int
	 */
	@Override
	protected int getKilledInterval(MinionInstance minion)
	{
		return minion.getNpcId() == 29003 ? 10000 : 280000 + Rnd.get(40000);
	}
	
	/**
	 * Method onDeath.
	 * @param killer Creature
	 */
	@Override
	protected void onDeath(Creature killer)
	{
		broadcastPacketToOthers(new PlaySound(PlaySound.Type.MUSIC, "BS02_D", 1, 0, getLoc()));
		Functions.deSpawnNPCs(_spawns);
		Larva = null;
		super.onDeath(killer);
	}
	
	/**
	 * Method onSpawn.
	 */
	@Override
	protected void onSpawn()
	{
		super.onSpawn();
		getLarva();
		broadcastPacketToOthers(new PlaySound(PlaySound.Type.MUSIC, "BS01_A", 1, 0, getLoc()));
	}
	
	/**
	 * Method SpawnNPC.
	 * @param npcId int
	 * @param loc Location
	 * @return NpcInstance
	 */
	private NpcInstance SpawnNPC(int npcId, Location loc)
	{
		NpcTemplate template = NpcHolder.getInstance().getTemplate(npcId);
		if (template == null)
		{
			System.out.println("WARNING! template is null for npc: " + npcId);
			Thread.dumpStack();
			return null;
		}
		try
		{
			SimpleSpawner sp = new SimpleSpawner(template);
			sp.setLoc(loc);
			sp.setAmount(1);
			sp.setRespawnDelay(0);
			_spawns.add(sp);
			return sp.spawnOne();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
}
