package lineage2.gameserver.network.serverpackets;

import lineage2.gameserver.data.xml.holder.PlayerTemplateHolder;
import lineage2.gameserver.model.base.ClassId;
import lineage2.gameserver.model.base.ClassLevel;
import lineage2.gameserver.model.base.Sex;
import lineage2.gameserver.templates.player.PlayerTemplate;

import java.util.ArrayList;
import java.util.List;

public class NewCharacterSuccess extends L2GameServerPacket
{
	private List<ClassId> _chars = new ArrayList<ClassId>();

	public NewCharacterSuccess()
	{
		for (ClassId classId : ClassId.VALUES)
		{
			if (classId.isOfLevel(ClassLevel.NONE))
				_chars.add(classId);
		}
	}

	@Override
	protected final void writeImpl()
	{
		writeC(0x0d);
		writeD(_chars.size());

		for (ClassId temp : _chars)
		{
			PlayerTemplate template = PlayerTemplateHolder.getInstance().getPlayerTemplate(temp.getRace(), temp, Sex.MALE);
			writeD(temp.getRace().ordinal());
			writeD(temp.getId());
			writeD(0x46);
			writeD(template.getBaseAttr().getSTR());
			writeD(0x0a);
			writeD(0x46);
			writeD(template.getBaseAttr().getDEX());
			writeD(0x0a);
			writeD(0x46);
			writeD(template.getBaseAttr().getCON());
			writeD(0x0a);
			writeD(0x46);
			writeD(template.getBaseAttr().getINT());
			writeD(0x0a);
			writeD(0x46);
			writeD(template.getBaseAttr().getWIT());
			writeD(0x0a);
			writeD(0x46);
			writeD(template.getBaseAttr().getMEN());
			writeD(0x0a);
		}
	}
}