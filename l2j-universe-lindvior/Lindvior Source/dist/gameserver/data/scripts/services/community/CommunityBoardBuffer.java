/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package services.community;

import gnu.trove.list.array.TIntArrayList;
import javolution.text.TextBuilder;
import l2p.commons.dbutils.DbUtils;
import l2p.gameserver.Config;
import l2p.gameserver.ThreadPoolManager;
import l2p.gameserver.data.htm.HtmCache;
import l2p.gameserver.database.DatabaseFactory;
import l2p.gameserver.handler.bbs.CommunityBoardHandler;
import l2p.gameserver.handler.bbs.ICommunityBoardHandler;
import l2p.gameserver.model.Effect;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.Skill;
import l2p.gameserver.model.Summon;
import l2p.gameserver.model.Zone.ZoneType;
import l2p.gameserver.network.serverpackets.ShowBoard;
import l2p.gameserver.scripts.ScriptFile;
import l2p.gameserver.skills.EffectType;
import l2p.gameserver.skills.effects.EffectTemplate;
import l2p.gameserver.stats.Env;
import l2p.gameserver.tables.SkillTable;
import l2p.gameserver.utils.GCSArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.StringTokenizer;
import java.util.concurrent.ScheduledFuture;

public class CommunityBoardBuffer implements ScriptFile, ICommunityBoardHandler {
    private static final TIntArrayList allowed_buff = new TIntArrayList(new int[]{
            1032, 1033, 1035, 1036, 1040,
            1044, 1045, 1048, 1059, 1062,
            1068, 1073, 1077, 1078, 1085,
            1086, 1087, 1182, 1189, 1191,
            1204, 1240, 1242, 1243, 1257,
            1268, 1284, 1303, 1304, 1307,
            1352, 1353, 1354, 1355, 1356,
            1357, 1363, 1388, 1389, 1392,
            1393, 1397, 1413, 1460, 1461,
            1499, 1500, 1501, 1502, 1503,
            1504, 1519, 4699, 4700, 4702,
            4703, 264, 265, 266, 267,
            268, 269, 270, 271, 272,
            273, 274, 275, 276, 277,
            304, 305, 306, 307, 308,
            309, 310, 311, 349, 363,
            364, 365, 366, 529, 530,
            764, 825, 826, 827, 828,
            829, 830, 1043, 1259, 1542,
            1548, 1549, 914, 915, 6429,
            11517, 11518, 11519, 11520, 11521,
            11522, 11523, 11524, 11525, 11529,
            11530, 11532, 11570, 11571, 1586,
            1588, 1590, 1592, 1594, 1596,
            1599, 11566, 11565, 11567, 1364});
    private static final Logger _log = LoggerFactory.getLogger(CommunityBoardBuffer.class);

    /**
     * Имплементированые методы скриптов
     */
    @Override
    public void onLoad() {
        if (Config.COMMUNITYBOARD_ENABLED) {
            _log.info("CommunityBoard: Manage Buffer service loaded.");
            CommunityBoardHandler.getInstance().registerHandler(this);
        }
    }

    @Override
    public void onReload() {
        if (Config.COMMUNITYBOARD_ENABLED)
            CommunityBoardHandler.getInstance().removeHandler(this);
    }

    @Override
    public void onShutdown() {
    }

    public class CBBuffGroups {
        public int GpId = 0; // ID группы
        public String GpName = ""; // Имя группы
        public int PlayerId = 0; // ID владельца
    }

    public class CBBuffGroup {
        public int OneId = 0; // Уникальный ID группы в таблице bbs_buffer
        public int OneGpId = 0; // ID группы
        public int OnePlayerId = 0; // ID владельца
        public int OneBuffId = 0; // ID баффа
    }

    public class CBBuffAllowedBuffs {
        public int BuffLvL = 0; // LvL баффа
        public int BuffId = 0; // ID баффа
    }

    /**
     * Регистратор команд
     */
    @Override
    public String[] getBypassCommands() {
        return new String[]{
                "_bbsbuff",
                "_bbsbuff;reghp",
                "_bbsbuff;regmp",
                "_bbsbuff;regcp",
                "_bbsbuff;cancel",
                "_bbsbuff;buff",
                "_bbsbuff;grpbuff",
                "_bbsbuff;fixedgrpbuff",
                "_bbsbuff;addgrp",
                "_bbsbuff;dellGrp",
                "_bbsbuff;use",
                "_bbsbuff;usefixed",
                "_bbsbuff;editegrp",
                "_bbsbuff;addbuffin",
                "_bbsbuff;fingrpaddbuf",
                "_bbsbuff;dellbufffrom",
                "_bbsbuff;autobuffgrp",
                "_bbsbuff;stopautobuffgrp",
                "_bbsbuff;petautobuffgrp",
                "_bbsbuff;petstopbuffgrpauto"};
    }

