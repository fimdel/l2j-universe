package lineage2.gameserver.model;

import java.util.ArrayList;
import java.util.List;

import lineage2.gameserver.network.serverpackets.L2GameServerPacket;
import lineage2.gameserver.network.serverpackets.ServerObjectInfo;
import lineage2.gameserver.templates.StatuesSpawnTemplate;

public final class WinnerStatueInstance extends GameObject
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private StatuesSpawnTemplate template;

	public WinnerStatueInstance(int objectId, StatuesSpawnTemplate template)
	{
		super(objectId);
		this.template = template;
	}

	@Override
	public boolean isAttackable(Creature creature)
	{
		return false;
	}

	@Override
	public double getColRadius()
	{
		return 30.0;
	}

	@Override
	public double getColHeight()
	{
		return 40.0;
	}

	@Override
	public List<L2GameServerPacket> addPacketList(Player forPlayer, Creature dropper)
	{
		List<L2GameServerPacket> list = new ArrayList<>(1);
		list.add(new ServerObjectInfo(this, forPlayer));
		return list;
	}

/*	@Override
	public void onAction(Player player, boolean shift)
	{
		List<CharacterStatistic> globalStatistic = WorldStatisticsManager.getInstance().getWinners(template.getCategoryType(), true, WorldStatisticsManager.STATUES_TOP_PLAYER_LIMIT);
		List<CharacterStatistic> monthlyStatistic = WorldStatisticsManager.getInstance().getWinners(template.getCategoryType(), false, WorldStatisticsManager.STATUES_TOP_PLAYER_LIMIT);
		player.sendPacket(new ExLoadStatHotLink(template.getCategoryType().getClientId(), template.getCategoryType().getSubcat(), globalStatistic, monthlyStatistic));
		player.sendActionFailed();
	}*/

	public StatuesSpawnTemplate getTemplate()
	{
		return template;
	}
}
