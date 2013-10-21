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

import lineage2.gameserver.Config;
import lineage2.gameserver.dao.CharacterDAO;
import lineage2.gameserver.instancemanager.QuestManager;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.actor.instances.player.ShortCut;
import lineage2.gameserver.model.base.ClassId;
import lineage2.gameserver.model.base.ClassLevel;
import lineage2.gameserver.model.base.Experience;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.network.GameClient;
import lineage2.gameserver.network.serverpackets.CharacterCreateFail;
import lineage2.gameserver.network.serverpackets.CharacterCreateSuccess;
import lineage2.gameserver.network.serverpackets.CharacterSelectionInfo;
import lineage2.gameserver.templates.item.StartItem;
import lineage2.gameserver.templates.player.PlayerTemplate;
import lineage2.gameserver.utils.ItemFunctions;
import lineage2.gameserver.utils.Util;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class CharacterCreate extends L2GameClientPacket
{
	/**
	 * Field _name.
	 */
	private String _name;
	/**
	 * Field _sex.
	 */
	private int _sex;
	/**
	 * Field _classId.
	 */
	private int _classId;
	/**
	 * Field _hairStyle.
	 */
	private int _hairStyle;
	/**
	 * Field _hairColor.
	 */
	private int _hairColor;
	/**
	 * Field _face.
	 */
	private int _face;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_name = readS();
		readD();
		_sex = readD();
		_classId = readD();
		readD();
		readD();
		readD();
		readD();
		readD();
		readD();
		_hairStyle = readD();
		_hairColor = readD();
		_face = readD();
	}
	
	/**
	 * Method runImpl.
	 */
	@Override
	protected void runImpl()
	{
		for (ClassId cid : ClassId.VALUES)
		{
			if ((cid.getId() == _classId) && !cid.isOfLevel(ClassLevel.First))
			{
				return;
			}
		}
		if (CharacterDAO.getInstance().accountCharNumber(getClient().getLogin()) >= 8)
		{
			sendPacket(CharacterCreateFail.REASON_TOO_MANY_CHARACTERS);
			return;
		}
		if (!Util.isMatchingRegexp(_name, Config.CNAME_TEMPLATE))
		{
			sendPacket(CharacterCreateFail.REASON_16_ENG_CHARS);
			return;
		}
		else if (CharacterDAO.getInstance().getObjectIdByName(_name) > 0)
		{
			sendPacket(CharacterCreateFail.REASON_NAME_ALREADY_EXISTS);
			return;
		}
		Player newChar = Player.create(_classId, _sex, getClient().getLogin(), _name, _hairStyle, _hairColor, _face);
		if (newChar == null)
		{
			return;
		}
		sendPacket(CharacterCreateSuccess.STATIC);
		initNewChar(getClient(), newChar);
	}
	
	/**
	 * Method initNewChar.
	 * @param client GameClient
	 * @param newChar Player
	 */
	private void initNewChar(GameClient client, Player newChar)
	{
		PlayerTemplate template = newChar.getTemplate();
		newChar.getSubClassList().restore();
		if (Config.STARTING_ADENA > 0)
		{
			newChar.addAdena(Config.STARTING_ADENA);
		}
		newChar.setLoc(template.getStartLocation());
		if (Config.CHAR_TITLE)
		{
			newChar.setTitle(Config.ADD_CHAR_TITLE);
		}
		else
		{
			newChar.setTitle("");
		}
		for (StartItem i : template.getStartItems())
		{
			ItemInstance item = ItemFunctions.createItem(i.getItemId());
			long count = i.getCount();
			if (item.isStackable())
			{
				item.setCount(count);
				newChar.getInventory().addItem(item);
			}
			else
			{
				for (long n = 0; n < count; n++)
				{
					item = ItemFunctions.createItem(i.getItemId());
					newChar.getInventory().addItem(item);
				}
				if (item.isEquipable() && i.isEquiped())
				{
					newChar.getInventory().equipItem(item);
				}
			}
			if (item.getItemId() == 5588)
			{
				newChar.registerShortCut(new ShortCut(11, 0, ShortCut.TYPE_ITEM, item.getObjectId(), -1, 1));
			}
		}
		newChar.rewardSkills(false,false);
		if (newChar.getSkillLevel(1001) > 0)
		{
			newChar.registerShortCut(new ShortCut(1, 0, ShortCut.TYPE_SKILL, 1001, 1, 1));
		}
		if (newChar.getSkillLevel(1177) > 0)
		{
			newChar.registerShortCut(new ShortCut(1, 0, ShortCut.TYPE_SKILL, 1177, 1, 1));
		}
		if (newChar.getSkillLevel(1216) > 0)
		{
			newChar.registerShortCut(new ShortCut(9, 0, ShortCut.TYPE_SKILL, 1216, 1, 1));
		}
		newChar.registerShortCut(new ShortCut(0, 0, ShortCut.TYPE_ACTION, 2, -1, 1));
		newChar.registerShortCut(new ShortCut(3, 0, ShortCut.TYPE_ACTION, 5, -1, 1));
		newChar.registerShortCut(new ShortCut(10, 0, ShortCut.TYPE_ACTION, 0, -1, 1));
		newChar.registerShortCut(new ShortCut(0, ShortCut.PAGE_FLY_TRANSFORM, ShortCut.TYPE_SKILL, 911, 1, 1));
		newChar.registerShortCut(new ShortCut(3, ShortCut.PAGE_FLY_TRANSFORM, ShortCut.TYPE_SKILL, 884, 1, 1));
		newChar.registerShortCut(new ShortCut(4, ShortCut.PAGE_FLY_TRANSFORM, ShortCut.TYPE_SKILL, 885, 1, 1));
		newChar.registerShortCut(new ShortCut(0, ShortCut.PAGE_AIRSHIP, ShortCut.TYPE_ACTION, 70, 0, 1));
		startTutorialQuest(newChar);
		if ((Config.STARTING_LEVEL > 1) && (Config.STARTING_LEVEL <= Experience.getMaxLevel()))
		{
			newChar.addExpAndSp(Experience.LEVEL[(Config.STARTING_LEVEL + 1)] - 1L, 0L);
		}
		newChar.setCurrentHpMp(newChar.getMaxHp(), newChar.getMaxMp());
		newChar.setCurrentCp(0);
		newChar.setOnlineStatus(false);
		newChar.restoreVitality();
		newChar.setVar("isPlayerBuff", "1", -1);
		newChar.store(false);
		newChar.getInventory().store();
		newChar.deleteMe();
		client.setCharSelection(CharacterSelectionInfo.loadCharacterSelectInfo(client.getLogin()));
	}
	
	/**
	 * Method startTutorialQuest.
	 * @param player Player
	 */
	public static void startTutorialQuest(Player player)
	{
		Quest q = QuestManager.getQuest(255);
		if (q != null)
		{
			q.newQuestState(player, Quest.CREATED);
		}
	}
}
