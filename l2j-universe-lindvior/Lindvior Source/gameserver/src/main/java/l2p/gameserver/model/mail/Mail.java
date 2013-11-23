package l2p.gameserver.model.mail;

import l2p.commons.dao.JdbcEntity;
import l2p.commons.dao.JdbcEntityState;
import l2p.gameserver.dao.MailDAO;
import l2p.gameserver.model.items.ItemInstance;

import java.util.HashSet;
import java.util.Set;


public class Mail implements JdbcEntity, Comparable<Mail> {
    public int getSystemMsg1() {
        return systemMsg1;
    }

    public void setSystemMsg1(int systemMsg1) {
        this.systemMsg1 = systemMsg1;
    }

    public int getSystemMsg2() {
        return systemMsg2;
    }

    public void setSystemMsg2(int systemMsg2) {
        this.systemMsg2 = systemMsg2;
    }

    public static enum SenderType {
        NORMAL,// 0
        NEWS_INFORMER,// 1
        NONE,// 2
        BIRTHDAY,// 3
        UNKNOWN,// 4
        SYSTEM,// 5
        MENTOR,// 6
        PRESENT;// 7 (для подарков, отправленных из итем молла)

        public static SenderType[] VALUES = values();
    }

    private static final long serialVersionUID = -8704970972611917153L;

    public static final int DELETED = 0;
    public static final int READED = 1;
    public static final int REJECTED = 2;

    private static final MailDAO _mailDAO = MailDAO.getInstance();

    private int messageId;
    private int senderId;
    private String senderName;
    private int receiverId;
    private String receiverName;
    private int expireTime;
    private String topic;
    private String body;
    private long price;
    private SenderType _type = SenderType.NORMAL;
    private boolean isUnread;
    private boolean isReturnable;
    private Set<ItemInstance> attachments = new HashSet<ItemInstance>();

    private int systemMsg1, systemMsg2;

    private JdbcEntityState _state = JdbcEntityState.CREATED;

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public int getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(int expireTime) {
        this.expireTime = expireTime;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public boolean isPayOnDelivery() {
        return price > 0L;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public boolean isUnread() {
        return isUnread;
    }

    public void setUnread(boolean isUnread) {
        this.isUnread = isUnread;
    }

    public boolean isReturnable() {
        return isReturnable;
    }

    public void setReturnable(boolean isReturnable) {
        this.isReturnable = isReturnable;
    }

    public Set<ItemInstance> getAttachments() {
        return attachments;
    }

    public void addAttachment(ItemInstance item) {
        attachments.add(item);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (o == null)
            return false;
        if (o.getClass() != this.getClass())
            return false;
        return ((Mail) o).getMessageId() == getMessageId();
    }

    @Override
    public void setJdbcState(JdbcEntityState state) {
        _state = state;
    }

    @Override
    public JdbcEntityState getJdbcState() {
        return _state;
    }

    public void save() {
        _mailDAO.save(this);
    }

    public void update() {
        _mailDAO.update(this);
    }

    public void delete() {
        _mailDAO.delete(this);
    }

    public Mail reject() {
        Mail mail = new Mail();
        mail.setSenderId(getReceiverId());
        mail.setSenderName(getReceiverName());
        mail.setReceiverId(getSenderId());
        mail.setReceiverName(getSenderName());
        mail.setTopic(getTopic());
        mail.setBody(getBody());
        mail.setReturnable(false);
        synchronized (getAttachments()) {
            for (ItemInstance item : getAttachments())
                mail.addAttachment(item);
            getAttachments().clear();
        }
        mail.setType(SenderType.NORMAL);
        mail.setUnread(true);
        return mail;
    }

    public Mail reply() {
        Mail mail = new Mail();
        mail.setSenderId(getReceiverId());
        mail.setSenderName(getReceiverName());
        mail.setReceiverId(getSenderId());
        mail.setReceiverName(getSenderName());
        mail.setTopic("[Re]" + getTopic());
        mail.setBody(getBody());
        mail.setType(SenderType.NEWS_INFORMER);
        mail.setUnread(true);
        return mail;
    }

    @Override
    public int compareTo(Mail o) {
        return o.getMessageId() - this.getMessageId();
    }

    public SenderType getType() {
        return _type;
    }

    public void setType(SenderType type) {
        _type = type;
    }
}
