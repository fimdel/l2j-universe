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
package lineage2.gameserver.skills.effects;

import gnu.trove.iterator.TObjectIntIterator;
import gnu.trove.map.hash.TObjectIntHashMap;
import lineage2.commons.lang.reference.HardReference;
import lineage2.gameserver.listener.actor.OnCurrentHpDamageListener;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Effect;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.stats.Env;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class EffectCurseOfLifeFlow extends Effect
{
	/**
	 * Field _listener.
	 */
	private CurseOfLifeFlowListener _listener;
	/**
	 * Field _damageList.
	 */
	final TObjectIntHashMap<HardReference<? extends Creature>> _damageList = new TObjectIntHashMap<>();
	
	/**
	 * Constructor for EffectCurseOfLifeFlow.
	 * @param env Env
	 * @param template EffectTemplate
	 */
	public EffectCurseOfLifeFlow(Env env, EffectTemplate template)
	{
		super(env, template);
	}
	
	/**
	 * Method onStart.
	 */
	@Override
	public void onStart()
	{
		super.onStart();
		_listener = new CurseOfLifeFlowListener();
		_effected.addListener(_listener);
	}
	
	/**
	 * Method onExit.
	 */
	@Override
	public void onExit()
	{
		super.onExit();
		_effected.removeListener(_listener);
		_listener = null;
	}
	
	/**
	 * Method onActionTime.
	 * @return boolean
	 */
	@Override
	public boolean onActionTime()
	{
		if (_effected.isDead())
		{
			return false;
		}
		for (TObjectIntIterator<HardReference<? extends Creature>> iterator = _damageList.iterator(); iterator.hasNext();)
		{
			iterator.advance();
			Creature damager = iterator.key().get();
			if ((damager == null) || damager.isDead() || damager.isCurrentHpFull())
			{
				continue;
			}
			int damage = iterator.value();
			if (damage <= 0)
			{
				continue;
			}
			double max_heal = calc();
			double heal = Math.min(damage, max_heal);
			double newHp = Math.min(damager.getCurrentHp() + heal, damager.getMaxHp());
			damager.sendPacket(new SystemMessage(SystemMessage.S1_HPS_HAVE_BEEN_RESTORED).addNumber((long) (newHp - damager.getCurrentHp())));
			damager.setCurrentHp(newHp, false);
		}
		_damageList.clear();
		return true;
	}
	
	/**
	 * @author Mobius
	 */
	private class CurseOfLifeFlowListener implements OnCurrentHpDamageListener
	{
		/**
		 * Constructor for CurseOfLifeFlowListener.
		 */
		public CurseOfLifeFlowListener()
		{
			// TODO Auto-generated constructor stub
		}
		
		/**
		 * Method onCurrentHpDamage.
		 * @param actor Creature
		 * @param damage double
		 * @param attacker Creature
		 * @param skill Skill
		 * @see lineage2.gameserver.listener.actor.OnCurrentHpDamageListener#onCurrentHpDamage(Creature, double, Creature, Skill)
		 */
		@Override
		public void onCurrentHpDamage(Creature actor, double damage, Creature attacker, Skill skill)
		{
			if ((attacker == actor) || (attacker == _effected))
			{
				return;
			}
			int old_damage = _damageList.get(attacker.getRef());
			_damageList.put(attacker.getRef(), old_damage == 0 ? (int) damage : old_damage + (int) damage);
		}
	}
}
