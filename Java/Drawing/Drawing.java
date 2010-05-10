// Smith, Chris, 02386569, Assignment 1, 159235

/*
 * This program creates an interactive environment where the user can draw
 * different shapes, alter their properties, save/load from a file and a JPEG
 * export/print function.
 * 
 * This is quite a large project to cram into one file. I should like to hand
 * it in as a .jar next time! :)
 * 
 * All code released under the GPL.
 * (c)Chris Smith, 2004.
 */

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.*;
import java.io.*;
import com.sun.image.codec.jpeg.*;

public class Drawing extends JFrame {

	/*
	 * This should be enough, considering you can resize the app because of the
	 * layout manager.
	 */
	public static int WIDTH = 786;
	public static int HEIGHT = 786;
	
	/* I Know i really shouldn't, but it makes it easier! I'm sorta using the base
	 * class as a registry so that all inner classes can send data between each other
	 */
	public static GUI drawingGUI;	
	public static Vector shapelist;
	public static Shape ghostShape;
	public static DrawableShape currentShape;
	public static boolean selectModeEnabled;

	public static void main( String args[] ) {
        System.out.println( "----------------------------------------" );
        System.out.println( "|  Assignment 2, Semester 1, 159.235   |" );
        System.out.println( "| Submitted by: Smith, Chris, 02386569 |" );
        System.out.println( "----------------------------------------" );
		new Drawing();
	}

	public Drawing() {
		/* Set the title and other random housekeeping stuff */
		super( "Drawing" );
		setDefaultCloseOperation( EXIT_ON_CLOSE );
		
		/* Create a list of shapes in this drawing */
		shapelist = new Vector();
		currentShape = null;
		ghostShape = null;
		selectModeEnabled = false;

		/* Set up the GUI! */
		drawingGUI = new GUI( this );
		drawingGUI.createMenus();
		drawingGUI.createChoosers();
		drawingGUI.createCanvas();

		setSize( WIDTH, HEIGHT );
		this.setVisible( true );
	}
	
	/* This class makes GUI creation easy! */
	class GUI {

		/*
		 * Stores the application goodies so I can muck with them from inside
		 * this class.
		 */
		private JFrame mainFrame;
		private Container contentPane;
		
		public MenuGUI menus;
		public ChooserGUI choosers;
		public CanvasGUI canvas;

		/* Constructor, needs JFrame reference */
		public GUI( JFrame jf ) {
			mainFrame = jf;
			contentPane = mainFrame.getContentPane();
			contentPane.setLayout( new BorderLayout() );
		}

		/*
		 * Create the menu's, making sure to store a reference to each one for
		 * later reference. RAM is cheap :)
		 */
		public void createMenus() {
			menus = new MenuGUI( mainFrame );
			menus.createMenus();
		}

		/*
		 * Create the "Choosers". Basically drop down combo boxes where users
		 * can select what they want (shape, colour, thickness etc...)
		 */
		public void createChoosers() {
			choosers = new ChooserGUI( contentPane );
			choosers.createChoosers();
		}

		/*
		 * Create the canvas where all the drawing will take place.
		 */
		public void createCanvas() {
			canvas = new CanvasGUI( contentPane );
			canvas.createCanvas();
		}
	}
	
	class MenuGUI implements ActionListener {
			private JFrame mainFrame;
			
			private JMenuBar menuBar;
			private JMenu file;
			private JMenuItem fileLoad;
			private JMenuItem fileSave;
			private JMenuItem fileExportJPEG;
			private JMenuItem fileExit;
			private JMenu help;
			private JMenuItem helpHowToUse;
			private JMenuItem helpAbout;

			public MenuGUI( JFrame jf ) {
				mainFrame = jf;
			}

