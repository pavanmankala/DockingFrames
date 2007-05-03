package bibliothek.gui.dock.action.views.buttons;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import bibliothek.gui.dock.Dockable;
import bibliothek.gui.dock.action.DockAction;
import bibliothek.gui.dock.action.DockActionSource;
import bibliothek.gui.dock.action.DropDownAction;
import bibliothek.gui.dock.action.StandardDockAction;
import bibliothek.gui.dock.action.dropdown.DropDownFilter;
import bibliothek.gui.dock.action.dropdown.DropDownView;
import bibliothek.gui.dock.action.views.ViewTarget;
import bibliothek.gui.dock.action.views.dropdown.DropDownViewItem;
import bibliothek.gui.dock.event.DockActionSourceListener;
import bibliothek.gui.dock.event.DropDownActionListener;
import bibliothek.gui.dock.event.StandardDockActionListener;
import bibliothek.gui.dock.title.DockTitle.Orientation;

/**
 * A handler that connects a {@link DropDownAction} with a {@link DropDownMiniButton}.
 * @author Benjamin Sigg
 *
 * @param <D> the type of actions used with this handler
 * @param <T> the type of button used with this handler
 */
public class DropDownMiniButtonHandler<
			D extends DropDownAction,
			T extends DropDownMiniButton>
		implements MiniButtonHandler<D, T>{
	
	/** the action to observe and to trigger */
	private D action;
	/** the current source of child-actions */
	private DockActionSource source;
	/** the element for which the action is shown */
	private Dockable dockable;
	/** the view */
	private T button;
	/** a listener to the model */
	private Listener listener = new Listener();
	
	/** the currently selected item, can be <code>null</code> */
	private Item selection;
	/** the currently known actions */
	private List<DockAction> actions = new ArrayList<DockAction>();
	/** the views for the items of {@link #actions}. Not all actions have a view. */
	private Map<DockAction, Item> items = new HashMap<DockAction, Item>();
	
	/** the menu to show when the button is clicked */
	private JPopupMenu menu = new JPopupMenu();
	
	/** connection between current selection and filter */
	private SelectionView selectionView = new SelectionView();
	/** connection between filter and button */
	private ButtonView buttonView = new ButtonView();
	/** filters the properties of the action and its selection */
	private DropDownFilter filter;
	
	/**
	 * Creates a new handler.
	 * @param action the action to observe
	 * @param button the button that will show <code>action</code>.
	 * @param dockable the element for which the <code>action</code> is shown
	 */
	public DropDownMiniButtonHandler( D action, T button, Dockable dockable ){
		this.action = action;
		this.dockable = dockable;
		this.button = button;
		
		button.setHandler( this );
	}
	
	public void setOrientation( Orientation orientation ){
		button.setOrientation( orientation );
	}

	public void bind(){
		action.bind( dockable );
		filter = action.getFilter( dockable ).createView( action, dockable, buttonView );
		filter.bind();
		
		source = action.getSubActions( dockable );
		
		for( int i = 0, n = source.getDockActionCount(); i<n; i++ ){
			DockAction sub = source.getDockAction( i );
			add( i, sub );
		}
		
		reset();
		selection = items.get( action.getSelection( dockable ) );
		if( selection != null )
			selection.view.setView( selectionView );
		
		action.addDropDownActionListener( listener );
		action.addDockActionListener( listener );
		source.addDockActionSourceListener( listener );
		
		button.setEnabled( action.isEnabled( dockable ) );
	}
	
	public void unbind(){
		action.removeDockActionListener( listener );
		action.removeDropDownActionListener( listener );
		source.removeDockActionSourceListener( listener );
		
		for( int i = actions.size()-1; i >= 0; i-- )
			remove( i );
		
		menu.removeAll();
		
		if( selection != null )
			selection.view.setView( null );
		
		filter.unbind();
		filter = null;
		action.unbind( dockable );
		
		source = null;
		selection = null;
		items.clear();
		actions.clear();
	}

	/**
	 * Adds an action into the list of all known actions.
	 * @param index the location of the action
	 * @param action the new action
	 */
	private void add( int index, DockAction action ){
		actions.add( action );
		DropDownViewItem item = action.createView( ViewTarget.DROP_DOWN, dockable.getController().getActionViewConverter(), dockable );
		if( item != null ){
			Item entry = new Item( action, item );
			entry.bind();
			items.put( action, entry );
			menu.add( item.getItem() );
		}
	}
	
	/**
	 * Removes an action from the list of all known actions.
	 * @param index the location of the action
	 */
	private void remove( int index ){
		DockAction action = actions.remove( index );
		Item item = items.remove( action );
		if( item != null ){
			item.unbind();
			menu.remove( item.view.getItem() );
		}
	}
	
	public D getAction(){
		return action;
	}
	
	public T getButton(){
		return button;
	}
	
	public Dockable getDockable(){
		return dockable;
	}
	
	public JComponent getItem(){
		return button;
	}
	
	public void triggered(){
		if( selection == null || !button.isSelectionEnabled() || !selection.view.isTriggerable(  true ) )
			popupTriggered();
		else{
			if( selection.view.isTriggerable( true ) ){
				selection.view.triggered();
			}
		}
	}
	
	/**
	 * Shows the popup menu
	 */
	public void popupTriggered(){
		if( button.getOrientation().isHorizontal() ){
			menu.show( button, 0, button.getHeight() );
		}
		else{
			menu.show( button, button.getWidth(), 0 );
		}
	}
    
    /**
     * Update the look and feel of the menu
     */
    public void updateUI(){
        if( menu != null )
            SwingUtilities.updateComponentTreeUI( menu );
    }
	
	/**
	 * Gets the view which contains information about the currently selected
	 * action.
	 * @return the information
	 */
	protected ButtonView getButtonView(){
		return buttonView;
	}
	
	/**
	 * Sets all values of the {@link #filter} to <code>null</code>.
	 */
	protected void reset(){
		button.setSelectionEnabled( false );
		if( filter != null ){
			filter.setDisabledIcon( null );
			filter.setEnabled( true );
			filter.setIcon( null );
			filter.setSelected( false );
			filter.setText( null );
			filter.setTooltip( null );
		}
		update();
	}
	
	/**
	 * Updates the {@link #filter}. This might change some contents of the button.
	 */
	protected void update(){
		if( filter != null )
			filter.update( selection == null ? null : selection.view );
	}
    
	/**
	 * Represents an action and its view.
	 * @author Benjamin Sigg
	 */
	private class Item implements ActionListener{
		/** the action */
		private DockAction item;
		/** the view of {@link #item} */
		private DropDownViewItem view;
		
		/**
		 * Creates a new item.
		 * @param item the action
		 * @param view the view of <code>item</code>
		 */
		public Item( DockAction item, DropDownViewItem view ){
			this.item = item;
			this.view = view;
		}
		
		/**
		 * Connects the view.
		 */
		public void bind(){
			view.bind();
			view.addActionListener( this );
		}
		
		/**
		 * Disconnects the view
		 */
		public void unbind(){
			view.removeActionListener( this );
			view.unbind();
		}
		
		public void actionPerformed( ActionEvent e ){
			if( view.isSelectable() )
				action.setSelection( dockable, item );
		}
	}
	
	/**
	 * A set of properties which can be set by the selected action.
	 * @author Benjamin Sigg
	 */
	protected class SelectionView implements DropDownView{
		public void setEnabled( boolean enabled ){
			button.setSelectionEnabled( enabled );
			filter.setEnabled( enabled );
			update();
		}

		public void setIcon( Icon icon ){
			filter.setIcon( icon );
			update();
		}

		public void setDisabledIcon( Icon icon ){
			filter.setDisabledIcon( icon );
			update();
		}
		
		public void setSelected( boolean selected ){
			filter.setSelected( selected );
			update();
		}

		public void setText( String text ){
			filter.setText( text );
			update();
		}

		public void setTooltip( String tooltip ){
			filter.setTooltip( tooltip );
			update();
		}
	}
	
	/**
	 * A view that sends all values directly to the button.
	 * @author Benjamin Sigg
	 */
	protected class ButtonView implements DropDownView{
		public void setDisabledIcon( Icon icon ){
			button.setDisabledIcon( icon );
		}

		public void setEnabled( boolean enabled ){
			button.setSelectionEnabled( enabled );
		}

		public void setIcon( Icon icon ){
			button.setIcon( icon );
		}

		public void setSelected( boolean selected ){
			button.setSelected( selected );
		}

		public void setText( String text ){
			// ignore
		}

		public void setTooltip( String tooltip ){
			button.setToolTipText( tooltip );
		}
	}
	
	/**
	 * A listener to the action that is handled by this handler
	 * @author Benjamin Sigg
	 */
	private class Listener implements StandardDockActionListener, DropDownActionListener, DockActionSourceListener{
		public void actionEnabledChanged( StandardDockAction action, Set<Dockable> dockables ){
			if( dockables.contains( dockable ))
				button.setEnabled( action.isEnabled( dockable ) );
		}

		public void actionIconChanged( StandardDockAction action, Set<Dockable> dockables ){
			if( dockables.contains( dockable ))
				update();
		}
		
		public void actionDisabledIconChanged( StandardDockAction action, Set<Dockable> dockables ){
			if( dockables.contains( dockable ))
				update();
		}

		public void actionTextChanged( StandardDockAction action, Set<Dockable> dockables ){
			if( dockables.contains( dockable ))
				update();
		}

		public void actionTooltipTextChanged( StandardDockAction action, Set<Dockable> dockables ){
			if( dockables.contains( dockable ))
				update();
		}
		
		public void selectionChanged( DropDownAction action, Set<Dockable> dockables, DockAction newSelection ){
			if( selection != null )
				selection.view.setView( null );
			
			reset();
			selection = items.get( newSelection );
			
			if( selection != null )
				selection.view.setView( selectionView );
			
			button.repaint();
		}

		public void actionsAdded( DockActionSource source, int firstIndex, int lastIndex ){
			for( int i = firstIndex; i <= lastIndex; i++ )
				add( i, source.getDockAction( i ));
		}

		public void actionsRemoved( DockActionSource source, int firstIndex, int lastIndex ){
			for( int i = lastIndex; i >= firstIndex; i-- )
				remove( i );
		}
	}
}