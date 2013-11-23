package l2p.gameserver.model;

/**
 * @author ALF
 * @data 07.02.2012 Класс для иконки эффекта
 */
public class IconEffect {
    private final int _skillId;
    private final int _level;
    private final int _duration;
    private final int _obj;

    public IconEffect(int skillId, int level, int duration, int obj) {
        _skillId = skillId;
        _level = level;
        _duration = duration;
        _obj = obj;
    }

    public int getSkillId() {
        return _skillId;
    }

    public int getLevel() {
        return _level;
    }

    public int getDuration() {
        return _duration;
    }

    public int getObj() {
        return _obj;
    }
}
