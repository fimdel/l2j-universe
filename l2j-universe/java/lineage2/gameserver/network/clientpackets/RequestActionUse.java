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

import java.util.List;

import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.Config;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.ai.CtrlIntention;
import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.geodata.GeoEngine;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.GameObject;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Request;
import lineage2.gameserver.model.Request.L2RequestType;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.Summon;
import lineage2.gameserver.model.TacticalSignManager;
import lineage2.gameserver.model.entity.boat.ClanAirShip;
import lineage2.gameserver.model.instances.PetBabyInstance;
import lineage2.gameserver.model.instances.StaticObjectInstance;
import lineage2.gameserver.model.instances.residences.SiegeFlagInstance;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.network.serverpackets.ActionFail;
import lineage2.gameserver.network.serverpackets.ExAirShipTeleportList;
import lineage2.gameserver.network.serverpackets.ExAskCoupleAction;
import lineage2.gameserver.network.serverpackets.PrivateStoreManageListBuy;
import lineage2.gameserver.network.serverpackets.PrivateStoreManageListSell;
import lineage2.gameserver.network.serverpackets.RecipeShopManageList;
import lineage2.gameserver.network.serverpackets.SocialAction;
import lineage2.gameserver.network.serverpackets.SystemMessage2;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.tables.PetDataTable;
import lineage2.gameserver.tables.PetSkillsTable;
import lineage2.gameserver.tables.SkillTable;
import lineage2.gameserver.utils.TradeHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestActionUse extends L2GameClientPacket
{
	/**
	 * Field PLAYER_ACTION. (value is 0)
	 */
	private static final int PLAYER_ACTION = 0;
	/**
	 * Field PET_ACTION. (value is 1)
	 */
	private static final int PET_ACTION = 1;
	/**
	 * Field SERVITOR_ACTION. (value is 2)
	 */
	private static final int SERVITOR_ACTION = 2;
	/**
	 * Field SERVITOR_GROUP_ACTION. (value is 3)
	 */
	private static final int SERVITOR_GROUP_ACTION = 3;
	/**
	 * Field SOCIAL_ACTION. (value is 4)
	 */
	private static final int SOCIAL_ACTION = 4;
	/**
	 * Field COUPLE_ACTION. (value is 5)
	 */
	private static final int COUPLE_ACTION = 5;
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(RequestActionUse.class);
	/**
	 * Field _actionId.
	 */
	private int _actionId;
	/**
	 * Field _ctrlPressed.
	 */
	private boolean _ctrlPressed;
	/**
	 * Field _shiftPressed.
	 */
	private boolean _shiftPressed;
	
	/**
	 * @author Mobius
	 */
	public static enum Action
	{
		/**
		 * Field ACTION0.
		 */
		ACTION0(0, PLAYER_ACTION, 0, 1),
		/**
		 * Field ACTION1.
		 */
		ACTION1(1, PLAYER_ACTION, 0, 0),
		/**
		 * Field ACTION7.
		 */
		ACTION7(7, PLAYER_ACTION, 0, 1),
		/**
		 * Field ACTION10.
		 */
		ACTION10(10, PLAYER_ACTION, 0, 1),
		/**
		 * Field ACTION28.
		 */
		ACTION28(28, PLAYER_ACTION, 0, 1),
		/**
		 * Field ACTION37.
		 */
		ACTION37(37, PLAYER_ACTION, 0, 1),
		/**
		 * Field ACTION38.
		 */
		ACTION38(38, PLAYER_ACTION, 0, 1),
		/**
		 * Field ACTION51.
		 */
		ACTION51(51, PLAYER_ACTION, 0, 1),
		/**
		 * Field ACTION61.
		 */
		ACTION61(61, PLAYER_ACTION, 0, 1),
		/**
		 * Field ACTION96.
		 */
		ACTION96(96, PLAYER_ACTION, 0, 1),
		/**
		 * Field ACTION97.
		 */
		ACTION97(97, PLAYER_ACTION, 0, 1),
		/**
		 * Field ACTION67.
		 */
		ACTION67(67, PLAYER_ACTION, 0, 1),
		/**
		 * Field ACTION68.
		 */
		ACTION68(68, PLAYER_ACTION, 0, 1),
		/**
		 * Field ACTION69.
		 */
		ACTION69(69, PLAYER_ACTION, 0, 1),
		/**
		 * Field ACTION70.
		 */
		ACTION70(70, PLAYER_ACTION, 0, 1),
		/**
		 * Field ACTION15.
		 */
		ACTION15(15, PET_ACTION, 0, 0),
		/**
		 * Field ACTION16.
		 */
		ACTION16(16, PET_ACTION, 0, 0),
		/**
		 * Field ACTION17.
		 */
		ACTION17(17, PET_ACTION, 0, 0),
		/**
		 * Field ACTION19.
		 */
		ACTION19(19, PET_ACTION, 0, 0),
		/**
		 * Field ACTION54.
		 */
		ACTION54(54, PET_ACTION, 0, 0),
		/**
		 * Field ACTION21.
		 */
		ACTION21(21, SERVITOR_ACTION, 0, 0),
		/**
		 * Field ACTION22.
		 */
		ACTION22(22, SERVITOR_ACTION, 0, 0),
		/**
		 * Field ACTION23.
		 */
		ACTION23(23, SERVITOR_ACTION, 0, 0),
		/**
		 * Field ACTION52.
		 */
		ACTION52(52, SERVITOR_ACTION, 0, 0),
		/**
		 * Field ACTION53.
		 */
		ACTION53(53, SERVITOR_ACTION, 0, 0),
		/**
		 * Field ACTION1100.
		 */
		ACTION1100(1100, SERVITOR_GROUP_ACTION, 0, 0),
		/**
		 * Field ACTION1101.
		 */
		ACTION1101(1101, SERVITOR_GROUP_ACTION, 0, 0),
		/**
		 * Field ACTION1102.
		 */
		ACTION1102(1102, SERVITOR_GROUP_ACTION, 0, 0),
		/**
		 * Field ACTION32.
		 */
		ACTION32(32, SERVITOR_ACTION, 4230, 0),
		/**
		 * Field ACTION36.
		 */
		ACTION36(36, SERVITOR_ACTION, 4259, 0),
		/**
		 * Field ACTION39.
		 */
		ACTION39(39, SERVITOR_ACTION, 4138, 0),
		/**
		 * Field ACTION41.
		 */
		ACTION41(41, SERVITOR_ACTION, 4230, 0),
		/**
		 * Field ACTION42.
		 */
		ACTION42(42, SERVITOR_ACTION, 4378, 0),
		/**
		 * Field ACTION43.
		 */
		ACTION43(43, SERVITOR_ACTION, 4137, 0),
		/**
		 * Field ACTION44.
		 */
		ACTION44(44, SERVITOR_ACTION, 4139, 0),
		/**
		 * Field ACTION45.
		 */
		ACTION45(45, SERVITOR_ACTION, 4025, 0),
		/**
		 * Field ACTION46.
		 */
		ACTION46(46, SERVITOR_ACTION, 4261, 0),
		/**
		 * Field ACTION47.
		 */
		ACTION47(47, SERVITOR_ACTION, 4260, 0),
		/**
		 * Field ACTION48.
		 */
		ACTION48(48, SERVITOR_ACTION, 4068, 0),
		/**
		 * Field ACTION1000.
		 */
		ACTION1000(1000, SERVITOR_ACTION, 4079, 0),
		/**
		 * Field ACTION1003.
		 */
		ACTION1003(1003, PET_ACTION, 4710, 0),
		/**
		 * Field ACTION1004.
		 */
		ACTION1004(1004, PET_ACTION, 4711, 0),
		/**
		 * Field ACTION1005.
		 */
		ACTION1005(1005, PET_ACTION, 4712, 0),
		/**
		 * Field ACTION1006.
		 */
		ACTION1006(1006, PET_ACTION, 4713, 0),
		/**
		 * Field ACTION1007.
		 */
		ACTION1007(1007, SERVITOR_ACTION, 4699, 0),
		/**
		 * Field ACTION1008.
		 */
		ACTION1008(1008, SERVITOR_ACTION, 4700, 0),
		/**
		 * Field ACTION1009.
		 */
		ACTION1009(1009, SERVITOR_ACTION, 4701, 0),
		/**
		 * Field ACTION1010.
		 */
		ACTION1010(1010, SERVITOR_ACTION, 4702, 0),
		/**
		 * Field ACTION1011.
		 */
		ACTION1011(1011, SERVITOR_ACTION, 4703, 0),
		/**
		 * Field ACTION1012.
		 */
		ACTION1012(1012, SERVITOR_ACTION, 4704, 0),
		/**
		 * Field ACTION1013.
		 */
		ACTION1013(1013, SERVITOR_ACTION, 4705, 0),
		/**
		 * Field ACTION1014.
		 */
		ACTION1014(1014, SERVITOR_ACTION, 4706, 0),
		/**
		 * Field ACTION1015.
		 */
		ACTION1015(1015, SERVITOR_ACTION, 4707, 0),
		/**
		 * Field ACTION1016.
		 */
		ACTION1016(1016, SERVITOR_ACTION, 4709, 0),
		/**
		 * Field ACTION1017.
		 */
		ACTION1017(1017, SERVITOR_ACTION, 4708, 0),
		/**
		 * Field ACTION1031.
		 */
		ACTION1031(1031, SERVITOR_ACTION, 5135, 0),
		/**
		 * Field ACTION1032.
		 */
		ACTION1032(1032, SERVITOR_ACTION, 5136, 0),
		/**
		 * Field ACTION1033.
		 */
		ACTION1033(1033, SERVITOR_ACTION, 5137, 0),
		/**
		 * Field ACTION1034.
		 */
		ACTION1034(1034, SERVITOR_ACTION, 5138, 0),
		/**
		 * Field ACTION1035.
		 */
		ACTION1035(1035, SERVITOR_ACTION, 5139, 0),
		/**
		 * Field ACTION1036.
		 */
		ACTION1036(1036, SERVITOR_ACTION, 5142, 0),
		/**
		 * Field ACTION1037.
		 */
		ACTION1037(1037, SERVITOR_ACTION, 5141, 0),
		/**
		 * Field ACTION1038.
		 */
		ACTION1038(1038, SERVITOR_ACTION, 5140, 0),
		/**
		 * Field ACTION1039.
		 */
		ACTION1039(1039, SERVITOR_ACTION, 5110, 0),
		/**
		 * Field ACTION1040.
		 */
		ACTION1040(1040, SERVITOR_ACTION, 5111, 0),
		/**
		 * Field ACTION1041.
		 */
		ACTION1041(1041, PET_ACTION, 5442, 0),
		/**
		 * Field ACTION1042.
		 */
		ACTION1042(1042, PET_ACTION, 5444, 0),
		/**
		 * Field ACTION1043.
		 */
		ACTION1043(1043, PET_ACTION, 5443, 0),
		/**
		 * Field ACTION1044.
		 */
		ACTION1044(1044, PET_ACTION, 5445, 0),
		/**
		 * Field ACTION1045.
		 */
		ACTION1045(1045, PET_ACTION, 5584, 0),
		/**
		 * Field ACTION1046.
		 */
		ACTION1046(1046, PET_ACTION, 5585, 0),
		/**
		 * Field ACTION1047.
		 */
		ACTION1047(1047, SERVITOR_ACTION, 5580, 0),
		/**
		 * Field ACTION1048.
		 */
		ACTION1048(1048, SERVITOR_ACTION, 5581, 0),
		/**
		 * Field ACTION1049.
		 */
		ACTION1049(1049, SERVITOR_ACTION, 5582, 0),
		/**
		 * Field ACTION1050.
		 */
		ACTION1050(1050, SERVITOR_ACTION, 5583, 0),
		/**
		 * Field ACTION1051.
		 */
		ACTION1051(1051, SERVITOR_ACTION, 5638, 0),
		/**
		 * Field ACTION1052.
		 */
		ACTION1052(1052, SERVITOR_ACTION, 5639, 0),
		/**
		 * Field ACTION1053.
		 */
		ACTION1053(1053, SERVITOR_ACTION, 5640, 0),
		/**
		 * Field ACTION1054.
		 */
		ACTION1054(1054, SERVITOR_ACTION, 5643, 0),
		/**
		 * Field ACTION1055.
		 */
		ACTION1055(1055, SERVITOR_ACTION, 5647, 0),
		/**
		 * Field ACTION1056.
		 */
		ACTION1056(1056, SERVITOR_ACTION, 5648, 0),
		/**
		 * Field ACTION1057.
		 */
		ACTION1057(1057, SERVITOR_ACTION, 5646, 0),
		/**
		 * Field ACTION1058.
		 */
		ACTION1058(1058, SERVITOR_ACTION, 5652, 0),
		/**
		 * Field ACTION1059.
		 */
		ACTION1059(1059, SERVITOR_ACTION, 5653, 0),
		/**
		 * Field ACTION1060.
		 */
		ACTION1060(1060, SERVITOR_ACTION, 5654, 0),
		/**
		 * Field ACTION1061.
		 */
		ACTION1061(1061, PET_ACTION, 5745, 0),
		/**
		 * Field ACTION1062.
		 */
		ACTION1062(1062, PET_ACTION, 5746, 0),
		/**
		 * Field ACTION1063.
		 */
		ACTION1063(1063, PET_ACTION, 5747, 0),
		/**
		 * Field ACTION1064.
		 */
		ACTION1064(1064, PET_ACTION, 5748, 0),
		/**
		 * Field ACTION1065.
		 */
		ACTION1065(1065, PET_ACTION, 5753, 0),
		/**
		 * Field ACTION1066.
		 */
		ACTION1066(1066, PET_ACTION, 5749, 0),
		/**
		 * Field ACTION1067.
		 */
		ACTION1067(1067, PET_ACTION, 5750, 0),
		/**
		 * Field ACTION1068.
		 */
		ACTION1068(1068, PET_ACTION, 5751, 0),
		/**
		 * Field ACTION1069.
		 */
		ACTION1069(1069, PET_ACTION, 5752, 0),
		/**
		 * Field ACTION1070.
		 */
		ACTION1070(1070, PET_ACTION, 5771, 0),
		/**
		 * Field ACTION1071.
		 */
		ACTION1071(1071, PET_ACTION, 5761, 0),
		/**
		 * Field ACTION1072.
		 */
		ACTION1072(1072, PET_ACTION, 6046, 0),
		/**
		 * Field ACTION1073.
		 */
		ACTION1073(1073, PET_ACTION, 6047, 0),
		/**
		 * Field ACTION1074.
		 */
		ACTION1074(1074, PET_ACTION, 6048, 0),
		/**
		 * Field ACTION1075.
		 */
		ACTION1075(1075, PET_ACTION, 6049, 0),
		/**
		 * Field ACTION1076.
		 */
		ACTION1076(1076, PET_ACTION, 6050, 0),
		/**
		 * Field ACTION1077.
		 */
		ACTION1077(1077, PET_ACTION, 6051, 0),
		/**
		 * Field ACTION1078.
		 */
		ACTION1078(1078, PET_ACTION, 6052, 0),
		/**
		 * Field ACTION1079.
		 */
		ACTION1079(1079, PET_ACTION, 6053, 0),
		/**
		 * Field ACTION1080.
		 */
		ACTION1080(1080, SERVITOR_ACTION, 6041, 0),
		/**
		 * Field ACTION1081.
		 */
		ACTION1081(1081, SERVITOR_ACTION, 6042, 0),
		/**
		 * Field ACTION1082.
		 */
		ACTION1082(1082, SERVITOR_ACTION, 6043, 0),
		/**
		 * Field ACTION1083.
		 */
		ACTION1083(1083, SERVITOR_ACTION, 6044, 0),
		/**
		 * Field ACTION1084.
		 */
		ACTION1084(1084, SERVITOR_ACTION, 6054, 0),
		/**
		 * Field ACTION1086.
		 */
		ACTION1086(1086, SERVITOR_ACTION, 6094, 0),
		/**
		 * Field ACTION1087.
		 */
		ACTION1087(1087, SERVITOR_ACTION, 6095, 0),
		/**
		 * Field ACTION1088.
		 */
		ACTION1088(1088, SERVITOR_ACTION, 6096, 0),
		/**
		 * Field ACTION1089.
		 */
		ACTION1089(1089, PET_ACTION, 6199, 0),
		/**
		 * Field ACTION1090.
		 */
		ACTION1090(1090, SERVITOR_ACTION, 6205, 0),
		/**
		 * Field ACTION1091.
		 */
		ACTION1091(1091, SERVITOR_ACTION, 6206, 0),
		/**
		 * Field ACTION1092.
		 */
		ACTION1092(1092, SERVITOR_ACTION, 6207, 0),
		/**
		 * Field ACTION1093.
		 */
		ACTION1093(1093, SERVITOR_ACTION, 6618, 0),
		/**
		 * Field ACTION1094.
		 */
		ACTION1094(1094, SERVITOR_ACTION, 6681, 0),
		/**
		 * Field ACTION1095.
		 */
		ACTION1095(1095, SERVITOR_ACTION, 6619, 0),
		/**
		 * Field ACTION1096.
		 */
		ACTION1096(1096, SERVITOR_ACTION, 6682, 0),
		/**
		 * Field ACTION1097.
		 */
		ACTION1097(1097, SERVITOR_ACTION, 6683, 0),
		/**
		 * Field ACTION1098.
		 */
		ACTION1098(1098, SERVITOR_ACTION, 6684, 0),
		/**
		 * Field ACTION1099.
		 */
		ACTION1099(1099, SERVITOR_GROUP_ACTION, 0, 0),
		/**
		 * Field ACTION5000.
		 */
		ACTION5000(5000, PET_ACTION, 23155, 0),
		/**
		 * Field ACTION5001.
		 */
		ACTION5001(5001, PET_ACTION, 23167, 0),
		/**
		 * Field ACTION5002.
		 */
		ACTION5002(5002, PET_ACTION, 23168, 0),
		/**
		 * Field ACTION5003.
		 */
		ACTION5003(5003, PET_ACTION, 5749, 0),
		/**
		 * Field ACTION5004.
		 */
		ACTION5004(5004, PET_ACTION, 5750, 0),
		/**
		 * Field ACTION5005.
		 */
		ACTION5005(5005, PET_ACTION, 5751, 0),
		/**
		 * Field ACTION5006.
		 */
		ACTION5006(5006, PET_ACTION, 5771, 0),
		/**
		 * Field ACTION5007.
		 */
		ACTION5007(5007, PET_ACTION, 6046, 0),
		/**
		 * Field ACTION5008.
		 */
		ACTION5008(5008, PET_ACTION, 6047, 0),
		/**
		 * Field ACTION5009.
		 */
		ACTION5009(5009, PET_ACTION, 6048, 0),
		/**
		 * Field ACTION5010.
		 */
		ACTION5010(5010, PET_ACTION, 6049, 0),
		/**
		 * Field ACTION5011.
		 */
		ACTION5011(5011, PET_ACTION, 6050, 0),
		/**
		 * Field ACTION5012.
		 */
		ACTION5012(5012, PET_ACTION, 6051, 0),
		/**
		 * Field ACTION5013.
		 */
		ACTION5013(5013, PET_ACTION, 6052, 0),
		/**
		 * Field ACTION5014.
		 */
		ACTION5014(5014, PET_ACTION, 6053, 0),
		/**
		 * Field ACTION5015.
		 */
		ACTION5015(5015, PET_ACTION, 6054, 0),
		/**
		 * Field ACTION5016.
		 */
		ACTION5016(5016, PET_ACTION, 23318, 0),
		/**
		 * Field ACTION1103.
		 */
		ACTION1103(1103, SERVITOR_GROUP_ACTION, 0, 0),
		/**
		 * Field ACTION1104.
		 */
		ACTION1104(1104, SERVITOR_GROUP_ACTION, 0, 0),
		/**
		 * Field ACTION1106.
		 */
		ACTION1106(1106, SERVITOR_GROUP_ACTION, 11278, 0),
		/**
		 * Field ACTION1107.
		 */
		ACTION1107(1107, SERVITOR_GROUP_ACTION, 11279, 0),
		/**
		 * Field ACTION1108.
		 */
		ACTION1108(1108, SERVITOR_GROUP_ACTION, 11280, 0),
		/**
		 * Field ACTION1109.
		 */
		ACTION1109(1109, SERVITOR_GROUP_ACTION, 11281, 0),
		/**
		 * Field ACTION1110.
		 */
		ACTION1110(1110, SERVITOR_GROUP_ACTION, 11282, 0),
		/**
		 * Field ACTION1111.
		 */
		ACTION1111(1111, SERVITOR_GROUP_ACTION, 11283, 0),
		/**
		 * Field ACTION1113.
		 */
		ACTION1113(1113, SERVITOR_GROUP_ACTION, 10051, 0),
		/**
		 * Field ACTION1114.
		 */
		ACTION1114(1114, SERVITOR_GROUP_ACTION, 10052, 0),
		/**
		 * Field ACTION1115.
		 */
		ACTION1115(1115, SERVITOR_GROUP_ACTION, 10053, 0),
		/**
		 * Field ACTION1116.
		 */
		ACTION1116(1116, SERVITOR_GROUP_ACTION, 10054, 0),
		/**
		 * Field ACTION1117.
		 */
		ACTION1117(1117, SERVITOR_ACTION, 10794, 0),
		/**
		 * Field ACTION1118.
		 */
		ACTION1118(1118, SERVITOR_ACTION, 10795, 0),
		/**
		 * Field ACTION1120.
		 */
		ACTION1120(1120, SERVITOR_ACTION, 10797, 0),
		/**
		 * Field ACTION1121.
		 */
		ACTION1121(1121, SERVITOR_ACTION, 10798, 0),
		/**
		 * Field ACTION1122.
		 */
		ACTION1122(1122, SERVITOR_GROUP_ACTION, 11806, 0),
		/**
		 * Field ACTION1123.
		 */
		ACTION1123(1123, SERVITOR_GROUP_ACTION, 14767, 0),
		/**
		 * Field ACTION12.
		 */
		ACTION12(12, SOCIAL_ACTION, SocialAction.GREETING, 2),
		/**
		 * Field ACTION13.
		 */
		ACTION13(13, SOCIAL_ACTION, SocialAction.VICTORY, 2),
		/**
		 * Field ACTION14.
		 */
		ACTION14(14, SOCIAL_ACTION, SocialAction.ADVANCE, 2),
		/**
		 * Field ACTION24.
		 */
		ACTION24(24, SOCIAL_ACTION, SocialAction.YES, 2),
		/**
		 * Field ACTION25.
		 */
		ACTION25(25, SOCIAL_ACTION, SocialAction.NO, 2),
		/**
		 * Field ACTION26.
		 */
		ACTION26(26, SOCIAL_ACTION, SocialAction.BOW, 2),
		/**
		 * Field ACTION29.
		 */
		ACTION29(29, SOCIAL_ACTION, SocialAction.UNAWARE, 2),
		/**
		 * Field ACTION30.
		 */
		ACTION30(30, SOCIAL_ACTION, SocialAction.WAITING, 2),
		/**
		 * Field ACTION31.
		 */
		ACTION31(31, SOCIAL_ACTION, SocialAction.LAUGH, 2),
		/**
		 * Field ACTION33.
		 */
		ACTION33(33, SOCIAL_ACTION, SocialAction.APPLAUD, 2),
		/**
		 * Field ACTION34.
		 */
		ACTION34(34, SOCIAL_ACTION, SocialAction.DANCE, 2),
		/**
		 * Field ACTION35.
		 */
		ACTION35(35, SOCIAL_ACTION, SocialAction.SORROW, 2),
		/**
		 * Field ACTION62.
		 */
		ACTION62(62, SOCIAL_ACTION, SocialAction.CHARM, 2),
		/**
		 * Field ACTION66.
		 */
		ACTION66(66, SOCIAL_ACTION, SocialAction.SHYNESS, 2),
		/**
		 * Field ACTION87.
		 */
		ACTION87(87, SOCIAL_ACTION, SocialAction.PROPOSE, 2),
		/**
		 * Field ACTION88.
		 */
		ACTION88(88, SOCIAL_ACTION, SocialAction.PROVOKE, 2),
		/**
		 * Field ACTION71.
		 */
		ACTION71(71, COUPLE_ACTION, SocialAction.COUPLE_BOW, 2),
		/**
		 * Field ACTION72.
		 */
		ACTION72(72, COUPLE_ACTION, SocialAction.COUPLE_HIGH_FIVE, 2),
		/**
		 * Field ACTION73.
		 */
		ACTION73(73, COUPLE_ACTION, SocialAction.COUPLE_DANCE, 2),
		/**
		 * Field ACTION78.
		 */
		ACTION78(78, PLAYER_ACTION, 0, 1),
		/**
		 * Field ACTION79.
		 */
		ACTION79(79, PLAYER_ACTION, 0, 1),
		/**
		 * Field ACTION80.
		 */
		ACTION80(80, PLAYER_ACTION, 0, 1),
		/**
		 * Field ACTION81.
		 */
		ACTION81(81, PLAYER_ACTION, 0, 1),
		/**
		 * Field ACTION82.
		 */
		ACTION82(82, PLAYER_ACTION, 0, 1),
		/**
		 * Field ACTION83.
		 */
		ACTION83(83, PLAYER_ACTION, 0, 1),
		/**
		 * Field ACTION84.
		 */
		ACTION84(84, PLAYER_ACTION, 0, 1),
		/**
		 * Field ACTION85.
		 */
		ACTION85(85, PLAYER_ACTION, 0, 1),
		/**
		 * Field ACTION86.
		 */
		ACTION86(86, PLAYER_ACTION, 0, 1);
		/**
		 * Field id.
		 */
		public int id;
		/**
		 * Field type.
		 */
		public int type;
		/**
		 * Field value.
		 */
		public int value;
		/**
		 * Field transform.
		 */
		public int transform;
		
		/**
		 * Constructor for Action.
		 * @param id int
		 * @param type int
		 * @param value int
		 * @param transform int
		 */
		private Action(int id, int type, int value, int transform)
		{
			this.id = id;
			this.type = type;
			this.value = value;
			this.transform = transform;
		}
		
		/**
		 * Method find.
		 * @param id int
		 * @return Action
		 */
		public static Action find(int id)
		{
			for (Action action : Action.values())
			{
				if (action.id == id)
				{
					return action;
				}
			}
			return null;
		}
	}
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_actionId = readD();
		_ctrlPressed = readD() == 1;
		_shiftPressed = readC() == 1;
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
		Action action = Action.find(_actionId);
		if (action == null)
		{
			_log.warn("unhandled action type " + _actionId + " by player " + activeChar.getName());
			activeChar.sendActionFailed();
			return;
		}
		final boolean usePet = action.type == PET_ACTION;
		final boolean useServitor = action.type == SERVITOR_ACTION;
		final boolean useServitorGroup = action.type == SERVITOR_GROUP_ACTION;
		final GameObject target = activeChar.getTarget();
		final Summon pet = activeChar.getSummonList().getPet();
		final Summon servitor = activeChar.getSummonList().getFirstServitor();
		final List<Summon> servitors = activeChar.getSummonList().getServitors();
		if (activeChar.isGM() && activeChar.isDebug())
		{
			activeChar.sendMessage("ActionId:" + action.id + " useServitor:" + useServitor + " useServitorGroup:" + useServitorGroup);
		}
		if (!usePet && !useServitor && !useServitorGroup && (activeChar.isOutOfControl() || activeChar.isActionsDisabled()) && !(activeChar.isFakeDeath() && (_actionId == 0)))
		{
			activeChar.sendActionFailed();
			return;
		}
		if ((activeChar.getTransformation() != 0) && (action.transform > 0))
		{
			activeChar.sendActionFailed();
			return;
		}
		switch (action.type)
		{
			case PLAYER_ACTION:
				switch (action.id)
				{
					case 0:
						if (activeChar.isMounted())
						{
							activeChar.sendActionFailed();
							break;
						}
						if (activeChar.isFakeDeath())
						{
							activeChar.breakFakeDeath();
							activeChar.updateEffectIcons();
							break;
						}
						if (!activeChar.isSitting())
						{
							if ((target != null) && (target instanceof StaticObjectInstance) && (((StaticObjectInstance) target).getType() == 1) && (activeChar.getDistance3D(target) <= Creature.INTERACTION_DISTANCE))
							{
								activeChar.sitDown((StaticObjectInstance) target);
							}
							else
							{
								activeChar.sitDown(null);
							}
						}
						else
						{
							activeChar.standUp();
						}
						break;
					case 1:
						if (activeChar.isRunning())
						{
							activeChar.setWalking();
						}
						else
						{
							activeChar.setRunning();
						}
						break;
					case 10:
					case 61:
					{
						if (activeChar.getSittingTask())
						{
							activeChar.sendActionFailed();
							return;
						}
						if (activeChar.isInStoreMode())
						{
							activeChar.setPrivateStoreType(Player.STORE_PRIVATE_NONE);
							activeChar.standUp();
							activeChar.broadcastCharInfo();
						}
						else if (!TradeHelper.checksIfCanOpenStore(activeChar, _actionId == 61 ? Player.STORE_PRIVATE_SELL_PACKAGE : Player.STORE_PRIVATE_SELL))
						{
							activeChar.sendActionFailed();
							return;
						}
						activeChar.sendPacket(new PrivateStoreManageListSell(activeChar, _actionId == 61));
						break;
					}
					case 28:
					{
						if (activeChar.getSittingTask())
						{
							activeChar.sendActionFailed();
							return;
						}
						if (activeChar.isInStoreMode())
						{
							activeChar.setPrivateStoreType(Player.STORE_PRIVATE_NONE);
							activeChar.standUp();
							activeChar.broadcastCharInfo();
						}
						else if (!TradeHelper.checksIfCanOpenStore(activeChar, Player.STORE_PRIVATE_BUY))
						{
							activeChar.sendActionFailed();
							return;
						}
						activeChar.sendPacket(new PrivateStoreManageListBuy(activeChar));
						break;
					}
					case 51:
					{
						if (activeChar.getSittingTask())
						{
							activeChar.sendActionFailed();
							return;
						}
						if (activeChar.isInStoreMode())
						{
							activeChar.setPrivateStoreType(Player.STORE_PRIVATE_NONE);
							activeChar.standUp();
							activeChar.broadcastCharInfo();
						}
						else if (!TradeHelper.checksIfCanOpenStore(activeChar, Player.STORE_PRIVATE_MANUFACTURE))
						{
							activeChar.sendActionFailed();
							return;
						}
						activeChar.sendPacket(new RecipeShopManageList(activeChar, true));
						break;
					}
					case 38:
						if (activeChar.getTransformation() != 0)
						{
							activeChar.sendPacket(SystemMsg.YOU_CANNOT_BOARD_BECAUSE_YOU_DO_NOT_MEET_THE_REQUIREMENTS);
						}
						else if (activeChar.isMounted())
						{
							if (activeChar.isFlying() && !activeChar.checkLandingState())
							{
								activeChar.sendPacket(Msg.YOU_ARE_NOT_ALLOWED_TO_DISMOUNT_AT_THIS_LOCATION, ActionFail.STATIC);
								activeChar.sendActionFailed();
								return;
							}
							activeChar.setMount(0, 0, 0);
						}
						else if (activeChar.isMounted() || activeChar.isInBoat())
						{
							activeChar.sendPacket(Msg.YOU_CANNOT_MOUNT_BECAUSE_YOU_DO_NOT_MEET_THE_REQUIREMENTS);
						}
						else if (activeChar.isDead())
						{
							activeChar.sendPacket(Msg.YOU_CANNOT_MOUNT_BECAUSE_YOU_DO_NOT_MEET_THE_REQUIREMENTS);
						}
						else if (activeChar.isInDuel())
						{
							activeChar.sendPacket(Msg.YOU_CANNOT_MOUNT_BECAUSE_YOU_DO_NOT_MEET_THE_REQUIREMENTS);
						}
						else if (activeChar.isFishing())
						{
							activeChar.sendPacket(Msg.YOU_CANNOT_MOUNT_BECAUSE_YOU_DO_NOT_MEET_THE_REQUIREMENTS);
						}
						else if (activeChar.isSitting())
						{
							activeChar.sendPacket(Msg.YOU_CANNOT_MOUNT_BECAUSE_YOU_DO_NOT_MEET_THE_REQUIREMENTS);
						}
						else if (activeChar.isCursedWeaponEquipped())
						{
							activeChar.sendPacket(Msg.YOU_CANNOT_MOUNT_BECAUSE_YOU_DO_NOT_MEET_THE_REQUIREMENTS);
						}
						else if (activeChar.getActiveWeaponFlagAttachment() != null)
						{
							activeChar.sendPacket(Msg.YOU_CANNOT_MOUNT_BECAUSE_YOU_DO_NOT_MEET_THE_REQUIREMENTS);
						}
						else if (activeChar.isCastingNow())
						{
							activeChar.sendPacket(Msg.YOU_CANNOT_MOUNT_BECAUSE_YOU_DO_NOT_MEET_THE_REQUIREMENTS);
						}
						else if (activeChar.isParalyzed())
						{
							activeChar.sendPacket(Msg.YOU_CANNOT_MOUNT_BECAUSE_YOU_DO_NOT_MEET_THE_REQUIREMENTS);
						}
						else if ((pet == null) || activeChar.isInCombat() || pet.isInCombat())
						{
							activeChar.sendPacket(Msg.YOU_CANNOT_MOUNT_BECAUSE_YOU_DO_NOT_MEET_THE_REQUIREMENTS);
						}
						else if (pet.isDead())
						{
							activeChar.sendPacket(Msg.A_DEAD_PET_CANNOT_BE_RIDDEN);
						}
						else if ((pet != null) && pet.isMountable())
						{
							activeChar.getEffectList().stopEffect(Skill.SKILL_EVENT_TIMER);
							activeChar.setMount(pet.getTemplate().npcId, pet.getObjectId(), pet.getLevel());
							activeChar.getSummonList().unsummonPet(false);
						}
						break;
					case 37:
					{
						if (activeChar.getSittingTask())
						{
							activeChar.sendActionFailed();
							return;
						}
						if (activeChar.isInStoreMode())
						{
							activeChar.setPrivateStoreType(Player.STORE_PRIVATE_NONE);
							activeChar.standUp();
							activeChar.broadcastCharInfo();
						}
						else if (!TradeHelper.checksIfCanOpenStore(activeChar, Player.STORE_PRIVATE_MANUFACTURE))
						{
							activeChar.sendActionFailed();
							return;
						}
						activeChar.sendPacket(new RecipeShopManageList(activeChar, false));
						break;
					}
					case 67:
						if (activeChar.isInBoat() && activeChar.getBoat().isClanAirShip() && !activeChar.getBoat().isMoving)
						{
							ClanAirShip boat = (ClanAirShip) activeChar.getBoat();
							if (boat.getDriver() == null)
							{
								boat.setDriver(activeChar);
							}
							else
							{
								activeChar.sendPacket(SystemMsg.ANOTHER_PLAYER_IS_PROBABLY_CONTROLLING_THE_TARGET);
							}
						}
						break;
					case 68:
						if (activeChar.isClanAirShipDriver())
						{
							ClanAirShip boat = (ClanAirShip) activeChar.getBoat();
							boat.setDriver(null);
							activeChar.broadcastCharInfo();
						}
						break;
					case 69:
						if (activeChar.isClanAirShipDriver() && activeChar.getBoat().isDocked())
						{
							activeChar.sendPacket(new ExAirShipTeleportList((ClanAirShip) activeChar.getBoat()));
						}
						break;
					case 70:
						if (activeChar.isInBoat() && activeChar.getBoat().isAirShip() && activeChar.getBoat().isDocked())
						{
							activeChar.getBoat().oustPlayer(activeChar, activeChar.getBoat().getReturnLoc(), true);
						}
						break;
					case 78:
						TacticalSignManager.setTacticalSign(activeChar, target, 1);
						break;
					case 79:
						TacticalSignManager.setTacticalSign(activeChar, target, 2);
						break;
					case 80:
						TacticalSignManager.setTacticalSign(activeChar, target, 3);
						break;
					case 81:
						TacticalSignManager.setTacticalSign(activeChar, target, 4);
						break;
					case 82:
						TacticalSignManager.getTargetOnTacticalSign(activeChar, 1);
						break;
					case 83:
						TacticalSignManager.getTargetOnTacticalSign(activeChar, 2);
						break;
					case 84:
						TacticalSignManager.getTargetOnTacticalSign(activeChar, 3);
						break;
					case 85:
						TacticalSignManager.getTargetOnTacticalSign(activeChar, 4);
						break;
					case 96:
						_log.info("96 Accessed");
						break;
					case 97:
						_log.info("97 Accessed");
						break;
				}
				break;
			case PET_ACTION:
				if ((pet == null) || pet.isOutOfControl())
				{
					activeChar.sendActionFailed();
					return;
				}
				if (pet.isDepressed())
				{
					activeChar.sendPacket(SystemMsg.YOUR_PETSERVITOR_IS_UNRESPONSIVE_AND_WILL_NOT_OBEY_ANY_ORDERS);
					return;
				}
				switch (action.id)
				{
					case 15:
						pet.setFollowMode(!pet.isFollowMode());
						break;
					case 16:
						pet.setFollowMode(!pet.isFollowMode());
						if ((target == null) || !target.isCreature() || (pet == target) || pet.isDead())
						{
							activeChar.sendActionFailed();
							return;
						}
						if (activeChar.isInOlympiadMode() && !activeChar.isOlympiadCompStart())
						{
							activeChar.sendActionFailed();
							return;
						}
						if (pet.getTemplate().getNpcId() == PetDataTable.SIN_EATER_ID)
						{
							activeChar.sendActionFailed();
							return;
						}
						if (!_ctrlPressed && target.isCreature() && !((Creature) target).isAutoAttackable(pet))
						{
							activeChar.sendActionFailed();
							return;
						}
						if (_ctrlPressed && !target.isAttackable(pet))
						{
							activeChar.sendPacket(SystemMsg.INVALID_TARGET);
							activeChar.sendActionFailed();
							return;
						}
						if (!target.isMonster() && (pet.isInZonePeace() || (target.isCreature() && ((Creature) target).isInZonePeace())))
						{
							activeChar.sendPacket(SystemMsg.YOU_MAY_NOT_ATTACK_THIS_TARGET_IN_A_PEACEFUL_ZONE);
							activeChar.sendActionFailed();
							return;
						}
						if ((activeChar.getLevel() + 20) <= pet.getLevel())
						{
							activeChar.sendPacket(SystemMsg.YOUR_PET_IS_TOO_HIGH_LEVEL_TO_CONTROL);
							activeChar.sendActionFailed();
							return;
						}
						pet.getAI().Attack(target, _ctrlPressed, _shiftPressed);
						break;
					case 17:
						pet.getAI().setIntention(CtrlIntention.AI_INTENTION_ACTIVE);
						break;
					case 19:
						if (pet.isDead())
						{
							activeChar.sendPacket(SystemMsg.DEAD_PETS_CANNOT_BE_RETURNED_TO_THEIR_SUMMONING_ITEM, ActionFail.STATIC);
							return;
						}
						if (pet.isInCombat())
						{
							activeChar.sendPacket(SystemMsg.A_PET_CANNOT_BE_UNSUMMONED_DURING_BATTLE, ActionFail.STATIC);
							return;
						}
						if (!PetDataTable.isVitaminPet(pet.getNpcId()) && pet.isPet() && (pet.getCurrentFed() < (0.55 * pet.getMaxFed())))
						{
							activeChar.sendPacket(SystemMsg.YOU_MAY_NOT_RESTORE_A_HUNGRY_PET, ActionFail.STATIC);
							return;
						}
						activeChar.getSummonList().unsummonPet(false);
						break;
					case 54:
						if ((target != null) && (pet != target) && !pet.isMovementDisabled())
						{
							pet.setFollowMode(false);
							pet.moveToLocation(target.getLoc(), 100, true);
						}
						break;
					case 1070:
						if (pet instanceof PetBabyInstance)
						{
							((PetBabyInstance) pet).triggerBuff();
						}
						break;
				}
				if (action.value > 0)
				{
					UseSkill(action.value, pet);
					activeChar.sendActionFailed();
					return;
				}
				break;
			case SERVITOR_ACTION:
				if (servitor == null)
				{
					activeChar.sendActionFailed();
					return;
				}
				switch (action.id)
				{
					case 21:
						servitor.setFollowMode(!servitor.isFollowMode());
						break;
					case 22:
						if ((target == null) || !target.isCreature())
						{
							activeChar.sendActionFailed();
							return;
						}
						if ((servitor == target) || servitor.isDead())
						{
							activeChar.sendActionFailed();
							return;
						}
						if (activeChar.isInOlympiadMode() && !activeChar.isOlympiadCompStart())
						{
							activeChar.sendActionFailed();
							return;
						}
						if (_ctrlPressed)
						{
							if (!target.isAttackable(servitor))
							{
								activeChar.sendPacket(SystemMsg.INVALID_TARGET);
								activeChar.sendActionFailed();
								return;
							}
						}
						else if (!_ctrlPressed && target.isCreature())
						{
							if (!((Creature) target).isAutoAttackable(servitor))
							{
								activeChar.sendActionFailed();
								return;
							}
						}
						if (!target.isMonster() && (target.isCreature() && ((Creature) target).isInZonePeace()))
						{
							if (servitor.isInZonePeace())
							{
								activeChar.sendPacket(SystemMsg.YOU_MAY_NOT_ATTACK_THIS_TARGET_IN_A_PEACEFUL_ZONE);
								activeChar.sendActionFailed();
								return;
							}
						}
						servitor.setFollowMode(!servitor.isFollowMode());
						servitor.getAI().Attack(target, _ctrlPressed, _shiftPressed);
						break;
					case 23:
						servitor.getAI().setIntention(CtrlIntention.AI_INTENTION_ACTIVE);
						break;
					case 52:
						if (servitor.isInCombat())
						{
							activeChar.sendPacket(SystemMsg.A_PET_CANNOT_BE_UNSUMMONED_DURING_BATTLE);
							activeChar.sendActionFailed();
						}
						else
						{
							servitor.saveEffects();
							activeChar.getSummonList().unsummonAllServitors();
						}
						break;
					case 53:
						if ((target != null) && (servitor != target) && !servitor.isMovementDisabled())
						{
							servitor.setFollowMode(false);
							servitor.moveToLocation(target.getLoc(), 100, true);
						}
						break;
					case 1000:
						if ((target != null) && !target.isDoor())
						{
							activeChar.sendActionFailed();
							return;
						}
						break;
					case 1039:
					case 1040:
						if (target.isDoor() || (target instanceof SiegeFlagInstance))
						{
							activeChar.sendActionFailed();
							return;
						}
						break;
				}
				if (action.value > 0)
				{
					UseSkill(action.value, servitor);
					activeChar.sendActionFailed();
					return;
				}
				break;
			case SERVITOR_GROUP_ACTION:
				if (servitors.isEmpty())
				{
					activeChar.sendActionFailed();
					return;
				}
				switch (action.id)
				{
					case 1099:	//Attack
						if ((target == null) || !target.isCreature())
						{
							activeChar.sendActionFailed();
							return;
						}
						if (activeChar.isInOlympiadMode() && !activeChar.isOlympiadCompStart())
						{
							activeChar.sendActionFailed();
							return;
						}
						if (servitors.contains(target))
						{
							activeChar.sendActionFailed();
							return;
						}
						for (Summon summon : servitors)
						{
							if (_ctrlPressed)
							{
								if (!target.isAttackable(summon))
								{
									activeChar.sendPacket(SystemMsg.INVALID_TARGET);
									activeChar.sendActionFailed();
									return;
								}
							}
							else if (!_ctrlPressed && target.isCreature())
							{
								if (!((Creature) target).isAutoAttackable(summon))
								{
									activeChar.sendActionFailed();
									return;
								}
							}
							if (!target.isMonster() && (target.isCreature() && ((Creature) target).isInZonePeace()))
							{
								if (summon.isInZonePeace())
								{
									activeChar.sendPacket(SystemMsg.YOU_MAY_NOT_ATTACK_THIS_TARGET_IN_A_PEACEFUL_ZONE);
									activeChar.sendActionFailed();
									return;
								}
							}
						}
						for (Summon summon : servitors)
						{
							if (!summon.isDead())
							{
								summon.setFollowMode(true);
								summon.getAI().Attack(target, _ctrlPressed, _shiftPressed);
							}
						}
						break;
					case 1100:	//Move
						if ((target != null) && !servitors.contains(target))
						{
							for (Summon summon : servitors)
							{
								if (!summon.isMovementDisabled())
								{
									summon.setFollowMode(false);
									summon.moveToLocation(target.getLoc(), 100, true);
								}
							}
						}
						break;
					case 1101:	//Pause
						for (Summon summon : servitors)
						{
							summon.getAI().setIntention(CtrlIntention.AI_INTENTION_ACTIVE);
						}
						break;
					case 1102:	//Cancel the Summoning
						if (activeChar.getSummonList().isInCombat())
						{
							activeChar.sendPacket(SystemMsg.A_PET_CANNOT_BE_UNSUMMONED_DURING_BATTLE);
							activeChar.sendActionFailed();
						}
						else
						{
							activeChar.getSummonList().unsummonAllServitors();
						}
						break;
					case 1103:	//Passive
						for (Summon summon : servitors)
						{
							summon.setDefendMode(false);
						}
						break;
					case 1104:	//Defend
						for (Summon summon : servitors)
						{
							summon.setDefendMode(true);
						}
						break;
				}
				if (action.value > 0)
				{
					UseSkill(action.value, servitors.toArray(new Summon[servitors.size()]));
					activeChar.sendActionFailed();
					return;
				}
				break;
			case SOCIAL_ACTION:
				if (activeChar.isOutOfControl() || (activeChar.getTransformation() != 0) || activeChar.isActionsDisabled() || activeChar.isSitting() || (activeChar.getPrivateStoreType() != Player.STORE_PRIVATE_NONE) || activeChar.isProcessingRequest())
				{
					activeChar.sendActionFailed();
					return;
				}
				if (activeChar.isFishing())
				{
					activeChar.sendPacket(SystemMsg.YOU_CANNOT_DO_THAT_WHILE_FISHING_2);
					activeChar.sendActionFailed();
					return;
				}
				activeChar.broadcastPacket(new SocialAction(activeChar.getObjectId(), action.value));
				for (QuestState state : activeChar.getAllQuestsStates())
				{
					state.getQuest().notifySocialActionUse(state, action.value);
				}
				if (Config.ALT_SOCIAL_ACTION_REUSE)
				{
					ThreadPoolManager.getInstance().schedule(new SocialTask(activeChar), 2600);
					activeChar.startParalyzed();
				}
				activeChar.getListeners().onSocialAction(action); // DynamicQuest
				break;
			case COUPLE_ACTION:
				if (activeChar.isOutOfControl() || activeChar.isActionsDisabled() || activeChar.isSitting())
				{
					activeChar.sendActionFailed();
					return;
				}
				if ((target == null) || !target.isPlayer())
				{
					activeChar.sendActionFailed();
					return;
				}
				final Player pcTarget = target.getPlayer();
				if (pcTarget.isProcessingRequest() && pcTarget.getRequest().isTypeOf(L2RequestType.COUPLE_ACTION))
				{
					activeChar.sendPacket(new SystemMessage2(SystemMsg.C1_IS_ALREADY_PARTICIPATING_IN_A_COUPLE_ACTION_AND_CANNOT_BE_REQUESTED_FOR_ANOTHER_COUPLE_ACTION).addName(pcTarget));
					return;
				}
				if (pcTarget.isProcessingRequest())
				{
					activeChar.sendPacket(new SystemMessage2(SystemMsg.C1_IS_ON_ANOTHER_TASK).addName(pcTarget));
					return;
				}
				if (!activeChar.isInRange(pcTarget, 300) || activeChar.isInRange(pcTarget, 25) || (activeChar.getTargetId() == activeChar.getObjectId()) || !GeoEngine.canSeeTarget(activeChar, pcTarget, false))
				{
					activeChar.sendPacket(SystemMsg.THE_REQUEST_CANNOT_BE_COMPLETED_BECAUSE_THE_TARGET_DOES_NOT_MEET_LOCATION_REQUIREMENTS);
					return;
				}
				if (!activeChar.checkCoupleAction(pcTarget))
				{
					activeChar.sendActionFailed();
					return;
				}
				new Request(L2RequestType.COUPLE_ACTION, activeChar, pcTarget).setTimeout(10000L);
				activeChar.sendPacket(new SystemMessage2(SystemMsg.YOU_HAVE_REQUESTED_A_COUPLE_ACTION_WITH_C1).addName(pcTarget));
				pcTarget.sendPacket(new ExAskCoupleAction(activeChar.getObjectId(), action.value));
				if (Config.ALT_SOCIAL_ACTION_REUSE)
				{
					ThreadPoolManager.getInstance().schedule(new SocialTask(activeChar), 2600);
					activeChar.startParalyzed();
				}
				break;
		}
		activeChar.sendActionFailed();
	}
	
	/**
	 * Method UseSkill.
	 * @param skillId int
	 * @param casters Summon[]
	 */
	private void UseSkill(int skillId, Summon... casters)
	{
		Player activeChar = getClient().getActiveChar();
		if (casters.length == 0)
		{
			return;
		}
		for (Summon summon : casters)
		{
			if (summon.isPet() && ((activeChar.getLevel() + 20) <= summon.getLevel()))
			{
				activeChar.sendPacket(SystemMsg.YOUR_PET_IS_TOO_HIGH_LEVEL_TO_CONTROL);
				continue;
			}
			int skillLevel = PetSkillsTable.getInstance().getAvailableLevel(summon, skillId);
			Skill skill = SkillTable.getInstance().getInfo(skillId, skillLevel);
			if (skill == null)
			{
				continue;
			}
			Creature aimingTarget = skill.getAimingTarget(summon, activeChar.getTarget());
			if (skill.checkCondition(summon, aimingTarget, _ctrlPressed, _shiftPressed, true))
			{
				summon.getAI().Cast(skill, aimingTarget, _ctrlPressed, _shiftPressed);
			}
		}
	}
	
	/**
	 * @author Mobius
	 */
	static class SocialTask extends RunnableImpl
	{
		/**
		 * Field _player.
		 */
		Player _player;
		
		/**
		 * Constructor for SocialTask.
		 * @param player Player
		 */
		SocialTask(Player player)
		{
			_player = player;
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			_player.stopParalyzed();
		}
	}
}
