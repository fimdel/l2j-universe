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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import lineage2.commons.collections.JoinedIterator;
import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcFriendInstance;
import lineage2.gameserver.model.matching.MatchingRoom;
import lineage2.gameserver.network.serverpackets.ExMPCCClose;
import lineage2.gameserver.network.serverpackets.ExMPCCOpen;
import lineage2.gameserver.network.serverpackets.ExMPCCPartyInfoUpdate;
import lineage2.gameserver.network.serverpackets.L2GameServerPacket;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.network.serverpackets.components.IStaticPacket;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class CommandChannel implements PlayerGroup
{
	/**
	 * Field STRATEGY_GUIDE_ID. (value is 8871)
	 */
	public static final int STRATEGY_GUIDE_ID = 8871;
	/**
	 * Field CLAN_IMPERIUM_ID. (value is 391)
	 */
	public static final int CLAN_IMPERIUM_ID = 391;
	
	/**
	 * Field _commandChannelParties.
	 */
	private final List<Party> _commandChannelParties = new CopyOnWriteArrayList<>();
	/**
	 * Field _commandChannelLeader.
	 */
	private Player _commandChannelLeader;
	/**
	 * Field _commandChannelLvl.
	 */
	private int _commandChannelLvl;
	/**
	 * Field _reflection.
	 */
	private Reflection _reflection;
	
	/**
	 * Field _matchingRoom.
	 */
	private MatchingRoom _matchingRoom;
	
	/**
	 * Constructor for CommandChannel.
	 * @param leader Player
	 */
	public CommandChannel(Player leader)
	{
		_commandChannelLeader = leader;
		_commandChannelParties.add(leader.getParty());
		_commandChannelLvl = leader.getParty().getLevel();
		leader.getParty().setCommandChannel(this);
		broadCast(ExMPCCOpen.STATIC);
	}
	
	/**
	 * Method addParty.
	 * @param party Party
	 */
	public void addParty(Party party)
	{
		broadCast(new ExMPCCPartyInfoUpdate(party, 1));
		_commandChannelParties.add(party);
		refreshLevel();
		party.setCommandChannel(this);
		
		for (Player $member : party)
		{
			$member.sendPacket(ExMPCCOpen.STATIC);
			if (_matchingRoom != null)
			{
				_matchingRoom.broadcastPlayerUpdate($member);
			}
		}
	}
	
	/**
	 * Method removeParty.
	 * @param party Party
	 */
	public void removeParty(Party party)
	{
		_commandChannelParties.remove(party);
		refreshLevel();
		party.setCommandChannel(null);
		party.broadCast(ExMPCCClose.STATIC);
		Reflection reflection = getReflection();
		if (reflection != null)
		{
			for (Player player : party.getPartyMembers())
			{
				player.teleToLocation(reflection.getReturnLoc(), 0);
			}
		}
		
		if (_commandChannelParties.size() < 2)
		{
			disbandChannel();
		}
		else
		{
			for (Player $member : party)
			{
				$member.sendPacket(new ExMPCCPartyInfoUpdate(party, 0));
				if (_matchingRoom != null)
				{
					_matchingRoom.broadcastPlayerUpdate($member);
				}
			}
		}
	}
	
	/**
	 * Method disbandChannel.
	 */
	public void disbandChannel()
	{
		broadCast(Msg.THE_COMMAND_CHANNEL_HAS_BEEN_DISBANDED);
		for (Party party : _commandChannelParties)
		{
			party.setCommandChannel(null);
			party.broadCast(ExMPCCClose.STATIC);
			if (isInReflection())
			{
				party.broadCast(new SystemMessage(SystemMessage.THIS_DUNGEON_WILL_EXPIRE_IN_S1_MINUTES).addNumber(1));
			}
		}
		Reflection reflection = getReflection();
		if (reflection != null)
		{
			reflection.startCollapseTimer(60000L);
			setReflection(null);
		}
		
		if (_matchingRoom != null)
		{
			_matchingRoom.disband();
		}
		_commandChannelParties.clear();
		_commandChannelLeader = null;
	}
	
	/**
	 * Method getMemberCount.
	 * @return int
	 */
	public int getMemberCount()
	{
		int count = 0;
		for (Party party : _commandChannelParties)
		{
			count += party.getMemberCount();
		}
		return count;
	}
	
	/**
	 * Method broadCast.
	 * @param gsp IStaticPacket[]
	 * @see lineage2.gameserver.model.PlayerGroup#broadCast(IStaticPacket[])
	 */
	@Override
	public void broadCast(IStaticPacket... gsp)
	{
		for (Party party : _commandChannelParties)
		{
			party.broadCast(gsp);
		}
	}
	
	/**
	 * Method broadcastToChannelPartyLeaders.
	 * @param gsp L2GameServerPacket
	 */
	public void broadcastToChannelPartyLeaders(L2GameServerPacket gsp)
	{
		for (Party party : _commandChannelParties)
		{
			Player leader = party.getPartyLeader();
			if (leader != null)
			{
				leader.sendPacket(gsp);
			}
		}
	}
	
	/**
	 * Method getParties.
	 * @return List<Party>
	 */
	public List<Party> getParties()
	{
		return _commandChannelParties;
	}
	
	/**
	 * Method getMembers.
	 * @return List<Player>
	 */
	@Deprecated
	public List<Player> getMembers()
	{
		List<Player> members = new ArrayList<>(_commandChannelParties.size());
		for (Party party : getParties())
		{
			members.addAll(party.getPartyMembers());
		}
		return members;
	}
	
	/**
	 * Method iterator.
	 * @return Iterator<Player> * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<Player> iterator()
	{
		List<Iterator<Player>> iterators = new ArrayList<>(_commandChannelParties.size());
		for (Party p : getParties())
		{
			iterators.add(p.getPartyMembers().iterator());
		}
		return new JoinedIterator<>(iterators);
	}
	
	/**
	 * Method getLevel.
	 * @return int
	 */
	public int getLevel()
	{
		return _commandChannelLvl;
	}
	
	/**
	 * Method setChannelLeader.
	 * @param newLeader Player
	 */
	public void setChannelLeader(Player newLeader)
	{
		_commandChannelLeader = newLeader;
		broadCast(new SystemMessage(SystemMessage.COMMAND_CHANNEL_AUTHORITY_HAS_BEEN_TRANSFERRED_TO_S1).addString(newLeader.getName()));
	}
	
	/**
	 * Method getChannelLeader.
	 * @return Player
	 */
	public Player getChannelLeader()
	{
		return _commandChannelLeader;
	}
	
	/**
	 * Method meetRaidWarCondition.
	 * @param npc NpcFriendInstance
	 * @return boolean
	 */
	public boolean meetRaidWarCondition(NpcFriendInstance npc)
	{
		if (!npc.isRaid())
		{
			return false;
		}
		int npcId = npc.getNpcId();
		switch (npcId)
		{
			case 29001: // Queen Ant
			case 29006: // Core
			case 29014: // Orfen
			case 29022: // Zaken
				return getMemberCount() > 36;
			case 29020: // Baium
				return getMemberCount() > 56;
			case 29019: // Antharas
				return getMemberCount() > 225;
			case 29028: // Valakas
				return getMemberCount() > 99;
			default: // normal Raidboss
				return getMemberCount() > 18;
		}
	}
	
	/**
	 * Method refreshLevel.
	 */
	private void refreshLevel()
	{
		_commandChannelLvl = 0;
		for (Party pty : _commandChannelParties)
		{
			if (pty.getLevel() > _commandChannelLvl)
			{
				_commandChannelLvl = pty.getLevel();
			}
		}
	}
	
	/**
	 * Method isInReflection.
	 * @return boolean
	 */
	public boolean isInReflection()
	{
		return _reflection != null;
	}
	
	/**
	 * Method setReflection.
	 * @param reflection Reflection
	 */
	public void setReflection(Reflection reflection)
	{
		_reflection = reflection;
	}
	
	/**
	 * Method getReflection.
	 * @return Reflection
	 */
	public Reflection getReflection()
	{
		return _reflection;
	}
	
	/**
	 * Method checkAuthority.
	 * @param creator Player
	 * @return boolean
	 */
	public static boolean checkAuthority(Player creator)
	{
		if ((creator.getClan() == null) || !creator.isInParty() || !creator.getParty().isLeader(creator) || (creator.getPledgeClass() < Player.RANK_BARON))
		{
			creator.sendPacket(Msg.YOU_DO_NOT_HAVE_AUTHORITY_TO_USE_THE_COMMAND_CHANNEL);
			return false;
		}
		
		boolean haveSkill = creator.getSkillLevel(CLAN_IMPERIUM_ID) > 0;
		
		boolean haveItem = creator.getInventory().getItemByItemId(STRATEGY_GUIDE_ID) != null;
		
		if (!haveSkill && !haveItem)
		{
			creator.sendPacket(Msg.YOU_DO_NOT_HAVE_AUTHORITY_TO_USE_THE_COMMAND_CHANNEL);
			return false;
		}
		
		return true;
	}
	
	/**
	 * Method getMatchingRoom.
	 * @return MatchingRoom
	 */
	public MatchingRoom getMatchingRoom()
	{
		return _matchingRoom;
	}
	
	/**
	 * Method setMatchingRoom.
	 * @param matchingRoom MatchingRoom
	 */
	public void setMatchingRoom(MatchingRoom matchingRoom)
	{
		_matchingRoom = matchingRoom;
	}

	public boolean isLeaderCommandChannel(Player player)
	{
		return getChannelLeader() == player;
	}
}