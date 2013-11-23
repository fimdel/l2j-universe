package l2p.gameserver.network.telnet.commands;

import l2p.gameserver.Announcements;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.World;
import l2p.gameserver.network.serverpackets.Say2;
import l2p.gameserver.network.serverpackets.components.ChatType;
import l2p.gameserver.network.telnet.TelnetCommand;
import l2p.gameserver.network.telnet.TelnetCommandHolder;

import java.util.LinkedHashSet;
import java.util.Set;

public class TelnetSay implements TelnetCommandHolder {
    private Set<TelnetCommand> _commands = new LinkedHashSet<TelnetCommand>();

    public TelnetSay() {
        _commands.add(new TelnetCommand("announce", "ann") {
            @Override
            public String getUsage() {
                return "announce <text>";
            }

            @Override
            public String handle(String[] args) {
                if (args.length == 0)
                    return null;

                Announcements.getInstance().announceToAll(args[0]);

                return "Announcement sent.\n";
            }
        });

        _commands.add(new TelnetCommand("message", "msg") {
            @Override
            public String getUsage() {
                return "message <player> <text>";
            }

            @Override
            public String handle(String[] args) {
                if (args.length < 2)
                    return null;

                Player player = World.getPlayer(args[0]);
                if (player == null)
                    return "Player not found.\n";

                Say2 cs = new Say2(0, ChatType.TELL, "[Admin]", args[1]);
                player.sendPacket(cs);

                return "Message sent.\n";
            }

        });
    }

    @Override
    public Set<TelnetCommand> getCommands() {
        return _commands;
    }
}