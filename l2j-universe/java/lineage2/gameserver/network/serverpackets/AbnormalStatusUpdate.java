package lineage2.gameserver.network.serverpackets;

import lineage2.gameserver.model.IconEffect;

import java.util.ArrayList;
import java.util.List;

/**
 * sample
 * <p/>
 * 0000: 85 02 00 10 04 00 00 01 00 4b 02 00 00 2c 04 00 .........K...,.. 0010: 00 01 00 58 02 00 00 ...X...
 * <p/>
 * <p/>
 * format h (dhd)
 * 
 * @version $Revision: 1.3.2.1.2.6 $ $Date: 2005/04/05 19:41:08 $
 */
public class AbnormalStatusUpdate extends L2GameServerPacket implements IconEffectPacket
{
	public static final int INFINITIVE_EFFECT = -1;
	private List<IconEffect> _effects;

	public AbnormalStatusUpdate()
	{
		_effects = new ArrayList<IconEffect>();
	}

	@Override
	protected final void writeImpl()
	{
		writeC(0x85);

		writeH(_effects.size());

		for (IconEffect temp : _effects)
		{
			writeD(temp.getSkillId());
			writeH(temp.getLevel());
			writeD(temp.getDuration());
		}
	}

	@Override
	public void addIconEffect(int skillId, int level, int duration, int obj)
	{
		_effects.add(new IconEffect(skillId, level, duration, obj));
	}
}