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

import lineage2.commons.util.Rnd;
import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.base.EnchantSkillLearn;
import lineage2.gameserver.network.serverpackets.ExEnchantSkillInfo;
import lineage2.gameserver.network.serverpackets.ExEnchantSkillResult;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.tables.SkillTable;
import lineage2.gameserver.tables.SkillTreeTable;
import lineage2.gameserver.utils.Log;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class RequestExEnchantSkillRouteChange extends L2GameClientPacket
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
		EnchantSkillLearn sl = SkillTreeTable.getSkillEnchant(_skillId, _skillLvl);
		if (sl == null)
		{
			return;
		}
		int slevel = activeChar.getSkillDisplayLevel(_skillId);
		if (slevel == -1)
		{
			return;
		}
		if ((slevel <= sl.getBaseLevel()) || ((slevel % 100) != (_skillLvl % 100)))
		{
			return;
		}
		int[] cost = sl.getCost();
		int requiredSp = (cost[1] * sl.getCostMult()) / SkillTreeTable.SAFE_ENCHANT_COST_MULTIPLIER;
		int requiredAdena = (cost[0] * sl.getCostMult()) / SkillTreeTable.SAFE_ENCHANT_COST_MULTIPLIER;
		if (activeChar.getSp() < requiredSp)
		{
			sendPacket(Msg.SP_REQUIRED_FOR_SKILL_ENCHANT_IS_INSUFFICIENT);
			return;
		}
		if (activeChar.getAdena() < requiredAdena)
		{
			sendPacket(Msg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
			return;
		}
		if (_skillId < 10000)
		{
			if (Functions.getItemCount(activeChar, SkillTreeTable.CHANGE_ENCHANT_BOOK) == 0)
			{
				activeChar.sendPacket(Msg.ITEMS_REQUIRED_FOR_SKILL_ENCHANT_ARE_INSUFFICIENT);
				return;
			}
			Functions.removeItem(activeChar, SkillTreeTable.CHANGE_ENCHANT_BOOK, 1);
		}
		else if (_skillId >= 10000)
		{
			if (Functions.getItemCount(activeChar, SkillTreeTable.NEW_CHANGE_ENCHANT_BOOK) == 0)
			{
				activeChar.sendPacket(Msg.ITEMS_REQUIRED_FOR_SKILL_ENCHANT_ARE_INSUFFICIENT);
				return;
			}
			Functions.removeItem(activeChar, SkillTreeTable.NEW_CHANGE_ENCHANT_BOOK, 1);
		}
		Functions.removeItem(activeChar, 57, requiredAdena);
		activeChar.addExpAndSp(0, -1 * requiredSp);
		int levelPenalty = Rnd.get(Math.min(4, _skillLvl % 100));
		_skillLvl -= levelPenalty;
		if ((_skillLvl % 100) == 0)
		{
			_skillLvl = sl.getBaseLevel();
		}
		Skill skill = SkillTable.getInstance().getInfo(_skillId, SkillTreeTable.convertEnchantLevel(sl.getBaseLevel(), _skillLvl, sl.getMaxLevel()));
		if (skill != null)
		{
			activeChar.addSkill(skill, true);
		}
		if (levelPenalty == 0)
		{
			SystemMessage sm = new SystemMessage(SystemMessage.Enchant_skill_route_change_was_successful_Lv_of_enchant_skill_S1_will_remain);
			sm.addSkillName(_skillId, _skillLvl);
			activeChar.sendPacket(sm);
		}
		else
		{
			SystemMessage sm = new SystemMessage(SystemMessage.Enchant_skill_route_change_was_successful_Lv_of_enchant_skill_S1_has_been_decreased_by_S2);
			sm.addSkillName(_skillId, _skillLvl);
			sm.addNumber(levelPenalty);
			activeChar.sendPacket(sm);
		}
		Log.add(activeChar.getName() + "|Successfully changed route|" + _skillId + "|" + slevel + "|to+" + _skillLvl + "|" + levelPenalty, "enchant_skills");
		activeChar.sendPacket(new ExEnchantSkillInfo(_skillId, activeChar.getSkillDisplayLevel(_skillId)), new ExEnchantSkillResult(1));
		RequestExEnchantSkill.updateSkillShortcuts(activeChar, _skillId, _skillLvl);
	}
}
