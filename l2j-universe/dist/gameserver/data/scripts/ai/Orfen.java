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

import lineage2.commons.text.PrintfFormat;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.ai.Fighter;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.utils.Location;
import npc.model.OrfenInstance;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Orfen extends Fighter
{
	/**
	 * Field MsgOnRecall.
	 */
	public static final PrintfFormat[] MsgOnRecall =
	{
		new PrintfFormat("%s. Stop kidding yourself about your own powerlessness!"),
		new PrintfFormat("%s. I'll make you feel what true fear is!"),
		new PrintfFormat("You're really stupid to have challenged me. %s! Get ready!"),
		new PrintfFormat("%s. Do you think that's going to work?!")
	};
	/**
	 * Field _paralyze.
	 */
	public final Skill[] _paralyze;
	
	/**
	 * Constructor for Orfen.
	 * @param actor NpcInstance
	 */
	public Orfen(NpcInstance actor)
	{
		super(actor);
		_paralyze = getActor().getTemplate().getDebuffSkills();
	}
	
	/**
	 * Method thinkActive.
	 * @return boolean
	 */
	@Override
	protected boolean thinkActive()
	{
		if (super.thinkActive())
		{
			return true;
		}
		final OrfenInstance actor = getActor();
		if (actor.isTeleported() && (actor.getCurrentHpPercents() > 95))
		{
			actor.setTeleported(false);
			return true;
		}
		return false;
	}
	
	/**
	 * Method createNewTask.
	 * @return boolean
	 */
	@Override
	protected boolean createNewTask()
	{
		return defaultNewTask();
	}
	
	/**
	 * Method onEvtAttacked.
	 * @param attacker Creature
	 * @param damage int
	 */
	@Override
	protected void onEvtAttacked(Creature attacker, int damage)
	{
		super.onEvtAttacked(attacker, damage);
		final OrfenInstance actor = getActor();
		if (actor.isCastingNow())
		{
			return;
		}
		final double distance = actor.getDistance(attacker);
		if ((distance > 300) && (distance < 1000) && (_damSkills.length > 0) && Rnd.chance(10))
		{
			Functions.npcSay(actor, MsgOnRecall[Rnd.get(MsgOnRecall.length - 1)].sprintf(attacker.getName()));
			teleToLocation(attacker, Location.findFrontPosition(actor, attacker, 0, 50));
			final Skill r_skill = _damSkills[Rnd.get(_damSkills.length)];
			if (canUseSkill(r_skill, attacker, -1))
			{
				addTaskAttack(attacker, r_skill, 1000000);
			}
		}
		else if ((_paralyze.length > 0) && Rnd.chance(20))
		{
			final Skill r_skill = _paralyze[Rnd.get(_paralyze.length)];
			if (canUseSkill(r_skill, attacker, -1))
			{
				addTaskAttack(attacker, r_skill, 1000000);
			}
		}
	}
	
	/**
	 * Method onEvtSeeSpell.
	 * @param skill Skill
	 * @param caster Creature
	 */
	@Override
	protected void onEvtSeeSpell(Skill skill, Creature caster)
	{
		super.onEvtSeeSpell(skill, caster);
		final OrfenInstance actor = getActor();
		if (actor.isCastingNow())
		{
			return;
		}
		final double distance = actor.getDistance(caster);
		if ((_damSkills.length > 0) && (skill.getEffectPoint() > 0) && (distance < 1000) && Rnd.chance(20))
		{
			Functions.npcSay(actor, MsgOnRecall[Rnd.get(MsgOnRecall.length)].sprintf(caster.getName()));
			teleToLocation(caster, Location.findFrontPosition(actor, caster, 0, 50));
			final Skill r_skill = _damSkills[Rnd.get(_damSkills.length)];
			if (canUseSkill(r_skill, caster, -1))
			{
				addTaskAttack(caster, r_skill, 1000000);
			}
		}
	}
	
	/**
	 * Method getActor.
	 * @return OrfenInstance
	 */
	@Override
	public OrfenInstance getActor()
	{
		return (OrfenInstance) super.getActor();
	}
	
	/**
	 * Method teleToLocation.
	 * @param attacker Creature
	 * @param loc Location
	 */
	private void teleToLocation(Creature attacker, Location loc)
	{
		attacker.teleToLocation(loc);
	}
}
