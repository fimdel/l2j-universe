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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import lineage2.commons.dbutils.DbUtils;
import lineage2.commons.net.utils.Net;
import lineage2.commons.net.utils.NetList;
import lineage2.loginserver.database.L2DatabaseFactory;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.napile.primitive.maps.IntObjectMap;
import org.napile.primitive.maps.impl.HashIntObjectMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Account
{
	/**
	 * Field _log.
	 */
	private final static Logger _log = LoggerFactory.getLogger(Account.class);
	/**
	 * Field login.
	 */
	private final String login;
	/**
	 * Field passwordHash.
	 */
	private String passwordHash;
	/**
	 * Field allowedIP.
	 */
	private String allowedIP;
	/**
	 * Field allowedIpList.
	 */
	private final NetList allowedIpList = new NetList();
	/**
	 * Field accessLevel.
	 */
	private int accessLevel;
	/**
	 * Field banExpire.
	 */
	private int banExpire;
	/**
	 * Field bonus.
	 */
	private double bonus;
	/**
	 * Field bonusExpire.
	 */
	private int bonusExpire;
	/**
	 * Field lastIP.
	 */
	private String lastIP;
	/**
	 * Field lastAccess.
	 */
	private int lastAccess;
	/**
	 * Field lastServer.
	 */
	private int lastServer;
	/**
	 * Field _serversInfo.
	 */
	private final IntObjectMap<Pair<Integer, int[]>> _serversInfo = new HashIntObjectMap<>(2);
	
	/**
	 * Constructor for Account.
	 * @param login String
	 */
	public Account(String login)
	{
		this.login = login;
	}
	
	/**
	 * Method getLogin.
	 * @return String
	 */
	public String getLogin()
	{
		return login;
	}
	
	/**
	 * Method getPasswordHash.
	 * @return String
	 */
	public String getPasswordHash()
	{
		return passwordHash;
	}
	
	/**
	 * Method setPasswordHash.
	 * @param passwordHash String
	 */
	public void setPasswordHash(String passwordHash)
	{
		this.passwordHash = passwordHash;
	}
	
	/**
	 * Method getAllowedIP.
	 * @return String
	 */
	public String getAllowedIP()
	{
		return allowedIP;
	}
	
	/**
	 * Method isAllowedIP.
	 * @param ip String
	 * @return boolean
	 */
	public boolean isAllowedIP(String ip)
	{
		return allowedIpList.isEmpty() || allowedIpList.isInRange(ip);
	}
	
	/**
	 * Method setAllowedIP.
	 * @param allowedIP String
	 */
	public void setAllowedIP(String allowedIP)
	{
		allowedIpList.clear();
		this.allowedIP = allowedIP;
		if (allowedIP.isEmpty())
		{
			return;
		}
		String[] masks = allowedIP.split("[\\s,;]+");
		for (String mask : masks)
		{
			allowedIpList.add(Net.valueOf(mask));
		}
	}
	
	/**
	 * Method getAccessLevel.
	 * @return int
	 */
	public int getAccessLevel()
	{
		return accessLevel;
	}
	
	/**
	 * Method setAccessLevel.
	 * @param accessLevel int
	 */
	public void setAccessLevel(int accessLevel)
	{
		this.accessLevel = accessLevel;
	}
	
	/**
	 * Method getBonus.
	 * @return double
	 */
	public double getBonus()
	{
		return bonus;
	}
	
	/**
	 * Method setBonus.
	 * @param bonus double
	 */
	public void setBonus(double bonus)
	{
		this.bonus = bonus;
	}
	
	/**
	 * Method getBonusExpire.
	 * @return int
	 */
	public int getBonusExpire()
	{
		return bonusExpire;
	}
	
	/**
	 * Method setBonusExpire.
	 * @param bonusExpire int
	 */
	public void setBonusExpire(int bonusExpire)
	{
		this.bonusExpire = bonusExpire;
	}
	
	/**
	 * Method getBanExpire.
	 * @return int
	 */
	public int getBanExpire()
	{
		return banExpire;
	}
	
	/**
	 * Method setBanExpire.
	 * @param banExpire int
	 */
	public void setBanExpire(int banExpire)
	{
		this.banExpire = banExpire;
	}
	
	/**
	 * Method setLastIP.
	 * @param lastIP String
	 */
	public void setLastIP(String lastIP)
	{
		this.lastIP = lastIP;
	}
	
	/**
	 * Method getLastIP.
	 * @return String
	 */
	public String getLastIP()
	{
		return lastIP;
	}
	
	/**
	 * Method getLastAccess.
	 * @return int
	 */
	public int getLastAccess()
	{
		return lastAccess;
	}
	
	/**
	 * Method setLastAccess.
	 * @param lastAccess int
	 */
	public void setLastAccess(int lastAccess)
	{
		this.lastAccess = lastAccess;
	}
	
	/**
	 * Method getLastServer.
	 * @return int
	 */
	public int getLastServer()
	{
		return lastServer;
	}
	
	/**
	 * Method setLastServer.
	 * @param lastServer int
	 */
	public void setLastServer(int lastServer)
	{
		this.lastServer = lastServer;
	}
	
	/**
	 * Method addAccountInfo.
	 * @param serverId int
	 * @param size int
	 * @param deleteChars int[]
	 */
	public void addAccountInfo(int serverId, int size, int[] deleteChars)
	{
		_serversInfo.put(serverId, new ImmutablePair<>(size, deleteChars));
	}
	
	/**
	 * Method getAccountInfo.
	 * @param serverId int
	 * @return Pair<Integer,int[]>
	 */
	public Pair<Integer, int[]> getAccountInfo(int serverId)
	{
		return _serversInfo.get(serverId);
	}
	
	/**
	 * Method toString.
	 * @return String
	 */
	@Override
	public String toString()
	{
		return login;
	}
	
	/**
	 * Method restore.
	 */
	public void restore()
	{
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = L2DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("SELECT password, access_level, ban_expire, allow_ip, bonus, bonus_expire, last_server, last_ip, last_access FROM accounts WHERE login = ?");
			statement.setString(1, login);
			rset = statement.executeQuery();
			if (rset.next())
			{
				setPasswordHash(rset.getString("password"));
				setAccessLevel(rset.getInt("access_level"));
				setBanExpire(rset.getInt("ban_expire"));
				setAllowedIP(rset.getString("allow_ip"));
				setBonus(rset.getDouble("bonus"));
				setBonusExpire(rset.getInt("bonus_expire"));
				setLastServer(rset.getInt("last_server"));
				setLastIP(rset.getString("last_ip"));
				setLastAccess(rset.getInt("last_access"));
			}
		}
		catch (Exception e)
		{
			_log.error("", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
	}
	
	/**
	 * Method save.
	 */
	public void save()
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = L2DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("INSERT INTO accounts (login, password) VALUES(?,?)");
			statement.setString(1, getLogin());
			statement.setString(2, getPasswordHash());
			statement.execute();
		}
		catch (Exception e)
		{
			_log.error("", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
	
	/**
	 * Method update.
	 */
	public void update()
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = L2DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("UPDATE accounts SET password = ?, access_level = ?, ban_expire = ?, allow_ip = ?, bonus = ?, bonus_expire = ?, last_server = ?, last_ip = ?, last_access = ? WHERE login = ?");
			statement.setString(1, getPasswordHash());
			statement.setInt(2, getAccessLevel());
			statement.setInt(3, getBanExpire());
			statement.setString(4, getAllowedIP());
			statement.setDouble(5, getBonus());
			statement.setInt(6, getBonusExpire());
			statement.setInt(7, getLastServer());
			statement.setString(8, getLastIP());
			statement.setInt(9, getLastAccess());
			statement.setString(10, getLogin());
			statement.execute();
		}
		catch (Exception e)
		{
			_log.error("", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
}