			public void createMenus() {
				menuBar = new JMenuBar();
				mainFrame.setJMenuBar( menuBar );

				file = new JMenu( "File" );
				menuBar.add( file );

				fileLoad = new JMenuItem( "Load..." );
				fileLoad.addActionListener( this );
				file.add( fileLoad );

				fileSave = new JMenuItem( "Save..." );
				fileSave.addActionListener( this );
				file.add( fileSave );

				fileExportJPEG = new JMenuItem( "Export JPEG file..." );
				fileExportJPEG.addActionListener( this );
				file.add( fileExportJPEG );

				file.addSeparator();

				fileExit = new JMenuItem( "Exit" );
				fileExit.addActionListener( this );
				file.add( fileExit );

				help = new JMenu( "Help" );
				menuBar.add( help );

				helpHowToUse = new JMenuItem( "How to use..." );
				helpHowToUse.addActionListener( this );
				help.add( helpHowToUse );

				help.addSeparator();

				helpAbout = new JMenuItem( "About..." );
				helpAbout.addActionListener( this );
				help.add( helpAbout );
			}

			public void actionPerformed( ActionEvent ev ) {
				Object src = ev.getSource();

				if( src == fileLoad ) {
					try {
						JFileChooser fc = new JFileChooser();
						fc.setFileFilter( new DrawingFilter() );
						int returnVal = fc.showOpenDialog( Drawing.this );
						
						if( returnVal == JFileChooser.APPROVE_OPTION ) {
							FileInputStream fis = new FileInputStream( fc.getSelectedFile() );
							ObjectInputStream oos = new ObjectInputStream( fis );
							FileFormat load = (FileFormat)oos.readObject();
							oos.close();
							
							Drawing.shapelist.clear();
							Drawing.shapelist.addAll( load.shapes );
							Drawing.drawingGUI.canvas.canvas.repaint();
						}
					} catch ( Exception e ) {
						System.out.println( "Failed to load File, stack trace following: " );
						e.getCause();
						e.printStackTrace();
					}
					
				} else if( src == fileSave ) {
					try {
						JFileChooser fc = new JFileChooser();
						fc.setFileFilter( new DrawingFilter() );
						int returnVal = fc.showSaveDialog(Drawing.this);
					
						if (returnVal == JFileChooser.APPROVE_OPTION) {
							FileOutputStream fos = new FileOutputStream( fc.getSelectedFile() );
							ObjectOutputStream oos = new ObjectOutputStream( fos );
							FileFormat save = new FileFormat( Drawing.shapelist );
							oos.writeObject( save );
							oos.close();
						}
					} catch (Exception e) {
						System.out.println("Failed to open file, stack trace following:");
						e.getCause();
						e.printStackTrace();
					}
				} else if( src == fileExportJPEG ) {
					/* 
					 * NB: This throws a "ClassCastException" on my Linux machine (as does
					 * MyProg07) but runs fine when I tested it under Windows. Odd...
					 */
					Canvas can = Drawing.drawingGUI.canvas.canvas;
					BufferedImage img = (BufferedImage)can.createImage( can.getWidth(), can.getHeight() );
					can.paintComponent( img.getGraphics() );
					try {
						JFileChooser chooser = new JFileChooser();
						chooser.setFileFilter( new JpegFilter() );
						int returnVal = chooser.showSaveDialog( Drawing.this );
						if(returnVal == JFileChooser.APPROVE_OPTION) {
							File file = chooser.getSelectedFile();
							FileOutputStream fos = new FileOutputStream( file );
							
							JPEGEncodeParam jep = JPEGCodec.getDefaultJPEGEncodeParam( img );
							JPEGCodec.createJPEGEncoder( fos, jep ).encode(img);
							
							fos.close();
						}	
					} catch ( Exception e ) { e.getCause(); e.printStackTrace(); }						
				} else if( src == fileExit ) {
					/* UNIX "exited with no errors" */
					System.exit(0);
				}
				/*
				 * Help Menu
				 */
				else if( src == helpHowToUse ) {
					String message = "Drawing: How to use me!\n\nUse the ComboBoxes at the top of the screen to" +
							"describe the shape you want. Enable \"Select Mode\" to alter the objects you have already " +
							"Drawn.\n\nNo Key combinations currently implimented.\n\nKNOWN BUGS:\n- There is a bug when" +
							"you draw a square forwards, and move the cursor backwards, as my square constraint algorithm" +
							"is a little screwey.\n- There is a bug in the JPEG export code that throws a ClassCastException" +
							"on certain platforms (wasn't java supposed to fix this whole problem!?).\n- Choosers don't update" +
							"when you select an object in Select Mode. You can still change them.\n\nHave fun!";
					JOptionPane.showMessageDialog( Drawing.this, message );
				} else if( src == helpAbout ) {
					String message = "Drawing\n\nCompleted as assignment 1 for 159.235, Semester2, 2004\n\n" +
							"All code released under the GPL\n(c)Chris Smith, 2004";
					JOptionPane.showMessageDialog( Drawing.this, message );
				}
			}
		}
	
