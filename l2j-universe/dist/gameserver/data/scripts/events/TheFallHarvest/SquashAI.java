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
package events.TheFallHarvest;

import java.util.List;
import java.util.concurrent.ScheduledFuture;

import lineage2.commons.threading.RunnableImpl;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.ai.Fighter;
import lineage2.gameserver.data.xml.holder.NpcHolder;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.SimpleSpawner;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.reward.RewardData;
import lineage2.gameserver.model.reward.RewardItem;
import lineage2.gameserver.network.serverpackets.MagicSkillUse;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.utils.Log;
import npc.model.SquashInstance;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class SquashAI extends Fighter
{
	/**
	 * @author Mobius
	 */
	public class PolimorphTask extends RunnableImpl
	{
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			final SquashInstance actor = getActor();
			if (actor == null)
			{
				return;
			}
			SimpleSpawner spawn = null;
			try
			{
				spawn = new SimpleSpawner(NpcHolder.getInstance().getTemplate(_npcId));
				spawn.setLoc(actor.getLoc());
				final NpcInstance npc = spawn.doSpawn(true);
				npc.setAI(new SquashAI(npc));
				((SquashInstance) npc).setSpawner(actor.getSpawner());
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			_timeToUnspawn = Long.MAX_VALUE;
			actor.deleteMe();
		}
	}
	
	/**
	 * Field _dropList.
	 */
	protected static final RewardData[] _dropList = new RewardData[]
	{
		new RewardData(1539, 1, 5, 15000),
		new RewardData(1374, 1, 3, 15000),
		new RewardData(4411, 1, 1, 5000),
		new RewardData(4412, 1, 1, 5000),
		new RewardData(4413, 1, 1, 5000),
		new RewardData(4414, 1, 1, 5000),
		new RewardData(4415, 1, 1, 5000),
		new RewardData(4416, 1, 1, 5000),
		new RewardData(4417, 1, 1, 5000),
		new RewardData(5010, 1, 1, 5000),
		new RewardData(1458, 10, 30, 13846),
		new RewardData(1459, 10, 30, 3000),
		new RewardData(1460, 10, 30, 1000),
		new RewardData(1461, 10, 30, 600),
		new RewardData(1462, 10, 30, 360),
		new RewardData(4161, 1, 1, 5000),
		new RewardData(4182, 1, 1, 5000),
		new RewardData(4174, 1, 1, 5000),
		new RewardData(4166, 1, 1, 5000),
		new RewardData(8660, 1, 1, 1000),
		new RewardData(8661, 1, 1, 1000),
		new RewardData(4393, 1, 1, 300),
		new RewardData(7836, 1, 1, 200),
		new RewardData(5590, 1, 1, 200),
		new RewardData(7058, 1, 1, 50),
		new RewardData(8350, 1, 1, 50),
		new RewardData(5133, 1, 1, 50),
		new RewardData(5817, 1, 1, 50),
		new RewardData(9140, 1, 1, 30),
		new RewardData(9177, 1, 1, 100),
		new RewardData(9178, 1, 1, 100),
		new RewardData(9179, 1, 1, 100),
		new RewardData(9180, 1, 1, 100),
		new RewardData(9181, 1, 1, 100),
		new RewardData(9182, 1, 1, 100),
		new RewardData(9183, 1, 1, 100),
		new RewardData(9184, 1, 1, 100),
		new RewardData(9185, 1, 1, 100),
		new RewardData(9186, 1, 1, 100),
		new RewardData(9187, 1, 1, 100),
		new RewardData(9188, 1, 1, 100),
		new RewardData(9189, 1, 1, 100),
		new RewardData(9190, 1, 1, 100),
		new RewardData(9191, 1, 1, 100),
		new RewardData(9192, 1, 1, 100),
		new RewardData(9193, 1, 1, 100),
		new RewardData(9194, 1, 1, 100),
		new RewardData(9195, 1, 1, 100),
		new RewardData(9196, 1, 1, 100),
		new RewardData(9197, 1, 1, 100),
		new RewardData(9198, 1, 1, 100),
		new RewardData(9199, 1, 1, 100),
		new RewardData(9200, 1, 1, 100),
		new RewardData(9201, 1, 1, 100),
		new RewardData(9202, 1, 1, 100),
		new RewardData(9203, 1, 1, 100),
		new RewardData(9204, 1, 1, 100),
		new RewardData(9146, 1, 3, 5000),
		new RewardData(9147, 1, 3, 5000),
		new RewardData(9148, 1, 3, 5000),
		new RewardData(9149, 1, 3, 5000),
		new RewardData(9150, 1, 3, 5000),
		new RewardData(9151, 1, 3, 5000),
		new RewardData(9152, 1, 3, 5000),
		new RewardData(9153, 1, 3, 5000),
		new RewardData(9154, 1, 3, 5000),
		new RewardData(9155, 1, 3, 5000),
		new RewardData(9156, 1, 3, 2000),
		new RewardData(9157, 1, 3, 1000),
		new RewardData(955, 1, 1, 400),
		new RewardData(956, 1, 1, 2000),
		new RewardData(951, 1, 1, 300),
		new RewardData(952, 1, 1, 1500),
		new RewardData(947, 1, 1, 200),
		new RewardData(948, 1, 1, 1000),
		new RewardData(729, 1, 1, 100),
		new RewardData(730, 1, 1, 500),
		new RewardData(959, 1, 1, 50),
		new RewardData(960, 1, 1, 300),
	};
	/**
	 * Field Young_Squash. (value is 12774)
	 */
	public final static int Young_Squash = 12774;
	/**
	 * Field High_Quality_Squash. (value is 12775)
	 */
	public final static int High_Quality_Squash = 12775;
	/**
	 * Field Low_Quality_Squash. (value is 12776)
	 */
	public final static int Low_Quality_Squash = 12776;
	/**
	 * Field Large_Young_Squash. (value is 12777)
	 */
	public final static int Large_Young_Squash = 12777;
	/**
	 * Field High_Quality_Large_Squash. (value is 12778)
	 */
	public final static int High_Quality_Large_Squash = 12778;
	/**
	 * Field Low_Quality_Large_Squash. (value is 12779)
	 */
	public final static int Low_Quality_Large_Squash = 12779;
	/**
	 * Field King_Squash. (value is 13016)
	 */
	public final static int King_Squash = 13016;
	/**
	 * Field Emperor_Squash. (value is 13017)
	 */
	public final static int Emperor_Squash = 13017;
	/**
	 * Field Squash_Level_up. (value is 4513)
	 */
	public final static int Squash_Level_up = 4513;
	/**
	 * Field Squash_Poisoned. (value is 4514)
	 */
	public final static int Squash_Poisoned = 4514;
	/**
	 * Field textOnSpawn.
	 */
	private static final String[] textOnSpawn = new String[]
	{
		"scripts.events.TheFallHarvest.SquashAI.textOnSpawn.0",
		"scripts.events.TheFallHarvest.SquashAI.textOnSpawn.1",
		"scripts.events.TheFallHarvest.SquashAI.textOnSpawn.2"
	};
	/**
	 * Field textOnAttack.
	 */
	private static final String[] textOnAttack = new String[]
	{
		"Bites rat-a-tat... to change... body...!",
		"Ha ha, grew up! Completely on all!",
		"Cannot to aim all? Had a look all to flow out...",
		"Is that also calculated hit? Look for person which has the strength!",
		"Don't waste your time!",
		"Ha, this sound is really pleasant to hear?",
		"I eat your attack to grow!",
		"Time to hit again! Come again!",
		"Only useful music can open big pumpkin... It can not be opened with weapon!"
	};
	/**
	 * Field textTooFast.
	 */
	private static final String[] textTooFast = new String[]
	{
		"heh heh,looks well hit!",
		"yo yo? Your skill is mediocre?",
		"Time to hit again! Come again!",
		"I eat your attack to grow!",
		"Make an effort... to get down like this, I walked...",
		"What is this kind of degree to want to open me? Really is indulges in fantasy!",
		"Good fighting method. Evidently flies away the fly also can overcome.",
		"Strives to excel strength oh! But waste your time..."
	};
	/**
	 * Field textSuccess0.
	 */
	private static final String[] textSuccess0 = new String[]
	{
		"The lovely pumpkin young fruit start to glisten when taken to the threshing ground! From now on will be able to grow healthy and strong!",
		"Oh, Haven't seen for a long time?",
		"Suddenly, thought as soon as to see my beautiful appearance?",
		"Well! This is something! Is the nectar?",
		"Refuels! Drink 5 bottles to be able to grow into the big pumpkin oh!"
	};
	/**
	 * Field textFail0.
	 */
	private static final String[] textFail0 = new String[]
	{
		"If I drink nectar, I can grow up faster!",
		"Come, believe me, sprinkle a nectar! I can certainly turn the big pumpkin!!!",
		"Take nectar to come, pumpkin nectar!"
	};
	/**
	 * Field textSuccess1.
	 */
	private static final String[] textSuccess1 = new String[]
	{
		"Wish the big pumpkin!",
		"completely became the recreation area! Really good!",
		"Guessed I am mature or am rotten?",
		"Nectar is just the best! Ha! Ha! Ha!"
	};
	/**
	 * Field textFail1.
	 */
	private static final String[] textFail1 = new String[]
	{
		"oh! Randomly missed! Too quickly sprinkles the nectar?",
		"If I die like this, you only could get young pumpkin...",
		"Cultivate a bit faster! The good speech becomes the big pumpkin, the young pumpkin is not good!",
		"The such small pumpkin you all must eat? Bring the nectar, I can be bigger!"
	};
	/**
	 * Field textSuccess2.
	 */
	private static final String[] textSuccess2 = new String[]
	{
		"Young pumpkin wishing! Has how already grown up?",
		"Already grew up! Quickly sneaked off...",
		"Graciousness, is very good. Come again to see, now felt more and more well"
	};
	/**
	 * Field textFail2.
	 */
	private static final String[] textFail2 = new String[]
	{
		"Hey! Was not there! Here is! Here! Not because I can not properly care? Small!",
		"Wow, stops? Like this got down to have to thank",
		"Hungry for a nectar oh...",
		"Do you want the big pumpkin? But I like young pumpkin..."
	};
	/**
	 * Field textSuccess3.
	 */
	private static final String[] textSuccess3 = new String[]
	{
		"Big pumpkin wishing! Ask, to sober!",
		"Rumble rumble... it's really tasty! Hasn't it?",
		"Cultivating me just to eat? Good, is casual your... not to give the manna on the suicide!"
	};
	/**
	 * Field textFail3.
	 */
	private static final String[] textFail3 = new String[]
	{
		"Isn't it the water you add? What flavor?",
		"Master, rescue my... I don't have the nectar flavor, I must die..."
	};
	/**
	 * Field textSuccess4.
	 */
	private static final String[] textSuccess4 = new String[]
	{
		"is very good, does extremely well! Knew what next step should make?",
		"If you catch me, I give you 10 million adena!!! Agree?"
	};
	/**
	 * Field textFail4.
	 */
	private static final String[] textFail4 = new String[]
	{
		"Hungry for a nectar oh...",
		"If I drink nectar, I can grow up faster!"
	};
	/**
	 * Field _npcId.
	 */
	int _npcId;
	/**
	 * Field _nectar.
	 */
	private int _nectar;
	/**
	 * Field _tryCount.
	 */
	private int _tryCount;
	/**
	 * Field _lastNectarUse.
	 */
	private long _lastNectarUse;
	/**
	 * Field _timeToUnspawn.
	 */
	long _timeToUnspawn;
	/**
	 * Field _polimorphTask.
	 */
	private ScheduledFuture<?> _polimorphTask;
	/**
	 * Field NECTAR_REUSE.
	 */
	private static final int NECTAR_REUSE = 3000;
	
	/**
	 * Constructor for SquashAI.
	 * @param actor NpcInstance
	 */
	public SquashAI(NpcInstance actor)
	{
		super(actor);
		_npcId = getActor().getNpcId();
		Functions.npcSayCustomMessage(getActor(), textOnSpawn[Rnd.get(textOnSpawn.length)]);
		_timeToUnspawn = System.currentTimeMillis() + 120000;
	}
	
	/**
	 * Method thinkActive.
	 * @return boolean
	 */
	@Override
	protected boolean thinkActive()
	{
		if (System.currentTimeMillis() > _timeToUnspawn)
		{
			_timeToUnspawn = Long.MAX_VALUE;
			if (_polimorphTask != null)
			{
				_polimorphTask.cancel(false);
				_polimorphTask = null;
			}
			final SquashInstance actor = getActor();
			if (actor != null)
			{
				actor.deleteMe();
			}
		}
		return false;
	}
	
	/**
	 * Method onEvtSeeSpell.
	 * @param skill Skill
	 * @param caster Creature
	 */
	@Override
	protected void onEvtSeeSpell(Skill skill, Creature caster)
	{
		final SquashInstance actor = getActor();
		if ((actor == null) || (skill.getId() != 2005))
		{
			return;
		}
		switch (_tryCount)
		{
			case 0:
				_tryCount++;
				_lastNectarUse = System.currentTimeMillis();
				if (Rnd.chance(50))
				{
					_nectar++;
					Functions.npcSay(actor, textSuccess0[Rnd.get(textSuccess0.length)]);
					actor.broadcastPacket(new MagicSkillUse(actor, actor, Squash_Level_up, 1, NECTAR_REUSE, 0));
				}
				else
				{
					Functions.npcSay(actor, textFail0[Rnd.get(textFail0.length)]);
					actor.broadcastPacket(new MagicSkillUse(actor, actor, Squash_Poisoned, 1, NECTAR_REUSE, 0));
				}
				break;
			case 1:
				if ((System.currentTimeMillis() - _lastNectarUse) < NECTAR_REUSE)
				{
					Functions.npcSay(actor, textTooFast[Rnd.get(textTooFast.length)]);
					return;
				}
				_tryCount++;
				_lastNectarUse = System.currentTimeMillis();
				if (Rnd.chance(50))
				{
					_nectar++;
					Functions.npcSay(actor, textSuccess1[Rnd.get(textSuccess1.length)]);
					actor.broadcastPacket(new MagicSkillUse(actor, actor, Squash_Level_up, 1, NECTAR_REUSE, 0));
				}
				else
				{
					Functions.npcSay(actor, textFail1[Rnd.get(textFail1.length)]);
					actor.broadcastPacket(new MagicSkillUse(actor, actor, Squash_Poisoned, 1, NECTAR_REUSE, 0));
				}
				break;
			case 2:
				if ((System.currentTimeMillis() - _lastNectarUse) < NECTAR_REUSE)
				{
					Functions.npcSay(actor, textTooFast[Rnd.get(textTooFast.length)]);
					return;
				}
				_tryCount++;
				_lastNectarUse = System.currentTimeMillis();
				if (Rnd.chance(50))
				{
					_nectar++;
					Functions.npcSay(actor, textSuccess2[Rnd.get(textSuccess2.length)]);
					actor.broadcastPacket(new MagicSkillUse(actor, actor, Squash_Level_up, 1, NECTAR_REUSE, 0));
				}
				else
				{
					Functions.npcSay(actor, textFail2[Rnd.get(textFail2.length)]);
					actor.broadcastPacket(new MagicSkillUse(actor, actor, Squash_Poisoned, 1, NECTAR_REUSE, 0));
				}
				break;
			case 3:
				if ((System.currentTimeMillis() - _lastNectarUse) < NECTAR_REUSE)
				{
					Functions.npcSay(actor, textTooFast[Rnd.get(textTooFast.length)]);
					return;
				}
				_tryCount++;
				_lastNectarUse = System.currentTimeMillis();
				if (Rnd.chance(50))
				{
					_nectar++;
					Functions.npcSay(actor, textSuccess3[Rnd.get(textSuccess3.length)]);
					actor.broadcastPacket(new MagicSkillUse(actor, actor, Squash_Level_up, 1, NECTAR_REUSE, 0));
				}
				else
				{
					Functions.npcSay(actor, textFail3[Rnd.get(textFail3.length)]);
					actor.broadcastPacket(new MagicSkillUse(actor, actor, Squash_Poisoned, 1, NECTAR_REUSE, 0));
				}
				break;
			case 4:
				if ((System.currentTimeMillis() - _lastNectarUse) < NECTAR_REUSE)
				{
					Functions.npcSay(actor, textTooFast[Rnd.get(textTooFast.length)]);
					return;
				}
				_tryCount++;
				_lastNectarUse = System.currentTimeMillis();
				if (Rnd.chance(50))
				{
					_nectar++;
					Functions.npcSay(actor, textSuccess4[Rnd.get(textSuccess4.length)]);
					actor.broadcastPacket(new MagicSkillUse(actor, actor, Squash_Level_up, 1, NECTAR_REUSE, 0));
				}
				else
				{
					Functions.npcSay(actor, textFail4[Rnd.get(textFail4.length)]);
					actor.broadcastPacket(new MagicSkillUse(actor, actor, Squash_Poisoned, 1, NECTAR_REUSE, 0));
				}
				if (_npcId == Young_Squash)
				{
					if (_nectar < 3)
					{
						_npcId = Low_Quality_Squash;
					}
					else if (_nectar == 5)
					{
						_npcId = King_Squash;
					}
					else
					{
						_npcId = High_Quality_Squash;
					}
				}
				else if (_npcId == Large_Young_Squash)
				{
					if (_nectar < 3)
					{
						_npcId = Low_Quality_Large_Squash;
					}
					else if (_nectar == 5)
					{
						_npcId = Emperor_Squash;
					}
					else
					{
						_npcId = High_Quality_Large_Squash;
					}
				}
				_polimorphTask = ThreadPoolManager.getInstance().schedule(new PolimorphTask(), NECTAR_REUSE);
				break;
		}
	}
	
	/**
	 * Method onEvtAttacked.
	 * @param attacker Creature
	 * @param damage int
	 */
	@Override
	protected void onEvtAttacked(Creature attacker, int damage)
	{
		final SquashInstance actor = getActor();
		if ((actor != null) && Rnd.chance(5))
		{
			Functions.npcSay(actor, textOnAttack[Rnd.get(textOnAttack.length)]);
		}
	}
	
	/**
	 * Method onEvtDead.
	 * @param killer Creature
	 */
	@Override
	protected void onEvtDead(Creature killer)
	{
		_tryCount = -1;
		final SquashInstance actor = getActor();
		if (actor == null)
		{
			return;
		}
		double dropMod = 1.5;
		switch (_npcId)
		{
			case Low_Quality_Squash:
				dropMod *= 1;
				Functions.npcSay(actor, "The pampkin opens!!!");
				Functions.npcSay(actor, "ya yo! Opens! Good thing many...");
				break;
			case High_Quality_Squash:
				dropMod *= 2;
				Functions.npcSay(actor, "The pampkin opens!!!");
				Functions.npcSay(actor, "ya yo! Opens! Good thing many...");
				break;
			case King_Squash:
				dropMod *= 4;
				Functions.npcSay(actor, "The pampkin opens!!!");
				Functions.npcSay(actor, "ya yo! Opens! Good thing many...");
				break;
			case Low_Quality_Large_Squash:
				dropMod *= 12.5;
				Functions.npcSay(actor, "The pampkin opens!!!");
				Functions.npcSay(actor, "ya yo! Opens! Good thing many...");
				break;
			case High_Quality_Large_Squash:
				dropMod *= 25;
				Functions.npcSay(actor, "The pampkin opens!!!");
				Functions.npcSay(actor, "ya yo! Opens! Good thing many...");
				break;
			case Emperor_Squash:
				dropMod *= 50;
				Functions.npcSay(actor, "The pampkin opens!!!");
				Functions.npcSay(actor, "ya yo! Opens! Good thing many...");
				break;
			default:
				dropMod *= 0;
				Functions.npcSay(actor, "Ouch, if I had died like this, you could obtain nothing!");
				Functions.npcSay(actor, "The news about my death shouldn't spread, oh!");
				break;
		}
		super.onEvtDead(actor);
		if (dropMod > 0)
		{
			if (_polimorphTask != null)
			{
				_polimorphTask.cancel(false);
				_polimorphTask = null;
				Log.add("TheFallHarvest :: Player " + actor.getSpawner().getName() + " tried to use cheat (SquashAI clone): killed " + actor + " after polymorfing started", "illegal-actions");
				return;
			}
			for (RewardData d : _dropList)
			{
				List<RewardItem> itd = d.roll(null, dropMod);
				for (RewardItem i : itd)
				{
					actor.dropItem(actor.getSpawner(), i.itemId, i.count);
				}
			}
		}
	}
	
	/**
	 * Method randomAnimation.
	 * @return boolean
	 */
	@Override
	protected boolean randomAnimation()
	{
		return false;
	}
	
	/**
	 * Method randomWalk.
	 * @return boolean
	 */
	@Override
	protected boolean randomWalk()
	{
		return false;
	}
	
	/**
	 * Method getActor.
	 * @return SquashInstance
	 */
	@Override
	public SquashInstance getActor()
	{
		return (SquashInstance) super.getActor();
	}
}
