package l2p.gameserver.network.clientpackets;

public class SnoopQuit extends L2GameClientPacket {
    @SuppressWarnings("unused")
    private int _snoopID;

    /**
     * format: cd
     */
    @Override
    protected void readImpl() {
        _snoopID = readD();
    }

    @Override
    protected void runImpl() {
        /**
         L2Player player = (L2Player) L2World.findObject(_snoopID);
         if(player == null)
         return;
         L2Player activeChar = getClient().getActiveChar();
         if(activeChar == null)
         return;
         player.removeSnooper(activeChar);
         activeChar.removeSnooped(player);
         */
    }
}