	class ChooserGUI implements ActionListener {

		private Container contentPane;
		private JPanel containerPanel;
		
		
		/* In the future, could be protected by some getters and setters, but
		 * I think it may be a little over engineered!
		 */
		public JCheckBox selectSwitch;
		public JComboBox shapeChooser;
		public JComboBox lineColorChooser;
		public JComboBox fillColorChooser;
		public JComboBox widthChooser;
		public JButton clearButton;
		
		public String selectedShape;
		public Color selectedLineColor;
		public Color selectedFillColor;
		public int selectedWidth;

		public ChooserGUI( Container cp ) {
			contentPane = cp;
			
			selectedShape = "Line";
			selectedLineColor = Color.RED;
			selectedFillColor = Color.RED;
			selectedWidth = 1;
		}

		public void createChoosers() {
			String shapeLabel = "Shape:";
			String[] shapeList = { "Line", "Rectangle", "Square", "Circle",	"Ellipse" };
			String lineColorLabel = "Line Color:";
			String[] lineColorList = { "Black", "Red", "Green", "Blue", "Custom" };
			String fillColorLabel = "Fill Color:";
			String[] fillColorList = { "None", "White", "Black", "Red", "Green", "Blue", "Custom" };
			/*
			String widthLabel = "Width:";
			String[] widthList = { "1 px", "2 px", "4 px", "8 px", "Custom" };
			*/
			
			/* I had some ugly artifacts using Panel here, JPanel fixes it */
			containerPanel = new JPanel();
			
			selectSwitch = new JCheckBox( "Select Mode", false);
			selectSwitch.addActionListener( this );

			shapeChooser = new JComboBox( shapeList );
			shapeChooser.addActionListener( this );
			shapeChooser.setSelectedIndex( 0 );

			lineColorChooser = new JComboBox( lineColorList );
			lineColorChooser.addActionListener( this );
			lineColorChooser.setSelectedIndex( 0 );

			fillColorChooser = new JComboBox( fillColorList );
			fillColorChooser.addActionListener( this );
			fillColorChooser.setSelectedIndex( 0 );
			
			/* I couldn't get this to work properly so I've disabled it */
			/*
			widthChooser = new JComboBox( widthList );
			widthChooser.addActionListener( this );
			widthChooser.setSelectedIndex( 0 );
			*/
			
			clearButton = new JButton("Clear");
			clearButton.addActionListener( this );

			/*
			 * I had to add the combos to a panel then add the panel
			 * to the content pane. Feels dirty but yet... It feels so
			 * right...
			 */
			containerPanel.add( new JLabel( shapeLabel ) );
			containerPanel.add( shapeChooser );
			containerPanel.add(new JLabel(lineColorLabel) );
			containerPanel.add( lineColorChooser );
			containerPanel.add( new JLabel(fillColorLabel) );
			containerPanel.add( fillColorChooser );
			//containerPanel.add( widthChooser );
			containerPanel.add( new JLabel( "  " ) );
			containerPanel.add( clearButton );
			containerPanel.add( selectSwitch );

			contentPane.add( containerPanel, BorderLayout.NORTH );
		}

