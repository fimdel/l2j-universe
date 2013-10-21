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
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.scripts.Functions;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class GuardianAngel extends DefaultAI
{
	/**
	 * Field flood.
	 */
	static final String[] flood =
	{
		"Waaaah! Step back from the confounded box! I will take it myself!",
		"Grr! Who are you and why have you stopped me?",
		"Grr. I've been hit..."
	};
	
	/**
	 * Constructor for GuardianAngel.
	 * @param actor NpcInstance
	 */
	public GuardianAngel(NpcInstance actor)
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
		Functions.npcSay(actor, flood[Rnd.get(2)]);
		return super.thinkActive();
	}
	
	/**
	 * Method onEvtDead.
	 * @param killer Creature
	 */
	@Override
	protected void onEvtDead(Creature killer)
	{
		final NpcInstance actor = getActor();
		if (actor != null)
		{
			Functions.npcSay(actor, flood[2]);
		}
		super.onEvtDead(killer);
	}
}
