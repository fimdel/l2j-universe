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
package lineage2.gameserver.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import lineage2.commons.dbutils.DbUtils;
import lineage2.gameserver.database.DatabaseFactory;
import lineage2.gameserver.model.Effect;
import lineage2.gameserver.model.Playable;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.skills.EffectType;
import lineage2.gameserver.skills.effects.EffectTemplate;
import lineage2.gameserver.stats.Env;
import lineage2.gameserver.tables.SkillTable;
import lineage2.gameserver.utils.SqlBatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class EffectsDAO
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(EffectsDAO.class);
	/**
	 * Field _instance.
	 */
	private static final EffectsDAO _instance = new EffectsDAO();
	
	/**
	 * Constructor for EffectsDAO.
	 */
	EffectsDAO()
	{
	}
	
	/**
	 * Method getInstance.
	 * @return EffectsDAO
	 */
	public static EffectsDAO getInstance()
	{
		return _instance;
	}
	
	/**
	 * Method restoreEffects.
	 * @param playable Playable
	 */
	public void restoreEffects(Playable playable)
	{
		int objectId, id;
		if (playable.isPlayer())
		{
			objectId = playable.getObjectId();
			id = ((Player) playable).getActiveClassId();
		}
		else if (playable.isServitor())
		{
			objectId = playable.getPlayer().getObjectId();
			id = playable.getObjectId();
		}
		else
		{
			return;
		}
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("SELECT `skill_id`,`skill_level`,`effect_count`,`effect_cur_time`,`duration` FROM `character_effects_save` WHERE `object_id`=? AND `id`=? ORDER BY `order` ASC");
			statement.setInt(1, objectId);
			statement.setInt(2, id);
			rset = statement.executeQuery();
			while (rset.next())
			{
				int skillId = rset.getInt("skill_id");
				int skillLvl = rset.getInt("skill_level");
				int effectCount = rset.getInt("effect_count");
				long effectCurTime = rset.getLong("effect_cur_time");
				long duration = rset.getLong("duration");
				Skill skill = SkillTable.getInstance().getInfo(skillId, skillLvl);
				if (skill == null)
				{
					continue;
				}
				for (EffectTemplate et : skill.getEffectTemplates())
				{
					if (et == null)
					{
						continue;
					}
					Env env = new Env(playable, playable, skill);
					Effect effect = et.getEffect(env);
					if ((effect == null) || effect.isOneTime())
					{
						continue;
					}
					effect.setCount(effectCount);
					effect.setPeriod(effectCount == 1 ? duration - effectCurTime : duration);
					if(et.getEffectType() == EffectType.ServitorShare && playable.isPlayer())
					{
						playable.getPlayer().setServitorShareRestore(true, effect);
						continue;
					}
					playable.getEffectList().addEffect(effect);
				}
			}
			DbUtils.closeQuietly(statement, rset);
			statement = con.prepareStatement("DELETE FROM character_effects_save WHERE object_id = ? AND id=?");
			statement.setInt(1, objectId);
			statement.setInt(2, id);
			statement.execute();
			DbUtils.close(statement);
		}
		catch (final Exception e)
		{
			_log.error("Could not restore active effects data!", e);
		}
		finally
		{
			DbUtils.closeQuietly(con);
		}
	}
	
	/**
	 * Method insert.
	 * @param playable Playable
	 */
	public void insert(Playable playable)
	{
		int objectId, id;
		if (playable.isPlayer())
		{
			objectId = playable.getObjectId();
			id = ((Player) playable).getActiveClassId();
		}
		else if (playable.isServitor())
		{
			objectId = playable.getPlayer().getObjectId();
			id = playable.getObjectId();
		}
		else
		{
			return;
		}
		List<Effect> effects = playable.getEffectList().getAllEffects();
		if (effects.isEmpty())
		{
			return;
		}
		Connection con = null;
		Statement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.createStatement();
			int order = 0;
			SqlBatch b = new SqlBatch("INSERT IGNORE INTO `character_effects_save` (`object_id`,`skill_id`,`skill_level`,`effect_count`,`effect_cur_time`,`duration`,`order`,`id`) VALUES");
			StringBuilder sb;
			for (Effect effect : effects)
			{
				if ((effect != null) && effect.isInUse() && !effect.getSkill().isToggle() && (effect.getEffectType() != EffectType.HealOverTime) && (effect.getEffectType() != EffectType.CombatPointHealOverTime) && (effect.getEffectType() != EffectType.Mentoring))
				{
					if (effect.isSaveable())
					{
						sb = new StringBuilder("(");
						sb.append(objectId).append(',');
						sb.append(effect.getSkill().getId()).append(',');
						sb.append(effect.getSkill().getLevel()).append(',');
						sb.append(effect.getCount()).append(',');
						sb.append(effect.getTime()).append(',');
						sb.append(effect.getPeriod()).append(',');
						sb.append(order).append(',');
						sb.append(id).append(')');
						b.write(sb.toString());
					}
					while (((effect = effect.getNext()) != null) && effect.isSaveable())
					{
						sb = new StringBuilder("(");
						sb.append(objectId).append(',');
						sb.append(effect.getSkill().getId()).append(',');
						sb.append(effect.getSkill().getLevel()).append(',');
						sb.append(effect.getCount()).append(',');
						sb.append(effect.getTime()).append(',');
						sb.append(effect.getPeriod()).append(',');
						sb.append(order).append(',');
						sb.append(id).append(')');
						b.write(sb.toString());
					}
					order++;
				}
			}
			if (!b.isEmpty())
			{
				statement.executeUpdate(b.close());
			}
		}
		catch (final Exception e)
		{
			_log.error("Could not store active effects data!", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
}
