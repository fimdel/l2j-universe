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
package lineage2.loginserver.gameservercon.gspackets;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Logger;

import lineage2.commons.dbutils.DbUtils;
import lineage2.loginserver.Config;
import lineage2.loginserver.database.L2DatabaseFactory;
import lineage2.loginserver.gameservercon.ReceivablePacket;
import lineage2.loginserver.gameservercon.lspackets.ChangePasswordResponse;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ChangePassword extends ReceivablePacket
{
	/**
	 * Field log.
	 */
	private static final Logger log = Logger.getLogger(ChangePassword.class.getName());
	/**
	 * Field accname.
	 */
	private String accname;
	/**
	 * Field oldPass.
	 */
	String oldPass;
	/**
	 * Field newPass.
	 */
	String newPass;
	/**
	 * Field hwid.
	 */
	String hwid;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		accname = readS();
		oldPass = readS();
		newPass = readS();
		hwid = readS();
	}
	
	/**
	 * Method runImpl.
	 */
	@Override
	protected void runImpl()
	{
		String dbPassword = null;
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		try
		{
			con = L2DatabaseFactory.getInstance().getConnection();
			try
			{
				statement = con.prepareStatement("SELECT * FROM accounts WHERE login = ?");
				statement.setString(1, accname);
				rs = statement.executeQuery();
				if (rs.next())
				{
					dbPassword = rs.getString("password");
				}
			}
			catch (Exception e)
			{
				log.warning("Can't recive old password for account " + accname + ", exciption :" + e);
			}
			finally
			{
				DbUtils.closeQuietly(statement, rs);
			}
			try
			{
				if (!Config.DEFAULT_CRYPT.compare(oldPass, dbPassword))
				{
					ChangePasswordResponse cp1;
					cp1 = new ChangePasswordResponse(accname, false);
					sendPacket(cp1);
				}
				else
				{
					statement = con.prepareStatement("UPDATE accounts SET password = ? WHERE login = ?");
					statement.setString(1, Config.DEFAULT_CRYPT.encrypt(newPass));
					statement.setString(2, accname);
					int result = statement.executeUpdate();
					ChangePasswordResponse cp1;
					cp1 = new ChangePasswordResponse(accname, result != 0);
					sendPacket(cp1);
				}
			}
			catch (Exception e1)
			{
				e1.printStackTrace();
			}
			finally
			{
				DbUtils.closeQuietly(statement);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			DbUtils.closeQuietly(con);
		}
	}
}
