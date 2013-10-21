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

import lineage2.gameserver.ai.Fighter;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.tables.SkillTable;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Gargos extends Fighter
{
	/**
	 * Field _lastFire.
	 */
	private long _lastFire;
	
	/**
	 * Constructor for Gargos.
	 * @param actor NpcInstance
	 */
	public Gargos(NpcInstance actor)
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
		return super.thinkActive() || thinkFire();
	}
	
	/**
	 * Method thinkFire.
	 * @return boolean
	 */
	protected boolean thinkFire()
	{
		if ((System.currentTimeMillis() - _lastFire) > 60000L)
		{
			final NpcInstance actor = getActor();
			Functions.npcSayCustomMessage(actor, "scripts.ai.Gargos.fire");
			actor.doCast(SkillTable.getInstance().getInfo(5705, 1), actor, false);
			_lastFire = System.currentTimeMillis();
			return true;
		}
		return false;
	}
}
