/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.handler.admincommands.impl;

import l2p.gameserver.handler.admincommands.IAdminCommandHandler;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.entity.residence.Castle;
import l2p.gameserver.network.serverpackets.*;
import l2p.gameserver.network.serverpackets.CuriousHouse.*;
import l2p.gameserver.network.serverpackets.commission.ExResponseCommissionDelete;
import l2p.gameserver.network.serverpackets.commission.ExResponseCommissionRegister;
import l2p.gameserver.network.serverpackets.company.MPCC.ExSetMpccRouting;
import l2p.gameserver.network.serverpackets.company.alliance.*;
import l2p.gameserver.network.serverpackets.company.party.ExPartyMemberRenamed;
import l2p.gameserver.network.serverpackets.company.pledge.PledgeExtendedInfo;
import l2p.gameserver.network.serverpackets.company.pledge.StartPledgeWar;
import l2p.gameserver.network.serverpackets.company.pledge.StopPledgeWar;
import l2p.gameserver.network.serverpackets.company.pledge.SurrenderPledgeWar;
import l2p.gameserver.templates.StatsSet;

public class AdminTest implements IAdminCommandHandler {

    private static enum Commands {
        admin_st_1, admin_st_2, admin_st_3, admin_st_4, admin_st_5, admin_st_6, admin_st_7, admin_st_8, admin_st_9, admin_st_10, admin_st_11, admin_st_12, admin_st_13, admin_st_14, admin_st_15, admin_st_16, admin_st_17, admin_st_18, admin_st_19, admin_st_20, admin_st_21, admin_st_22, admin_st_23, admin_st_24, admin_st_25, admin_st_26, admin_st_27, admin_st_28, admin_st_29, admin_st_30, admin_st_31, admin_st_32, admin_st_33, admin_st_34, admin_st_35, admin_st_36, admin_st_37, admin_st_38, admin_st_39, admin_st_40, admin_st_41, admin_st_42, admin_st_43, admin_st_44, admin_st_45, admin_st_46, admin_st_47, admin_st_48, admin_st_49, admin_st_50, admin_st_51, admin_st_52, admin_st_53, admin_st_54, admin_st_55, admin_st_56, admin_st_57, admin_st_58, admin_st_59, admin_st_60, admin_st_61, admin_st_62, admin_st_63, admin_st_64, admin_st_65, admin_st_66, admin_st_67, admin_st_68, admin_st_69, admin_st_70, admin_st_71, admin_st_72, admin_st_73, admin_st_74, admin_st_75, admin_st_76, admin_st_77, admin_st_78, admin_st_79, admin_st_80, admin_st_81, admin_st_82, admin_st_83, admin_st_84, admin_st_85, admin_st_86, admin_st_87, admin_st_88, admin_st_89, admin_st_90, admin_st_91, admin_st_92, admin_st_93, admin_st_94, admin_st_95, admin_st_96, admin_st_97, admin_st_98, admin_st_99, admin_st_100, admin_st_101, admin_st_102, admin_st_103, admin_st_104, admin_st_105, admin_st_106, admin_st_107, admin_st_108, admin_st_109, admin_st_110, admin_st_111, admin_st_112, admin_st_113, admin_st_114, admin_st_115, admin_st_116
    }

