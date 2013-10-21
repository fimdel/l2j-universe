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
package lineage2.gameserver.model.entity.events.objects;

import java.io.Serializable;
import java.util.Comparator;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.entity.events.impl.SiegeEvent;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.network.serverpackets.L2GameServerPacket;
import lineage2.gameserver.network.serverpackets.components.IStaticPacket;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class SiegeClanObject implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * @author Mobius
	 */
	public static class SiegeClanComparatorImpl implements Comparator<SiegeClanObject>
	{
		/**
		 * Field _instance.
		 */
		private static final SiegeClanComparatorImpl _instance = new SiegeClanComparatorImpl();
		
		/**
		 * Method getInstance.
		 * @return SiegeClanComparatorImpl
		 */
		public static SiegeClanComparatorImpl getInstance()
		{
			return _instance;
		}
		
		/**
		 * Method compare.
		 * @param o1 SiegeClanObject
		 * @param o2 SiegeClanObject
		 * @return int
		 */
		@Override
		public int compare(SiegeClanObject o1, SiegeClanObject o2)
		{
			return (o2.getParam() < o1.getParam()) ? -1 : ((o2.getParam() == o1.getParam()) ? 0 : 1);
		}
	}
	
	/**
	 * Field _type.
	 */
	private String _type;
	/**
	 * Field _clan.
	 */
	private final Clan _clan;
	/**
	 * Field _flag.
	 */
	private NpcInstance _flag;
	/**
	 * Field _date.
	 */
	private final long _date;
	
	/**
	 * Constructor for SiegeClanObject.
	 * @param type String
	 * @param clan Clan
	 * @param param long
	 */
	public SiegeClanObject(String type, Clan clan, long param)
	{
		this(type, clan, 0, System.currentTimeMillis());
	}
	
	/**
	 * Constructor for SiegeClanObject.
	 * @param type String
	 * @param clan Clan
	 * @param param long
	 * @param date long
	 */
	public SiegeClanObject(String type, Clan clan, long param, long date)
	{
		_type = type;
		_clan = clan;
		_date = date;
	}
	
	/**
	 * Method getObjectId.
	 * @return int
	 */
	public int getObjectId()
	{
		return _clan.getClanId();
	}
	
	/**
	 * Method getClan.
	 * @return Clan
	 */
	public Clan getClan()
	{
		return _clan;
	}
	
	/**
	 * Method getFlag.
	 * @return NpcInstance
	 */
	public NpcInstance getFlag()
	{
		return _flag;
	}
	
	/**
	 * Method deleteFlag.
	 */
	public void deleteFlag()
	{
		if (_flag != null)
		{
			_flag.deleteMe();
			_flag = null;
		}
	}
	
	/**
	 * Method setFlag.
	 * @param npc NpcInstance
	 */
	public void setFlag(NpcInstance npc)
	{
		_flag = npc;
	}
	
	/**
	 * Method setType.
	 * @param type String
	 */
	public void setType(String type)
	{
		_type = type;
	}
	
	/**
	 * Method getType.
	 * @return String
	 */
	public String getType()
	{
		return _type;
	}
	
	/**
	 * Method broadcast.
	 * @param packet IStaticPacket[]
	 */
	public void broadcast(IStaticPacket... packet)
	{
		getClan().broadcastToOnlineMembers(packet);
	}
	
	/**
	 * Method broadcast.
	 * @param packet L2GameServerPacket[]
	 */
	public void broadcast(L2GameServerPacket... packet)
	{
		getClan().broadcastToOnlineMembers(packet);
	}
	
	/**
	 * Method setEvent.
	 * @param start boolean
	 * @param event SiegeEvent<?,?>
	 */
	public void setEvent(boolean start, SiegeEvent<?, ?> event)
	{
		if (start)
		{
			for (Player player : _clan.getOnlineMembers(0))
			{
				player.addEvent(event);
				player.broadcastCharInfo();
			}
		}
		else
		{
			for (Player player : _clan.getOnlineMembers(0))
			{
				player.removeEvent(event);
				player.getEffectList().stopEffect(Skill.SKILL_BATTLEFIELD_DEATH_SYNDROME);
				player.broadcastCharInfo();
			}
		}
	}
	
	/**
	 * Method isParticle.
	 * @param player Player
	 * @return boolean
	 */
	public boolean isParticle(Player player)
	{
		return true;
	}
	
	/**
	 * Method getParam.
	 * @return long
	 */
	public long getParam()
	{
		return 0;
	}
	
	/**
	 * Method getDate.
	 * @return long
	 */
	public long getDate()
	{
		return _date;
	}
}
