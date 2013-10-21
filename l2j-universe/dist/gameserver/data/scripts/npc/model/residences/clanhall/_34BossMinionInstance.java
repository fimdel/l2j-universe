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
package npc.model.residences.clanhall;

import lineage2.gameserver.model.Creature;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.templates.npc.NpcTemplate;
import npc.model.residences.SiegeGuardInstance;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public abstract class _34BossMinionInstance extends SiegeGuardInstance implements _34SiegeGuard
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor for _34BossMinionInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public _34BossMinionInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	/**
	 * Method onDeath.
	 * @param killer Creature
	 */
	@Override
	public void onDeath(Creature killer)
	{
		setCurrentHp(1, true);
	}
	
	/**
	 * Method onSpawn.
	 */
	@Override
	public void onSpawn()
	{
		super.onSpawn();
		Functions.npcShout(this, spawnChatSay());
	}
	
	/**
	 * Method spawnChatSay.
	 * @return NpcString
	 */
	public abstract NpcString spawnChatSay();
	
	/**
	 * Method teleChatSay.
	 * @return NpcString * @see npc.model.residences.clanhall._34SiegeGuard#teleChatSay()
	 */
	@Override
	public abstract NpcString teleChatSay();
}
