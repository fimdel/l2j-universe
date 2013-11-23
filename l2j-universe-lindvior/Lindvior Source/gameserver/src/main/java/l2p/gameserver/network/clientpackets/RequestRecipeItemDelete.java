package l2p.gameserver.network.clientpackets;

import l2p.gameserver.data.xml.holder.RecipeHolder;
import l2p.gameserver.model.Player;
import l2p.gameserver.network.serverpackets.RecipeBookItemList;
import l2p.gameserver.templates.item.RecipeTemplate;

public class RequestRecipeItemDelete extends L2GameClientPacket {
    private int _recipeId;

    @Override
    protected void readImpl() {
        _recipeId = readD();
    }

    @Override
    protected void runImpl() {
        Player activeChar = getClient().getActiveChar();
        if (activeChar == null)
            return;

        if (activeChar.getPrivateStoreType() == Player.STORE_PRIVATE_MANUFACTURE) {
            activeChar.sendActionFailed();
            return;
        }

        RecipeTemplate rp = RecipeHolder.getInstance().getRecipeByRecipeId(_recipeId);
        if (rp == null) {
            activeChar.sendActionFailed();
            return;
        }

        activeChar.unregisterRecipe(_recipeId);
        activeChar.sendPacket(new RecipeBookItemList(activeChar, rp.isCommon()));
    }
}