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
package lineage2.gameserver.model.instances;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

import lineage2.commons.lang.reference.HardReference;
import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.data.xml.holder.NpcHolder;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.network.serverpackets.AutoAttackStart;
import lineage2.gameserver.network.serverpackets.CharInfo;
import lineage2.gameserver.network.serverpackets.L2GameServerPacket;
import lineage2.gameserver.tables.SkillTable;
import lineage2.gameserver.templates.npc.NpcTemplate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class DecoyInstance extends NpcInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field _log.
	 */
	@SuppressWarnings("unused")
	private static final Logger _log = LoggerFactory.getLogger(DecoyInstance.class);
	/**
	 * Field _playerRef.
	 */
	private final HardReference<Player> _playerRef;
	/**
	 * Field _timeRemaining. Field _lifeTime.
	 */
	private int _lifeTime, _timeRemaining;
	/**
	 * Field _hateSpam. Field _decoyLifeTask.
	 */
	private ScheduledFuture<?> _decoyLifeTask, _hateSpam;
	
	/**
	 * Constructor for DecoyInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 * @param owner Player
	 * @param lifeTime int
	 */
	public DecoyInstance(int objectId, NpcTemplate template, Player owner, int lifeTime)
	{
		super(objectId, template);
		_playerRef = owner.getRef();
		_lifeTime = lifeTime;
		_timeRemaining = _lifeTime;
		int skilllevel = getNpcId() < 13257 ? getNpcId() - 13070 : getNpcId() - 13250;
		_decoyLifeTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new DecoyLifetime(), 1000, 1000);
		_hateSpam = ThreadPoolManager.getInstance().scheduleAtFixedRate(new HateSpam(SkillTable.getInstance().getInfo(5272, skilllevel)), 1000, 3000);
	}
	
	/**
	 * Method onDeath.
	 * @param killer Creature
	 */
	@Override
	protected void onDeath(Creature killer)
	{
		super.onDeath(killer);
		if (_hateSpam != null)
		{
			_hateSpam.cancel(false);
			_hateSpam = null;
		}
		_lifeTime = 0;
	}
	
	/**
	 * @author Mobius
	 */
	class DecoyLifetime extends RunnableImpl
	{
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			try
			{
				double newTimeRemaining;
				decTimeRemaining(1000);
				newTimeRemaining = getTimeRemaining();
				if (newTimeRemaining < 0)
				{
					unSummon();
				}
			}
			catch (Exception e)
			{
				_log.error("", e);
			}
		}
	}
	
	/**
	 * @author Mobius
	 */
	class HateSpam extends RunnableImpl
	{
		/**
		 * Field _skill.
		 */
		private final Skill _skill;
		
		/**
		 * Constructor for HateSpam.
		 * @param skill Skill
		 */
		HateSpam(Skill skill)
		{
			_skill = skill;
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			try
			{
				setTarget(DecoyInstance.this);
				doCast(_skill, DecoyInstance.this, true);
			}
			catch (Exception e)
			{
				_log.error("", e);
			}
		}
	}
	
	/**
	 * Method unSummon.
	 */
	public void unSummon()
	{
		if (_decoyLifeTask != null)
		{
			_decoyLifeTask.cancel(false);
			_decoyLifeTask = null;
		}
		if (_hateSpam != null)
		{
			_hateSpam.cancel(false);
			_hateSpam = null;
		}
		deleteMe();
	}
	
	/**
	 * Method decTimeRemaining.
	 * @param value int
	 */
	public void decTimeRemaining(int value)
	{
		_timeRemaining -= value;
	}
	
	/**
	 * Method getTimeRemaining.
	 * @return int
	 */
	public int getTimeRemaining()
	{
		return _timeRemaining;
	}
	
	/**
	 * Method getLifeTime.
	 * @return int
	 */
	public int getLifeTime()
	{
		return _lifeTime;
	}
	
	/**
	 * Method getPlayer.
	 * @return Player
	 */
	@Override
	public Player getPlayer()
	{
		return _playerRef.get();
	}
	
	/**
	 * Method isAutoAttackable.
	 * @param attacker Creature
	 * @return boolean
	 */
	@Override
	public boolean isAutoAttackable(Creature attacker)
	{
		Player owner = getPlayer();
		return (owner != null) && owner.isAutoAttackable(attacker);
	}
	
	/**
	 * Method isAttackable.
	 * @param attacker Creature
	 * @return boolean
	 */
	@Override
	public boolean isAttackable(Creature attacker)
	{
		Player owner = getPlayer();
		return (owner != null) && owner.isAttackable(attacker);
	}
	
	/**
	 * Method onDelete.
	 */
	@Override
	protected void onDelete()
	{
		Player owner = getPlayer();
		if (owner != null)
		{
			owner.setDecoy(null);
		}
		super.onDelete();
	}
	
	/**
	 * Method getColRadius.
	 * @return double
	 */
	@Override
	public double getColRadius()
	{
		Player player = getPlayer();
		if (player == null)
		{
			return 0;
		}
		if ((player.getTransformation() != 0) && (player.getTransformationTemplate() != 0))
		{
			return NpcHolder.getInstance().getTemplate(player.getTransformationTemplate()).getCollisionRadius();
		}
		return player.getTemplate().getCollisionRadius();
	}
	
	/**
	 * Method getColHeight.
	 * @return double
	 */
	@Override
	public double getColHeight()
	{
		Player player = getPlayer();
		if (player == null)
		{
			return 0;
		}
		if ((player.getTransformation() != 0) && (player.getTransformationTemplate() != 0))
		{
			return NpcHolder.getInstance().getTemplate(player.getTransformationTemplate()).getCollisionHeight();
		}
		return player.getTemplate().getCollisionHeight();
	}
	
	/**
	 * Method addPacketList.
	 * @param forPlayer Player
	 * @param dropper Creature
	 * @return List<L2GameServerPacket>
	 */
	@Override
	public List<L2GameServerPacket> addPacketList(Player forPlayer, Creature dropper)
	{
		if (!isInCombat())
		{
			return Collections.<L2GameServerPacket> singletonList(new CharInfo(this));
		}
		List<L2GameServerPacket> list = new ArrayList<>(2);
		list.add(new CharInfo(this));
		list.add(new AutoAttackStart(objectId));
		return list;
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
}
