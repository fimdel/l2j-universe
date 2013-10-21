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
package lineage2.gameserver.model.instances;

import java.util.Collections;
import java.util.List;

import lineage2.commons.lang.reference.HardReference;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.GameObject;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.World;
import lineage2.gameserver.model.reference.L2Reference;
import lineage2.gameserver.network.serverpackets.L2GameServerPacket;
import lineage2.gameserver.network.serverpackets.NpcHtmlMessage;
import lineage2.gameserver.network.serverpackets.ShowTownMap;
import lineage2.gameserver.network.serverpackets.StaticObject;
import lineage2.gameserver.templates.StaticObjectTemplate;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class StaticObjectInstance extends GameObject
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field reference.
	 */
	private final HardReference<StaticObjectInstance> reference;
	/**
	 * Field _template.
	 */
	private final StaticObjectTemplate _template;
	/**
	 * Field _meshIndex.
	 */
	private int _meshIndex;
	
	/**
	 * Constructor for StaticObjectInstance.
	 * @param objectId int
	 * @param template StaticObjectTemplate
	 */
	public StaticObjectInstance(int objectId, StaticObjectTemplate template)
	{
		super(objectId);
		_template = template;
		reference = new L2Reference<>(this);
	}
	
	/**
	 * Method getRef.
	 * @return HardReference<StaticObjectInstance>
	 */
	@Override
	public HardReference<StaticObjectInstance> getRef()
	{
		return reference;
	}
	
	/**
	 * Method getUId.
	 * @return int
	 */
	public int getUId()
	{
		return _template.getUId();
	}
	
	/**
	 * Method getType.
	 * @return int
	 */
	public int getType()
	{
		return _template.getType();
	}
	
	@Override
	public void onInteract(final Player player)
	{
		switch(_template.getType())
		{
			case 0:
				player.sendPacket(new NpcHtmlMessage(player, getUId(), "newspaper/arena.htm", 0));
				break;
			case 2:
				player.sendPacket(new ShowTownMap(_template.getFilePath(), _template.getMapX(), _template.getMapY()));
				break;
		}
	}
	
	/**
	 * Method addPacketList.
	 * @param forPlayer Player
	 * @param dropper Creature
	 * @return List<L2GameServerPacket>
	 */
	@Override
	public List<L2GameServerPacket> addPacketList(Player forPlayer, Creature dropper)
	{
		return Collections.<L2GameServerPacket> singletonList(new StaticObject(this));
	}
	
	/**
	 * Method isAttackable.
	 * @param attacker Creature
	 * @return boolean
	 */
	@Override
	public boolean isAttackable(Creature attacker)
	{
		return false;
	}
	
	/**
	 * Method broadcastInfo.
	 * @param force boolean
	 */
	public void broadcastInfo(boolean force)
	{
		StaticObject p = new StaticObject(this);
		for (Player player : World.getAroundPlayers(this))
		{
			player.sendPacket(p);
		}
	}
	
	/**
	 * Method getGeoZ.
	 * @param loc Location
	 * @return int
	 */
	@Override
	public int getGeoZ(Location loc)
	{
		return loc.z;
	}
	
	/**
	 * Method getMeshIndex.
	 * @return int
	 */
	public int getMeshIndex()
	{
		return _meshIndex;
	}
	
	/**
	 * Method setMeshIndex.
	 * @param meshIndex int
	 */
	public void setMeshIndex(int meshIndex)
	{
		_meshIndex = meshIndex;
	}
}