    @Override
    public void onBypassCommand(Player activeChar, String command) {
        activeChar.setSessionVar("add_fav", null);

        if (command.equals("_bbsbuff"))
            showBuffIndexPage(activeChar);
        else if (command.startsWith("_bbsbuff;buff")) {
            StringTokenizer st = new StringTokenizer(command, " ");
            st.nextToken();
            int BuffIdUse = Integer.parseInt(st.nextToken());
            int BuffLvL = Integer.parseInt(st.nextToken());
            BuffOne(activeChar, BuffIdUse, BuffLvL);
            showBuffIndexPage(activeChar);
        } else if (command.startsWith("_bbsbuff;grpbuff")) {
            StringTokenizer st = new StringTokenizer(command, " ");
            st.nextToken();
            int BuffIdUse = Integer.parseInt(st.nextToken());
            String target = st.nextToken();
            BuffGrp(activeChar, BuffIdUse, target);
            showBuffIndexPage(activeChar);
        } else if (command.startsWith("_bbsbuff;fixedgrpbuff")) {
            StringTokenizer st = new StringTokenizer(command, " ");
            st.nextToken();
            String GrpName = st.nextToken();
            String target = st.nextToken();
            BuffFixedGrp(activeChar, GrpName, target);
            showBuffIndexPage(activeChar);
        } else if (command.startsWith("_bbsbuff;addgrp")) {
            StringTokenizer st = new StringTokenizer(command, " ");
            st.nextToken();
            String GrpNameAdd = st.nextToken();
            AddBuffGrp(activeChar, GrpNameAdd);
            showBuffIndexPage(activeChar);
        } else if (command.startsWith("_bbsbuff;dellGrp")) {
            StringTokenizer st = new StringTokenizer(command, " ");
            st.nextToken();
            int GpNameDell = Integer.parseInt(st.nextToken());
            DellBuffGrp(activeChar, GpNameDell);
            showBuffIndexPage(activeChar);
        } else if (command.startsWith("_bbsbuff;use")) {
            StringTokenizer st = new StringTokenizer(command, " ");
            st.nextToken();
            int GpIdUse = Integer.parseInt(st.nextToken());
            UseBuffGrp(activeChar, GpIdUse);
        } else if (command.startsWith("_bbsbuff;usefixed")) {
            StringTokenizer st = new StringTokenizer(command, " ");
            st.nextToken();
            String GrpName = st.nextToken();
            int page = Integer.parseInt(st.nextToken());
            UseFixedBuffGrp(activeChar, GrpName, page);
        } else if (command.startsWith("_bbsbuff;editegrp")) {
            StringTokenizer st = new StringTokenizer(command, " ");
            st.nextToken();
            int GpIdUse = Integer.parseInt(st.nextToken());
            EditeBuffGrp(activeChar, GpIdUse);
        } else if (command.startsWith("_bbsbuff;addbuffin")) {
            StringTokenizer st = new StringTokenizer(command, " ");
            st.nextToken();
            int GpIdUse = Integer.parseInt(st.nextToken());
            int page = Integer.parseInt(st.nextToken());
            EditeAddBuffInGrp(activeChar, GpIdUse, page);
        } else if (command.startsWith("_bbsbuff;fingrpaddbuf")) {
            StringTokenizer st = new StringTokenizer(command, " ");
            st.nextToken();
            int GpIdUse = Integer.parseInt(st.nextToken());
            int Buff = Integer.parseInt(st.nextToken());
            int BuffLvL = Integer.parseInt(st.nextToken());
            AddBuffInGrp(activeChar, GpIdUse, Buff, BuffLvL, 1);
        } else if (command.startsWith("_bbsbuff;dellbufffrom")) {
            StringTokenizer st = new StringTokenizer(command, " ");
            st.nextToken();
            int GpIdUse = Integer.parseInt(st.nextToken());
            int Buff = Integer.parseInt(st.nextToken());
            DellBuffFromGrp(activeChar, GpIdUse, Buff);
        } else if (command.startsWith("_bbsbuff;autobuffgrp")) {
            StringTokenizer st = new StringTokenizer(command, " ");
            st.nextToken();
            int GpIdUse = Integer.parseInt(st.nextToken());
            String GpNameUse = st.nextToken();
            int price = Integer.parseInt(st.nextToken());
            StartAutoBuff(activeChar, GpIdUse, GpNameUse, price);// Запуск задачи
        } else if (command.startsWith("_bbsbuff;stopautobuffgrp")) {
            StringTokenizer st = new StringTokenizer(command, " ");
            st.nextToken();
            int GpIdUse = Integer.parseInt(st.nextToken());
            String GpNameUse = st.nextToken();
            StopAutoBuff(activeChar, GpIdUse, GpNameUse);// Остановка задачи
        } else if (command.startsWith("_bbsbuff;petautobuffgrp")) {
            StringTokenizer st = new StringTokenizer(command, " ");
            st.nextToken();
            int GpIdUse = Integer.parseInt(st.nextToken());
            String GpNameUse = st.nextToken();
            int price = Integer.parseInt(st.nextToken());
            StartAutoBuffPet(activeChar, GpIdUse, GpNameUse, price);// Запуск задачи
        } else if (command.startsWith("_bbsbuff;petstopbuffgrpauto")) {
            StringTokenizer st = new StringTokenizer(command, " ");
            st.nextToken();
            int GpIdUse = Integer.parseInt(st.nextToken());
            String GpNameUse = st.nextToken();
            StopAutoBuffPet(activeChar, GpIdUse, GpNameUse);// Остановка задачи
        } else if (command.startsWith("_bbsbuff;regmp")) {
            if (activeChar.isDead() || activeChar.isAlikeDead() || activeChar.isCastingNow() || activeChar.isInCombat() || activeChar.isAttackingNow() || activeChar.isInOlympiadMode() || activeChar.isFlying() || activeChar.isTerritoryFlagEquipped() || !Config.BBS_PVP_BUFFER_ALLOW_SIEGE && activeChar.isInZone(ZoneType.SIEGE)) {
                activeChar.sendMessage(activeChar.isLangRus() ? "Невозможно восстановить MP в данный момент!" : "It is impossible to restore the MP at the moment!");
                showBuffIndexPage(activeChar);
                return;
            } else {
                activeChar.setCurrentMp(activeChar.getMaxMp());
                showBuffIndexPage(activeChar);
            }
        } else if (command.startsWith("_bbsbuff;reghp")) {
            if (activeChar.isDead() || activeChar.isAlikeDead() || activeChar.isCastingNow() || activeChar.isInCombat() || activeChar.isAttackingNow() || activeChar.isInOlympiadMode() || activeChar.isFlying() || activeChar.isTerritoryFlagEquipped() || !Config.BBS_PVP_BUFFER_ALLOW_SIEGE && activeChar.isInZone(ZoneType.SIEGE)) {
                activeChar.sendMessage(activeChar.isLangRus() ? "Невозможно восстановить HP в данный момент!" : "It is impossible to restore the HP at the moment!");
                showBuffIndexPage(activeChar);
                return;
            } else {
                activeChar.setCurrentHp(activeChar.getMaxHp(), true);
                showBuffIndexPage(activeChar);
            }
        } else if (command.startsWith("_bbsbuff;regcp")) {
            if (activeChar.isDead() || activeChar.isAlikeDead() || activeChar.isCastingNow() || activeChar.isInCombat() || activeChar.isAttackingNow() || activeChar.isInOlympiadMode() || activeChar.isFlying() || activeChar.isTerritoryFlagEquipped() || Config.BBS_PVP_BUFFER_ALLOW_SIEGE == false && activeChar.isInZone(ZoneType.SIEGE)) {
                activeChar.sendMessage(activeChar.isLangRus() ? "Невозможно восстановить CP в данный момент!" : "It is impossible to restore the CP at the moment!");
                showBuffIndexPage(activeChar);
                return;
            } else {
                activeChar.setCurrentCp(activeChar.getMaxCp());
                showBuffIndexPage(activeChar);
            }
        } else if (command.startsWith("_bbsbuff;cancel")) {
            if (!activeChar.isDead() || !activeChar.isAlikeDead() || !activeChar.isCastingNow() || !activeChar.isInCombat() || !activeChar.isAttackingNow() || !activeChar.isInOlympiadMode() || !activeChar.isFlying() || !activeChar.isTerritoryFlagEquipped() || Config.BBS_PVP_BUFFER_ALLOW_SIEGE == true && activeChar.isInZone(ZoneType.SIEGE) && activeChar.getEffectList().getEffectsBySkillId(Skill.SKILL_RAID_CURSE) == null && activeChar.getEffectList().getEffectByType(EffectType.Debuff) == null) {
                activeChar.getEffectList().stopAllEffects();
                showBuffIndexPage(activeChar);
            } else if (!activeChar.isDead() || !activeChar.isAlikeDead() || !activeChar.isCastingNow() || !activeChar.isInCombat() || !activeChar.isAttackingNow() || !activeChar.isInOlympiadMode() || !activeChar.isFlying() || !activeChar.isTerritoryFlagEquipped() || Config.BBS_PVP_BUFFER_ALLOW_SIEGE == true && activeChar.isInZone(ZoneType.SIEGE) && activeChar.getPet() != null) {
                activeChar.getPet().getEffectList().stopAllEffects();
                showBuffIndexPage(activeChar);
            }
        } else if (activeChar.isLangRus())
            ShowBoard.separateAndSend("<html><body><br><br><center>На данный момент функция: " + command + " пока не реализована</center><br><br>", activeChar);
        else
            ShowBoard.separateAndSend("<html><body><br><br><center>At the moment the function: " + command + " not implemented yet</center><br><br></body></html>", activeChar);
    }

    /**
     * Бафаем группу баффов составленную администратором сервера.<br>
     * Формируется переменная со списком всех баффов из группы и передается в
     * StartBuffGrp
     *
     * @param
     */
    private void BuffFixedGrp(Player activeChar, String GrpName, String target) {
        Connection con = null;
        CBBuffGroup bgrp;
        String allbuff = "";
        int price = 0;
        GCSArray<String> skillIds = new GCSArray<String>();
        try {
            con = DatabaseFactory.getInstance().getConnection();
            PreparedStatement statement = con.prepareStatement("SELECT * FROM bbs_buffer_allowed_buffs WHERE (locate( ? ,skillgrp)>0)");
            statement.setString(1, GrpName);
            ResultSet rcln = statement.executeQuery();

            while (rcln.next()) {
                bgrp = new CBBuffGroup();
                bgrp.OneBuffId = rcln.getInt("skillID");
                skillIds.add("" + bgrp.OneBuffId + "");
            }
            DbUtils.closeQuietly(statement, rcln);

            for (int j = 0; j < skillIds.size(); j++)
                allbuff = new StringBuilder().append(allbuff).append(skillIds.get(j) + ";").toString();

            if (!chekCondition(activeChar))
                return;

            if (activeChar.getLevel() > Config.CBB_BUFFER_FREE_LEVEL)
                price = skillIds.size() * Config.BBS_PVP_BUFFER_PRICE_ONE;
            else
                price = 0;

            if (price > 0 && activeChar.getAdena() < price) {
                activeChar.sendMessage(activeChar.isLangRus() ? "Недостаточно денег." : "It is not enough money.");
                return;
            }

            if (price > 0)
                activeChar.reduceAdena(price, true);

            StartBuffGrp(activeChar, allbuff, target);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(con);
        }
    }

    /**
     * Бафаем группу баффов.<br>
     * Формируется переменная со списком всех баффов из группы и передается в
     * StartBuffGrp
     *
     * @param
     */
    private void BuffGrp(Player activeChar, int buffIdUse, String target) {
        Connection con = null;
        CBBuffGroup bgrp;
        String allbuff = "";
        int price = 0;
        GCSArray<String> skillIds = new GCSArray<String>();
        try {
            con = DatabaseFactory.getInstance().getConnection();
            PreparedStatement statement = con.prepareStatement("SELECT * FROM bbs_buffer_buffs WHERE GpId=?;");
            statement.setInt(1, buffIdUse);
            ResultSet rcln = statement.executeQuery();

            while (rcln.next()) {
                bgrp = new CBBuffGroup();
                bgrp.OneBuffId = rcln.getInt("buffid");
                skillIds.add("" + bgrp.OneBuffId + "");
            }
            DbUtils.closeQuietly(statement, rcln);

            for (int j = 0; j < skillIds.size(); j++)
                allbuff = new StringBuilder().append(allbuff).append(skillIds.get(j) + ";").toString();

            if (!chekCondition(activeChar))
                return;

            if (activeChar.getLevel() > Config.CBB_BUFFER_FREE_LEVEL)
                price = skillIds.size() * Config.BBS_PVP_BUFFER_PRICE_ONE;
            else
                price = 0;

            if (price > 0 && activeChar.getAdena() < price) {
                activeChar.sendMessage(activeChar.isLangRus() ? "Недостаточно денег." : "It is not enough money.");
                return;
            }

            if (price > 0)
                activeChar.reduceAdena(price, true);

            StartBuffGrp(activeChar, allbuff, target);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(con);
        }
    }

