package lineage2.gameserver.network.serverpackets;

import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.base.RestartType;
import lineage2.gameserver.model.entity.events.GlobalEvent;
import lineage2.gameserver.model.instances.MonsterInstance;
import lineage2.gameserver.model.pledge.Clan;

import java.util.HashMap;
import java.util.Map;

public class Die extends L2GameServerPacket
{
	private int _objectId;
	private boolean _fake;
	private boolean _sweepable;

	private Map<RestartType, Boolean> _types = new HashMap<RestartType, Boolean>(RestartType.VALUES.length);

	public Die(Creature cha)
	{
		_objectId = cha.getObjectId();
		_fake = !cha.isDead();

		if (cha.isMonster())
			_sweepable = ((MonsterInstance) cha).isSweepActive();
		else if (cha.isPlayer())
		{
			Player player = (Player) cha;
			put(RestartType.FIXED, player.getPlayerAccess().ResurectFixed || (player.getInventory().getCountOf(10649) > 0 || player.getInventory().getCountOf(13300) > 0) && !player.isOnSiegeField());
			put(RestartType.AGATHION, player.isAgathionResAvailable());
			put(RestartType.TO_VILLAGE, true);

			Clan clan = null;
			if (get(RestartType.TO_VILLAGE))
				clan = player.getClan();
			if (clan != null)
			{
				put(RestartType.TO_CLANHALL, clan.getHasHideout() > 0);
				put(RestartType.TO_CASTLE, clan.getCastle() > 0);
				put(RestartType.TO_FORTRESS, clan.getHasFortress() > 0);
			}

			for (GlobalEvent e : cha.getEvents())
				e.checkRestartLocs(player, _types);
		}
	}

	@Override
	protected final void writeImpl()
	{
		if (_fake)
			return;

		writeC(0x00);
		writeD(_objectId);
		writeD(get(RestartType.TO_VILLAGE)); // to nearest village
		writeD(get(RestartType.TO_CLANHALL)); // to hide away
		writeD(get(RestartType.TO_CASTLE)); // to castle
		writeD(get(RestartType.TO_FLAG));// to siege HQ
		writeD(_sweepable ? 0x01 : 0x00); // sweepable (blue glow)
		writeD(get(RestartType.FIXED));// FIXED
		writeD(get(RestartType.TO_FORTRESS));// fortress
		writeC(0); // show die animation
		writeD(get(RestartType.AGATHION));// agathion ress button
		writeD(0x00); // additional free space
	}

	private void put(RestartType t, boolean b)
	{
		_types.put(t, b);
	}

	private boolean get(RestartType t)
	{
		Boolean b = _types.get(t);
		return b != null && b;
	}
}