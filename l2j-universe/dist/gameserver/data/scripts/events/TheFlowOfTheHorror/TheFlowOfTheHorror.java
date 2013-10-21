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
package events.TheFlowOfTheHorror;

import java.util.ArrayList;
import java.util.List;

import lineage2.commons.lang.reference.HardReference;
import lineage2.commons.lang.reference.HardReferences;
import lineage2.gameserver.data.xml.holder.NpcHolder;
import lineage2.gameserver.idfactory.IdFactory;
import lineage2.gameserver.instancemanager.ServerVariables;
import lineage2.gameserver.model.GameObjectsStorage;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.MonsterInstance;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.Location;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class TheFlowOfTheHorror extends Functions implements ScriptFile
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(TheFlowOfTheHorror.class);
	/**
	 * Field Gilmore.
	 */
	private static int Gilmore = 30754;
	/**
	 * Field Shackle.
	 */
	private static int Shackle = 20235;
	/**
	 * Field _oldGilmoreRef.
	 */
	private static HardReference<NpcInstance> _oldGilmoreRef = HardReferences.emptyRef();
	/**
	 * Field _stage.
	 */
	private static int _stage = 1;
	/**
	 * Field _spawns.
	 */
	private static List<MonsterInstance> _spawns = new ArrayList<>();
	/**
	 * Field points11.
	 */
	private static List<Location> points11 = new ArrayList<>();
	/**
	 * Field points12.
	 */
	private static List<Location> points12 = new ArrayList<>();
	/**
	 * Field points13.
	 */
	private static List<Location> points13 = new ArrayList<>();
	/**
	 * Field points21.
	 */
	private static List<Location> points21 = new ArrayList<>();
	/**
	 * Field points22.
	 */
	private static List<Location> points22 = new ArrayList<>();
	/**
	 * Field points23.
	 */
	private static List<Location> points23 = new ArrayList<>();
	/**
	 * Field points31.
	 */
	private static List<Location> points31 = new ArrayList<>();
	/**
	 * Field points32.
	 */
	private static List<Location> points32 = new ArrayList<>();
	/**
	 * Field points33.
	 */
	private static List<Location> points33 = new ArrayList<>();
	
	/**
	 * Method onLoad.
	 * @see lineage2.gameserver.scripts.ScriptFile#onLoad()
	 */
	@Override
	public void onLoad()
	{
		points11.add(new Location(84211, 117965, -3020));
		points11.add(new Location(83389, 117590, -3036));
		points11.add(new Location(82226, 117051, -3150));
		points11.add(new Location(80902, 116155, -3533));
		points11.add(new Location(79832, 115784, -3733));
		points11.add(new Location(78442, 116510, -3823));
		points11.add(new Location(76299, 117355, -3786));
		points11.add(new Location(74244, 117674, -3785));
		points12.add(new Location(84231, 117597, -3020));
		points12.add(new Location(82536, 116986, -3093));
		points12.add(new Location(79428, 116341, -3749));
		points12.add(new Location(76970, 117362, -3771));
		points12.add(new Location(74322, 117845, -3767));
		points13.add(new Location(83962, 118387, -3022));
		points13.add(new Location(81960, 116925, -3216));
		points13.add(new Location(80223, 116059, -3665));
		points13.add(new Location(78214, 116783, -3854));
		points13.add(new Location(76208, 117462, -3791));
		points13.add(new Location(74278, 117454, -3804));
		points21.add(new Location(79192, 111481, -3011));
		points21.add(new Location(79014, 112396, -3090));
		points21.add(new Location(79309, 113692, -3437));
		points21.add(new Location(79350, 115337, -3758));
		points21.add(new Location(78390, 116309, -3772));
		points21.add(new Location(76794, 117092, -3821));
		points21.add(new Location(74451, 117623, -3797));
		points22.add(new Location(79297, 111456, -3017));
		points22.add(new Location(79020, 112217, -3087));
		points22.add(new Location(79167, 113236, -3289));
		points22.add(new Location(79513, 115408, -3752));
		points22.add(new Location(78555, 116816, -3812));
		points22.add(new Location(76932, 117277, -3781));
		points22.add(new Location(75422, 117788, -3755));
		points22.add(new Location(74223, 117898, -3753));
		points23.add(new Location(79635, 110741, -3003));
		points23.add(new Location(78994, 111858, -3061));
		points23.add(new Location(79088, 112949, -3226));
		points23.add(new Location(79424, 114499, -3674));
		points23.add(new Location(78913, 116266, -3779));
		points23.add(new Location(76930, 117137, -3819));
		points23.add(new Location(75533, 117569, -3781));
		points23.add(new Location(74255, 117398, -3804));
		points31.add(new Location(83128, 111358, -3663));
		points31.add(new Location(81538, 111896, -3631));
		points31.add(new Location(80312, 113837, -3752));
		points31.add(new Location(79012, 115998, -3772));
		points31.add(new Location(77377, 117052, -3812));
		points31.add(new Location(75394, 117608, -3772));
		points31.add(new Location(73998, 117647, -3784));
		points32.add(new Location(83245, 110790, -3772));
		points32.add(new Location(81832, 111379, -3641));
		points32.add(new Location(81405, 112403, -3648));
		points32.add(new Location(79827, 114496, -3752));
		points32.add(new Location(78174, 116968, -3821));
		points32.add(new Location(75944, 117653, -3777));
		points32.add(new Location(74379, 117939, -3755));
		points33.add(new Location(82584, 111930, -3568));
		points33.add(new Location(81389, 111989, -3647));
		points33.add(new Location(80129, 114044, -3748));
		points33.add(new Location(79190, 115579, -3743));
		points33.add(new Location(77989, 116811, -3849));
		points33.add(new Location(76009, 117405, -3800));
		points33.add(new Location(74113, 117441, -3797));
		if (isActive())
		{
			activateAI();
			_log.info("Loaded Event: The Flow Of The Horror [state: activated]");
		}
		else
		{
			_log.info("Loaded Event: The Flow Of The Horror [state: deactivated]");
		}
	}
	
	/**
	 * Method spawnNewWave.
	 */
	public static void spawnNewWave()
	{
		spawn(Shackle, points11);
		spawn(Shackle, points12);
		spawn(Shackle, points13);
		spawn(Shackle, points21);
		spawn(Shackle, points22);
		spawn(Shackle, points23);
		spawn(Shackle, points31);
		spawn(Shackle, points32);
		spawn(Shackle, points33);
		_stage = 2;
	}
	
	/**
	 * Method spawn.
	 * @param id int
	 * @param points List<Location>
	 */
	private static void spawn(int id, List<Location> points)
	{
		NpcTemplate template = NpcHolder.getInstance().getTemplate(id);
		MonsterInstance monster = new MonsterInstance(IdFactory.getInstance().getNextId(), template);
		monster.setCurrentHpMp(monster.getMaxHp(), monster.getMaxMp(), true);
		monster.setXYZ(points.get(0).x, points.get(0).y, points.get(0).z);
		MonstersAI ai = new MonstersAI(monster);
		ai.setPoints(points);
		monster.setAI(ai);
		monster.spawnMe();
		_spawns.add(monster);
	}
	
	/**
	 * Method activateAI.
	 */
	private void activateAI()
	{
		NpcInstance target = GameObjectsStorage.getByNpcId(Gilmore);
		if (target != null)
		{
			_oldGilmoreRef = target.getRef();
			target.decayMe();
			NpcTemplate template = NpcHolder.getInstance().getTemplate(Gilmore);
			MonsterInstance monster = new MonsterInstance(IdFactory.getInstance().getNextId(), template);
			monster.setCurrentHpMp(monster.getMaxHp(), monster.getMaxMp(), true);
			monster.setXYZ(73329, 117705, -3741);
			GilmoreAI ai = new GilmoreAI(monster);
			monster.setAI(ai);
			monster.spawnMe();
			_spawns.add(monster);
		}
	}
	
	/**
	 * Method deactivateAI.
	 */
	private void deactivateAI()
	{
		for (MonsterInstance monster : _spawns)
		{
			if (monster != null)
			{
				monster.deleteMe();
			}
		}
		NpcInstance GilmoreInstance = _oldGilmoreRef.get();
		if (GilmoreInstance != null)
		{
			GilmoreInstance.spawnMe();
		}
	}
	
	/**
	 * Method isActive.
	 * @return boolean
	 */
	private static boolean isActive()
	{
		return ServerVariables.getString("TheFlowOfTheHorror", "off").equalsIgnoreCase("on");
	}
	
	/**
	 * Method startEvent.
	 */
	public void startEvent()
	{
		Player player = getSelf();
		if (!player.getPlayerAccess().IsEventGm)
		{
			return;
		}
		if (!isActive())
		{
			ServerVariables.set("TheFlowOfTheHorror", "on");
			activateAI();
			System.out.println("Event 'The Flow Of The Horror' started.");
		}
		else
		{
			player.sendMessage("Event 'The Flow Of The Horror' already started.");
		}
		show("admin/events.htm", player);
	}
	
	/**
	 * Method stopEvent.
	 */
	public void stopEvent()
	{
		Player player = getSelf();
		if (!player.getPlayerAccess().IsEventGm)
		{
			return;
		}
		if (isActive())
		{
			ServerVariables.unset("TheFlowOfTheHorror");
			deactivateAI();
			System.out.println("Event 'The Flow Of The Horror' stopped.");
		}
		else
		{
			player.sendMessage("Event 'The Flow Of The Horror' not started.");
		}
		show("admin/events.htm", player);
	}
	
	/**
	 * Method onReload.
	 * @see lineage2.gameserver.scripts.ScriptFile#onReload()
	 */
	@Override
	public void onReload()
	{
		deactivateAI();
	}
	
	/**
	 * Method onShutdown.
	 * @see lineage2.gameserver.scripts.ScriptFile#onShutdown()
	 */
	@Override
	public void onShutdown()
	{
		deactivateAI();
	}
	
	/**
	 * Method getStage.
	 * @return int
	 */
	public static int getStage()
	{
		return _stage;
	}
	
	/**
	 * Method setStage.
	 * @param stage int
	 */
	public static void setStage(int stage)
	{
		_stage = stage;
	}
}