		public void actionPerformed( ActionEvent ev ) {
			if( ev.getSource() == shapeChooser ) {
				selectedShape = (String)shapeChooser.getSelectedItem();
			} else if ( ev.getSource() == lineColorChooser ) {
				if( ((String)lineColorChooser.getSelectedItem()).equals("Red")) {
					selectedLineColor = Color.RED;
				} else if( ((String)lineColorChooser.getSelectedItem()).equals("Green")) {
					selectedLineColor = Color.GREEN;
				} else if( ((String)lineColorChooser.getSelectedItem()).equals("Blue")) {
					selectedLineColor = Color.BLUE;
				} else if( ((String)lineColorChooser.getSelectedItem()).equals("Black")) {
					selectedLineColor = Color.BLACK;
				} else if( ((String)lineColorChooser.getSelectedItem()).equals("Custom")) {
					if( Drawing.currentShape != null ) {
						selectedLineColor = JColorChooser.showDialog( Drawing.this, "Choose Line Color", Color.BLACK);
					}
				}
				if( Drawing.selectModeEnabled ) {
					if( Drawing.currentShape != null ) {
						Drawing.currentShape.setLineColor( selectedLineColor );
						Drawing.drawingGUI.canvas.canvas.repaint();
					}
				}
			} else if( ev.getSource() == fillColorChooser ) {
				if( ((String)fillColorChooser.getSelectedItem()).equals("Red")) {
					selectedFillColor = Color.RED;
				} else if( ((String)fillColorChooser.getSelectedItem()).equals("Green")) {
					selectedFillColor = Color.GREEN;
				} else if( ((String)fillColorChooser.getSelectedItem()).equals("Blue")) {
					selectedFillColor = Color.BLUE;
				} else if( ((String)fillColorChooser.getSelectedItem()).equals("Black")) {
					selectedFillColor = Color.BLACK;
				} else if( ((String)fillColorChooser.getSelectedItem()).equals("White")) {
					selectedFillColor = Color.WHITE;
				} else if( ((String)fillColorChooser.getSelectedItem()).equals("Custom")) {
					if( Drawing.currentShape != null ) {
						selectedFillColor = JColorChooser.showDialog( Drawing.this, "Choose Fill Color", Color.BLACK);
					}
				} else if( ((String)fillColorChooser.getSelectedItem()).equals("None")) {
					selectedFillColor = null;
				}
				if( Drawing.selectModeEnabled ) {
					if( Drawing.currentShape != null ) {
						Drawing.currentShape.setFilled( true );
						if( selectedFillColor == null ) {
							Drawing.currentShape.setFilled( false );
						}
						Drawing.currentShape.setFillColor( selectedFillColor );
						Drawing.drawingGUI.canvas.canvas.repaint();
					}
				}
			} else if( ev.getSource() == clearButton ) {
				Drawing.shapelist.clear();
				Drawing.drawingGUI.canvas.canvas.repaint();
			} else if( ev.getSource() == selectSwitch ) {
				if( selectSwitch.isSelected() ) {
					Drawing.selectModeEnabled = true;
					Drawing.drawingGUI.canvas.canvas.setCursor( new Cursor( Cursor.HAND_CURSOR ) );
				} else {
					Drawing.selectModeEnabled = false;
					Drawing.drawingGUI.canvas.canvas.setCursor( new Cursor( Cursor.DEFAULT_CURSOR ) );
				}
				
			}
		}
	}
	
	class CanvasGUI {

		private Container contentPane;
		public Canvas canvas;

		public CanvasGUI( Container cp ) {
			contentPane = cp;
		}

		public void createCanvas() {
			/*
			 * Get some padding so the display area doesnt look so bad This
			 * could be a dirty hack too, but it works! Nicely!
			 * 
			 * I had some nasty artifacts using Panel, JPanel fixes it.
			 */
			contentPane.add( new JPanel(), BorderLayout.EAST );
			contentPane.add( new JPanel(), BorderLayout.WEST );
			contentPane.add( new JPanel(), BorderLayout.SOUTH );

			/*
			 * Thanks to Fred Swartz for this help. Code was released under
			 * MIT licence.
			 * 
			 * http://tinyurl.com/6r3d7
			 */
			Border etchedBdr = BorderFactory.createEtchedBorder();
			Border titledBdr = BorderFactory.createTitledBorder( etchedBdr,	"Drawing Space" );
			Border emptyBdr = BorderFactory.createEmptyBorder( 10, 10, 10, 10 );
			Border compoundBdr = BorderFactory.createCompoundBorder( titledBdr, emptyBdr );

			canvas = new Canvas( new Rectangle( 0, 0, 512, 512 ) );
			canvas.setBorder(compoundBdr);
			contentPane.add( canvas, BorderLayout.CENTER );
		}
	}
	
	class Canvas extends JPanel implements MouseListener,MouseMotionListener {

		private int mouseX1 = -1;
		private int mouseY1 = -1;
		private int mouseX2 = -1;
		private int mouseY2 = -1;
		
