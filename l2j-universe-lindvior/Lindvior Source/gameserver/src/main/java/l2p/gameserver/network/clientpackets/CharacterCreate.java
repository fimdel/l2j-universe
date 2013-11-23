/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.clientpackets;

import l2p.gameserver.Config;
import l2p.gameserver.dao.CharacterDAO;
import l2p.gameserver.instancemanager.QuestManager;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.actor.instances.player.ShortCut;
import l2p.gameserver.model.base.ClassId;
import l2p.gameserver.model.base.ClassLevel;
import l2p.gameserver.model.items.ItemInstance;
import l2p.gameserver.model.quest.Quest;
import l2p.gameserver.network.GameClient;
import l2p.gameserver.network.serverpackets.CharacterCreateFail;
import l2p.gameserver.network.serverpackets.CharacterCreateSuccess;
import l2p.gameserver.network.serverpackets.CharacterSelectionInfo;
import l2p.gameserver.templates.item.type.StartItem;
import l2p.gameserver.templates.player.PlayerTemplate;
import l2p.gameserver.utils.ItemFunctions;
import l2p.gameserver.utils.Util;

public class CharacterCreate extends L2GameClientPacket {
    // cSdddddddddddd
    private String _name;
    private int _sex;
    private int _classId;
    private int _hairStyle;
    private int _hairColor;
    private int _face;

    @Override
    protected void readImpl() {
        _name = readS();
        readD(); // race
        _sex = readD();
        _classId = readD();
        readD(); // int
        readD(); // str
        readD(); // con
        readD(); // men
        readD(); // dex
        readD(); // wit
        _hairStyle = readD();
        _hairColor = readD();
        _face = readD();
    }

    @Override
    protected void runImpl() {
        for (ClassId cid : ClassId.VALUES) {
            if ((cid.getId() == _classId) && !cid.isOfLevel(ClassLevel.NONE)) {
                return;
            }
        }
        if (CharacterDAO.getInstance().accountCharNumber(getClient().getLogin()) >= 8) {
            sendPacket(CharacterCreateFail.REASON_TOO_MANY_CHARACTERS);
            return;
        }
        if (!Util.isMatchingRegexp(_name, Config.CNAME_TEMPLATE)) {
            sendPacket(CharacterCreateFail.REASON_16_ENG_CHARS);
            return;
        } else if (CharacterDAO.getInstance().getObjectIdByName(_name) > 0) {
            sendPacket(CharacterCreateFail.REASON_NAME_ALREADY_EXISTS);
            return;
        }

        Player newChar = Player.create(_classId, _sex, getClient().getLogin(), _name, _hairStyle, _hairColor, _face);
        if (newChar == null) {
            return;
        }

        sendPacket(CharacterCreateSuccess.STATIC);

        initNewChar(getClient(), newChar);
    }

    private void initNewChar(GameClient client, Player newChar) {
        PlayerTemplate template = newChar.getTemplate();

        newChar.getSubClassList().restore();
        if (Config.STARTING_ADENA > 0) {
            newChar.addAdena(Config.STARTING_ADENA);
        }
        // newChar.setNoble(true);
        newChar.setLoc(template.getStartLocation());

        if (Config.CHAR_TITLE) {
            newChar.setTitle(Config.ADD_CHAR_TITLE);
        } else {
            newChar.setTitle("");
        }

        for (StartItem i : template.getStartItems()) {
            ItemInstance item = ItemFunctions.createItem(i.getItemId());
            long count = i.getCount();
            if (item.isStackable()) {
                item.setCount(count);
                newChar.getInventory().addItem(item);
            } else {
                for (long n = 0; n < count; n++) {
                    item = ItemFunctions.createItem(i.getItemId());
                    newChar.getInventory().addItem(item);
                }
                if (item.isEquipable() && i.isEquiped()) {
                    newChar.getInventory().equipItem(item);
                }
            }

            if (i._warehouse)
                newChar.getWarehouse().addItem(i._id, i._count);
            else {
                newChar.getInventory().addItem(i._id, i._count);
            }

            if (item.getItemId() == 5588) {
                newChar.registerShortCut(new ShortCut(11, 0, ShortCut.TYPE_ITEM, item.getObjectId(), -1, 1));
            }
        }

        newChar.rewardSkills(false);

        if (newChar.getSkillLevel(1001) > 0) {
            newChar.registerShortCut(new ShortCut(1, 0, ShortCut.TYPE_SKILL, 1001, 1, 1));
        }
        if (newChar.getSkillLevel(1177) > 0) {
            newChar.registerShortCut(new ShortCut(1, 0, ShortCut.TYPE_SKILL, 1177, 1, 1));
        }
        if (newChar.getSkillLevel(1216) > 0) {
            newChar.registerShortCut(new ShortCut(9, 0, ShortCut.TYPE_SKILL, 1216, 1, 1));
        }

        // add attack, take, sit shortcut
        newChar.registerShortCut(new ShortCut(0, 0, ShortCut.TYPE_ACTION, 2, -1, 1));
        newChar.registerShortCut(new ShortCut(3, 0, ShortCut.TYPE_ACTION, 5, -1, 1));
        newChar.registerShortCut(new ShortCut(10, 0, ShortCut.TYPE_ACTION, 0, -1, 1));
        // fly transform
        newChar.registerShortCut(new ShortCut(0, ShortCut.PAGE_FLY_TRANSFORM, ShortCut.TYPE_SKILL, 911, 1, 1));
        newChar.registerShortCut(new ShortCut(3, ShortCut.PAGE_FLY_TRANSFORM, ShortCut.TYPE_SKILL, 884, 1, 1));
        newChar.registerShortCut(new ShortCut(4, ShortCut.PAGE_FLY_TRANSFORM, ShortCut.TYPE_SKILL, 885, 1, 1));
        // air ship
        newChar.registerShortCut(new ShortCut(0, ShortCut.PAGE_AIRSHIP, ShortCut.TYPE_ACTION, 70, 0, 1));

        startTutorialQuest(newChar);

        newChar.setCurrentHpMp(newChar.getMaxHp(), newChar.getMaxMp());
        newChar.setCurrentCp(0); // retail
        newChar.setOnlineStatus(false);
        newChar.vitalityNewInit();

        newChar.store(false);
        newChar.getInventory().store();
        newChar.deleteMe();
        client.setCharSelection(CharacterSelectionInfo.loadCharacterSelectInfo(client.getLogin()));
    }

    public static void startTutorialQuest(Player player) {
        Quest q = QuestManager.getQuest(255);
        if (q != null) {
            q.newQuestState(player, Quest.CREATED);
        }
    }
}
