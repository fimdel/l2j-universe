package lineage2.gameserver.network.serverpackets;

import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.WinnerStatueInstance;

/**
 * Пример дампа: 0000: 92 2c 05 10 58 77 bb 0f 00 00 00 00 00 00 00 00 .,..Xw.......... 0010: 54 ff ff b0 42 fe ff 14 ff ff ff 00 00 00 00 00
 * T...B........... 0020: 00 00 00 00 00 f0 3f 00 00 00 00 00 00 f0 3f 00 ......?.......?. 0030: 00 00 00 00 00 3e 40 00 00 00 00 00 00 3e 40 00
 * .....>@......>@. 0040: 00 00 00 00 00 00 00 04 00 00 00 00 00 00 00 ...............
 */
public class ServerObjectInfo extends L2GameServerPacket
{
	private final int idTemplate;
	private final String name;
	private final boolean isAttackable;
	private final int x;
	private final int y;
	private final int z;
	private final int heading;
	private final double collisionRadius;
	private final double collisionHeight;
	private final int categoryId;
	private final int classId;
	private final int raceId;
	private final int sex;
	private final int hairstyle;
	private final int haircolor;
	private final int face;
	private final int necklace;
	private final int head;
	private final int rhand;
	private final int objectId;
	private final int lhand;
	private final int gloves;
	private final int chest;
	private final int pants;
	private final int boots;
	private final int cloak;
	private final int hair1;
	private final int hair2;

	public ServerObjectInfo(WinnerStatueInstance statue, Creature actor)
	{
		objectId = statue.getObjectId();
		idTemplate = 1000000;
		name = statue.getTemplate().getName();
		isAttackable = statue.isAttackable(actor);
		x = statue.getX();
		y = statue.getY();
		z = statue.getZ();
		heading = statue.getHeading();
		collisionRadius = statue.getColRadius();
		collisionHeight = statue.getColHeight();
		categoryId = statue.getTemplate().getCategoryType().getClientId();
		classId = statue.getTemplate().getClassId();
		raceId = statue.getTemplate().getRaceId();
		sex = statue.getTemplate().getSex();
		hairstyle = statue.getTemplate().getHairStyle();
		haircolor = statue.getTemplate().getHairColor();
		face = statue.getTemplate().getFace();
		necklace = statue.getTemplate().getNecklace();
		head = statue.getTemplate().getHead();
		rhand = statue.getTemplate().getRhand();
		lhand = statue.getTemplate().getLhand();
		gloves = statue.getTemplate().getGloves();
		chest = statue.getTemplate().getChest();
		pants = statue.getTemplate().getPants();
		boots = statue.getTemplate().getBoots();
		cloak = statue.getTemplate().getCloak();
		hair1 = statue.getTemplate().getHair1();
		hair2 = statue.getTemplate().getHair2();
	}

	@Override
	protected void writeImpl()
	{
		writeC(0x92);
		writeD(objectId);
		writeD(idTemplate + 1000000);
		writeS(name); // name
		writeD(isAttackable ? 1 : 0);
		writeD(x);
		writeD(y);
		writeD(z);
		writeD(heading);
		writeF(1.0); // movement multiplier
		writeF(1.0); // attack speed multiplier
		writeF(collisionRadius);
		writeF(collisionHeight);
		writeD(0); // Current HP
		writeD(0); // Max HP
		writeD(7); // Color
		writeD(0x00);

		writeD(categoryId);
		writeD(0x00);
		writeD(0x00); // Social ID
		writeD(0x00); // Social frame

		writeD(classId);
		writeD(raceId);
		writeD(sex);

		writeD(hairstyle);
		writeD(haircolor);
		writeD(face);

		writeD(necklace);
		writeD(head);
		writeD(rhand);
		writeD(lhand);
		writeD(gloves);
		writeD(chest);
		writeD(pants);
		writeD(boots);
		writeD(cloak);
		writeD(hair1);
		writeD(hair2);
	}
}
