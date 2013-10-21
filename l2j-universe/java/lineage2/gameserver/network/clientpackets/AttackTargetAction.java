package lineage2.gameserver.network.clientpackets;

/**
 */
public class AttackTargetAction extends AbstractTargetAction
{
	@Override
	protected boolean isAttackRequest()
	{
		return true;
	}
}
