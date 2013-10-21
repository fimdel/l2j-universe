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
package lineage2.gameserver.model.entity;

import java.sql.Connection;
import java.sql.PreparedStatement;

import lineage2.commons.dbutils.DbUtils;
import lineage2.gameserver.idfactory.IdFactory;
import lineage2.gameserver.instancemanager.CoupleManager;
import lineage2.gameserver.model.Player;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Couple
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(Couple.class);
	/**
	 * Field _id.
	 */
	private int _id = 0;
	/**
	 * Field _player1Id.
	 */
	private int _player1Id = 0;
	/**
	 * Field _player2Id.
	 */
	private int _player2Id = 0;
	/**
	 * Field _maried.
	 */
	private boolean _maried = false;
	/**
	 * Field _affiancedDate.
	 */
	private long _affiancedDate;
	/**
	 * Field _weddingDate.
	 */
	private long _weddingDate;
	/**
	 * Field isChanged.
	 */
	private boolean isChanged;
	
	/**
	 * Constructor for Couple.
	 * @param coupleId int
	 */
	public Couple(int coupleId)
	{
		_id = coupleId;
	}
	
	/**
	 * Constructor for Couple.
	 * @param player1 Player
	 * @param player2 Player
	 */
	public Couple(Player player1, Player player2)
	{
		_id = IdFactory.getInstance().getNextId();
		_player1Id = player1.getObjectId();
		_player2Id = player2.getObjectId();
		long time = System.currentTimeMillis();
		_affiancedDate = time;
		_weddingDate = time;
		player1.setCoupleId(_id);
		player1.setPartnerId(_player2Id);
		player2.setCoupleId(_id);
		player2.setPartnerId(_player1Id);
	}
	
	/**
	 * Method marry.
	 */
	public void marry()
	{
		_weddingDate = System.currentTimeMillis();
		_maried = true;
		setChanged(true);
	}
	
	/**
	 * Method divorce.
	 */
	public void divorce()
	{
		CoupleManager.getInstance().getCouples().remove(this);
		CoupleManager.getInstance().getDeletedCouples().add(this);
	}
	
	/**
	 * Method store.
	 * @param con Connection
	 */
	public void store(Connection con)
	{
		PreparedStatement statement = null;
		try
		{
			statement = con.prepareStatement("REPLACE INTO couples (id, player1Id, player2Id, maried, affiancedDate, weddingDate) VALUES (?, ?, ?, ?, ?, ?)");
			statement.setInt(1, _id);
			statement.setInt(2, _player1Id);
			statement.setInt(3, _player2Id);
			statement.setBoolean(4, _maried);
			statement.setLong(5, _affiancedDate);
			statement.setLong(6, _weddingDate);
			statement.execute();
		}
		catch (Exception e)
		{
			_log.error("", e);
		}
		finally
		{
			DbUtils.closeQuietly(statement);
		}
	}
	
	/**
	 * Method getId.
	 * @return int
	 */
	public final int getId()
	{
		return _id;
	}
	
	/**
	 * Method getPlayer1Id.
	 * @return int
	 */
	public final int getPlayer1Id()
	{
		return _player1Id;
	}
	
	/**
	 * Method getPlayer2Id.
	 * @return int
	 */
	public final int getPlayer2Id()
	{
		return _player2Id;
	}
	
	/**
	 * Method getMaried.
	 * @return boolean
	 */
	public final boolean getMaried()
	{
		return _maried;
	}
	
	/**
	 * Method getAffiancedDate.
	 * @return long
	 */
	public final long getAffiancedDate()
	{
		return _affiancedDate;
	}
	
	/**
	 * Method getWeddingDate.
	 * @return long
	 */
	public final long getWeddingDate()
	{
		return _weddingDate;
	}
	
	/**
	 * Method setPlayer1Id.
	 * @param _player1Id int
	 */
	public void setPlayer1Id(int _player1Id)
	{
		this._player1Id = _player1Id;
	}
	
	/**
	 * Method setPlayer2Id.
	 * @param _player2Id int
	 */
	public void setPlayer2Id(int _player2Id)
	{
		this._player2Id = _player2Id;
	}
	
	/**
	 * Method setMaried.
	 * @param _maried boolean
	 */
	public void setMaried(boolean _maried)
	{
		this._maried = _maried;
	}
	
	/**
	 * Method setAffiancedDate.
	 * @param _affiancedDate long
	 */
	public void setAffiancedDate(long _affiancedDate)
	{
		this._affiancedDate = _affiancedDate;
	}
	
	/**
	 * Method setWeddingDate.
	 * @param _weddingDate long
	 */
	public void setWeddingDate(long _weddingDate)
	{
		this._weddingDate = _weddingDate;
	}
	
	/**
	 * Method isChanged.
	 * @return boolean
	 */
	public boolean isChanged()
	{
		return isChanged;
	}
	
	/**
	 * Method setChanged.
	 * @param val boolean
	 */
	public void setChanged(boolean val)
	{
		isChanged = val;
	}
}
