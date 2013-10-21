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
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.scripts.Functions;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Thomas extends Fighter
{
	/**
	 * Field _lastSay.
	 */
	private long _lastSay;
	/**
	 * Field _stay.
	 */
	private static final String[] _stay =
	{
		"Ха...Ха... Вы при�?ли �?па�?ти �?неговика?",
		"Так про�?то �? вам его не отдам!",
		"Чтобы �?па�?ти ва�?его �?неговика, вам придет�?�? убит�? мен�?!",
		"Ха...Ха... Вы думаете �?то так про�?то?"
	};
	
	/**
	 * Field _attacked.
	 */
	private static final String[] _attacked =
	{
		"Вы должны в�?е умерет�?!",
		"Снеговик мой и не будет у ва�? �?ового Года!",
		"Я ва�? в�?ех уб�?�?!",
		"Что так �?лабо б�?ете? �?��ло ка�?и ели? Ха... Ха...",
		"�? �?то называет�?�? герои?",
		"�?е видат�? вам �?неговика!",
		"Тол�?ко древнее оружие �?по�?обно победит�? мен�?!"
	};
	
	/**
	 * Constructor for Thomas.
	 * @param actor NpcInstance
	 */
	public Thomas(NpcInstance actor)
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
		if (!actor.isInCombat() && ((System.currentTimeMillis() - _lastSay) > 10000))
		{
			Functions.npcSay(actor, _stay[Rnd.get(_stay.length)]);
			_lastSay = System.currentTimeMillis();
		}
		return super.thinkActive();
	}
	
	/**
	 * Method onEvtAttacked.
	 * @param attacker Creature
	 * @param damage int
	 */
	@Override
	protected void onEvtAttacked(Creature attacker, int damage)
	{
		final NpcInstance actor = getActor();
		if ((attacker == null) || (attacker.getPlayer() == null))
		{
			return;
		}
		if ((System.currentTimeMillis() - _lastSay) > 5000)
		{
			Functions.npcSay(actor, _attacked[Rnd.get(_attacked.length)]);
			_lastSay = System.currentTimeMillis();
		}
		super.onEvtAttacked(attacker, damage);
	}
}
