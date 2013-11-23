package l2p.gameserver.skills;

import l2p.gameserver.model.Skill;

import java.util.AbstractMap;

/**
 * @author VISTALL
 * @date 0:15/03.06.2011
 */
public class SkillEntry extends AbstractMap.SimpleImmutableEntry<SkillEntryType, Skill> {
    private static final long serialVersionUID = -4116839388602869434L;
    private boolean _disabled;

    public SkillEntry(SkillEntryType key, Skill value) {
        super(key, value);
    }

    public boolean isDisabled() {
        return _disabled;
    }

    public void setDisabled(boolean disabled) {
        _disabled = disabled;
    }
}
