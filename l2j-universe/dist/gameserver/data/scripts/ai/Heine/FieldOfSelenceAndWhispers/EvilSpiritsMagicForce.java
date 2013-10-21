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
package ai.Heine.FieldOfSelenceAndWhispers;

import java.util.List;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.ai.CtrlEvent;
import lineage2.gameserver.ai.CtrlIntention;
import lineage2.gameserver.ai.Fighter;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.components.ChatType;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.scripts.Functions;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class EvilSpiritsMagicForce extends Fighter
{
	/**
	 * Field mob.
	 */
	private NpcInstance mob = null;
	/**
	 * Field _firstTimeAttacked.
	 */
	private boolean _firstTimeAttacked = true;
	/**
	 * Field MsgText.
	 */
	public static final NpcString[] MsgText =
	{
		NpcString.AH_AH_FROM_THE_MAGIC_FORCE_NO_MORE_I_WILL_BE_FREED,
		NpcString.EVEN_THE_MAGIC_FORCE_BINDS_YOU_YOU_WILL_NEVER_BE_FORGIVEN
	};
	
	/**
	 * Constructor for EvilSpiritsMagicForce.
	 * @param actor NpcInstance
	 */
	public EvilSpiritsMagicForce(NpcInstance actor)
	{
		super(actor);
	}
	
	/**
	 * Method onIntentionAttack.
	 * @param target Creature
	 */
	@Override
	protected void onIntentionAttack(Creature target)
	{
		final NpcInstance actor = getActor();
		if (actor == null)
		{
			return;
		}
		if (getIntention() == CtrlIntention.AI_INTENTION_ACTIVE)
		{
			Functions.npcSay(actor, NpcString.PROTECT_THE_BRAZIERS_OF_PURITY_AT_ALL_COSTS, ChatType.SHOUT, 5000);
		}
		super.onIntentionAttack(target);
	}
	
	/**
	 * Method thinkActive.
	 * @return boolean
	 */
	@Override
	protected boolean thinkActive()
	{
		final NpcInstance actor = getActor();
		if ((actor == null) || actor.isDead())
		{
			return true;
		}
		if (mob == null)
		{
			final List<NpcInstance> around = getActor().getAroundNpc(300, 300);
			if ((around != null) && !around.isEmpty())
			{
				for (NpcInstance npc : around)
				{
					if ((npc.getNpcId() >= 22650) && (npc.getNpcId() <= 22655))
					{
						if ((mob == null) || (getActor().getDistance3D(npc) < getActor().getDistance3D(mob)))
						{
							mob = npc;
						}
					}
				}
			}
		}
		if (mob != null)
		{
			actor.stopMove();
			actor.setRunning();
			getActor().getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, mob, 1);
			return true;
		}
		return false;
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
		if (actor == null)
		{
			return;
		}
		if (_firstTimeAttacked)
		{
			_firstTimeAttacked = false;
			if (Rnd.chance(25))
			{
				Functions.npcSay(actor, Rnd.get(MsgText), ChatType.ALL, 5000);
			}
		}
		else if (Rnd.chance(10))
		{
			Functions.npcSay(actor, NpcString.PROTECT_THE_BRAZIERS_OF_PURITY_AT_ALL_COSTS, ChatType.SHOUT, 5000);
		}
		super.onEvtAttacked(attacker, damage);
	}
	
	/**
	 * Method onEvtDead.
	 * @param killer Creature
	 */
	@Override
	protected void onEvtDead(Creature killer)
	{
		_firstTimeAttacked = true;
		super.onEvtDead(killer);
	}
}
