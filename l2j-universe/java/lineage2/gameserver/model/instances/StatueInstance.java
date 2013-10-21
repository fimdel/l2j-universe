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
package lineage2.gameserver.model.instances;

import lineage2.gameserver.dao.CharacterDAO;
import lineage2.gameserver.templates.npc.NpcTemplate;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class StatueInstance extends NpcInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field _recordId.
	 */
	private final int _recordId;
	/**
	 * Field _socialId.
	 */
	private final int _socialId;
	/**
	 * Field _socialFrame.
	 */
	private final int _socialFrame;
	/**
	 * Field _sex.
	 */
	private final int _sex;
	/**
	 * Field _hairStyle.
	 */
	private final int _hairStyle;
	/**
	 * Field _hairColor.
	 */
	private final int _hairColor;
	/**
	 * Field _face.
	 */
	private final int _face;
	/**
	 * Field _necklace.
	 */
	private int _necklace = 0;
	/**
	 * Field _head.
	 */
	private int _head = 0;
	/**
	 * Field _classId.
	 */
	private int _classId = 0;
	/**
	 * Field _rHand.
	 */
	private int _rHand = 0;
	/**
	 * Field _lHand.
	 */
	private int _lHand = 0;
	/**
	 * Field _gloves.
	 */
	private int _gloves = 0;
	/**
	 * Field _chest.
	 */
	private int _chest = 0;
	/**
	 * Field _pants.
	 */
	private int _pants = 0;
	/**
	 * Field _boots.
	 */
	private int _boots = 0;
	/**
	 * Field _cloak.
	 */
	private int _cloak = 0;
	/**
	 * Field _hair1.
	 */
	private int _hair1 = 0;
	/**
	 * Field _hair2.
	 */
	private int _hair2 = 0;
	/**
	 * Field _race.
	 */
	private int _race = 0;
	
	/**
	 * Constructor for StatueInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 * @param playerObjId int
	 * @param loc int[]
	 * @param items int[]
	 * @param appearance int[]
	 */
	public StatueInstance(int objectId, NpcTemplate template, int playerObjId, int loc[], int items[], int appearance[])
	{
		super(objectId, template);
		_recordId = loc[4];
		_socialId = 0;
		_socialFrame = 0;
		_necklace = items[0];
		_head = items[1];
		_rHand = items[2];
		_lHand = items[3];
		_gloves = items[4];
		_chest = items[5];
		_pants = items[6];
		_boots = items[7];
		_cloak = items[8];
		_hair1 = items[9];
		_hair2 = items[10];
		setName(CharacterDAO.getInstance().getNameByObjectId(playerObjId));
		_classId = appearance[0];
		_race = appearance[1];
		_sex = appearance[2];
		_hairStyle = appearance[3];
		_hairColor = appearance[4];
		_face = appearance[5];
		setIsInvul(true);
		setXYZ(loc[0], loc[1], loc[2]);
		setHeading(loc[3]);
		spawnMe();
	}
	
	/**
	 * Method getRecordId.
	 * @return int
	 */
	public int getRecordId()
	{
		return _recordId;
	}
	
	/**
	 * Method getSocialId.
	 * @return int
	 */
	public int getSocialId()
	{
		return _socialId;
	}
	
	/**
	 * Method getSocialFrame.
	 * @return int
	 */
	public int getSocialFrame()
	{
		return _socialFrame;
	}
	
	/**
	 * Method getClassId.
	 * @return int
	 */
	public int getClassId()
	{
		return _classId;
	}
	
	/**
	 * Method getRace.
	 * @return int
	 */
	public int getRace()
	{
		return _race;
	}
	
	/**
	 * Method getSex.
	 * @return int
	 */
	public int getSex()
	{
		return _sex;
	}
	
	/**
	 * Method getHairStyle.
	 * @return int
	 */
	public int getHairStyle()
	{
		return _hairStyle;
	}
	
	/**
	 * Method getHairColor.
	 * @return int
	 */
	public int getHairColor()
	{
		return _hairColor;
	}
	
	/**
	 * Method getFace.
	 * @return int
	 */
	public int getFace()
	{
		return _face;
	}
	
	/**
	 * Method getNecklace.
	 * @return int
	 */
	public int getNecklace()
	{
		return _necklace;
	}
	
	/**
	 * Method getHead.
	 * @return int
	 */
	public int getHead()
	{
		return _head;
	}
	
	/**
	 * Method getRHand.
	 * @return int
	 */
	public int getRHand()
	{
		return _rHand;
	}
	
	/**
	 * Method getLHand.
	 * @return int
	 */
	public int getLHand()
	{
		return _lHand;
	}
	
	/**
	 * Method getGloves.
	 * @return int
	 */
	public int getGloves()
	{
		return _gloves;
	}
	
	/**
	 * Method getChest.
	 * @return int
	 */
	public int getChest()
	{
		return _chest;
	}
	
	/**
	 * Method getPants.
	 * @return int
	 */
	public int getPants()
	{
		return _pants;
	}
	
	/**
	 * Method getBoots.
	 * @return int
	 */
	public int getBoots()
	{
		return _boots;
	}
	
	/**
	 * Method getCloak.
	 * @return int
	 */
	public int getCloak()
	{
		return _cloak;
	}
	
	/**
	 * Method getHair1.
	 * @return int
	 */
	public int getHair1()
	{
		return _hair1;
	}
	
	/**
	 * Method getHair2.
	 * @return int
	 */
	public int getHair2()
	{
		return _hair2;
	}
}
