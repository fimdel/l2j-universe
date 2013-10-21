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
package lineage2.gameserver.network.loginservercon.lspackets;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import lineage2.commons.dbutils.DbUtils;
import lineage2.gameserver.Config;
import lineage2.gameserver.database.DatabaseFactory;
import lineage2.gameserver.network.loginservercon.LoginServerCommunication;
import lineage2.gameserver.network.loginservercon.ReceivablePacket;
import lineage2.gameserver.network.loginservercon.gspackets.SetAccountInfo;

import org.napile.primitive.Containers;
import org.napile.primitive.lists.IntList;
import org.napile.primitive.lists.impl.ArrayIntList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class GetAccountInfo extends ReceivablePacket
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(GetAccountInfo.class);
	/**
	 * Field _account.
	 */
	private String _account;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_account = readS();
	}
	
	/**
	 * Method runImpl.
	 */
	@Override
	protected void runImpl()
	{
		int playerSize = 0;
		IntList deleteChars = Containers.EMPTY_INT_LIST;
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("SELECT deletetime FROM characters WHERE account_name=?");
			statement.setString(1, _account);
			rset = statement.executeQuery();
			while (rset.next())
			{
				playerSize++;
				int d = rset.getInt("deletetime");
				if (d > 0)
				{
					if (deleteChars.isEmpty())
					{
						deleteChars = new ArrayIntList(3);
					}
					deleteChars.add(d + (Config.DELETE_DAYS * 24 * 60 * 60));
				}
			}
		}
		catch (Exception e)
		{
			_log.error("GetAccountInfo:runImpl():" + e, e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
		LoginServerCommunication.getInstance().sendPacket(new SetAccountInfo(_account, playerSize, deleteChars.toArray()));
	}
}
