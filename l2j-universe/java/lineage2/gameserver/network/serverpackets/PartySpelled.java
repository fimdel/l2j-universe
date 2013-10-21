package lineage2.gameserver.network.serverpackets;

import lineage2.gameserver.model.IconEffect;
import lineage2.gameserver.model.Playable;
import lineage2.gameserver.utils.EffectsComparator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PartySpelled extends L2GameServerPacket implements IconEffectPacket
{
	private int _type;
	private int _objId;
	private List<IconEffect> _effects;

	public PartySpelled(Playable activeChar, boolean full)
	{
		_objId = activeChar.getObjectId();
		_type = activeChar.isPet() ? 1 : activeChar.isServitor() ? 2 : 0;
		_effects = new ArrayList<IconEffect>();
		if (full)
		{
			lineage2.gameserver.model.Effect[] effects = activeChar.getEffectList().getAllFirstEffects();
			Arrays.sort(effects, EffectsComparator.getInstance());
			for (lineage2.gameserver.model.Effect effect : effects)
			{
				if (effect != null && effect.isInUse())
				{
					effect.addIcon(this);
				}
			}
		}
	}

	@Override
	protected final void writeImpl()
	{
		writeC(0xf4);
		writeD(_type);
		writeD(_objId);
		writeD(_effects.size());
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