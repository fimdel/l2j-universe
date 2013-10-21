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
package npc.model;

import java.util.StringTokenizer;

import lineage2.gameserver.instancemanager.HellboundManager;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.skills.AbnormalEffect;
import lineage2.gameserver.templates.npc.NpcTemplate;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class NativePrisonerInstance extends NpcInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor for NativePrisonerInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public NativePrisonerInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	/**
	 * Method onSpawn.
	 */
	@Override
	protected void onSpawn()
	{
		startAbnormalEffect(AbnormalEffect.HOLD_2);
		super.onSpawn();
	}
	
	/**
	 * Method onBypassFeedback.
	 * @param player Player
	 * @param command String
	 */
	@Override
	public void onBypassFeedback(Player player, String command)
	{
		if (!canBypassCheck(player, this) || isBusy())
		{
			return;
		}
		StringTokenizer st = new StringTokenizer(command);
		if (st.nextToken().equals("rescue"))
		{
			stopAbnormalEffect(AbnormalEffect.HOLD_2);
			Functions.npcSay(this, "Thank you for saving me! Guards are coming, run!");
			HellboundManager.addConfidence(15);
			deleteMe();
		}
		else
		{
			super.onBypassFeedback(player, command);
		}
	}
}
