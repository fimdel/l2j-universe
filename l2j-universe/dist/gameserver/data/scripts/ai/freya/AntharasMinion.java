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

import lineage2.gameserver.ai.CtrlEvent;
import lineage2.gameserver.ai.Fighter;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.tables.SkillTable;
import bosses.AntharasManager;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class AntharasMinion extends Fighter
{
	/**
	 * Constructor for AntharasMinion.
	 * @param actor NpcInstance
	 */
	public AntharasMinion(NpcInstance actor)
	{
		super(actor);
		actor.startDebuffImmunity();
	}
	
	/**
	 * Method onEvtSpawn.
	 */
	@Override
	protected void onEvtSpawn()
	{
		super.onEvtSpawn();
		for (Player p : AntharasManager.getZone().getInsidePlayers())
		{
			notifyEvent(CtrlEvent.EVT_AGGRESSION, p, 5000);
		}
	}
	
	/**
	 * Method onEvtDead.
	 * @param killer Creature
	 */
	@Override
	protected void onEvtDead(Creature killer)
	{
		getActor().doCast(SkillTable.getInstance().getInfo(5097, 1), getActor(), true);
		super.onEvtDead(killer);
	}
	
	/**
	 * Method returnHome.
	 * @param clearAggro boolean
	 * @param teleport boolean
	 */
	@Override
	protected void returnHome(boolean clearAggro, boolean teleport)
	{
		return;
	}
}
