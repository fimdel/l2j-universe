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

import lineage2.gameserver.ai.DefaultAI;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.Earthquake;
import lineage2.gameserver.network.serverpackets.L2GameServerPacket;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class BaiumNpc extends DefaultAI
{
	/**
	 * Field _wait_timeout.
	 */
	private long _wait_timeout = 0;
	/**
	 * Field BAIUM_EARTHQUAKE_TIMEOUT.
	 */
	private static final int BAIUM_EARTHQUAKE_TIMEOUT = 1000 * 60 * 15;
	
	/**
	 * Constructor for BaiumNpc.
	 * @param actor NpcInstance
	 */
	public BaiumNpc(NpcInstance actor)
	{
		super(actor);
	}
	
	/**
	 * Method isGlobalAI.
	 * @return boolean
	 */
	@Override
	public boolean isGlobalAI()
	{
		return true;
	}
	
	/**
	 * Method thinkActive.
	 * @return boolean
	 */
	@Override
	protected boolean thinkActive()
	{
		final NpcInstance actor = getActor();
		if (_wait_timeout < System.currentTimeMillis())
		{
			_wait_timeout = System.currentTimeMillis() + BAIUM_EARTHQUAKE_TIMEOUT;
			final L2GameServerPacket eq = new Earthquake(actor.getLoc(), 40, 10);
			final List<Creature> chars = actor.getAroundCharacters(5000, 10000);
			for (Creature character : chars)
			{
				if (character.isPlayer())
				{
					character.sendPacket(eq);
				}
			}
		}
		return false;
	}
	
	/**
	 * Method randomWalk.
	 * @return boolean
	 */
	@Override
	protected boolean randomWalk()
	{
		return false;
	}
}
