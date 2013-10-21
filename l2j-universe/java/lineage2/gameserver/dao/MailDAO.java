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
package lineage2.gameserver.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import lineage2.commons.dao.JdbcDAO;
import lineage2.commons.dao.JdbcEntityState;
import lineage2.commons.dao.JdbcEntityStats;
import lineage2.commons.dbutils.DbUtils;
import lineage2.gameserver.database.DatabaseFactory;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.model.mail.Mail;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class MailDAO implements JdbcDAO<Integer, Mail>
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(MailDAO.class);
	/**
	 * Field RESTORE_MAIL. (value is ""SELECT sender_id, sender_name, receiver_id, receiver_name, expire_time, topic, body, price, type, unread, returnable, systemMsg1, systemMsg2 FROM mail WHERE message_id = ?"")
	 */
	private final static String RESTORE_MAIL = "SELECT sender_id, sender_name, receiver_id, receiver_name, expire_time, topic, body, price, type, unread, returnable, systemMsg1, systemMsg2 FROM mail WHERE message_id = ?";
	/**
	 * Field STORE_MAIL. (value is ""INSERT INTO mail(sender_id, sender_name, receiver_id, receiver_name, expire_time, topic, body, price, type, unread, returnable, systemMsg1, systemMsg2) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)"")
	 */
	private final static String STORE_MAIL = "INSERT INTO mail(sender_id, sender_name, receiver_id, receiver_name, expire_time, topic, body, price, type, unread, returnable, systemMsg1, systemMsg2) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
	/**
	 * Field UPDATE_MAIL. (value is ""UPDATE mail SET sender_id = ?, sender_name = ?, receiver_id = ?, receiver_name = ?, expire_time = ?, topic = ?, body = ?, price = ?, type = ?, unread = ?, returnable = ?, systemMsg1 = ?, systemMsg2 = ? WHERE message_id = ?"")
	 */
	private final static String UPDATE_MAIL = "UPDATE mail SET sender_id = ?, sender_name = ?, receiver_id = ?, receiver_name = ?, expire_time = ?, topic = ?, body = ?, price = ?, type = ?, unread = ?, returnable = ?, systemMsg1 = ?, systemMsg2 = ? WHERE message_id = ?";
	/**
	 * Field REMOVE_MAIL. (value is ""DELETE FROM mail WHERE message_id = ?"")
	 */
	private final static String REMOVE_MAIL = "DELETE FROM mail WHERE message_id = ?";
	/**
	 * Field RESTORE_EXPIRED_MAIL. (value is ""SELECT message_id FROM mail WHERE expire_time <= ?"")
	 */
	private final static String RESTORE_EXPIRED_MAIL = "SELECT message_id FROM mail WHERE expire_time <= ?";
	/**
	 * Field RESTORE_OWN_MAIL. (value is ""SELECT message_id FROM character_mail WHERE char_id = ? AND is_sender = ?"")
	 */
	private final static String RESTORE_OWN_MAIL = "SELECT message_id FROM character_mail WHERE char_id = ? AND is_sender = ?";
	/**
	 * Field STORE_OWN_MAIL. (value is ""INSERT INTO character_mail(char_id, message_id, is_sender) VALUES (?,?,?)"")
	 */
	private final static String STORE_OWN_MAIL = "INSERT INTO character_mail(char_id, message_id, is_sender) VALUES (?,?,?)";
	/**
	 * Field REMOVE_OWN_MAIL. (value is ""DELETE FROM character_mail WHERE char_id = ? AND message_id = ? AND is_sender = ?"")
	 */
	private final static String REMOVE_OWN_MAIL = "DELETE FROM character_mail WHERE char_id = ? AND message_id = ? AND is_sender = ?";
	/**
	 * Field RESTORE_MAIL_ATTACHMENTS. (value is ""SELECT item_id FROM mail_attachments WHERE message_id = ?"")
	 */
	private final static String RESTORE_MAIL_ATTACHMENTS = "SELECT item_id FROM mail_attachments WHERE message_id = ?";
	/**
	 * Field STORE_MAIL_ATTACHMENT. (value is ""INSERT INTO mail_attachments(message_id, item_id) VALUES (?,?)"")
	 */
	private final static String STORE_MAIL_ATTACHMENT = "INSERT INTO mail_attachments(message_id, item_id) VALUES (?,?)";
	/**
	 * Field REMOVE_MAIL_ATTACHMENTS. (value is ""DELETE FROM mail_attachments WHERE message_id = ?"")
	 */
	private final static String REMOVE_MAIL_ATTACHMENTS = "DELETE FROM mail_attachments WHERE message_id = ?";
	/**
	 * Field instance.
	 */
	private final static MailDAO instance = new MailDAO();
	
	/**
	 * Method getInstance.
	 * @return MailDAO
	 */
	public static MailDAO getInstance()
	{
		return instance;
	}
	
	/**
	 * Field load.
	 */
	final AtomicLong load = new AtomicLong();
	/**
	 * Field insert.
	 */
	final AtomicLong insert = new AtomicLong();
	/**
	 * Field update.
	 */
	final AtomicLong update = new AtomicLong();
	/**
	 * Field delete.
	 */
	final AtomicLong delete = new AtomicLong();
	/**
	 * Field cache.
	 */
	private final Cache cache;
	/**
	 * Field stats.
	 */
	private final JdbcEntityStats stats = new JdbcEntityStats()
	{
		@Override
		public long getLoadCount()
		{
			return load.get();
		}
		
		@Override
		public long getInsertCount()
		{
			return insert.get();
		}
		
		@Override
		public long getUpdateCount()
		{
			return update.get();
		}
		
		@Override
		public long getDeleteCount()
		{
			return delete.get();
		}
	};
	
	/**
	 * Constructor for MailDAO.
	 */
	private MailDAO()
	{
		cache = CacheManager.getInstance().getCache(Mail.class.getName());
	}
	
	/**
	 * Method getCache.
	 * @return Cache
	 */
	public Cache getCache()
	{
		return cache;
	}
	
	/**
	 * Method getStats.
	 * @return JdbcEntityStats * @see lineage2.commons.dao.JdbcDAO#getStats()
	 */
	@Override
	public JdbcEntityStats getStats()
	{
		return stats;
	}
	
	/**
	 * Method save0.
	 * @param mail Mail
	 * @throws SQLException
	 */
	private void save0(Mail mail) throws SQLException
	{
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(STORE_MAIL, Statement.RETURN_GENERATED_KEYS);
			statement.setInt(1, mail.getSenderId());
			statement.setString(2, mail.getSenderName());
			statement.setInt(3, mail.getReceiverId());
			statement.setString(4, mail.getReceiverName());
			statement.setInt(5, mail.getExpireTime());
			statement.setString(6, mail.getTopic());
			statement.setString(7, mail.getBody());
			statement.setLong(8, mail.getPrice());
			statement.setInt(9, mail.getType().ordinal());
			statement.setBoolean(10, mail.isUnread());
			statement.setBoolean(11, mail.isReturnable());
			statement.setInt(12, mail.getSystemMsg1());
			statement.setInt(13, mail.getSystemMsg2());
			statement.execute();
			rset = statement.getGeneratedKeys();
			rset.next();
			mail.setMessageId(rset.getInt(1));
			if (!mail.getAttachments().isEmpty())
			{
				DbUtils.close(statement);
				statement = con.prepareStatement(STORE_MAIL_ATTACHMENT);
				for (ItemInstance item : mail.getAttachments())
				{
					statement.setInt(1, mail.getMessageId());
					statement.setInt(2, item.getObjectId());
					statement.addBatch();
				}
				statement.executeBatch();
			}
			DbUtils.close(statement);
			if (mail.getType() == Mail.SenderType.NORMAL)
			{
				statement = con.prepareStatement(STORE_OWN_MAIL);
				statement.setInt(1, mail.getSenderId());
				statement.setInt(2, mail.getMessageId());
				statement.setBoolean(3, true);
				statement.execute();
			}
			DbUtils.close(statement);
			statement = con.prepareStatement(STORE_OWN_MAIL);
			statement.setInt(1, mail.getReceiverId());
			statement.setInt(2, mail.getMessageId());
			statement.setBoolean(3, false);
			statement.execute();
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
		insert.incrementAndGet();
	}
	
	/**
	 * Method load0.
	 * @param messageId int
	 * @return Mail
	 */
	private Mail load0(int messageId)
	{
		Mail mail = null;
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(RESTORE_MAIL);
			statement.setInt(1, messageId);
			rset = statement.executeQuery();
			if (rset.next())
			{
				mail = new Mail();
				mail.setMessageId(messageId);
				mail.setSenderId(rset.getInt(1));
				mail.setSenderName(rset.getString(2));
				mail.setReceiverId(rset.getInt(3));
				mail.setReceiverName(rset.getString(4));
				mail.setExpireTime(rset.getInt(5));
				mail.setTopic(rset.getString(6));
				mail.setBody(rset.getString(7));
				mail.setPrice(rset.getLong(8));
				mail.setType(Mail.SenderType.VALUES[rset.getInt(9)]);
				mail.setUnread(rset.getBoolean(10));
				mail.setReturnable(rset.getBoolean(11));
				mail.setSystemMsg1(rset.getInt(12));
				mail.setSystemMsg2(rset.getInt(13));
				DbUtils.close(statement, rset);
				statement = con.prepareStatement(RESTORE_MAIL_ATTACHMENTS);
				statement.setInt(1, messageId);
				rset = statement.executeQuery();
				ItemInstance item;
				int objectId;
				while (rset.next())
				{
					objectId = rset.getInt(1);
					item = ItemsDAO.getInstance().load(objectId);
					if (item != null)
					{
						mail.addAttachment(item);
					}
				}
			}
		}
		catch (SQLException e)
		{
			_log.error("Error while restoring mail : " + messageId, e);
			return null;
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
		load.incrementAndGet();
		return mail;
	}
	
	/**
	 * Method update0.
	 * @param mail Mail
	 * @throws SQLException
	 */
	private void update0(Mail mail) throws SQLException
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(UPDATE_MAIL);
			statement.setInt(1, mail.getSenderId());
			statement.setString(2, mail.getSenderName());
			statement.setInt(3, mail.getReceiverId());
			statement.setString(4, mail.getReceiverName());
			statement.setInt(5, mail.getExpireTime());
			statement.setString(6, mail.getTopic());
			statement.setString(7, mail.getBody());
			statement.setLong(8, mail.getPrice());
			statement.setInt(9, mail.getType().ordinal());
			statement.setBoolean(10, mail.isUnread());
			statement.setBoolean(11, mail.isReturnable());
			statement.setInt(12, mail.getMessageId());
			statement.setInt(13, mail.getSystemMsg1());
			statement.setInt(14, mail.getSystemMsg2());
			statement.execute();
			if (mail.getAttachments().isEmpty())
			{
				DbUtils.close(statement);
				statement = con.prepareStatement(REMOVE_MAIL_ATTACHMENTS);
				statement.setInt(1, mail.getMessageId());
				statement.execute();
			}
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
		update.incrementAndGet();
	}
	
	/**
	 * Method delete0.
	 * @param mail Mail
	 * @throws SQLException
	 */
	private void delete0(Mail mail) throws SQLException
	{
		try (
				Connection con = DatabaseFactory.getInstance().getConnection();
				PreparedStatement statement = con.prepareStatement(REMOVE_OWN_MAIL);
		)
		{
			//ADDED FOR MAIL FIX
			statement.setInt(1, mail.getSenderId());
			statement.setInt(2, mail.getMessageId());
			statement.setInt(3, 1);
			statement.execute();
		}
		try (
				Connection con = DatabaseFactory.getInstance().getConnection();
				PreparedStatement statement = con.prepareStatement(REMOVE_OWN_MAIL);
		)
		{
			statement.setInt(1, mail.getReceiverId());
			statement.setInt(2, mail.getMessageId());
			statement.setInt(3, 0);
			statement.execute();
		}
		try (
				Connection con = DatabaseFactory.getInstance().getConnection();
				PreparedStatement statement = con.prepareStatement(REMOVE_MAIL);
		)
		{
			//END
			statement.setInt(1, mail.getMessageId());
			statement.execute();
		}
		delete.incrementAndGet();
	}
	
	/**
	 * Method getMailByOwnerId.
	 * @param ownerId int
	 * @param sent boolean
	 * @return List<Mail>
	 */
	private List<Mail> getMailByOwnerId(int ownerId, boolean sent)
	{
		List<Integer> messageIds = Collections.emptyList();
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(RESTORE_OWN_MAIL);
			statement.setInt(1, ownerId);
			statement.setBoolean(2, sent);
			rset = statement.executeQuery();
			messageIds = new ArrayList<>();
			while (rset.next())
			{
				messageIds.add(rset.getInt(1));
			}
		}
		catch (SQLException e)
		{
			_log.error("Error while restore mail of owner : " + ownerId, e);
			messageIds.clear();
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
		return load(messageIds);
	}
	
	/**
	 * Method deleteMailByOwnerIdAndMailId.
	 * @param ownerId int
	 * @param messageId int
	 * @param sent boolean
	 * @return boolean
	 */
	private boolean deleteMailByOwnerIdAndMailId(int ownerId, int messageId, boolean sent)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(REMOVE_OWN_MAIL);
			statement.setInt(1, ownerId);
			statement.setInt(2, messageId);
			statement.setBoolean(3, sent);
			return statement.execute();
		}
		catch (SQLException e)
		{
			_log.error("Error while deleting mail of owner : " + ownerId, e);
			return false;
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
	
	/**
	 * Method getReceivedMailByOwnerId.
	 * @param receiverId int
	 * @return List<Mail>
	 */
	public List<Mail> getReceivedMailByOwnerId(int receiverId)
	{
		return getMailByOwnerId(receiverId, false);
	}
	
	/**
	 * Method getSentMailByOwnerId.
	 * @param senderId int
	 * @return List<Mail>
	 */
	public List<Mail> getSentMailByOwnerId(int senderId)
	{
		return getMailByOwnerId(senderId, true);
	}
	
	/**
	 * Method getReceivedMailByMailId.
	 * @param receiverId int
	 * @param messageId int
	 * @return Mail
	 */
	public Mail getReceivedMailByMailId(int receiverId, int messageId)
	{
		List<Mail> list = getMailByOwnerId(receiverId, false);
		for (Mail mail : list)
		{
			if (mail.getMessageId() == messageId)
			{
				return mail;
			}
		}
		return null;
	}
	
	/**
	 * Method getSentMailByMailId.
	 * @param senderId int
	 * @param messageId int
	 * @return Mail
	 */
	public Mail getSentMailByMailId(int senderId, int messageId)
	{
		List<Mail> list = getMailByOwnerId(senderId, true);
		for (Mail mail : list)
		{
			if (mail.getMessageId() == messageId)
			{
				return mail;
			}
		}
		return null;
	}
	
	/**
	 * Method deleteReceivedMailByMailId.
	 * @param receiverId int
	 * @param messageId int
	 * @return boolean
	 */
	public boolean deleteReceivedMailByMailId(int receiverId, int messageId)
	{
		return deleteMailByOwnerIdAndMailId(receiverId, messageId, false);
	}
	
	/**
	 * Method deleteSentMailByMailId.
	 * @param senderId int
	 * @param messageId int
	 * @return boolean
	 */
	public boolean deleteSentMailByMailId(int senderId, int messageId)
	{
		return deleteMailByOwnerIdAndMailId(senderId, messageId, true);
	}
	
	/**
	 * Method getExpiredMail.
	 * @param expireTime int
	 * @return List<Mail>
	 */
	public List<Mail> getExpiredMail(int expireTime)
	{
		List<Integer> messageIds = Collections.emptyList();
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(RESTORE_EXPIRED_MAIL);
			statement.setInt(1, expireTime);
			rset = statement.executeQuery();
			messageIds = new ArrayList<>();
			while (rset.next())
			{
				messageIds.add(rset.getInt(1));
			}
		}
		catch (SQLException e)
		{
			_log.error("Error while restore expired mail!", e);
			messageIds.clear();
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
		return load(messageIds);
	}
	
	/**
	 * Method load.
	 * @param id Integer
	 * @return Mail
	 */
	@Override
	public Mail load(Integer id)
	{
		Mail mail;
		Element ce = cache.get(id);
		if (ce != null)
		{
			mail = (Mail) ce.getObjectValue();
			return mail;
		}
		mail = load0(id);
		if (mail==null)
		{
			_log.warn("Mail load error id:" + id);
		}
		else
		{
			mail.setJdbcState(JdbcEntityState.STORED);
			cache.put(new Element(mail.getMessageId(), mail));
		}
		return mail;
	}
	
	/**
	 * Method load.
	 * @param messageIds Collection<Integer>
	 * @return List<Mail>
	 */
	public List<Mail> load(Collection<Integer> messageIds)
	{
		if (messageIds.isEmpty())
		{
			return Collections.emptyList();
		}
		List<Mail> list = new ArrayList<>(messageIds.size());
		Mail mail;
		for (Integer messageId : messageIds)
		{
			mail = load(messageId);
			if (mail != null)
			{
				list.add(mail);
			}
		}
		return list;
	}
	
	/**
	 * Method save.
	 * @param mail Mail
	 */
	@Override
	public void save(Mail mail)
	{
		if (!mail.getJdbcState().isSavable())
		{
			return;
		}
		try
		{
			save0(mail);
			mail.setJdbcState(JdbcEntityState.STORED);
		}
		catch (SQLException e)
		{
			_log.error("Error while saving mail!", e);
			return;
		}
		cache.put(new Element(mail.getMessageId(), mail));
	}
	
	/**
	 * Method update.
	 * @param mail Mail
	 */
	@Override
	public void update(Mail mail)
	{
		if (!mail.getJdbcState().isUpdatable())
		{
			return;
		}
		try
		{
			update0(mail);
			mail.setJdbcState(JdbcEntityState.STORED);
		}
		catch (SQLException e)
		{
			_log.error("Error while updating mail : " + mail.getMessageId(), e);
			return;
		}
		cache.putIfAbsent(new Element(mail.getMessageId(), mail));
	}
	
	/**
	 * Method saveOrUpdate.
	 * @param mail Mail
	 */
	@Override
	public void saveOrUpdate(Mail mail)
	{
		if (mail.getJdbcState().isSavable())
		{
			save(mail);
		}
		else if (mail.getJdbcState().isUpdatable())
		{
			update(mail);
		}
	}
	
	/**
	 * Method delete.
	 * @param mail Mail
	 */
	@Override
	public void delete(Mail mail)
	{
		if (!mail.getJdbcState().isDeletable())
		{
			return;
		}
		try
		{
			delete0(mail);
			mail.setJdbcState(JdbcEntityState.DELETED);
		}
		catch (SQLException e)
		{
			_log.error("Error while deleting mail : " + mail.getMessageId(), e);
			return;
		}
		cache.remove(mail.getExpireTime());
	}
}
