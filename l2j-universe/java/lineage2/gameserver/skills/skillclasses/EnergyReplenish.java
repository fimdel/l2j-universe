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
package lineage2.gameserver.skills.skillclasses;

import java.util.List;

import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.items.Inventory;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.network.serverpackets.SystemMessage2;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.templates.StatsSet;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class EnergyReplenish extends Skill
{
	/**
	 * Field _addEnergy.
	 */
	private final int _addEnergy;
	
	/**
	 * Constructor for EnergyReplenish.
	 * @param set StatsSet
	 */
	public EnergyReplenish(StatsSet set)
	{
		super(set);
		_addEnergy = set.getInteger("addEnergy");
	}
	
	/**
	 * Method checkCondition.
	 * @param activeChar Creature
	 * @param target Creature
	 * @param forceUse boolean
	 * @param dontMove boolean
	 * @param first boolean
	 * @return boolean
	 */
	@Override
	public boolean checkCondition(Creature activeChar, Creature target, boolean forceUse, boolean dontMove, boolean first)
	{
		if (!super.checkCondition(activeChar, target, forceUse, dontMove, first))
		{
			return false;
		}
		if (!activeChar.isPlayer())
		{
			return false;
		}
		Player player = (Player) activeChar;
		ItemInstance item = player.getInventory().getPaperdollItem(Inventory.PAPERDOLL_LBRACELET);
		if ((item == null) || ((item.getTemplate().getAgathionEnergy() - item.getAgathionEnergy()) < _addEnergy))
		{
			player.sendPacket(SystemMsg.YOUR_ENERGY_CANNOT_BE_REPLENISHED_BECAUSE_CONDITIONS_ARE_NOT_MET);
			return false;
		}
		return true;
	}
	
	/**
	 * Method useSkill.
	 * @param activeChar Creature
	 * @param targets List<Creature>
	 */
	@Override
	public void useSkill(Creature activeChar, List<Creature> targets)
	{
		for (Creature cha : targets)
		{
			cha.setAgathionEnergy(cha.getAgathionEnergy() + _addEnergy);
			cha.sendPacket(new SystemMessage2(SystemMsg.ENERGY_S1_REPLENISHED).addInteger(_addEnergy));
		}
	}
}
