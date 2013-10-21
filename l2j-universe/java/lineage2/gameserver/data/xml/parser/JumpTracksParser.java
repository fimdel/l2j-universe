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
package lineage2.gameserver.data.xml.parser;

import java.io.File;
import java.util.Iterator;

import lineage2.commons.data.xml.AbstractFileParser;
import lineage2.gameserver.Config;
import lineage2.gameserver.data.xml.holder.JumpTracksHolder;
import lineage2.gameserver.templates.jump.JumpPoint;
import lineage2.gameserver.templates.jump.JumpTrack;
import lineage2.gameserver.templates.jump.JumpWay;
import lineage2.gameserver.utils.Location;

import org.dom4j.Element;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class JumpTracksParser extends AbstractFileParser<JumpTracksHolder>
{
	/**
	 * Field _instance.
	 */
	private static final JumpTracksParser _instance = new JumpTracksParser();
	
	/**
	 * Method getInstance.
	 * @return JumpTracksParser
	 */
	public static JumpTracksParser getInstance()
	{
		return _instance;
	}
	
	/**
	 * Constructor for JumpTracksParser.
	 */
	protected JumpTracksParser()
	{
		super(JumpTracksHolder.getInstance());
	}
	
	/**
	 * Method getXMLFile.
	 * @return File
	 */
	@Override
	public File getXMLFile()
	{
		return new File(Config.DATAPACK_ROOT, "data/xml/jumps/jumping_tracks.xml");
	}
	
	/**
	 * Method getDTDFileName.
	 * @return String
	 */
	@Override
	public String getDTDFileName()
	{
		return "jumping_tracks.dtd";
	}
	
	/**
	 * Method readData.
	 * @param rootElement Element
	 * @throws Exception
	 */
	@Override
	protected void readData(Element rootElement) throws Exception
	{
		for (Iterator<Element> iterator = rootElement.elementIterator(); iterator.hasNext();)
		{
			Element trackElement = iterator.next();
			int trackId = Integer.parseInt(trackElement.attributeValue("id"));
			Location startLoc = Location.parse(trackElement);
			JumpTrack jumpTrack = new JumpTrack(trackId, startLoc);
			for (Iterator<?> wayIterator = trackElement.elementIterator("way"); wayIterator.hasNext();)
			{
				Element wayElement = (Element) wayIterator.next();
				int wayId = Integer.parseInt(wayElement.attributeValue("id"));
				JumpWay jumpWay = new JumpWay(wayId);
				for (Iterator<?> pointIterator = wayElement.elementIterator("point"); pointIterator.hasNext();)
				{
					Element pointElement = (Element) pointIterator.next();
					Location pointLoc = Location.parse(pointElement);
					int nextWayId = Integer.parseInt(pointElement.attributeValue("next_way_id"));
					jumpWay.addPoint(new JumpPoint(pointLoc, nextWayId));
				}
				jumpTrack.addWay(jumpWay);
			}
			getHolder().addTrack(jumpTrack);
		}
	}
}
