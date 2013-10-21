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
package ai.custom;

import lineage2.gameserver.ai.CtrlIntention;
import lineage2.gameserver.ai.Fighter;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.utils.Location;
import lineage2.gameserver.utils.NpcUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class GuardPustosh extends Fighter
{
	/**
	 * Field KANILOV. (value is 27459)
	 */
	private final static int KANILOV = 27459;
	/**
	 * Field POSLOF. (value is 27460)
	 */
	private final static int POSLOF = 27460;
	/**
	 * Field SAKUM. (value is 27453)
	 */
	private final static int SAKUM = 27453;
	/**
	 * Field SCHUAZEN. (value is 33517)
	 */
	private final static int SCHUAZEN = 33517;
	/**
	 * Field SELON. (value is 33518)
	 */
	private final static int SELON = 33518;
	/**
	 * Field POSLOF_OFFICER. (value is 19163)
	 */
	private final static int POSLOF_OFFICER = 19163;
	/**
	 * Field COMMANDO. (value is 19126)
	 */
	private final static int COMMANDO = 19126;
	/**
	 * Field COMMANDO_CAPTAIN. (value is 19127)
	 */
	private final static int COMMANDO_CAPTAIN = 19127;
	/**
	 * Field COMMANDO_SPAWNS.
	 */
	private final static int[][] COMMANDO_SPAWNS =
	{
		{
			-36405,
			191635,
			-3632,
			63583
		},
		{
			-36408,
			191784,
			-3665,
			63583
		},
		{
			-36312,
			191720,
			-3665,
			63583
		},
		{
			-36264,
			191656,
			-3665,
			63583
		},
	};
	
	/**
	 * Constructor for GuardPustosh.
	 * @param actor NpcInstance
	 */
	public GuardPustosh(NpcInstance actor)
	{
		super(actor);
	}
	
	/**
	 * Method thinkActive.
	 * @return boolean
	 */
	@Override
	protected boolean thinkActive()
	{
		final NpcInstance actor = getActor();
		final int actorId = actor.getNpcId();
		actor.setRunning();
		if (actorId != SELON)
		{
			boolean monsterFound = false;
			NpcInstance poslofAttacker = null;
			for (NpcInstance npc : actor.getAroundNpc(1000, 1000))
			{
				int npcId = npc.getNpcId();
				if ((npcId == KANILOV) || (npcId == POSLOF) || (npcId == SAKUM))
				{
					monsterFound = true;
					actor.getAggroList().addDamageHate(npc, 0, 1000);
					setIntention(CtrlIntention.AI_INTENTION_ATTACK);
					if (actorId == SCHUAZEN)
					{
						NpcUtils.spawnSingle(POSLOF_OFFICER, -29533, 187059, -3912, 45362);
					}
				}
				else if (npcId == POSLOF_OFFICER)
				{
					poslofAttacker = npc;
				}
			}
			if (!monsterFound && (poslofAttacker != null))
			{
				poslofAttacker.deleteMe();
			}
		}
		return true;
	}
	
	/**
	 * Method onEvtDead.
	 * @param killer Creature
	 */
	@Override
	protected void onEvtDead(Creature killer)
	{
		final NpcInstance actor = getActor();
		final int currentNpcId = actor.getNpcId();
		final int respawnNpcId = (currentNpcId == COMMANDO) ? COMMANDO_CAPTAIN : COMMANDO;
		boolean needRespawn = true;
		for (NpcInstance npc : actor.getAroundNpc(1000, 1000))
		{
			if ((npc.getNpcId() == currentNpcId) && !npc.isDead())
			{
				needRespawn = false;
			}
		}
		if (needRespawn)
		{
			for (int[] element : COMMANDO_SPAWNS)
			{
				NpcUtils.spawnSingle(respawnNpcId, new Location(element[0], element[1], element[2]));
			}
		}
		super.onEvtDead(killer);
	}
}