    /**
     * Запуск баффа для группы.
     *
     * @param allbuff - переменная со списком всех баффов из группы. Значение
     *                устанавливается в методах BuffGrp и BuffFixedGrp
     * @param
     */
    private void StartBuffGrp(Player activeChar, String allbuff, String target) {
        Connection con = null;
        Skill skill = null;
        Skill skillTmp = null;
        Summon pet = activeChar.getSummonList().getPet();
        StringTokenizer stBuff = new StringTokenizer(allbuff, ";");
        while (stBuff.hasMoreTokens()) {
            int skilltoresatore = Integer.parseInt(stBuff.nextToken());
            skillTmp = SkillTable.getInstance().getInfo(skilltoresatore, 1);
            skill = SkillTable.getInstance().getInfo(skilltoresatore, skillTmp.getBaseLevel());

            try {
                con = DatabaseFactory.getInstance().getConnection();
                PreparedStatement st = con.prepareStatement("SELECT COUNT(*) FROM bbs_buffer_allowed_buffs WHERE skillID=?;");
                st.setInt(1, skilltoresatore);
                ResultSet rs = st.executeQuery();
                if (rs.next() && rs.getInt(1) != 0) {
                    if (target.startsWith("Player"))
                        for (EffectTemplate et : skill.getEffectTemplates()) {
                            Env env = new Env(activeChar, activeChar, skill);
                            Effect effect = et.getEffect(env);
                            effect.setPeriod(Config.BBS_PVP_BUFFER_ALT_TIME);
                            activeChar.getEffectList().addEffect(effect);
                        }
                    if (target.startsWith("Pet")) {
                        if (pet == null)
                            return;

                        for (EffectTemplate et : skill.getEffectTemplates()) {
                            Env env = new Env(pet, pet, skill);
                            Effect effect = et.getEffect(env);
                            effect.setPeriod(Config.BBS_PVP_BUFFER_ALT_TIME);
                            pet.getEffectList().addEffect(effect);
                        }
                    }
                    DbUtils.closeQuietly(st, rs);
                } else if (activeChar.isLangRus())
                    activeChar.sendMessage("Бафф: " + skill.getName() + " (" + skill.getId() + "), не может быть использован!");
                else
                    activeChar.sendMessage("Buff: " + skill.getName() + " (" + skill.getId() + "), can not be used!");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                DbUtils.closeQuietly(con);
            }
        }
    }

    /**
     * Бафаем один бафф
     *
     * @param
     */
    private void BuffOne(Player activeChar, int buffIdUse, int buffLvL) {
        Summon pet = activeChar.getSummonList().getPet();
        if (!chekCondition(activeChar))
            return;

        if (Config.BBS_PVP_BUFFER_PRICE_ONE > 0 && activeChar.getAdena() < Config.BBS_PVP_BUFFER_PRICE_ONE) {
            activeChar.sendMessage(activeChar.isLangRus() ? "Недостаточно денег." : "It is not enough money.");
            return;
        }

        if (Config.BBS_PVP_BUFFER_PRICE_ONE > 0 && activeChar.getLevel() > Config.CBB_BUFFER_FREE_LEVEL)
            activeChar.reduceAdena(Config.BBS_PVP_BUFFER_PRICE_ONE, true);

        if (allowed_buff.contains(buffIdUse)) {
            Skill skill = SkillTable.getInstance().getInfo(buffIdUse, buffLvL);
            for (EffectTemplate et : skill.getEffectTemplates()) {
                Env env = new Env(activeChar, activeChar, skill);
                Effect effect = et.getEffect(env);
                effect.setPeriod(Config.BBS_PVP_BUFFER_ALT_TIME);
                activeChar.getEffectList().addEffect(effect);
                if (Config.BBS_PVP_BUFER_ONE_BUFF_PET && activeChar.getPet() != null)
                    pet.getEffectList().addEffect(effect);
            }
        } else {
            activeChar.sendMessage(activeChar.isLangRus() ? "Ну и глупый же ты..." : "You are so stupid...");
            _log.warn("Player: " + activeChar + " used not allow buff: " + buffIdUse + " - Player: " + activeChar + " BANNED!!!");
            activeChar.setAccessLevel(-100); // Без временный бан на плеера
            activeChar.kick(); // Выбрасываем плеера из мира
        }
    }

    /**
     * Удаляем бафф из набора и возвращаем список.
     *
     * @param
     */
    private void DellBuffFromGrp(Player activeChar, int gpIdUse, int buff) {
        Connection conDel = null;
        try {
            conDel = DatabaseFactory.getInstance().getConnection();
            PreparedStatement stDel = conDel.prepareStatement("DELETE FROM bbs_buffer_buffs WHERE GpId=? AND buffid=?;");
            stDel.setInt(1, gpIdUse);
            stDel.setInt(2, buff);
            stDel.execute();
            DbUtils.closeQuietly(stDel);
            EditeBuffGrp(activeChar, gpIdUse);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(conDel);
        }

    }

