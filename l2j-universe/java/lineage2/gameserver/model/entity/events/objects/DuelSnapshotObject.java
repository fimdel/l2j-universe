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
package lineage2.gameserver.model.entity.events.objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.instancemanager.ReflectionManager;
import lineage2.gameserver.model.Effect;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.base.TeamType;
import lineage2.gameserver.stats.Env;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class DuelSnapshotObject implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field _team.
	 */
	private final TeamType _team;
	/**
	 * Field _player.
	 */
	final Player _player;
	/**
	 * Field _effects.
	 */
	private final List<Effect> _effects;
	/**
	 * Field _returnLoc.
	 */
	final Location _returnLoc;
	/**
	 * Field _currentHp.
	 */
	private final double _currentHp;
	/**
	 * Field _currentMp.
	 */
	private final double _currentMp;
	/**
	 * Field _currentCp.
	 */
	private final double _currentCp;
	
	/**
	 * Field _isDead.
	 */
	private boolean _isDead;
	
	/**
	 * Constructor for DuelSnapshotObject.
	 * @param player Player
	 * @param team TeamType
	 */
	public DuelSnapshotObject(Player player, TeamType team)
	{
		_player = player;
		_team = team;
		_returnLoc = player.getReflection().getReturnLoc() == null ? player.getLoc() : player.getReflection().getReturnLoc();
		
		_currentCp = player.getCurrentCp();
		_currentHp = player.getCurrentHp();
		_currentMp = player.getCurrentMp();
		
		List<Effect> effectList = player.getEffectList().getAllEffects();
		_effects = new ArrayList<>(effectList.size());
		for (Effect $effect : effectList)
		{
			Effect effect = $effect.getTemplate().getEffect(new Env($effect.getEffector(), $effect.getEffected(), $effect.getSkill()));
			effect.setCount($effect.getCount());
			effect.setPeriod($effect.getCount() == 1 ? $effect.getPeriod() - $effect.getTime() : $effect.getPeriod());
			
			_effects.add(effect);
		}
	}
	
	/**
	 * Method restore.
	 * @param abnormal boolean
	 */
	public void restore(boolean abnormal)
	{
		if (!abnormal)
		{
			_player.getEffectList().stopAllEffects();
			for (Effect e : _effects)
			{
				_player.getEffectList().addEffect(e);
			}
			
			_player.setCurrentCp(_currentCp);
			_player.setCurrentHpMp(_currentHp, _currentMp);
		}
	}
	
	/**
	 * Method teleport.
	 */
	public void teleport()
	{
		_player._stablePoint = null;
		if (_player.isFrozen())
		{
			_player.stopFrozen();
		}
		
		ThreadPoolManager.getInstance().schedule(new RunnableImpl()
		{
			@Override
			public void runImpl()
			{
				_player.teleToLocation(_returnLoc, ReflectionManager.DEFAULT);
			}
		}, 5000L);
	}
	
	/**
	 * Method getPlayer.
	 * @return Player
	 */
	public Player getPlayer()
	{
		return _player;
	}
	
	/**
	 * Method isDead.
	 * @return boolean
	 */
	public boolean isDead()
	{
		return _isDead;
	}
	
	/**
	 * Method setDead.
	 */
	public void setDead()
	{
		_isDead = true;
	}
	
	/**
	 * Method getLoc.
	 * @return Location
	 */
	public Location getLoc()
	{
		return _returnLoc;
	}
	
	/**
	 * Method getTeam.
	 * @return TeamType
	 */
	public TeamType getTeam()
	{
		return _team;
	}
}
