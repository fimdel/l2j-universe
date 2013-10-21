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
package lineage2.gameserver.model.actor.recorder;

import lineage2.commons.collections.CollectionUtils;
import lineage2.gameserver.Config;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Summon;
import lineage2.gameserver.model.base.Element;
import lineage2.gameserver.model.matching.MatchingRoom;
import lineage2.gameserver.network.serverpackets.ExStorageMaxCount;
import lineage2.gameserver.network.serverpackets.StatusUpdate.StatusUpdateField;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class PlayerStatsChangeRecorder extends CharStatsChangeRecorder<Player>
{
	/**
	 * Field BROADCAST_KARMA.
	 */
	public static final int BROADCAST_KARMA = 1 << 3;
	/**
	 * Field SEND_STORAGE_INFO.
	 */
	public static final int SEND_STORAGE_INFO = 1 << 4;
	/**
	 * Field SEND_MAX_LOAD.
	 */
	public static final int SEND_MAX_LOAD = 1 << 5;
	/**
	 * Field SEND_CUR_LOAD.
	 */
	public static final int SEND_CUR_LOAD = 1 << 6;
	/**
	 * Field BROADCAST_CHAR_INFO2.
	 */
	public static final int BROADCAST_CHAR_INFO2 = 1 << 7;
	/**
	 * Field _maxCp.
	 */
	private int _maxCp;
	/**
	 * Field _maxLoad.
	 */
	private int _maxLoad;
	/**
	 * Field _curLoad.
	 */
	private int _curLoad;
	/**
	 * Field _attackElement.
	 */
	private final int[] _attackElement = new int[6];
	/**
	 * Field _defenceElement.
	 */
	private final int[] _defenceElement = new int[6];
	/**
	 * Field _exp.
	 */
	private long _exp;
	/**
	 * Field _sp.
	 */
	private int _sp;
	/**
	 * Field _karma.
	 */
	private int _karma;
	/**
	 * Field _pk.
	 */
	private int _pk;
	/**
	 * Field _pvp.
	 */
	private int _pvp;
	/**
	 * Field _fame.
	 */
	private int _fame;
	/**
	 * Field _inventory.
	 */
	private int _inventory;
	/**
	 * Field _warehouse.
	 */
	private int _warehouse;
	/**
	 * Field _clan.
	 */
	private int _clan;
	/**
	 * Field _trade.
	 */
	private int _trade;
	/**
	 * Field _recipeDwarven.
	 */
	private int _recipeDwarven;
	/**
	 * Field _recipeCommon.
	 */
	private int _recipeCommon;
	/**
	 * Field _partyRoom.
	 */
	private int _partyRoom;
	/**
	 * Field _title.
	 */
	private String _title = StringUtils.EMPTY;
	/**
	 * Field _cubicsHash.
	 */
	private int _cubicsHash;
	
	/**
	 * Constructor for PlayerStatsChangeRecorder.
	 * @param activeChar Player
	 */
	public PlayerStatsChangeRecorder(Player activeChar)
	{
		super(activeChar);
	}
	
	/**
	 * Method refreshStats.
	 */
	@Override
	protected void refreshStats()
	{
		_maxCp = set(SEND_STATUS_INFO, _maxCp, _activeChar.getMaxCp());
		super.refreshStats();
		_maxLoad = set(SEND_CHAR_INFO | SEND_MAX_LOAD, _maxLoad, _activeChar.getMaxLoad());
		_curLoad = set(SEND_CUR_LOAD, _curLoad, _activeChar.getCurrentLoad());
		for (Element e : Element.VALUES)
		{
			_attackElement[e.getId()] = set(SEND_CHAR_INFO, _attackElement[e.getId()], _activeChar.getAttack(e));
			_defenceElement[e.getId()] = set(SEND_CHAR_INFO, _defenceElement[e.getId()], _activeChar.getDefence(e));
		}
		_exp = set(SEND_CHAR_INFO, _exp, _activeChar.getExp());
		_sp = set(SEND_CHAR_INFO, _sp, _activeChar.getIntSp());
		_pk = set(SEND_CHAR_INFO, _pk, _activeChar.getPkKills());
		_pvp = set(SEND_CHAR_INFO, _pvp, _activeChar.getPvpKills());
		_fame = set(SEND_CHAR_INFO, _fame, _activeChar.getFame());
		_karma = set(BROADCAST_KARMA, _karma, _activeChar.getKarma());
		_inventory = set(SEND_STORAGE_INFO, _inventory, _activeChar.getInventoryLimit());
		_warehouse = set(SEND_STORAGE_INFO, _warehouse, _activeChar.getWarehouseLimit());
		_clan = set(SEND_STORAGE_INFO, _clan, Config.WAREHOUSE_SLOTS_CLAN);
		_trade = set(SEND_STORAGE_INFO, _trade, _activeChar.getTradeLimit());
		_recipeDwarven = set(SEND_STORAGE_INFO, _recipeDwarven, _activeChar.getDwarvenRecipeLimit());
		_recipeCommon = set(SEND_STORAGE_INFO, _recipeCommon, _activeChar.getCommonRecipeLimit());
		_cubicsHash = set(BROADCAST_CHAR_INFO, _cubicsHash, CollectionUtils.hashCode(_activeChar.getCubics()));
		_partyRoom = set(BROADCAST_CHAR_INFO, _partyRoom, (_activeChar.getMatchingRoom() != null) && (_activeChar.getMatchingRoom().getType() == MatchingRoom.PARTY_MATCHING) && (_activeChar.getMatchingRoom().getLeader() == _activeChar) ? _activeChar.getMatchingRoom().getId() : 0);
		_team = set(BROADCAST_CHAR_INFO2, _team, _activeChar.getTeam());
		_title = set(BROADCAST_CHAR_INFO, _title, _activeChar.getTitle());
	}
	
	/**
	 * Method onSendChanges.
	 */
	@Override
	protected void onSendChanges()
	{
		super.onSendChanges();
		if ((_changes & BROADCAST_CHAR_INFO2) == BROADCAST_CHAR_INFO2)
		{
			_activeChar.broadcastCharInfo();
			for (Summon summon : _activeChar.getSummonList())
			{
				summon.broadcastCharInfo();
			}
		}
		if ((_changes & BROADCAST_CHAR_INFO) == BROADCAST_CHAR_INFO)
		{
			_activeChar.broadcastCharInfo();
		}
		else if ((_changes & SEND_CHAR_INFO) == SEND_CHAR_INFO)
		{
			_activeChar.sendUserInfo();
		}
		if ((_changes & SEND_CUR_LOAD) == SEND_CUR_LOAD)
		{
			_activeChar.sendStatusUpdate(false, false, StatusUpdateField.CUR_LOAD);
		}
		if ((_changes & SEND_MAX_LOAD) == SEND_MAX_LOAD)
		{
			_activeChar.sendStatusUpdate(false, false, StatusUpdateField.MAX_LOAD);
		}
		if ((_changes & BROADCAST_KARMA) == BROADCAST_KARMA)
		{
			_activeChar.sendStatusUpdate(true, false, StatusUpdateField.KARMA);
		}
		if ((_changes & SEND_STORAGE_INFO) == SEND_STORAGE_INFO)
		{
			_activeChar.sendPacket(new ExStorageMaxCount(_activeChar));
		}
	}
}
