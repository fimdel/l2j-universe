/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

/*
 * Copyright (C) 2004-2013 L2J DataPack
 * 
 * This file is part of L2J DataPack.
 * 
 * L2J DataPack is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * L2J DataPack is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package l2p.gameserver.model.quest.dynamic;

public class DynamicQuestResult implements Comparable {
    private final String _participiant;
    private int _currentContributed;
    private int _additionalContributed;
    private int _totalContributed;

    public DynamicQuestResult(String participiant, int currentContributed, int additionalContributed, int totalContributed) {
        _participiant = participiant;
        _currentContributed = currentContributed;
        _additionalContributed = additionalContributed;
        _totalContributed = totalContributed;
    }

    @Override
    public int compareTo(Object o) {
        if ((o instanceof DynamicQuestResult)) {
            int oValue = ((DynamicQuestResult) o).getContributed(DynamicQuest.ContributionType.TOTAL);
            return oValue > _totalContributed ? 1 : oValue == _totalContributed ? 0 : -1;
        }
        return 0;
    }

    public int getContributed(DynamicQuest.ContributionType type) {
        switch (type.ordinal()) {
            case 1:
                return _currentContributed;
            case 2:
                return _additionalContributed;
            case 3:
                return _totalContributed;
        }

        return 0;
    }

    public void setContributed(DynamicQuest.ContributionType type, int value) {
        switch (type.ordinal()) {
            case 1:
                _currentContributed = value;
                break;
            case 2:
                _additionalContributed = value;
                break;
            case 3:
                _totalContributed = value;
        }
    }

    public String getParticipiant() {
        return _participiant;
    }
}
