package l2p.gameserver.ccpGuard;


import l2p.gameserver.ccpGuard.managers.HwidBan;
import l2p.gameserver.ccpGuard.managers.HwidInfo;
import l2p.gameserver.ccpGuard.managers.HwidManager;
import l2p.gameserver.ccpGuard.managers.ProtectManager;
import l2p.gameserver.handler.admincommands.IAdminCommandHandler;
import l2p.gameserver.model.GameObject;
import l2p.gameserver.model.Player;

import java.util.StringTokenizer;


public class AdminHWID implements IAdminCommandHandler {

    private static enum Commands {

        admin_hwid_ban,
        admin_hwid_reload,
        admin_hwid_count,
        admin_hwid_names,
        admin_hwid_windows,
        admin_hwid_lock_account,
        admin_hwid_lock_player
    }

    public boolean useAdminCommand(Enum comm, String[] wordList, String fullString, Player player) {
        Commands command = (Commands) comm;

        if (!ConfigProtect.PROTECT_ENABLE) {
            return false;
        }
        if (player == null) {
            return false;
        }
        if (!fullString.startsWith("admin_hwid")) {
            return false;
        }
        if (fullString.startsWith("admin_hwid_ban")) {

            GameObject plTarget = player.getTarget();
            if (plTarget == null && !(plTarget instanceof Player)) {
                player.sendMessage("Target is empty");
                return false;
            }
            Player target = (Player) plTarget;
            if (target != null) {
                HwidBan.addHwidBan(target.getNetConnection());
                player.sendMessage(target.getName() + " banned in HWID");
            } else {
                player.sendMessage("Target is not player");
            }
        } else if (fullString.startsWith("admin_hwid_reload")) {
            HwidBan.reload();
            player.sendMessage("HWID reload, " + HwidBan.getCountHwidBan() + " bans");
            HwidManager.reload();
            player.sendMessage("HwidManager reload, " + HwidManager.getCountHwidInfo() + " hwids");
        } else if (fullString.startsWith("admin_hwid_count")) {
            if (player.getTarget() != null && player.getTarget() instanceof Player) {
                Player target = (Player) player.getTarget();
                int count = ProtectManager.getInstance().getCountByHWID(target.getNetConnection()._prot_info.getHWID());
                player.sendMessage(target.getName() + " has " + count + " connections opened.");
            } else {
                player.sendMessage("Target is not player");
            }
        } else if (fullString.startsWith("admin_hwid_names")) {
            if (player.getTarget() != null && player.getTarget() instanceof Player) {
                Player target = (Player) player.getTarget();
                player.sendMessage("Here all character's names by targeted character HWID:");
                for (String name : ProtectManager.getInstance().getNamesByHWID(target.getNetConnection()._prot_info.getHWID())) {
                    player.sendMessage(name);
                }
            } else {
                player.sendMessage("Target is not player");
            }
        } else if (fullString.startsWith("admin_hwid_windows")) {
            try {
                StringTokenizer st = new StringTokenizer(fullString);
                if (st.countTokens() > 1) {
                    st.nextToken();
                    String countStr = st.nextToken();
                    int windowsCount = Integer.parseInt(countStr);
                    Player target = null;
                    if (player.getTarget() != null && player.getTarget() instanceof Player) {
                        target = (Player) player.getTarget();
                    } else {
                        player.sendMessage("Target is not player");
                    }
                    if (target != null) {
                        HwidManager.updateHwidInfo(target, windowsCount);
                        player.sendMessage(target.getName() + " set " + windowsCount + " allowed windows.");
                    } else {
                        player.sendMessage("Target is not player");
                    }
                }
            } catch (StringIndexOutOfBoundsException e) {
                player.sendMessage("Please specify new allowed windows count value.");
            }
        } else if (fullString.startsWith("admin_hwid_lock_account")) {
            if (!ConfigProtect.PROTECT_ENABLE_HWID_LOCK) {
                return false;
            }
            if (player.getTarget() != null && player.getTarget() instanceof Player) {
                Player target = (Player) player.getTarget();
                HwidManager.updateHwidInfo(target, HwidInfo.LockType.ACCOUNT_LOCK);
                player.sendMessage(target.getName() + " was locked (account lock)");
            } else {
                player.sendMessage("Target is not player");
            }
        } else if (fullString.startsWith("admin_hwid_lock_player")) {
            if (!ConfigProtect.PROTECT_ENABLE_HWID_LOCK) {
                return false;
            }
            if (player.getTarget() != null && player.getTarget() instanceof Player) {
                Player target = (Player) player.getTarget();
                HwidManager.updateHwidInfo(target, HwidInfo.LockType.PLAYER_LOCK);
                player.sendMessage(target.getName() + " was locked (account lock)");
            } else {
                player.sendMessage("Target is not player");
            }
        }
        return true;
    }

    public Enum[] getAdminCommandEnum() {
        return Commands.values();
    }
}
