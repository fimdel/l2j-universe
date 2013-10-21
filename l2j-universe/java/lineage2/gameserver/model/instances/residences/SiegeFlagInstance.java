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

import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.entity.events.objects.SiegeClanObject;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.templates.npc.NpcTemplate;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class SiegeFlagInstance extends NpcInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field _owner.
	 */
	private SiegeClanObject _owner;
	/**
	 * Field _lastAnnouncedAttackedTime.
	 */
	private long _lastAnnouncedAttackedTime = 0;
	
	/**
	 * Constructor for SiegeFlagInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public SiegeFlagInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
		setHasChatWindow(false);
	}
	
	/**
	 * Method getName.
	 * @return String
	 */
	@Override
	public String getName()
	{
		return _owner.getClan().getName();
	}
	
	/**
	 * Method getClan.
	 * @return Clan
	 */
	@Override
	public Clan getClan()
	{
		return _owner.getClan();
	}
	
	/**
	 * Method getTitle.
	 * @return String
	 */
	@Override
	public String getTitle()
	{
		return StringUtils.EMPTY;
	}
	
	/**
	 * Method isAutoAttackable.
	 * @param attacker Creature
	 * @return boolean
	 */
	@Override
	public boolean isAutoAttackable(Creature attacker)
	{
		Player player = attacker.getPlayer();
		if ((player == null) || isInvul())
		{
			return false;
		}
		Clan clan = player.getClan();
		return (clan == null) || (_owner.getClan() != clan);
	}
	
	/**
	 * Method isAttackable.
	 * @param attacker Creature
	 * @return boolean
	 */
	@Override
	public boolean isAttackable(Creature attacker)
	{
		return true;
	}
	
	/**
	 * Method onDeath.
	 * @param killer Creature
	 */
	@Override
	protected void onDeath(Creature killer)
	{
		_owner.setFlag(null);
		super.onDeath(killer);
	}
	
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
	protected void onReduceCurrentHp(final double damage, final Creature attacker, Skill skill, final boolean awake, final boolean standUp, boolean directHp)
	{
		if ((System.currentTimeMillis() - _lastAnnouncedAttackedTime) > 120000)
		{
			_lastAnnouncedAttackedTime = System.currentTimeMillis();
			_owner.getClan().broadcastToOnlineMembers(SystemMsg.YOUR_BASE_IS_BEING_ATTACKED);
		}
		super.onReduceCurrentHp(damage, attacker, skill, awake, standUp, directHp);
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
	 * Method isInvul.
	 * @return boolean
	 */
	@Override
	public boolean isInvul()
	{
		return _isInvul;
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
	 * Method isHealBlocked.
	 * @return boolean
	 */
	@Override
	public boolean isHealBlocked()
	{
		return true;
	}
	
	/**
	 * Method isEffectImmune.
	 * @return boolean
	 */
	@Override
	public boolean isEffectImmune()
	{
		return true;
	}
	
	/**
	 * Method setClan.
	 * @param owner SiegeClanObject
	 */
	public void setClan(SiegeClanObject owner)
	{
		_owner = owner;
	}
}
