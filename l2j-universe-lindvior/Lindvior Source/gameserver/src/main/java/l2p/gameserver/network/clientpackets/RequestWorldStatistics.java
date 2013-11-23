package l2p.gameserver.network.clientpackets;


import l2p.gameserver.instancemanager.WorldStatisticsManager;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.WorldStatistic.CategoryType;
import l2p.gameserver.model.WorldStatistic.CharacterStatistic;
import l2p.gameserver.network.serverpackets.ExLoadStatWorldRank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author Darvin
 */
public class RequestWorldStatistics extends L2GameClientPacket {
    private static final Logger _log = LoggerFactory.getLogger(RequestWorldStatistics.class);
    private int _section;
    private int _subSection;

    @Override
    protected void readImpl() throws Exception {
        _section = readD();
        _subSection = readD();
    }

    @Override
    protected void runImpl() throws Exception {
        Player activeChar = getClient().getActiveChar();

        if (activeChar == null)
            return;

        CategoryType cat = CategoryType.getCategoryById(_section, _subSection);

        if (cat == null) {
            _log.warn("RequestWorldStatistics: Not found category for section: " + _section + " subsection: " + _subSection);
            return;
        }

        List<CharacterStatistic> generalStatisticList = WorldStatisticsManager.getInstance().getStatisticTop(cat, true, WorldStatisticsManager.STATISTIC_TOP_PLAYER_LIMIT);
        List<CharacterStatistic> monthlyStatisticList = WorldStatisticsManager.getInstance().getStatisticTop(cat, false, WorldStatisticsManager.STATISTIC_TOP_PLAYER_LIMIT);

        activeChar.sendPacket(new ExLoadStatWorldRank(_section, _subSection, generalStatisticList, monthlyStatisticList));
    }
}
