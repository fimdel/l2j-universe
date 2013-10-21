package lineage2.gameserver.templates;

import lineage2.gameserver.model.worldstatistics.CategoryType;

public class StatuesSpawnTemplate
{
	private final CategoryType categoryType;
	private int classId;
	private int raceId;
	private int sex;
	private int hairStyle;
	private int hairColor;
	private int face;
	private int necklace;
	private int head;
	private int rhand;
	private int lhand;
	private int gloves;
	private int chest;
	private int pants;
	private int boots;
	private int cloak;
	private int hair1;
	private int hair2;
	private String name;

	public StatuesSpawnTemplate(CategoryType categoryType)
	{
		this.categoryType = categoryType;
	}

	public int getClassId()
	{
		return classId;
	}

	public void setClassId(int classId)
	{
		this.classId = classId;
	}

	public int getRaceId()
	{
		return raceId;
	}

	public void setRaceId(int raceId)
	{
		this.raceId = raceId;
	}

	public int getSex()
	{
		return sex;
	}

	public void setSex(int sex)
	{
		this.sex = sex;
	}

	public int getHairStyle()
	{
		return hairStyle;
	}

	public void setHairStyle(int hairStyle)
	{
		this.hairStyle = hairStyle;
	}

	public int getHairColor()
	{
		return hairColor;
	}

	public void setHairColor(int hairColor)
	{
		this.hairColor = hairColor;
	}

	public int getFace()
	{
		return face;
	}

	public void setFace(int face)
	{
		this.face = face;
	}

	public int getNecklace()
	{
		return necklace;
	}

	public void setNecklace(int necklace)
	{
		this.necklace = necklace;
	}

	public int getHead()
	{
		return head;
	}

	public void setHead(int head)
	{
		this.head = head;
	}

	public int getRhand()
	{
		return rhand;
	}

	public int getLhand()
	{
		return lhand;
	}

	public int getGloves()
	{
		return gloves;
	}

	public void setGloves(int gloves)
	{
		this.gloves = gloves;
	}

	public int getChest()
	{
		return chest;
	}

	public void setChest(int chest)
	{
		this.chest = chest;
	}

	public int getPants()
	{
		return pants;
	}

	public void setPants(int pants)
	{
		this.pants = pants;
	}

	public int getBoots()
	{
		return boots;
	}

	public void setBoots(int boots)
	{
		this.boots = boots;
	}

	public int getCloak()
	{
		return cloak;
	}

	public void setCloak(int cloak)
	{
		this.cloak = cloak;
	}

	public int getHair1()
	{
		return hair1;
	}

	public void setHair1(int hair1)
	{
		this.hair1 = hair1;
	}

	public int getHair2()
	{
		return hair2;
	}

	public void setHair2(int hair2)
	{
		this.hair2 = hair2;

	}

	public void setRHand(int RHand)
	{
		this.rhand = RHand;
	}

	public void setLHand(int LHand)
	{
		this.lhand = LHand;
	}

	public CategoryType getCategoryType()
	{
		return categoryType;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}
}
