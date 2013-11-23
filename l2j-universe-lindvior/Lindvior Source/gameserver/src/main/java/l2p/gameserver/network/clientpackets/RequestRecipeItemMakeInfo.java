package l2p.gameserver.network.clientpackets;

import l2p.gameserver.data.xml.holder.RecipeHolder;
import l2p.gameserver.model.Player;
import l2p.gameserver.network.serverpackets.RecipeItemMakeInfo;
import l2p.gameserver.templates.item.RecipeTemplate;

public class RequestRecipeItemMakeInfo extends L2GameClientPacket {
    private int _id;

    /**
     * packet type id 0xB7
     * format:		cd
     */
    @Override
    protected void readImpl() {
        _id = readD();
    }

    @Override
    protected void runImpl() {
        Player activeChar = getClient().getActiveChar();
        if (activeChar == null)
            return;

        RecipeTemplate recipeList = RecipeHolder.getInstance().getRecipeByRecipeId(_id);
        if (recipeList == null) {
            activeChar.sendActionFailed();
            return;
        }

        sendPacket(new RecipeItemMakeInfo(activeChar, recipeList, 0xffffffff));
    }
}