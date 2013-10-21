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
package lineage2.gameserver.model.mail;

import lineage2.gameserver.model.items.ItemInstance;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Attachment
{
	/**
	 * Field messageId.
	 */
	private int messageId;
	/**
	 * Field item.
	 */
	private ItemInstance item;
	/**
	 * Field mail.
	 */
	private Mail mail;
	
	/**
	 * Method getMessageId.
	 * @return int
	 */
	public int getMessageId()
	{
		return messageId;
	}
	
	/**
	 * Method setMessageId.
	 * @param messageId int
	 */
	public void setMessageId(int messageId)
	{
		this.messageId = messageId;
	}
	
	/**
	 * Method getItem.
	 * @return ItemInstance
	 */
	public ItemInstance getItem()
	{
		return item;
	}
	
	/**
	 * Method setItem.
	 * @param item ItemInstance
	 */
	public void setItem(ItemInstance item)
	{
		this.item = item;
	}
	
	/**
	 * Method getMail.
	 * @return Mail
	 */
	public Mail getMail()
	{
		return mail;
	}
	
	/**
	 * Method setMail.
	 * @param mail Mail
	 */
	public void setMail(Mail mail)
	{
		this.mail = mail;
	}
	
	/**
	 * Method equals.
	 * @param o Object
	 * @return boolean
	 */
	@Override
	public boolean equals(Object o)
	{
		if (o == this)
		{
			return true;
		}
		if (o == null)
		{
			return false;
		}
		if (o.getClass() != this.getClass())
		{
			return false;
		}
		return ((Attachment) o).getItem() == getItem();
	}
}
