package l2p.gameserver.model.instances;

import l2p.gameserver.templates.npc.NpcTemplate;

public class FunEventInstance extends NpcInstance {
    private static final long serialVersionUID = 6661350567450579991L;

    public FunEventInstance(int objectId, NpcTemplate template) {
        super(objectId, template);
    }
    /*
         @Override
         public void showChatWindow(Player player, int val, Object... arg)
         {
             if (player == null)
                 return;

             if (!player.getEventName().equals("") && !player.getEventName().equalsIgnoreCase(_event.EVENT_NAME))
             {
                 player.sendMessage("You are already registered in other Fun Event.");
                 player.sendPacket(ActionFailed.STATIC_PACKET);
                 return;
             }

             NpcHtmlMessage npcHtmlMessage = _event.getChatWindow(player);

             if (npcHtmlMessage != null)
             {
                 npcHtmlMessage.replace("%objectId%", String.valueOf(getObjectId()));
                 player.sendPacket(npcHtmlMessage);
             }

             player.sendPacket(ActionFailed.STATIC_PACKET);
         }

         @Override
         public void onBypassFeedback(Player player, String command)
         {
             StringTokenizer st = new StringTokenizer(command, " ");
             String currentCommand = st.nextToken();

             if (currentCommand.startsWith("join"))
             {
                 int joinTeamId = Integer.parseInt(st.nextToken());
                 _event.addPlayer(player, joinTeamId);
                 showChatWindow(player, 0);
             }
             else if (currentCommand.startsWith("leave"))
             {
                 _event.removePlayer(player);
                 showChatWindow(player, 0);
             }
             else
                 showChatWindow(player, 0);
         }*/
}
