package l2p.gameserver.model;


import l2p.commons.dbutils.DbUtils;
import l2p.commons.util.Rnd;
import l2p.gameserver.ThreadPoolManager;
import l2p.gameserver.database.DatabaseFactory;
import l2p.gameserver.listener.actor.npc.OnSpawnListener;
import l2p.gameserver.model.actor.listener.CharListenerList;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.network.serverpackets.NpcSayNative;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

/**
 * @author ALF
 *         Урезаный AutoChatHandler предназначен ТОЛЬКО для фраз НПЦ (серым цветом) и только ГЛОБАЛЬНЫХ!
 */
public class AutoChatHandler implements OnSpawnListener {
    private static final Logger _log = LoggerFactory.getLogger(AutoChatHandler.class);
    private static AutoChatHandler _instance;
    private static final long DEFAULT_CHAT_DELAY = 180000; // 3 mins by default
    private Map<Integer, AutoChatInstance> _registeredChats;
    private static final ConcurrentHashMap<Integer, String> _random_chat = new ConcurrentHashMap<Integer, String>();

    protected AutoChatHandler() {
        _registeredChats = new ConcurrentHashMap<Integer, AutoChatInstance>();
        restoreChatData();
        CharListenerList.addGlobal(this);
    }

    private void restoreChatData() {
        Connection con = null;
        PreparedStatement statement = null;
        ResultSet rset = null;
        try {
            con = DatabaseFactory.getInstance().getConnection();
            statement = con.prepareStatement("SELECT npcId,chatDelay,npcStringId FROM auto_chat");
            rset = statement.executeQuery();
            while (rset.next()) {
                _random_chat.put(rset.getInt("npcId"), rset.getString("npcStringId"));
                registerGlobalChat(rset.getInt("npcId"), (rset.getLong("chatDelay") + Rnd.get(5)) * 1000L);
            }
        } catch (Exception e) {
            _log.warn("AutoSpawnHandler: Could not restore chat data: " + e);
        } finally {
            DbUtils.closeQuietly(con, statement, rset);
        }
    }

    public static AutoChatHandler getInstance() {
        if (_instance == null)
            _instance = new AutoChatHandler();

        return _instance;
    }

    public int size() {
        return _registeredChats.size();
    }


    public AutoChatInstance registerGlobalChat(int npcId, long chatDelay) {
        AutoChatInstance chatInst;

        if (chatDelay < 0)
            chatDelay = DEFAULT_CHAT_DELAY;

        if (_registeredChats.containsKey(npcId))
            chatInst = _registeredChats.get(npcId);
        else
            chatInst = new AutoChatInstance(npcId, chatDelay);

        _registeredChats.put(npcId, chatInst);

        return chatInst;
    }

    public boolean removeChat(int npcId) {
        AutoChatInstance chatInst = _registeredChats.get(npcId);

        return removeChat(chatInst);
    }

    public boolean removeChat(AutoChatInstance chatInst) {
        if (chatInst == null)
            return false;

        _registeredChats.remove(chatInst.getNPCId());
        chatInst.setActive(false);

        return true;
    }

    @Override
    public void onSpawn(NpcInstance actor) {
        synchronized (_registeredChats) {
            if (actor == null)
                return;

            int npcId = actor.getNpcId();

            if (_registeredChats.containsKey(npcId)) {
                AutoChatInstance chatInst = _registeredChats.get(npcId);

                if (chatInst != null)
                    chatInst.addChatDefinition(actor);
            }
        }

    }

    public class AutoChatInstance {
        int _npcId;
        private long _defaultDelay = DEFAULT_CHAT_DELAY;
        private boolean _defaultRandom = false;
        private boolean _isActive;
        private Map<Integer, AutoChatDefinition> _chatDefinitions = new ConcurrentHashMap<Integer, AutoChatDefinition>();
        private ScheduledFuture<?> _chatTask;

        AutoChatInstance(int npcId, long chatDelay) {
            _npcId = npcId;
            _defaultDelay = chatDelay;
            setActive(true);
        }

        AutoChatDefinition getChatDefinition(int objectId) {
            return _chatDefinitions.get(objectId);
        }

        AutoChatDefinition[] getChatDefinitions() {
            return _chatDefinitions.values().toArray(new AutoChatDefinition[_chatDefinitions.values().size()]);
        }

        public int addChatDefinition(NpcInstance npcInst) {
            return addChatDefinition(npcInst, null, 0);
        }

        public int addChatDefinition(NpcInstance npcInst, String[] chatTexts, long chatDelay) {
            int objectId = npcInst.getObjectId();
            AutoChatDefinition chatDef = new AutoChatDefinition(this, npcInst, chatTexts, chatDelay);

            _chatDefinitions.put(objectId, chatDef);
            return objectId;
        }