		DrawableShape currentShape = null;
		
		public Canvas( Rectangle bounds ) {
			setLayout( null );
			setBounds( bounds );
			setOpaque( false );
			setPreferredSize( new Dimension( bounds.width, bounds.height ) );
			addMouseListener( this );
			addMouseMotionListener( this );
		}

		public void paintComponent( Graphics g ) {
			Graphics2D g2 = (Graphics2D)g;
			g2.setColor( Color.WHITE );
			g2.fillRect( 0, 0, getWidth(), getHeight() );
			
			ListIterator iter = Drawing.shapelist.listIterator();
			while(iter.hasNext()) {
				DrawableShape shape = (DrawableShape)iter.next();
				if( shape.isFilled() ) {
					g2.setColor( shape.fillColor );
					g2.fill( shape.getShape() );
				}
			    g2.setColor( shape.lineColor );
				g2.draw( shape.getShape() );
			}				
			if( Drawing.ghostShape != null ) {
				g2.setColor( Color.GRAY );
				g2.draw( Drawing.ghostShape );
			}
		}

		public void mousePressed( MouseEvent mev ) {
			mouseX1 = mev.getX();
			mouseY1 = mev.getY();
			if ( Drawing.selectModeEnabled ) {
				ListIterator iter = Drawing.shapelist.listIterator();
				while(iter.hasNext()) {
					DrawableShape shape = (DrawableShape)iter.next();
					if( shape.getShape().contains(mouseX1, mouseY1) ) {
						System.out.println( "Setting currentShape = " + shape.getShapeType() + " at " + shape.getX1() + ", " + shape.getY1() );
						Drawing.currentShape = shape;
					}
				}
				if(currentShape != null) {
					Drawing.drawingGUI.choosers.shapeChooser.setSelectedItem(currentShape.shapeType);
					Drawing.drawingGUI.choosers.lineColorChooser.setSelectedItem("Custom");
					Drawing.drawingGUI.choosers.fillColorChooser.setSelectedItem("Custom");
				}
			}
		}

		public void mouseReleased( MouseEvent mev ) {
			mouseX2 = mev.getX();
			mouseY2 = mev.getY();
			
			if( Drawing.selectModeEnabled ) {
				Drawing.ghostShape = null;
				if( Drawing.currentShape.getShape().contains( mouseX1, mouseY1 ) ) {
					DrawableShape shape = Drawing.currentShape;
					int newX = shape.getX1() + ( mev.getX() - shape.getX1() );
					int newY = shape.getY1() + ( mev.getY() - shape.getY1() );
					shape.setCoords( newX, newY, shape.getX2(), shape.getY2() );
				}
			} else {			
				Drawing.ghostShape = null;
				Rectangle bounds = null;
				
				int val1 = -1, val2 = -1, val3 = -1, val4 = -1;
				String shape = Drawing.drawingGUI.choosers.selectedShape;
				Color lineColor = Drawing.drawingGUI.choosers.selectedLineColor;
				Color fillColor = Drawing.drawingGUI.choosers.selectedFillColor;
				boolean filled = true;
				
				if( fillColor == null ) {
					filled = false;
				}
				
				if( shape.equals( "Line" ) ) {
					val1 = mouseX1;
					val2 = mouseY1;
					val3 = mouseX2;
					val4 = mouseY2;
				} else if( shape.equals( "Rectangle" ) || shape.equals( "Ellipse" ) ) {
					bounds = getRectangle( mouseX1, mouseY1, mouseX2, mouseY2 );
					val1 = (int)bounds.getX();
					val2 = (int)bounds.getY();
					val3 = (int)bounds.getWidth();
					val4 = (int)bounds.getHeight();
				} else if( shape.equals( "Square" ) || shape.equals( "Circle" ) ) {
					bounds = getSquare( mouseX1, mouseY1, mouseX2, mouseY2 );
					val1 = (int)bounds.getX();
					val2 = (int)bounds.getY();
					val3 = (int)bounds.getWidth();
					val4 = (int)bounds.getHeight();
				}
				Drawing.shapelist.add( new DrawableShape(shape, val1, val2, val3, val4, lineColor, filled, fillColor ) );
			}
			repaint();
		}

