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
package lineage2.gameserver.skills.effects;

import lineage2.gameserver.ai.CtrlEvent;
import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.model.Effect;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.network.serverpackets.ChangeWaitType;
import lineage2.gameserver.network.serverpackets.Revive;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.stats.Env;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class EffectFakeDeath extends Effect
{
	/**
	 * Constructor for EffectFakeDeath.
	 * @param env Env
	 * @param template EffectTemplate
	 */
	public EffectFakeDeath(Env env, EffectTemplate template)
	{
		super(env, template);
	}
	
	/**
	 * Method onStart.
	 */
	@Override
	public void onStart()
	{
		super.onStart();
		Player player = (Player) getEffected();
		player.setFakeDeath(true);
		player.getAI().notifyEvent(CtrlEvent.EVT_FAKE_DEATH, null, null);
		player.broadcastPacket(new ChangeWaitType(player, ChangeWaitType.WT_START_FAKEDEATH));
		player.broadcastCharInfo();
	}
	
	/**
	 * Method onExit.
	 */
	@Override
	public void onExit()
	{
		super.onExit();
		Player player = (Player) getEffected();
		player.setNonAggroTime(System.currentTimeMillis() + 5000L);
		player.setFakeDeath(false);
		player.broadcastPacket(new ChangeWaitType(player, ChangeWaitType.WT_STOP_FAKEDEATH));
		player.broadcastPacket(new Revive(player));
		player.broadcastCharInfo();
	}
	
	/**
	 * Method onActionTime.
	 * @return boolean
	 */
	@Override
	public boolean onActionTime()
	{
		if (getEffected().isDead())
		{
			return false;
		}
		double manaDam = calc();
		if (manaDam > getEffected().getCurrentMp())
		{
			if (getSkill().isToggle())
			{
				getEffected().sendPacket(Msg.NOT_ENOUGH_MP);
				getEffected().sendPacket(new SystemMessage(SystemMessage.THE_EFFECT_OF_S1_HAS_BEEN_REMOVED).addSkillName(getSkill().getId(), getSkill().getDisplayLevel()));
				return false;
			}
		}
		getEffected().reduceCurrentMp(manaDam, null);
		return true;
	}
}
