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
package lineage2.gameserver.model.actor.instances.player;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Bonus
{
	/**
	 * Field NO_BONUS. (value is 0)
	 */
	public static final int NO_BONUS = 0;
	/**
	 * Field BONUS_GLOBAL_ON_LOGINSERVER. (value is 1)
	 */
	public static final int BONUS_GLOBAL_ON_LOGINSERVER = 1;
	/**
	 * Field BONUS_GLOBAL_ON_GAMESERVER. (value is 2)
	 */
	public static final int BONUS_GLOBAL_ON_GAMESERVER = 2;
	/**
	 * Field rateXp.
	 */
	private double rateXp = 1.;
	/**
	 * Field rateSp.
	 */
	private double rateSp = 1.;
	/**
	 * Field questRewardRate.
	 */
	private double questRewardRate = 1.;
	/**
	 * Field questDropRate.
	 */
	private double questDropRate = 1.;
	/**
	 * Field dropAdena.
	 */
	private double dropAdena = 1.;
	/**
	 * Field dropItems.
	 */
	private double dropItems = 1.;
	/**
	 * Field dropSpoil.
	 */
	private double dropSpoil = 1.;
	/**
	 * Field bonusExpire.
	 */
	private int bonusExpire;
	
	/**
	 * Method getRateXp.
	 * @return double
	 */
	public double getRateXp()
	{
		return rateXp;
	}
	
	/**
	 * Method setRateXp.
	 * @param rateXp double
	 */
	public void setRateXp(double rateXp)
	{
		this.rateXp = rateXp;
	}
	
	/**
	 * Method getRateSp.
	 * @return double
	 */
	public double getRateSp()
	{
		return rateSp;
	}
	
	/**
	 * Method setRateSp.
	 * @param rateSp double
	 */
	public void setRateSp(double rateSp)
	{
		this.rateSp = rateSp;
	}
	
	/**
	 * Method getQuestRewardRate.
	 * @return double
	 */
	public double getQuestRewardRate()
	{
		return questRewardRate;
	}
	
	/**
	 * Method setQuestRewardRate.
	 * @param questRewardRate double
	 */
	public void setQuestRewardRate(double questRewardRate)
	{
		this.questRewardRate = questRewardRate;
	}
	
	/**
	 * Method getQuestDropRate.
	 * @return double
	 */
	public double getQuestDropRate()
	{
		return questDropRate;
	}
	
	/**
	 * Method setQuestDropRate.
	 * @param questDropRate double
	 */
	public void setQuestDropRate(double questDropRate)
	{
		this.questDropRate = questDropRate;
	}
	
	/**
	 * Method getDropAdena.
	 * @return double
	 */
	public double getDropAdena()
	{
		return dropAdena;
	}
	
	/**
	 * Method setDropAdena.
	 * @param dropAdena double
	 */
	public void setDropAdena(double dropAdena)
	{
		this.dropAdena = dropAdena;
	}
	
	/**
	 * Method getDropItems.
	 * @return double
	 */
	public double getDropItems()
	{
		return dropItems;
	}
	
	/**
	 * Method setDropItems.
	 * @param dropItems double
	 */
	public void setDropItems(double dropItems)
	{
		this.dropItems = dropItems;
	}
	
	/**
	 * Method getDropSpoil.
	 * @return double
	 */
	public double getDropSpoil()
	{
		return dropSpoil;
	}
	
	/**
	 * Method setDropSpoil.
	 * @param dropSpoil double
	 */
	public void setDropSpoil(double dropSpoil)
	{
		this.dropSpoil = dropSpoil;
	}
	
	/**
	 * Method getBonusExpire.
	 * @return int
	 */
	public int getBonusExpire()
	{
		return bonusExpire;
	}
	
	/**
	 * Method setBonusExpire.
	 * @param bonusExpire int
	 */
	public void setBonusExpire(int bonusExpire)
	{
		this.bonusExpire = bonusExpire;
	}
}