		/* Completes MouseListener implimentation */
		public void mouseEntered( MouseEvent ev ) {
		}

		public void mouseExited( MouseEvent ev ) {
		}

		public void mouseClicked( MouseEvent ev ) {
									
		}
		
		public void mouseDragged( MouseEvent ev ) {
			if( Drawing.selectModeEnabled ) {
				if( Drawing.currentShape.getShape().contains( mouseX1, mouseY1 ) ) {
					String shapeType = Drawing.currentShape.getShapeType();
					DrawableShape shape = Drawing.currentShape;
					int newX = shape.getX1() + ( ev.getX() - shape.getX1() );
					int newY = shape.getY1() + ( ev.getY() - shape.getY1() );
					if( shapeType.equals( "Rectangle" ) ) {
						Drawing.ghostShape = new Rectangle2D.Float( newX, newY, shape.getX2(), shape.getY2());
					} else if( shapeType.equals( "Square" ) ) {
						Drawing.ghostShape = new Rectangle2D.Float( newX, newY, shape.getX2(), shape.getY2() );
					} else if( shapeType.equals( "Ellipse" ) ) {
						Drawing.ghostShape = new Ellipse2D.Float( newX, newY, shape.getX2(), shape.getY2() );
					} else if( shapeType.equals( "Circle" ) ) {
						Drawing.ghostShape = new Ellipse2D.Float( newX, newY, shape.getX2(), shape.getY2() );
					}
				}
			} else {
				String shapeType = (String)Drawing.drawingGUI.choosers.shapeChooser.getSelectedItem();
				Rectangle b;
				if( shapeType.equals( "Line" ) ) {
					Drawing.ghostShape = new Line2D.Float( mouseX1, mouseY1, ev.getX(), ev.getY() );
				} else if( shapeType.equals( "Rectangle" ) ) {
					b = getRectangle( mouseX1, mouseY1, ev.getX(), ev.getY() );
					Drawing.ghostShape = new Rectangle2D.Float( (int)b.getX(), (int)b.getY(), (int)b.getWidth(), (int)b.getHeight() );
				} else if( shapeType.equals( "Square" ) ) {
					b = getSquare( mouseX1, mouseY1, ev.getX(), ev.getY() );
					Drawing.ghostShape = new Rectangle2D.Float( (int)b.getX(), (int)b.getY(), (int)b.getWidth(), (int)b.getHeight() );
				} else if( shapeType.equals( "Ellipse" ) ) {
					b = getRectangle( mouseX1, mouseY1, ev.getX(), ev.getY() );
					Drawing.ghostShape = new Ellipse2D.Float( (int)b.getX(), (int)b.getY(), (int)b.getWidth(), (int)b.getHeight() );
				} else if( shapeType.equals( "Circle" ) ) {
					b = getSquare( mouseX1, mouseY1, ev.getX(), ev.getY() );
					Drawing.ghostShape = new Ellipse2D.Float( (int)b.getX(), (int)b.getY(), (int)b.getWidth(), (int)b.getHeight() );
				}
			}
			repaint();
		}
				
		public void mouseMoved( MouseEvent ev ) {
			
		}
		
		public Rectangle getRectangle( int x1, int y1, int x2, int y2 ) {
			int val1 = -1, val2 = -1, val3 = -1, val4 = -1;
			val1 = x1;
			val2 = y1; 
			val3 = x2 - x1;
			val4 = y2 - y1;
			
			/* Sanatise the co-ords */
			if( x1 > x2 ) {
				val1 = x2;
				val3 = x1 - x2;
			}
			if( y1 > y2 ) {
				val2 = y2;
				val4 = y1 - y2;
			}
			return new Rectangle( val1, val2, val3, val4 );
		}
		
		public Rectangle getSquare( int x1, int y1, int x2, int y2 ) {
			int val1 = -1, val2 = -1, val3 = -1, val4 = -1;
			val1 = x1;
			val2 = y1; 
			val3 = x2 - x1;
			val4 = y2 - y1;
			
			/* Sanatise the co-ords */
			if( x1 > x2 ) {
				val1 = x2;
				val3 = x1 - x2;
			}
			if( y1 > y2 ) {
				val2 = y2;
				val4 = y1 - y2;
			}
			
			if( val3 > val4 ) {
				return new Rectangle( val1, val2, val4, val4 );				
			} else {
				return new Rectangle( val1, val2, val3, val3 );
			}
//			return new Rectangle( val1, val2, val3, val4 );
		}
		
	}
	
