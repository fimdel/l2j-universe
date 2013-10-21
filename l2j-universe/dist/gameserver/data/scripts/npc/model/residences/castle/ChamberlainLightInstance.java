package npc.model.residences.castle;

import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

import npc.model.residences.ResidenceManager;

import lineage2.gameserver.dao.CastleDamageZoneDAO;
import lineage2.gameserver.dao.CastleDoorUpgradeDAO;
import lineage2.gameserver.data.xml.holder.ResidenceHolder;
import lineage2.gameserver.instancemanager.CastleManorManager;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.events.impl.CastleSiegeEvent;
import lineage2.gameserver.model.entity.events.impl.SiegeEvent;
import lineage2.gameserver.model.entity.events.objects.CastleDamageZoneObject;
import lineage2.gameserver.model.entity.events.objects.DoorObject;
import lineage2.gameserver.model.entity.residence.Castle;
import lineage2.gameserver.model.entity.residence.Fortress;
import lineage2.gameserver.model.entity.residence.Residence;
import lineage2.gameserver.model.instances.DoorInstance;
import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.model.pledge.Privilege;
import lineage2.gameserver.network.serverpackets.CastleSiegeInfo;
import lineage2.gameserver.network.serverpackets.ExShowCropInfo;
import lineage2.gameserver.network.serverpackets.ExShowCropSetting;
import lineage2.gameserver.network.serverpackets.ExShowManorDefaultInfo;
import lineage2.gameserver.network.serverpackets.ExShowSeedInfo;
import lineage2.gameserver.network.serverpackets.ExShowSeedSetting;
import lineage2.gameserver.network.serverpackets.L2GameServerPacket;
import lineage2.gameserver.network.serverpackets.NpcHtmlMessage;
import lineage2.gameserver.network.serverpackets.components.CustomMessage;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.templates.item.ItemTemplate;
import lineage2.gameserver.templates.manor.CropProcure;
import lineage2.gameserver.templates.manor.SeedProduction;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.HtmlUtils;
import lineage2.gameserver.utils.ItemFunctions;
import lineage2.gameserver.utils.Log;
import lineage2.gameserver.utils.ReflectionUtils;
import org.napile.primitive.maps.IntObjectMap;

public class ChamberlainLightInstance extends ResidenceManager
{
	/**
	 *
	 */
	private static final long serialVersionUID = 7889598664307011523L;

