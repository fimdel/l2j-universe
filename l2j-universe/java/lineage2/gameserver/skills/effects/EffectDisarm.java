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

import lineage2.gameserver.model.Effect;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.stats.Env;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class EffectDisarm extends Effect
{
	/**
	 * Constructor for EffectDisarm.
	 * @param env Env
	 * @param template EffectTemplate
	 */
	public EffectDisarm(Env env, EffectTemplate template)
	{
		super(env, template);
	}
	
	/**
	 * Method checkCondition.
	 * @return boolean
	 */
	@Override
	public boolean checkCondition()
	{
		if (!_effected.isPlayer())
		{
			return false;
		}
		Player player = _effected.getPlayer();
		if (player.isCursedWeaponEquipped() || (player.getActiveWeaponFlagAttachment() != null))
		{
			return false;
		}
		return super.checkCondition();
	}
	
	/**
	 * Method onStart.
	 */
	@Override
	public void onStart()
	{
		super.onStart();
		Player player = (Player) _effected;
		ItemInstance wpn = player.getActiveWeaponInstance();
		if (wpn != null)
		{
			player.getInventory().unEquipItem(wpn);
			player.sendDisarmMessage(wpn);
		}
		player.startWeaponEquipBlocked();
	}
	
	/**
	 * Method onExit.
	 */
	@Override
	public void onExit()
	{
		super.onExit();
		_effected.stopWeaponEquipBlocked();
	}
	
	/**
	 * Method onActionTime.
	 * @return boolean
	 */
	@Override
	public boolean onActionTime()
	{
		return false;
	}
}
