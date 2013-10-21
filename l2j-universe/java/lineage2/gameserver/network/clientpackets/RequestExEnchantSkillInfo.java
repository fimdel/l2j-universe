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
package lineage2.gameserver.network.clientpackets;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.base.EnchantSkillLearn;
import lineage2.gameserver.network.serverpackets.ExEnchantSkillInfo;
import lineage2.gameserver.tables.SkillTable;
import lineage2.gameserver.tables.SkillTreeTable;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestExEnchantSkillInfo extends L2GameClientPacket
{
	/**
	 * Field _skillId.
	 */
	private int _skillId;
	/**
	 * Field _skillLvl.
	 */
	private int _skillLvl;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_skillId = readD();
		_skillLvl = readD();
	}
	
	/**
	 * Method runImpl.
	 */
	@Override
	protected void runImpl()
	{
		Player activeChar = getClient().getActiveChar();
		if (activeChar == null)
		{
			return;
		}
		if (_skillLvl > 100)
		{
			EnchantSkillLearn sl = SkillTreeTable.getSkillEnchant(_skillId, _skillLvl);
			if (sl == null)
			{
				activeChar.sendMessage("Not found enchant info for this skill");
				return;
			}
			Skill skill = SkillTable.getInstance().getInfo(_skillId, SkillTreeTable.convertEnchantLevel(sl.getBaseLevel(), _skillLvl, sl.getMaxLevel()));
			if ((skill == null) || (skill.getId() != _skillId))
			{
				activeChar.sendMessage("This skill doesn't yet have enchant info in Datapack");
				return;
			}
			if (activeChar.getSkillLevel(_skillId) != skill.getLevel())
			{
				activeChar.sendMessage("Skill not found");
				return;
			}
		}
		else if (activeChar.getSkillLevel(_skillId) != _skillLvl)
		{
			activeChar.sendMessage("Skill not found");
			return;
		}
		sendPacket(new ExEnchantSkillInfo(_skillId, _skillLvl));
	}
}