    @Override
    public boolean useAdminCommand(Enum comm, String[] wordList, String fullString, Player activeChar) {
        Commands command = (Commands) comm;

        switch (command) {
            case admin_st_1:
                activeChar.sendPacket(new AbnormalVisualEffectPacket());
                break;
            case admin_st_2:
                activeChar.sendPacket(new AttackDeadTarget());
                break;
            case admin_st_3:
                activeChar.sendPacket(new AttackinCoolTime());
                break;
            case admin_st_4:
                activeChar.sendPacket(new AttackOutOfRange());
                break;
            case admin_st_5:
                activeChar.sendPacket(new BlockList(activeChar));
                break;
            case admin_st_6:
                activeChar.sendPacket(new ClientAction());
                break;
            case admin_st_7:
                activeChar.sendPacket(new DeleteRadar());
                break;
            case admin_st_8:
                activeChar.sendPacket(new DismissAlliance());
                break;
            case admin_st_9:
                activeChar.sendPacket(new ExCuriousHouseEnter());
                break;
            case admin_st_10:
                activeChar.sendPacket(new ExCuriousHouseLeave());
                break;
            case admin_st_11:
                activeChar.sendPacket(new ExCuriousHouseMemberUpdate());
                break;
            case admin_st_12:
                activeChar.sendPacket(new ExCuriousHouseMemberList());
                break;
            case admin_st_13:
                activeChar.sendPacket(new ExAlterSkillRequest(10023, 10022, 5));
                break;
            case admin_st_14:
                activeChar.sendPacket(new ExCuriousHouseObserveList());
                break;
            case admin_st_20:
                activeChar.sendPacket(new ExBR_BuffEventState(1, 5, 5, 5));
                break;
            case admin_st_21:
                //      activeChar.sendPacket(new ExBR_BuyProductResult(1));
                break;
            case admin_st_22:
                activeChar.sendPacket(new ExBR_LoadEventTopRankers(1, 1, 1, 1, 1));
                break;
            case admin_st_23:
                activeChar.sendPacket(new ExBR_RecentProductListPacket(activeChar));
                break;
            case admin_st_24:
                activeChar.sendPacket(new ExBR_BroadcastEventState(20090401, 1));
                break;
            case admin_st_25:
                activeChar.sendPacket(new ExBR_BuffEventState(1, 1, 1, 1970));
                break;
            case admin_st_26:
                activeChar.sendPacket(new ExBR_ExtraUserInfo(activeChar));
                break;
            case admin_st_27:
                StatsSet set = new StatsSet();
                Castle cl = new Castle(set);
                activeChar.sendPacket(new ExCastleState(cl));
                break;
            case admin_st_28:
                activeChar.sendPacket(new ExChangeAttributeFail());
                break;
            case admin_st_29:
                activeChar.sendPacket(new ExChangeMPCost());
                break;
            case admin_st_31:
                activeChar.sendPacket(new ExCleftList());
                break;
            case admin_st_32:
                activeChar.sendPacket(new ExCleftState());
                break;
            case admin_st_33:
                activeChar.sendPacket(new ExColosseumFenceInfo());
                break;
            case admin_st_34:
                activeChar.sendPacket(new ExDominionChannelSet(1));
                break;
            case admin_st_35:
                activeChar.sendPacket(new ExDuelEnemyRelation());
                break;
            case admin_st_36:
                activeChar.sendPacket(new ExDummyPacket());
                break;
            case admin_st_37:
                activeChar.sendPacket(new ExEnchantSkillList(ExEnchantSkillList.EnchantSkillType.NORMAL));
                break;
            case admin_st_38:
                activeChar.sendPacket(new ExEventMatchCreate());
                break;
            case admin_st_39:
                activeChar.sendPacket(new ExEventMatchFirecracker());
                break;
            case admin_st_40:
                activeChar.sendPacket(new ExEventMatchGMTest());
                break;
            case admin_st_41:
                activeChar.sendPacket(new ExEventMatchList());
                break;
            case admin_st_42:
                activeChar.sendPacket(new ExEventMatchLockResult());
                break;
            case admin_st_43:
                activeChar.sendPacket(new ExEventMatchManage());
                break;
            case admin_st_44:
                activeChar.sendPacket(new ExEventMatchMessage());
                break;
            case admin_st_45:
                activeChar.sendPacket(new ExEventMatchObserver());
                break;
            case admin_st_46:
                activeChar.sendPacket(new ExEventMatchScore());
                break;
            case admin_st_47:
                activeChar.sendPacket(new ExEventMatchSpelledInfo());
                break;
            case admin_st_48:
                // activeChar.sendPacket(new ExEventMatchTeamInfo()); break;
            case admin_st_49:
                activeChar.sendPacket(new ExEventMatchTeamUnlocked());
                break;
            case admin_st_50:
                activeChar.sendPacket(new ExEventMatchUserInfo());
                break;
            case admin_st_51:
                activeChar.sendPacket(new ExFriendNotifyNameChange());
                break;
            case admin_st_52:
                activeChar.sendPacket(new ExGet24HzSessionID());
                break;
            case admin_st_53:
                activeChar.sendPacket(new ExGetCrystalizingFail(1));
                break;
            case admin_st_54:
                // activeChar.sendPacket(new ExGoodsInventoryInfo()); break;
            case admin_st_55:
                activeChar.sendPacket(new ExGoodsInventoryResult(2));
                break;
            case admin_st_56:
                // activeChar.sendPacket(new ExJumpToLocation()); break;
            case admin_st_57:
                activeChar.sendPacket(new ExMagicAttackInfo(activeChar));
                break;
            case admin_st_58:
                activeChar.sendPacket(new ExMagicSkillUseInAirShip());
                break;
            case admin_st_59:
                activeChar.sendPacket(new ExMenteeSearch(1, 40, 76));
                break;
            case admin_st_60:
                // activeChar.sendPacket(new ExMoveToTargetInAirShip()); break;
            case admin_st_61:
                activeChar.sendPacket(new ExNeedToChangeName(1, 1, "Test"));
                break;
            case admin_st_62:
                activeChar.sendPacket(new ExNotifyBirthDay());
                break;
            case admin_st_63:
                activeChar.sendPacket(new ExPartyMemberRenamed());
                break;
            case admin_st_64:
                activeChar.sendPacket(new ExPeriodicItemList());
                break;
            case admin_st_65:
                activeChar.sendPacket(new ExPlayAnimation());
                break;
            case admin_st_66:
                activeChar.sendPacket(new ExPlayScene());
                break;
            case admin_st_69:
                activeChar.sendPacket(new ExPVPMatchRecord());
                break;
            case admin_st_70:
                activeChar.sendPacket(new ExPVPMatchUserDie());
                break;
            case admin_st_71:
                activeChar.sendPacket(new ExRaidCharSelected());
                break;
            case admin_st_72:
                activeChar.sendPacket(new ExRaidReserveResult());
                break;
            case admin_st_73:
                activeChar.sendPacket(new ExRegistPartySubstitute(activeChar.getObjectId(), 1));
                break;
            case admin_st_74:
                activeChar.sendPacket(new ExRequestHackShield());
                break;
            case admin_st_75:
                activeChar.sendPacket(new ExResponseCommissionDelete());
                break;
            case admin_st_76:
                activeChar.sendPacket(new ExResponseCommissionRegister());
                break;
            case admin_st_77:
                activeChar.sendPacket(new ExResponseFreeServer());
                break;
            case admin_st_78:
                activeChar.sendPacket(new ExRestartClient());
                break;
            case admin_st_79:
                activeChar.sendPacket(new ExSearchOrc());
                break;
            case admin_st_80:
                activeChar.sendPacket(new ExServerPrimitive());
                break;
            case admin_st_81:
                activeChar.sendPacket(new ExSetMpccRouting());
                break;
            case admin_st_82:
                activeChar.sendPacket(new ExShowAdventurerGuideBook());
                break;
            case admin_st_83:
                activeChar.sendPacket(new ExShowLines());
                break;
            case admin_st_84:
                activeChar.sendPacket(new ExShowPetitionHtml());
                break;
            case admin_st_85:
                activeChar.sendPacket(new ExShowTerritory());
                break;
            case admin_st_86:
                activeChar.sendPacket(new ExStopScenePlayerPacket(76));
                break;
            case admin_st_87:
                activeChar.sendPacket(new FlySelfDestination());
                break;
            case admin_st_88:
                activeChar.sendPacket(new FriendAdd("Mazaffaka"));
                break;
            case admin_st_89:
                activeChar.sendPacket(new FriendAddRequestResult());
                break;
            case admin_st_90:
                activeChar.sendPacket(new FriendRemove());
                break;
            case admin_st_91:
                activeChar.sendPacket(new FriendStatus(activeChar.getObjectId()));
                break;
            case admin_st_92:
                activeChar.sendPacket(new GameGuardQuery());
                break;
            case admin_st_93:
                activeChar.sendPacket(new GMHide(activeChar.getObjectId()));
                break;
            case admin_st_94:
                // activeChar.sendPacket(new MagicAndSkillList()); break;
            case admin_st_95:
                // activeChar.sendPacket(new MoveToPawn()); break;
            case admin_st_96:
                activeChar.sendPacket(new NormalCamera());
                break;
            case admin_st_97:
                activeChar.sendPacket(new OustAllianceMemberPledge());
                break;
            case admin_st_98:
                activeChar.sendPacket(new PetitionVote());
                break;
            case admin_st_99:
                activeChar.sendPacket(new PledgeExtendedInfo());
                break;
            case admin_st_100:
                activeChar.sendPacket(new RequestTimeCheck());
                break;
            case admin_st_101:
                // activeChar.sendPacket(new SellListProcure()); break;
            case admin_st_103:
                activeChar.sendPacket(new SetPledgeCrestPacket());
                break;
            case admin_st_104:
                activeChar.sendPacket(new ShowPCCafeCouponShowUI());
                break;
            case admin_st_105:
                activeChar.sendPacket(new ShowRadar());
                break;
            case admin_st_106:
                activeChar.sendPacket(new SkillRemainSec());
                break;
            case admin_st_107:
                // activeChar.sendPacket(new Snoop()); break;
            case admin_st_108:
                activeChar.sendPacket(new StartAllianceWar("Test", "Mazaffaka"));
                break;
            case admin_st_109:
                activeChar.sendPacket(new StartPledgeWar("Test", "Mazaffaka"));
                break;
            case admin_st_110:
                activeChar.sendPacket(new StopAllianceWar("Test", "Mazaffaka"));
                break;
            case admin_st_111:
                activeChar.sendPacket(new StopPledgeWar("Test", "Mazaffaka"));
                break;
            case admin_st_112:
                activeChar.sendPacket(new SunRise());
                break;
            case admin_st_113:
                activeChar.sendPacket(new SunSet());
                break;
            case admin_st_114:
                activeChar.sendPacket(new SurrenderPledgeWar("Test", "Mazaffaka"));
                break;
            case admin_st_115:
                activeChar.sendPacket(new WareHouseDone());
                break;
            case admin_st_116:
                activeChar.sendPacket(new WithdrawAlliance());
                break;
        }
        return true;
    }

    @Override
    public Enum[] getAdminCommandEnum() {
        return Commands.values();
    }
}
