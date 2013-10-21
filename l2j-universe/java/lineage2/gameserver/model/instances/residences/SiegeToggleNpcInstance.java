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
package lineage2.gameserver.model.instances.residences;

import java.util.Set;

import lineage2.gameserver.data.xml.holder.NpcHolder;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.Spawner;
import lineage2.gameserver.model.entity.events.impl.SiegeEvent;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.templates.npc.NpcTemplate;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public abstract class SiegeToggleNpcInstance extends NpcInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field _fakeInstance.
	 */
	private NpcInstance _fakeInstance;
	/**
	 * Field _maxHp.
	 */
	private int _maxHp;
	
	/**
	 * Constructor for SiegeToggleNpcInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public SiegeToggleNpcInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
		setHasChatWindow(false);
	}
	
	/**
	 * Method setMaxHp.
	 * @param maxHp int
	 */
	public void setMaxHp(int maxHp)
	{
		_maxHp = maxHp;
	}
	
	/**
	 * Method setZoneList.
	 * @param set Set<String>
	 */
	public void setZoneList(Set<String> set)
	{
	}
	
	/**
	 * Method register.
	 * @param spawn Spawner
	 */
	public void register(Spawner spawn)
	{
	}
	
	/**
	 * Method initFake.
	 * @param fakeNpcId int
	 */
	public void initFake(int fakeNpcId)
	{
		_fakeInstance = NpcHolder.getInstance().getTemplate(fakeNpcId).getNewInstance();
		_fakeInstance.setCurrentHpMp(1, _fakeInstance.getMaxMp());
		_fakeInstance.setHasChatWindow(false);
	}
	
	/**
	 * Method onDeathImpl.
	 * @param killer Creature
	 */
	public abstract void onDeathImpl(Creature killer);
	
	/**
	 * Method onReduceCurrentHp.
	 * @param damage double
	 * @param attacker Creature
	 * @param skill Skill
	 * @param awake boolean
	 * @param standUp boolean
	 * @param directHp boolean
	 */
	@Override
	protected void onReduceCurrentHp(double damage, Creature attacker, Skill skill, boolean awake, boolean standUp, boolean directHp)
	{
		setCurrentHp(Math.max(getCurrentHp() - damage, 0), false);
		if (getCurrentHp() < 0.5)
		{
			doDie(attacker);
			onDeathImpl(attacker);
			decayMe();
			_fakeInstance.spawnMe(getLoc());
		}
	}
	
	/**
	 * Method isAutoAttackable.
	 * @param attacker Creature
	 * @return boolean
	 */
	@Override
	public boolean isAutoAttackable(Creature attacker)
	{
		if (attacker == null)
		{
			return false;
		}
		Player player = attacker.getPlayer();
		if (player == null)
		{
			return false;
		}
		SiegeEvent<?, ?> siegeEvent = getEvent(SiegeEvent.class);
		if ((siegeEvent == null) || !siegeEvent.isInProgress())
		{
			return false;
		}
		if (siegeEvent.getSiegeClan(SiegeEvent.DEFENDERS, player.getClan()) != null)
		{
			return false;
		}
		return true;
	}
	
	/**
	 * Method isAttackable.
	 * @param attacker Creature
	 * @return boolean
	 */
	@Override
	public boolean isAttackable(Creature attacker)
	{
		return isAutoAttackable(attacker);
	}
	
	/**
	 * Method isInvul.
	 * @return boolean
	 */
	@Override
	public boolean isInvul()
	{
		return false;
	}
	
	/**
	 * Method hasRandomAnimation.
	 * @return boolean
	 */
	@Override
	public boolean hasRandomAnimation()
	{
		return false;
	}
	
	/**
	 * Method isFearImmune.
	 * @return boolean
	 */
	@Override
	public boolean isFearImmune()
	{
		return true;
	}
	
	/**
	 * Method isParalyzeImmune.
	 * @return boolean
	 */
	@Override
	public boolean isParalyzeImmune()
	{
		return true;
	}
	
	/**
	 * Method isLethalImmune.
	 * @return boolean
	 */
	@Override
	public boolean isLethalImmune()
	{
		return true;
	}
	
	/**
	 * Method decayFake.
	 */
	public void decayFake()
	{
		_fakeInstance.decayMe();
	}
	
	/**
	 * Method getMaxHp.
	 * @return int
	 */
	@Override
	public int getMaxHp()
	{
		return _maxHp;
	}
	
	/**
	 * Method onDecay.
	 */
	@Override
	protected void onDecay()
	{
		decayMe();
		_spawnAnimation = 2;
	}
	
	/**
	 * Method getClan.
	 * @return Clan
	 */
	@Override
	public Clan getClan()
	{
		return null;
	}
}
