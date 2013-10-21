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

import lineage2.commons.util.Rnd;
import lineage2.gameserver.ai.DefaultAI;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.scripts.Functions;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class BlacksmithMammon extends DefaultAI
{
	/**
	 * Field _chatVar.
	 */
	private long _chatVar = 0;
	/**
	 * Field chatDelay.
	 */
	private static final long chatDelay = 30 * 60 * 1000L;
	/**
	 * Field mamonText.
	 */
	private static final NpcString[] mamonText =
	{
		NpcString.RULERS_OF_THE_SEAL_I_BRING_YOU_WONDROUS_GIFTS,
		NpcString.RULERS_OF_THE_SEAL_I_HAVE_SOME_EXCELLENT_WEAPONS_TO_SHOW_YOU,
		NpcString.IVE_BEEN_SO_BUSY_LATELY_IN_ADDITION_TO_PLANNING_MY_TRIP
	};
	
	/**
	 * Constructor for BlacksmithMammon.
	 * @param actor NpcInstance
	 */
	public BlacksmithMammon(NpcInstance actor)
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
		if (actor.isDead())
		{
			return true;
		}
		if ((_chatVar + chatDelay) < System.currentTimeMillis())
		{
			_chatVar = System.currentTimeMillis();
			Functions.npcShout(actor, mamonText[Rnd.get(mamonText.length)]);
		}
		return false;
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
}
