/* This class encapsulates all of the windowing and GUI stuff. Should only
 * be instansiated from Render class.
 * 
 * I've based this on the design from my Drawing program, i.e. a toolbar at the
 * top and the display area below, padded by some panels. We can always change
 * it around later, but this is simple to use and most importantly simple to
 * code.
 * 	-- Chris
 */

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {
	
	/* These variables are NOT final as they may change if the user resizes
	 * the window. This way these values can be referenced easily by other
	 * parts of the program.
	 */
	public static int windowWidth = 768;
	public static int windowHeight = 768;
	
	/* References to remember the important bits of the window */
	private Container contentPane;
	private ControlPanel controlPanel;
	private RenderPane renderPane;
	
	public MainWindow() {
		/* Sets up the window title and close button. */
		super("Cretatous Rendersaurus");
		this.setDefaultCloseOperation( EXIT_ON_CLOSE );
		
		/* Get a handle on the content pane and create a layout manager */
		contentPane = this.getContentPane();
		contentPane.setLayout( new BorderLayout() );

		/* Adds a menu bar to our lovely little window */
		MenuClass MenuSys = new MenuClass();
		setJMenuBar(MenuSys.createMenuBar());
		
		/* Add the RenderPane to the contentPane */
		renderPane = new RenderPane();
		contentPane.add( renderPane, BorderLayout.CENTER );
		
		/* Add the control Panel to the contentPane, change the NORTH to change where it sits */
		controlPanel = new ControlPanel();
		contentPane.add( controlPanel, BorderLayout.NORTH );
		
		/* Add some padding along the edges */
		contentPane.add( new JPanel(), BorderLayout.EAST );
		contentPane.add( new JPanel(), BorderLayout.SOUTH );
		contentPane.add( new JPanel(), BorderLayout.WEST );
				
		/* Sets size and shows the window. */
		this.setSize( windowWidth, windowHeight );
		this.setVisible( true );
	}
	
	public RenderPane getRenderPane() {
		return renderPane;
	}
	
}


