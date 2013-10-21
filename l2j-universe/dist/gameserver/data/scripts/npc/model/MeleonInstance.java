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

import lineage2.commons.lang.reference.HardReference;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.instances.SpecialMonsterInstance;
import lineage2.gameserver.templates.npc.NpcTemplate;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class MeleonInstance extends SpecialMonsterInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field Young_Watermelon. (value is 13271)
	 */
	public final static int Young_Watermelon = 13271;
	/**
	 * Field Rain_Watermelon. (value is 13273)
	 */
	public final static int Rain_Watermelon = 13273;
	/**
	 * Field Defective_Watermelon. (value is 13272)
	 */
	public final static int Defective_Watermelon = 13272;
	/**
	 * Field Young_Honey_Watermelon. (value is 13275)
	 */
	public final static int Young_Honey_Watermelon = 13275;
	/**
	 * Field Rain_Honey_Watermelon. (value is 13277)
	 */
	public final static int Rain_Honey_Watermelon = 13277;
	/**
	 * Field Defective_Honey_Watermelon. (value is 13276)
	 */
	public final static int Defective_Honey_Watermelon = 13276;
	/**
	 * Field Large_Rain_Watermelon. (value is 13274)
	 */
	public final static int Large_Rain_Watermelon = 13274;
	/**
	 * Field Large_Rain_Honey_Watermelon. (value is 13278)
	 */
	public final static int Large_Rain_Honey_Watermelon = 13278;
	/**
	 * Field _spawnerRef.
	 */
	private HardReference<Player> _spawnerRef;
	
	/**
	 * Constructor for MeleonInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public MeleonInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	/**
	 * Method setSpawner.
	 * @param spawner Player
	 */
	public void setSpawner(Player spawner)
	{
		_spawnerRef = spawner.getRef();
	}
	
	/**
	 * Method getSpawner.
	 * @return Player
	 */
	public Player getSpawner()
	{
		return _spawnerRef.get();
	}
	
	/**
	 * Method reduceCurrentHp.
	 * @param i double
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
	public void reduceCurrentHp(double i, double reflectableDamage, Creature attacker, Skill skill, boolean awake, boolean standUp, boolean directHp, boolean canReflect, boolean transferDamage, boolean isDot, boolean sendMessage)
	{
		if (attacker.getActiveWeaponInstance() == null)
		{
			return;
		}
		int weaponId = attacker.getActiveWeaponInstance().getItemId();
		if ((getNpcId() == Defective_Honey_Watermelon) || (getNpcId() == Rain_Honey_Watermelon) || (getNpcId() == Large_Rain_Honey_Watermelon))
		{
			if ((weaponId != 4202) && (weaponId != 5133) && (weaponId != 5817) && (weaponId != 7058) && (weaponId != 8350))
			{
				return;
			}
			i = 1;
		}
		else if ((getNpcId() == Rain_Watermelon) || (getNpcId() == Defective_Watermelon) || (getNpcId() == Large_Rain_Watermelon))
		{
			i = 5;
		}
		else
		{
			return;
		}
		super.reduceCurrentHp(i, reflectableDamage, attacker, skill, awake, standUp, directHp, canReflect, transferDamage, isDot, sendMessage);
	}
	
	/**
	 * Method getRegenTick.
	 * @return long
	 */
	@Override
	public long getRegenTick()
	{
		return 0L;
	}
	
	/**
	 * Method canChampion.
	 * @return boolean
	 */
	@Override
	public boolean canChampion()
	{
		return false;
	}
}
