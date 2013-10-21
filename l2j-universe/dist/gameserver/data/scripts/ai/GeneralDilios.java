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
package ai;

import java.util.List;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.ai.DefaultAI;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.SocialAction;
import lineage2.gameserver.scripts.Functions;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class GeneralDilios extends DefaultAI
{
	/**
	 * Field GUARD_ID. (value is 32619)
	 */
	private static final int GUARD_ID = 32619;
	/**
	 * Field _wait_timeout.
	 */
	private long _wait_timeout = 0;
	/**
	 * Field diliosText.
	 */
	private static final String[] diliosText =
	{
		"Messenger, inform the patrons of the Keucereus Alliance Base! We're gathering brave adventurers to attack Tiat's Mounted Troop that's rooted in the Seed of Destruction.",
		"Messenger, inform the brothers in Keucereus's clan outpost! Brave adventurers are currently eradicating Undead that are widespread in Seed of Immortality's Hall of Suffering and Hall of Erosion!",
		"Stabbing three times!"
	};
	
	/**
	 * Constructor for GeneralDilios.
	 * @param actor NpcInstance
	 */
	public GeneralDilios(NpcInstance actor)
	{
		super(actor);
		AI_TASK_ATTACK_DELAY = 10000;
	}
	
	/**
	 * Method thinkActive.
	 * @return boolean
	 */
	@Override
	public boolean thinkActive()
	{
		final NpcInstance actor = getActor();
		if (System.currentTimeMillis() > _wait_timeout)
		{
			_wait_timeout = System.currentTimeMillis() + 60000;
			final int j = Rnd.get(1, 3);
			switch (j)
			{
				case 1:
					Functions.npcSay(actor, diliosText[0]);
					break;
				case 2:
					Functions.npcSay(actor, diliosText[1]);
					break;
				case 3:
					Functions.npcSay(actor, diliosText[2]);
					final List<NpcInstance> around = actor.getAroundNpc(1500, 100);
					if ((around != null) && !around.isEmpty())
					{
						for (NpcInstance guard : around)
						{
							if (!guard.isMonster() && (guard.getNpcId() == GUARD_ID))
							{
								guard.broadcastPacket(new SocialAction(guard.getObjectId(), 4));
							}
						}
					}
			}
		}
		return false;
	}
}
