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

import lineage2.gameserver.data.xml.holder.NpcHolder;
import lineage2.gameserver.idfactory.IdFactory;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.ReflectionBossInstance;
import lineage2.gameserver.templates.InstantZone;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class LostCaptainInstance extends ReflectionBossInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field TELE_DEVICE_ID. (value is 4314)
	 */
	private static final int TELE_DEVICE_ID = 4314;
	
	/**
	 * Constructor for LostCaptainInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public LostCaptainInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	/**
	 * Method onDeath.
	 * @param killer Creature
	 */
	@Override
	protected void onDeath(Creature killer)
	{
		Reflection r = getReflection();
		r.setReenterTime(System.currentTimeMillis());
		super.onDeath(killer);
		InstantZone iz = r.getInstancedZone();
		if (iz != null)
		{
			String tele_device_loc = iz.getAddParams().getString("tele_device_loc", null);
			if (tele_device_loc != null)
			{
				KamalokaGuardInstance npc = new KamalokaGuardInstance(IdFactory.getInstance().getNextId(), NpcHolder.getInstance().getTemplate(TELE_DEVICE_ID));
				npc.setSpawnedLoc(Location.parseLoc(tele_device_loc));
				npc.setReflection(r);
				npc.spawnMe(npc.getSpawnedLoc());
			}
		}
	}
}
