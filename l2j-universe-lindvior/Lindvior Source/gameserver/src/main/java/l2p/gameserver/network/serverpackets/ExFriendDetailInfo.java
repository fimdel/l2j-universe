/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.serverpackets;

import l2p.gameserver.dao.CharacterDAO;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.World;

/**
 * @author Darvin
 */
public class ExFriendDetailInfo extends L2GameServerPacket {
    private static final String _S__FE_ExFriendDetailInfo_0XEB = "[S] 83 ExFriendDetailInfo";
    Player player;
    int friendObjId;
    String name;
    int isOnline;
    int level;
    int classId;
    int clanId;
    int clanCrestId = 0;
    String clanName = "";
    int allyId = 0;
    int allyCrestId = 0;
    String allyName = "";
    int createdMonth;
    int createdDay;
    long lastLogin;
    String memo;

    public ExFriendDetailInfo(Player activeChar, int objId) {
        player = activeChar;
        friendObjId = objId;
        name = CharacterDAO.getInstance().getNameByObjectId(objId);
        isOnline = World.getPlayer(objId) != null ? 1 : 0;
        //  memo = activeChar.getFriendMemo(objId);
        if (isOnline == 1) {
            Player friend = World.getPlayer(objId);
            level = friend.getLevel();
            classId = friend.getClassId().getId();
            clanId = friend.getClanId();
            if (clanId != 0) {
                clanCrestId = friend.getClanId();
                clanName = friend.getClan().getName();
                allyId = friend.getAllyId();
                allyCrestId = friend.getAllyId();
                allyName = friend.getClan().getName();
            }
            //  createdMonth = friend.getCreateDate().get(Calendar.MONTH) + 1;
            //    createdDay = friend.getCreateDate().get(Calendar.DAY_OF_MONTH);
            lastLogin = (int) friend.getLastAccess();
        }
        //	else
        //		offlineFriendInfo(objId);
    }

/*	private void offlineFriendInfo(int objId)
    {
		String createDate = "";
		int level = 0;
		int bClassId = 0;
		Connection con = null;

		try
		{
			// Retrieve the Player from the characters table of the database
			con = L2DatabaseFactory.getInstance().getConnection();
			
			PreparedStatement statement = con.prepareStatement("SELECT * FROM characters WHERE charId=?");
			statement.setInt(1, objId);
			ResultSet rset = statement.executeQuery();
			while (rset.next())
			{
				level = (rset.getByte("level"));
				if (rset.getInt("classid") > 138)
					classId = AwakingManager.getInstance().getAwakeningClassToInfo(rset.getInt("classid"));
				else
					classId = rset.getInt("classid");
				if (rset.getInt("base_class") > 138)
					bClassId = AwakingManager.getInstance().getAwakeningClassToInfo(rset.getInt("base_class"));
				else
					bClassId = rset.getInt("base_class");
				clanId = (rset.getInt("clanid"));
				lastLogin = (rset.getLong("lastAccess"));
				createDate = (rset.getString("createDate"));
			}
			statement.execute();
			
			rset.close();
			statement.close();
		}
		catch (Exception e)
		{
			System.out.println("Failed loading character. " + e);
		}
		finally
		{
			DbUtils.closeQuietly(con);
		}
		if (classId != bClassId)
		{
			try
			{
				// Retrieve the Player from the characters table of the database
				con = L2DatabaseFactory.getInstance().getConnection();
				
				PreparedStatement statement = con.prepareStatement("SELECT level FROM character_subclasses WHERE charId=? AND class_id=?");
				statement.setInt(1, objId);
				statement.setInt(2, classId);
				ResultSet rset = statement.executeQuery();
				
				while (rset.next())
					this.level = (rset.getByte("level"));

				statement.execute();
				rset.close();
				statement.close();
			}
			catch (Exception e)
			{
				System.out.println("Failed loading character_subclasses. " + e);
			}
			finally
			{
				DbUtils.closeQuietly(con);
			}
		}
		else
			this.level = level;
		if (clanId != 0)
		{
			try
			{
				// Retrieve the Player from the characters table of the database
				con = L2DatabaseFactory.getInstance().getConnection();
				
				PreparedStatement statement = con.prepareStatement("SELECT * FROM clan_data WHERE clan_id=?");
				statement.setInt(1, clanId);
				ResultSet rset = statement.executeQuery();
				while (rset.next())
				{
					clanName = (rset.getString("clan_name"));
					clanCrestId = (rset.getInt("crest_id"));
					allyId = (rset.getInt("ally_id"));
					allyName = (rset.getString("ally_name"));
					allyCrestId = (rset.getInt("ally_crest_id"));
				}
				statement.execute();
				rset.close();
				statement.close();
			}
			catch (Exception e)
			{
				System.out.println("Failed loading clan_data. " + e);
			}
			finally
			{
				DbUtils.closeQuietly(con);
			}
		}
		createdMonth = Integer.parseInt(createDate.substring(5, 7));
		createdDay = Integer.parseInt(createDate.substring(8));
	}*/


    @Override
    protected void writeImpl() {
        writeEx(0xEC);
        // TODO: when implementing, consult an up-to-date packets_game_server.xml and/or savormix
        writeD(player.getObjectId()); // Character ID
        writeS(name); // Name
        writeD(isOnline); // Online
        writeD(isOnline == 1 ? friendObjId : 0x00); // Friend OID
        writeH(level); // Level
        writeH(classId); // Class
        writeD(clanId); // Pledge ID
        writeD(clanCrestId); // Pledge crest ID
        writeS(clanName); // Pledge name
        writeD(allyId); // Alliance ID
        writeD(allyCrestId); // Alliance crest ID
        writeS(allyName); // Alliance name
        writeC(createdMonth); // Creation month
        writeC(createdDay); // Creation day
        writeD(isOnline == 1 ? (int) System.currentTimeMillis() : (int) (System.currentTimeMillis() - lastLogin) / 1000);
        writeS(memo); // Memo
    }

    @Override
    public String getType() {
        return _S__FE_ExFriendDetailInfo_0XEB;
    }
}
