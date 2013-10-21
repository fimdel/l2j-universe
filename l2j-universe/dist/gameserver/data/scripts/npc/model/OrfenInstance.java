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

import lineage2.commons.util.Rnd;
import lineage2.gameserver.ai.CtrlIntention;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.instances.BossInstance;
import lineage2.gameserver.network.serverpackets.PlaySound;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class OrfenInstance extends BossInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field nest.
	 */
	public static final Location nest = new Location(43728, 17220, -4342);
	/**
	 * Field locs.
	 */
	public static final Location[] locs = new Location[]
	{
		new Location(55024, 17368, -5412),
		new Location(53504, 21248, -5496),
		new Location(53248, 24576, -5272)
	};
	
	/**
	 * Constructor for OrfenInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public OrfenInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	/**
	 * Method setTeleported.
	 * @param flag boolean
	 */
	@Override
	public void setTeleported(boolean flag)
	{
		super.setTeleported(flag);
		Location loc = flag ? nest : locs[Rnd.get(locs.length)];
		setSpawnedLoc(loc);
		getAggroList().clear(true);
		getAI().setIntention(CtrlIntention.AI_INTENTION_ACTIVE, null, null);
		teleToLocation(loc);
	}
	
	/**
	 * Method onSpawn.
	 */
	@Override
	protected void onSpawn()
	{
		super.onSpawn();
		setTeleported(false);
		broadcastPacketToOthers(new PlaySound(PlaySound.Type.MUSIC, "BS01_A", 1, 0, getLoc()));
	}
	
	/**
	 * Method onDeath.
	 * @param killer Creature
	 */
	@Override
	protected void onDeath(Creature killer)
	{
		broadcastPacketToOthers(new PlaySound(PlaySound.Type.MUSIC, "BS02_D", 1, 0, getLoc()));
		super.onDeath(killer);
	}
	
	/**
	 * Method reduceCurrentHp.
	 * @param damage double
	 * @param reflectableDamage double
	 * @param attacker Creature
	 * @param skill Skill
	 * @param awake boolean
	 * @param standUp boolean
	 * @param directHp boolean
	 * @param canReflect boolean
	 * @param transferDamage boolean
	 * @param isDot boolean
	 * @param sendMessage boolean
	 */
	@Override
	public void reduceCurrentHp(double damage, double reflectableDamage, Creature attacker, Skill skill, boolean awake, boolean standUp, boolean directHp, boolean canReflect, boolean transferDamage, boolean isDot, boolean sendMessage)
	{
		super.reduceCurrentHp(damage, reflectableDamage, attacker, skill, awake, standUp, directHp, canReflect, transferDamage, isDot, sendMessage);
		if (!isTeleported() && (getCurrentHpPercents() <= 50))
		{
			setTeleported(true);
		}
	}
}
