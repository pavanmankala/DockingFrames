/*
 * Bibliothek - DockingFrames
 * Library built on Java/Swing, allows the user to "drag and drop"
 * panels containing any Swing-Component the developer likes to add.
 * 
 * Copyright (C) 2012 Benjamin Sigg
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 * 
 * Benjamin Sigg
 * benjamin_sigg@gmx.ch
 * CH - Switzerland
 */
package bibliothek.gui.dock.extension.css;

import bibliothek.gui.dock.title.DockTitle;

/**
 * An item represents a set of properties, accessible by some {@link #getPath() key}. For example
 * a {@link DockTitle} could get its properties from a {@link CssItem}.
 * @author Benjamin Sigg
 */
public interface CssItem extends CssPropertyContainer{
	/**
	 * Gets the key to this item. Note that more than one item may have the same path.
	 * @return the location of this node in the tree of items
	 */
	public CssPath getPath();
	
	/**
	 * Adds a listener to this item, the listener will be informed when properties of this
	 * item are changed.
	 * @param listener the new listener, not <code>null</code>
	 */
	public void addItemListener( CssItemListener listener );
	
	/**
	 * Removes <code>listener</code> from this item.
	 * @param listener the listener to remove
	 */
	public void removeItemListener( CssItemListener listener );
}
