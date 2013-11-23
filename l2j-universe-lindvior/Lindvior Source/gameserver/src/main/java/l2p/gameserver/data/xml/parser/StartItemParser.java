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
package l2p.gameserver.data.xml.parser;

import l2p.commons.data.xml.AbstractFileParser;
import l2p.gameserver.Config;
import l2p.gameserver.data.xml.holder.StartItemHolder;
import org.dom4j.Element;

import java.io.File;

public class StartItemParser extends AbstractFileParser<StartItemHolder> {
    private static StartItemParser _instance = new StartItemParser();

    protected StartItemParser() {
        super(StartItemHolder.getInstance());
    }

    public static StartItemParser getInstance() {
        return _instance;
    }

    @Override
    public File getXMLFile() {
        return new File(Config.DATAPACK_ROOT, "data/stats/player/start_items.xml");
    }

    @Override
    public String getDTDFileName() {
        return "start_items.dtd";
    }

    @Override
    protected void readData(Element rootElement) throws Exception {
        StartItemHolder.StartItem template = null;
        int classId;
        for (Element equipmentElement : rootElement.elements()) {
            classId = Integer.parseInt(equipmentElement.attributeValue("classId"));
            for (Element item : equipmentElement.elements()) {
                template = new StartItemHolder.StartItem();
                try {
                    template.id = Integer.parseInt(item.attributeValue("id"));
                    template.count = Integer.parseInt(item.attributeValue("count"));
                    template.equipped = Boolean.parseBoolean(item.attributeValue("equipped", "false"));
                    template.warehouse = Boolean.parseBoolean(item.attributeValue("warehouse", "false"));

                    getHolder().addStartItem(template, classId);
                } catch (NumberFormatException e) {
                    this._log.warn("Cold not load item: " + e.getMessage());
                }
            }
        }
    }
}