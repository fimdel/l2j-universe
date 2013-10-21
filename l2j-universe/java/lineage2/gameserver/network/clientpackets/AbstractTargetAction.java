package lineage2.gameserver.network.clientpackets;

import lineage2.gameserver.model.GameObject;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.utils.Location;

/**
 */
public abstract class AbstractTargetAction extends L2GameClientPacket
{
	private final Location _actorPosition = new Location();
	private boolean _forced;
	private int _targetObjectId;

	@Override
	protected void readImpl()
	{
		_targetObjectId = readD();
		_actorPosition.x = readD();
		_actorPosition.y = readD();
		_actorPosition.z = readD();
		_forced = readC() == 1;
	}

	@Override
	protected void runImpl()
	{
		final Player actor = getActiveChar();

		if(actor == null)
		{
			return;
		}

		final GameObject obj = actor.getVisibleObject(_targetObjectId);

		if(obj == null)
		{
			return;
		}

		if(_actorPosition.isNull())
		{
			return;
		}

		if(obj.isTargetable(actor))
		{
			if(isAttackRequest())
			{
				if(actor.getPlayerAccess().CanAttack)
				{
					obj.onForcedAttack(actor, _forced);
				}
			}
			else
			{
				obj.onActionSelect(actor, _forced);
			}
			actor.setActive();
		}
		actor.sendActionFailed();
	}

	protected abstract boolean isAttackRequest();
}
