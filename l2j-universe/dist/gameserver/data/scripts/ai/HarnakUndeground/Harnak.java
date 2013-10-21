package ai.HarnakUndeground;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.ai.Fighter;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.tables.SkillTable;
import instances.HarnakUndergroundRuins;

import quests._10338_SeizeYourDestiny;

public class Harnak extends Fighter
{
	private static final int[] SKILL_IDS = { 14612, 14613, 14614 };

	private boolean firstMsg = false;
	private boolean secondMsg = false;
	private boolean thirdMsg = false;
	private boolean sealLaunched = false;

	private int seal_active = 0;

	public Harnak(NpcInstance actor)
	{
		super(actor);
	}

	@Override
	protected void onEvtScriptEvent(String event, Object arg1, Object arg2)
	{
		if(event.equalsIgnoreCase("SEAL_ACTIVATED"))
		{
			seal_active++;
			if(seal_active == 2)
			{
				Reflection r = getActor().getReflection();
				if(!(r instanceof HarnakUndergroundRuins))
					return;
				for(Player p : r.getPlayers())
				{
					QuestState st = p.getQuestState(_10338_SeizeYourDestiny.class);
					if(st != null)
					{
						st.setCond(3);
						st.playSound(Quest.SOUND_MIDDLE);
					}
				}
				((HarnakUndergroundRuins) r).successEndInstance();
			}
		}
	}

	@Override
	protected void onEvtAttacked(Creature attacker, int damage)
	{
		super.onEvtAttacked(attacker, damage);

		if(!firstMsg)
		{
			firstMsg = true;
			getActor().broadcastPacket(new ExShowScreenMessage(NpcString.FREE_ME_FROM_THIS_BINDING_OF_LIGHT, 10000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, ExShowScreenMessage.STRING_TYPE, 0, false, 0));
		}
		else if(!secondMsg && getActor().getCurrentHpPercents() <= 80.0)
		{
			secondMsg = true;
			getActor().broadcastPacket(new ExShowScreenMessage(NpcString.DESTROY_THE_GHOST_OF_HARNAK_THIS_CORRUPTED_CREATURE, 10000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, ExShowScreenMessage.STRING_TYPE, 0, false, 0));
		}
		else if(!thirdMsg && getActor().getCurrentHpPercents() <= 60.0)
		{
			thirdMsg = true;
			getActor().broadcastPacket(new ExShowScreenMessage(NpcString.FREE_ME_AND_I_PROMISE_YOU_THE_POWER_OF_GIANTS, 10000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, ExShowScreenMessage.STRING_TYPE, 0, false, 0));
		}
		else if(!sealLaunched && getActor().getCurrentHpPercents() <= 50.0)
		{
			sealLaunched = true;
			Reflection r = getActor().getReflection();
			if(!(r instanceof HarnakUndergroundRuins))
				return;
			((HarnakUndergroundRuins) r).startLastStage();
		}
		if(Rnd.chance(10))
		{
			int SKILL_ID = SKILL_IDS[0];
			if(getActor().getCurrentHpPercents() < 90)
				SKILL_ID = SKILL_IDS[1];
			else if(getActor().getCurrentHpPercents() < 75)
				SKILL_ID = SKILL_IDS[2];

			Skill skill = SkillTable.getInstance().getInfo(SKILL_ID, 1);
			skill.getEffects(getActor(), getActor(), false, false);
		}
	}

	@Override
	protected void onEvtDead(Creature killer)
	{
		super.onEvtDead(killer);
		Reflection r = getActor().getReflection();
		if(!(r instanceof HarnakUndergroundRuins))
			return;
		((HarnakUndergroundRuins) r).successEndInstance();
	}
}