	class DrawableShape implements Serializable {
		
		private String shapeType;
		private Color lineColor;
		private int lineWidth;
		private Color fillColor;
		private int x1, y1, x2, y2;
		private boolean filled;
		
		
		public DrawableShape( String type, int X1, int Y1, int X2, int Y2, Color line, boolean isFilled, Color fill ) {
			shapeType = type;
			x1 = X1; y1 = Y1;
			x2 = X2; y2 = Y2;
			lineColor = line;
			filled = isFilled;
			fillColor = fill;
		}
		
		public Shape getShape() {
			if( shapeType.equals( "Line" ) ) {
				return new Line2D.Float( x1, y1, x2, y2 );
			} else if( shapeType.equals( "Rectangle" ) || shapeType.equals( "Square" ) ) {
				return new Rectangle2D.Float( x1, y1, x2, y2 );
			} else if( shapeType.equals( "Ellipse" ) || shapeType.equals( "Circle" ) ) {
				return new Ellipse2D.Float( x1, y1, x2, y2 );
			} else {
				return null;
			}
		}
		
		public boolean isFilled() {
			return filled;
		}

		public void setFilled( boolean filled ) {
			this.filled = filled;
		}
		
		public Color getFillColor() {
			return fillColor;
		}

		public void setFillColor( Color fillColor ) {
			this.fillColor = fillColor;
		}

		public Color getLineColor() {
			return lineColor;
		}

		public void setLineColor( Color lineColor ) {
			this.lineColor = lineColor;
		}

		public String getShapeType() {
			return shapeType;
		}

		public void setShapeType( String shapeType ) {
			this.shapeType = shapeType;
		}

		public void setCoords( int X1, int Y1, int X2, int Y2 ) {
			x1 = X1;
			y1 = Y1;
			x2 = X2;
			y2 = Y2;
		}
		
		public int getX1() {
			return x1;
		}

		public int getX2() {
			return x2;
		}

		public int getY1() {
			return y1;
		}

		public int getY2() {
			return y2;
		}
	}
	
	/* Made a class for flexability, can add other meta-data to the
	 * file format if I need to!
	 */
	class FileFormat implements Serializable {
		Vector shapes;
	
		public FileFormat( Vector shapeList ) {
			shapes = shapeList;
		}
	}
	
	/* File filters for my open and save dialogs, very standard */
	class DrawingFilter extends javax.swing.filechooser.FileFilter implements Serializable {
		public boolean accept(File f) {
		    if (f.isDirectory()) {
		        return true;
		    }

		    String extension = getExtension(f);
		    if (extension != null) {
		        if (extension.equals( "draw" ) || extension.equals( "drw" ) ) {
		        	return true;
		        } else {
		            return false;
		        }
		    }

		    return false;
		}
		
		public String getDescription() {
			return "Drawing Files (*.draw, *.drw)";
		}
		
		private String getExtension( File f ) {
			String ext = null;
	        String s = f.getName();
	        int i = s.lastIndexOf('.');

	        if (i > 0 &&  i < s.length() - 1) {
	            ext = s.substring(i+1).toLowerCase();
	        }
	        return ext;
		}
	}
	
	class JpegFilter extends javax.swing.filechooser.FileFilter implements Serializable {
		public boolean accept(File f) {
		    if (f.isDirectory()) {
		        return true;
		    }

		    String extension = getExtension(f);
		    if (extension != null) {
		        if (extension.equals( "jpeg" ) || extension.equals( "jpg" ) ) {
		        	return true;
		        } else {
		            return false;
		        }
		    }

		    return false;
		}
		
		public String getDescription() {
			return "JPEG Image Files (*.jpeg, *.jpg)";
		}
		
		private String getExtension( File f ) {
			String ext = null;
	        String s = f.getName();
	        int i = s.lastIndexOf('.');

	        if (i > 0 &&  i < s.length() - 1) {
	            ext = s.substring(i+1).toLowerCase();
	        }
	        return ext;
		}
	}
}

