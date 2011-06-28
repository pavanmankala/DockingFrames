/*
 * Bibliothek - DockingFrames
 * Library built on Java/Swing, allows the user to "drag and drop"
 * panels containing any Swing-Component the developer likes to add.
 * 
 * Copyright (C) 2007 Benjamin Sigg
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

package bibliothek.gui.dock;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;

import javax.swing.Icon;
import javax.swing.LayoutFocusTraversalPolicy;

import bibliothek.gui.DockController;
import bibliothek.gui.DockStation;
import bibliothek.gui.Dockable;
import bibliothek.gui.dock.dockable.AbstractDockable;
import bibliothek.gui.dock.dockable.DefaultDockableFactory;
import bibliothek.gui.dock.dockable.DockableBackgroundComponent;
import bibliothek.gui.dock.dockable.DockableIcon;
import bibliothek.gui.dock.themes.ThemeManager;
import bibliothek.gui.dock.util.BackgroundAlgorithm;
import bibliothek.gui.dock.util.BackgroundPanel;
import bibliothek.gui.dock.util.PropertyKey;
import bibliothek.gui.dock.util.icon.DockIcon;

/**
 * A {@link Dockable} which consists only of one {@link Component} called
 * "content pane". It's possible to add or remove components from the
 * content pane at any time.
 * @author Benjamin Sigg
 */
public class DefaultDockable extends AbstractDockable {
    /** the content pane */
    private BackgroundPanel pane = new BackgroundPanel( new BorderLayout(), true, false );
    
    /** the id used to identify the factory of this dockable */
    private String factoryId = DefaultDockableFactory.ID;
    
    /** the background of this dockable */
    private Background background = new Background();
    
    /**
     * Constructs a new DefaultDockable
     */
    public DefaultDockable(){
        this(  null, null, null );
    }

    /**
     * Constructs a new DefaultDockable and sets the icon.
     * @param icon the icon, to be shown at various places
     */
    public DefaultDockable( Icon icon ){
        this( null, null, icon );
    }
    
    /**
     * Constructs a new DefaultDockable and sets the title.
     * @param title the title, to be shown at various places
     */
    public DefaultDockable( String title ){
        this( null, title, null );
    }
    
    /**
     * Constructs a new DefaultDockable and places one component onto the
     * content pane.
     * @param component the only child of the content pane 
     */
    public DefaultDockable( Component component ){
        this( component, null, null );
    }

    /**
     * Constructs a new DefaultDockable, sets an icon and places one
     * component.
     * @param component the only child of the content pane
     * @param icon the icon, to be shown at various places
     */
    public DefaultDockable( Component component, Icon icon ){
        this( component, null, icon );
    }
    
    /**
     * Constructs a new DefaultDockable, sets the title and places one
     * component.
     * @param component the only child of the content pane
     * @param title the title, to be shown at various places
     */
    public DefaultDockable( Component component, String title ){
        this( component, title, null );
    }
    
    /**
     * Constructs a new DefaultDockable, sets the icon and the title, and
     * places a component.
     * @param component the only child of the content pane
     * @param title the title, to be shown at various places
     * @param icon the icon, to be shown at various places
     */
    public DefaultDockable( Component component, String title, Icon icon ){
    	super( PropertyKey.DOCKABLE_TITLE, PropertyKey.DOCKABLE_TOOLTIP );
    	
    	pane.setFocusable( false );
    	pane.setFocusTraversalPolicyProvider( true );
    	pane.setFocusTraversalPolicy( new LayoutFocusTraversalPolicy() );
    	pane.setBackground( background );
    	
        if( component != null ){
            getContentPane().setLayout( new GridLayout( 1, 1 ));
            getContentPane().add( component );
        }
        
        if( icon != null ){
        	setTitleIcon( icon );
        }
        setTitleText( title );
    }
    
    @Override
    protected DockIcon createTitleIcon(){
	    return new DockableIcon( "dockable.default", this ){
			protected void changed( Icon oldValue, Icon newValue ){
				fireTitleIconChanged( oldValue, newValue );	
			}
		};
    }
    
    public String getFactoryID() {
        return factoryId;
    }
    
    /**
     * Sets the id for the {@link DockFactory} which will be used to store
     * and load this dockable.
     * @param factoryId the id of the factory
     */
    public void setFactoryID( String factoryId ){
    	if( factoryId == null )
    		throw new IllegalArgumentException( "FactoryID must not be null" );
		this.factoryId = factoryId;
	}
    
    public Component getComponent() {
        return pane;
    }

    public DockStation asDockStation() {
        return null;
    }
    
    /**
     * Gets the number of {@link Component}s on this dockable, this is equivalent of calling
     * <code>getContentPane().getComponentCount()</code>.
     * @return the number of components
     * @see #getContentPane()
     * @see Container#getComponentCount()
     */
    public int getComponentCount(){
    	return getContentPane().getComponentCount();
    }

    /**
     * Gets the index'th child of this {@link Dockable}, this is equivalent of calling
     * <code>getContentPane().getComponent( index )</code>.
     * @param index the index of the child
     * @return the component
     * @see #getContentPane()
     * @see Container#getComponent(int)
     */
    public Component getComponent( int index ){
    	return getContentPane().getComponent( index );
    }
    
    /**
     * Gets the first child {@link Component} of this dockable. This is equivalent of calling
     * <code>getContentPane().getComponent( 0 )</code>. For many dockables the first component will be the
     * object that was given to the constructor {@link #DefaultDockable(Component)}.
     * @return the first child or <code>null</code> if no children are present
     */
    public Component getFirstComponent(){
    	if( getComponentCount() == 0 ){
    		return null;
    	}
    	else{
    		return getComponent( 0 );
    	}
    }
    
    /**
     * Gets a panel for children of this Dockable. Clients can do whatever
     * they like, except removing the content pane from its parent.
     * @return the representation of this dockable
     */
    public Container getContentPane(){
        return pane;
    }
    
    /**
     * Adds <code>component</code> to the content pane.
     * @param component the new child
     */
    public void add( Component component ){
        getContentPane().add( component );
    }
    
    /**
     * Adds <code>component</code> to the content pane.
     * @param component the new child
     * @param constraints information for th {@link LayoutManager}
     */
    public void add( Component component, Object constraints ){
        getContentPane().add( component, constraints );
    }
    
    /**
     * Removes <code>component</code> from the content pane.
     * @param component the child to remove
     */
    public void remove( Component component ){
        getContentPane().remove( component );
    }
    
    /**
     * Sets the layout of the content pane. The layout is normaly a
     * {@link FlowLayout}, except the constructor has added a component to the
     * layout. In that case, the layout is a {@link GridLayout}.
     * @param layout the new layout of the content pane
     */
    public void setLayout( LayoutManager layout ){
        getContentPane().setLayout( layout );
    }
    
    @Override
    public void setController( DockController controller ){
    	super.setController( controller );
    	background.setController( controller );
    }
    
    /**
     * A representation of the background of this {@link Dockable}.
     * @author Benjamin Sigg
     *
     */
    private class Background extends BackgroundAlgorithm implements DockableBackgroundComponent{
    	public Background(){
    		super( DockableBackgroundComponent.KIND, ThemeManager.BACKGROUND_PAINT + ".dockable" );
    	}
    	
    	public Component getComponent(){
    		return getDockable().getComponent();
    	}
    	
    	public Dockable getDockable(){
    		return DefaultDockable.this;
    	}
    }
}
