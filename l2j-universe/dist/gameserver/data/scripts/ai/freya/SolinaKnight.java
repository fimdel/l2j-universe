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
package ai.freya;

import java.util.List;

import lineage2.gameserver.ai.CtrlEvent;
import lineage2.gameserver.ai.Fighter;
import lineage2.gameserver.model.instances.NpcInstance;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class SolinaKnight extends Fighter
{
	/**
	 * Field scarecrow.
	 */
	private NpcInstance scarecrow = null;
	
	/**
	 * Constructor for SolinaKnight.
	 * @param actor NpcInstance
	 */
	public SolinaKnight(NpcInstance actor)
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
		if (scarecrow == null)
		{
			final List<NpcInstance> around = getActor().getAroundNpc(300, 100);
			if ((around != null) && !around.isEmpty())
			{
				for (NpcInstance npc : around)
				{
					if (npc.getNpcId() == 18912)
					{
						if ((scarecrow == null) || (getActor().getDistance3D(npc) < getActor().getDistance3D(scarecrow)))
						{
							scarecrow = npc;
						}
					}
				}
			}
		}
		if (scarecrow != null)
		{
			getActor().getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, scarecrow, 1);
			return true;
		}
		return false;
	}
}
