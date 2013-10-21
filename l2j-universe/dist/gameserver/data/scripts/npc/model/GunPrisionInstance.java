package npc.model;

import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.World;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.Earthquake;
import lineage2.gameserver.network.serverpackets.NpcHtmlMessage;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.network.serverpackets.MagicSkillUse;
import lineage2.gameserver.tables.SkillTable;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.Location;

public final class GunPrisionInstance extends NpcInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @author cruel
	 */
	 
	private boolean checkShot = true;
	
	private static Location[] point_bombs = { new Location(176856, 144152, -11875), new Location(176808, 141384, -11859), new Location(174264, 141208, -11874), new Location(174056, 144056, -11870) };
		
	private static Location[] point_bombs_spezion = { new Location(186056, 144152, -11851), new Location(186072, 141320, -11855), new Location(183720, 141256, -11859), new Location(183672, 143944, -11841) };
	
	public GunPrisionInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
		setTitle("Empty Cannon");
	}

	@Override
	public void onBypassFeedback(Player player, String command)
	{
		if(!canBypassCheck(player, this))
			return;

		if(command.equalsIgnoreCase("zalp"))
		{
			if (!checkShot) {
				Functions.npcSay(this, NpcString.CANNON_READY_TO_FIRE);
				player.sendPacket(new NpcHtmlMessage(player, this).setHtml("Cannon:<br><br>Preparations are underway to re-activate the cannon. This process can take up to 5 minutes."));
				return;
			} else if (!player.getInventory().destroyItemByItemId(17611, 1)) {
				player.sendPacket(new NpcHtmlMessage(player, this).setHtml("Cannon:<br><br>\"Huge Charges\" not available."));
				return;
			}

			broadcastPacketToOthers(new MagicSkillUse(this, this, 14175, 1, 3000, 0));
			broadcastPacket(new Earthquake(player.getLoc(), 10, 7));
			ThreadPoolManager.getInstance().schedule(new Shot(), 300 * 1000L);
			checkShot = false;
			ThreadPoolManager.getInstance().schedule(new RunnableImpl() {
			@Override
			public void runImpl() throws Exception {
				decayMe();
				spawnMe();
			}
			}, 3100);
			setTitle("Cannon is loading");

			Location loc = point_bombs[getNpcId()-32939];
			
			for (NpcInstance monster : World.getAroundNpcCor(loc, getCurrentRegion(), getReflectionId(), 650, 500))
			{
				if (monster == null || !monster.isNpc() || monster.getNpcId() != 22966 && monster.getNpcId() != 22965 && monster.getNpcId() != 22967)
					continue;
			
				if (monster.getNpcId() == 22966)
					Functions.spawn(monster.getLoc(), 22980);
				else if (monster.getNpcId() == 22965)
					Functions.spawn(monster.getLoc(), 22979);
				else if (monster.getNpcId() == 22967)
					Functions.spawn(monster.getLoc(), 22981);
					
				monster.decayMe();
				monster.doDie(this);
			}	
		} else if (command.equalsIgnoreCase("spezion_bomb"))
		{
			if (!checkShot) {
				Functions.npcSay(this, NpcString.CANNON_READY_TO_FIRE);
				player.sendPacket(new NpcHtmlMessage(player, this).setHtml("Cannon:<br><br>Preparations are underway to re-activate the cannon. This process can take up to 5 minutes."));
				return;
			} else if (!player.getInventory().destroyItemByItemId(17611, 1)) {
				player.sendPacket(new NpcHtmlMessage(player, this).setHtml("Cannon:<br><br>\"Huge Charges\" not available."));
				return;
			}
			
			checkShot = false;
			broadcastPacketToOthers(new MagicSkillUse(this, this, 14175, 1, 3000, 0));
			broadcastPacket(new Earthquake(player.getLoc(), 10, 7));
			ThreadPoolManager.getInstance().schedule(new RunnableImpl() {
			@Override
			public void runImpl() throws Exception {
				checkShot = true;
				setTitle("Empty Cannon");
			}
			}, 60000);
			ThreadPoolManager.getInstance().schedule(new RunnableImpl() {
			@Override
			public void runImpl() throws Exception {
				decayMe();
				spawnMe();
			}
			}, 3100);
			setTitle("Cannon is loading");
			
			Location loc = point_bombs_spezion[getNpcId()-33288];
			
			for (NpcInstance monster : World.getAroundNpcCor(loc, getCurrentRegion(), getReflectionId(), 700, 500))
			{
				if (monster == null || !monster.isNpc() || monster.getNpcId() != 25779)
					continue;
				monster.getEffectList().stopEffect(SkillTable.getInstance().getInfo(14190, 1));
				monster.setNpcState(2);
				ThreadPoolManager.getInstance().schedule(new Buff(monster), 60 * 1000L);
			}	
		}
		else
			super.onBypassFeedback(player, command);
	}
	
	private class Buff extends RunnableImpl
	{
		private NpcInstance _monster;

		public Buff(NpcInstance monster)
		{
			_monster = monster;
		}
		@Override
		public void runImpl() throws Exception
		{
			Skill fp = SkillTable.getInstance().getInfo(14190, 1);
			fp.getEffects(_monster, _monster, false, false);
			_monster.setNpcState(1);
		}
	}
	
	private class Shot extends RunnableImpl
	{
		@Override
		public void runImpl() throws Exception
		{
			checkShot = true;
			setTitle("Empty Cannon");
			decayMe();
			spawnMe();
		}
	}

}