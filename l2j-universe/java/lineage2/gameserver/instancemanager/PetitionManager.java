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
package lineage2.gameserver.instancemanager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import lineage2.gameserver.Config;
import lineage2.gameserver.handler.petition.IPetitionHandler;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.World;
import lineage2.gameserver.network.serverpackets.L2GameServerPacket;
import lineage2.gameserver.network.serverpackets.NpcHtmlMessage;
import lineage2.gameserver.network.serverpackets.Say2;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.network.serverpackets.components.ChatType;
import lineage2.gameserver.tables.GmListTable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class PetitionManager implements IPetitionHandler
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(PetitionManager.class.getName());
	
	/**
	 * @author Mobius
	 */
	public static enum PetitionState
	{
		/**
		 * Field Pending.
		 */
		Pending,
		/**
		 * Field Responder_Cancel.
		 */
		Responder_Cancel,
		/**
		 * Field Responder_Missing.
		 */
		Responder_Missing,
		/**
		 * Field Responder_Reject.
		 */
		Responder_Reject,
		/**
		 * Field Responder_Complete.
		 */
		Responder_Complete,
		/**
		 * Field Petitioner_Cancel.
		 */
		Petitioner_Cancel,
		/**
		 * Field Petitioner_Missing.
		 */
		Petitioner_Missing,
		/**
		 * Field In_Process.
		 */
		In_Process,
		/**
		 * Field Completed.
		 */
		Completed
	}
	
	/**
	 * @author Mobius
	 */
	public static enum PetitionType
	{
		/**
		 * Field Immobility.
		 */
		Immobility,
		/**
		 * Field Recovery_Related.
		 */
		Recovery_Related,
		/**
		 * Field Bug_Report.
		 */
		Bug_Report,
		/**
		 * Field Quest_Related.
		 */
		Quest_Related,
		/**
		 * Field Bad_User.
		 */
		Bad_User,
		/**
		 * Field Suggestions.
		 */
		Suggestions,
		/**
		 * Field Game_Tip.
		 */
		Game_Tip,
		/**
		 * Field Operation_Related.
		 */
		Operation_Related,
		/**
		 * Field Other.
		 */
		Other
	}
	
	/**
	 * Field _instance.
	 */
	private static final PetitionManager _instance = new PetitionManager();
	
	/**
	 * Method getInstance.
	 * @return PetitionManager
	 */
	public static final PetitionManager getInstance()
	{
		return _instance;
	}
	
	/**
	 * Field _nextId.
	 */
	private final AtomicInteger _nextId = new AtomicInteger();
	/**
	 * Field _pendingPetitions.
	 */
	private final Map<Integer, Petition> _pendingPetitions = new ConcurrentHashMap<>();
	/**
	 * Field _completedPetitions.
	 */
	private final Map<Integer, Petition> _completedPetitions = new ConcurrentHashMap<>();
	
	/**
	 * @author Mobius
	 */
	private class Petition
	{
		/**
		 * Field _submitTime.
		 */
		private final long _submitTime = System.currentTimeMillis();
		/**
		 * Field _endTime.
		 */
		private long _endTime = -1;
		/**
		 * Field _id.
		 */
		private final int _id;
		/**
		 * Field _type.
		 */
		private final PetitionType _type;
		/**
		 * Field _state.
		 */
		private PetitionState _state = PetitionState.Pending;
		/**
		 * Field _content.
		 */
		private final String _content;
		/**
		 * Field _messageLog.
		 */
		private final List<Say2> _messageLog = new ArrayList<>();
		/**
		 * Field _petitioner.
		 */
		private final int _petitioner;
		/**
		 * Field _responder.
		 */
		private int _responder;
		
		/**
		 * Constructor for Petition.
		 * @param petitioner Player
		 * @param petitionText String
		 * @param petitionType int
		 */
		public Petition(Player petitioner, String petitionText, int petitionType)
		{
			_id = getNextId();
			_type = PetitionType.values()[petitionType - 1];
			_content = petitionText;
			_petitioner = petitioner.getObjectId();
		}
		
		/**
		 * Method addLogMessage.
		 * @param cs Say2
		 * @return boolean
		 */
		protected boolean addLogMessage(Say2 cs)
		{
			return _messageLog.add(cs);
		}
		
		/**
		 * Method getLogMessages.
		 * @return List<Say2>
		 */
		protected List<Say2> getLogMessages()
		{
			return _messageLog;
		}
		
		/**
		 * Method endPetitionConsultation.
		 * @param endState PetitionState
		 * @return boolean
		 */
		public boolean endPetitionConsultation(PetitionState endState)
		{
			setState(endState);
			_endTime = System.currentTimeMillis();
			if ((getResponder() != null) && getResponder().isOnline())
			{
				if (endState == PetitionState.Responder_Reject)
				{
					getPetitioner().sendMessage("Your petition was rejected. Please try again later.");
				}
				else
				{
					getResponder().sendPacket(new SystemMessage(SystemMessage.ENDING_PETITION_CONSULTATION_WITH_S1).addString(getPetitioner().getName()));
					if (endState == PetitionState.Petitioner_Cancel)
					{
						getResponder().sendPacket(new SystemMessage(SystemMessage.RECEIPT_NO_S1_PETITION_CANCELLED).addNumber(getId()));
					}
				}
			}
			if ((getPetitioner() != null) && getPetitioner().isOnline())
			{
				getPetitioner().sendPacket(new SystemMessage(SystemMessage.ENDING_PETITION_CONSULTATION));
			}
			getCompletedPetitions().put(getId(), this);
			return getPendingPetitions().remove(getId()) != null;
		}
		
		/**
		 * Method getContent.
		 * @return String
		 */
		public String getContent()
		{
			return _content;
		}
		
		/**
		 * Method getId.
		 * @return int
		 */
		public int getId()
		{
			return _id;
		}
		
		/**
		 * Method getPetitioner.
		 * @return Player
		 */
		public Player getPetitioner()
		{
			return World.getPlayer(_petitioner);
		}
		
		/**
		 * Method getResponder.
		 * @return Player
		 */
		public Player getResponder()
		{
			return World.getPlayer(_responder);
		}
		
		/**
		 * Method getEndTime.
		 * @return long
		 */
		@SuppressWarnings("unused")
		public long getEndTime()
		{
			return _endTime;
		}
		
		/**
		 * Method getSubmitTime.
		 * @return long
		 */
		public long getSubmitTime()
		{
			return _submitTime;
		}
		
		/**
		 * Method getState.
		 * @return PetitionState
		 */
		public PetitionState getState()
		{
			return _state;
		}
		
		/**
		 * Method getTypeAsString.
		 * @return String
		 */
		public String getTypeAsString()
		{
			return _type.toString().replace("_", " ");
		}
		
		/**
		 * Method sendPetitionerPacket.
		 * @param responsePacket L2GameServerPacket
		 */
		public void sendPetitionerPacket(L2GameServerPacket responsePacket)
		{
			if ((getPetitioner() == null) || !getPetitioner().isOnline())
			{
				return;
			}
			getPetitioner().sendPacket(responsePacket);
		}
		
		/**
		 * Method sendResponderPacket.
		 * @param responsePacket L2GameServerPacket
		 */
		public void sendResponderPacket(L2GameServerPacket responsePacket)
		{
			if ((getResponder() == null) || !getResponder().isOnline())
			{
				endPetitionConsultation(PetitionState.Responder_Missing);
				return;
			}
			getResponder().sendPacket(responsePacket);
		}
		
		/**
		 * Method setState.
		 * @param state PetitionState
		 */
		public void setState(PetitionState state)
		{
			_state = state;
		}
		
		/**
		 * Method setResponder.
		 * @param responder Player
		 */
		public void setResponder(Player responder)
		{
			if (getResponder() != null)
			{
				return;
			}
			_responder = responder.getObjectId();
		}
	}
	
	/**
	 * Constructor for PetitionManager.
	 */
	private PetitionManager()
	{
		_log.info("Initializing PetitionManager");
	}
	
	/**
	 * Method getNextId.
	 * @return int
	 */
	public int getNextId()
	{
		return _nextId.incrementAndGet();
	}
	
	/**
	 * Method clearCompletedPetitions.
	 */
	public void clearCompletedPetitions()
	{
		int numPetitions = getPendingPetitionCount();
		getCompletedPetitions().clear();
		_log.info("PetitionManager: Completed petition data cleared. " + numPetitions + " petition(s) removed.");
	}
	
	/**
	 * Method clearPendingPetitions.
	 */
	public void clearPendingPetitions()
	{
		int numPetitions = getPendingPetitionCount();
		getPendingPetitions().clear();
		_log.info("PetitionManager: Pending petition queue cleared. " + numPetitions + " petition(s) removed.");
	}
	
	/**
	 * Method acceptPetition.
	 * @param respondingAdmin Player
	 * @param petitionId int
	 * @return boolean
	 */
	public boolean acceptPetition(Player respondingAdmin, int petitionId)
	{
		if (!isValidPetition(petitionId))
		{
			return false;
		}
		Petition currPetition = getPendingPetitions().get(petitionId);
		if (currPetition.getResponder() != null)
		{
			return false;
		}
		currPetition.setResponder(respondingAdmin);
		currPetition.setState(PetitionState.In_Process);
		currPetition.sendPetitionerPacket(new SystemMessage(SystemMessage.PETITION_APPLICATION_ACCEPTED));
		currPetition.sendResponderPacket(new SystemMessage(SystemMessage.PETITION_APPLICATION_ACCEPTED_RECEIPT_NO_IS_S1).addNumber(currPetition.getId()));
		currPetition.sendResponderPacket(new SystemMessage(SystemMessage.PETITION_CONSULTATION_WITH_S1_UNDER_WAY).addString(currPetition.getPetitioner().getName()));
		return true;
	}
	
	/**
	 * Method cancelActivePetition.
	 * @param player Player
	 * @return boolean
	 */
	public boolean cancelActivePetition(Player player)
	{
		for (Petition currPetition : getPendingPetitions().values())
		{
			if ((currPetition.getPetitioner() != null) && (currPetition.getPetitioner().getObjectId() == player.getObjectId()))
			{
				return currPetition.endPetitionConsultation(PetitionState.Petitioner_Cancel);
			}
			if ((currPetition.getResponder() != null) && (currPetition.getResponder().getObjectId() == player.getObjectId()))
			{
				return currPetition.endPetitionConsultation(PetitionState.Responder_Cancel);
			}
		}
		return false;
	}
	
	/**
	 * Method checkPetitionMessages.
	 * @param petitioner Player
	 */
	public void checkPetitionMessages(Player petitioner)
	{
		if (petitioner != null)
		{
			for (Petition currPetition : getPendingPetitions().values())
			{
				if (currPetition == null)
				{
					continue;
				}
				if ((currPetition.getPetitioner() != null) && (currPetition.getPetitioner().getObjectId() == petitioner.getObjectId()))
				{
					for (Say2 logMessage : currPetition.getLogMessages())
					{
						petitioner.sendPacket(logMessage);
					}
					return;
				}
			}
		}
	}
	
	/**
	 * Method endActivePetition.
	 * @param player Player
	 * @return boolean
	 */
	public boolean endActivePetition(Player player)
	{
		if (!player.isGM())
		{
			return false;
		}
		for (Petition currPetition : getPendingPetitions().values())
		{
			if (currPetition == null)
			{
				continue;
			}
			if ((currPetition.getResponder() != null) && (currPetition.getResponder().getObjectId() == player.getObjectId()))
			{
				return currPetition.endPetitionConsultation(PetitionState.Completed);
			}
		}
		return false;
	}
	
	/**
	 * Method getCompletedPetitions.
	 * @return Map<Integer,Petition>
	 */
	protected Map<Integer, Petition> getCompletedPetitions()
	{
		return _completedPetitions;
	}
	
	/**
	 * Method getPendingPetitions.
	 * @return Map<Integer,Petition>
	 */
	protected Map<Integer, Petition> getPendingPetitions()
	{
		return _pendingPetitions;
	}
	
	/**
	 * Method getPendingPetitionCount.
	 * @return int
	 */
	public int getPendingPetitionCount()
	{
		return getPendingPetitions().size();
	}
	
	/**
	 * Method getPlayerTotalPetitionCount.
	 * @param player Player
	 * @return int
	 */
	public int getPlayerTotalPetitionCount(Player player)
	{
		if (player == null)
		{
			return 0;
		}
		int petitionCount = 0;
		for (Petition currPetition : getPendingPetitions().values())
		{
			if (currPetition == null)
			{
				continue;
			}
			if ((currPetition.getPetitioner() != null) && (currPetition.getPetitioner().getObjectId() == player.getObjectId()))
			{
				petitionCount++;
			}
		}
		for (Petition currPetition : getCompletedPetitions().values())
		{
			if (currPetition == null)
			{
				continue;
			}
			if ((currPetition.getPetitioner() != null) && (currPetition.getPetitioner().getObjectId() == player.getObjectId()))
			{
				petitionCount++;
			}
		}
		return petitionCount;
	}
	
	/**
	 * Method isPetitionInProcess.
	 * @return boolean
	 */
	public boolean isPetitionInProcess()
	{
		for (Petition currPetition : getPendingPetitions().values())
		{
			if (currPetition == null)
			{
				continue;
			}
			if (currPetition.getState() == PetitionState.In_Process)
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Method isPetitionInProcess.
	 * @param petitionId int
	 * @return boolean
	 */
	public boolean isPetitionInProcess(int petitionId)
	{
		if (!isValidPetition(petitionId))
		{
			return false;
		}
		Petition currPetition = getPendingPetitions().get(petitionId);
		return currPetition.getState() == PetitionState.In_Process;
	}
	
	/**
	 * Method isPlayerInConsultation.
	 * @param player Player
	 * @return boolean
	 */
	public boolean isPlayerInConsultation(Player player)
	{
		if (player != null)
		{
			for (Petition currPetition : getPendingPetitions().values())
			{
				if (currPetition == null)
				{
					continue;
				}
				if (currPetition.getState() != PetitionState.In_Process)
				{
					continue;
				}
				if (((currPetition.getPetitioner() != null) && (currPetition.getPetitioner().getObjectId() == player.getObjectId())) || ((currPetition.getResponder() != null) && (currPetition.getResponder().getObjectId() == player.getObjectId())))
				{
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Method isPetitioningAllowed.
	 * @return boolean
	 */
	public boolean isPetitioningAllowed()
	{
		return Config.PETITIONING_ALLOWED;
	}
	
	/**
	 * Method isPlayerPetitionPending.
	 * @param petitioner Player
	 * @return boolean
	 */
	public boolean isPlayerPetitionPending(Player petitioner)
	{
		if (petitioner != null)
		{
			for (Petition currPetition : getPendingPetitions().values())
			{
				if (currPetition == null)
				{
					continue;
				}
				if ((currPetition.getPetitioner() != null) && (currPetition.getPetitioner().getObjectId() == petitioner.getObjectId()))
				{
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Method isValidPetition.
	 * @param petitionId int
	 * @return boolean
	 */
	private boolean isValidPetition(int petitionId)
	{
		return getPendingPetitions().containsKey(petitionId);
	}
	
	/**
	 * Method rejectPetition.
	 * @param respondingAdmin Player
	 * @param petitionId int
	 * @return boolean
	 */
	public boolean rejectPetition(Player respondingAdmin, int petitionId)
	{
		if (!isValidPetition(petitionId))
		{
			return false;
		}
		Petition currPetition = getPendingPetitions().get(petitionId);
		if (currPetition.getResponder() != null)
		{
			return false;
		}
		currPetition.setResponder(respondingAdmin);
		return currPetition.endPetitionConsultation(PetitionState.Responder_Reject);
	}
	
	/**
	 * Method sendActivePetitionMessage.
	 * @param player Player
	 * @param messageText String
	 * @return boolean
	 */
	public boolean sendActivePetitionMessage(Player player, String messageText)
	{
		Say2 cs;
		for (Petition currPetition : getPendingPetitions().values())
		{
			if (currPetition == null)
			{
				continue;
			}
			if ((currPetition.getPetitioner() != null) && (currPetition.getPetitioner().getObjectId() == player.getObjectId()))
			{
				cs = new Say2(player.getObjectId(), ChatType.PETITION_PLAYER, player.getName(), messageText);
				currPetition.addLogMessage(cs);
				currPetition.sendResponderPacket(cs);
				currPetition.sendPetitionerPacket(cs);
				return true;
			}
			if ((currPetition.getResponder() != null) && (currPetition.getResponder().getObjectId() == player.getObjectId()))
			{
				cs = new Say2(player.getObjectId(), ChatType.PETITION_GM, player.getName(), messageText);
				currPetition.addLogMessage(cs);
				currPetition.sendResponderPacket(cs);
				currPetition.sendPetitionerPacket(cs);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Method sendPendingPetitionList.
	 * @param activeChar Player
	 */
	public void sendPendingPetitionList(Player activeChar)
	{
		final StringBuilder htmlContent = new StringBuilder(600 + (getPendingPetitionCount() * 300));
		htmlContent.append("<html><body><center><table width=270><tr>" + "<td width=45><button value=\"Main\" action=\"bypass -h admin_admin\" width=45 height=21 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>" + "<td width=180><center>Petition Menu</center></td>" + "<td width=45><button value=\"Back\" action=\"bypass -h admin_admin\" width=45 height=21 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr></table><br>" + "<table width=\"270\">" + "<tr><td><table width=\"270\"><tr><td><button value=\"Reset\" action=\"bypass -h admin_reset_petitions\" width=\"80\" height=\"21\" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>" + "<td align=right><button value=\"Refresh\" action=\"bypass -h admin_view_petitions\" width=\"80\" height=\"21\" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr></table><br></td></tr>");
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		if (getPendingPetitionCount() == 0)
		{
			htmlContent.append("<tr><td>There are no currently pending petitions.</td></tr>");
		}
		else
		{
			htmlContent.append("<tr><td><font color=\"LEVEL\">Current Petitions:</font><br></td></tr>");
		}
		boolean color = true;
		int petcount = 0;
		for (Petition currPetition : getPendingPetitions().values())
		{
			if (currPetition == null)
			{
				continue;
			}
			htmlContent.append("<tr><td width=\"270\"><table width=\"270\" cellpadding=\"2\" bgcolor=").append(color ? "131210" : "444444").append("><tr><td width=\"130\">").append(dateFormat.format(new Date(currPetition.getSubmitTime())));
			htmlContent.append("</td><td width=\"140\" align=right><font color=\"").append(currPetition.getPetitioner().isOnline() ? "00FF00" : "999999").append("\">").append(currPetition.getPetitioner().getName()).append("</font></td></tr>");
			htmlContent.append("<tr><td width=\"130\">");
			if (currPetition.getState() != PetitionState.In_Process)
			{
				htmlContent.append("<table width=\"130\" cellpadding=\"2\"><tr><td><button value=\"View\" action=\"bypass -h admin_view_petition ").append(currPetition.getId()).append("\" width=\"50\" height=\"21\" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td><td><button value=\"Reject\" action=\"bypass -h admin_reject_petition ").append(currPetition.getId()).append("\" width=\"50\" height=\"21\" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr></table>");
			}
			else
			{
				htmlContent.append("<font color=\"").append(currPetition.getResponder().isOnline() ? "00FF00" : "999999").append("\">").append(currPetition.getResponder().getName()).append("</font>");
			}
			htmlContent.append("</td>").append(currPetition.getTypeAsString()).append("<td width=\"140\" align=right>").append(currPetition.getTypeAsString()).append("</td></tr></table></td></tr>");
			color = !color;
			petcount++;
			if (petcount > 10)
			{
				htmlContent.append("<tr><td><font color=\"LEVEL\">There is more pending petition...</font><br></td></tr>");
				break;
			}
		}
		htmlContent.append("</table></center></body></html>");
		NpcHtmlMessage htmlMsg = new NpcHtmlMessage(0);
		htmlMsg.setHtml(htmlContent.toString());
		activeChar.sendPacket(htmlMsg);
	}
	
	/**
	 * Method submitPetition.
	 * @param petitioner Player
	 * @param petitionText String
	 * @param petitionType int
	 * @return int
	 */
	public int submitPetition(Player petitioner, String petitionText, int petitionType)
	{
		Petition newPetition = new Petition(petitioner, petitionText, petitionType);
		int newPetitionId = newPetition.getId();
		getPendingPetitions().put(newPetitionId, newPetition);
		String msgContent = petitioner.getName() + " has submitted a new petition.";
		GmListTable.broadcastToGMs(new Say2(petitioner.getObjectId(), ChatType.HERO_VOICE, "Petition System", msgContent));
		return newPetitionId;
	}
	
	/**
	 * Method viewPetition.
	 * @param activeChar Player
	 * @param petitionId int
	 */
	public void viewPetition(Player activeChar, int petitionId)
	{
		if (!activeChar.isGM())
		{
			return;
		}
		if (!isValidPetition(petitionId))
		{
			return;
		}
		Petition currPetition = getPendingPetitions().get(petitionId);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		NpcHtmlMessage html = new NpcHtmlMessage(0);
		html.setFile("admin/petition.htm");
		html.replace("%petition%", String.valueOf(currPetition.getId()));
		html.replace("%time%", dateFormat.format(new Date(currPetition.getSubmitTime())));
		html.replace("%type%", currPetition.getTypeAsString());
		html.replace("%petitioner%", currPetition.getPetitioner().getName());
		html.replace("%online%", (currPetition.getPetitioner().isOnline() ? "00FF00" : "999999"));
		html.replace("%text%", currPetition.getContent());
		activeChar.sendPacket(html);
	}
	
	/**
	 * Method handle.
	 * @param player Player
	 * @param id int
	 * @param txt String
	 * @see lineage2.gameserver.handler.petition.IPetitionHandler#handle(Player, int, String)
	 */
	@Override
	public void handle(Player player, int id, String txt)
	{
		if (GmListTable.getAllVisibleGMs().size() == 0)
		{
			player.sendPacket(new SystemMessage(SystemMessage.THERE_ARE_NOT_ANY_GMS_THAT_ARE_PROVIDING_CUSTOMER_SERVICE_CURRENTLY));
			return;
		}
		if (!PetitionManager.getInstance().isPetitioningAllowed())
		{
			player.sendPacket(new SystemMessage(SystemMessage.CANNOT_CONNECT_TO_PETITION_SERVER));
			return;
		}
		if (PetitionManager.getInstance().isPlayerPetitionPending(player))
		{
			player.sendPacket(new SystemMessage(SystemMessage.ALREADY_APPLIED_FOR_PETITION));
			return;
		}
		if (PetitionManager.getInstance().getPendingPetitionCount() == Config.MAX_PETITIONS_PENDING)
		{
			player.sendPacket(new SystemMessage(SystemMessage.THE_PETITION_SYSTEM_IS_CURRENTLY_UNAVAILABLE_PLEASE_TRY_AGAIN_LATER));
			return;
		}
		int totalPetitions = PetitionManager.getInstance().getPlayerTotalPetitionCount(player) + 1;
		if (totalPetitions > Config.MAX_PETITIONS_PER_PLAYER)
		{
			player.sendPacket(new SystemMessage(SystemMessage.WE_HAVE_RECEIVED_S1_PETITIONS_FROM_YOU_TODAY_AND_THAT_IS_THE_MAXIMUM_THAT_YOU_CAN_SUBMIT_IN_ONE_DAY_YOU_CANNOT_SUBMIT_ANY_MORE_PETITIONS));
			return;
		}
		if (txt.length() > 255)
		{
			player.sendPacket(new SystemMessage(SystemMessage.PETITIONS_CANNOT_EXCEED_255_CHARACTERS));
			return;
		}
		if (id >= PetitionManager.PetitionType.values().length)
		{
			_log.warn("PetitionManager: Invalid petition type : " + id);
			return;
		}
		int petitionId = PetitionManager.getInstance().submitPetition(player, txt, id);
		player.sendPacket(new SystemMessage(SystemMessage.PETITION_APPLICATION_ACCEPTED_RECEIPT_NO_IS_S1).addNumber(petitionId));
		player.sendPacket(new SystemMessage(SystemMessage.YOU_HAVE_SUBMITTED_S1_PETITIONS_YOU_MAY_SUBMIT_S2_MORE_PETITIONS_TODAY).addNumber(totalPetitions).addNumber(Config.MAX_PETITIONS_PER_PLAYER - totalPetitions));
		player.sendPacket(new SystemMessage(SystemMessage.THERE_ARE_S1_PETITIONS_PENDING).addNumber(PetitionManager.getInstance().getPendingPetitionCount()));
	}
}
