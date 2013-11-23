/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.templates.npc;

import gnu.trove.map.hash.TIntObjectHashMap;
import l2p.commons.util.Rnd;
import l2p.gameserver.network.serverpackets.components.NpcString;

public class RandomActions {
    private final TIntObjectHashMap<Action> _actions;
    private final boolean _randomOrder;

    public RandomActions(boolean randomOrder) {
        this._actions = new TIntObjectHashMap<Action>();
        this._randomOrder = randomOrder;
    }

    public void addAction(Action action) {
        this._actions.put(action.getId(), action);
    }

    public Action getAction(int id) {
        if (this._randomOrder) {
            Action[] actionsArr = (Action[]) this._actions.values(new Action[this._actions.size()]);
            return actionsArr[Rnd.get(actionsArr.length)];
        }
        return (Action) this._actions.get(id);
    }

    public int getActionsCount() {
        return this._actions.size();
    }

    public static class Action {
        private int _id;
        private NpcString _phrase;
        private int _socialActionId;
        private int _delay;

        public Action(int id, NpcString phrase, int socialActionId, int delay) {
            this._id = id;
            this._phrase = phrase;
            this._socialActionId = socialActionId;
            this._delay = delay;
        }

        public int getId() {
            return this._id;
        }

        public NpcString getPhrase() {
            return this._phrase;
        }

        public int getSocialActionId() {
            return this._socialActionId;
        }

        public int getDelay() {
            return this._delay;
        }
    }
}
