package lineage2.gameserver.stats.conditions;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.stats.Env;

public class ConditionCastleDarkClanLeader extends Condition
{
	public ConditionCastleDarkClanLeader()
	{
	}

	@Override
	protected boolean testImpl(Env env)
	{
		if(!env.character.isPlayer())
		{
			return false;
		}

		Player player = env.character.getPlayer();

		if(player.getClan() == null || player.getCastle() == null)
		{
			return false;
		}

		if(!player.isClanLeader())
		{
			return false;
		}

		return !player.getCastle().isCastleTypeLight();
	}
}