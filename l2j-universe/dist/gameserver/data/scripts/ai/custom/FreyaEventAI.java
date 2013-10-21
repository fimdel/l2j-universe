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
package ai.custom;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.ai.DefaultAI;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.GameObject;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.scripts.Functions;

import org.apache.commons.lang3.ArrayUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class FreyaEventAI extends DefaultAI
{
	/**
	 * Field GIFT_SKILLS.
	 */
	private static final int[] GIFT_SKILLS =
	{
		9150,
		9151,
		9152,
		9153,
		9154,
		9155,
		9156
	};
	/**
	 * Field GIFT_CHANCE. (value is 5)
	 */
	private static final int GIFT_CHANCE = 5;
	/**
	 * Field FREYA_GIFT. (value is 17138)
	 */
	private static final int FREYA_GIFT = 17138;
	/**
	 * Field SAY_TEXT.
	 */
	private static final NpcString[] SAY_TEXT = new NpcString[]
	{
		NpcString.DEAR_S1,
		NpcString.BUT_I_KIND_OF_MISS_IT,
		NpcString.I_JUST_DONT_KNOW_WHAT_EXPRESSION_I_SHOULD_HAVE_IT_APPEARED_ON_ME,
		NpcString.EVEN_THOUGH_YOU_BRING_SOMETHING_CALLED_A_GIFT_AMONG_YOUR_HUMANS_IT_WOULD_JUST_BE_PROBLEMATIC_FOR_ME,
		NpcString.THE_FEELING_OF_THANKS_IS_JUST_TOO_MUCH_DISTANT_MEMORY_FOR_ME,
		NpcString.I_AM_ICE_QUEEN_FREYA
	};
	
	/**
	 * Constructor for FreyaEventAI.
	 * @param actor NpcInstance
	 */
	public FreyaEventAI(NpcInstance actor)
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
	 * Method randomWalk.
	 * @return boolean
	 */
	@Override
	protected boolean randomWalk()
	{
		return false;
	}
	
	/**
	 * Method onEvtSeeSpell.
	 * @param skill Skill
	 * @param caster Creature
	 */
	@Override
	protected void onEvtSeeSpell(Skill skill, Creature caster)
	{
		final NpcInstance actor = getActor();
		if ((caster == null) || !caster.isPlayer())
		{
			return;
		}
		final GameObject casterTarget = caster.getTarget();
		if ((casterTarget == null) || (casterTarget.getObjectId() != actor.getObjectId()))
		{
			return;
		}
		final Player player = caster.getPlayer();
		if (ArrayUtils.contains(GIFT_SKILLS, skill.getId()))
		{
			if (Rnd.chance(GIFT_CHANCE))
			{
				Functions.npcSay(actor, SAY_TEXT[0], player.getName());
				Functions.addItem(player, FREYA_GIFT, 1);
			}
			else if (Rnd.chance(70))
			{
				Functions.npcSay(actor, SAY_TEXT[Rnd.get(1, SAY_TEXT.length - 1)]);
			}
		}
	}
	
	/**
	 * Method onEvtAttacked.
	 * @param attacker Creature
	 * @param damage int
	 */
	@Override
	protected void onEvtAttacked(Creature attacker, int damage)
	{
		// empty method
	}
	
	/**
	 * Method onEvtAggression.
	 * @param target Creature
	 * @param aggro int
	 */
	@Override
	protected void onEvtAggression(Creature target, int aggro)
	{
		// empty method
	}
}
