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

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public enum QuestEventType
{
	/**
	 * Field MOB_TARGETED_BY_SKILL.
	 */
	MOB_TARGETED_BY_SKILL,
	/**
	 * Field ATTACKED_WITH_QUEST.
	 */
	ATTACKED_WITH_QUEST,
	/**
	 * Field MOB_KILLED_WITH_QUEST.
	 */
	MOB_KILLED_WITH_QUEST,
	/**
	 * Field QUEST_START.
	 */
	QUEST_START,
	/**
	 * Field QUEST_TALK.
	 */
	QUEST_TALK,
	/**
	 * Field NPC_FIRST_TALK.
	 */
	NPC_FIRST_TALK
}
