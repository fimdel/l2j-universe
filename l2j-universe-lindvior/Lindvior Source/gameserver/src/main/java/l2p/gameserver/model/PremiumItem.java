/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.model;

/**
 * @author Gnacik
 * @corrected by n0nam3
 */
public class PremiumItem {
    private int _itemId;
    private long _count;
    private String _sender;
    //private String _senderMessage;

    public PremiumItem(int itemid, long count, String sender) {//, String senderMessage) {
        _itemId = itemid;
        _count = count;
        _sender = sender;
        //  _senderMessage = senderMessage;
    }

    public void updateCount(long newcount) {
        _count = newcount;
    }

    public int getItemId() {
        return _itemId;
    }

    public long getCount() {
        return _count;
    }

    public String getSender() {
        return _sender;
    }

    //  public String getSenderMessage()
    //   {
//        return _senderMessage;
    //   }
}