    /**
     * Добавляем бафф в набор и возвращаем список.
     *
     * @param
     */
    private void AddBuffInGrp(Player player, int gpIdUse, int buff, int lvl, int page) {
        Connection con = null;
        TextBuilder html = new TextBuilder();
        TextBuilder htmltoppanel = new TextBuilder();
        CBBuffGroups bgrpOne;
        CBBuffAllowedBuffs bgrpAlw;
        String GrpName = null;
        int GrpID = 0;
        double numpages = 0;
        GCSArray<String> AlwBuffIds = new GCSArray<String>();
        int offset = Config.BBS_PVP_BUFFER_BUFFS_PER_PAGE * (page - 1);
        try {
            con = DatabaseFactory.getInstance().getConnection();

            PreparedStatement st = con.prepareStatement("SELECT COUNT(*) FROM bbs_buffer_buffs WHERE charId=? AND GpId=?;");
            st.setInt(1, player.getObjectId());
            st.setInt(2, gpIdUse);
            ResultSet rs = st.executeQuery();
            rs.next();
            if (rs.getInt(1) <= 27) {
                PreparedStatement stAdd = con.prepareStatement("INSERT INTO bbs_buffer_buffs (charId,GpId,buffid,bufflvl) VALUES (?,?,?,?)");
                stAdd.setInt(1, player.getObjectId());
                stAdd.setInt(2, gpIdUse);
                stAdd.setInt(3, buff);
                stAdd.setInt(4, lvl);
                stAdd.execute();
                DbUtils.closeQuietly(stAdd);
            } else
                player.sendMessage(player.isLangRus() ? "Набор не может содержать более " + Config.BBS_PVP_BUFFER_BUFFS_PER_PAGE + " баффов" : "Set can not contain more than " + Config.BBS_PVP_BUFFER_BUFFS_PER_PAGE + " buffs");
            DbUtils.closeQuietly(st, rs);

            con = DatabaseFactory.getInstance().getConnection();

            PreparedStatement stC = con.prepareStatement("SELECT * FROM bbs_buffer WHERE GpId=?;");
            stC.setInt(1, gpIdUse);
            ResultSet rsC = stC.executeQuery();

            while (rsC.next()) {
                bgrpOne = new CBBuffGroups();
                bgrpOne.GpId = rsC.getInt("GpId");
                bgrpOne.GpName = rsC.getString("GpName");
                bgrpOne.PlayerId = rsC.getInt("charId");
                GrpName = bgrpOne.GpName;
                GrpID = bgrpOne.GpId;
            }
            DbUtils.closeQuietly(stC, rsC);

            PreparedStatement stAlwCount = con.prepareStatement("SELECT COUNT(*) FROM bbs_buffer_allowed_buffs");
            ResultSet rsAlwCount = stAlwCount.executeQuery();
            rsAlwCount.next();
            if (rsAlwCount.getInt(1) > Config.BBS_PVP_BUFFER_BUFFS_PER_PAGE - 1)
                numpages = Math.ceil((double) rsAlwCount.getInt(1) / Config.BBS_PVP_BUFFER_BUFFS_PER_PAGE);
            DbUtils.closeQuietly(stAlwCount, rsAlwCount);

            PreparedStatement stAlw = con.prepareStatement("SELECT * FROM bbs_buffer_allowed_buffs WHERE NOT EXISTS (SELECT * FROM bbs_buffer_buffs WHERE bbs_buffer_buffs.buffid = bbs_buffer_allowed_buffs.skillID AND bbs_buffer_buffs.GpId =?) LIMIT " + offset + ", " + Config.BBS_PVP_BUFFER_BUFFS_PER_PAGE + "");
            stAlw.setInt(1, gpIdUse);
            ResultSet rsAlw = stAlw.executeQuery();
            while (rsAlw.next()) {
                bgrpAlw = new CBBuffAllowedBuffs();
                bgrpAlw.BuffId = rsAlw.getInt("skillID");
                bgrpAlw.BuffLvL = rsAlw.getInt("skillLvl");
                AlwBuffIds.add("" + bgrpAlw.BuffId + "");
            }
            DbUtils.closeQuietly(stAlw, rsAlw);

            html.append(buildTable(player, AlwBuffIds, 4, 3, gpIdUse, null));
            htmltoppanel.append("<table width=700>");
            htmltoppanel.append("<tr>");
            htmltoppanel.append(player.isLangRus() ? "<td>Редактирование шаблона: " + GrpName + "</td>" : "<td>Edit the template: " + GrpName + "</td>");
            htmltoppanel.append("<td></td>");
            htmltoppanel.append(page_list(numpages, page, GrpID, null));
            if (player.isLangRus())
                htmltoppanel.append("<td><button value=\"Назад\" action=\"bypass _bbsbuff;use " + GrpID + "\" width=50 height=20 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
            else
                htmltoppanel.append("<td><button value=\"Back\" action=\"bypass _bbsbuff;use " + GrpID + "\" width=50 height=20 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
            htmltoppanel.append("</tr>");
            htmltoppanel.append("</table>");

            String content = HtmCache.getInstance().getNotNull("scripts/services/community/" + Config.BBS_FOLDER + "/buffer/group.htm", player); // 601
            content = content.replace("%buffgrptoppanel%", htmltoppanel.toString());
            content = content.replace("%buffgrp%", html.toString());
            content = content.replace("<?cb_name?>", Config.COMMUNITYBOARD_NAME);
            content = content.replace("<?cb_copy?>", Config.COMMUNITYBOARD_COPY);
            content = content.replace("%bbs_menu%", HtmCache.getInstance().getNotNull("scripts/services/community/" + Config.BBS_FOLDER + "/bbs_menu.htm", player).toString());
            ShowBoard.separateAndSend(content, player);
        } catch (Exception e) {
        } finally {
            DbUtils.closeQuietly(con);
        }
    }

    /**
     * Страница редактора набора. Список баффов которые можно добавить в набор.
     *
     * @param
     */
    private void EditeAddBuffInGrp(Player player, int gpIdUse, int page) {
        CBBuffAllowedBuffs bgrpAlw;
        CBBuffGroups bgrpOne;
        String GrpName = null;
        int GrpID = 0;
        double numpages = 0;
        GCSArray<String> AlwBuffIds = new GCSArray<String>();
        int offset = Config.BBS_PVP_BUFFER_BUFFS_PER_PAGE * (page - 1);
        TextBuilder html = new TextBuilder();
        TextBuilder htmltoppanel = new TextBuilder();
        Connection con = null;
        try {
            con = DatabaseFactory.getInstance().getConnection();

            PreparedStatement stC = con.prepareStatement("SELECT * FROM bbs_buffer WHERE GpId=?;");
            stC.setInt(1, gpIdUse);
            ResultSet rsC = stC.executeQuery();

            while (rsC.next()) {
                bgrpOne = new CBBuffGroups();
                bgrpOne.GpId = rsC.getInt("GpId");
                bgrpOne.GpName = rsC.getString("GpName");
                bgrpOne.PlayerId = rsC.getInt("charId");
                GrpName = bgrpOne.GpName;
                GrpID = bgrpOne.GpId;
            }
            DbUtils.closeQuietly(stC, rsC);

            PreparedStatement stAlwCount = con.prepareStatement("SELECT COUNT(*) FROM bbs_buffer_allowed_buffs");
            ResultSet rsAlwCount = stAlwCount.executeQuery();
            rsAlwCount.next();
            if (rsAlwCount.getInt(1) > Config.BBS_PVP_BUFFER_BUFFS_PER_PAGE - 1)
                numpages = Math.ceil((double) rsAlwCount.getInt(1) / Config.BBS_PVP_BUFFER_BUFFS_PER_PAGE);
            DbUtils.closeQuietly(stAlwCount, rsAlwCount);

            PreparedStatement stAlw = con.prepareStatement("SELECT * FROM bbs_buffer_allowed_buffs WHERE NOT EXISTS (SELECT * FROM bbs_buffer_buffs WHERE bbs_buffer_buffs.buffid = bbs_buffer_allowed_buffs.skillID AND bbs_buffer_buffs.GpId =?) LIMIT " + offset + ", " + Config.BBS_PVP_BUFFER_BUFFS_PER_PAGE + "");
            stAlw.setInt(1, gpIdUse);
            ResultSet rsAlw = stAlw.executeQuery();
            while (rsAlw.next()) {
                bgrpAlw = new CBBuffAllowedBuffs();
                bgrpAlw.BuffId = rsAlw.getInt("skillID");
                bgrpAlw.BuffLvL = rsAlw.getInt("skillLvl");
                AlwBuffIds.add("" + bgrpAlw.BuffId + "");
            }
            DbUtils.closeQuietly(stAlw, rsAlw);

            html.append(buildTable(player, AlwBuffIds, 4, 3, gpIdUse, null));
            htmltoppanel.append("<table width=700>");
            htmltoppanel.append("<tr>");
            htmltoppanel.append(player.isLangRus() ? "<td>Редактирование шаблона: " + GrpName + "</td>" : "<td>Edit the template: " + GrpName + "</td>");
            htmltoppanel.append("<td></td>");
            htmltoppanel.append(page_list(numpages, page, GrpID, null));
            if (player.isLangRus())
                htmltoppanel.append("<td><button value=\"Назад\" action=\"bypass _bbsbuff;use " + GrpID + "\" width=50 height=20 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
            else
                htmltoppanel.append("<td><button value=\"Back\" action=\"bypass _bbsbuff;use " + GrpID + "\" width=50 height=20 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
            htmltoppanel.append("</tr>");
            htmltoppanel.append("</table>");

            String content = HtmCache.getInstance().getNotNull("scripts/services/community/" + Config.BBS_FOLDER + "/buffer/group.htm", player); // 601
            content = content.replace("%buffgrptoppanel%", htmltoppanel.toString());
            content = content.replace("%buffgrp%", html.toString());
            content = content.replace("<?cb_name?>", Config.COMMUNITYBOARD_NAME);
            content = content.replace("<?cb_copy?>", Config.COMMUNITYBOARD_COPY);
            content = content.replace("%bbs_menu%", HtmCache.getInstance().getNotNull("scripts/services/community/" + Config.BBS_FOLDER + "/bbs_menu.htm", player).toString());
            ShowBoard.separateAndSend(content, player);
            return;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(con);
        }

    }

    /**
     * Страница редактора набора. Список уже имеющихся баффов в наборе.
     *
     * @param
     */
    private void EditeBuffGrp(Player player, int gpIdUse) {
        CBBuffGroup bgrp;
        CBBuffGroups bgrpOne;
        String GrpName = null;
        int GrpID = 0;
        Connection con = null;
        try {
            con = DatabaseFactory.getInstance().getConnection();

            PreparedStatement stC = con.prepareStatement("SELECT * FROM bbs_buffer WHERE GpId=?;");
            stC.setInt(1, gpIdUse);
            ResultSet rsC = stC.executeQuery();

            while (rsC.next()) {
                bgrpOne = new CBBuffGroups();
                bgrpOne.GpId = rsC.getInt("GpId");
                bgrpOne.GpName = rsC.getString("GpName");
                bgrpOne.PlayerId = rsC.getInt("charId");
                GrpName = bgrpOne.GpName;
                GrpID = bgrpOne.GpId;
            }

            PreparedStatement st = con.prepareStatement("SELECT * FROM bbs_buffer_buffs WHERE GpId=?;");
            st.setInt(1, gpIdUse);
            ResultSet rs = st.executeQuery();
            TextBuilder html = new TextBuilder();

            GCSArray<String> buffIds = new GCSArray<String>();

            while (rs.next()) {
                bgrp = new CBBuffGroup();
                bgrp.OneId = rs.getInt("Id");
                bgrp.OneGpId = rs.getInt("GpId");
                bgrp.OnePlayerId = rs.getInt("charId");
                bgrp.OneBuffId = rs.getInt("buffid");
                buffIds.add("" + bgrp.OneBuffId + "");
            }

            DbUtils.closeQuietly(st, rs);
            DbUtils.closeQuietly(stC, rsC);

            html.append(buildTable(player, buffIds, 4, 2, gpIdUse, null));

            TextBuilder htmltoppanel = new TextBuilder();
            htmltoppanel.append("<table width=700>");
            htmltoppanel.append("<tr>");
            htmltoppanel.append(player.isLangRus() ? "<td>Редактирование: " + GrpName + "</td>" : "<td>Edit: " + GrpName + "</td>");
            htmltoppanel.append("<td></td>");
            if (player.isLangRus()) {
                htmltoppanel.append("<td><button value=\"Добавить бафф\" action=\"bypass _bbsbuff;addbuffin " + GrpID + " 1\" width=140 height=20 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
                htmltoppanel.append("<td><button value=\"Удалить набор\" action=\"bypass _bbsbuff;dellGrp " + GrpID + "\" width=140 height=20 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
                htmltoppanel.append("<td><button value=\"Назад\" action=\"bypass _bbsbuff;use " + GrpID + "\" width=100 height=20 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
            } else {
                htmltoppanel.append("<td><button value=\"Add buff\" action=\"bypass _bbsbuff;addbuffin " + GrpID + " 1\" width=140 height=20 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
                htmltoppanel.append("<td><button value=\"Delete set\" action=\"bypass _bbsbuff;dellGrp " + GrpID + "\" width=140 height=20 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
                htmltoppanel.append("<td><button value=\"Back\" action=\"bypass _bbsbuff;use " + GrpID + "\" width=100 height=20 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
            }
            htmltoppanel.append("</tr>");
            htmltoppanel.append("</table>");

            String content = HtmCache.getInstance().getNotNull("scripts/services/community/" + Config.BBS_FOLDER + "/buffer/group.htm", player); // 601
            content = content.replace("%buffgrptoppanel%", htmltoppanel.toString());
            content = content.replace("%buffgrp%", html.toString());
            content = content.replace("<?cb_name?>", Config.COMMUNITYBOARD_NAME);
            content = content.replace("<?cb_copy?>", Config.COMMUNITYBOARD_COPY);
            content = content.replace("%bbs_menu%", HtmCache.getInstance().getNotNull("scripts/services/community/" + Config.BBS_FOLDER + "/bbs_menu.htm", player).toString());
            ShowBoard.separateAndSend(content, player);
            return;

        } catch (Exception e) {
        } finally {
            DbUtils.closeQuietly(con);
        }

    }

    /**
     * Показываем страницу фиксированной группы.<br>
     * Группа составляется администратором и не может быть отредактированна
     * игроком.
     *
     * @param
     */
    private void UseFixedBuffGrp(Player player, String GrpName, int page) {
        CBBuffGroup bgrp;
        int price = 0;
        double numpages = 0;
        Summon l2Summon = player.getSummonList().getPet();
        int offset = Config.BBS_PVP_BUFFER_BUFFS_PER_PAGE * (page - 1);
        Connection con = null;
        PreparedStatement statement = null;
        GCSArray<String> buffIds = new GCSArray<String>();
        try {
            con = DatabaseFactory.getInstance().getConnection();

            PreparedStatement stAlwCount = con.prepareStatement("SELECT COUNT(*) FROM bbs_buffer_allowed_buffs WHERE (locate( ? ,skillgrp)>0)");
            stAlwCount.setString(1, GrpName);
            ResultSet rsAlwCount = stAlwCount.executeQuery();
            rsAlwCount.next();
            if (rsAlwCount.getInt(1) > Config.BBS_PVP_BUFFER_BUFFS_PER_PAGE - 1)
                numpages = Math.ceil((double) rsAlwCount.getInt(1) / Config.BBS_PVP_BUFFER_BUFFS_PER_PAGE);
            DbUtils.closeQuietly(stAlwCount, rsAlwCount);

            statement = con.prepareStatement("SELECT * FROM bbs_buffer_allowed_buffs WHERE (locate( ? ,skillgrp)>0) LIMIT " + offset + ", " + Config.BBS_PVP_BUFFER_BUFFS_PER_PAGE + "");
            statement.setString(1, GrpName);
            ResultSet rcln = statement.executeQuery();

            while (rcln.next()) {
                bgrp = new CBBuffGroup();
                bgrp.OneBuffId = rcln.getInt("skillID");
                buffIds.add("" + bgrp.OneBuffId + "");
            }
            DbUtils.closeQuietly(statement, rcln);

            if (player.getLevel() > Config.CBB_BUFFER_FREE_LEVEL)
                price = buffIds.size() * Config.BBS_PVP_BUFFER_PRICE_ONE;
            else
                price = 0;

            TextBuilder html = new TextBuilder();
            html.append(buildTable(player, buffIds, 4, 1, 0, GrpName));

            TextBuilder htmltoppanel = new TextBuilder();
            htmltoppanel.append("<table width=700>");
            htmltoppanel.append("<tr>");
            htmltoppanel.append(player.isLangRus() ? "<td>[Все: " + price + " адена]" : "<td>[All: " + price + " adena]");
            htmltoppanel.append("</td></tr><tr>");
            if (numpages <= 1) {
                htmltoppanel.append(player.isLangRus() ? "<td>Себе:</td>" : "<td>Self:</td>");
                if (player.isLangRus()) {
                    htmltoppanel.append("<td><button value=\"Все\" action=\"bypass _bbsbuff;fixedgrpbuff " + GrpName + " Player\" width=40 height=20 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
                    if (player.getVar("autoBuff@") == null && player.getVar("autoFixedBuff@") == null)
                        htmltoppanel.append("<td><button value=\"Включить авто-бафф\" action=\"bypass _bbsbuff;autobuffgrp 0 " + GrpName + " " + price + "\" width=140 height=20 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
                    else
                        htmltoppanel.append("<td><button value=\"Отключить авто-бафф\" action=\"bypass _bbsbuff;stopautobuffgrp 0 " + GrpName + "\" width=140 height=20 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
                } else {
                    htmltoppanel.append("<td><button value=\"All\" action=\"bypass _bbsbuff;fixedgrpbuff " + GrpName + " Player\" width=40 height=20 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
                    if (player.getVar("autoBuff@") == null && player.getVar("autoFixedBuff@") == null)
                        htmltoppanel.append("<td><button value=\"Autobuff On\" action=\"bypass _bbsbuff;autobuffgrp 0 " + GrpName + " " + price + "\" width=140 height=20 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
                    else
                        htmltoppanel.append("<td><button value=\"Autobuff Off\" action=\"bypass _bbsbuff;stopautobuffgrp 0 " + GrpName + "\" width=140 height=20 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
                }
            } else
                htmltoppanel.append(page_list(numpages, page, 0, GrpName));
            htmltoppanel.append("</tr>");

            if (l2Summon != null) {
                htmltoppanel.append("<tr>");
                htmltoppanel.append("<td></td>");
                if (numpages <= 1) {
                    htmltoppanel.append(player.isLangRus() ? "<td>Питомец:</td>" : "<td>Pet:</td>");
                    if (player.isLangRus()) {
                        htmltoppanel.append("<td><button value=\"Все\" action=\"bypass _bbsbuff;fixedgrpbuff " + GrpName + " Pet\" width=40 height=20 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
                        if (player.getVar("autoBuffPet@") == null && player.getVar("autoFixedBuffPet@") == null)
                            htmltoppanel.append("<td><button value=\"Включить авто-бафф\" action=\"bypass _bbsbuff;petautobuffgrp 0 " + GrpName + " " + price + "\" width=140 height=20 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
                        else
                            htmltoppanel.append("<td><button value=\"Отключить авто-бафф\" action=\"bypass _bbsbuff;petstopbuffgrpauto 0 " + GrpName + "\" width=140 height=20 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
                    } else {
                        htmltoppanel.append("<td><button value=\"All\" action=\"bypass _bbsbuff;fixedgrpbuff " + GrpName + " Pet\" width=40 height=20 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
                        if (player.getVar("autoBuffPet@") == null && player.getVar("autoFixedBuffPet@") == null)
                            htmltoppanel.append("<td><button value=\"Autobuff On\" action=\"bypass _bbsbuff;petautobuffgrp 0 " + GrpName + " " + price + "\" width=140 height=20 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
                        else
                            htmltoppanel.append("<td><button value=\"Autobuff Off\" action=\"bypass _bbsbuff;petstopbuffgrpauto 0 " + GrpName + "\" width=140 height=20 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
                    }
                }
                htmltoppanel.append("<td></td>");
                htmltoppanel.append("</tr>");
            }

            htmltoppanel.append("</table>");

            String content = HtmCache.getInstance().getNotNull("scripts/services/community/" + Config.BBS_FOLDER + "/buffer/group.htm", player);
            content = content.replace("%buffgrptoppanel%", htmltoppanel.toString());
            content = content.replace("%buffgrp%", html.toString());
            content = content.replace("<?cb_name?>", Config.COMMUNITYBOARD_NAME);
            content = content.replace("<?cb_copy?>", Config.COMMUNITYBOARD_COPY);
            content = content.replace("%bbs_menu%", HtmCache.getInstance().getNotNull("scripts/services/community/" + Config.BBS_FOLDER + "/bbs_menu.htm", player).toString());
            ShowBoard.separateAndSend(content, player);
            return;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(con);
        }

    }

    /**
     * Показываем страницу группы
     *
     * @param
     */
    private void UseBuffGrp(Player player, int gpIdUse) {
        CBBuffGroup bgrp;
        CBBuffGroups bgrpOne;
        String GrpName = null;
        int GrpID = 0;
        int price = 0;
        Summon l2Summon = player.getSummonList().getPet();
        Connection con = null;
        try {
            con = DatabaseFactory.getInstance().getConnection();

            PreparedStatement stC = con.prepareStatement("SELECT * FROM bbs_buffer WHERE GpId=?;");
            stC.setInt(1, gpIdUse);
            ResultSet rsC = stC.executeQuery();

            while (rsC.next()) {
                bgrpOne = new CBBuffGroups();
                bgrpOne.GpId = rsC.getInt("GpId");
                bgrpOne.GpName = rsC.getString("GpName");
                bgrpOne.PlayerId = rsC.getInt("charId");
                GrpID = bgrpOne.GpId;
                GrpName = bgrpOne.GpName;
            }
            DbUtils.closeQuietly(stC, rsC);

            PreparedStatement st = con.prepareStatement("SELECT * FROM bbs_buffer_buffs WHERE GpId=?;");
            st.setInt(1, gpIdUse);
            ResultSet rs = st.executeQuery();
            TextBuilder html = new TextBuilder();

            GCSArray<String> buffIds = new GCSArray<String>();

            while (rs.next()) {
                bgrp = new CBBuffGroup();
                bgrp.OneId = rs.getInt("Id");
                bgrp.OneGpId = rs.getInt("GpId");
                bgrp.OnePlayerId = rs.getInt("charId");
                bgrp.OneBuffId = rs.getInt("buffid");
                buffIds.add("" + bgrp.OneBuffId + "");
            }
            DbUtils.closeQuietly(st, rs);

            if (player.getLevel() > Config.CBB_BUFFER_FREE_LEVEL)
                price = buffIds.size() * Config.BBS_PVP_BUFFER_PRICE_ONE;
            else
                price = 0;

            html.append(buildTable(player, buffIds, 4, 1, gpIdUse, null));

            TextBuilder htmltoppanel = new TextBuilder();
            htmltoppanel.append("<table width=700>");
            htmltoppanel.append("<tr>");
            htmltoppanel.append(player.isLangRus() ? "<td>" + GrpName + " [Все: " + price + " адена]" : "<td>" + GrpName + " [All: " + price + " adena]");
            htmltoppanel.append("</td>");
            htmltoppanel.append(player.isLangRus() ? "<td>Себе:</td>" : "<td>Self:</td>");
            if (player.isLangRus()) {
                htmltoppanel.append("<td><button value=\"Все\" action=\"bypass _bbsbuff;grpbuff " + GrpID + " Player\" width=50 height=20 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
                if (player.getVar("autoBuff@") == null && player.getVar("autoFixedBuff@") == null)
                    htmltoppanel.append("<td><button value=\"Включить авто-бафф\" action=\"bypass _bbsbuff;autobuffgrp " + GrpID + " null " + price + "\" width=140 height=20 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
                else
                    htmltoppanel.append("<td><button value=\"Отключить авто-бафф\" action=\"bypass _bbsbuff;stopautobuffgrp " + GrpID + " null\" width=140 height=20 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
                htmltoppanel.append("<td><button value=\"Редактировать\" action=\"bypass _bbsbuff;editegrp " + GrpID + "\" width=100 height=20 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
            } else {
                htmltoppanel.append("<td><button value=\"All\" action=\"bypass _bbsbuff;grpbuff " + GrpID + " Player\" width=50 height=20 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
                if (player.getVar("autoBuff@") == null && player.getVar("autoFixedBuff@") == null)
                    htmltoppanel.append("<td><button value=\"Autobuff On\" action=\"bypass _bbsbuff;autobuffgrp " + GrpID + " null " + price + "\" width=140 height=20 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
                else
                    htmltoppanel.append("<td><button value=\"Autobuff Off\" action=\"bypass _bbsbuff;stopautobuffgrp " + GrpID + " null\" width=140 height=20 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
                htmltoppanel.append("<td><button value=\"Edit\" action=\"bypass _bbsbuff;editegrp " + GrpID + "\" width=100 height=20 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
            }
            htmltoppanel.append("</tr>");

            if (l2Summon != null) {
                htmltoppanel.append("<tr>");
                htmltoppanel.append("<td></td>");
                htmltoppanel.append(player.isLangRus() ? "<td>Питомец:</td>" : "<td>Pet:</td>");
                if (player.isLangRus()) {
                    htmltoppanel.append("<td><button value=\"Все\" action=\"bypass _bbsbuff;grpbuff " + GrpID + " Pet\" width=50 height=20 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
                    if (player.getVar("autoBuffPet@") == null && player.getVar("autoFixedBuffPet@") == null)
                        htmltoppanel.append("<td><button value=\"Включить авто-бафф\" action=\"bypass _bbsbuff;petautobuffgrp " + GrpID + " null " + price + "\" width=140 height=20 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
                    else
                        htmltoppanel.append("<td><button value=\"Отключить авто-бафф\" action=\"bypass _bbsbuff;petstopbuffgrpauto " + GrpID + " null\" width=140 height=20 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
                } else {
                    htmltoppanel.append("<td><button value=\"All\" action=\"bypass _bbsbuff;grpbuff " + GrpID + " Pet\" width=50 height=20 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
                    if (player.getVar("autoBuffPet@") == null && player.getVar("autoFixedBuffPet@") == null)
                        htmltoppanel.append("<td><button value=\"Autobuff On\" action=\"bypass _bbsbuff;petautobuffgrp " + GrpID + " null " + price + "\" width=140 height=20 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
                    else
                        htmltoppanel.append("<td><button value=\"Autobuff Off\" action=\"bypass _bbsbuff;petstopbuffgrpauto " + GrpID + " null\" width=140 height=20 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
                }
                htmltoppanel.append("<td></td>");
                htmltoppanel.append("</tr>");
            }

            htmltoppanel.append("</table>");

            String content = HtmCache.getInstance().getNotNull("scripts/services/community/" + Config.BBS_FOLDER + "/buffer/group.htm", player); // 601
            content = content.replace("%buffgrptoppanel%", htmltoppanel.toString());
            content = content.replace("%buffgrp%", html.toString());
            content = content.replace("<?cb_name?>", Config.COMMUNITYBOARD_NAME);
            content = content.replace("<?cb_copy?>", Config.COMMUNITYBOARD_COPY);
            content = content.replace("%bbs_menu%", HtmCache.getInstance().getNotNull("scripts/services/community/" + Config.BBS_FOLDER + "/bbs_menu.htm", player).toString());
            ShowBoard.separateAndSend(content, player);
            return;

        } catch (Exception e) {
        } finally {
            DbUtils.closeQuietly(con);
        }

    }

    /**
     * Удаляем группу баффов.
     *
     * @param
     */
    private void DellBuffGrp(Player activeChar, int gpNameDell) {
        Connection conDel = null;
        try {
            conDel = DatabaseFactory.getInstance().getConnection();

            // Удаляем группу бафов.
            PreparedStatement stDel = conDel.prepareStatement("DELETE FROM bbs_buffer WHERE charId=? AND GpId=?;");
            stDel.setInt(1, activeChar.getObjectId());
            stDel.setInt(2, gpNameDell);
            stDel.execute();
            // Удаляем баффы которые были в удаляемой группе.
            PreparedStatement stDelin = conDel.prepareStatement("DELETE FROM bbs_buffer_buffs WHERE charId=? AND GpId=?;");
            stDelin.setInt(1, activeChar.getObjectId());
            stDelin.setInt(2, gpNameDell);
            stDelin.execute();
        } catch (Exception e) {
        } finally {
            DbUtils.closeQuietly(conDel);
        }

    }

    /**
     * Создаем группу баффов.
     *
     * @param
     */
    private void AddBuffGrp(Player activeChar, String grpNameAdd) {
        if (grpNameAdd.equals("") || grpNameAdd.equals(null)) {
            activeChar.sendMessage(activeChar.isLangRus() ? "Вы не ввели Имя группы" : "You did not enter the Group Name");
            return;
        }

        Connection con = null;
        try {
            con = DatabaseFactory.getInstance().getConnection();
            // Получаем кол-во групп текущего чара из таблицы и если кол-во их
            // не превышает установленное добавляем новую.
            PreparedStatement st = con.prepareStatement("SELECT COUNT(*) FROM bbs_buffer WHERE charId=?;");
            st.setLong(1, activeChar.getObjectId());
            ResultSet rs = st.executeQuery();
            rs.next();
            if (rs.getInt(1) <= 7) {
                // Проверяем существует ли группа с именем которое передано в
                // параметре
                PreparedStatement st1 = con.prepareStatement("SELECT COUNT(*) FROM bbs_buffer WHERE charId=? AND GpName=?;");
                st1.setLong(1, activeChar.getObjectId());
                st1.setString(2, grpNameAdd);
                ResultSet rs1 = st1.executeQuery();
                rs1.next();
                if (rs1.getInt(1) == 0) {
                    // Если группы нет, создаем.
                    PreparedStatement stAdd = con.prepareStatement("INSERT INTO bbs_buffer (charId,GpName) VALUES(?,?)");
                    stAdd.setInt(1, activeChar.getObjectId());
                    stAdd.setString(2, grpNameAdd);
                    stAdd.execute();
                } else {
                    // Если группа есть, просто обновляем ее имя.
                    PreparedStatement stAdd = con.prepareStatement("UPDATE bbs_buffer SET GpName=? WHERE charId=? AND GpName=?;");
                    stAdd.setInt(1, activeChar.getObjectId());
                    stAdd.setString(2, grpNameAdd);
                    stAdd.execute();
                }
            } else {
                activeChar.sendMessage(activeChar.isLangRus() ? "Вы не можете сохранить более 8 групп" : "You can not save more than 8 groups");
                return;
            }

        } catch (Exception e) {
        } finally {
            DbUtils.closeQuietly(con);
        }
    }

    /**
     * Показываем чару первую страницу баффера со списком его персональных
     * групп.
     *
     * @param
     */
    private void showBuffIndexPage(Player player) {
        CBBuffGroups bgrp;
        Connection con = null;
        try {
            con = DatabaseFactory.getInstance().getConnection();
            PreparedStatement st = con.prepareStatement("SELECT * FROM bbs_buffer WHERE charId=?;");
            st.setLong(1, player.getObjectId());
            ResultSet rs = st.executeQuery();
            TextBuilder html = new TextBuilder();
            html.append("<table width=150>");
            while (rs.next()) {
                bgrp = new CBBuffGroups();
                bgrp.GpId = rs.getInt("GpId");
                bgrp.GpName = rs.getString("GpName");
                bgrp.PlayerId = rs.getInt("charId");
                html.append("<tr>");
                html.append("<td valign=\"top\" align=\"center\"><button value=\"" + bgrp.GpName + "\" action=\"bypass _bbsbuff;use " + bgrp.GpId + "\" width=150 height=30 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>");
                html.append("<td valign=\"top\" align=\"center\"><button action=\"bypass _bbsbuff;dellGrp " + bgrp.GpId + "\" width=\"15\" height=\"15\" back=\"L2UI_CT1.Button_DF_Delete_Down\" fore=\"L2UI_CT1.Button_DF_Delete\"></td>");
                html.append("</tr>");
            }
            html.append("</table>");

            DbUtils.closeQuietly(st, rs);

            String content = HtmCache.getInstance().getNotNull("scripts/services/community/" + Config.BBS_FOLDER + "/buffer/index.htm", player); // 60
            content = content.replace("%buffgrps%", html.toString());
            content = content.replace("<?cb_name?>", Config.COMMUNITYBOARD_NAME);
            content = content.replace("<?cb_copy?>", Config.COMMUNITYBOARD_COPY);
            content = content.replace("<?cb_buff_time_minute?>", String.valueOf(Config.BBS_PVP_BUFFER_ALT_TIME / 60 / 1000));
            content = content.replace("<?cb_price?>", String.valueOf(Config.BBS_PVP_BUFFER_PRICE_ONE));
            content = content.replace("%bbs_menu%", HtmCache.getInstance().getNotNull("scripts/services/community/" + Config.BBS_FOLDER + "/bbs_menu.htm", player).toString());
            ShowBoard.separateAndSend(content, player);
            return;

        } catch (Exception e) {
        } finally {
            DbUtils.closeQuietly(con);
        }

    }

    public static String buildTable(Player activeChar, GCSArray<String> buffIds, int cols_number, int type, int grpId, String grpName) {
        Skill skill;
        String bottom = null;
        String res = "<table width=570>";
        double rows = Math.ceil((double) buffIds.size() / cols_number);
        int c = 0;
        for (int i = 0; i < (int) rows; i++) {
            res += "<tr>";

            for (int j = 0; j < cols_number; j++) {
                if (buffIds.size() > c) {
                    skill = SkillTable.getInstance().getInfo(Integer.parseInt(buffIds.get(c)), 1);
                    if (type == 1)
                        if (activeChar.isLangRus())
                            bottom = "<button value=\"Баф\" action=\"bypass _bbsbuff;buff " + buffIds.get(c) + " " + skill.getBaseLevel() + "\" width=40 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>";
                        else
                            bottom = "<button value=\"Buff\" action=\"bypass _bbsbuff;buff " + buffIds.get(c) + " " + skill.getBaseLevel() + "\" width=40 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>";
                    else if (type == 2)
                        if (activeChar.isLangRus())
                            bottom = "<button value=\"Удал.\" action=\"bypass _bbsbuff;dellbufffrom " + grpId + " " + buffIds.get(c) + "\" width=40 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>";
                        else
                            bottom = "<button value=\"Del\" action=\"bypass _bbsbuff;dellbufffrom " + grpId + " " + buffIds.get(c) + "\" width=40 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>";
                    else if (type == 3)
                        if (activeChar.isLangRus())
                            bottom = "<button value=\"Доб.\" action=\"bypass _bbsbuff;fingrpaddbuf " + grpId + " " + buffIds.get(c) + " " + skill.getBaseLevel() + "\" width=40 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>";
                        else
                            bottom = "<button value=\"Add\" action=\"bypass _bbsbuff;fingrpaddbuf " + grpId + " " + buffIds.get(c) + " " + skill.getBaseLevel() + "\" width=40 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>";
                    if (activeChar.isLangRus())
                        res += "<center><td valign=\"top\" align=\"center\" width=160>" + "<center>" + "<table width=160 height=32>" + "<tr>" + "<td width=32><center><img src=icon." + skill.getIcon() + " width=32 height=32></center>" + bottom + "<td width=128>" + "<table width=128><tr><td><font color=B59A75>" + skill.getName() + "</font></td></tr><tr><td><font color=F2C202>Уровень: " + skill.getBaseLevel() + "</font></td></tr><tr><td><font color=0099FF><a action=\"bypass _bbsscripts:services.community.ShowInfo:show skills " + buffIds.get(c) + "\">Информация</a></font></td></tr></table>" + "</td>" + "</tr>" + "</table></center></td></center>";
                    else
                        res += "<center><td valign=\"top\" align=\"center\" width=160>" + "<center>" + "<table width=160 height=32>" + "<tr>" + "<td width=32><center><img src=icon." + skill.getIcon() + " width=32 height=32></center>" + bottom + "<td width=128>" + "<table width=128><tr><td><font color=B59A75>" + skill.getName() + "</font></td></tr><tr><td><font color=F2C202>Level: " + skill.getBaseLevel() + "</font></td></tr><tr><td><font color=0099FF><a action=\"bypass _bbsscripts:services.community.ShowInfo:show skills " + buffIds.get(c) + "\">Info</a></font></td></tr></table>" + "</td>" + "</tr>" + "</table></center></td></center>";
                } else
                    res += "<td width=150 height=60><center></center></td>";
                c++;
            }

            res += "</tr>";
        }
        res += "</table><br>";
        return res;
    }

    public static String page_list(double numpages, int page, int GrpID, String GrpName) {
        String index;
        String cmd;
        if (GrpID == 0 && GrpName != null) {
            index = GrpName;
            cmd = "usefixed";
        } else {
            index = Integer.toString(GrpID);
            cmd = "addbuffin";
        }
        String pager = "";
        pager += page > 1 ? "<td width=50><center><button value=\"<<<\" action=\"bypass _bbsbuff;" + cmd + " " + index + " " + (page - 1) + "\" width=50 height=20 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></center></td>" : "<td width=50></td>";
        pager += "<td width=50><center>Page №" + page + "</center></td>";
        pager += page < numpages ? "<td width=50><center><button value=\">>>\" action=\"bypass _bbsbuff;" + cmd + " " + index + " " + (page + 1) + "\" width=50 height=20 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></center></td>" : "<td width=50></td>";

        return pager;
    }

    public boolean chekCondition(Player activeChar) {
        if (activeChar == null || !activeChar.isConnected() || activeChar.isInOfflineMode())
            return false;
        if (activeChar.isDead() || activeChar.isAlikeDead() || activeChar.isCastingNow() || activeChar.isInCombat() || activeChar.isAttackingNow() || activeChar.isInOlympiadMode() || activeChar.isFlying() || activeChar.isTerritoryFlagEquipped()) {
            activeChar.sendMessage(activeChar.isLangRus() ? "Невозможно использовать в данный момент!" : "Can not be used at the moment!");
            return false;
        }
        // Можно ли юзать бафера во время осады?
        if (!Config.BBS_PVP_BUFFER_ALLOW_SIEGE)
            if (activeChar.isInZone(ZoneType.SIEGE)) {
                activeChar.sendMessage(activeChar.isLangRus() ? "Невозможно использовать в данный момент!" : "Can not be used at the moment!");
                return false;
            }
        return true;
    }

    /**
     * Запускаем задачу автобафа
     *
     * @param
     */
    private void StartAutoBuff(Player activeChar, int gpIdUse, String gpUseName, int price) {
        if (!chekCondition(activeChar))
            return;

        if (price > 0 && activeChar.getAdena() < price) {
            activeChar.sendMessage(activeChar.isLangRus() ? "Недостаточно денег для запуска автобаффа!" : "Not enough money to run autobuff!");
            return;
        }

        if (gpIdUse == 0 && gpUseName != null) {
            activeChar.setVar("autoFixedBuff@", "" + gpUseName + "", -1);

            BuffFixedGrp(activeChar, gpUseName, " Player");
            new buffTask(activeChar, price, true);
            UseFixedBuffGrp(activeChar, gpUseName, 1);
        } else {
            activeChar.setVar("autoBuff@", "" + gpIdUse + "", -1);

            BuffGrp(activeChar, gpIdUse, " Player");
            new buffTask(activeChar, price, false);
            UseBuffGrp(activeChar, gpIdUse);
        }
    }

    /**
     * Останавливаем задачу автобафа
     *
     * @param
     */
    private void StopAutoBuff(Player activeChar, int gpIdUse, String gpUseName) {
        if (gpIdUse == 0 && gpUseName != null) {
            activeChar.unsetVar("autoFixedBuff@");
            UseFixedBuffGrp(activeChar, gpUseName, 1);
        } else {
            activeChar.unsetVar("autoBuff@");
            UseBuffGrp(activeChar, gpIdUse);
        }
    }

    /**
     * Запускаем задачу автобафа для пета
     *
     * @param
     */
    private void StartAutoBuffPet(Player activeChar, int gpIdUse, String gpUseName, int price) {
        Summon l2Summon = activeChar.getSummonList().getPet();

        if (l2Summon == null) {
            activeChar.sendMessage(activeChar.isLangRus() ? "Призовите питомца для запуска!" : "Encourage your pet to run!");
            return;
        }

        if (!chekCondition(activeChar))
            return;

        if (price > 0 && activeChar.getAdena() < price) {
            activeChar.sendMessage(activeChar.isLangRus() ? "Недостаточно денег для запуска автобаффа питомца!" : "Not enough money to run autobuff pet!");
            return;
        }

        if (gpIdUse == 0 && gpUseName != null) {
            activeChar.setVar("autoFixedBuffPet@", "" + gpUseName + "", -1);

            BuffFixedGrp(activeChar, gpUseName, " Pet");
            new buffTaskPet(activeChar, price, true);
            UseFixedBuffGrp(activeChar, gpUseName, 1);
        } else {
            activeChar.setVar("autoBuffPet@", "" + gpIdUse + "", -1);

            BuffGrp(activeChar, gpIdUse, " Pet");
            new buffTaskPet(activeChar, price, false);
            UseBuffGrp(activeChar, gpIdUse);
        }
    }

    /**
     * Останавливаем задачу автобафа для пета
     *
     * @param
     */
    private void StopAutoBuffPet(Player activeChar, int gpIdUse, String gpUseName) {
        if (gpIdUse == 0 && gpUseName != null) {
            activeChar.unsetVar("autoFixedBuffPet@");
            UseFixedBuffGrp(activeChar, gpUseName, 1);
        } else {
            activeChar.unsetVar("autoBuffPet@");
            UseBuffGrp(activeChar, gpIdUse);
        }

    }

    /**
     * Задача автобафа
     *
     * @param
     */
    public class buffTask {
        private ScheduledFuture<?> buffTask = null;

        class buff implements Runnable {
            Player activeChartoBuff;
            int pricetoBuff;
            boolean fixedBuff;

            public buff(Player _activeChar, int _price, boolean _fixed) {
                activeChartoBuff = _activeChar;
                pricetoBuff = _price;
                fixedBuff = _fixed;
            }

            @Override
            public void run() {
                try {
                    if (pricetoBuff > 0 && activeChartoBuff.getAdena() < pricetoBuff) {
                        activeChartoBuff.sendMessage(activeChartoBuff.isLangRus() ? "Недостаточно денег для запуска автобаффа! Задача остановлена!" : "Not enough money to run avtobaffa! The task is stopped!");

                        stopBuffTask(true);
                        return;
                    }

                    if (activeChartoBuff.getVar("autoBuff@") == null && !fixedBuff) {
                        stopBuffTask(true);
                        return;
                    }

                    if (activeChartoBuff.getVar("autoFixedBuff@") == null && fixedBuff) {
                        stopBuffTask(true);
                        return;
                    }

                    if (activeChartoBuff == null || !activeChartoBuff.isConnected() || activeChartoBuff.isInOfflineMode()) {
                        stopBuffTask(true);
                        return;
                    }

                    if (activeChartoBuff.isDead() || activeChartoBuff.isAlikeDead() || activeChartoBuff.isCastingNow() || activeChartoBuff.isInCombat() || activeChartoBuff.isAttackingNow() || activeChartoBuff.isInOlympiadMode() || activeChartoBuff.isFlying() || activeChartoBuff.isTerritoryFlagEquipped() || activeChartoBuff.isInZone(ZoneType.SIEGE)) {
                        activeChartoBuff.sendMessage(activeChartoBuff.isLangRus() ? "Невозможно запустить автобафф в данный момент! Задача остановлена!" : "Unable to run avtobaff at the moment! The task is stopped!");
                        stopBuffTask(true);
                        return;
                    }

                    if (activeChartoBuff.getVar("autoBuff@") != null && activeChartoBuff.getVar("autoFixedBuff@") == null)
                        BuffGrp(activeChartoBuff, Integer.parseInt(activeChartoBuff.getVar("autoBuff@")), " Player");
                    else if (activeChartoBuff.getVar("autoBuff@") == null && activeChartoBuff.getVar("autoFixedBuff@") != null)
                        BuffFixedGrp(activeChartoBuff, activeChartoBuff.getVar("autoFixedBuff@"), " Player");
                    else {
                        stopBuffTask(true);
                        return;
                    }

                    buffTask = ThreadPoolManager.getInstance().schedule(new buff(activeChartoBuff, pricetoBuff, fixedBuff), Config.BBS_PVP_BUFFER_TASK_DELAY);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }

        public void Shutdown(Player _activeChar) {
            stopBuffTask(false);
        }

        public void stopBuffTask(final boolean mayInterruptIfRunning) {
            if (buffTask != null) {
                buffTask.cancel(mayInterruptIfRunning);
                buffTask = null;
            }
        }

        private buffTask(Player activeChar, int price, boolean fixed) {
            buffTask = ThreadPoolManager.getInstance().schedule(new buff(activeChar, price, fixed), Config.BBS_PVP_BUFFER_TASK_DELAY);
        }
    }

    /**
     * Задача автобафа для петов
     *
     * @param
     */
    public class buffTaskPet {
        private ScheduledFuture<?> buffTaskPet = null;

        class buffPet implements Runnable {
            Player activeChartoBuff;
            long pricetoBuff;
            boolean fixedBuff;

            public buffPet(Player _activeChar, long _price, boolean _fixed) {
                activeChartoBuff = _activeChar;
                pricetoBuff = _price;
                fixedBuff = _fixed;
            }

            @Override
            public void run() {
                try {
                    Summon l2Summon = activeChartoBuff.getSummonList().getPet();

                    if (l2Summon == null) {
                        activeChartoBuff.sendMessage(activeChartoBuff.isLangRus() ? "Призовите питомца для запуска автобаффа! Задача остановлена!" : "Encourage your pet to run autobuff! The task is stopped!");
                        stopPetBuffTask(true);
                        return;
                    }

                    if (pricetoBuff > 0 && activeChartoBuff.getAdena() < pricetoBuff) {
                        activeChartoBuff.sendMessage(activeChartoBuff.isLangRus() ? "Недостаточно денег для запуска автобаффа питомца! Задача остановлена!" : "Not enough money to run autobuff pet! The task is stopped!");
                        stopPetBuffTask(true);
                        return;
                    }

                    if (activeChartoBuff.getVar("autoBuffPet@") == null && !fixedBuff) {
                        stopPetBuffTask(true);
                        return;
                    }

                    if (activeChartoBuff.getVar("autoFixedBuffPet@") == null && fixedBuff) {
                        stopPetBuffTask(true);
                        return;
                    }

                    if (activeChartoBuff == null || !activeChartoBuff.isConnected() || activeChartoBuff.isInOfflineMode()) {
                        stopPetBuffTask(true);
                        return;
                    }

                    if (activeChartoBuff.isDead() || activeChartoBuff.isAlikeDead() || activeChartoBuff.isCastingNow() || activeChartoBuff.isInCombat() || activeChartoBuff.isAttackingNow() || activeChartoBuff.isInOlympiadMode() || activeChartoBuff.isFlying() || activeChartoBuff.isTerritoryFlagEquipped() || activeChartoBuff.isInZone(ZoneType.SIEGE)) {
                        activeChartoBuff.sendMessage(activeChartoBuff.isLangRus() ? "Невозможно запустить автобафф питомца в данном! Задача остановлена!" : "Unable to run autobuff pet in this! The task is stopped!");
                        stopPetBuffTask(true);
                        return;
                    }

                    if (activeChartoBuff.getVar("autoBuffPet@") != null && activeChartoBuff.getVar("autoFixedBuffPet@") == null)
                        BuffGrp(activeChartoBuff, Integer.parseInt(activeChartoBuff.getVar("autoBuffPet@")), " Pet");
                    else if (activeChartoBuff.getVar("autoBuffPet@") == null && activeChartoBuff.getVar("autoFixedBuffPet@") != null)
                        BuffFixedGrp(activeChartoBuff, activeChartoBuff.getVar("autoFixedBuffPet@"), " Pet");
                    else {
                        stopPetBuffTask(true);
                        return;
                    }

                    buffTaskPet = ThreadPoolManager.getInstance().schedule(new buffPet(activeChartoBuff, pricetoBuff, fixedBuff), Config.BBS_PVP_BUFFER_TASK_DELAY);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }

        public void Shutdown(Player _activeChar) {
            stopPetBuffTask(false);
        }

        public void stopPetBuffTask(final boolean mayInterruptIfRunning) {
            if (buffTaskPet != null) {
                buffTaskPet.cancel(mayInterruptIfRunning);
                buffTaskPet = null;
            }
        }

        private buffTaskPet(Player activeChar, long price, boolean fixed) {
            buffTaskPet = ThreadPoolManager.getInstance().schedule(new buffPet(activeChar, price, fixed), Config.BBS_PVP_BUFFER_TASK_DELAY);
        }
    }

    /**
     * Не используемый, но вызываемый метод имплемента
     */
    @Override
    public void onWriteCommand(Player player, String bypass, String arg1, String arg2, String arg3, String arg4, String arg5) {
    }
}