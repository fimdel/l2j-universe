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
package npc.model;

import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.instances.MonsterInstance;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.ExStartScenePlayer;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.ItemFunctions;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class SealDeviceInstance extends MonsterInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field _gaveItem.
	 */
	private boolean _gaveItem = false;
	
	/**
	 * Constructor for SealDeviceInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public SealDeviceInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	/**
	 * Method reduceCurrentHp.
	 * @param i double
	 * @param reflectableDamage double
	 * @param attacker Creature
	 * @param skill Skill
	 * @param awake boolean
	 * @param standUp boolean
	 * @param directHp boolean
	 * @param canReflect boolean
	 * @param transferDamage boolean
	 * @param isDot boolean
	 * @param sendMessage boolean
	 */
	@Override
	public void reduceCurrentHp(double i, double reflectableDamage, Creature attacker, Skill skill, boolean awake, boolean standUp, boolean directHp, boolean canReflect, boolean transferDamage, boolean isDot, boolean sendMessage)
	{
		if (getCurrentHp() < i)
		{
			if (!_gaveItem && (ItemFunctions.getItemCount(attacker.getPlayer(), 13846) < 4))
			{
				setRHandId(15281);
				broadcastCharInfo();
				ItemFunctions.addItem(attacker.getPlayer(), 13846, 1, true);
				_gaveItem = true;
				if (ItemFunctions.getItemCount(attacker.getPlayer(), 13846) >= 4)
				{
					attacker.getPlayer().showQuestMovie(ExStartScenePlayer.SCENE_SSQ_SEALING_EMPEROR_2ND);
					ThreadPoolManager.getInstance().schedule(new TeleportPlayer(attacker.getPlayer()), 26500L);
				}
			}
			i = getCurrentHp() - 1;
		}
		attacker.reduceCurrentHp(450, 0, this, null, true, false, true, false, false, false, true);
		super.reduceCurrentHp(i, reflectableDamage, attacker, skill, awake, standUp, directHp, canReflect, transferDamage, isDot, sendMessage);
	}
	
	/**
	 * @author Mobius
	 */
	private class TeleportPlayer extends RunnableImpl
	{
		/**
		 * Field _p.
		 */
		Player _p;
		
		/**
		 * Constructor for TeleportPlayer.
		 * @param p Player
		 */
		public TeleportPlayer(Player p)
		{
			_p = p;
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			for (NpcInstance n : _p.getReflection().getNpcs())
			{
				if ((n.getNpcId() != 32586) && (n.getNpcId() != 32587))
				{
					n.deleteMe();
				}
			}
			_p.getPlayer().teleToLocation(new Location(-89560, 215784, -7488));
		}
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
	 * Method isMovementDisabled.
	 * @return boolean
	 */
	@Override
	public boolean isMovementDisabled()
	{
		return true;
	}
}
