package l2p.gameserver.network.serverpackets;

import l2p.gameserver.model.Player;

import java.util.ArrayList;
import java.util.List;

public class ExOlympiadSpelledInfo extends L2GameServerPacket {
    // chdd(dhd)
    private int char_obj_id = 0;
    private List<Effect> _effects;

    public ExOlympiadSpelledInfo() {
        _effects = new ArrayList<Effect>();
    }

    public void addEffect(int skillId, int level, int duration) {
        _effects.add(new Effect(skillId, level, duration));
    }

    public void addSpellRecivedPlayer(Player cha) {
        if (cha != null) {
            char_obj_id = cha.getObjectId();
        }
    }

    @Override
    protected final void writeImpl() {
        writeEx(0x7C);
        writeD(char_obj_id);
        writeD(_effects.size());

        for (Effect temp : _effects) {
            writeD(temp.skillId);
            writeH(temp.level);
            writeD(temp.duration);
        }
    }

    class Effect {
        int duration;
        int level;
        int skillId;

        public Effect(int skillId, int level, int duration) {
            this.skillId = skillId;
            this.level = level;
            this.duration = duration;
        }
    }
}
