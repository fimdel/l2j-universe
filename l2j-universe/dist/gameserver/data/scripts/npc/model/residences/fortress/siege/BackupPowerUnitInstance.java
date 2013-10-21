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
package npc.model.residences.fortress.siege;

import java.util.StringTokenizer;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.base.ClassId;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.NpcHtmlMessage;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.templates.npc.NpcTemplate;

import org.apache.commons.lang3.ArrayUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class BackupPowerUnitInstance extends NpcInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field LIMIT. (value is 3)
	 */
	public static final int LIMIT = 3;
	/**
	 * Field COND_NO_ENTERED. (value is 0)
	 */
	public static final int COND_NO_ENTERED = 0;
	/**
	 * Field COND_ENTERED. (value is 1)
	 */
	public static final int COND_ENTERED = 1;
	/**
	 * Field COND_ALL_OK. (value is 2)
	 */
	public static final int COND_ALL_OK = 2;
	/**
	 * Field COND_FAIL. (value is 3)
	 */
	public static final int COND_FAIL = 3;
	/**
	 * Field COND_TIMEOUT. (value is 4)
	 */
	public static final int COND_TIMEOUT = 4;
	/**
	 * Field _generated.
	 */
	private final int[] _generated = new int[LIMIT];
	/**
	 * Field _index.
	 */
	private int _index;
	/**
	 * Field _tryCount.
	 */
	private int _tryCount;
	/**
	 * Field _invalidatePeriod.
	 */
	private long _invalidatePeriod;
	
	/**
	 * Constructor for BackupPowerUnitInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public BackupPowerUnitInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	/**
	 * Method onBypassFeedback.
	 * @param player Player
	 * @param command String
	 */
	@Override
	public void onBypassFeedback(Player player, String command)
	{
		if (!canBypassCheck(player, this))
		{
			return;
		}
		StringTokenizer token = new StringTokenizer(command);
		token.nextToken();
		int val = Integer.parseInt(token.nextToken());
		if ((player.getClassId() == ClassId.WARSMITH) || (player.getClassId() == ClassId.MAESTRO))
		{
			if (_tryCount == 0)
			{
				_tryCount++;
			}
			else
			{
				_index++;
			}
		}
		else
		{
			if (_generated[_index] == val)
			{
				_index++;
			}
			else
			{
				_tryCount++;
			}
		}
		showChatWindow(player, 0);
	}
	
	/**
	 * Method onSpawn.
	 */
	@Override
	public void onSpawn()
	{
		super.onSpawn();
		generate();
	}
	
	/**
	 * Method showChatWindow.
	 * @param player Player
	 * @param val int
	 * @param arg Object[]
	 */
	@Override
	public void showChatWindow(Player player, int val, Object... arg)
	{
		NpcHtmlMessage message = new NpcHtmlMessage(player, this);
		if ((_invalidatePeriod > 0) && (_invalidatePeriod < System.currentTimeMillis()))
		{
			generate();
		}
		int cond = getCond();
		switch (cond)
		{
			case COND_ALL_OK:
				message.setFile("residence2/fortress/fortress_subpower002.htm");
				onDecay();
				break;
			case COND_TIMEOUT:
				message.setFile("residence2/fortress/fortress_subpower003.htm");
				break;
			case COND_FAIL:
				message.setFile("residence2/fortress/fortress_subpower003.htm");
				_invalidatePeriod = System.currentTimeMillis() + 30000L;
				break;
			case COND_ENTERED:
				message.setFile("residence2/fortress/fortress_subpower004.htm");
				message.replaceNpcString("%password%", _index == 0 ? NpcString.THE_MARKS_HAVE_NOT_BEEN_ASSEMBLED : _index == 1 ? NpcString.THE_1ST_MARK_IS_CORRECT : NpcString.THE_2ND_MARK_IS_CORRECT);
				message.replaceNpcString("%try_count%", NpcString.ATTEMPT_S1__3_IS_IN_PROGRESS, _tryCount);
				break;
			case COND_NO_ENTERED:
				message.setFile("residence2/fortress/fortress_subpower001.htm");
				break;
		}
		player.sendPacket(message);
	}
	
	/**
	 * Method generate.
	 */
	private void generate()
	{
		_invalidatePeriod = 0;
		_tryCount = 0;
		_index = 0;
		for (int i = 0; i < _generated.length; i++)
		{
			_generated[i] = -1;
		}
		int j = 0;
		while (j != LIMIT)
		{
			int val = Rnd.get(0, LIMIT);
			if (ArrayUtils.contains(_generated, val))
			{
				continue;
			}
			_generated[j++] = val;
		}
	}
	
	/**
	 * Method getCond.
	 * @return int
	 */
	private int getCond()
	{
		if (_invalidatePeriod > System.currentTimeMillis())
		{
			return COND_TIMEOUT;
		}
		else if (_tryCount >= LIMIT)
		{
			return COND_FAIL;
		}
		else if ((_index == 0) && (_tryCount == 0))
		{
			return COND_NO_ENTERED;
		}
		else if (_index == LIMIT)
		{
			return COND_ALL_OK;
		}
		else
		{
			return COND_ENTERED;
		}
	}
	
	/**
	 * Method getGenerated.
	 * @return int[]
	 */
	public int[] getGenerated()
	{
		return _generated;
	}
}
