/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package npc.model;

import java.util.concurrent.Future;

import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.instances.MonsterInstance;
import lineage2.gameserver.network.serverpackets.NpcSay;
import lineage2.gameserver.network.serverpackets.components.ChatType;
import lineage2.gameserver.tables.SkillTable;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.ItemFunctions;
import bosses.FourSepulchersSpawn;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class SepulcherMonsterInstance extends MonsterInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field mysteriousBoxId.
	 */
	public int mysteriousBoxId = 0;
	/**
	 * Field _victimShout.
	 */
	Future<?> _victimShout = null;
	/**
	 * Field _victimSpawnKeyBoxTask.
	 */
	private Future<?> _victimSpawnKeyBoxTask = null;
	/**
	 * Field _changeImmortalTask.
	 */
	private Future<?> _changeImmortalTask = null;
	/**
	 * Field _onDeadEventTask.
	 */
	private Future<?> _onDeadEventTask = null;
	/**
	 * Field HALLS_KEY. (value is 7260)
	 */
	private final static int HALLS_KEY = 7260;
	
	/**
	 * Constructor for SepulcherMonsterInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public SepulcherMonsterInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	/**
	 * Method onSpawn.
	 */
	@Override
	protected void onSpawn()
	{
		switch (getNpcId())
		{
			case 18150:
			case 18151:
			case 18152:
			case 18153:
			case 18154:
			case 18155:
			case 18156:
			case 18157:
				if (_victimSpawnKeyBoxTask != null)
				{
					_victimSpawnKeyBoxTask.cancel(false);
				}
				_victimSpawnKeyBoxTask = ThreadPoolManager.getInstance().schedule(new VictimSpawnKeyBox(this), 300000);
				if (_victimShout != null)
				{
					_victimShout.cancel(false);
				}
				_victimShout = ThreadPoolManager.getInstance().schedule(new VictimShout(this), 5000);
				break;
			case 18196:
			case 18197:
			case 18198:
			case 18199:
			case 18200:
			case 18201:
			case 18202:
			case 18203:
			case 18204:
			case 18205:
			case 18206:
			case 18207:
			case 18208:
			case 18209:
			case 18210:
			case 18211:
				break;
			case 18231:
			case 18232:
			case 18233:
			case 18234:
			case 18235:
			case 18236:
			case 18237:
			case 18238:
			case 18239:
			case 18240:
			case 18241:
			case 18242:
			case 18243:
				if (_changeImmortalTask != null)
				{
					_changeImmortalTask.cancel(false);
				}
				_changeImmortalTask = ThreadPoolManager.getInstance().schedule(new ChangeImmortal(this), 1600);
				break;
			case 18256:
				break;
		}
		super.onSpawn();
	}
	
	/**
	 * Method onDeath.
	 * @param killer Creature
	 */
	@Override
	protected void onDeath(Creature killer)
	{
		super.onDeath(killer);
		switch (getNpcId())
		{
			case 18120:
			case 18121:
			case 18122:
			case 18123:
			case 18124:
			case 18125:
			case 18126:
			case 18127:
			case 18128:
			case 18129:
			case 18130:
			case 18131:
			case 18149:
			case 18158:
			case 18159:
			case 18160:
			case 18161:
			case 18162:
			case 18163:
			case 18164:
			case 18165:
			case 18183:
			case 18184:
			case 18212:
			case 18213:
			case 18214:
			case 18215:
			case 18216:
			case 18217:
			case 18218:
			case 18219:
				if (_onDeadEventTask != null)
				{
					_onDeadEventTask.cancel(false);
				}
				_onDeadEventTask = ThreadPoolManager.getInstance().schedule(new OnDeadEvent(this), 3500);
				break;
			case 18150:
			case 18151:
			case 18152:
			case 18153:
			case 18154:
			case 18155:
			case 18156:
			case 18157:
				if (_victimSpawnKeyBoxTask != null)
				{
					_victimSpawnKeyBoxTask.cancel(false);
					_victimSpawnKeyBoxTask = null;
				}
				if (_victimShout != null)
				{
					_victimShout.cancel(false);
					_victimShout = null;
				}
				if (_onDeadEventTask != null)
				{
					_onDeadEventTask.cancel(false);
				}
				_onDeadEventTask = ThreadPoolManager.getInstance().schedule(new OnDeadEvent(this), 3500);
				break;
			case 18141:
			case 18142:
			case 18143:
			case 18144:
			case 18145:
			case 18146:
			case 18147:
			case 18148:
				if (FourSepulchersSpawn.isViscountMobsAnnihilated(mysteriousBoxId) && !hasPartyAKey(killer.getPlayer()))
				{
					if (_onDeadEventTask != null)
					{
						_onDeadEventTask.cancel(false);
					}
					_onDeadEventTask = ThreadPoolManager.getInstance().schedule(new OnDeadEvent(this), 3500);
				}
				break;
			case 18220:
			case 18221:
			case 18222:
			case 18223:
			case 18224:
			case 18225:
			case 18226:
			case 18227:
			case 18228:
			case 18229:
			case 18230:
			case 18231:
			case 18232:
			case 18233:
			case 18234:
			case 18235:
			case 18236:
			case 18237:
			case 18238:
			case 18239:
			case 18240:
				if (FourSepulchersSpawn.isDukeMobsAnnihilated(mysteriousBoxId))
				{
					if (_onDeadEventTask != null)
					{
						_onDeadEventTask.cancel(false);
					}
					_onDeadEventTask = ThreadPoolManager.getInstance().schedule(new OnDeadEvent(this), 3500);
				}
				break;
		}
	}
	
	/**
	 * Method onDelete.
	 */
	@Override
	protected void onDelete()
	{
		if (_victimSpawnKeyBoxTask != null)
		{
			_victimSpawnKeyBoxTask.cancel(false);
			_victimSpawnKeyBoxTask = null;
		}
		if (_onDeadEventTask != null)
		{
			_onDeadEventTask.cancel(false);
			_onDeadEventTask = null;
		}
		super.onDelete();
	}
	
	/**
	 * @author Mobius
	 */
	private class VictimShout extends RunnableImpl
	{
		/**
		 * Field _activeChar.
		 */
		private final SepulcherMonsterInstance _activeChar;
		
		/**
		 * Constructor for VictimShout.
		 * @param activeChar SepulcherMonsterInstance
		 */
		public VictimShout(SepulcherMonsterInstance activeChar)
		{
			_activeChar = activeChar;
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			if (_activeChar.isDead())
			{
				return;
			}
			if (!_activeChar.isVisible())
			{
				return;
			}
			broadcastPacket(new NpcSay(SepulcherMonsterInstance.this, ChatType.ALL, "forgive me!!"));
		}
	}
	
	/**
	 * @author Mobius
	 */
	private class VictimSpawnKeyBox extends RunnableImpl
	{
		/**
		 * Field _activeChar.
		 */
		private final SepulcherMonsterInstance _activeChar;
		
		/**
		 * Constructor for VictimSpawnKeyBox.
		 * @param activeChar SepulcherMonsterInstance
		 */
		public VictimSpawnKeyBox(SepulcherMonsterInstance activeChar)
		{
			_activeChar = activeChar;
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			if (_activeChar.isDead())
			{
				return;
			}
			if (!_activeChar.isVisible())
			{
				return;
			}
			FourSepulchersSpawn.spawnKeyBox(_activeChar);
			broadcastPacket(new NpcSay(SepulcherMonsterInstance.this, ChatType.ALL, "Many thanks for rescue me."));
			if (_victimShout != null)
			{
				_victimShout.cancel(false);
				_victimShout = null;
			}
		}
	}
	
	/**
	 * @author Mobius
	 */
	private class OnDeadEvent extends RunnableImpl
	{
		/**
		 * Field _activeChar.
		 */
		SepulcherMonsterInstance _activeChar;
		
		/**
		 * Constructor for OnDeadEvent.
		 * @param activeChar SepulcherMonsterInstance
		 */
		public OnDeadEvent(SepulcherMonsterInstance activeChar)
		{
			_activeChar = activeChar;
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			switch (_activeChar.getNpcId())
			{
				case 18120:
				case 18121:
				case 18122:
				case 18123:
				case 18124:
				case 18125:
				case 18126:
				case 18127:
				case 18128:
				case 18129:
				case 18130:
				case 18131:
				case 18149:
				case 18158:
				case 18159:
				case 18160:
				case 18161:
				case 18162:
				case 18163:
				case 18164:
				case 18165:
				case 18183:
				case 18184:
				case 18212:
				case 18213:
				case 18214:
				case 18215:
				case 18216:
				case 18217:
				case 18218:
				case 18219:
					FourSepulchersSpawn.spawnKeyBox(_activeChar);
					break;
				case 18150:
				case 18151:
				case 18152:
				case 18153:
				case 18154:
				case 18155:
				case 18156:
				case 18157:
					FourSepulchersSpawn.spawnExecutionerOfHalisha(_activeChar);
					break;
				case 18141:
				case 18142:
				case 18143:
				case 18144:
				case 18145:
				case 18146:
				case 18147:
				case 18148:
					FourSepulchersSpawn.spawnMonster(_activeChar.mysteriousBoxId);
					break;
				case 18220:
				case 18221:
				case 18222:
				case 18223:
				case 18224:
				case 18225:
				case 18226:
				case 18227:
				case 18228:
				case 18229:
				case 18230:
				case 18231:
				case 18232:
				case 18233:
				case 18234:
				case 18235:
				case 18236:
				case 18237:
				case 18238:
				case 18239:
				case 18240:
					FourSepulchersSpawn.spawnArchonOfHalisha(_activeChar.mysteriousBoxId);
					break;
			}
		}
	}
	
	/**
	 * @author Mobius
	 */
	private class ChangeImmortal extends RunnableImpl
	{
		/**
		 * Field activeChar.
		 */
		private final SepulcherMonsterInstance activeChar;
		
		/**
		 * Constructor for ChangeImmortal.
		 * @param mob SepulcherMonsterInstance
		 */
		public ChangeImmortal(SepulcherMonsterInstance mob)
		{
			activeChar = mob;
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			Skill fp = SkillTable.getInstance().getInfo(4616, 1);
			fp.getEffects(activeChar, activeChar, false, false);
		}
	}
	
	/**
	 * Method hasPartyAKey.
	 * @param player Player
	 * @return boolean
	 */
	private boolean hasPartyAKey(Player player)
	{
		for (Player m : player.getParty().getPartyMembers())
		{
			if (ItemFunctions.getItemCount(m, HALLS_KEY) > 0)
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Method canChampion.
	 * @return boolean
	 */
	@Override
	public boolean canChampion()
	{
		return false;
	}
}
