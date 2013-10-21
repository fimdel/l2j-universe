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
package lineage2.gameserver.model.mail;

import java.util.HashSet;
import java.util.Set;

import lineage2.commons.dao.JdbcEntity;
import lineage2.commons.dao.JdbcEntityState;
import lineage2.gameserver.dao.MailDAO;
import lineage2.gameserver.model.items.ItemInstance;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Mail implements JdbcEntity, Comparable<Mail>
{
	/**
	 * Method getSystemMsg1.
	 * @return int
	 */
	public int getSystemMsg1()
	{
		return systemMsg1;
	}
	
	/**
	 * Method setSystemMsg1.
	 * @param systemMsg1 int
	 */
	public void setSystemMsg1(int systemMsg1)
	{
		this.systemMsg1 = systemMsg1;
	}
	
	/**
	 * Method getSystemMsg2.
	 * @return int
	 */
	public int getSystemMsg2()
	{
		return systemMsg2;
	}
	
	/**
	 * Method setSystemMsg2.
	 * @param systemMsg2 int
	 */
	public void setSystemMsg2(int systemMsg2)
	{
		this.systemMsg2 = systemMsg2;
	}
	
	/**
	 * @author Mobius
	 */
	public static enum SenderType
	{
		/**
		 * Field NORMAL.
		 */
		NORMAL,
		/**
		 * Field NEWS_INFORMER.
		 */
		NEWS_INFORMER,
		/**
		 * Field NONE.
		 */
		NONE,
		/**
		 * Field BIRTHDAY.
		 */
		BIRTHDAY,
		/**
		 * Field UNKNOWN.
		 */
		UNKNOWN,
		/**
		 * Field SYSTEM.
		 */
		SYSTEM,
		/**
		 * Field MENTOR.
		 */
		MENTOR,
		/**
		 * Field PRESENT.
		 */
		PRESENT;
		/**
		 * Field VALUES.
		 */
		public static SenderType[] VALUES = values();
	}
	
	/**
	 * Field serialVersionUID. (value is -8704970972611917153)
	 */
	private static final long serialVersionUID = -8704970972611917153L;
	/**
	 * Field DELETED. (value is 0)
	 */
	public static final int DELETED = 0;
	/**
	 * Field READED. (value is 1)
	 */
	public static final int READED = 1;
	/**
	 * Field REJECTED. (value is 2)
	 */
	public static final int REJECTED = 2;
	/**
	 * Field _mailDAO.
	 */
	private static final MailDAO _mailDAO = MailDAO.getInstance();
	/**
	 * Field messageId.
	 */
	private int messageId;
	/**
	 * Field senderId.
	 */
	private int senderId;
	/**
	 * Field senderName.
	 */
	private String senderName;
	/**
	 * Field receiverId.
	 */
	private int receiverId;
	/**
	 * Field receiverName.
	 */
	private String receiverName;
	/**
	 * Field expireTime.
	 */
	private int expireTime;
	/**
	 * Field topic.
	 */
	private String topic;
	/**
	 * Field body.
	 */
	private String body;
	/**
	 * Field price.
	 */
	private long price;
	/**
	 * Field _type.
	 */
	private SenderType _type = SenderType.NORMAL;
	/**
	 * Field isUnread.
	 */
	private boolean isUnread;
	/**
	 * Field isReturnable.
	 */
	private boolean isReturnable;
	/**
	 * Field attachments.
	 */
	private final Set<ItemInstance> attachments = new HashSet<>();
	/**
	 * Field systemMsg2. Field systemMsg1.
	 */
	private int systemMsg1, systemMsg2;
	/**
	 * Field _state.
	 */
	private JdbcEntityState _state = JdbcEntityState.CREATED;
	
	/**
	 * Method getMessageId.
	 * @return int
	 */
	public int getMessageId()
	{
		return messageId;
	}
	
	/**
	 * Method setMessageId.
	 * @param messageId int
	 */
	public void setMessageId(int messageId)
	{
		this.messageId = messageId;
	}
	
	/**
	 * Method getSenderId.
	 * @return int
	 */
	public int getSenderId()
	{
		return senderId;
	}
	
	/**
	 * Method setSenderId.
	 * @param senderId int
	 */
	public void setSenderId(int senderId)
	{
		this.senderId = senderId;
	}
	
	/**
	 * Method getSenderName.
	 * @return String
	 */
	public String getSenderName()
	{
		return senderName;
	}
	
	/**
	 * Method setSenderName.
	 * @param senderName String
	 */
	public void setSenderName(String senderName)
	{
		this.senderName = senderName;
	}
	
	/**
	 * Method getReceiverId.
	 * @return int
	 */
	public int getReceiverId()
	{
		return receiverId;
	}
	
	/**
	 * Method setReceiverId.
	 * @param receiverId int
	 */
	public void setReceiverId(int receiverId)
	{
		this.receiverId = receiverId;
	}
	
	/**
	 * Method getReceiverName.
	 * @return String
	 */
	public String getReceiverName()
	{
		return receiverName;
	}
	
	/**
	 * Method setReceiverName.
	 * @param receiverName String
	 */
	public void setReceiverName(String receiverName)
	{
		this.receiverName = receiverName;
	}
	
	/**
	 * Method getExpireTime.
	 * @return int
	 */
	public int getExpireTime()
	{
		return expireTime;
	}
	
	/**
	 * Method setExpireTime.
	 * @param expireTime int
	 */
	public void setExpireTime(int expireTime)
	{
		this.expireTime = expireTime;
	}
	
	/**
	 * Method getTopic.
	 * @return String
	 */
	public String getTopic()
	{
		return topic;
	}
	
	/**
	 * Method setTopic.
	 * @param topic String
	 */
	public void setTopic(String topic)
	{
		this.topic = topic;
	}
	
	/**
	 * Method getBody.
	 * @return String
	 */
	public String getBody()
	{
		return body;
	}
	
	/**
	 * Method setBody.
	 * @param body String
	 */
	public void setBody(String body)
	{
		this.body = body;
	}
	
	/**
	 * Method isPayOnDelivery.
	 * @return boolean
	 */
	public boolean isPayOnDelivery()
	{
		return price > 0L;
	}
	
	/**
	 * Method getPrice.
	 * @return long
	 */
	public long getPrice()
	{
		return price;
	}
	
	/**
	 * Method setPrice.
	 * @param price long
	 */
	public void setPrice(long price)
	{
		this.price = price;
	}
	
	/**
	 * Method isUnread.
	 * @return boolean
	 */
	public boolean isUnread()
	{
		return isUnread;
	}
	
	/**
	 * Method setUnread.
	 * @param isUnread boolean
	 */
	public void setUnread(boolean isUnread)
	{
		this.isUnread = isUnread;
	}
	
	/**
	 * Method isReturnable.
	 * @return boolean
	 */
	public boolean isReturnable()
	{
		return isReturnable;
	}
	
	/**
	 * Method setReturnable.
	 * @param isReturnable boolean
	 */
	public void setReturnable(boolean isReturnable)
	{
		this.isReturnable = isReturnable;
	}
	
	/**
	 * Method getAttachments.
	 * @return Set<ItemInstance>
	 */
	public Set<ItemInstance> getAttachments()
	{
		return attachments;
	}
	
	/**
	 * Method addAttachment.
	 * @param item ItemInstance
	 */
	public void addAttachment(ItemInstance item)
	{
		attachments.add(item);
	}
	
	/**
	 * Method equals.
	 * @param o Object
	 * @return boolean
	 */
	@Override
	public boolean equals(Object o)
	{
		if (o == this)
		{
			return true;
		}
		if (o == null)
		{
			return false;
		}
		if (o.getClass() != this.getClass())
		{
			return false;
		}
		return ((Mail) o).getMessageId() == getMessageId();
	}
	
	/**
	 * Method setJdbcState.
	 * @param state JdbcEntityState
	 * @see lineage2.commons.dao.JdbcEntity#setJdbcState(JdbcEntityState)
	 */
	@Override
	public void setJdbcState(JdbcEntityState state)
	{
		_state = state;
	}
	
	/**
	 * Method getJdbcState.
	 * @return JdbcEntityState * @see lineage2.commons.dao.JdbcEntity#getJdbcState()
	 */
	@Override
	public JdbcEntityState getJdbcState()
	{
		return _state;
	}
	
	/**
	 * Method save.
	 * @see lineage2.commons.dao.JdbcEntity#save()
	 */
	@Override
	public void save()
	{
		_mailDAO.save(this);
	}
	
	/**
	 * Method update.
	 * @see lineage2.commons.dao.JdbcEntity#update()
	 */
	@Override
	public void update()
	{
		_mailDAO.update(this);
	}
	
	/**
	 * Method delete.
	 * @see lineage2.commons.dao.JdbcEntity#delete()
	 */
	@Override
	public void delete()
	{
		_mailDAO.delete(this);
	}
	
	/**
	 * Method reject.
	 * @return Mail
	 */
	public Mail reject()
	{
		Mail mail = new Mail();
		mail.setSenderId(getReceiverId());
		mail.setSenderName(getReceiverName());
		mail.setReceiverId(getSenderId());
		mail.setReceiverName(getSenderName());
		mail.setTopic(getTopic());
		mail.setBody(getBody());
		mail.setReturnable(false);
		synchronized (getAttachments())
		{
			for (ItemInstance item : getAttachments())
			{
				mail.addAttachment(item);
			}
			getAttachments().clear();
		}
		mail.setType(SenderType.NORMAL);
		mail.setUnread(true);
		return mail;
	}
	
	/**
	 * Method reply.
	 * @return Mail
	 */
	public Mail reply()
	{
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
	
	/**
	 * Method compareTo.
	 * @param o Mail
	 * @return int
	 */
	@Override
	public int compareTo(Mail o)
	{
		return o.getMessageId() - getMessageId();
	}
	
	/**
	 * Method getType.
	 * @return SenderType
	 */
	public SenderType getType()
	{
		return _type;
	}
	
	/**
	 * Method setType.
	 * @param type SenderType
	 */
	public void setType(SenderType type)
	{
		_type = type;
	}
}
