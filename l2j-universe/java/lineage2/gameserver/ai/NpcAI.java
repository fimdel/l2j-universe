package lineage2.gameserver.ai;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.L2GameServerPacket;
import lineage2.gameserver.network.serverpackets.SocialAction;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.templates.npc.RandomActions;
import lineage2.gameserver.templates.npc.WalkerRoute;
import lineage2.gameserver.templates.npc.WalkerRoutePoint;
import lineage2.gameserver.utils.Location;

public class NpcAI extends CharacterAI
{
	public static final String WALKER_ROUTE_PARAM = "walker_route_id";
	private final RandomActions _randomActions;
	private final boolean _haveRandomActions;
	private int _currentActionId;
	private final WalkerRoute _walkerRoute;
	private final boolean _haveWalkerRoute;
	private boolean _toBackWay;
	private int _currentWalkerPoint;
	private boolean _delete;
	private boolean _isActive;

	public NpcAI(NpcInstance actor)
	{
		super(actor);

		_randomActions = actor.getTemplate().getRandomActions();
		_haveRandomActions = ((_randomActions != null) && (_randomActions.getActionsCount() > 0));
		_currentActionId = 0;
 
		int walkerRouteId = actor.getParameter("walker_route_id", -1);
		_walkerRoute = actor.getTemplate().getWalkerRoute(walkerRouteId);
		_haveWalkerRoute = ((_walkerRoute != null) && (_walkerRoute.isValid()));
		_toBackWay = false;
		_currentWalkerPoint = -1;
		_delete = false;
		_isActive = false;
	}

	protected void onEvtArrived()
	{
		if (!_isActive)
		{
			return;
		}
		continueWalkerRoute();
	}

	protected void onEvtTimer(int timerId, Object arg1, Object arg2)
	{
		if (timerId == -1000)
		{
			if (_haveWalkerRoute)
			{
				if (!(arg1 instanceof Location))
				{
					return;
				}
				moveToLocation((Location)arg1);
			}
		}
		else if (timerId == -2000)
		{
			if (_haveRandomActions)
				makeRandomAction();
		}
	}

	public boolean isActive()
	{
		return _isActive;
	}

	public void stopAITask()
	{
		_isActive = false;
		if (_haveWalkerRoute)
		{
			if (_toBackWay)
				_currentWalkerPoint += 1;
			else
				_currentWalkerPoint -= 1;
		}
	}

	public void startAITask()
	{
		_isActive = true;
		if (_haveWalkerRoute)
		{
			moveToNextPoint(0);
		}
		if (_haveRandomActions)
		{
			RandomActions.Action action = _randomActions.getAction(1);
			if (action == null)
			{
				return;
			}
 			addTimer(-2000, Rnd.get(0, action.getDelay()) * 1000L);
		}
	}

	private void continueWalkerRoute()
	{
		if (_haveWalkerRoute)
		{
	       WalkerRoutePoint route = _walkerRoute.getPoint(_currentWalkerPoint);
	       if (route == null)
	       {
	    	   return;
	       }
	       NpcInstance actor = getActor();
	       int socialActionId = route.getSocialActionId();
	       if (socialActionId >= 0)
	       {
	    	   actor.broadcastPacket(new L2GameServerPacket[] { new SocialAction(actor.getObjectId(), socialActionId) });
	       }
	       NpcString phrase = route.getPhrase();
	       if (phrase != null)
	       {
	    	   Functions.npcSay(actor, phrase, new String[0]);
	       }
	       moveToNextPoint(route.getDelay());
		}
	}
 
	private void moveToNextPoint(int delay)
	{
		if (!_isActive)
		{
			return;
		}
		NpcInstance actor = getActor();
		if (actor == null)
		{
			return;
		}
		switch (_walkerRoute.getType().ordinal())
		{
			case 1:
				if (_toBackWay)
				{
					_currentWalkerPoint -= 1;
				}
				else
				{
					_currentWalkerPoint += 1;
				}
				if (_currentWalkerPoint >= _walkerRoute.size() - 1)
				{
					_toBackWay = true;
				}
				if (_currentWalkerPoint == 0)
				{
					_toBackWay = false; break;
				}
			case 2:
				_currentWalkerPoint += 1;
				if (_currentWalkerPoint >= _walkerRoute.size() - 1)
				{
					_currentWalkerPoint = 0; break;
				}
			case 3:
				if (_walkerRoute.size() > 1)
				{
					int oldPoint = _currentWalkerPoint;
					while (oldPoint == _currentWalkerPoint)
					{
						_currentWalkerPoint = Rnd.get(_walkerRoute.size() - 1);
					}
				}
				break;
			case 4:
				if (_delete)
				{
					actor.deleteMe();
					return;
				}
				_currentWalkerPoint += 1;
				if (_currentWalkerPoint >= _walkerRoute.size() - 1)
				{
					_delete = true; break;
				}
			case 5:
				_currentWalkerPoint += 1;
				if (_currentWalkerPoint >= _walkerRoute.size() - 1)
				{
					actor.stopMove();
				}
				break;
		}
 
		WalkerRoutePoint route = _walkerRoute.getPoint(_currentWalkerPoint);
		if (route == null)
		{
			return;
		}
		if (route.isRunning())
		{
			actor.setRunning();
		}
		else
		{
			actor.setWalking();
		}
		if (delay > 0)
		{
			addTimer(-1000, route.getLocation(), delay * 1000L);
		}
		else
		{
			moveToLocation(route.getLocation());
		}
	}
 
	private void makeRandomAction()
	{
		if (!_isActive)
		{
			return;
		}
		NpcInstance actor = getActor();
		if (actor == null)
		{
			return;
		}
		_currentActionId += 1;
		if (_currentActionId > _randomActions.getActionsCount())
		{
			_currentActionId = 1;
		}
		RandomActions.Action action = _randomActions.getAction(_currentActionId);
		if (action == null)
		{
			return;
		}
		int socialActionId = action.getSocialActionId();
		if (socialActionId >= 0)
		{
			actor.broadcastPacket(new L2GameServerPacket[] { new SocialAction(actor.getObjectId(), socialActionId) });
		}
		NpcString phrase = action.getPhrase();
		if (phrase != null)
		{
			Functions.npcSay(actor, phrase, new String[0]);
		}
		addTimer(-2000, action.getDelay() * 1000L);
	}

	private void moveToLocation(Location loc)
	{
		NpcInstance actor = getActor();
		if (actor == null)
		{
			return;
		}
		if (!actor.moveToLocation(loc, 0, false))
		{
			clientStopMoving();
			actor.teleToLocation(loc);
			continueWalkerRoute();
  		}
	}

	public NpcInstance getActor()
	{
		return (NpcInstance)super.getActor();
	}
}
