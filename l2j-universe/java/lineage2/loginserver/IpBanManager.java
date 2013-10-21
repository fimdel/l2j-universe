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
package lineage2.loginserver;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class IpBanManager
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(IpBanManager.class);
	/**
	 * Field _instance.
	 */
	private static final IpBanManager _instance = new IpBanManager();
	
	/**
	 * Method getInstance.
	 * @return IpBanManager
	 */
	public static final IpBanManager getInstance()
	{
		return _instance;
	}
	
	/**
	 * @author Mobius
	 */
	private class IpSession
	{
		/**
		 * Constructor for IpSession.
		 */
		public IpSession()
		{
			// TODO Auto-generated constructor stub
		}
		
		/**
		 * Field tryCount.
		 */
		public int tryCount;
		/**
		 * Field lastTry.
		 */
		public long lastTry;
		/**
		 * Field banExpire.
		 */
		public long banExpire;
	}
	
	/**
	 * Field ips.
	 */
	final Map<String, IpSession> ips = new HashMap<>();
	/**
	 * Field lock.
	 */
	private final ReadWriteLock lock = new ReentrantReadWriteLock();
	/**
	 * Field readLock.
	 */
	private final Lock readLock = lock.readLock();
	/**
	 * Field writeLock.
	 */
	final Lock writeLock = lock.writeLock();
	
	/**
	 * Constructor for IpBanManager.
	 */
	private IpBanManager()
	{
		ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable()
		{
			@Override
			public void run()
			{
				long currentMillis = System.currentTimeMillis();
				writeLock.lock();
				try
				{
					IpSession session;
					for (Iterator<IpSession> itr = ips.values().iterator(); itr.hasNext();)
					{
						session = itr.next();
						if ((session.banExpire < currentMillis) && (session.lastTry < (currentMillis - Config.LOGIN_TRY_TIMEOUT)))
						{
							itr.remove();
						}
					}
				}
				finally
				{
					writeLock.unlock();
				}
			}
		}, 1000L, 1000L);
	}
	
	/**
	 * Method isIpBanned.
	 * @param ip String
	 * @return boolean
	 */
	public boolean isIpBanned(String ip)
	{
		readLock.lock();
		try
		{
			IpSession ipsession;
			if ((ipsession = ips.get(ip)) == null)
			{
				return false;
			}
			return ipsession.banExpire > System.currentTimeMillis();
		}
		finally
		{
			readLock.unlock();
		}
	}
	
	/**
	 * Method tryLogin.
	 * @param ip String
	 * @param success boolean
	 * @return boolean
	 */
	public boolean tryLogin(String ip, boolean success)
	{
		writeLock.lock();
		try
		{
			IpSession ipsession;
			if ((ipsession = ips.get(ip)) == null)
			{
				ips.put(ip, ipsession = new IpSession());
			}
			long currentMillis = System.currentTimeMillis();
			if ((currentMillis - ipsession.lastTry) < Config.LOGIN_TRY_TIMEOUT)
			{
				success = false;
			}
			if (success)
			{
				if (ipsession.tryCount > 0)
				{
					ipsession.tryCount--;
				}
			}
			else
			{
				if (ipsession.tryCount < Config.LOGIN_TRY_BEFORE_BAN)
				{
					ipsession.tryCount++;
				}
			}
			ipsession.lastTry = currentMillis;
			if (ipsession.tryCount == Config.LOGIN_TRY_BEFORE_BAN)
			{
				_log.warn("IpBanManager: " + ip + " banned for " + (Config.IP_BAN_TIME / 1000L) + " seconds.");
				ipsession.banExpire = currentMillis + Config.IP_BAN_TIME;
				return false;
			}
			return true;
		}
		finally
		{
			writeLock.unlock();
		}
	}
}
