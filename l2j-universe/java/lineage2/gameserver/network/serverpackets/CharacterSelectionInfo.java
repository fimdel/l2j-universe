package lineage2.gameserver.network.serverpackets;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import lineage2.commons.dbutils.DbUtils;
import lineage2.gameserver.Config;
import lineage2.gameserver.dao.CharacterDAO;
import lineage2.gameserver.database.DatabaseFactory;
import lineage2.gameserver.model.CharSelectionInfo;
import lineage2.gameserver.model.CharSelectionInfo.CharSelectInfoPackage;
import lineage2.gameserver.model.base.ClassId;
import lineage2.gameserver.model.base.Experience;
import lineage2.gameserver.model.base.Race;
import lineage2.gameserver.model.base.SubClassType;
import lineage2.gameserver.model.items.Inventory;
import lineage2.gameserver.utils.AutoBan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CharacterSelectionInfo extends L2GameServerPacket
{
	private static final Logger _log = LoggerFactory.getLogger(CharacterSelectionInfo.class);

	private String _loginName;

	private int _sessionId;

	private CharSelectionInfo charSelectionInfo;

	public CharacterSelectionInfo(String loginName, int sessionId)
	{
		_sessionId = sessionId;
		_loginName = loginName;
		charSelectionInfo = loadCharacterSelectInfo(loginName);
	}

	public CharSelectionInfo getCharInfo()
	{
		return charSelectionInfo;
	}

	@Override
	protected final void writeImpl()
	{
		int size = charSelectionInfo.size();

		writeC(0x09);
		writeD(size);
		writeD(0x07);
		writeC(0x00);
		writeC(0x01);
		writeD(0x00);

		long lastAccess = -1L;
		int lastUsed = -1;
		for (CharSelectInfoPackage info : charSelectionInfo)
			if (lastAccess < info.getLastAccess())
			{
				lastAccess = info.getLastAccess();
				lastUsed++;
			}
		int i = 0;
		for (CharSelectInfoPackage charInfoPackage : charSelectionInfo)
		{
			writeS(charInfoPackage.getName());
			writeD(charInfoPackage.getCharId()); // ?
			writeS(_loginName);
			writeD(_sessionId);
			writeD(charInfoPackage.getClanId());
			writeD(0x00); // ??

			writeD(charInfoPackage.getSex());
			writeD(charInfoPackage.getRace());
			writeD(charInfoPackage.getBaseClassId());

			writeD(0x01);

			writeD(charInfoPackage.getX());
			writeD(charInfoPackage.getY());
			writeD(charInfoPackage.getZ());

			writeF(charInfoPackage.getCurrentHp());
			writeF(charInfoPackage.getCurrentMp());

			writeD(charInfoPackage.getSp());
			writeQ(charInfoPackage.getExp());
			int lvl = charInfoPackage.getLevel();
			writeF(Experience.getExpPercent(lvl, charInfoPackage.getExp()));
			writeD(lvl);

			writeD(charInfoPackage.getKarma());
			writeD(charInfoPackage.getPk());
			writeD(charInfoPackage.getPvP());

			for (int j = 0; j < 7; j++)
				writeD(0x00);

			for (int PAPERDOLL_ID : Inventory.PAPERDOLL_ORDER)
				writeD(charInfoPackage.getPaperdollItemId(PAPERDOLL_ID));

			writeD(charInfoPackage.getVisualItemId(Inventory.PAPERDOLL_RHAND));
			writeD(charInfoPackage.getVisualItemId(Inventory.PAPERDOLL_LHAND));
			writeD(charInfoPackage.getVisualItemId(Inventory.PAPERDOLL_GLOVES));
			writeD(charInfoPackage.getVisualItemId(Inventory.PAPERDOLL_CHEST));
			writeD(charInfoPackage.getVisualItemId(Inventory.PAPERDOLL_LEGS));
			writeD(charInfoPackage.getVisualItemId(Inventory.PAPERDOLL_FEET));
			writeD(0x00);
			writeD(charInfoPackage.getVisualItemId(Inventory.PAPERDOLL_HAIR));
			writeD(charInfoPackage.getVisualItemId(Inventory.PAPERDOLL_DHAIR));

			writeD(charInfoPackage.getHairStyle());
			writeD(charInfoPackage.getHairColor());
			writeD(charInfoPackage.getFace());

			writeF(charInfoPackage.getMaxHp()); // hp max
			writeF(charInfoPackage.getMaxMp()); // mp max

			writeD(charInfoPackage.getAccessLevel() > -100 ? charInfoPackage.getDeleteTimer() : -1);
			writeD(charInfoPackage.getClassId());
			writeD(i == lastUsed ? 1 : 0);

			writeC(Math.min(charInfoPackage.getPaperdollEnchantEffect(Inventory.PAPERDOLL_RHAND), 127));
			//writeD(charInfoPackage.getPaperdollAugmentationId(Inventory.PAPERDOLL_RHAND));
                        
			writeH(0x00);
			writeH(0x00);
            
			int weaponId = charInfoPackage.getPaperdollItemId(Inventory.PAPERDOLL_RHAND);
			if (weaponId == 8190) // Transform id
				writeD(301);
			else if (weaponId == 8689)
				writeD(302);
			else
				writeD(0x00);

                        for (int j = 0; j < 4; j++)
                                writeD(0x00);
                        
			writeF(0x00);
			writeF(0x00);

			writeD(charSelectionInfo.getVitalityPoints());
			writeD(200); // Vitality percent
			writeD(0x00); //Vitaliti items count
			writeD(charInfoPackage.getAccessLevel() > -100 ? 0x01 : 0x00);

			writeC(0x00);
			writeC(0x00);
			i++;
		}
	}

	public static CharSelectionInfo loadCharacterSelectInfo(String loginName)
	{
		CharSelectionInfo charSelectionInfo = new CharSelectionInfo();

		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("SELECT * FROM characters AS c LEFT JOIN character_subclasses AS cs ON (c.obj_Id=cs.char_obj_id AND cs.active=1) WHERE account_name=? LIMIT 7");
			statement.setString(1, loginName);
			rset = statement.executeQuery();
			while (rset.next())
				charSelectionInfo.addSelectionInfo(restoreChar(rset));
			DbUtils.closeQuietly(statement, rset);
			statement = con.prepareStatement("SELECT `points` FROM `vitality_points` WHERE `account_name`=?");
			statement.setString(1, loginName);
			rset = statement.executeQuery();
			if (rset.next())
			{
				int points = rset.getInt(1);
				charSelectionInfo.setVitalityPoints(points);
			}
		}
		catch (Exception e)
		{
			_log.error("could not restore charinfo:", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}

		return charSelectionInfo;
	}

	/**
	 * Method restoreBaseClassId.
	 * @param objId int
	 * @return int[]
	 */
	private static int[] restoreBaseClassId(int objId)
	{
		int[] classId = new int[2];
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("SELECT class_id, default_class_id FROM character_subclasses WHERE char_obj_id=? AND type=" + SubClassType.BASE_CLASS.ordinal());
			statement.setInt(1, objId);
			rset = statement.executeQuery();
			while (rset.next())
			{
				classId[0] = rset.getInt("class_id");
				classId[1] = rset.getInt("default_class_id");
			}
		}
		catch (Exception e)
		{
			_log.error("could not restore base class id:", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
		return classId;
	}

	private static CharSelectInfoPackage restoreChar(ResultSet chardata)
	{
		CharSelectInfoPackage charInfopackage = null;
		try
		{
			int objectId = chardata.getInt("obj_Id");
			int classid = chardata.getInt("class_id");
			int[] baseClassId = new int[]
			{
				classid,
				chardata.getInt("default_class_id")
			};
			boolean useBaseClass = chardata.getInt("type") == SubClassType.BASE_CLASS.ordinal();
			if (!useBaseClass)
			{
				baseClassId = restoreBaseClassId(objectId);
			}
			Race race = ClassId.VALUES[baseClassId[1]].getRace();
			String name = chardata.getString("char_name");
			charInfopackage = new CharSelectInfoPackage(objectId, name);
			charInfopackage.setLevel(chardata.getInt("level"));
			charInfopackage.setMaxHp(chardata.getInt("maxHp"));
			charInfopackage.setCurrentHp(chardata.getDouble("curHp"));
			charInfopackage.setMaxMp(chardata.getInt("maxMp"));
			charInfopackage.setCurrentMp(chardata.getDouble("curMp"));

			charInfopackage.setX(chardata.getInt("x"));
			charInfopackage.setY(chardata.getInt("y"));
			charInfopackage.setZ(chardata.getInt("z"));
			charInfopackage.setPk(chardata.getInt("pkkills"));
			charInfopackage.setPvP(chardata.getInt("pvpkills"));

			charInfopackage.setFace(chardata.getInt("face"));
			charInfopackage.setHairStyle(chardata.getInt("hairstyle"));
			charInfopackage.setHairColor(chardata.getInt("haircolor"));
			charInfopackage.setSex(chardata.getInt("sex"));

			charInfopackage.setExp(chardata.getLong("exp"));
			charInfopackage.setSp(chardata.getInt("sp"));
			charInfopackage.setClanId(chardata.getInt("clanid"));

			charInfopackage.setKarma(chardata.getInt("karma"));
			charInfopackage.setRace(race.ordinal());
			charInfopackage.setClassId(classid);
			charInfopackage.setBaseClassId(baseClassId[0]);
			long deletetime = chardata.getLong("deletetime");
			int deletedays = 0;
			if (Config.DELETE_DAYS > 0)
				if (deletetime > 0)
				{
					deletetime = (int) (System.currentTimeMillis() / 1000 - deletetime);
					deletedays = (int) (deletetime / 3600 / 24);
					if (deletedays >= Config.DELETE_DAYS)
					{
						CharacterDAO.getInstance().deleteCharByObjId(objectId);
						return null;
					}
					deletetime = Config.DELETE_DAYS * 3600 * 24 - deletetime;
				}
				else
					deletetime = 0;
			charInfopackage.setDeleteTimer((int) deletetime);
			charInfopackage.setLastAccess(chardata.getLong("lastAccess") * 1000L);
			charInfopackage.setAccessLevel(chardata.getInt("accesslevel"));

			if (charInfopackage.getAccessLevel() < 0 && !AutoBan.isBanned(objectId))
				charInfopackage.setAccessLevel(0);
		}
		catch (Exception e)
		{
			_log.error("", e);
		}

		return charInfopackage;
	}
}