        public boolean removeChatDefinition(int objectId) {
            if (!_chatDefinitions.containsKey(objectId))
                return false;

            AutoChatDefinition chatDefinition = _chatDefinitions.get(objectId);
            chatDefinition.setActive(false);

            _chatDefinitions.remove(objectId);

            return true;
        }

        public boolean isActive() {
            return _isActive;
        }

        public boolean isDefaultRandom() {
            return _defaultRandom;
        }

        public boolean isRandomChat(int objectId) {
            if (!_chatDefinitions.containsKey(objectId))
                return false;

            return _chatDefinitions.get(objectId).isRandomChat();
        }


        public int getNPCId() {
            return _npcId;
        }


        public int getDefinitionCount() {
            return _chatDefinitions.size();
        }

        public NpcInstance[] getNPCInstanceList() {
            List<NpcInstance> npcInsts = new ArrayList<NpcInstance>();

            for (AutoChatDefinition chatDefinition : _chatDefinitions.values())
                npcInsts.add(chatDefinition._npcInstance);

            return npcInsts.toArray(new NpcInstance[npcInsts.size()]);
        }

        public long getDefaultDelay() {
            return _defaultDelay;
        }


        public void setDefaultChatDelay(long delayValue) {
            _defaultDelay = delayValue;
        }


        public void setDefaultRandom(boolean randValue) {
            _defaultRandom = randValue;
        }

        public void setChatDelay(int objectId, long delayValue) {
            AutoChatDefinition chatDef = getChatDefinition(objectId);

            if (chatDef != null)
                chatDef.setChatDelay(delayValue);
        }

        public void setRandomChat(int objectId, boolean randValue) {
            AutoChatDefinition chatDef = getChatDefinition(objectId);

            if (chatDef != null)
                chatDef.setRandomChat(randValue);
        }

        public void setActive(boolean activeValue) {
            if (_isActive == activeValue)
                return;

            _isActive = activeValue;

            if (isActive()) {
                String textid = _random_chat.get(_npcId);
                AutoChatRunner acr = new AutoChatRunner(_npcId, -1, textid);
                _chatTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(acr, _defaultDelay, _defaultDelay);
            } else
                _chatTask.cancel(false);
        }

        private class AutoChatDefinition {

            protected NpcInstance _npcInstance;
            protected AutoChatInstance _chatInstance;
            protected ScheduledFuture<?> _chatTask;
            private long _chatDelay = 0;
            private boolean _isActive;
            private boolean _randomChat;

            protected AutoChatDefinition(AutoChatInstance chatInst, NpcInstance npcInst, String[] chatTexts, long chatDelay) {
                _npcInstance = npcInst;
                _chatInstance = chatInst;
                _randomChat = chatInst.isDefaultRandom();
                _chatDelay = chatDelay;
            }

            private long getChatDelay() {
                if (_chatDelay > 0)
                    return _chatDelay;
                return _chatInstance.getDefaultDelay();
            }

            private boolean isActive() {
                return _isActive;
            }

            boolean isRandomChat() {
                return _randomChat;
            }

            void setRandomChat(boolean randValue) {
                _randomChat = randValue;
            }

            void setChatDelay(long delayValue) {
                _chatDelay = delayValue;
            }


            void setActive(boolean activeValue) {
                if (isActive() == activeValue)
                    return;

                if (activeValue) {
                    String textid = _random_chat.get(_npcId);
                    AutoChatRunner acr = new AutoChatRunner(_npcId, _npcInstance.getObjectId(), textid);
                    _chatTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(acr, getChatDelay(), getChatDelay());
                } else
                    _chatTask.cancel(false);

                _isActive = activeValue;
            }
        }

        private class AutoChatRunner implements Runnable {
            private int _npcId;
            private String _textId;

            protected AutoChatRunner(int npcId, int objectId, String textId) {
                _npcId = npcId;
                _textId = textId;
            }

            public synchronized void run() {
                try {
                    NpcInstance chatNpc = null;
                    for (NpcInstance npc : GameObjectsStorage.getAllByNpcId(_npcId, true)) {
                        if (chatNpc == null)
                            chatNpc = npc;

                        if (chatNpc == null || !chatNpc.isVisible()) {
                            System.out.println("npc " + _npcId + " null");
                            return;
                        }

                        if (Rnd.get(5) == 1)
                            return;

                        List<Player> nearbyPlayers = World.getAroundPlayers(chatNpc, 1500, 200);

                        if (nearbyPlayers == null || nearbyPlayers.isEmpty())
                            return;

                        String[] vals = _textId.split(",");
                        NpcSayNative cs = new NpcSayNative(chatNpc, Integer.parseInt(vals[Rnd.get(0, vals.length - 1)]));
                        for (Player nearbyPlayer : nearbyPlayers) {
                            if (nearbyPlayer == null)
                                continue;
                            nearbyPlayer.sendPacket(cs);
                        }

                        chatNpc = null;
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }

}
