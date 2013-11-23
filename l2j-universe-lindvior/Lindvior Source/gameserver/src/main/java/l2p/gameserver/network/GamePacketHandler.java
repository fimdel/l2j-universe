/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network;

import l2p.commons.net.nio.impl.*;
import l2p.gameserver.Config;
import l2p.gameserver.ThreadPoolManager;
import l2p.gameserver.network.clientpackets.*;
import l2p.gameserver.network.clientpackets.Beautyshop.NotifyExitBeautyshop;
import l2p.gameserver.network.clientpackets.Beautyshop.RequestRegistBeauty;
import l2p.gameserver.network.clientpackets.Beautyshop.RequestResetBeauty;
import l2p.gameserver.network.clientpackets.Beautyshop.RequestShowBeautyList;
import l2p.gameserver.network.clientpackets.CuriousHouse.*;
import l2p.gameserver.network.clientpackets.PledgeRecruit.*;
import l2p.gameserver.network.clientpackets.Test_packet.d0de;
import l2p.gameserver.network.clientpackets.shapeShifting.RequestExTryToPutShapeShiftingTargetItem;
import l2p.gameserver.network.clientpackets.shapeShifting.RequestShapeShiftingItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

public final class GamePacketHandler implements IPacketHandler<GameClient>, IClientFactory<GameClient>, IMMOExecutor<GameClient> {
    private static final Logger _log = LoggerFactory.getLogger(GamePacketHandler.class);

