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
package lineage2.gameserver.model.quest;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.model.instances.NpcInstance;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class QuestTimer extends RunnableImpl
{
	/**
	 * Field _name.
	 */
	private final String _name;
	/**
	 * Field _npc.
	 */
	private final NpcInstance _npc;
	/**
	 * Field _time.
	 */
	private long _time;
	/**
	 * Field _qs.
	 */
	private QuestState _qs;
	/**
	 * Field _schedule.
	 */
	private ScheduledFuture<?> _schedule;
	
	/**
	 * Constructor for QuestTimer.
	 * @param name String
	 * @param time long
	 * @param npc NpcInstance
	 */
	public QuestTimer(String name, long time, NpcInstance npc)
	{
		_name = name;
		_time = time;
		_npc = npc;
	}
	
	/**
	 * Method setQuestState.
	 * @param qs QuestState
	 */
	void setQuestState(QuestState qs)
	{
		_qs = qs;
	}
	
	/**
	 * Method getQuestState.
	 * @return QuestState
	 */
	QuestState getQuestState()
	{
		return _qs;
	}
	
	/**
	 * Method start.
	 */
	void start()
	{
		_schedule = ThreadPoolManager.getInstance().schedule(this, _time);
	}
	
	/**
	 * Method runImpl.
	 */
	@Override
	public void runImpl()
	{
		QuestState qs = getQuestState();
		if (qs != null)
		{
			qs.removeQuestTimer(getName());
			qs.getQuest().notifyEvent(getName(), qs, getNpc());
		}
	}
	
	/**
	 * Method pause.
	 */
	void pause()
	{
		if (_schedule != null)
		{
			_time = _schedule.getDelay(TimeUnit.SECONDS);
			_schedule.cancel(false);
		}
	}
	
	/**
	 * Method stop.
	 */
	void stop()
	{
		if (_schedule != null)
		{
			_schedule.cancel(false);
		}
	}
	
	/**
	 * Method isActive.
	 * @return boolean
	 */
	public boolean isActive()
	{
		return (_schedule != null) && !_schedule.isDone();
	}
	
	/**
	 * Method getName.
	 * @return String
	 */
	public String getName()
	{
		return _name;
	}
	
	/**
	 * Method getTime.
	 * @return long
	 */
	public long getTime()
	{
		return _time;
	}
	
	/**
	 * Method getNpc.
	 * @return NpcInstance
	 */
	public NpcInstance getNpc()
	{
		return _npc;
	}
	
	/**
	 * Method toString.
	 * @return String
	 */
	@Override
	public final String toString()
	{
		return _name;
	}
	
	/**
	 * Method equals.
	 * @param o Object
	 * @return boolean
	 */
	@Override
	public boolean equals(Object o)
	{
		if (o == this)
		{
			return true;
		}
		if (o == null)
		{
			return false;
		}
		if (o.getClass() != this.getClass())
		{
			return false;
		}
		return ((QuestTimer) o).getName().equals(getName());
	}
}
