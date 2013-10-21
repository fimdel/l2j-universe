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
package lineage2.gameserver.data.xml.holder;

import gnu.trove.map.hash.TIntObjectHashMap;
import lineage2.commons.data.xml.AbstractHolder;
import lineage2.gameserver.model.PetData;
import lineage2.gameserver.templates.StatsSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class PetDataHolder extends AbstractHolder
{
	/**
	 * Field _instance.
	 */
	private static PetDataHolder _instance = new PetDataHolder();
	/**
	 * Field _pets.
	 */
	private final TIntObjectHashMap<PetData> _pets = new TIntObjectHashMap<>();
	/**
	 * Field _log.
	 */
	@SuppressWarnings("unused")
	private static final Logger _log = LoggerFactory.getLogger(PetDataHolder.class);
	
	/**
	 * Method getInstance.
	 * @return PetDataHolder
	 */
	public static PetDataHolder getInstance()
	{
		return _instance;
	}
	
	/**
	 * Method addPetData.
	 * @param set StatsSet
	 */
	public void addPetData(StatsSet set)
	{
		PetData petData = new PetData();
		int id = set.getInteger("id");
		petData.setID(id);
		petData.setLevel(set.getInteger("level"));
		petData.setExp(set.getLong("exp"));
		petData.setHP(set.getInteger("hp"));
		petData.setMP(set.getInteger("mp"));
		petData.setPAtk(set.getInteger("patk"));
		petData.setPDef(set.getInteger("pdef"));
		petData.setMAtk(set.getInteger("matk"));
		petData.setMDef(set.getInteger("mdef"));
		petData.setAccuracy(37 + petData.getLevel());
		petData.setEvasion(33 + petData.getLevel());
		petData.setCritical(40);
		petData.setSpeed(137);
		petData.setAtkSpeed(278);
		petData.setCastSpeed(333);
		petData.setFeedMax(set.getInteger("max_meal"));
		petData.setFeedBattle(set.getInteger("consume_meal_in_battle"));
		petData.setFeedNormal(set.getInteger("consume_meal_in_normal"));
		petData.setMaxLoad(set.getInteger("load"));
		petData.setHpRegen(set.getInteger("hpreg"));
		petData.setMpRegen(set.getInteger("mpreg"));
		_pets.put((petData.getID() * 100) + petData.getLevel(), petData);
	}
	
	/**
	 * Method getInfo.
	 * @param petNpcId int
	 * @param level int
	 * @return PetData
	 */
	public PetData getInfo(int petNpcId, int level)
	{
		PetData result = null;
		while ((result == null) && (level < 100))
		{
			result = _pets.get((petNpcId * 100) + level);
			level++;
		}
		return result;
	}
	
	/**
	 * Method size.
	 * @return int
	 */
	@Override
	public int size()
	{
		return _pets.size();
	}
	
	/**
	 * Method clear.
	 */
	@Override
	public void clear()
	{
		_pets.clear();
	}
}
