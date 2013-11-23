/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.serverpackets;

import l2p.gameserver.model.Creature;
import l2p.gameserver.model.Effect;
import l2p.gameserver.model.IconEffect;
import l2p.gameserver.utils.EffectsComparator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author ALF
 * @data 07.02.2012
 */
public class ExAbnormalStatusUpdateFromTargetPacket extends L2GameServerPacket implements IconEffectPacket {
    private int objId;
    private List<IconEffect> _effects;

    public ExAbnormalStatusUpdateFromTargetPacket(Creature target) {
        _effects = new ArrayList<IconEffect>();
        objId = target.getObjectId();

        Effect[] effects = target.getEffectList().getAllFirstEffects();
        Arrays.sort(effects, EffectsComparator.getInstance());

        for (Effect effect : effects)
            if (effect != null && effect.isInUse())
                effect.addIcon(this);
    }

    @Override
    protected void writeImpl() {
        writeEx(0xE6);
        writeD(objId);
        if (_effects != null) {
            writeH(_effects.size());
            for (IconEffect e : _effects) {
                writeD(e.getSkillId());
                writeH(e.getLevel());
                writeD(0x00);
                writeD(e.getDuration());
                writeD(e.getObj()); // objId того кто наложил дебаф, нужно для определения ты наложил или нет.
            }
        }
    }

    @Override
    public void addIconEffect(int skillId, int level, int duration, int obj) {
        _effects.add(new IconEffect(skillId, level, duration, obj));
    }

}
