package lineage2.gameserver.network.serverpackets;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.actor.instances.player.ShortCut;

public class ShortCutRegister extends ShortCutPacket
{
	private ShortcutInfo _shortcutInfo;

	public ShortCutRegister(Player player, ShortCut sc)
	{
		_shortcutInfo = convert(player, sc);
	}

	@Override
	protected final void writeImpl()
	{
		writeC(0x44);

		_shortcutInfo.write(this);
	}
}