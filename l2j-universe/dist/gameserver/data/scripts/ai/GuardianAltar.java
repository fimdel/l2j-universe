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
package ai;

import java.util.List;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.ai.CtrlEvent;
import lineage2.gameserver.ai.DefaultAI;
import lineage2.gameserver.data.xml.holder.NpcHolder;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.SimpleSpawner;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class GuardianAltar extends DefaultAI
{
	/**
	 * Field DarkShamanVarangka. (value is 18808)
	 */
	private static final int DarkShamanVarangka = 18808;
	
	/**
	 * Constructor for GuardianAltar.
	 * @param actor NpcInstance
	 */
	public GuardianAltar(NpcInstance actor)
	{
		super(actor);
		actor.setIsInvul(true);
	}
	
	/**
	 * Method onEvtAttacked.
	 * @param attacker Creature
	 * @param damage int
	 */
	@Override
	protected void onEvtAttacked(Creature attacker, int damage)
	{
		final NpcInstance actor = getActor();
		if (attacker == null)
		{
			return;
		}
		final Player player = attacker.getPlayer();
		if (Rnd.chance(40) && player.getInventory().destroyItemByItemId(14848, 1L))
		{
			final List<NpcInstance> around = actor.getAroundNpc(1500, 300);
			if ((around != null) && !around.isEmpty())
			{
				for (NpcInstance npc : around)
				{
					if (npc.getNpcId() == 18808)
					{
						Functions.npcSay(actor, "I can sense the presence of Dark Shaman already!");
						return;
					}
				}
			}
			try
			{
				final SimpleSpawner sp = new SimpleSpawner(NpcHolder.getInstance().getTemplate(DarkShamanVarangka));
				sp.setLoc(Location.findPointToStay(actor, 400, 420));
				final NpcInstance npc = sp.doSpawn(true);
				if (attacker.isPet() || attacker.isServitor())
				{
					npc.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, attacker, Rnd.get(2, 100));
				}
				npc.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, attacker.getPlayer(), Rnd.get(1, 100));
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		else if (Rnd.chance(5))
		{
			final List<NpcInstance> around = actor.getAroundNpc(1000, 300);
			if ((around != null) && !around.isEmpty())
			{
				for (NpcInstance npc : around)
				{
					if (npc.getNpcId() == 22702)
					{
						return;
					}
				}
			}
			for (int i = 0; i < 2; i++)
			{
				try
				{
					SimpleSpawner sp = new SimpleSpawner(NpcHolder.getInstance().getTemplate(22702));
					sp.setLoc(Location.findPointToStay(actor, 150, 160));
					NpcInstance npc = sp.doSpawn(true);
					if (attacker.isPet() || attacker.isServitor())
					{
						npc.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, attacker, Rnd.get(2, 100));
					}
					npc.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, attacker.getPlayer(), Rnd.get(1, 100));
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
		return;
	}
	
	/**
	 * Method randomWalk.
	 * @return boolean
	 */
	@Override
	protected boolean randomWalk()
	{
		return false;
	}
}
