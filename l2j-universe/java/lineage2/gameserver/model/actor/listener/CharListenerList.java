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
package lineage2.gameserver.model.actor.listener;

import lineage2.commons.listener.Listener;
import lineage2.commons.listener.ListenerList;
import lineage2.gameserver.ai.CtrlEvent;
import lineage2.gameserver.ai.CtrlIntention;
import lineage2.gameserver.listener.actor.OnAttackHitListener;
import lineage2.gameserver.listener.actor.OnAttackListener;
import lineage2.gameserver.listener.actor.OnCurrentHpDamageListener;
import lineage2.gameserver.listener.actor.OnDeathListener;
import lineage2.gameserver.listener.actor.OnKillListener;
import lineage2.gameserver.listener.actor.OnMagicHitListener;
import lineage2.gameserver.listener.actor.OnMagicUseListener;
import lineage2.gameserver.listener.actor.ai.OnAiEventListener;
import lineage2.gameserver.listener.actor.ai.OnAiIntentionListener;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Skill;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class CharListenerList extends ListenerList<Creature>
{
	/**
	 * Field global.
	 */
	final static ListenerList<Creature> global = new ListenerList<>();
	/**
	 * Field actor.
	 */
	protected final Creature actor;
	
	/**
	 * Constructor for CharListenerList.
	 * @param actor Creature
	 */
	public CharListenerList(Creature actor)
	{
		this.actor = actor;
	}
	
	/**
	 * Method getActor.
	 * @return Creature
	 */
	public Creature getActor()
	{
		return actor;
	}
	
	/**
	 * Method addGlobal.
	 * @param listener Listener<Creature>
	 * @return boolean
	 */
	public static boolean addGlobal(Listener<Creature> listener)
	{
		return global.add(listener);
	}
	
	/**
	 * Method removeGlobal.
	 * @param listener Listener<Creature>
	 * @return boolean
	 */
	public static boolean removeGlobal(Listener<Creature> listener)
	{
		return global.remove(listener);
	}
	
	/**
	 * Method onAiIntention.
	 * @param intention CtrlIntention
	 * @param arg0 Object
	 * @param arg1 Object
	 */
	public void onAiIntention(CtrlIntention intention, Object arg0, Object arg1)
	{
		if (!getListeners().isEmpty())
		{
			for (Listener<Creature> listener : getListeners())
			{
				if (OnAiIntentionListener.class.isInstance(listener))
				{
					((OnAiIntentionListener) listener).onAiIntention(getActor(), intention, arg0, arg1);
				}
			}
		}
	}
	
	/**
	 * Method onAiEvent.
	 * @param evt CtrlEvent
	 * @param args Object[]
	 */
	public void onAiEvent(CtrlEvent evt, Object[] args)
	{
		if (!getListeners().isEmpty())
		{
			for (Listener<Creature> listener : getListeners())
			{
				if (OnAiEventListener.class.isInstance(listener))
				{
					((OnAiEventListener) listener).onAiEvent(getActor(), evt, args);
				}
			}
		}
	}
	
	/**
	 * Method onAttack.
	 * @param target Creature
	 */
	public void onAttack(Creature target)
	{
		if (!global.getListeners().isEmpty())
		{
			for (Listener<Creature> listener : global.getListeners())
			{
				if (OnAttackListener.class.isInstance(listener))
				{
					((OnAttackListener) listener).onAttack(getActor(), target);
				}
			}
		}
		if (!getListeners().isEmpty())
		{
			for (Listener<Creature> listener : getListeners())
			{
				if (OnAttackListener.class.isInstance(listener))
				{
					((OnAttackListener) listener).onAttack(getActor(), target);
				}
			}
		}
	}
	
	/**
	 * Method onAttackHit.
	 * @param attacker Creature
	 */
	public void onAttackHit(Creature attacker)
	{
		if (!global.getListeners().isEmpty())
		{
			for (Listener<Creature> listener : global.getListeners())
			{
				if (OnAttackHitListener.class.isInstance(listener))
				{
					((OnAttackHitListener) listener).onAttackHit(getActor(), attacker);
				}
			}
		}
		if (!getListeners().isEmpty())
		{
			for (Listener<Creature> listener : getListeners())
			{
				if (OnAttackHitListener.class.isInstance(listener))
				{
					((OnAttackHitListener) listener).onAttackHit(getActor(), attacker);
				}
			}
		}
	}
	
	/**
	 * Method onMagicUse.
	 * @param skill Skill
	 * @param target Creature
	 * @param alt boolean
	 */
	public void onMagicUse(Skill skill, Creature target, boolean alt)
	{
		if (!global.getListeners().isEmpty())
		{
			for (Listener<Creature> listener : global.getListeners())
			{
				if (OnMagicUseListener.class.isInstance(listener))
				{
					((OnMagicUseListener) listener).onMagicUse(getActor(), skill, target, alt);
				}
			}
		}
		if (!getListeners().isEmpty())
		{
			for (Listener<Creature> listener : getListeners())
			{
				if (OnMagicUseListener.class.isInstance(listener))
				{
					((OnMagicUseListener) listener).onMagicUse(getActor(), skill, target, alt);
				}
			}
		}
	}
	
	/**
	 * Method onMagicHit.
	 * @param skill Skill
	 * @param caster Creature
	 */
	public void onMagicHit(Skill skill, Creature caster)
	{
		if (!global.getListeners().isEmpty())
		{
			for (Listener<Creature> listener : global.getListeners())
			{
				if (OnMagicHitListener.class.isInstance(listener))
				{
					((OnMagicHitListener) listener).onMagicHit(getActor(), skill, caster);
				}
			}
		}
		if (!getListeners().isEmpty())
		{
			for (Listener<Creature> listener : getListeners())
			{
				if (OnMagicHitListener.class.isInstance(listener))
				{
					((OnMagicHitListener) listener).onMagicHit(getActor(), skill, caster);
				}
			}
		}
	}
	
	/**
	 * Method onDeath.
	 * @param killer Creature
	 */
	public void onDeath(Creature killer)
	{
		if (!global.getListeners().isEmpty())
		{
			for (Listener<Creature> listener : global.getListeners())
			{
				if (OnDeathListener.class.isInstance(listener))
				{
					((OnDeathListener) listener).onDeath(getActor(), killer);
				}
			}
		}
		if (!getListeners().isEmpty())
		{
			for (Listener<Creature> listener : getListeners())
			{
				if (OnDeathListener.class.isInstance(listener))
				{
					((OnDeathListener) listener).onDeath(getActor(), killer);
				}
			}
		}
	}
	
	/**
	 * Method onKill.
	 * @param victim Creature
	 */
	public void onKill(Creature victim)
	{
		if (!global.getListeners().isEmpty())
		{
			for (Listener<Creature> listener : global.getListeners())
			{
				if (OnKillListener.class.isInstance(listener) && !((OnKillListener) listener).ignorePetOrSummon())
				{
					((OnKillListener) listener).onKill(getActor(), victim);
				}
			}
		}
		if (!getListeners().isEmpty())
		{
			for (Listener<Creature> listener : getListeners())
			{
				if (OnKillListener.class.isInstance(listener) && !((OnKillListener) listener).ignorePetOrSummon())
				{
					((OnKillListener) listener).onKill(getActor(), victim);
				}
			}
		}
	}
	
	/**
	 * Method onKillIgnorePetOrSummon.
	 * @param victim Creature
	 */
	public void onKillIgnorePetOrSummon(Creature victim)
	{
		if (!global.getListeners().isEmpty())
		{
			for (Listener<Creature> listener : global.getListeners())
			{
				if (OnKillListener.class.isInstance(listener) && ((OnKillListener) listener).ignorePetOrSummon())
				{
					((OnKillListener) listener).onKill(getActor(), victim);
				}
			}
		}
		if (!getListeners().isEmpty())
		{
			for (Listener<Creature> listener : getListeners())
			{
				if (OnKillListener.class.isInstance(listener) && ((OnKillListener) listener).ignorePetOrSummon())
				{
					((OnKillListener) listener).onKill(getActor(), victim);
				}
			}
		}
	}
	
	/**
	 * Method onCurrentHpDamage.
	 * @param damage double
	 * @param attacker Creature
	 * @param skill Skill
	 */
	public void onCurrentHpDamage(double damage, Creature attacker, Skill skill)
	{
		if (!global.getListeners().isEmpty())
		{
			for (Listener<Creature> listener : global.getListeners())
			{
				if (OnCurrentHpDamageListener.class.isInstance(listener))
				{
					((OnCurrentHpDamageListener) listener).onCurrentHpDamage(getActor(), damage, attacker, skill);
				}
			}
		}
		if (!getListeners().isEmpty())
		{
			for (Listener<Creature> listener : getListeners())
			{
				if (OnCurrentHpDamageListener.class.isInstance(listener))
				{
					((OnCurrentHpDamageListener) listener).onCurrentHpDamage(getActor(), damage, attacker, skill);
				}
			}
		}
	}
}
