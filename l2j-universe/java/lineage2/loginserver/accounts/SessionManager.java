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
package lineage2.loginserver.accounts;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import lineage2.commons.threading.RunnableImpl;
import lineage2.loginserver.Config;
import lineage2.loginserver.SessionKey;
import lineage2.loginserver.ThreadPoolManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class SessionManager
{
	/**
	 * Field _log.
	 */
	@SuppressWarnings("unused")
	private static final Logger _log = LoggerFactory.getLogger(SessionManager.class);
	/**
	 * Field _instance.
	 */
	private static final SessionManager _instance = new SessionManager();
	
	/**
	 * Method getInstance.
	 * @return SessionManager
	 */
	public static final SessionManager getInstance()
	{
		return _instance;
	}
	
	/**
	 * @author Mobius
	 */
	public final class Session
	{
		/**
		 * Field account.
		 */
		final Account account;
		/**
		 * Field skey.
		 */
		private final SessionKey skey;
		/**
		 * Field expireTime.
		 */
		private final long expireTime;
		
		/**
		 * Constructor for Session.
		 * @param account Account
		 */
		Session(Account account)
		{
			this.account = account;
			skey = SessionKey.create();
			expireTime = System.currentTimeMillis() + Config.LOGIN_TIMEOUT;
		}
		
		/**
		 * Method getSessionKey.
		 * @return SessionKey
		 */
		public SessionKey getSessionKey()
		{
			return skey;
		}
		
		/**
		 * Method getAccount.
		 * @return Account
		 */
		public Account getAccount()
		{
			return account;
		}
		
		/**
		 * Method getExpireTime.
		 * @return long
		 */
		public long getExpireTime()
		{
			return expireTime;
		}
	}
	
	/**
	 * Field sessions.
	 */
	final Map<SessionKey, Session> sessions = new HashMap<>();
	/**
	 * Field lock.
	 */
	final Lock lock = new ReentrantLock();
	
	/**
	 * Constructor for SessionManager.
	 */
	private SessionManager()
	{
		ThreadPoolManager.getInstance().scheduleAtFixedRate(new RunnableImpl()
		{
			@Override
			public void runImpl()
			{
				lock.lock();
				try
				{
					long currentMillis = System.currentTimeMillis();
					Session session;
					for (Iterator<Session> itr = sessions.values().iterator(); itr.hasNext();)
					{
						session = itr.next();
						if (session.getExpireTime() < currentMillis)
						{
							itr.remove();
						}
					}
				}
				finally
				{
					lock.unlock();
				}
			}
		}, 30000L, 30000L);
	}
	
	/**
	 * Method openSession.
	 * @param account Account
	 * @return Session
	 */
	public Session openSession(Account account)
	{
		lock.lock();
		try
		{
			Session session = new Session(account);
			sessions.put(session.getSessionKey(), session);
			return session;
		}
		finally
		{
			lock.unlock();
		}
	}
	
	/**
	 * Method closeSession.
	 * @param skey SessionKey
	 * @return Session
	 */
	public Session closeSession(SessionKey skey)
	{
		lock.lock();
		try
		{
			return sessions.remove(skey);
		}
		finally
		{
			lock.unlock();
		}
	}
	
	/**
	 * Method getSessionByName.
	 * @param name String
	 * @return Session
	 */
	public Session getSessionByName(String name)
	{
		for (Session session : sessions.values())
		{
			if (session.account.getLogin().equalsIgnoreCase(name))
			{
				return session;
			}
		}
		return null;
	}
}
