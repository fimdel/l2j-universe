package l2p.gameserver.handler.admincommands;

import l2p.commons.data.xml.AbstractHolder;
import l2p.gameserver.ccpGuard.AdminHWID;
import l2p.gameserver.handler.admincommands.impl.*;
import l2p.gameserver.model.Player;
import l2p.gameserver.network.serverpackets.components.CustomMessage;
import l2p.gameserver.utils.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AdminCommandHandler extends AbstractHolder {
    private static final AdminCommandHandler _instance = new AdminCommandHandler();

    public static AdminCommandHandler getInstance() {
        return _instance;
    }

    private Map<String, IAdminCommandHandler> _datatable = new HashMap<String, IAdminCommandHandler>();

    private AdminCommandHandler() {
        registerAdminCommandHandler(new AdminAdmin());
        registerAdminCommandHandler(new AdminTest());
        registerAdminCommandHandler(new AdminHWID());
        registerAdminCommandHandler(new AdminAnnouncements());
        registerAdminCommandHandler(new AdminBan());
        registerAdminCommandHandler(new AdminCamera());
        registerAdminCommandHandler(new AdminCancel());
        registerAdminCommandHandler(new AdminChangeAccessLevel());
        registerAdminCommandHandler(new AdminClanHall());
        registerAdminCommandHandler(new AdminCreateItem());
        registerAdminCommandHandler(new AdminCursedWeapons());
        registerAdminCommandHandler(new AdminDelete());
        registerAdminCommandHandler(new AdminDisconnect());
        registerAdminCommandHandler(new AdminDoorControl());
        registerAdminCommandHandler(new AdminEditChar());
        registerAdminCommandHandler(new AdminEffects());
        registerAdminCommandHandler(new AdminEnchant());
        registerAdminCommandHandler(new AdminEvents());
        registerAdminCommandHandler(new AdminGeodata());
        registerAdminCommandHandler(new AdminGeoEditor());
        registerAdminCommandHandler(new AdminGm());
        registerAdminCommandHandler(new AdminGmChat());
        registerAdminCommandHandler(new AdminHeal());
        registerAdminCommandHandler(new AdminHellbound());
        registerAdminCommandHandler(new AdminHelpPage());
        registerAdminCommandHandler(new AdminInstance());
        registerAdminCommandHandler(new AdminIP());
        registerAdminCommandHandler(new AdminLevel());
        registerAdminCommandHandler(new AdminMammon());
        registerAdminCommandHandler(new AdminManor());
        registerAdminCommandHandler(new AdminMenu());
        registerAdminCommandHandler(new AdminMonsterRace());
        registerAdminCommandHandler(new AdminNochannel());
        registerAdminCommandHandler(new AdminOlympiad());
        registerAdminCommandHandler(new AdminPetition());
        registerAdminCommandHandler(new AdminPledge());
        registerAdminCommandHandler(new AdminPolymorph());
        registerAdminCommandHandler(new AdminQuests());
        registerAdminCommandHandler(new AdminReload());
        registerAdminCommandHandler(new AdminRepairChar());
        registerAdminCommandHandler(new AdminRes());
        registerAdminCommandHandler(new AdminRide());
        registerAdminCommandHandler(new AdminServer());
        registerAdminCommandHandler(new AdminShop());
        registerAdminCommandHandler(new AdminShutdown());
        registerAdminCommandHandler(new AdminSkill());
        registerAdminCommandHandler(new AdminScripts());
        registerAdminCommandHandler(new AdminSpawn());
        registerAdminCommandHandler(new AdminTarget());
        registerAdminCommandHandler(new AdminTeleport());
        registerAdminCommandHandler(new AdminZone());
        registerAdminCommandHandler(new AdminKill());

    }

    public void registerAdminCommandHandler(IAdminCommandHandler handler) {
        for (Enum<?> e : handler.getAdminCommandEnum())
            _datatable.put(e.toString().toLowerCase(), handler);
    }

    public IAdminCommandHandler getAdminCommandHandler(String adminCommand) {
        String command = adminCommand;
        if (adminCommand.indexOf(" ") != -1)
            command = adminCommand.substring(0, adminCommand.indexOf(" "));
        return _datatable.get(command);
    }

    public void useAdminCommandHandler(Player activeChar, String adminCommand) {
        if (!(activeChar.isGM() || activeChar.getPlayerAccess().CanUseGMCommand)) {
            activeChar.sendMessage(new CustomMessage("l2p.gameserver.clientpackets.SendBypassBuildCmd.NoCommandOrAccess", activeChar).addString(adminCommand));
            return;
        }

        String[] wordList = adminCommand.split(" ");
        IAdminCommandHandler handler = _datatable.get(wordList[0]);
        if (handler != null) {
            boolean success = false;
            try {
                for (Enum<?> e : handler.getAdminCommandEnum())
                    if (e.toString().equalsIgnoreCase(wordList[0])) {
                        success = handler.useAdminCommand(e, wordList, adminCommand, activeChar);
                        break;
                    }
            } catch (Exception e) {
                error("", e);
            }

            Log.LogCommand(activeChar, activeChar.getTarget(), adminCommand, success);
        }
    }

    @Override
    public void process() {

    }

    @Override
    public int size() {
        return _datatable.size();
    }

    @Override
    public void clear() {
        _datatable.clear();
    }

    /**
     * Получение списка зарегистрированных админ команд
     *
     * @return список команд
     */
    public Set<String> getAllCommands() {
        return _datatable.keySet();
    }
}
