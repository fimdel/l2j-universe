package lineage2.gameserver.network.clientpackets;

/**
 */
public class SelectTargetAction extends AbstractTargetAction
{
	@Override
	protected boolean isAttackRequest()
	{
		return false;
	}
}
