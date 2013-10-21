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
import lineage2.gameserver.model.actor.instances.player.ShortCut;
import lineage2.gameserver.model.base.EnchantSkillLearn;
import lineage2.gameserver.network.serverpackets.ExEnchantSkillInfo;
import lineage2.gameserver.network.serverpackets.ExEnchantSkillResult;
import lineage2.gameserver.network.serverpackets.ShortCutRegister;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.tables.SkillTable;
import lineage2.gameserver.tables.SkillTreeTable;
import lineage2.gameserver.utils.Log;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestExEnchantSkill extends L2GameClientPacket
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
		int slevel = activeChar.getSkillLevel(_skillId);
		if (slevel == -1)
		{
			return;
		}
		int enchantLevel = SkillTreeTable.convertEnchantLevel(sl.getBaseLevel(), _skillLvl, sl.getMaxLevel());
		if (slevel >= enchantLevel)
		{
			return;
		}
		if (slevel == sl.getBaseLevel() ? (_skillLvl % 100) != 1 : slevel != (enchantLevel - 1))
		{
			activeChar.sendMessage("Incorrect enchant level.");
			return;
		}
		Skill skill = SkillTable.getInstance().getInfo(_skillId, enchantLevel);
		if (skill == null)
		{
			activeChar.sendMessage("Internal error: not found skill level");
			return;
		}
		int[] cost = sl.getCost();
		int requiredSp = cost[1] * SkillTreeTable.NORMAL_ENCHANT_COST_MULTIPLIER * sl.getCostMult();
		int requiredAdena = cost[0] * SkillTreeTable.NORMAL_ENCHANT_COST_MULTIPLIER * sl.getCostMult();
		int rate = sl.getRate(activeChar);
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
		if ((_skillId < 10000) && ((_skillLvl % 100) == 1))
		{
			if (Functions.getItemCount(activeChar, SkillTreeTable.NORMAL_ENCHANT_BOOK) == 0)
			{
				activeChar.sendPacket(Msg.ITEMS_REQUIRED_FOR_SKILL_ENCHANT_ARE_INSUFFICIENT);
				return;
			}
			Functions.removeItem(activeChar, SkillTreeTable.NORMAL_ENCHANT_BOOK, 1);
		}
		else if ((_skillId >= 10000) && ((_skillLvl % 100) == 1))
		{
			if (Functions.getItemCount(activeChar, SkillTreeTable.NEW_ENCHANT_BOOK) == 0)
			{
				activeChar.sendPacket(Msg.ITEMS_REQUIRED_FOR_SKILL_ENCHANT_ARE_INSUFFICIENT);
				return;
			}
			Functions.removeItem(activeChar, SkillTreeTable.NEW_ENCHANT_BOOK, 1);
		}
		if (Rnd.chance(rate))
		{
			activeChar.addExpAndSp(0, -1 * requiredSp);
			Functions.removeItem(activeChar, 57, requiredAdena);
			activeChar.sendPacket(new SystemMessage(SystemMessage.SP_HAS_DECREASED_BY_S1).addNumber(requiredSp), new SystemMessage(SystemMessage.SUCCEEDED_IN_ENCHANTING_SKILL_S1).addSkillName(_skillId, _skillLvl), new ExEnchantSkillResult(1));
			Log.add(activeChar.getName() + "|Successfully enchanted|" + _skillId + "|to+" + _skillLvl + "|" + rate, "enchant_skills");
		}
		else
		{
			skill = SkillTable.getInstance().getInfo(_skillId, sl.getBaseLevel());
			activeChar.sendPacket(new SystemMessage(SystemMessage.FAILED_IN_ENCHANTING_SKILL_S1).addSkillName(_skillId, _skillLvl), new ExEnchantSkillResult(0));
			Log.add(activeChar.getName() + "|Failed to enchant|" + _skillId + "|to+" + _skillLvl + "|" + rate, "enchant_skills");
		}
		activeChar.addSkill(skill, true);
		activeChar.sendSkillList();
		updateSkillShortcuts(activeChar, _skillId, _skillLvl);
		activeChar.sendPacket(new ExEnchantSkillInfo(_skillId, activeChar.getSkillDisplayLevel(_skillId)));
	}
	
	/**
	 * Method updateSkillShortcuts.
	 * @param player Player
	 * @param skillId int
	 * @param skillLevel int
	 */
	protected static void updateSkillShortcuts(Player player, int skillId, int skillLevel)
	{
		for (ShortCut sc : player.getAllShortCuts())
		{
			if ((sc.getId() == skillId) && (sc.getType() == ShortCut.TYPE_SKILL))
			{
				ShortCut newsc = new ShortCut(sc.getSlot(), sc.getPage(), sc.getType(), sc.getId(), skillLevel, 1);
				player.sendPacket(new ShortCutRegister(player, newsc));
				player.registerShortCut(newsc);
			}
		}
	}
}
