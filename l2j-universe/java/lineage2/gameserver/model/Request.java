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
package lineage2.gameserver.model;

import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import lineage2.commons.collections.MultiValueSet;
import lineage2.commons.lang.reference.HardReference;
import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.cache.Msg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Request extends MultiValueSet<String>
{
	/**
	 * Field serialVersionUID. (value is 1)
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field _log.
	 */
	@SuppressWarnings("unused")
	private static final Logger _log = LoggerFactory.getLogger(Request.class);
	
	/**
	 * @author Mobius
	 */
	public static enum L2RequestType
	{
		/**
		 * Field CUSTOM.
		 */
		CUSTOM,
		/**
		 * Field PARTY.
		 */
		PARTY,
		/**
		 * Field PARTY_ROOM.
		 */
		PARTY_ROOM,
		/**
		 * Field CLAN.
		 */
		CLAN,
		/**
		 * Field ALLY.
		 */
		ALLY,
		/**
		 * Field TRADE.
		 */
		TRADE,
		/**
		 * Field TRADE_REQUEST.
		 */
		TRADE_REQUEST,
		/**
		 * Field FRIEND.
		 */
		FRIEND,
		/**
		 * Field CHANNEL.
		 */
		CHANNEL,
		/**
		 * Field DUEL.
		 */
		DUEL,
		/**
		 * Field COUPLE_ACTION.
		 */
		COUPLE_ACTION,
		/**
		 * Field MENTEE.
		 */
		MENTEE,
		/**
		 * Field SUBSTITUTE.
		 */
		SUBSTITUTE
	}
	
	/**
	 * Field _nextId.
	 */
	private final static AtomicInteger _nextId = new AtomicInteger();
	/**
	 * Field _id.
	 */
	private final int _id;
	/**
	 * Field _type.
	 */
	private final L2RequestType _type;
	/**
	 * Field _requestor.
	 */
	private final HardReference<Player> _requestor;
	/**
	 * Field _reciever.
	 */
	private final HardReference<Player> _receiver;
	/**
	 * Field _isRequestorConfirmed.
	 */
	private boolean _isRequestorConfirmed;
	/**
	 * Field _isRecieverConfirmed.
	 */
	private boolean _isRecieverConfirmed;
	/**
	 * Field _isCancelled.
	 */
	private boolean _isCancelled;
	/**
	 * Field _isDone.
	 */
	private boolean _isDone;
	/**
	 * Field _timeout.
	 */
	private long _timeout;
	/**
	 * Field _timeoutTask.
	 */
	private Future<?> _timeoutTask;
	
	/**
	 * Constructor for Request.
	 * @param type L2RequestType
	 * @param requestor Player
	 * @param reciever Player
	 */
	public Request(L2RequestType type, Player requestor, Player receiver)
	{
		_id = _nextId.incrementAndGet();
		_requestor = requestor.getRef();
		_receiver = receiver.getRef();
		_type = type;
		requestor.setRequest(this);
		receiver.setRequest(this);
	}
	
	/**
	 * Method setTimeout.
	 * @param timeout long
	 * @return Request
	 */
	public Request setTimeout(long timeout)
	{
		_timeout = timeout > 0 ? System.currentTimeMillis() + timeout : 0;
		_timeoutTask = ThreadPoolManager.getInstance().schedule(new RunnableImpl()
		{
			@Override
			public void runImpl()
			{
				timeout();
			}
		}, timeout);
		return this;
	}
	
	/**
	 * Method getId.
	 * @return int
	 */
	public int getId()
	{
		return _id;
	}
	
	/**
	 * Method cancel.
	 */
	public void cancel()
	{
		_isCancelled = true;
		if (_timeoutTask != null)
		{
			_timeoutTask.cancel(false);
		}
		_timeoutTask = null;
		Player player = getRequestor();
		if ((player != null) && (player.getRequest() == this))
		{
			player.setRequest(null);
		}
		player = getReceiver();
		if ((player != null) && (player.getRequest() == this))
		{
			player.setRequest(null);
		}
	}
	
	/**
	 * Method done.
	 */
	public void done()
	{
		_isDone = true;
		if (_timeoutTask != null)
		{
			_timeoutTask.cancel(false);
		}
		_timeoutTask = null;
		Player player = getRequestor();
		if ((player != null) && (player.getRequest() == this))
		{
			player.setRequest(null);
		}
		player = getReceiver();
		if ((player != null) && (player.getRequest() == this))
		{
			player.setRequest(null);
		}
	}
	
	/**
	 * Method timeout.
	 */
	public void timeout()
	{
		Player player = getReceiver();
		if (player != null)
		{
			if (player.getRequest() == this)
			{
				player.sendPacket(Msg.TIME_EXPIRED);
			}
		}
		cancel();
	}
	
	/**
	 * Method getOtherPlayer.
	 * @param player Player
	 * @return Player
	 */
	public Player getOtherPlayer(Player player)
	{
		if (player == getRequestor())
		{
			return getReceiver();
		}
		if (player == getReceiver())
		{
			return getRequestor();
		}
		return null;
	}
	
	/**
	 * Method getRequestor.
	 * @return Player
	 */
	public Player getRequestor()
	{
		return _requestor.get();
	}
	
	/**
	 * Method getReciever.
	 * @return Player
	 */
	public Player getReceiver()
	{
		return _receiver.get();
	}
	
	/**
	 * Method isInProgress.
	 * @return boolean
	 */
	public boolean isInProgress()
	{
		if (_isCancelled)
		{
			return false;
		}
		if (_isDone)
		{
			return false;
		}
		if (_timeout == 0)
		{
			return true;
		}
		if (_timeout > System.currentTimeMillis())
		{
			return true;
		}
		return false;
	}
	
	/**
	 * Method isTypeOf.
	 * @param type L2RequestType
	 * @return boolean
	 */
	public boolean isTypeOf(L2RequestType type)
	{
		return _type == type;
	}
	
	/**
	 * Method confirm.
	 * @param player Player
	 */
	public void confirm(Player player)
	{
		if (player == getRequestor())
		{
			_isRequestorConfirmed = true;
		}
		else if (player == getReceiver())
		{
			_isRecieverConfirmed = true;
		}
	}
	
	/**
	 * Method isConfirmed.
	 * @param player Player
	 * @return boolean
	 */
	public boolean isConfirmed(Player player)
	{
		if (player == getRequestor())
		{
			return _isRequestorConfirmed;
		}
		else if (player == getReceiver())
		{
			return _isRecieverConfirmed;
		}
		return false;
	}
}
