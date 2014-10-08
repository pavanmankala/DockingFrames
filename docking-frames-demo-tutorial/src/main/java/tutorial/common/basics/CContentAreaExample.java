package tutorial.common.basics;

import java.awt.BorderLayout;
import java.awt.Color;

import tutorial.support.ColorSingleCDockable;
import tutorial.support.JTutorialFrame;
import bibliothek.gui.dock.DefaultDockable;
import bibliothek.gui.dock.common.CContentArea;
import bibliothek.gui.dock.common.CControl;
import bibliothek.gui.dock.common.CWorkingArea;
import bibliothek.gui.dock.common.DefaultSingleCDockable;

public class CContentAreaExample {
	public static void main( String[] args ){
		/* CControl supports factories to lazily create SingleCDockables. This example
		 * shows how to set up two factories. */
		
		/* creating a frame and a control */
		JTutorialFrame frame = new JTutorialFrame( CContentAreaExample.class );
		CControl control = new CControl( frame );
		frame.destroyOnClose( control );
		frame.add(control.getContentArea(), BorderLayout.CENTER);

		createDockable(control, "Red", Color.RED );
		createDockable(control, "Green", Color.GREEN );
		createDockable(control, "Blue", Color.BLUE );

		CWorkingArea newcca = control.createWorkingArea("my2");
		newcca.show(new ColorSingleCDockable( "newccaRed", Color.RED ));
		newcca.show(new ColorSingleCDockable( "newccaGreen", Color.GREEN ));
		newcca.show(new ColorSingleCDockable( "newccaBlue", Color.BLUE ));

		DefaultSingleCDockable dscd = new DefaultSingleCDockable("workingarea", newcca.getComponent());
		control.addDockable(dscd);
		dscd.setVisible(true);

		frame.setVisible(true);
	}

	static ColorSingleCDockable createDockable(CControl control, String title, Color color) {
	    ColorSingleCDockable dc = new ColorSingleCDockable( title, color );
	    control.addDockable(dc);
	    dc.setVisible(true);
	    return dc;
	}
}
