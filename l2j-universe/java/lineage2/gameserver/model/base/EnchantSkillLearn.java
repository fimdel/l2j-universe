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
package lineage2.gameserver.model.base;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.tables.SkillTable;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class EnchantSkillLearn
{
	/**
	 * Field _id.
	 */
	private final int _id;
	/**
	 * Field _level.
	 */
	private final int _level;
	/**
	 * Field _name.
	 */
	private final String _name;
	/**
	 * Field _type.
	 */
	private final String _type;
	/**
	 * Field _baseLvl.
	 */
	private final int _baseLvl;
	/**
	 * Field _maxLvl.
	 */
	private final int _maxLvl;
	/**
	 * Field _minSkillLevel.
	 */
	private final int _minSkillLevel;
	/**
	 * Field _costMul.
	 */
	private final int _costMul;
	
	/**
	 * Constructor for EnchantSkillLearn.
	 * @param id int
	 * @param lvl int
	 * @param name String
	 * @param type String
	 * @param minSkillLvl int
	 * @param baseLvl int
	 * @param maxLvl int
	 */
	public EnchantSkillLearn(int id, int lvl, String name, String type, int minSkillLvl, int baseLvl, int maxLvl)
	{
		_id = id;
		_level = lvl;
		_baseLvl = baseLvl;
		_maxLvl = maxLvl;
		_minSkillLevel = minSkillLvl;
		_name = name.intern();
		_type = type.intern();
		_costMul = _maxLvl == 15 ? 5 : 1;
	}
	
	/**
	 * Method getId.
	 * @return int
	 */
	public int getId()
	{
		return _id;
	}
	
	/**
	 * Method getLevel.
	 * @return int
	 */
	public int getLevel()
	{
		return _level;
	}
	
	/**
	 * Method getBaseLevel.
	 * @return int
	 */
	public int getBaseLevel()
	{
		return _baseLvl;
	}
	
	/**
	 * Method getMinSkillLevel.
	 * @return int
	 */
	public int getMinSkillLevel()
	{
		return _minSkillLevel;
	}
	
	/**
	 * Method getName.
	 * @return String
	 */
	public String getName()
	{
		return _name;
	}
	
	/**
	 * Method getCostMult.
	 * @return int
	 */
	public int getCostMult()
	{
		return _costMul;
	}
	
	/**
	 * Method getCost.
	 * @return int[]
	 */
	public int[] getCost()
	{
		return SkillTable.getInstance().getInfo(_id, 1).isOffensive() ? _priceCombat[_level % 100] : _priceBuff[_level % 100];
	}
	
	/**
	 * Field _chance.
	 */
	private static final int[][] _chance =
	{
		{},
		{
			82,
			92,
			97,
			97,
			97,
			97,
			97,
			97,
			97,
			97
		},
		{
			80,
			90,
			95,
			95,
			95,
			95,
			95,
			95,
			95,
			95
		},
		{
			78,
			88,
			93,
			93,
			93,
			93,
			93,
			93,
			93,
			93
		},
		{
			52,
			76,
			86,
			91,
			91,
			91,
			91,
			91,
			91,
			91
		},
		{
			50,
			74,
			84,
			89,
			89,
			89,
			89,
			89,
			89,
			89
		},
		{
			48,
			72,
			82,
			87,
			87,
			87,
			87,
			87,
			87,
			87
		},
		{
			01,
			46,
			70,
			80,
			85,
			85,
			85,
			85,
			85,
			85
		},
		{
			01,
			44,
			68,
			78,
			83,
			83,
			83,
			83,
			83,
			83
		},
		{
			01,
			42,
			66,
			76,
			81,
			81,
			81,
			81,
			81,
			81
		},
		{
			01,
			01,
			40,
			64,
			74,
			79,
			79,
			79,
			79,
			79
		},
		{
			01,
			01,
			38,
			62,
			72,
			77,
			77,
			77,
			77,
			77
		},
		{
			01,
			01,
			36,
			60,
			70,
			75,
			75,
			75,
			75,
			75
		},
		{
			01,
			01,
			01,
			34,
			58,
			68,
			73,
			73,
			73,
			73
		},
		{
			01,
			01,
			01,
			32,
			56,
			66,
			71,
			71,
			71,
			71
		},
		{
			01,
			01,
			01,
			30,
			54,
			64,
			69,
			69,
			69,
			69
		},
		{
			01,
			01,
			01,
			01,
			28,
			52,
			62,
			67,
			67,
			67
		},
		{
			01,
			01,
			01,
			01,
			26,
			50,
			60,
			65,
			65,
			65
		},
		{
			01,
			01,
			01,
			01,
			24,
			48,
			58,
			63,
			63,
			63
		},
		{
			01,
			01,
			01,
			01,
			01,
			22,
			46,
			56,
			61,
			61
		},
		{
			01,
			01,
			01,
			01,
			01,
			20,
			44,
			54,
			59,
			59
		},
		{
			01,
			01,
			01,
			01,
			01,
			18,
			42,
			52,
			57,
			57
		},
		{
			01,
			01,
			01,
			01,
			01,
			01,
			16,
			40,
			50,
			55
		},
		{
			01,
			01,
			01,
			01,
			01,
			01,
			14,
			38,
			48,
			53
		},
		{
			01,
			01,
			01,
			01,
			01,
			01,
			12,
			36,
			46,
			51
		},
		{
			01,
			01,
			01,
			01,
			01,
			01,
			01,
			10,
			34,
			44
		},
		{
			01,
			01,
			01,
			01,
			01,
			01,
			01,
			8,
			32,
			42
		},
		{
			01,
			01,
			01,
			01,
			01,
			01,
			01,
			06,
			30,
			40
		},
		{
			01,
			01,
			01,
			01,
			01,
			01,
			01,
			01,
			04,
			28
		},
		{
			01,
			01,
			01,
			01,
			01,
			01,
			01,
			01,
			02,
			26
		},
		{
			01,
			01,
			01,
			01,
			01,
			01,
			01,
			01,
			02,
			24
		},
	};
	/**
	 * Field _chance15.
	 */
	private static final int[][] _chance15 =
	{
		{},
		{
			18,
			28,
			38,
			48,
			58,
			82,
			92,
			97,
			97,
			97
		},
		{
			01,
			01,
			01,
			46,
			56,
			80,
			90,
			95,
			95,
			95
		},
		{
			01,
			01,
			01,
			01,
			54,
			78,
			88,
			93,
			93,
			93
		},
		{
			01,
			01,
			01,
			01,
			42,
			52,
			76,
			86,
			91,
			91
		},
		{
			01,
			01,
			01,
			01,
			01,
			50,
			74,
			84,
			89,
			89
		},
		{
			01,
			01,
			01,
			01,
			01,
			48,
			72,
			82,
			87,
			87
		},
		{
			01,
			01,
			01,
			01,
			01,
			01,
			46,
			70,
			80,
			85
		},
		{
			01,
			01,
			01,
			01,
			01,
			01,
			44,
			68,
			78,
			83
		},
		{
			01,
			01,
			01,
			01,
			01,
			01,
			42,
			66,
			76,
			81
		},
		{
			01,
			01,
			01,
			01,
			01,
			01,
			01,
			40,
			64,
			74
		},
		{
			01,
			01,
			01,
			01,
			01,
			01,
			01,
			38,
			62,
			72
		},
		{
			01,
			01,
			01,
			01,
			01,
			01,
			01,
			36,
			60,
			70
		},
		{
			01,
			01,
			01,
			01,
			01,
			01,
			01,
			01,
			34,
			58
		},
		{
			01,
			01,
			01,
			01,
			01,
			01,
			01,
			01,
			32,
			56
		},
		{
			01,
			01,
			01,
			01,
			01,
			01,
			01,
			01,
			30,
			54
		},
	};
	/**
	 * Field _chance10.
	 */
	private static final int[][] _chance10 =
	{
		{},
		{
			85,
			86,
			87,
			88,
			89,
			90,
			91,
			92,
			93,
			94,
			95,
			96,
			97,
			98,
			99
		},
		{
			80,
			81,
			82,
			83,
			84,
			85,
			86,
			87,
			88,
			89,
			90,
			91,
			92,
			93,
			94
		},
		{
			75,
			76,
			77,
			78,
			79,
			80,
			81,
			82,
			83,
			84,
			85,
			86,
			87,
			88,
			89
		},
		{
			70,
			71,
			72,
			73,
			74,
			75,
			76,
			77,
			78,
			79,
			80,
			81,
			82,
			83,
			84
		},
		{
			65,
			66,
			67,
			68,
			69,
			70,
			71,
			72,
			73,
			74,
			75,
			76,
			77,
			78,
			79
		},
		{
			60,
			61,
			62,
			63,
			64,
			65,
			66,
			67,
			68,
			69,
			70,
			71,
			72,
			73,
			74
		},
		{
			30,
			56,
			57,
			58,
			59,
			60,
			61,
			62,
			63,
			64,
			65,
			66,
			67,
			68,
			69
		},
		{
			25,
			26,
			52,
			53,
			54,
			55,
			56,
			57,
			58,
			59,
			60,
			61,
			62,
			63,
			64
		},
		{
			20,
			21,
			22,
			48,
			49,
			50,
			51,
			52,
			53,
			54,
			55,
			56,
			57,
			58,
			59
		},
		{
			15,
			16,
			17,
			18,
			44,
			45,
			46,
			47,
			48,
			49,
			50,
			51,
			52,
			53,
			54
		},
	};
	/**
	 * Field _priceBuff.
	 */
	private static final int[][] _priceBuff =
	{
		{},
		{
			51975,
			352786
		},
		{
			51975,
			352786
		},
		{
			51975,
			352786
		},
		{
			78435,
			370279
		},
		{
			78435,
			370279
		},
		{
			78435,
			370279
		},
		{
			105210,
			388290
		},
		{
			105210,
			388290
		},
		{
			105210,
			388290
		},
		{
			132300,
			416514
		},
		{
			132300,
			416514
		},
		{
			132300,
			416514
		},
		{
			159705,
			435466
		},
		{
			159705,
			435466
		},
		{
			159705,
			435466
		},
		{
			187425,
			466445
		},
		{
			187425,
			466445
		},
		{
			187425,
			466445
		},
		{
			215460,
			487483
		},
		{
			215460,
			487483
		},
		{
			215460,
			487483
		},
		{
			243810,
			520215
		},
		{
			243810,
			520215
		},
		{
			243810,
			520215
		},
		{
			272475,
			542829
		},
		{
			272475,
			542829
		},
		{
			272475,
			542829
		},
		{
			304500,
			566426
		},
		{
			304500,
			566426
		},
		{
			304500,
			566426
		},
	};
	/**
	 * Field _priceCombat.
	 */
	private static final int[][] _priceCombat =
	{
		{},
		{
			93555,
			635014
		},
		{
			93555,
			635014
		},
		{
			93555,
			635014
		},
		{
			141183,
			666502
		},
		{
			141183,
			666502
		},
		{
			141183,
			666502
		},
		{
			189378,
			699010
		},
		{
			189378,
			699010
		},
		{
			189378,
			699010
		},
		{
			238140,
			749725
		},
		{
			238140,
			749725
		},
		{
			238140,
			749725
		},
		{
			287469,
			896981
		},
		{
			287469,
			896981
		},
		{
			287469,
			896981
		},
		{
			337365,
			959540
		},
		{
			337365,
			959540
		},
		{
			337365,
			959540
		},
		{
			387828,
			1002821
		},
		{
			387828,
			1002821
		},
		{
			387828,
			1002821
		},
		{
			438858,
			1070155
		},
		{
			438858,
			1070155
		},
		{
			438858,
			1070155
		},
		{
			496601,
			1142010
		},
		{
			496601,
			1142010
		},
		{
			496601,
			1142010
		},
		{
			561939,
			1218690
		},
		{
			561939,
			1218690
		},
		{
			561939,
			1218690
		},
	};
	
	/**
	 * Method getRate.
	 * @param ply Player
	 * @return int
	 */
	public int getRate(Player ply)
	{
		int level = _level % 100;
		int chance;
		switch (_maxLvl)
		{
			case 10:
			{
				chance = Math.min(_chance10[level].length - 1, ply.getLevel() - 85);
				return _chance10[level][chance];
			}
			case 15:
			{
				chance = Math.min(_chance15[level].length - 1, ply.getLevel() - 76);
				return _chance15[level][chance];
			}
			default:
			{
				chance = Math.min(_chance[level].length - 1, ply.getLevel() - 76);
			}
		}
		return _chance[level][chance];
	}
	
	/**
	 * Method getMaxLevel.
	 * @return int
	 */
	public int getMaxLevel()
	{
		return _maxLvl;
	}
	
	/**
	 * Method getType.
	 * @return String
	 */
	public String getType()
	{
		return _type;
	}
	
	/**
	 * Method hashCode.
	 * @return int
	 */
	@Override
	public int hashCode()
	{
		final int PRIME = 31;
		int result = 1;
		result = (PRIME * result) + _id;
		result = (PRIME * result) + _level;
		return result;
	}
	
	/**
	 * Method equals.
	 * @param obj Object
	 * @return boolean
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (getClass() != obj.getClass())
		{
			return false;
		}
		if (!(obj instanceof EnchantSkillLearn))
		{
			return false;
		}
		EnchantSkillLearn other = (EnchantSkillLearn) obj;
		return (getId() == other.getId()) && (getLevel() == other.getLevel());
	}
}
