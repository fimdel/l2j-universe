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
package lineage2.gameserver.data.xml.holder;

import gnu.trove.iterator.TIntObjectIterator;
import gnu.trove.map.hash.TIntObjectHashMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lineage2.commons.data.xml.AbstractHolder;
import lineage2.commons.lang.ArrayUtils;
import lineage2.gameserver.templates.npc.NpcTemplate;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class NpcHolder extends AbstractHolder
{
	/**
	 * Field _instance.
	 */
	private static final NpcHolder _instance = new NpcHolder();
	/**
	 * Field _npcs.
	 */
	private final TIntObjectHashMap<NpcTemplate> _npcs = new TIntObjectHashMap<>(30000);
	/**
	 * Field _npcsByLevel.
	 */
	private TIntObjectHashMap<List<NpcTemplate>> _npcsByLevel;
	/**
	 * Field _allTemplates.
	 */
	private NpcTemplate[] _allTemplates;
	/**
	 * Field _npcsNames.
	 */
	private Map<String, NpcTemplate> _npcsNames;
	
	/**
	 * Method getInstance.
	 * @return NpcHolder
	 */
	public static NpcHolder getInstance()
	{
		return _instance;
	}
	
	/**
	 * Constructor for NpcHolder.
	 */
	NpcHolder()
	{
	}
	
	/**
	 * Method addTemplate.
	 * @param template NpcTemplate
	 */
	public void addTemplate(NpcTemplate template)
	{
		_npcs.put(template.npcId, template);
	}
	
	/**
	 * Method getTemplate.
	 * @param id int
	 * @return NpcTemplate
	 */
	public NpcTemplate getTemplate(int id)
	{
		NpcTemplate npc = ArrayUtils.valid(_allTemplates, id);
		if (npc == null)
		{
			warn("Not defined npc id : " + id + ", or out of range!", new Exception());
			return null;
		}
		return _allTemplates[id];
	}
	
	/**
	 * Method getTemplateByName.
	 * @param name String
	 * @return NpcTemplate
	 */
	public NpcTemplate getTemplateByName(String name)
	{
		return _npcsNames.get(name.toLowerCase());
	}
	
	/**
	 * Method getAllOfLevel.
	 * @param lvl int
	 * @return List<NpcTemplate>
	 */
	public List<NpcTemplate> getAllOfLevel(int lvl)
	{
		return _npcsByLevel.get(lvl);
	}
	
	/**
	 * Method getAll.
	 * @return NpcTemplate[]
	 */
	public NpcTemplate[] getAll()
	{
		return _npcs.values(new NpcTemplate[_npcs.size()]);
	}
	
	/**
	 * Method buildFastLookupTable.
	 */
	private void buildFastLookupTable()
	{
		_npcsByLevel = new TIntObjectHashMap<>();
		_npcsNames = new HashMap<>();
		int highestId = 0;
		for (int id : _npcs.keys())
		{
			if (id > highestId)
			{
				highestId = id;
			}
		}
		_allTemplates = new NpcTemplate[highestId + 1];
		for (TIntObjectIterator<NpcTemplate> iterator = _npcs.iterator(); iterator.hasNext();)
		{
			iterator.advance();
			int npcId = iterator.key();
			NpcTemplate npc = iterator.value();
			_allTemplates[npcId] = npc;
			List<NpcTemplate> byLevel;
			if ((byLevel = _npcsByLevel.get(npc.level)) == null)
			{
				_npcsByLevel.put(npcId, byLevel = new ArrayList<>());
			}
			byLevel.add(npc);
			_npcsNames.put(npc.name.toLowerCase(), npc);
		}
	}
	
	/**
	 * Method process.
	 */
	@Override
	protected void process()
	{
		buildFastLookupTable();
	}
	
	/**
	 * Method size.
	 * @return int
	 */
	@Override
	public int size()
	{
		return _npcs.size();
	}
	
	/**
	 * Method clear.
	 */
	@Override
	public void clear()
	{
		_npcsNames.clear();
		_npcs.clear();
	}
}
