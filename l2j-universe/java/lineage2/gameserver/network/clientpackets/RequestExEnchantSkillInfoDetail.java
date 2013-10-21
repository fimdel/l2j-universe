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
import lineage2.gameserver.model.base.EnchantSkillLearn;
import lineage2.gameserver.network.serverpackets.ExEnchantSkillInfoDetail;
import lineage2.gameserver.tables.SkillTreeTable;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class RequestExEnchantSkillInfoDetail extends L2GameClientPacket
{
	/**
	 * Field TYPE_NORMAL_ENCHANT. (value is 0)
	 */
	private static final int TYPE_NORMAL_ENCHANT = 0;
	/**
	 * Field TYPE_SAFE_ENCHANT. (value is 1)
	 */
	private static final int TYPE_SAFE_ENCHANT = 1;
	/**
	 * Field TYPE_UNTRAIN_ENCHANT. (value is 2)
	 */
	private static final int TYPE_UNTRAIN_ENCHANT = 2;
	/**
	 * Field TYPE_CHANGE_ENCHANT. (value is 3)
	 */
	private static final int TYPE_CHANGE_ENCHANT = 3;
	/**
	 * Field _type.
	 */
	private int _type;
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
		_type = readD();
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
		if (activeChar.getTransformation() != 0)
		{
			activeChar.sendMessage("You must leave transformation mode first.");
			return;
		}
		if ((activeChar.getLevel() < 76) || (activeChar.getClassLevel() < 4))
		{
			activeChar.sendMessage("You must have 3rd class change quest completed.");
			return;
		}
		int bookId = 0;
		int sp = 0;
		int adenaCount = 0;
		double spMult = SkillTreeTable.NORMAL_ENCHANT_COST_MULTIPLIER;
		EnchantSkillLearn esd = null;
		switch (_type)
		{
			case TYPE_NORMAL_ENCHANT:
				if ((_skillId < 10000) & ((_skillLvl % 100) == 1))
				{
					bookId = SkillTreeTable.NORMAL_ENCHANT_BOOK;
				}
				else if (_skillId >= 10000)
				{
					bookId = SkillTreeTable.NEW_ENCHANT_BOOK;
				}
				esd = SkillTreeTable.getSkillEnchant(_skillId, _skillLvl);
				break;
			case TYPE_SAFE_ENCHANT:
				if (_skillId < 10000)
				{
					bookId = SkillTreeTable.SAFE_ENCHANT_BOOK;
				}
				else
				{
					bookId = SkillTreeTable.NEW_SAFE_ENCHANT_BOOK;
				}
				esd = SkillTreeTable.getSkillEnchant(_skillId, _skillLvl);
				spMult = SkillTreeTable.SAFE_ENCHANT_COST_MULTIPLIER;
				break;
			case TYPE_UNTRAIN_ENCHANT:
				if (_skillId < 10000)
				{
					bookId = SkillTreeTable.UNTRAIN_ENCHANT_BOOK;
				}
				else
				{
					bookId = SkillTreeTable.UNTRAIN_NEW_ENCHANT_BOOK;
				}
				esd = SkillTreeTable.getSkillEnchant(_skillId, _skillLvl + 1);
				break;
			case TYPE_CHANGE_ENCHANT:
				if (_skillId < 10000)
				{
					bookId = SkillTreeTable.CHANGE_ENCHANT_BOOK;
				}
				else
				{
					bookId = SkillTreeTable.NEW_CHANGE_ENCHANT_BOOK;
				}
				esd = SkillTreeTable.getEnchantsForChange(_skillId, _skillLvl).get(0);
				spMult = 1f / SkillTreeTable.SAFE_ENCHANT_COST_MULTIPLIER;
				break;
		}
		if (esd == null)
		{
			return;
		}
		spMult *= esd.getCostMult();
		int[] cost = esd.getCost();
		sp = (int) (cost[1] * spMult);
		if (_type != TYPE_UNTRAIN_ENCHANT)
		{
			adenaCount = (int) (cost[0] * spMult);
		}
		activeChar.sendPacket(new ExEnchantSkillInfoDetail(_skillId, _skillLvl, sp, esd.getRate(activeChar), bookId, adenaCount));
	}
}
