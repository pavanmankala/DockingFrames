/*
 * Bibliothek - DockingFrames
 * Library built on Java/Swing, allows the user to "drag and drop"
 * panels containing any Swing-Component the developer likes to add.
 * 
 * Copyright (C) 2008 Benjamin Sigg
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
package bibliothek.extension.gui.dock.preference;

import bibliothek.extension.gui.dock.util.Path;

/**
 * A preference model is a list of objects which represent some preferences
 * of another resource. A preference model has enough information to be displayed
 * in a graphical user interface and be modified by a user.
 * @author Benjamin Sigg
 */
public interface PreferenceModel {
    /**
     * Uses an unknown source to update this model and load all the preferences
     * that are currently available.
     */
    public void read();
    
    /**
     * Writes the current preferences to the location where they are used.
     */
    public void write();
    
    /**
     * Adds a listener to this model.
     * @param listener the new listener
     */
    public void addPreferenceModelListener( PreferenceModelListener listener );
    
    /**
     * Removes a listener from this model.
     * @param listener the listener to remove.
     */
    public void removePreferenceModelListener( PreferenceModelListener listener );
    
    /**
     * Gets the number of preferences stored in this model.
     * @return the number of preferences
     */
    public int getSize();
    
    /**
     * Gets a short label that can be presented to the user for the 
     * <code>index</code>'th object.
     * @param index the number the preference
     * @return a short human readable description
     */
    public String getLabel( int index );
    
    /**
     * Gets a description of the <code>index</code>'th object. The description
     * is a longer text that will be presented to the user.
     * @param index the number of the preference
     * @return the description, might be <code>null</code>, might be formated
     * in HTML
     */
    public String getDescription( int index );
    
    /**
     * Tells whether the operation <code>operation</code> is enabled for
     * the preference at location <code>index</code>.
     * @param index some location
     * @param operation an operation from {@link #getOperations(int)}
     * @return <code>true</code> if the operation is enabled, <code>false</code>
     * if not
     */
    public boolean isEnabled( int index, PreferenceOperation operation );
    
    /**
     * Gets all operations for which this model has a definition for
     * the preference at location <code>index</code>. Note: a {@link PreferenceEditor}
     * has operations as well, if the editor and the model share an operation,
     * then the operation is considered to belong to the editor.
     * @param index the location of a preference
     * @return the list of available operations (enabled and disabled operations),
     * can be <code>null</code>
     */
    public PreferenceOperation[] getOperations( int index );
    
    /**
     * Executes the enabled operation <code>operation</code>.
     * @param index the location of the affected preference
     * @param operation the operation to execute
     */
    public void doOperation( int index, PreferenceOperation operation );
    
    /**
     * Gets the <code>index</code>'th preference. The {@link #getTypePath(int) type path}
     * determines how the value is to be presented on the screen.
     * @param index the number of the preference
     * @return the value or maybe <code>null</code>, has to be immutable
     */
    public Object getValue( int index );
    
    /**
     * Sets the value of the <code>index</code>'th preference.
     * @param index the number of the preference
     * @param value the new value, may be <code>null</code>
     */
    public void setValue( int index, Object value );
    
    /**
     * Tells what kind of type the <code>index</code>'th value is. The type
     * is represented as a path. Most times the path would equal the name of
     * some class. Note: there is a set of standard paths defined in {@link Path}.
     * @param index the number of the value
     * @return a unique path for the type of this value
     */
    public Path getTypePath( int index );
    
    /**
     * Gets the unique location of the <code>index</code>'th preference of
     * this model.
     * @param index the index of the preference
     * @return the unique path, compared to the other paths of this model, 
     * to the preference
     */
    public Path getPath( int index );
}