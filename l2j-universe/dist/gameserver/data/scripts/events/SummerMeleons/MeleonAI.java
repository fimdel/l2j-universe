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
package events.SummerMeleons;

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
import npc.model.MeleonInstance;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class MeleonAI extends Fighter
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
			final MeleonInstance actor = getActor();
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
				npc.setAI(new MeleonAI(npc));
				((MeleonInstance) npc).setSpawner(actor.getSpawner());
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
		new RewardData(960, 1, 1, 300)
	};
	/**
	 * Field Young_Watermelon. (value is 13271)
	 */
	public final static int Young_Watermelon = 13271;
	/**
	 * Field Rain_Watermelon. (value is 13273)
	 */
	public final static int Rain_Watermelon = 13273;
	/**
	 * Field Defective_Watermelon. (value is 13272)
	 */
	public final static int Defective_Watermelon = 13272;
	/**
	 * Field Young_Honey_Watermelon. (value is 13275)
	 */
	public final static int Young_Honey_Watermelon = 13275;
	/**
	 * Field Rain_Honey_Watermelon. (value is 13277)
	 */
	public final static int Rain_Honey_Watermelon = 13277;
	/**
	 * Field Defective_Honey_Watermelon. (value is 13276)
	 */
	public final static int Defective_Honey_Watermelon = 13276;
	/**
	 * Field Large_Rain_Watermelon. (value is 13274)
	 */
	public final static int Large_Rain_Watermelon = 13274;
	/**
	 * Field Large_Rain_Honey_Watermelon. (value is 13278)
	 */
	public final static int Large_Rain_Honey_Watermelon = 13278;
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
		"scripts.events.SummerMeleons.MeleonAI.textOnSpawn.0",
		"scripts.events.SummerMeleons.MeleonAI.textOnSpawn.1",
		"scripts.events.SummerMeleons.MeleonAI.textOnSpawn.2"
	};
	/**
	 * Field textOnAttack.
	 */
	private static final String[] textOnAttack = new String[]
	{
		"�?то мен�? ку�?ает? ай! ой! �?й ты, �?ейча�? �? тебе задам!",
		"Ха-ха-ха! Я выро�? в�?ем на зави�?т�?, тол�?ко по�?мотри!",
		"Ты �?ов�?ем мазила? �?опа�?т�? во фрукт не може�?�?!",
		"Это ты так про�?читывае�?�? �?вои удары? �?оищи луч�?е учител�? прицеливани�?...",
		"�?е трат�? �?вое врем�?, �? бе�?�?мертен!",
		"Ха! �?равда при�?тный звук?",
		"�?ока ты атакуе�?�? �? ро�?ту, а выра�?ту, �?тану вы�?е теб�? в два раза!",
		"Ты б�?е�?�? или щекоче�?�?? �?е могу разобрат�?... Жалкие потуги!",
		"Тол�?ко музыкал�?ное оружие открывает арбуз. Твое затупленное оружие зде�?�? не помощник!"
	};
	/**
	 * Field textTooFast.
	 */
	private static final String[] textTooFast = new String[]
	{
		"Вот �?то удары! Вот �?то техника!",
		"Эй ты! Твои навыки плачевны, мо�? бабу�?ка дерет�?�? луч�?е! Ха-Ха-Ха!!!",
		"�?у-ка еще разок удар�?, и �?нова!",
		"Я твой дом труба �?атал!",
		"Слы�?�?, а �?емки е�?т�?? �? п�?т�? аден? �? позвонит�?? Хахаха!!!",
		"�?акие непри�?тойно�?ти! Давай без �?тих �?уточек!",
		"�?ро�?ви фантази�?, подойди �?зади, что ты там топче�?�?�?�?!",
		"Разбуди как буде�?�? уходит�?, ты �?ов�?ем уныл и �?кучен..."
	};
	/**
	 * Field textSuccess0.
	 */
	private static final String[] textSuccess0 = new String[]
	{
		"�?рбуз хоро�?о ра�?тет е�?ли его тщател�?но поит�?, ты знае�?�? �?тот �?екрет, не так ли?",
		"Вот �?то �? понима�? нектар, а то в�?егда какие-то помои!",
		"Я вижу! Вижу! Это �?итай! �? боже, �? китай�?кий арбуз!!",
		"Давай наливай еще, между первой и второй перерывчик небол�?�?ой!",
		"Дозаправка на лету!"
	};
	/**
	 * Field textFail0.
	 */
	private static final String[] textFail0 = new String[]
	{
		"Ты оглох? �?не нужен нектар, а не то что ты л�?е�?�?!",
		"�?акой же ты неудачник, а выгл�?ди�?�? вроде бы бодро! �?не нужен нектар, ле�� каче�?твенный, иначе получи�?�? �?и�?�?!",
		"�? �?нова fail, �?кол�?ко же можно? Ты мен�? на�?ме�?ит�? хоче�?�??"
	};
	/**
	 * Field textSuccess1.
	 */
	private static final String[] textSuccess1 = new String[]
	{
		"Сейча�? �?по�?! �?рбуууууу-�?-�?-�?-�?!",
		"Вот так хоро�?о, так очен�? хоро�?о, не о�?танавливай�?�?!",
		"Я ро�?ту бы�?тро, у�?пее�?�? от�?кочит�?? Ха!",
		"Да ты ма�?тер �?воего дела! �?родолжай, про�?у!"
	};
	/**
	 * Field textFail1.
	 */
	private static final String[] textFail1 = new String[]
	{
		"�?у�� железо не отход�? от ка�?�?ы! �? иначе никаких тебе коврижек.",
		"Ра�?т�?па! �?евежда! �?лух! �?еудачник! �?п�?т�? ты м��е �?кормил помои!",
		"Давай-ка активней мен�? окучивай, поливай хоро�?ен�?ко! Что �?то за жалкие потуги?",
		"Ты хоче�?�? чтобы �? так и помер? Давай выращивай правил�?ней!"
	};
	/**
	 * Field textSuccess2.
	 */
	private static final String[] textSuccess2 = new String[]
	{
		"Вот! Вот так! Давай же, и �?коро �? пол�?бл�? теб�? нав�?егда!",
		"Такими темпами �? �?тану императором арб��зов!",
		"�?чен�? хоро�?о, �?тавл�? тебе зачет по аграрному хоз�?й�?тву, ты �? умом мен�? ра�?ти�?�?!"
	};
	/**
	 * Field textFail2.
	 */
	private static final String[] textFail2 = new String[]
	{
		"�? ты вообще ме�?тный? Ты арбуз в глаза видел? Это провал!",
		"�?одар�? тебе табличку Лузер Года, тол�?ко неудачник может так плохо �?правл�?т�?�?�? �? таким про�?тым делом!",
		"�?у покорми мен�?, а? �?ормал�?но тол�?ко, а не вот �?тим �?омнител�?ным нектаром...",
		"�? ты �?лучаем не террори�?т? �?оже�?�? ты мен�? голодом мори�?�?? Чего тебе надо?!!"
	};
	/**
	 * Field textSuccess3.
	 */
	private static final String[] textSuccess3 = new String[]
	{
		"Жизн�? налаживает�?�?, лей не жалей!",
		"Теб�? �?тому мама учила? У теб�? здорово получает�?�?!",
		"�? зачем ты ро�?ти�?�? мен�?? Е�?т�? буде�?�?? Я буду очен�? �?очным арбузом!"
	};
	/**
	 * Field textFail3.
	 */
	private static final String[] textFail3 = new String[]
	{
		"Это что, водичка из канализации? Ты понимае�?�? что такое нектар?!",
		"Боги, �?па�?ите мен�? от �?того неумехи, он же в�?е портит!"
	};
	/**
	 * Field textSuccess4.
	 */
	private static final String[] textSuccess4 = new String[]
	{
		"Вот �?то зар�?д!! Ты что подме�?ал в нектар? Там точно граду�?ов 40! �?хахаха! Я п�?�?не�?!",
		"Ты ри�?куе�?�? выра�?тит�? не арбуз, а целу�? ракету! �?одливай, давай еще!"
	};
	/**
	 * Field textFail4.
	 */
	private static final String[] textFail4 = new String[]
	{
		"�?х как �? хочу пит�?... �?ектар, про�?у...",
		"Лей �?�?да нектар и по�?мотри что получит�?�?!"
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
	 * Constructor for MeleonAI.
	 * @param actor NpcInstance
	 */
	public MeleonAI(NpcInstance actor)
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
			final MeleonInstance actor = getActor();
			actor.deleteMe();
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
		final MeleonInstance actor = getActor();
		if ((actor == null) || (skill.getId() != 2005))
		{
			return;
		}
		if ((actor.getNpcId() != Young_Watermelon) && (actor.getNpcId() != Young_Honey_Watermelon))
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
				if (_npcId == Young_Watermelon)
				{
					if (_nectar < 3)
					{
						_npcId = Defective_Watermelon;
					}
					else if (_nectar == 5)
					{
						_npcId = Large_Rain_Watermelon;
					}
					else
					{
						_npcId = Rain_Watermelon;
					}
				}
				else if (_npcId == Young_Honey_Watermelon)
				{
					if (_nectar < 3)
					{
						_npcId = Defective_Honey_Watermelon;
					}
					else if (_nectar == 5)
					{
						_npcId = Large_Rain_Honey_Watermelon;
					}
					else
					{
						_npcId = Rain_Honey_Watermelon;
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
		final MeleonInstance actor = getActor();
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
		final MeleonInstance actor = getActor();
		if (actor == null)
		{
			return;
		}
		double dropMod = 1.5;
		switch (_npcId)
		{
			case Defective_Watermelon:
				dropMod *= 1;
				Functions.npcSay(actor, "�?рбуз открывает�?�?!");
				Functions.npcSay(actor, "�?хо-хо! Да тут жалкие крохи, �?тарай�?�? луч�?е!");
				break;
			case Rain_Watermelon:
				dropMod *= 2;
				Functions.npcSay(actor, "�?рбуз открывает�?�?!");
				Functions.npcSay(actor, "�?й-ай-ай! �?еплохой улов!");
				break;
			case Large_Rain_Watermelon:
				dropMod *= 4;
				Functions.npcSay(actor, "�?рбуз открывает�?�?!");
				Functions.npcSay(actor, "Вот �?то да! �?акие �?окровища!");
				break;
			case Defective_Honey_Watermelon:
				dropMod *= 12.5;
				Functions.npcSay(actor, "�?рбуз открывает�?�?!");
				Functions.npcSay(actor, "�?отратил много, а выудил мало!");
				break;
			case Rain_Honey_Watermelon:
				dropMod *= 25;
				Functions.npcSay(actor, "�?рбуз открывает�?�?!");
				Functions.npcSay(actor, "Бум-бум-бах! Улов хоро�?!");
				break;
			case Large_Rain_Honey_Watermelon:
				dropMod *= 50;
				Functions.npcSay(actor, "�?рбуз открывает�?�?!");
				Functions.npcSay(actor, "Фанфары! Ты открыл гигант�?кий арбуз! �?е�?метные богат�?тва на земле! Лови их!");
				break;
			default:
				dropMod *= 0;
				Functions.npcSay(actor, "Я вед�? ничего тебе не дам, е�?ли умру вот так...");
				Functions.npcSay(actor, "Этот позор навеки покроет твое им�?...");
				break;
		}
		super.onEvtDead(actor);
		if (dropMod > 0)
		{
			if (_polimorphTask != null)
			{
				_polimorphTask.cancel(false);
				_polimorphTask = null;
				Log.add("SummerMeleons :: Player " + actor.getSpawner().getName() + " tried to use cheat (SquashAI clone): killed " + actor + " after polymorfing started", "illegal-actions");
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
	 * @return MeleonInstance
	 */
	@Override
	public MeleonInstance getActor()
	{
		return (MeleonInstance) super.getActor();
	}
}
