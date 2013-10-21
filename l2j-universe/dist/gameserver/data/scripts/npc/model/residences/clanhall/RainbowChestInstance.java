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

import lineage2.commons.util.Rnd;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.instances.MonsterInstance;
import lineage2.gameserver.templates.npc.NpcTemplate;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RainbowChestInstance extends MonsterInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field ItemA. (value is 8035)
	 */
	private static final int ItemA = 8035;
	/**
	 * Field ItemB. (value is 8036)
	 */
	private static final int ItemB = 8036;
	/**
	 * Field ItemC. (value is 8037)
	 */
	private static final int ItemC = 8037;
	/**
	 * Field ItemD. (value is 8038)
	 */
	private static final int ItemD = 8038;
	/**
	 * Field ItemE. (value is 8039)
	 */
	private static final int ItemE = 8039;
	/**
	 * Field ItemF. (value is 8040)
	 */
	private static final int ItemF = 8040;
	/**
	 * Field ItemG. (value is 8041)
	 */
	private static final int ItemG = 8041;
	/**
	 * Field ItemH. (value is 8042)
	 */
	private static final int ItemH = 8042;
	/**
	 * Field ItemI. (value is 8043)
	 */
	private static final int ItemI = 8043;
	/**
	 * Field ItemK. (value is 8045)
	 */
	private static final int ItemK = 8045;
	/**
	 * Field ItemL. (value is 8046)
	 */
	private static final int ItemL = 8046;
	/**
	 * Field ItemN. (value is 8047)
	 */
	private static final int ItemN = 8047;
	/**
	 * Field ItemO. (value is 8048)
	 */
	private static final int ItemO = 8048;
	/**
	 * Field ItemP. (value is 8049)
	 */
	private static final int ItemP = 8049;
	/**
	 * Field ItemR. (value is 8050)
	 */
	private static final int ItemR = 8050;
	/**
	 * Field ItemS. (value is 8051)
	 */
	private static final int ItemS = 8051;
	/**
	 * Field ItemT. (value is 8052)
	 */
	private static final int ItemT = 8052;
	/**
	 * Field ItemU. (value is 8053)
	 */
	private static final int ItemU = 8053;
	/**
	 * Field ItemW. (value is 8054)
	 */
	private static final int ItemW = 8054;
	/**
	 * Field ItemY. (value is 8055)
	 */
	private static final int ItemY = 8055;
	
	/**
	 * Constructor for RainbowChestInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public RainbowChestInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
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
		if ((attacker == null) || !attacker.isPlayer() || (attacker.getActiveWeaponInstance() != null) || (skill != null) || isDot)
		{
			return;
		}
		super.reduceCurrentHp(getMaxHp() * 0.2, reflectableDamage, attacker, skill, awake, standUp, directHp, canReflect, transferDamage, isDot, sendMessage);
	}
	
	/**
	 * Method onDeath.
	 * @param k Creature
	 */
	@Override
	public void onDeath(Creature k)
	{
		super.onDeath(k);
		Player killer = k.getPlayer();
		if (killer == null)
		{
			return;
		}
		int count = 1 + Rnd.get(2);
		for (int i = 0; i < count; i++)
		{
			int chance = Rnd.get(100);
			if (chance <= 5)
			{
				dropItem(killer, ItemA, 1);
			}
			else if ((chance > 5) && (chance <= 10))
			{
				dropItem(killer, ItemB, 1);
			}
			else if ((chance > 10) && (chance <= 15))
			{
				dropItem(killer, ItemC, 1);
			}
			else if ((chance > 15) && (chance <= 20))
			{
				dropItem(killer, ItemD, 1);
			}
			else if ((chance > 20) && (chance <= 25))
			{
				dropItem(killer, ItemE, 1);
			}
			else if ((chance > 25) && (chance <= 30))
			{
				dropItem(killer, ItemF, 1);
			}
			else if ((chance > 30) && (chance <= 35))
			{
				dropItem(killer, ItemG, 1);
			}
			else if ((chance > 35) && (chance <= 40))
			{
				dropItem(killer, ItemH, 1);
			}
			else if ((chance > 40) && (chance <= 45))
			{
				dropItem(killer, ItemI, 1);
			}
			else if ((chance > 45) && (chance <= 50))
			{
				dropItem(killer, ItemK, 1);
			}
			else if ((chance > 50) && (chance <= 55))
			{
				dropItem(killer, ItemL, 1);
			}
			else if ((chance > 55) && (chance <= 60))
			{
				dropItem(killer, ItemN, 1);
			}
			else if ((chance > 60) && (chance <= 65))
			{
				dropItem(killer, ItemO, 1);
			}
			else if ((chance > 65) && (chance <= 70))
			{
				dropItem(killer, ItemP, 1);
			}
			else if ((chance > 70) && (chance <= 75))
			{
				dropItem(killer, ItemR, 1);
			}
			else if ((chance > 75) && (chance <= 80))
			{
				dropItem(killer, ItemS, 1);
			}
			else if ((chance > 80) && (chance <= 85))
			{
				dropItem(killer, ItemT, 1);
			}
			else if ((chance > 85) && (chance <= 90))
			{
				dropItem(killer, ItemU, 1);
			}
			else if ((chance > 90) && (chance <= 95))
			{
				dropItem(killer, ItemW, 1);
			}
			else if (chance > 95)
			{
				dropItem(killer, ItemY, 1);
			}
		}
	}
}
