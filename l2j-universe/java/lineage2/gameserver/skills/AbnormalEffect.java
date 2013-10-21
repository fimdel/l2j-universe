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
package lineage2.gameserver.skills;

import java.util.NoSuchElementException;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public enum AbnormalEffect
{
	NULL("null", 0x0, 0),
	BLEEDING("bleeding", 0x000001, 1),
	POISON("poison", 0x000002, 2),
	REDCIRCLE("redcircle", 0x000004, 3),
	ICE("ice", 0x000008, 4),
	WIND("wind", 0x000010, 5),
	FEAR("fear", 0x000020, 6),
	STUN("stun", 0x000040, 7),
	SLEEP("sleep", 0x000080, 8),
	MUTED("mute", 0x000100, 9),
	ROOT("root", 0x000200, 10),
	HOLD_1("hold1", 0x000400, 11),
	HOLD_2("hold2", 0x000800, 12),
	UNKNOWN_13("unknown13", 0x001000, 13),
	BIG_HEAD("bighead", 0x002000, 14),
	FLAME("flame", 0x004000, 15),
	ARCANE_SHIELD("arcane_shield", 0x008000, 16),
	GROW("grow", 0x010000, 17),
	FLOATING_ROOT("floatroot", 0x020000, 18),
	DANCE_STUNNED("dancestun", 0x040000, 19),
	FIREROOT_STUN("firerootstun", 0x080000, 20),
	STEALTH("shadow", 0x100000, 21),
	IMPRISIONING_1("imprison1", 0x200000, 22),
	IMPRISIONING_2("imprison2", 0x400000, 23),
	MAGIC_CIRCLE("magiccircle", 0x800000, 24),
	ICE2("ice2", 0x1000000, 25),
	EARTHQUAKE("earthquake", 0x2000000, 26),
	UNKNOWN_27("unknown27", 0x4000000, 27),
	INVULNERABLE("invul1", 0x8000000, 28),
	VITALITY("vitality", 0x10000000, 29),
	REAL_TARGET("realtarget", 0x20000000, 30),
	DEATH_MARK("deathmark", 0x40000000, 31),
	SKULL_FEAR("soulshock", 0x80000000, 32),
	
	// special effects
	S_INVINCIBLE("invul2", 0x000001, 33),
	S_AIR_STUN("airstun", 0x000002, 34),
	S_AIR_ROOT("airroot", 0x000004, 35),
	S_BAGUETTE_SWORD("baguettesword", 0x000008, 36),
	S_YELLOW_AFFRO("yellowafro", 0x000010, 37),
	S_PINK_AFFRO("pinkafro", 0x000020, 38),
	S_BLACK_AFFRO("blackafro", 0x000040, 39),
	S_UNKNOWN8("unknown8", 0x000080, 40),
	S_STIGMA_SHILIEN("stigma", 0x000100, 41),
	S_STAKATOROOT("stakatoroot", 0x000200, 42),
	S_FREEZING("frozenpillar", 0x000400, 43),
	S_VESPER_S("vesper_s", 0x000800, 44),
	S_VESPER_C("vesper_c", 0x001000, 45),
	S_VESPER_D("vesper_d", 0x002000, 46),
	S_47("s_47", 0x004000, 47),
	S_48("s_48", 0x008000, 48),
	S_HELLBINDING("hellbinding", 0x010000, 49),
	S_50("s_50", 0x020000, 50),
	S_51("s_51", 0x040000, 51),
	S_52("s_52", 0x080000, 52),
	S_53("s_53", 0x100000, 53),
	S_54("s_54", 0x200000, 57),
	S_55("s_55", 0x400000, 55),
	S_56("s_56", 0x800000, 56),
	S_57("s_57", 0x1000000, 57),
	S_58("s_58", 0x2000000, 58),
	S_59("s_59", 0x4000000, 59),
	S_60("s_60", 0x8000000, 60),
	S_61("s_61", 0x10000000, 61),
	S_ROLLINGTHUNDER("rollingThunder", 0x20000000, 62),
	S_63("s_63", 0x40000000, 63),
	S_64("s_64", 0x80000000, 64),
	S_65("s_65", 0x000001, 65),
	S_66("s_66", 0x000002, 66),
	S_67("s_67", 0x000004, 67),
	
	// event effects
	E_AFRO_1("afrobaguette1", 0x000001, 0),
	E_AFRO_2("afrobaguette2", 0x000002, 0),
	E_AFRO_3("afrobaguette3", 0x000004, 0),
	E_EVASWRATH("evaswrath", 0x000008, 0),
	E_HEADPHONE("headphone", 0x000010, 0),
	E_VESPER_1("vesper1", 0x000020, 0),
	E_VESPER_2("vesper2", 0x000040, 0),
	E_VESPER_3("vesper3", 0x000080, 0),
	HUNTING_BONUS("hunting_bonus", 0x80000, 0),
	AVE_ADVENT_BLESSING("ave_advent_blessing", 0x080000, 0); // Add NevitAdvent by pmq
	
	private final int _mask;
	private final String _name;
	private final int _id;
	
	private AbnormalEffect(String name, int mask, int id)
	{
		_name = name;
		_mask = mask;
		_id = id;
	}
	
	/**
	 * Method getMask.
	 * @return integer
	 */
	public final int getMask()
	{
		return _mask;
	}
	
	/**
	 * Method getName.
	 * @return String
	 */
	public final String getName()
	{
		return _name;
	}
	
	/**
	 * Method getName.
	 * @return integer
	 */
	public final int getId()
	{
		return _id;
	}
	
	/**
	 * Method isSpecial.
	 * @return boolean
	 */
	public final boolean isSpecial()
	{
		return getId() > 32;
	}
	
	/**
	 * Method isEvent.
	 * @return boolean
	 */
	public final boolean isEvent()
	{
		return false;
	}
	
	/**
	 * Method getByName.
	 * @param name String
	 * @return AbnormalEffect
	 */
	public static AbnormalEffect getByName(String name)
	{
		for (AbnormalEffect eff : AbnormalEffect.values())
		{
			if (eff.getName().equals(name))
			{
				return eff;
			}
		}
		throw new NoSuchElementException("AbnormalEffect not found for name: '" + name + "'.\n Please check " + AbnormalEffect.class.getCanonicalName());
	}
}