    @Override
    public ReceivablePacket<GameClient> handlePacket(ByteBuffer buf, GameClient client) {
        int id = buf.get() & 0xFF;

        ReceivablePacket<GameClient> msg = null;

        try {
            int id2 = 0;
            switch (client.getState()) {
                case CONNECTED:
                    switch (id) {
                        case 0x00:
                            msg = new RequestStatus();
                            break;
                        case 0x0e:
                            msg = new ProtocolVersion();
                            break;
                        case 0x2b:
                            msg = new AuthLogin();
                            break;
                        case 0xcb:
                            msg = new ReplyGameGuardQuery();
                            break;
                        default:
                            client.onUnknownPacket("CONNECTED" + " " + "id", id);
                            _log.warn("Unknown packet on state: CONNECTED, id: " + Integer.toHexString(id) + " from " + client.getConnection().getSocket().getInetAddress().getHostAddress());
                            break;
                    }
                    break;
                case AUTHED:
                    switch (id) {
                        case 0x00:
                            msg = new Logout();
                            break;
                        case 0x0c:
                            msg = new CharacterCreate(); //RequestCharacterCreate();
                            break;
                        case 0x0d:
                            msg = new CharacterDelete(); //RequestCharacterDelete();
                            break;
                        case 0x12:
                            msg = new CharacterSelected(); //CharacterSelect();
                            break;
                        case 0x13:
                            msg = new NewCharacter(); //RequestNewCharacter();
                            break;
                        case 0x7b:
                            msg = new CharacterRestore(); //RequestCharacterRestore();
                            break;
                        case 0xcb:
                            msg = new ReplyGameGuardQuery();
                            break;
                        case 0xd0:
                            int id3 = buf.getShort() & 0xffff;
                            switch (id3) {
                                case 0x33:
                                    msg = new GotoLobby();
                                    break;
                                case 0xA9:
                                    //  msg = new RequestWebSessionID();
                                    break;
                                case 0xAA:
                                    msg = new RequestEx2ndPasswordCheck();
                                    break;
                                case 0xAB:
                                    msg = new RequestEx2ndPasswordVerify();
                                    break;
                                case 0xAC:
                                    msg = new RequestEx2ndPasswordReq();
                                    break;
                                case 0xAD:
                                    msg = new RequestCharacterNameCreatable();
                                    break;
                                default:
                                    client.onUnknownPacket("AUTHED" + " " + "id", id3);
                                    _log.warn("Unknown packet on state: CONNECTED, id: " + Integer.toHexString(id) + " from " + client.getConnection().getSocket().getInetAddress().getHostAddress());

                                    break;
                            }
                            break;
                        default:
                            client.onUnknownPacket("AUTHED" + " " + "id", id);
                            _log.warn("Unknown packet on state: CONNECTED, id: " + Integer.toHexString(id) + " from " + client.getConnection().getSocket().getInetAddress().getHostAddress());
                            break;
                    }
                    break;
                case IN_GAME:
                    switch (id) {
                        case 0x00:
                            msg = new Logout();
                            break;
                        case 0x01:
                            msg = new AttackRequest();  // LINDVIOR
                            break;
                        case 0x02:
                            // msg = new ?();
                            break;
                        case 0x03:
                            msg = new RequestStartPledgeWar();
                            break;
                        case 0x04:
                            // msg = new ?();
                            break;
                        case 0x05:
                            msg = new RequestStopPledgeWar();
                            break;
                        case 0x06:
                            //  msg = RequestSCCheck(); // ? Format: cdx
                            _log.info("RequestSCCheck");
                            break;
                        case 0x07:
                            msg = new ReplyGameGuardQuery();
                            // здесь совсем другой пакет
                            // ResponseAuthGameGuard[cddddd] (c) Drin
                            break;
                        case 0x08:
                            // msg = new ?();
                            break;
                        case 0x09:
                            msg = new RequestSetPledgeCrest();
                            break;
                        case 0x0a:
                            // msg = new ?();
                            break;
                        case 0x0b:
                            msg = new RequestGiveNickName();
                            break;
                        case 0x0c:
                            // wtf???
                            break;
                        case 0x0d:
                            // wtf???
                            break;
                        case 0x0e:
                            //msg = new RequestPIAgreementCheck(); (cd)
                            _log.info("RequestPIAgreementCheck");
                            break;
                        case 0x0f:
                            msg = new MoveBackwardToLocation();
                            break;
                        case 0x10:
                            // msg = new Say(); //Format: cS // старый ?
                            _log.info("Say");
                            break;
                        case 0x11:
                            msg = new EnterWorld();
                            break;
                        case 0x12:
                            // wtf???
                            _log.info("0x12");
                            break;
                        case 0x14:
                            msg = new RequestItemList();
                            break;
                        case 0x15:
                            // msg = new RequestEquipItem(); // старый?
                            _log.info("RequestEquipItem");
                            // Format: cdd server id = %d Slot = %d
                            break;
                        case 0x16:
                            msg = new RequestUnEquipItem();
                            break;
                        case 0x17:
                            msg = new RequestDropItem();
                            break;
                        case 0x18:
                            // msg = new ?();
                            _log.info("0x18");
                            break;
                        case 0x19:
                            msg = new UseItem();
                            break;
                        case 0x1a:
                            msg = new TradeRequest();
                            break;
                        case 0x1b:
                            msg = new AddTradeItem();
                            break;
                        case 0x1c:
                            msg = new TradeDone();
                            break;
                        case 0x1d:
                            // msg = new ?();
                            _log.info("0x1d");
                            break;
                        case 0x1e:
                            // msg = new ?();
                            _log.info("0x1e");
                            break;
                        case 0x1f:
                            msg = new Action();
                            break;
                        case 0x20:
                            // msg = new ?();
                            _log.info("0x20");
                            break;
                        case 0x21:
                            // msg = new ?();
                            _log.info("0x21");
                            break;
                        case 0x22:
                            msg = new RequestLinkHtml();
                            break;
                        case 0x23:
                            msg = new RequestBypassToServer();
                            break;
                        case 0x24:
                            msg = new RequestBBSwrite();
                            break;
                        case 0x25:
                            msg = new RequestCreatePledge();
                            break;
                        case 0x26:
                            msg = new RequestJoinPledge();
                            break;
                        case 0x27:
                            msg = new RequestAnswerJoinPledge();
                            break;
                        case 0x28:
                            msg = new RequestWithdrawalPledge();
                            break;
                        case 0x29:
                            msg = new RequestOustPledgeMember();
                            break;
                        case 0x2a:
                            // msg = new ?();
                            _log.info("0x2a");
                            break;
                        case 0x2c:
                            msg = new RequestGetItemFromPet();
                            break;
                        case 0x2d:
                            // RequestDismissParty
                            _log.info("RequestDismissParty");
                            break;
                        case 0x2e:
                            msg = new RequestAllyInfo();
                            break;
                        case 0x2f:
                            msg = new RequestCrystallizeItem();
                            break;
                        case 0x30:
                            // RequestPrivateStoreManage, устарел
                            _log.info("RequestPrivateStoreManage");
                            break;
                        case 0x31:
                            msg = new SetPrivateStoreSellList();
                            break;
                        case 0x32:
                            // RequestPrivateStoreManageCancel, устарел
                            _log.info("RequestPrivateStoreManageCancel");
                            break;
                        case 0x33:
                            msg = new RequestTeleport();
                            break;
                        case 0x34:
                            // msg = new RequestSocialAction();
                            _log.info("RequestSocialAction");
                            break;
                        case 0x35:
                            // ChangeMoveType, устарел
                            _log.info("ChangeMoveType");
                            break;
                        case 0x36:
                            // ChangeWaitType, устарел
                            _log.info("ChangeWaitType");
                            break;
                        case 0x37:
                            msg = new RequestSellItem();
                            break;
                        case 0x38:
                            msg = new RequestMagicSkillList();
                            break;
                        case 0x39:
                            msg = new RequestMagicSkillUse();
                            break;
                        case 0x3a:
                            msg = new Appearing(); // Appering();
                            break;
                        case 0x3b:
                            if (Config.ALLOW_WAREHOUSE)
                                msg = new SendWareHouseDepositList();
                            break;
                        case 0x3c:
                            msg = new SendWareHouseWithDrawList();
                            break;
                        case 0x3d:
                            msg = new RequestShortCutReg();
                            break;
                        case 0x3e:
                            // msg = new RequestShortCutUse(); // Format: cddc ?
                            _log.info("RequestShortCutUse");
                            break;
                        case 0x3f:
                            msg = new RequestShortCutDel();
                            break;
                        case 0x40:
                            msg = new RequestBuyItem();
                            break;
                        case 0x41:
                            // msg = new RequestDismissPledge(); //Format: c ?
                            _log.info("RequestDismissPledge");
                            break;
                        case 0x42:
                            msg = new RequestJoinParty();
                            break;
                        case 0x43:
                            msg = new RequestAnswerJoinParty();
                            break;
                        case 0x44:
                            msg = new RequestWithDrawalParty();
                            break;
                        case 0x45:
                            msg = new RequestOustPartyMember();
                            break;
                        case 0x46:
                            msg = new RequestDismissParty();
                            break;
                        case 0x47:
                            msg = new CannotMoveAnymore();
                            break;
                        case 0x48:
                            msg = new RequestTargetCanceld();
                            break;
                        case 0x49:
                            msg = new Say2C();
                            break;
                        // -- maybe GM packet's
                        case 0x4a:
                            id2 = buf.get() & 0xff;
                            switch (id2) {
                                case 0x00:
                                    //	msg = new SendCharacterInfo(); // Format: S
                                    break;
                                case 0x01:
                                    //	msg = new SendSummonCmd(); // Format: S
                                    break;
                                case 0x02:
                                    //	msg = new SendServerStatus(); // Format: (noargs)
                                    break;
                                case 0x03:
                                    //	msg = new SendL2ParamSetting(); // Format: dd
                                    break;
                                default:
                                    client.onUnknownPacket("IN_GAME" + " " + "id", id2);
                                    break;
                            }
                            break;
                        case 0x4b:
                            // msg = new ?();
                            _log.info("0x4b");
                            break;
                        case 0x4c:
                            // msg = new ?();
                            _log.info("0x4c");
                            break;
                        case 0x4d:
                            msg = new RequestPledgeMemberList();
                            break;
                        case 0x4e:
                            // msg = new ?();
                            _log.info("0x4e");
                            break;
                        case 0x4f:
                            int id_2 = buf.getShort() & 0xFFFF;
                            switch (id_2) {
                                case 0:
                                    break;
                                case 1:
                                    break;
                                case 2:
                                    break;
                                case 3:
                                    break;
                                default:
                                    client.onUnknownPacket("INGAME id", id_2);
                            }
                            break;
                        case 0x50:
                            msg = new RequestSkillList(); // trigger
                            break;
                        case 0x51:
                            //	msg = new ?();
                            break;
                        case 0x52:
                            msg = new MoveWithDelta();
                            break;
                        case 0x53:
                            msg = new RequestGetOnVehicle();
                            break;
                        case 0x54:
                            msg = new RequestGetOffVehicle();
                            break;
                        case 0x55:
                            msg = new AnswerTradeRequest();
                            break;
                        case 0x56:
                            msg = new RequestActionUse();
                            break;
                        case 0x57:
                            msg = new RequestRestart();
                            break;
                        case 0x58:
                            msg = new RequestSiegeInfo();
                            break;
                        case 0x59:
                            msg = new ValidatePosition();
                            break;
                        case 0x5a:
                            msg = new RequestSEKCustom();
                            break;
                        case 0x5b:
                            msg = new StartRotatingC();
                            break;
                        case 0x5c:
                            msg = new FinishRotatingC();
                            break;
                        case 0x5d:
                            // msg = new ?();
                            _log.info("0x5d");
                            break;
                        case 0x5e:
                            msg = new RequestShowBoard();
                            break;
                        case 0x5f:
                            msg = new RequestEnchantItem();
                            break;
                        case 0x60:
                            msg = new RequestDestroyItem();
                            break;
                        case 0x61:
                            // msg = new ?();
                            _log.info("0x61");
                            break;
                        case 0x62:
                            msg = new RequestQuestList();
                            break;
                        case 0x63:
                            msg = new RequestQuestAbort(); // RequestDestroyQuest();
                            break;
                        case 0x64:
                            // msg = new ?();
                            _log.info("0x64");
                            break;
                        case 0x65:
                            msg = new RequestPledgeInfo(); // Follow EnterWorld
                            break;
                        case 0x66:
                            msg = new RequestPledgeExtendedInfo();
                            break;
                        case 0x67:
                            msg = new RequestPledgeCrest();
                            break;
                        case 0x68:
                            // msg = new ?();
                            _log.info("0x68");
                            break;
                        case 0x69:
                            // msg = new ?();
                            _log.info("0x69");
                            break;
                        case 0x6a:
                            // msg = new RequestFriendInfoList();
                            _log.info("RequestFriendInfoList");
                            break;
                        case 0x6b:
                            msg = new RequestSendL2FriendSay();
                            break;
                        case 0x6c:
                            msg = new RequestShowMiniMap();
                            break;
                        case 0x6d:
                            msg = new RequestSendMsnChatLog();
                            break;
                        case 0x6e:
                            msg = new RequestReload(); // record video
                            break;
                        case 0x6f:
                            msg = new RequestHennaEquip();
                            break;
                        case 0x70:
                            msg = new RequestHennaUnequipList();
                            break;
                        case 0x71:
                            msg = new RequestHennaUnequipInfo();
                            break;
                        case 0x72:
                            msg = new RequestHennaUnequip();
                            break;
                        case 0x73:
                            msg = new RequestAquireSkillInfo(); // RequestAcquireSkillInfo();
                            break;
                        case 0x74:
                            msg = new SendBypassBuildCmd();
                            break;
                        case 0x75:
                            msg = new RequestMoveToLocationInVehicle();
                            break;
                        case 0x76:
                            msg = new CannotMoveAnymoreInVehicle();
                            break;
                        case 0x77:
                            msg = new RequestFriendInvite();
                            break;
                        case 0x78:
                            msg = new RequestFriendAddReply();
                            break;
                        case 0x79:
                            msg = new RequestFriendList();
                            break;
                        case 0x7a:
                            msg = new RequestFriendDel();
                            break;
                        case 0x7c:
                            msg = new RequestAquireSkill();
                            break;
                        case 0x7d:
                            msg = new RequestRestartPoint();
                            break;
                        case 0x7e:
                            msg = new RequestGMCommand();
                            break;
                        case 0x7f:
                            msg = new RequestPartyMatchConfig();
                            break;
                        case 0x80:
                            msg = new RequestPartyMatchList();
                            break;
                        case 0x81:
                            msg = new RequestPartyMatchDetail();
                            break;
                        case 0x82:
                            msg = new RequestPrivateStoreList();
                            break;
                        case 0x83:
                            msg = new RequestPrivateStoreBuy();
                            break;
                        case 0x84:
                            // msg = new ReviveReply(); // format: cd ?
                            _log.info("ReviveReply");
                            break;
                        case 0x85:
                            msg = new RequestTutorialLinkHtml();
                            break;
                        case 0x86:
                            msg = new RequestTutorialPassCmdToServer();
                            break;
                        case 0x87:
                            msg = new RequestTutorialQuestionMark(); // RequestTutorialQuestionMarkPressed();
                            break;
                        case 0x88:
                            msg = new RequestTutorialClientEvent();
                            break;
                        case 0x89:
                            msg = new RequestPetition();
                            break;
                        case 0x8a:
                            msg = new RequestPetitionCancel();
                            break;
                        case 0x8b:
                            msg = new RequestGmList();
                            break;
                        case 0x8c:
                            msg = new RequestJoinAlly();
                            break;
                        case 0x8d:
                            msg = new RequestAnswerJoinAlly();
                            break;
                        case 0x8e:
                            // Команда /allyleave - выйти из альянса
                            msg = new RequestWithdrawAlly();
                            break;
                        case 0x8f:
                            // Команда /allydismiss - выгнать клан из альянса
                            msg = new RequestOustAlly();
                            break;
                        case 0x90:
                            // Команда /allydissolve - распустить альянс
                            msg = new RequestDismissAlly();
                            break;
                        case 0x91:
                            msg = new RequestSetAllyCrest();
                            break;
                        case 0x92:
                            msg = new RequestAllyCrest();
                            break;
                        case 0x93:
                            msg = new RequestChangePetName();
                            break;
                        case 0x94:
                            msg = new RequestPetUseItem();
                            break;
                        case 0x95:
                            msg = new RequestGiveItemToPet();
                            break;
                        case 0x96:
                            msg = new RequestPrivateStoreQuitSell();
                            break;
                        case 0x97:
                            msg = new SetPrivateStoreMsgSell();
                            break;
                        case 0x98:
                            msg = new RequestPetGetItem();
                            break;
                        case 0x99:
                            msg = new RequestPrivateStoreBuyManage();
                            break;
                        case 0x9a:
                            msg = new SetPrivateStoreBuyList();
                            break;
                        case 0x9b:
                            //
                            _log.info("0x9b");
                            break;
                        case 0x9c:
                            msg = new RequestPrivateStoreQuitBuy();
                            break;
                        case 0x9d:
                            msg = new SetPrivateStoreMsgBuy();
                            break;
                        case 0x9e:
                            //
                            _log.info("0x9e");
                            break;
                        case 0x9f:
                            msg = new RequestPrivateStoreBuySellList();
                            break;
                        case 0xa0:
                            msg = new RequestTimeCheck();
                            break;
                        case 0xa1:
                            // msg = new ?();
                            _log.info("0xa1");
                            break;
                        case 0xa2:
                            // msg = new ?();
                            _log.info("0xa2");
                            break;
                        case 0xa3:
                            // msg = new ?();
                            _log.info("0xa3");
                            break;
                        case 0xa4:
                            // msg = new ?();
                            _log.info("0xa4");
                            break;
                        case 0xa5:
                            // msg = new ?();
                            _log.info("0xa5");
                            break;
                        case 0xa6:
                            msg = new RequestSkillCoolTime(); //Deprecated ?
                            // _log.info("0xa6");
                            break;
                        case 0xa7:
                            msg = new RequestPackageSendableItemList();
                            break;
                        case 0xa8:
                            msg = new RequestPackageSend();
                            break;
                        case 0xa9:
                            msg = new RequestBlock();
                            break;
                        case 0xaa:
                            // msg = new RequestCastleSiegeInfo(); // format: cd
                            // ?
                            _log.info("RequestCastleSiegeInfo");
                            break;
                        case 0xab:
                            msg = new RequestCastleSiegeAttackerList();
                            break;
                        case 0xac:
                            msg = new RequestCastleSiegeDefenderList();
                            break;
                        case 0xad:
                            msg = new RequestJoinCastleSiege();
                            break;
                        case 0xae:
                            msg = new RequestConfirmCastleSiegeWaitingList();
                            break;
                        case 0xaf:
                            msg = new RequestSetCastleSiegeTime();
                            break;
                        case 0xb0:
                            msg = new RequestMultiSellChoose();
                            break;
                        case 0xb1:
                            msg = new NetPing();
                            break;
                        case 0xb2:
                            msg = new RequestRemainTime();
                            break;
                        case 0xb3:
                            msg = new BypassUserCmd();
                            break;
                        case 0xb4:
                            msg = new SnoopQuit();
                            break;
                        case 0xb5:
                            msg = new RequestRecipeBookOpen();
                            break;
                        case 0xb6:
                            msg = new RequestRecipeItemDelete();
                            break;
                        case 0xb7:
                            msg = new RequestRecipeItemMakeInfo();
                            break;
                        case 0xb8:
                            msg = new RequestRecipeItemMakeSelf();
                            break;
                        case 0xb9:
                            // msg = new RequestRecipeShopManageList();
                            // deprecated // format: c
                            _log.info("RequestRecipeShopManageList");
                            break;
                        case 0xba:
                            msg = new RequestRecipeShopMessageSet();
                            break;
                        case 0xbb:
                            msg = new RequestRecipeShopListSet();
                            break;
                        case 0xbc:
                            msg = new RequestRecipeShopManageQuit();
                            break;
                        case 0xbd:
                            msg = new RequestRecipeShopManageCancel();
                            break;
                        case 0xbe:
                            msg = new RequestRecipeShopMakeInfo();
                            break;
                        case 0xbf:
                            msg = new RequestRecipeShopMakeDo();
                            break;
                        case 0xc0:
                            msg = new RequestRecipeShopSellList();
                            break;
                        case 0xc1:
                            msg = new RequestObserverEnd();
                            break;
                        case 0xc2:
                            // msg = new VoteSociality(); // Recommend
                            _log.info("0xc2");
                            break;
                        case 0xc3:
                            msg = new RequestHennaList(); // RequestHennaItemList();
                            break;
                        case 0xc4:
                            msg = new RequestHennaItemInfo();
                            break;
                        case 0xc5:
                            msg = new RequestBuySeed();
                            break;
                        case 0xc6:
                            msg = new ConfirmDlg();
                            break;
                        case 0xc7:
                            msg = new RequestPreviewItem();
                            break;
                        case 0xc8:
                            msg = new RequestSSQStatus();
                            break;
                        case 0xc9:
                            msg = new PetitionVote();
                            break;
                        case 0xca:
                            // msg = new ?();
                            _log.info("0xca");
                            break;
                        case 0xcb:
                            msg = new ReplyGameGuardQuery();
                            break;
                        case 0xcc:
                            msg = new RequestPledgePower();
                            break;
                        case 0xcd:
                            msg = new RequestMakeMacro();
                            break;
                        case 0xce:
                            msg = new RequestDeleteMacro();
                            break;
                        case 0xcf:
                            msg = new RequestProcureCrop(); // ?
                            break;
                        case 0xd0:
                            int id3 = buf.getShort() & 0xffff;
                            //           printDebugDoubleOpcode(id, id3, buf, client.getState(), client);
                            switch (id3) {
                                case 0x00:
                                    // msg = new ?();
                                    _log.info("0xd0:0x00");
                                    break;
                                case 0x01:
                                    msg = new RequestManorList();
                                    break;
                                case 0x02:
                                    msg = new RequestProcureCropList();
                                    break;
                                case 0x03:
                                    msg = new RequestSetSeed();
                                    break;
                                case 0x04:
                                    msg = new RequestSetCrop();
                                    break;
                                case 0x05:
                                    msg = new RequestWriteHeroWords();
                                    break;
                                case 0x06:
                                    msg = new RequestExMPCCAskJoin(); // RequestExAskJoinMPCC();
                                    break;
                                case 0x07:
                                    msg = new RequestExMPCCAcceptJoin(); // RequestExAcceptJoinMPCC();
                                    break;
                                case 0x08:
                                    msg = new RequestExOustFromMPCC();
                                    break;
                                case 0x09:
                                    msg = new RequestOustFromPartyRoom();
                                    break;
                                case 0x0A:
                                    msg = new RequestDismissPartyRoom();
                                    break;
                                case 0x0B:
                                    msg = new RequestWithdrawPartyRoom();
                                    break;
                                case 0x0C:
                                    msg = new RequestHandOverPartyMaster();
                                    break;
                                case 0x0D:
                                    msg = new RequestAutoSoulShot();
                                    break;
                                case 0x0E:
                                    msg = new RequestExEnchantSkillInfo();
                                    break;
                                case 0x0F:
                                    msg = new RequestExEnchantSkill();
                                    break;
                                case 0x10:
                                    msg = new RequestPledgeCrestLarge();
                                    break;
                                case 0x11:
                                    msg = new RequestSetPledgeCrestLarge();
                                    break;
                                case 0x12:
                                    msg = new RequestPledgeSetAcademyMaster();
                                    break;
                                case 0x13:
                                    msg = new RequestPledgePowerGradeList();
                                    break;
                                case 0x14:
                                    msg = new RequestPledgeMemberPowerInfo();
                                    break;
                                case 0x15:
                                    msg = new RequestPledgeSetMemberPowerGrade();
                                    break;
                                case 0x16:
                                    msg = new RequestPledgeMemberInfo();
                                    break;
                                case 0x17:
                                    msg = new RequestPledgeWarList();
                                    break;
                                case 0x18:
                                    msg = new RequestExFishRanking();
                                    break;
                                case 0x19:
                                    msg = new RequestPCCafeCouponUse();
                                    break;
                                case 0x1A:
                                    //msg = new RequestServerLogin(); (chb) b - array размером в 64 байта
                                    _log.info("RequestServerLogin");
                                    break;
                                case 0x1B:
                                    msg = new RequestDuelStart();
                                    break;
                                case 0x1C:
                                    msg = new RequestDuelAnswerStart();
                                    break;
                                case 0x1D:
                                    msg = new RequestTutorialClientEvent(); // RequestExSetTutorial(); Format: d / требует отладки, ИМХО, это совсем другой пакет (с) Drin
                                    break;
                                case 0x1E:
                                    msg = new RequestExRqItemLink();
                                    break;
                                case 0x1F:
                                    msg = new CannotMoveAnymoreInVehicle(); // (AirShip) (ddddd)
                                    break;
                                case 0x20:
                                    msg = new RequestExMoveToLocationInAirShip();
                                    break;
                                case 0x21:
                                    msg = new RequestKeyMapping();
                                    break;
                                case 0x22:
                                    msg = new RequestSaveKeyMapping();
                                    break;
                                case 0x23:
                                    msg = new RequestExRemoveItemAttribute();
                                    break;
                                case 0x24:
                                    msg = new RequestSaveInventoryOrder();
                                    break;
                                case 0x25:
                                    msg = new RequestExitPartyMatchingWaitingRoom();
                                    break;
                                case 0x26:
                                    msg = new RequestConfirmTargetItem();
                                    break;
                                case 0x27:
                                    msg = new RequestConfirmRefinerItem();
                                    break;
                                case 0x28:
                                    msg = new RequestConfirmGemStone();
                                    break;
                                case 0x29:
                                    msg = new RequestOlympiadObserverEnd();
                                    break;
                                case 0x2A:
                                    msg = new RequestCursedWeaponList();
                                    break;
                                case 0x2B:
                                    msg = new RequestCursedWeaponLocation();
                                    break;
                                case 0x2C:
                                    msg = new RequestPledgeReorganizeMember();
                                    break;
                                case 0x2D:
                                    msg = new RequestExMPCCShowPartyMembersInfo();
                                    break;
                                case 0x2E:
                                    msg = new RequestExOlympiadObserverEnd(); // не уверен (в клиенте называется RequestOlympiadMatchList)
                                    break;
                                case 0x2F:
                                    msg = new RequestAskJoinPartyRoom();
                                    break;
                                case 0x30:
                                    msg = new AnswerJoinPartyRoom();
                                    break;
                                case 0x31:
                                    msg = new RequestListPartyMatchingWaitingRoom();
                                    break;
                                case 0x32:
                                    msg = new RequestEnchantItemAttribute();
                                    break;
                                case 0x33:
                                    //msg = new RequestGotoLobby();
                                    _log.info("RequestGotoLobby");
                                    break;
                                case 0x35:
                                    msg = new RequestExMoveToLocationAirShip();
                                    break;
                                case 0x36:
                                    msg = new RequestBidItemAuction();
                                    break;
                                case 0x37:
                                    msg = new RequestInfoItemAuction();
                                    break;
                                case 0x38:
                                    msg = new RequestExChangeName();
                                    break;
                                case 0x39:
                                    msg = new RequestAllCastleInfo();
                                    break;
                                case 0x3A:
                                    msg = new RequestAllFortressInfo();
                                    break;
                                case 0x3B:
                                    msg = new RequestAllAgitInfo();
                                    break;
                                case 0x3C:
                                    msg = new RequestFortressSiegeInfo();
                                    break;
                                case 0x3D:
                                    msg = new RequestGetBossRecord();
                                    break;
                                case 0x3E:
                                    msg = new RequestRefine();
                                    break;
                                case 0x3F:
                                    msg = new RequestConfirmCancelItem();
                                    break;
                                case 0x40:
                                    msg = new RequestRefineCancel();
                                    break;
                                case 0x41:
                                    msg = new RequestExMagicSkillUseGround();
                                    break;
                                case 0x42:
                                    msg = new RequestDuelSurrender();
                                    break;
                                case 0x43:
                                    msg = new RequestExEnchantSkillInfoDetail();
                                    break;
                                /** 0xD0:0x44 - пропущен корейцами */
                                case 0x45:
                                    msg = new RequestFortressMapInfo();
                                    break;
                                case 0x46:
                                    msg = new RequestPVPMatchRecord();
                                    break;
                                case 0x47:
                                    msg = new SetPrivateStoreWholeMsg();
                                    break;
                                case 0x48:
                                    msg = new RequestDispel();
                                    break;
                                case 0x49:
                                    msg = new RequestExTryToPutEnchantTargetItem();
                                    break;
                                case 0x4A:
                                    msg = new RequestExTryToPutEnchantSupportItem();
                                    break;
                                case 0x4B:
                                    msg = new RequestExCancelEnchantItem();
                                    break;
                                case 0x4C:
                                    msg = new RequestChangeNicknameColor();
                                    break;
                                case 0x4D:
                                    msg = new RequestResetNickname();
                                    break;
                                case 0x4E:
                                    int id4 = buf.getInt();
                                    switch (id4) {
                                        case 0x00:
                                            msg = new RequestBookMarkSlotInfo();
                                            break;
                                        case 0x01:
                                            msg = new RequestSaveBookMarkSlot();
                                            break;
                                        case 0x02:
                                            msg = new RequestModifyBookMarkSlot();
                                            break;
                                        case 0x03:
                                            msg = new RequestDeleteBookMarkSlot();
                                            break;
                                        case 0x04:
                                            msg = new RequestTeleportBookMark();
                                            break;
                                        case 0x05:
                                            msg = new RequestChangeBookMarkSlot();
                                            break;
                                        default:
                                            client.onUnknownPacket("IN_GAME" + " " + "id3", id4);
                                            break;
                                    }
                                    break;
                                case 0x4F:
                                    msg = new RequestWithDrawPremiumItem();
                                    break;
                                case 0x50:
                                    msg = new RequestExJump();
                                    break;
                                case 0x51:
                                    msg = new RequestExStartShowCrataeCubeRank();
                                    break;
                                case 0x52:
                                    msg = new RequestExStopShowCrataeCubeRank();
                                    break;
                                case 0x53:
                                    msg = new NotifyStartMiniGame();
                                    break;
                                case 0x54:
                                    msg = new RequestExJoinDominionWar();
                                    break;
                                case 0x55:
                                    msg = new RequestExDominionInfo();
                                    break;
                                case 0x56:
                                    msg = new RequestExCleftEnter();
                                    break;
                                case 0x57:
                                    msg = new RequestExCubeGameChangeTeam();
                                    break;
                                case 0x58:
                                    msg = new RequestExEndScenePlayer();
                                    break;
                                case 0x59:
                                    msg = new RequestExCubeGameReadyAnswer(); // RequestExBlockGameVote
                                    break;
                                case 0x5A:
                                    msg = new RequestExListMpccWaiting();
                                    break;
                                case 0x5B:
                                    msg = new RequestExManageMpccRoom();
                                    break;
                                case 0x5C:
                                    msg = new RequestExJoinMpccRoom();
                                    break;
                                case 0x5D:
                                    msg = new RequestExOustFromMpccRoom();
                                    break;
                                case 0x5E:
                                    msg = new RequestExDismissMpccRoom();
                                    break;
                                case 0x5F:
                                    msg = new RequestExWithdrawMpccRoom();
                                    break;
                                case 0x60:
                                    msg = new RequestExSeedPhase();
                                    break;
                                case 0x61:
                                    msg = new RequestExMpccPartymasterList();
                                    break;
                                case 0x62:
                                    msg = new RequestExPostItemList();
                                    break;
                                case 0x63:
                                    msg = new RequestExSendPost();
                                    break;
                                case 0x64:
                                    msg = new RequestExRequestReceivedPostList();
                                    break;
                                case 0x65:
                                    msg = new RequestExDeleteReceivedPost();
                                    break;
                                case 0x66:
                                    msg = new RequestExRequestReceivedPost();
                                    break;
                                case 0x67:
                                    msg = new RequestExReceivePost();
                                    break;
                                case 0x68:
                                    msg = new RequestExRejectPost();
                                    break;
                                case 0x69:
                                    msg = new RequestExRequestSentPostList();
                                    break;
                                case 0x6A:
                                    msg = new RequestExDeleteSentPost();
                                    break;
                                case 0x6B:
                                    msg = new RequestExRequestSentPost();
                                    break;
                                case 0x6C:
                                    msg = new RequestExCancelSentPost();
                                    break;
                                case 0x6D:
                                    msg = new RequestExShowNewUserPetition();
                                    break;
                                case 0x6E:
                                    msg = new RequestExShowStepTwo();
                                    break;
                                case 0x6F:
                                    msg = new RequestExShowStepThree();
                                    break;
                                case 0x70:
                                    // msg = new ExConnectToRaidServer(); (chddd)
                                    _log.info("ExConnectToRaidServer");
                                    break;
                                case 0x71:
                                    // msg = new ExReturnFromRaidServer(); (chd)
                                    _log.info("ExReturnFromRaidServer");
                                    break;
                                case 0x72:
                                    msg = new RequestExRefundItem();
                                    break;
                                case 0x73:
                                    msg = new RequestExBuySellUIClose();
                                    break;
                                case 0x74:
                                    msg = new RequestExEventMatchObserverEnd();
                                    break;
                                case 0x75:
                                    msg = new RequestPartyLootModification();
                                    break;
                                case 0x76:
                                    msg = new AnswerPartyLootModification();
                                    break;
                                case 0x77:
                                    msg = new AnswerCoupleAction();
                                    break;
                                case 0x78:
                                    msg = new RequestExBR_EventRankerList();
                                    break;
                                case 0x79:
                                    // msg = new RequestAskMemberShip();
                                    _log.info("RequestAskMemberShip");
                                    break;
                                case 0x7A:
                                    msg = new RequestAddExpandQuestAlarm();
                                    break;
                                case 0x7B:
                                    msg = new RequestVoteNew();
                                    break;
                                case 0x7C:
                                    msg = new RequestGetOnShuttle();
                                    break;
                                case 0x7D:
                                    msg = new RequestGetOffShuttle();
                                    break;
                                case 0x7E:
                                    msg = new RequestMoveToLocationInShuttle();
                                    break;
                                case 0x7F:
                                    msg = new CannotMoveAnymoreInVehicle(); // CannotMoveAnymoreInShuttle(); (chddddd)
                                    break;
                                case 0x80:
                                    int id5 = buf.getInt();
                                    switch (id5) {
                                        case 0x01:
                                            //msg = new RequestExAgitInitialize chd 0x01
                                            break;
                                        case 0x02:
                                            //msg = new RequestExAgitDetailInfo chdcd 0x02
                                            break;
                                        case 0x03:
                                            //msg = new RequestExMyAgitState chd 0x03
                                            break;
                                        case 0x04:
                                            //msg = new RequestExRegisterAgitForBidStep1 chd 0x04
                                            break;
                                        case 0x05:
                                            //msg = new RequestExRegisterAgitForBidStep2 chddQd 0x05 //msg = new RequestExRegisterAgitForBidStep3 chddQd 0x05 -no error? 0x05
                                            break;
                                        case 0x07:
                                            //msg = new RequestExConfirmCancelRegisteringAgit chd 0x07
                                            break;
                                        case 0x08:
                                            //msg = new RequestExProceedCancelRegisteringAgit chd 0x08
                                            break;
                                        case 0x09:
                                            //msg = new RequestExConfirmCancelAgitBid chdd 0x09
                                            break;
                                        case 0x10:
                                            //msg = new RequestExReBid chdd 0x10
                                            break;
                                        case 0x11:
                                            //msg = new RequestExAgitListForLot chd 0x11
                                            break;
                                        case 0x12:
                                            //msg = new RequestExApplyForAgitLotStep1 chdc 0x12
                                            break;
                                        case 0x13:
                                            //msg = new RequestExApplyForAgitLotStep2 chdc 0x13
                                            break;
                                        case 0x14:
                                            //msg = new RequestExAgitListForBid chdd 0x14
                                            break;
                                        case 0x0D:
                                            //msg = new RequestExApplyForBidStep1 chdd 0x0D
                                            break;
                                        case 0x0E:
                                            //msg = new RequestExApplyForBidStep2 chddQ 0x0E
                                            break;
                                        case 0x0F:
                                            //msg = new RequestExApplyForBidStep3 chddQ 0x0F
                                            break;
                                        //case 0x09:
                                        //msg = new RequestExConfirmCancelAgitLot chdc 0x09
                                        //break;
                                        case 0x0A:
                                            //msg = new RequestExProceedCancelAgitLot chdc 0x0A
                                            _log.info("RequestExProceedCancelAgitLot");
                                            break;
                                        //case 0x0A:
                                        //msg = new RequestExProceedCancelAgitBid chdd 0x0A
                                        //break;
                                    }
                                    break;
                                case 0x81:
                                    msg = new RequestExAddPostFriendForPostBox();
                                    break;
                                case 0x82:
                                    msg = new RequestExDeletePostFriendForPostBox();
                                    break;
                                case 0x83:
                                    msg = new RequestExShowPostFriendListForPostBox();
                                    break;
                                case 0x84:
                                    msg = new RequestExFriendListForPostBox(); // TODO[Mazaffaka] - по сути является 84 у оверов, но в клиенте никак не используется!
                                    break;
                                case 0x85:
                                    msg = new RequestOlympiadMatchList(); // TODO[Mazaffaka] - должен работать в буфере (на 00 позиции). Может заготовка корейцев на будущее О_О?
                                    break;
                                case 0x86:
                                    msg = new RequestExBR_GamePoint();
                                    break;
                                case 0x87:
                                    msg = new RequestExBR_ProductList();
                                    break;
                                case 0x88:
                                    msg = new RequestExBR_ProductInfo();
                                    break;
                                case 0x89:
                                    msg = new RequestExBR_BuyProduct();
                                    break;
                                case 0x8A:
                                    msg = new RequestExBR_RecentProductList();
                                    break;
                                case 0x8B:
                                    msg = new RequestBR_MiniGameLoadScores();
                                    break;
                                case 0x8C:
                                    msg = new RequestBR_MiniGameInsertScore();
                                    break;
                                case 0x8D:
                                    msg = new RequestExBR_LectureMark();
                                    break;
                                case 0x8E:
                                    msg = new RequestCrystallizeEstimate();
                                    break;
                                case 0x8F:
                                    msg = new RequestCrystallizeItemCancel();
                                    break;
                                case 0x90:
                                    msg = new RequestExEscapeScene();
                                    break;
                                case 0x91:
                                    msg = new RequestFlyMove();
                                    break;
                                case 0x92:
                                    //msg = new RequestSurrenderPledgeWarEX(); (chS)
                                    _log.info("RequestSurrenderPledgeWarEX");
                                    break;
                                case 0x93:
                                    int id6 = buf.getInt();
                                    switch (id6) {
                                        case 0x02:
                                            msg = new RequestDynamicQuestProgressInfo();
                                            break;
                                        case 0x03:
                                            msg = new RequestDynamicQuestScoreBoard();
                                            break;
                                        case 0x04:
                                            msg = new RequestDynamicQuestHTML();
                                            break;
                                        default:
                                            client.onUnknownPacket("IN_GAME" + " " + "id3", id6);
                                            break;
                                    }
                                    break;
                                case 0x94:
                                    msg = new RequestFriendDetailInfo();
                                    break;
                                case 0x95:
                                    //   msg = new RequestUpdateFriendMemo();
                                    _log.info("RequestUpdateFriendMemo");
                                    break;
                                case 0x96:
                                    //  msg = new RequestUpdateBlockMemo();
                                    _log.info("RequestUpdateBlockMemo");
                                    break;
                                case 0x97:
                                    msg = new RequestInzonePartyInfoHistory();
                                    break;
                                case 0x98:
                                    msg = new RequestCommissionRegistrableItemList();
                                    break;
                                case 0x99:
                                    msg = new RequestCommissionInfo();
                                    break;
                                case 0x9A:
                                    msg = new RequestCommissionRegister();
                                    break;
                                case 0x9B:
                                    msg = new RequestCommissionCancel();
                                    break;
                                case 0x9C:
                                    msg = new RequestCommissionDelete();
                                    break;
                                case 0x9D:
                                    msg = new RequestCommissionList();
                                    break;
                                case 0x9E:
                                    msg = new RequestCommissionBuyInfo();
                                    break;
                                case 0x9F:
                                    msg = new RequestCommissionBuyItem();
                                    break;
                                case 0xA0:
                                    msg = new RequestCommissionRegisteredItem();
                                    break;
                                case 0xA1:
                                    msg = new RequestCallToChangeClass();
                                    break;
                                case 0xA2:
                                    msg = new RequestChangeToAwakenedClass();
                                    break;
                                case 0xA3:
                                    msg = new RequestWorldStatistics();
                                    break;
                                case 0xA4:
                                    msg = new RequestUserStatistics();
                                    break;
                                case 0xA5:
                                    msg = new RequestRegistPartySubstitute();
                                    break;
                                case 0xA6:
                                    msg = new RequestDeletePartySubstitute();
                                    break;
                                case 0xA7:
                                    msg = new RequestRegistWaitingSubstitute();
                                    break;
                                case 0xA8:
                                    msg = new RequestAcceptWaitingSubstitute();
                                    break;
                                case 0xA9:
                                    msg = new Request24HzSessionID();
                                    break;
                                case 0xAE:
                                    msg = new RequestGoodsInventoryInfo();
                                    break;
                                case 0xAF:
                                    int id7 = buf.getInt();
                                    switch (id7) {
                                        case 0x00:
                                            msg = new RequestUseGoodsInventoryItem();
                                            break;
                                        case 0x01:
                                            msg = new RequestUseGoodsInventoryItem();
                                            break;
                                        default:
                                            client.onUnknownPacket("INGAME" + " " + "id3", id7);
                                            _log.warn("Unknown packet on state: CONNECTED, id: " + Integer.toHexString(id) + " from " + client.getConnection().getSocket().getInetAddress().getHostAddress());
                                    }
                                    break;
                                case 0xB0:
                                    msg = new RequestFirstPlayStart();
                                    break;
                                case 0xB1:
                                    msg = new RequestFlyMoveStart();
                                    break;
                                case 0xB2:
                                    _log.info("RequestHardWareInfo(SddddddSddddddddddSS)");
                                    break;
                                /** 0xD0:0xB3 пропущен корейцами */
                                case 0xB4:
                                    msg = new SendChangeAttributeTargetItem();
                                    break;
                                case 0xB5:
                                    msg = new RequestChangeAttributeItem();
                                    break;
                                case 0xB6:
                                    msg = new RequestChangeAttributeCancel();
                                    break;
                                case 0xB7:
                                    //  msg = new RequestExchangeSubstitute();
                                    _log.info("RequestExchangeSubstitute ddd");
                                    break;
                                case 0xB8:
                                    msg = new RequestBR_PresentBuyProduct();
                                    break;
                                case 0xB9:
                                    msg = new ConfirmMenteeAdd();
                                    break;
                                case 0xBA:
                                    msg = new RequestMentorCancel();
                                    break;
                                case 0xBB:
                                    msg = new RequestMentorList();
                                    break;
                                case 0xBC:
                                    msg = new RequestMenteeAdd();
                                    break;
                                case 0xBD:
                                    msg = new RequestMenteeWaitingList();
                                    break;
                                case 0xBE:
                                    msg = new RequestClanAskJoinByName();
                                    break;
                                case 0xBF:
                                    msg = new RequestInzoneWaitingTime();
                                    break;
                                case 0xC0:
                                    msg = new RequestJoinCuriousHouse();// (ch) TODO[Mazaffaka]
                                    break;
                                case 0xC1:
                                    msg = new RequestCancelCuriousHouse();// (ch) TODO[Mazaffaka]
                                    break;
                                case 0xC2:
                                    msg = new RequestLeaveCuriousHouse();// (ch) TODO[Mazaffaka]
                                    break;
                                case 0xC3:
                                    msg = new RequestObservingListCuriousHouse();// (ch) TODO[Mazaffaka]
                                    break;
                                case 0xC4:
                                    msg = new RequestObservingCuriousHouse();// (chd) TODO[Mazaffaka]
                                    break;
                                case 0xC5:
                                    msg = new RequestLeaveObservingCuriousHouse();// (ch) TODO[Mazaffaka]
                                    break;
                                case 0xC6:
                                    msg = new RequestCuriousHouseHtml();// (ch) TODO[Mazaffaka]
                                    break;
                                case 0xC7:
                                    msg = new RequestCuriousHouseRecord();// (ch) TODO[Mazaffaka]
                                    break;
                                case 0xC8:
                                    //msg = new ExSysstring(); (chdS) TODO[M]
                                    break;
                                case 0xC9:
                                    msg = new RequestExTryToPutShapeShiftingTargetItem();
                                    break;
                                case 0xCA:
                                    msg = new RequestExTryToPutShapeShiftingEnchantSupportItem();
                                    break;
                                case 0xCB:
                                    msg = new RequestExCancelShape_Shifting_Item();
                                    break;
                                case 0xCC:
                                    msg = new RequestShapeShiftingItem();
                                    break;
                                case 0xCD:
                                    //msg = new NCGuardSendDataToServer(); // (chdb) TODO[Mazaffaka]
                                    break;
                                case 0xCE:
                                    _log.info("RequestUnionJoin(d)");
                                    break;
                                case 0xCF:
                                    _log.info("RequestUnionChange(d)");
                                    break;
                                case 0xd0:
                                    _log.info("RequestUnionWithdraw()");
                                    break;
                                case 0xD1:
                                    _log.info("RequestUnionRequest(d)");
                                    break;
                                case 0xD2:
                                    _log.info("RequestUnionAdjust()");
                                    break;
                                case 0xD3:
                                    _log.info("RequestUnionSummon(d)");
                                    break;
                                case 0xD4:
                                    _log.info("RequestUnionStart(d)");
                                    break;
                                case 0xD5:
                                    _log.info("RequestEventKalieToken(d)");
                                    break;
                                case 0xD6:
                                    _log.info("RequestShowPledgeUnionInfo()"); // меню клаз заказов
                                    break;
                                case 0xD7:
                                    msg = new RequestShowBeautyList();   //Салон Красоты
                                    _log.info("RequestShowBeautyList(ddd)");    //Салон Красоты
                                    break;
                                case 0xD8:
                                    msg = new RequestRegistBeauty();   //Салон Красоты
                                    _log.info("RequestRegistBeauty(ddd)");    //Салон Красоты
                                    break;
                                case 0xD9:
                                    _log.info("0xd0:0xd9");
                                    break;
                                case 0xDA:
                                    // msg = new RequestShowResetShopList(); // (ch) TODO[Mazaffaka]
                                    _log.info("RequestShowResetShopList()");
                                    break;
                                case 0xDB:
                                    //msg = new NetPing(); // (chd) TODO[Mazaffaka]
                                    break;
                                case 0xDC:
                                    // msg = new RequestBR_AddBasketProductInfo(); //chd
                                    _log.info("RequestBR_AddBasketProductInfo()");
                                    break;
                                case 0xdd:
                                    _log.info("RequestResetBeauty(ddd)");    //Салон Красоты
                                    msg = new RequestResetBeauty();    //Салон Красоты
                                    break;
                                case 0xDE:
                                    msg = new d0de();
                                    break;
                                case 0xDF:
                                    int id8 = buf.getInt();
                                    switch (id8) {
                                        case 0x02:
                                            _log.info("RequestEventCampaignQuestHTML(cdd)");
                                            break;
                                        case 0x03:
                                            _log.info("RequestEventCampaignProgressInfo(cdd)");
                                            break;
                                        case 0x04:
                                            _log.info("RequestEventCampaignScoreBoard(cdd)");
                                            break;
                                        default:
                                            client.onUnknownPacket("AUTHED" + " " + "id3", id8);
                                            _log.warn("Unknown packet on state: CONNECTED, id: " + Integer.toHexString(id) + " from " + client.getConnection().getSocket().getInetAddress().getHostAddress());
                                    }
                                    break;
                                case 0xE0:
                                    msg = new RequestPledgeRecruitInfo();   //Поиск клана
                                    break;
                                case 0xE1:
                                    msg = new RequestPledgeRecruitBoardSearch(); //Поиск клана
                                    break;
                                case 0xE2:
                                    msg = new RequestPledgeRecruitBoardAccess(); //Поиск клана
                                    break;
                                case 0xE3:
                                    msg = new RequestPledgeRecruitBoardDetail();    //Поиск клана
                                    break;
                                case 0xE4:
                                    msg = new RequestPledgeWaitingApply();     //Поиск клана
                                    break;
                                case 0xE5:
                                    msg = new RequestPledgeWaitingApplied();   //Поиск клана
                                    break;
                                case 0xE6:
                                    msg = new RequestPledgeWaitingList();    //Поиск клана
                                    break;
                                case 0xE7:
                                    msg = new RequestPledgeWaitingUser();   //Поиск клана
                                    break;
                                case 0xE8:
                                    msg = new RequestPledgeWaitingUserAccept();   //Поиск клана
                                    break;
                                case 0xE9:
                                    msg = new RequestPledgeDraftListSearch();  //Поиск клана
                                    break;
                                case 0xEA:
                                    msg = new RequestPledgeDraftListApply();     //Поиск клана
                                    break;
                                case 0xEB:
                                    msg = new RequestPledgeRecruitApplyInfo();     //Поиск клана
                                    break;
                                case 0xEC:
                                    msg = new RequestPledgeJoinSys();       //Поиск клана
                                    break;
                                case 0xED:
                                    _log.info("ResponsePetitionAlarm()");
                                    break;
                                case 0xEF:
                                    _log.info("RequestRegisterXMasWishCard(S)");
                                    break;
                                case 0xEE:
                                    _log.info("NotifyExitBeautyshop()");         //Салон Красоты
                                    msg = new NotifyExitBeautyshop();         //Салон Красоты
                                    break;
                                case 0xF0:
                                    //msg = new RequestExAddEnchantScrollItem(); // (chdd) TODO[Mazaffaka]
                                    _log.info("RequestExAddEnchantScrollItem()");
                                    break;
                                case 0xF1:
                                    //msg = new RequestExRemoveEnchantSupportItem(); // (ch) TODO[Mazaffaka]
                                    _log.info("RequestExRemoveEnchantSupportItem()");
                                    break;
                                case 0xF2:
                                    //   msg = new RequestCardReward(); // (chd) TODO[Mazaffaka]
                                    _log.info("RequestCardReward()");
                                    break;
                                default:
                                    client.onUnknownPacket("id3", id3);
                                    break;
                            }
                            break;
                        default: {
                            client.onUnknownPacket("id", id);
                            _log.warn("Unknown packet on state: INGAME, id: " + Integer.toHexString(id) + " from " + client.getConnection().getSocket().getInetAddress().getHostAddress());
                            break;
                        }
                    }
                    break;
                default:
                    _log.warn("Unknown packet on state: CONNECTED, id: " + Integer.toHexString(id) + " from " + client.getConnection().getSocket().getInetAddress().getHostAddress());

                    break;
            }
        } catch (BufferUnderflowException e) {
            client.onPacketReadFail();
        }
        return msg;
    }

    @Override
    public GameClient create(MMOConnection<GameClient> con) {
        return new GameClient(con);
    }

    @Override
    public void execute(Runnable r) {
        ThreadPoolManager.getInstance().execute(r);
    }
}
