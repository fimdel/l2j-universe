/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package lineage2.gameserver.network.clientpackets;

import lineage2.gameserver.data.xml.holder.RecipeHolder;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.network.serverpackets.RecipeBookItemList;
import lineage2.gameserver.templates.item.RecipeTemplate;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestRecipeItemDelete extends L2GameClientPacket
{
	/**
	 * Field _recipeId.
	 */
	private int _recipeId;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_recipeId = readD();
	}
	
	/**
	 * Method runImpl.
	 */
	@Override
	protected void runImpl()
	{
		Player activeChar = getClient().getActiveChar();
		if (activeChar == null)
		{
			return;
		}
		if (activeChar.getPrivateStoreType() == Player.STORE_PRIVATE_MANUFACTURE)
		{
			activeChar.sendActionFailed();
			return;
		}
		RecipeTemplate rp = RecipeHolder.getInstance().getRecipeByRecipeId(_recipeId);
		if (rp == null)
		{
			activeChar.sendActionFailed();
			return;
		}
		activeChar.unregisterRecipe(_recipeId);
		activeChar.sendPacket(new RecipeBookItemList(activeChar, rp.isDwarven()));
	}
}
