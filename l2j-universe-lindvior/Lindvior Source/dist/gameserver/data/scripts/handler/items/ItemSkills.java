package handler.items;

import gnu.trove.TIntHashSet;
import l2p.gameserver.data.xml.holder.ItemHolder;
import l2p.gameserver.model.Creature;
import l2p.gameserver.model.Playable;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.Skill;
import l2p.gameserver.model.items.ItemInstance;
import l2p.gameserver.templates.item.ItemTemplate;

/**
 * @author VISTALL
 * @date 7:34/17.03.2011
 */
public class ItemSkills extends ScriptItemHandler {
    private int[] _itemIds;

    public ItemSkills() {
        TIntHashSet set = new TIntHashSet();
        for (ItemTemplate template : ItemHolder.getInstance().getAllTemplates()) {
            if (template == null)
                continue;

            for (Skill skill : template.getAttachedSkills())
                if (skill.isHandler())
                    set.add(template.getItemId());
        }
        _itemIds = set.toArray();
    }

    @Override
    public boolean useItem(Playable playable, ItemInstance item, boolean ctrl) {
        Player player;
        if (playable.isPlayer())
            player = (Player) playable;
        else if (playable.isPet())
            player = playable.getPlayer();
        else
            return false;

        Skill[] skills = item.getTemplate().getAttachedSkills();

        for (int i = 0; i < skills.length; i++) {
            Skill skill = skills[i];
            Creature aimingTarget = skill.getAimingTarget(player, player.getTarget());
            if (skill.checkCondition(player, aimingTarget, ctrl, false, true))
                player.getAI().Cast(skill, aimingTarget, ctrl, false);
            else if (i == 0) //FIXME [VISTALL] всегда первый скил идет вместо конда?
                return false;
        }
        return true;
    }

    @Override
    public int[] getItemIds() {
        return _itemIds;
    }
}
