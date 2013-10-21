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
import lineage2.gameserver.ai.Fighter;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class EvasGiftBox extends Fighter
{
	/**
	 * Field KISS_OF_EVA.
	 */
	private static final int[] KISS_OF_EVA = new int[]
	{
		1073,
		3141,
		3252
	};
	/**
	 * Field Red_Coral. (value is 9692)
	 */
	private static final int Red_Coral = 9692;
	/**
	 * Field Crystal_Fragment. (value is 9693)
	 */
	private static final int Crystal_Fragment = 9693;
	
	/**
	 * Constructor for EvasGiftBox.
	 * @param actor NpcInstance
	 */
	public EvasGiftBox(NpcInstance actor)
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
		if (killer != null)
		{
			final Player player = killer.getPlayer();
			if ((player != null) && player.getEffectList().containEffectFromSkills(KISS_OF_EVA))
			{
				actor.dropItem(player, Rnd.chance(50) ? Red_Coral : Crystal_Fragment, 1);
			}
		}
		super.onEvtDead(killer);
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
