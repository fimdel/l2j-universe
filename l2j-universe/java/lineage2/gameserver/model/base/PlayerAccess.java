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
package lineage2.gameserver.model.base;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class PlayerAccess
{
	/**
	 * Field PlayerID.
	 */
	public int PlayerID;
	/**
	 * Field IsGM.
	 */
	public boolean IsGM = false;
	/**
	 * Field CanUseGMCommand.
	 */
	public boolean CanUseGMCommand = false;
	/**
	 * Field CanAnnounce.
	 */
	public boolean CanAnnounce = false;
	/**
	 * Field CanBanChat.
	 */
	public boolean CanBanChat = false;
	/**
	 * Field CanUnBanChat.
	 */
	public boolean CanUnBanChat = false;
	/**
	 * Field CanChatPenalty.
	 */
	public boolean CanChatPenalty = false;
	/**
	 * Field BanChatDelay.
	 */
	public int BanChatDelay = -1;
	/**
	 * Field BanChatMaxValue.
	 */
	public int BanChatMaxValue = -1;
	/**
	 * Field BanChatCountPerDay.
	 */
	public int BanChatCountPerDay = -1;
	/**
	 * Field BanChatBonusId.
	 */
	public int BanChatBonusId = -1;
	/**
	 * Field BanChatBonusCount.
	 */
	public int BanChatBonusCount = -1;
	/**
	 * Field CanSetCarma.
	 */
	public boolean CanSetCarma = false;
	/**
	 * Field CanCharBan.
	 */
	public boolean CanCharBan = false;
	/**
	 * Field CanCharUnBan.
	 */
	public boolean CanCharUnBan = false;
	/**
	 * Field CanBan.
	 */
	public boolean CanBan = false;
	/**
	 * Field CanUnBan.
	 */
	public boolean CanUnBan = false;
	/**
	 * Field CanTradeBanUnban.
	 */
	public boolean CanTradeBanUnban = false;
	/**
	 * Field CanUseBanPanel.
	 */
	public boolean CanUseBanPanel = false;
	/**
	 * Field UseGMShop.
	 */
	public boolean UseGMShop = false;
	/**
	 * Field CanDelete.
	 */
	public boolean CanDelete = false;
	/**
	 * Field CanKick.
	 */
	public boolean CanKick = false;
	/**
	 * Field Menu.
	 */
	public boolean Menu = false;
	/**
	 * Field GodMode.
	 */
	public boolean GodMode = false;
	/**
	 * Field CanEditChar.
	 */
	public boolean CanEditChar = false;
	/**
	 * Field CanEditCharAll.
	 */
	public boolean CanEditCharAll = false;
	/**
	 * Field CanEditPledge.
	 */
	public boolean CanEditPledge = false;
	/**
	 * Field CanViewChar.
	 */
	public boolean CanViewChar = false;
	/**
	 * Field CanEditNPC.
	 */
	public boolean CanEditNPC = false;
	/**
	 * Field CanViewNPC.
	 */
	public boolean CanViewNPC = false;
	/**
	 * Field CanTeleport.
	 */
	public boolean CanTeleport = false;
	/**
	 * Field CanRestart.
	 */
	public boolean CanRestart = false;
	/**
	 * Field MonsterRace.
	 */
	public boolean MonsterRace = false;
	/**
	 * Field Rider.
	 */
	public boolean Rider = false;
	/**
	 * Field FastUnstuck.
	 */
	public boolean FastUnstuck = false;
	/**
	 * Field ResurectFixed.
	 */
	public boolean ResurectFixed = false;
	/**
	 * Field Door.
	 */
	public boolean Door = false;
	/**
	 * Field Res.
	 */
	public boolean Res = false;
	/**
	 * Field PeaceAttack.
	 */
	public boolean PeaceAttack = false;
	/**
	 * Field Heal.
	 */
	public boolean Heal = false;
	/**
	 * Field Unblock.
	 */
	public boolean Unblock = false;
	/**
	 * Field UseInventory.
	 */
	public boolean UseInventory = true;
	/**
	 * Field UseTrade.
	 */
	public boolean UseTrade = true;
	/**
	 * Field CanAttack.
	 */
	public boolean CanAttack = true;
	/**
	 * Field CanEvaluate.
	 */
	public boolean CanEvaluate = true;
	/**
	 * Field CanJoinParty.
	 */
	public boolean CanJoinParty = true;
	/**
	 * Field CanJoinClan.
	 */
	public boolean CanJoinClan = true;
	/**
	 * Field UseWarehouse.
	 */
	public boolean UseWarehouse = true;
	/**
	 * Field UseShop.
	 */
	public boolean UseShop = true;
	/**
	 * Field UseTeleport.
	 */
	public boolean UseTeleport = true;
	/**
	 * Field BlockInventory.
	 */
	public boolean BlockInventory = false;
	/**
	 * Field CanChangeClass.
	 */
	public boolean CanChangeClass = false;
	/**
	 * Field CanGmEdit.
	 */
	public boolean CanGmEdit = false;
	/**
	 * Field IsEventGm.
	 */
	public boolean IsEventGm = false;
	/**
	 * Field CanReload.
	 */
	public boolean CanReload = false;
	/**
	 * Field CanRename.
	 */
	public boolean CanRename = false;
	/**
	 * Field CanJail.
	 */
	public boolean CanJail = false;
	/**
	 * Field CanPolymorph.
	 */
	public boolean CanPolymorph = false;
	
	/**
	 * Constructor for PlayerAccess.
	 */
	public PlayerAccess()
	{
	}
}