	public ChamberlainLightInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}

	@Override
	protected void setDialogs()
	{
		_mainDialog = "castle/chamberlain/chamberlain.htm";
		_failDialog = "castle/chamberlain/chamberlain-notlord.htm";
		_siegeDialog = _mainDialog;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void onBypassFeedback(Player player, String command)
	{
		if(!canBypassCheck(player, this))
			return;

		int condition = getCond(player);
		if(condition != COND_OWNER)
			return;

		StringTokenizer st = new StringTokenizer(command, " ");
		String actualCommand = st.nextToken();
		String val = "";
		if(st.countTokens() >= 1)
			val = st.nextToken();

		Castle castle = getCastle();
		if(actualCommand.equalsIgnoreCase("viewSiegeInfo"))
		{
			if(!isHaveRigths(player, Clan.CP_CS_MANAGE_SIEGE))
			{
				player.sendPacket(SystemMsg.YOU_ARE_NOT_AUTHORIZED_TO_DO_THAT);
				return;
			}
			player.sendPacket(new CastleSiegeInfo(castle, player));
		}
		else if(actualCommand.equalsIgnoreCase("ManageTreasure"))
		{
			if(!player.isClanLeader())
			{
				player.sendPacket(SystemMsg.YOU_ARE_NOT_AUTHORIZED_TO_DO_THAT);
				return;
			}
			NpcHtmlMessage html = new NpcHtmlMessage(player, this);
			html.setFile("castle/chamberlain/chamberlain-castlevault.htm");
			html.replace("%Treasure%", String.valueOf(castle.getTreasury()));
			html.replace("%CollectedShops%", String.valueOf(castle.getCollectedShops()));
			html.replace("%CollectedSeed%", String.valueOf(castle.getCollectedSeed()));
			player.sendPacket(html);
		}
		else if(actualCommand.equalsIgnoreCase("TakeTreasure"))
		{
			if(!player.isClanLeader())
			{
				player.sendPacket(SystemMsg.YOU_ARE_NOT_AUTHORIZED_TO_DO_THAT);
				return;
			}
			if(!val.equals(""))
			{
				long treasure = Long.parseLong(val);
				if(castle.getTreasury() < treasure)
				{
					NpcHtmlMessage html = new NpcHtmlMessage(player, this);
					html.setFile("castle/chamberlain/chamberlain-havenottreasure.htm");
					html.replace("%Treasure%", String.valueOf(castle.getTreasury()));
					html.replace("%Requested%", String.valueOf(treasure));
					player.sendPacket(html);
					return;
				}
				if(treasure > 0)
				{
					castle.addToTreasuryNoTax(-treasure, false, false);
					Log.add(castle.getName() + "|" + -treasure + "|CastleChamberlain", "treasury");
					player.addAdena(treasure);
				}
			}

			NpcHtmlMessage html = new NpcHtmlMessage(player, this);
			html.setFile("castle/chamberlain/chamberlain-castlevault.htm");
			html.replace("%Treasure%", String.valueOf(castle.getTreasury()));
			html.replace("%CollectedShops%", String.valueOf(castle.getCollectedShops()));
			html.replace("%CollectedSeed%", String.valueOf(castle.getCollectedSeed()));
			player.sendPacket(html);
		}
		else if(actualCommand.equalsIgnoreCase("PutTreasure"))
		{
			if(!val.equals(""))
			{
				long treasure = Long.parseLong(val);
				if(treasure > player.getAdena())
				{
					player.sendPacket(SystemMsg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
					return;
				}
				if(treasure > 0)
				{
					castle.addToTreasuryNoTax(treasure, false, false);
					Log.add(castle.getName() + "|" + treasure + "|CastleChamberlain", "treasury");
					player.reduceAdena(treasure, true);
				}
			}

			NpcHtmlMessage html = new NpcHtmlMessage(player, this);
			html.setFile("castle/chamberlain/chamberlain-castlevault.htm");
			html.replace("%Treasure%", String.valueOf(castle.getTreasury()));
			html.replace("%CollectedShops%", String.valueOf(castle.getCollectedShops()));
			html.replace("%CollectedSeed%", String.valueOf(castle.getCollectedSeed()));
			player.sendPacket(html);
		}
		else if(actualCommand.equalsIgnoreCase("manor"))
		{
			if(!isHaveRigths(player, Clan.CP_CS_MANOR_ADMIN))
			{
				player.sendPacket(SystemMsg.YOU_ARE_NOT_AUTHORIZED_TO_DO_THAT);
				return;
			}
			String filename = "";
			if(CastleManorManager.getInstance().isDisabled())
				filename = "npcdefault.htm";
			else
			{
				int cmd = Integer.parseInt(val);
				switch(cmd)
				{
					case 0:
						filename = "castle/chamberlain/manor/manor.htm";
						break;
					// TODO: correct in html's to 1
					case 4:
						filename = "castle/chamberlain/manor/manor_help00" + st.nextToken() + ".htm";
						break;
					default:
						filename = "castle/chamberlain/chamberlain-no.htm";
						break;
				}
			}

			if(filename.length() > 0)
			{
				NpcHtmlMessage html = new NpcHtmlMessage(player, this);
				html.setFile(filename);
				player.sendPacket(html);
			}
		}
		else if(actualCommand.startsWith("manor_menu_select"))
		{
			if(!isHaveRigths(player, Clan.CP_CS_MANOR_ADMIN))
			{
				player.sendPacket(SystemMsg.YOU_ARE_NOT_AUTHORIZED_TO_DO_THAT);
				return;
			}
			// input string format:
			// manor_menu_select?ask=X&state=Y&time=X
			if(CastleManorManager.getInstance().isUnderMaintenance())
			{
				player.sendPacket(SystemMsg.THE_MANOR_SYSTEM_IS_CURRENTLY_UNDER_MAINTENANCE);
				player.sendActionFailed();
				return;
			}

			String params = actualCommand.substring(actualCommand.indexOf("?") + 1);
			StringTokenizer str = new StringTokenizer(params, "&");
			int ask = Integer.parseInt(str.nextToken().split("=")[1]);
			int state = Integer.parseInt(str.nextToken().split("=")[1]);
			int time = Integer.parseInt(str.nextToken().split("=")[1]);

			int castleId;
			if(state == -1) // info for current manor
				castleId = castle.getId();
			else
				// info for requested manor
				castleId = state;

			switch(ask)
			{ // Main action
				case 3: // Current seeds (Manor info)
					if(time == 1 && !ResidenceHolder.getInstance().getResidence(Castle.class, castleId).isNextPeriodApproved())
						player.sendPacket(new ExShowSeedInfo(castleId, Collections.<SeedProduction> emptyList()));
					else
						player.sendPacket(new ExShowSeedInfo(castleId, ResidenceHolder.getInstance().getResidence(Castle.class, castleId).getSeedProduction(time)));
					break;
				case 4: // Current crops (Manor info)
					if(time == 1 && !ResidenceHolder.getInstance().getResidence(Castle.class, castleId).isNextPeriodApproved())
						player.sendPacket(new ExShowCropInfo(castleId, Collections.<CropProcure> emptyList()));
					else
						player.sendPacket(new ExShowCropInfo(castleId, ResidenceHolder.getInstance().getResidence(Castle.class, castleId).getCropProcure(time)));
					break;
				case 5: // Basic info (Manor info)
					player.sendPacket(new ExShowManorDefaultInfo());
					break;
				case 7: // Edit seed setup
					if(castle.isNextPeriodApproved())
						player.sendPacket(SystemMsg.A_MANOR_CANNOT_BE_SET_UP_BETWEEN_430_AM_AND_8_PM);
					else
						player.sendPacket(new ExShowSeedSetting(castle.getId()));
					break;
				case 8: // Edit crop setup
					if(castle.isNextPeriodApproved())
						player.sendPacket(SystemMsg.A_MANOR_CANNOT_BE_SET_UP_BETWEEN_430_AM_AND_8_PM);
					else
						player.sendPacket(new ExShowCropSetting(castle.getId()));
					break;
			}
		}
		else if(actualCommand.equalsIgnoreCase("operate_door")) // door control
		{
			if(!isHaveRigths(player, Clan.CP_CS_ENTRY_EXIT))
			{
				player.sendPacket(SystemMsg.YOU_ARE_NOT_AUTHORIZED_TO_DO_THAT);
				return;
			}
			if(castle.getSiegeEvent().isInProgress())
			{
				showChatWindow(player, "residence2/castle/chamberlain_saius021.htm");
				return;
			}
			if(!val.equals(""))
			{
				boolean open = Integer.parseInt(val) == 1;
				while(st.hasMoreTokens())
				{
					DoorInstance door = ReflectionUtils.getDoor(Integer.parseInt(st.nextToken()));
					if(open)
						door.openMe(player, true);
					else
						door.closeMe(player, true);
				}
			}

			NpcHtmlMessage html = new NpcHtmlMessage(player, this);
			html.setFile("castle/chamberlain/" + getTemplate().npcId + "-d.htm");
			player.sendPacket(html);
		}
		else if(actualCommand.equalsIgnoreCase("tax_set")) // tax rates control
		{
			if(!isHaveRigths(player, Clan.CP_CS_TAXES))
			{
				player.sendPacket(SystemMsg.YOU_ARE_NOT_AUTHORIZED_TO_DO_THAT);
				return;
			}
			if(!val.equals(""))
			{
				int maxTax = 15;
				int tax = Integer.parseInt(val);
				if(tax < 0 || tax > maxTax)
				{
					NpcHtmlMessage html = new NpcHtmlMessage(player, this);
					html.setFile("castle/chamberlain/chamberlain-hightax.htm");
					html.replace("%CurrentTax%", String.valueOf(castle.getTaxPercent()));
					player.sendPacket(html);
					return;
				}
				castle.setTaxPercent(player, tax);
			}

			NpcHtmlMessage html = new NpcHtmlMessage(player, this);
			html.setFile("castle/chamberlain/chamberlain-settax.htm");
			html.replace("%CurrentTax%", String.valueOf(castle.getTaxPercent()));
			player.sendPacket(html);
		}
		else if(actualCommand.equalsIgnoreCase("upgrade_castle"))
		{
			if(!checkSiegeFunctions(player))
				return;

			showChatWindow(player, "castle/chamberlain/chamberlain-upgrades.htm");
		}
		else if(actualCommand.equalsIgnoreCase("reinforce"))
		{
			if(!checkSiegeFunctions(player))
				return;

			NpcHtmlMessage html = new NpcHtmlMessage(player, this);
			html.setFile("castle/chamberlain/doorStrengthen-" + castle.getName() + ".htm");
			player.sendPacket(html);
		}
		else if(actualCommand.equalsIgnoreCase("trap_select"))
		{
			if(!checkSiegeFunctions(player))
				return;

			NpcHtmlMessage html = new NpcHtmlMessage(player, this);
			html.setFile("castle/chamberlain/trap_select-" + castle.getName() + ".htm");
			player.sendPacket(html);
		}
		else if(actualCommand.equalsIgnoreCase("buy_trap"))
		{
			if(!checkSiegeFunctions(player))
				return;

			if(castle.getSiegeEvent().getObjects(CastleSiegeEvent.BOUGHT_ZONES).contains(val))
			{
				NpcHtmlMessage html = new NpcHtmlMessage(player, this);
				html.setFile("castle/chamberlain/trapAlready.htm");
				player.sendPacket(html);
				return;
			}

			List<CastleDamageZoneObject> objects = castle.getSiegeEvent().getObjects(val);
			long price = 0;
			for(CastleDamageZoneObject o : objects)
				price += o.getPrice();

			if(player.getClan().getAdenaCount() < price)
			{
				player.sendPacket(SystemMsg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
				return;
			}

			player.getClan().getWarehouse().destroyItemByItemId(ItemTemplate.ITEM_ID_ADENA, price);
			castle.getSiegeEvent().addObject(CastleSiegeEvent.BOUGHT_ZONES, val);
			CastleDamageZoneDAO.getInstance().insert(castle, val);

			NpcHtmlMessage html = new NpcHtmlMessage(player, this);
			html.setFile("castle/chamberlain/trapSuccess.htm");
			player.sendPacket(html);
		}
		else if(actualCommand.equalsIgnoreCase("door_manage"))
		{
			if(!isHaveRigths(player, Clan.CP_CS_ENTRY_EXIT))
			{
				player.sendPacket(SystemMsg.YOU_ARE_NOT_AUTHORIZED_TO_DO_THAT);
				return;
			}
			if(castle.getSiegeEvent().isInProgress())
			{
				showChatWindow(player, "residence2/castle/chamberlain_saius021.htm");
				return;
			}

			NpcHtmlMessage html = new NpcHtmlMessage(player, this);
			html.setFile("castle/chamberlain/doorManage.htm");
			html.replace("%id%", val);
			html.replace("%type%", st.nextToken());
			player.sendPacket(html);
		}
		else if(actualCommand.equalsIgnoreCase("upgrade_door_confirm"))
		{
			if(!isHaveRigths(player, Clan.CP_CS_MANAGE_SIEGE))
			{
				player.sendPacket(SystemMsg.YOU_ARE_NOT_AUTHORIZED_TO_DO_THAT);
				return;
			}
			int id = Integer.parseInt(val);
			int type = Integer.parseInt(st.nextToken());
			int level = Integer.parseInt(st.nextToken());
			long price = getDoorCost(type, level);

			NpcHtmlMessage html = new NpcHtmlMessage(player, this);
			html.setFile("castle/chamberlain/doorConfirm.htm");
			html.replace("%id%", String.valueOf(id));
			html.replace("%level%", String.valueOf(level));
			html.replace("%type%", String.valueOf(type));
			html.replace("%price%", String.valueOf(price));
			player.sendPacket(html);
		}
		else if(actualCommand.equalsIgnoreCase("upgrade_door"))
		{
			if(checkSiegeFunctions(player))
				return;

			int id = Integer.parseInt(val);
			int type = Integer.parseInt(st.nextToken());
			int level = Integer.parseInt(st.nextToken());
			long price = getDoorCost(type, level);

			List<DoorObject> doorObjects = castle.getSiegeEvent().getObjects(SiegeEvent.DOORS);
			DoorObject targetDoorObject = null;
			for(DoorObject o : doorObjects)
				if(o.getUId() == id)
				{
					targetDoorObject = o;
					break;
				}

			DoorInstance door = targetDoorObject.getDoor();
			int upgradeHp = (door.getMaxHp() - door.getUpgradeHp()) * level - door.getMaxHp();

			if(price == 0 || upgradeHp < 0)
			{
				player.sendMessage(new CustomMessage("common.Error", player));
				return;
			}

			if(door.getUpgradeHp() >= upgradeHp)
			{
				int oldLevel = door.getUpgradeHp() / (door.getMaxHp() - door.getUpgradeHp()) + 1;
				NpcHtmlMessage html = new NpcHtmlMessage(player, this);
				html.setFile("castle/chamberlain/doorAlready.htm");
				html.replace("%level%", String.valueOf(oldLevel));
				player.sendPacket(html);
				return;
			}

			if(player.getClan().getAdenaCount() < price)
			{
				player.sendPacket(SystemMsg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
				return;
			}

			player.getClan().getWarehouse().destroyItemByItemId(ItemTemplate.ITEM_ID_ADENA, price);

			targetDoorObject.setUpgradeValue(castle.<SiegeEvent<?, ?>> getSiegeEvent(), upgradeHp);
			CastleDoorUpgradeDAO.getInstance().insert(door.getDoorId(), upgradeHp);
		}
		else if(actualCommand.equalsIgnoreCase("report")) // Report page
		{
			if(!isHaveRigths(player, Clan.CP_CS_USE_FUNCTIONS))
			{
				player.sendPacket(SystemMsg.YOU_ARE_NOT_AUTHORIZED_TO_DO_THAT);
				return;
			}

			NpcHtmlMessage html = new NpcHtmlMessage(player, this);
			html.setFile("castle/chamberlain/chamberlain-report.htm");
			html.replaceNpcString("%FeudName%", castle.getNpcStringName());
			html.replace("%CharClan%", player.getClan().getName());
			html.replace("%CharName%", player.getName());
			player.sendPacket(html);
		}
		else if(actualCommand.equalsIgnoreCase("fortressStatus"))
		{
			NpcHtmlMessage html = new NpcHtmlMessage(player, this);
			html.setFile("castle/chamberlain/chamberlain-fortress-status.htm");
			StringBuilder b = new StringBuilder(100);

			for(IntObjectMap.Entry<List> entry : castle.getRelatedFortresses().entrySet())
			{
				NpcString type;
				switch(entry.getKey())
				{
					case Fortress.DOMAIN:
						type = NpcString.DOMAIN_FORTRESS;
						break;
					case Fortress.BOUNDARY:
						type = NpcString.BOUNDARY_FORTRESS;
						break;
					default:
						continue;
				}
				@SuppressWarnings("unchecked")
				List<Fortress> fortresses = entry.getValue();
				for(Fortress fort : fortresses)
				{
					b.append(HtmlUtils.htmlResidenceName(fort.getId())).append(" (").append(HtmlUtils.htmlNpcString(type)).append(") : <font color=\"00FFFF\">");

					NpcString contractType;
					switch(fort.getContractState())
					{
						case Fortress.NOT_DECIDED:
							contractType = NpcString.NONPARTISAN;
							break;
						case Fortress.INDEPENDENT:
							contractType = NpcString.INDEPENDENT_STATE;
							break;
						case Fortress.CONTRACT_WITH_CASTLE:
							contractType = NpcString.CONTRACT_STATE;
							break;
						default:
							continue;
					}
					b.append(HtmlUtils.htmlNpcString(contractType)).append("</font> <br>");
				}
			}
			html.replace("%list%", b.toString());
			player.sendPacket(html);
		}
		else if(actualCommand.equalsIgnoreCase("Crown")) // Give Crown to Castle Owner
		{
			if(!player.isClanLeader())
			{
				player.sendPacket(SystemMsg.YOU_ARE_NOT_AUTHORIZED_TO_DO_THAT);
				return;
			}
			if(player.getInventory().getItemByItemId(6841) == null)
			{
				player.getInventory().addItem(ItemFunctions.createItem(6841));

				NpcHtmlMessage html = new NpcHtmlMessage(player, this);
				html.setFile("castle/chamberlain/chamberlain-givecrown.htm");
				html.replace("%CharName%", player.getName());
				html.replaceNpcString("%FeudName%", castle.getNpcStringName());
				player.sendPacket(html);
			}
			else
			{
				NpcHtmlMessage html = new NpcHtmlMessage(player, this);
				html.setFile("castle/chamberlain/alreadyhavecrown.htm");
				player.sendPacket(html);
			}
		}
		else if(actualCommand.equalsIgnoreCase("Cloak")) // Give Radiant Cloak of Light to Castle Owner
		{
			if(!player.isClanLeader())
			{
				player.sendPacket(SystemMsg.YOU_ARE_NOT_AUTHORIZED_TO_DO_THAT);
				return;
			}
			if(player.getInventory().getItemByItemId(34996) == null)
			{
				player.getInventory().addItem(ItemFunctions.createItem(34996));

				NpcHtmlMessage html = new NpcHtmlMessage(player, this);
				html.setFile("castle/chamberlain/chamberlain-givecloak.htm");
				html.replace("%CharName%", player.getName());
				html.replaceNpcString("%FeudName%", castle.getNpcStringName());
				player.sendPacket(html);
			}
			else
			{
				NpcHtmlMessage html = new NpcHtmlMessage(player, this);
				html.setFile("castle/chamberlain/alreadyhavecloak.htm");
				player.sendPacket(html);
			}
		}
		else if(actualCommand.equalsIgnoreCase("manageFunctions"))
		{
			if(!player.hasPrivilege(Privilege.CS_FS_SET_FUNCTIONS))
				showChatWindow(player, "residence2/castle/chamberlain_saius063.htm");
			else
				showChatWindow(player, "residence2/castle/chamberlain_saius065.htm");
		}
		else if(actualCommand.equalsIgnoreCase("manageSiegeFunctions"))
		{
			if(!player.hasPrivilege(Privilege.CS_FS_SET_FUNCTIONS))
				showChatWindow(player, "residence2/castle/chamberlain_saius063.htm");
			else if(castle.getDomainFortressContract() == 0)
				showChatWindow(player, "residence2/castle/chamberlain_saius069.htm");
			else
				showChatWindow(player, "residence2/castle/chamberlain_saius052.htm");
		}
		else if(actualCommand.equalsIgnoreCase("items"))
		{
			NpcHtmlMessage html = new NpcHtmlMessage(player, this);
			html.setFile("residence2/castle/chamberlain_saius064.htm");
			html.replace("%npcId%", String.valueOf(getNpcId()));
			player.sendPacket(html);
		}
		else if(actualCommand.equalsIgnoreCase("default"))
		{
			NpcHtmlMessage html = new NpcHtmlMessage(player, this);
			html.setFile("castle/chamberlain/chamberlain.htm");
			player.sendPacket(html);
		}
		else
			super.onBypassFeedback(player, command);
	}

	@Override
	protected int getCond(Player player)
	{
		if(player.isGM())
			return COND_OWNER;
		Residence castle = getCastle();
		if(castle != null && castle.getId() > 0)
			if(player.getClan() != null)
				if(castle.getSiegeEvent().isInProgress())
					return COND_SIEGE; // Busy because of siege
				else if(castle.getOwnerId() == player.getClanId())
				{
					if(player.isClanLeader()) // Leader of clan
						return COND_OWNER;
					if(isHaveRigths(player, Clan.CP_CS_ENTRY_EXIT) || // doors
					isHaveRigths(player, Clan.CP_CS_MANOR_ADMIN) || // manor
					isHaveRigths(player, Clan.CP_CS_MANAGE_SIEGE) || // siege
					isHaveRigths(player, Clan.CP_CS_USE_FUNCTIONS) || // funcs
					isHaveRigths(player, Clan.CP_CS_DISMISS) || // banish
					isHaveRigths(player, Clan.CP_CS_TAXES) || // tax
					isHaveRigths(player, Clan.CP_CS_MERCENARIES) || // merc
					isHaveRigths(player, Clan.CP_CS_SET_FUNCTIONS) //funcs
					)
						return COND_OWNER;
				}

		return COND_FAIL;
	}

	private long getDoorCost(int type, int level)
	{
		int price = 0;

		switch(type)
		{
			case 1: // Главные ворота
				switch(level)
				{
					case 2:
						price = 3000000;
						break;
					case 3:
						price = 4000000;
						break;
					case 5:
						price = 5000000;
						break;
				}
				break;
			case 2: // Внутренние ворота
				switch(level)
				{
					case 2:
						price = 750000;
						break;
					case 3:
						price = 900000;
						break;
					case 5:
						price = 1000000;
						break;
				}
				break;
			case 3: // Стены
				switch(level)
				{
					case 2:
						price = 1600000;
						break;
					case 3:
						price = 1800000;
						break;
					case 5:
						price = 2000000;
						break;
				}
				break;
		}

		return price;
	}

	@Override
	protected Residence getResidence()
	{
		return getCastle();
	}

	@Override
	public L2GameServerPacket decoPacket()
	{
		return null;
	}

	@Override
	protected int getPrivUseFunctions()
	{
		return Clan.CP_CS_USE_FUNCTIONS;
	}

	@Override
	protected int getPrivSetFunctions()
	{
		return Clan.CP_CS_SET_FUNCTIONS;
	}

	@Override
	protected int getPrivDismiss()
	{
		return Clan.CP_CS_DISMISS;
	}

	@Override
	protected int getPrivDoors()
	{
		return Clan.CP_CS_ENTRY_EXIT;
	}

	private boolean checkSiegeFunctions(Player player)
	{
		Castle castle = getCastle();
		if(!player.hasPrivilege(Privilege.CS_FS_SIEGE_WAR))
		{
			player.sendPacket(SystemMsg.YOU_ARE_NOT_AUTHORIZED_TO_DO_THAT);
			return false;
		}

		if(castle.getSiegeEvent().isInProgress())
		{
			showChatWindow(player, "residence2/castle/chamberlain_saius021.htm");
			return false;
		}
		return true;
	}
}