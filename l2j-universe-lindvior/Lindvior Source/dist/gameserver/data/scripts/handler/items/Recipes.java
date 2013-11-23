package handler.items;

import l2p.gameserver.cache.Msg;
import l2p.gameserver.data.xml.holder.RecipeHolder;
import l2p.gameserver.model.Playable;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.Skill;
import l2p.gameserver.model.items.ItemInstance;
import l2p.gameserver.network.serverpackets.RecipeBookItemList;
import l2p.gameserver.network.serverpackets.SystemMessage;
import l2p.gameserver.network.serverpackets.components.SystemMsg;
import l2p.gameserver.templates.item.RecipeTemplate;

import java.util.Collection;

public class Recipes extends ScriptItemHandler {
    private static int[] _itemIds = null;

    public Recipes() {
        Collection<RecipeTemplate> rc = RecipeHolder.getInstance().getRecipes();
        _itemIds = new int[rc.size()];
        int i = 0;
        for (RecipeTemplate r : rc)
            _itemIds[i++] = r.getItemId();
    }

    @Override
    public boolean useItem(Playable playable, ItemInstance item, boolean ctrl) {
        if (playable == null || !playable.isPlayer())
            return false;
        Player player = (Player) playable;

        RecipeTemplate rp = RecipeHolder.getInstance().getRecipeByRecipeItem(item.getItemId());
        if (rp.isCommon()) {
            if (player.getDwarvenRecipeLimit() > 0) {
                if (player.getDwarvenRecipeBook().size() >= player.getDwarvenRecipeLimit()) {
                    player.sendPacket(Msg.NO_FURTHER_RECIPES_MAY_BE_REGISTERED);
                    return false;
                }

                if (rp.getLevel() > player.getSkillLevel(Skill.SKILL_CRAFTING)) {
                    player.sendPacket(Msg.CREATE_ITEM_LEVEL_IS_TOO_LOW_TO_REGISTER_THIS_RECIPE);
                    return false;
                }
                if (player.hasRecipe(rp)) {
                    player.sendPacket(Msg.THAT_RECIPE_IS_ALREADY_REGISTERED);
                    return false;
                }
                if (!player.getInventory().destroyItem(item, 1L)) {
                    player.sendPacket(SystemMsg.INCORRECT_ITEM_COUNT);
                    return false;
                }
                // add recipe to recipebook
                player.registerRecipe(rp, true);
                player.sendPacket(new SystemMessage(SystemMessage.S1_HAS_BEEN_ADDED).addItemName(item.getItemId()));
                player.sendPacket(new RecipeBookItemList(player, true));
                return true;
            } else
                player.sendPacket(Msg.YOU_ARE_NOT_AUTHORIZED_TO_REGISTER_A_RECIPE);
        } else if (player.getCommonRecipeLimit() > 0) {
            if (player.getCommonRecipeBook().size() >= player.getCommonRecipeLimit()) {
                player.sendPacket(Msg.NO_FURTHER_RECIPES_MAY_BE_REGISTERED);
                return false;
            }
            if (player.hasRecipe(rp)) {
                player.sendPacket(Msg.THAT_RECIPE_IS_ALREADY_REGISTERED);
                return false;
            }
            if (!player.getInventory().destroyItem(item, 1L)) {
                player.sendPacket(SystemMsg.INCORRECT_ITEM_COUNT);
                return false;
            }
            player.registerRecipe(rp, true);
            player.sendPacket(new SystemMessage(SystemMessage.S1_HAS_BEEN_ADDED).addItemName(item.getItemId()));
            player.sendPacket(new RecipeBookItemList(player, false));
            return true;
        } else
            player.sendPacket(Msg.YOU_ARE_NOT_AUTHORIZED_TO_REGISTER_A_RECIPE);
        return false;
    }

    @Override
    public int[] getItemIds() {
        return _itemIds;
    }
}