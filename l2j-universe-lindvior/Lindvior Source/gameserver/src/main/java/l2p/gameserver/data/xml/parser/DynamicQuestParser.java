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

import l2p.commons.data.xml.AbstractDirParser;
import l2p.gameserver.Config;
import l2p.gameserver.data.xml.holder.DynamicQuestHolder;
import l2p.gameserver.data.xml.holder.StartItemHolder;
import l2p.gameserver.data.xml.holder.StartItemHolder.StartItem;
import l2p.gameserver.model.Zone;
import l2p.gameserver.templates.DynamicQuestTemplate;
import l2p.gameserver.templates.DynamicQuestTemplate.DynamicQuestDate;
import l2p.gameserver.utils.ReflectionUtils;
import org.dom4j.Element;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DynamicQuestParser extends AbstractDirParser<DynamicQuestHolder> {
    private static final DynamicQuestParser _instance = new DynamicQuestParser();

    public DynamicQuestParser() {
        super(DynamicQuestHolder.getInstance());
    }

    public static DynamicQuestParser getInstance() {
        return _instance;
    }

    @Override
    public File getXMLDir() {
        return new File(Config.DATAPACK_ROOT, "data/stats/world/campaigns/");
    }

    @Override
    public boolean isIgnored(File f) {
        return false;
    }

    @Override
    public String getDTDFileName() {
        return "campaigns.dtd";
    }

    @Override
    protected void readData(Element rootElement) throws Exception {
        int questId;
        String questName;
        int minLevel;
        String spawnGroup;
        List<DynamicQuestDate> dates;
        for (Element questElement : rootElement.elements("quest")) {
            questId = Integer.parseInt(questElement.attributeValue("id"));
            questName = questElement.attributeValue("name");

            Element levelElement = questElement.element("level");
            Element spawnElement = questElement.element("spawn");
            Element datesElement = questElement.element("dates");
            Element stepsElement = questElement.element("steps");

            minLevel = levelElement != null ? Integer.parseInt(levelElement.attributeValue("min")) : 100;
            spawnGroup = spawnElement != null ? spawnElement.attributeValue("name") : null;

            dates = new ArrayList<DynamicQuestDate>();
            if (datesElement != null) {
                for (Element dateElement : datesElement.elements("date")) {
                    String dayOfWeek = dateElement.attributeValue("day");
                    int hour = Integer.parseInt(dateElement.attributeValue("time").split(":")[0]);
                    int minute = Integer.parseInt(dateElement.attributeValue("time").split(":")[1]);

                    dates.add(new DynamicQuestTemplate.DynamicQuestDate(dayOfWeek, hour, minute));
                }
            }

            for (Element stepElement : stepsElement.elements("step")) {
                int taskId = Integer.parseInt(stepElement.attributeValue("id"));
                int winPoints = Integer.parseInt(stepElement.attributeValue("points"));
                int nextTaskId = Integer.parseInt(stepElement.attributeValue("nextTaskId", "-1"));
                int duration = Integer.parseInt(stepElement.attributeValue("duration"));

                String taskName = stepElement.attributeValue("name", questName);
                String taskTitle = stepElement.attributeValue("title", questName);

                boolean autostart = Boolean.parseBoolean(stepElement.attributeValue("autostart", "true"));

                Map<String, String> dialogs = new HashMap<String, String>();
                Map<Integer, Integer> points = new HashMap<Integer, Integer>();

                List<Zone> zones = new ArrayList<Zone>();

                List<StartItem> rewards = new ArrayList<StartItem>();
                List<StartItem> eliteRewards = new ArrayList<StartItem>();

                Element zonesElement = stepElement.element("zones");
                Element pointsElement = stepElement.element("points");
                Element rewardsElement = stepElement.element("rewards");
                Element dialogsElement = stepElement.element("dialogs");

                if (zonesElement != null) {
                    for (Element zoneElement : zonesElement.elements("zone")) {
                        zones.add(ReflectionUtils.getZone(zoneElement.attributeValue("name")));
                    }
                }
                if (pointsElement != null) {
                    for (Element pointElement : pointsElement.elements("npc")) {
                        points.put(Integer.parseInt(pointElement.attributeValue("id")), Integer.parseInt(pointElement.attributeValue("points")));
                    }
                }
                if (rewardsElement != null) {
                    for (Element rewardElement : rewardsElement.elements("reward")) {
                        StartItemHolder.StartItem item = new StartItemHolder.StartItem();

                        item.id = Integer.parseInt(rewardElement.attributeValue("id"));
                        item.id = Integer.parseInt(rewardElement.attributeValue("count"));

                        if (rewardElement.attributeValue("type").equalsIgnoreCase("usual")) {
                            rewards.add(item);
                        } else {
                            eliteRewards.add(item);
                        }
                    }
                }
                if (dialogsElement != null) {
                    for (Element dialogElement : dialogsElement.elements("dialog")) {
                        dialogs.put(dialogElement.attributeValue("type"), dialogElement.attributeValue("name"));
                    }
                }
                getHolder().addDynamicQuest(taskId, new DynamicQuestTemplate(questId, taskId, taskName, questName, taskTitle, duration, minLevel, winPoints, nextTaskId, autostart, spawnGroup, zones, dates, points, rewards, eliteRewards, dialogs));
            }
        }
    }
}
