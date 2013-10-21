package lineage2.gameserver.templates.npc;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import gnu.trove.map.hash.TIntObjectHashMap;

public class RandomActions
{
	private final TIntObjectHashMap<Action> _actions;
	private final boolean _randomOrder;

	public RandomActions(boolean randomOrder)
	{
		_actions = new TIntObjectHashMap<Action>();
		_randomOrder = randomOrder;
	}

	public void addAction(Action action)
	{
		_actions.put(action.getId(), action);
	}

	public Action getAction(int id)
	{
		if (_randomOrder)
		{
			Action[] actionsArr = _actions.values(new Action[_actions.size()]);
			return actionsArr[Rnd.get(actionsArr.length)];
		}
		return _actions.get(id);
	}

	public int getActionsCount()
	{
		return _actions.size();
	}

	public static class Action
	{
		private int _id;
		private NpcString _phrase;
		private int _socialActionId;
		private int _delay;

		public Action(int id, NpcString phrase, int socialActionId, int delay)
		{
			_id = id;
			_phrase = phrase;
			_socialActionId = socialActionId;
			_delay = delay;
		}

		public int getId()
		{
			return _id;
		}

		public NpcString getPhrase()
		{
			return _phrase;
		}

		public int getSocialActionId()
		{
			return _socialActionId;
		}

		public int getDelay()
		{
			return _delay;
		}
	}
}