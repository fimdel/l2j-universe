package l2p.gameserver.stats;

import l2p.gameserver.model.Creature;
import l2p.gameserver.model.Skill;
import l2p.gameserver.model.items.ItemInstance;

/**
 * An Env object is just a class to pass parameters to a calculator such as L2Player,
 * L2ItemInstance, Initial value.
 */
public final class Env {
    public Creature character;
    public Creature target;
    public ItemInstance item;
    public Skill skill;
    public double value;

    public Env() {
    }

    public Env(Creature cha, Creature tar, Skill sk) {
        character = cha;
        target = tar;
        skill = sk;
    }
}
