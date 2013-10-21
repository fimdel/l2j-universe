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
package lineage2.gameserver.templates.spawn;

import lineage2.commons.collections.MultiValueSet;
import lineage2.gameserver.data.xml.holder.NpcHolder;
import lineage2.gameserver.templates.npc.NpcTemplate;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class SpawnNpcInfo
{
	/**
	 * Field _template.
	 */
	private final NpcTemplate _template;
	/**
	 * Field _max.
	 */
	private final int _max;
	/**
	 * Field _parameters.
	 */
	private final MultiValueSet<String> _parameters;
	
	/**
	 * Constructor for SpawnNpcInfo.
	 * @param npcId int
	 * @param max int
	 * @param set MultiValueSet<String>
	 */
	public SpawnNpcInfo(int npcId, int max, MultiValueSet<String> set)
	{
		_template = NpcHolder.getInstance().getTemplate(npcId);
		_max = max;
		_parameters = set;
	}
	
	/**
	 * Method getTemplate.
	 * @return NpcTemplate
	 */
	public NpcTemplate getTemplate()
	{
		return _template;
	}
	
	/**
	 * Method getMax.
	 * @return int
	 */
	public int getMax()
	{
		return _max;
	}
	
	/**
	 * Method getParameters.
	 * @return MultiValueSet<String>
	 */
	public MultiValueSet<String> getParameters()
	{
		return _parameters;
	}
}
