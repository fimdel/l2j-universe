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
package lineage2.gameserver.stats;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lineage2.commons.lang.ArrayUtils;
import lineage2.gameserver.stats.funcs.Func;
import lineage2.gameserver.stats.funcs.FuncTemplate;
import lineage2.gameserver.stats.triggers.TriggerInfo;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class StatTemplate
{
	/**
	 * Field _funcTemplates.
	 */
	protected FuncTemplate[] _funcTemplates = FuncTemplate.EMPTY_ARRAY;
	/**
	 * Field _triggerList.
	 */
	protected List<TriggerInfo> _triggerList = Collections.emptyList();
	
	/**
	 * Method getTriggerList.
	 * @return List<TriggerInfo>
	 */
	public List<TriggerInfo> getTriggerList()
	{
		return _triggerList;
	}
	
	/**
	 * Method addTrigger.
	 * @param f TriggerInfo
	 */
	public void addTrigger(TriggerInfo f)
	{
		if (_triggerList.isEmpty())
		{
			_triggerList = new ArrayList<>(4);
		}
		_triggerList.add(f);
	}
	
	/**
	 * Method attachFunc.
	 * @param f FuncTemplate
	 */
	public void attachFunc(FuncTemplate f)
	{
		_funcTemplates = ArrayUtils.add(_funcTemplates, f);
	}
	
	/**
	 * Method getAttachedFuncs.
	 * @return FuncTemplate[]
	 */
	public FuncTemplate[] getAttachedFuncs()
	{
		return _funcTemplates;
	}
	
	/**
	 * Method getStatFuncs.
	 * @param owner Object
	 * @return Func[]
	 */
	public Func[] getStatFuncs(Object owner)
	{
		if (_funcTemplates.length == 0)
		{
			return Func.EMPTY_FUNC_ARRAY;
		}
		Func[] funcs = new Func[_funcTemplates.length];
		for (int i = 0; i < funcs.length; i++)
		{
			funcs[i] = _funcTemplates[i].getFunc(owner);
		}
		return funcs;
	}
}
