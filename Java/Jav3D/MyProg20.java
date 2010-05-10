import java.awt.*;      
import java.awt.geom.*; 
import java.awt.event.*;
import javax.swing.*;   
import javax.swing.event.*;
import java.util.*;
import java.io.*;

// An adaptation of Prog19  that loads a wireframe model from file "hand.dat"
public class MyProg20 extends JFrame{

    private static final int DISPLAY_WIDTH  = 768;
    private static final int DISPLAY_HEIGHT = 768;
    private static final int CONTROL_WIDTH  = 256;

    private Container con;
    private DisplayArea da = null;
    ControlPanel cp = null;

    public static final boolean drawCube = false;

    public static String filename = "hand.dat";
    public static void main( String args[] ){
	if( args.length != 0 ) filename = args[0];
	new MyProg20();
    }

    public MyProg20(){
	super("3D Rendering from File (Wireframe Only)");
	setDefaultCloseOperation(EXIT_ON_CLOSE);
	con = getContentPane();
	con.setLayout( new BoxLayout( con, BoxLayout.X_AXIS ) ); // horizontal

	loadTriangles(filename);

	da = new DisplayArea( new Rectangle( 0, 0, DISPLAY_WIDTH, DISPLAY_HEIGHT) );
	con.add( da );

	cp = new ControlPanel( new Rectangle( DISPLAY_WIDTH, 0,
					      CONTROL_WIDTH, DISPLAY_HEIGHT ) );
	con.add( cp );

	setVisible(true);
      	resizeToInternalSize( DISPLAY_WIDTH + CONTROL_WIDTH, DISPLAY_HEIGHT );
    }

    public void resizeToInternalSize( int internalWidth, int internalHeight ){
	Insets insets = getInsets();
	final int newWidth  = internalWidth  + insets.left + insets.right;
	final int newHeight = internalHeight + insets.top  + insets.bottom;
	Runnable resize = new Runnable(){ // an anonymous inner class
		public void run(){
		    setSize( newWidth, newHeight);
		}
	};
	if(!SwingUtilities.isEventDispatchThread() ){
	    try{
		SwingUtilities.invokeAndWait( resize );
	    }catch( Exception e ) { }
	}
	else{
	    resize.run();
	}
	validate();
    }

    public class Triangle{
	int i1, i2, i3;
	Triangle( int j1, int j2, int j3 ){ i1=j1; i2=j2; i3=j3; }
    }

    public Point3D points[];
    public Triangle triangles[]; 

    public void drawTriangles( Graphics2D g2 ){
	g2.setColor( Color.red );
	for( int i = 0;i< triangles.length;i++){
	    try{
	    draw3DLine( g2, points[ triangles[i].i1 ], points[ triangles[i].i2 ] );
	    draw3DLine( g2, points[ triangles[i].i2 ], points[ triangles[i].i3 ] );
	    draw3DLine( g2, points[ triangles[i].i1 ], points[ triangles[i].i1 ] );
	    }catch( ArrayIndexOutOfBoundsException e ){
		System.out.println( "out of bounds i = " + i );
	    }
	}
    }

    // read  list of N vertices  followed by list of triangles using 1...N indexing 
    public void loadTriangles( String filename ){
	try{
	    FileInputStream fis = new FileInputStream(filename); 
	    BufferedReader br = new BufferedReader( new InputStreamReader( fis ) );
	    String line = br.readLine(); while(line.startsWith("#"))line = br.readLine();
	    int nVertices = Integer.parseInt( line );
	    points = new Point3D[nVertices];
	    for( int i=0;i<nVertices;i++){
		line = br.readLine(); while(line.startsWith("#"))line = br.readLine();
		StringTokenizer strtok = new StringTokenizer( line, " \t" );
		double x = Double.parseDouble( strtok.nextToken() );
		double y = Double.parseDouble( strtok.nextToken() );
		double z = Double.parseDouble( strtok.nextToken() );
		points[i] = new Point3D( x, y, z );
	    }
	    line = br.readLine();  while(line.startsWith("#"))line = br.readLine();
	    int nTriangles = Integer.parseInt( line );
	    triangles = new Triangle[nTriangles];
	    for( int i=0;i<nTriangles;i++){
		line = br.readLine(); while(line.startsWith("#"))line = br.readLine();
		StringTokenizer strtok = new StringTokenizer( line, " \t" );
		int i1 = Integer.parseInt( strtok.nextToken() );
		int i2 = Integer.parseInt( strtok.nextToken() );
		int i3 = Integer.parseInt( strtok.nextToken() );
		triangles[i] = new Triangle(i1-1, i2-1, i3-1);
	    }
	    br.close();
	    fis.close();
	}catch(Exception e ){e.printStackTrace(); }	

	// report on  file read:
	System.out.println( "Points Read: " + points.length );
	System.out.println( "Triangles Read: " + triangles.length );
    }


    public class DisplayArea extends JPanel{
	public DisplayArea( Rectangle bounds ){
	    setLayout(null);
	    setBounds( bounds);
	    setOpaque(false);
	    setPreferredSize( new Dimension (bounds.width, bounds.height )  );
	}

	public void paintComponent( Graphics g ){
	    Graphics2D g2 = (Graphics2D)g;
	    g2.setColor( Color.lightGray );
	    g2.fillRect( 0, 0, getWidth(), getHeight() );
	    g2.setColor( Color.black );
	    g2.translate( getWidth()/2, getHeight()/2 );   // make 0,0 the screen centre
	    g2.scale( 1.0, -1.0 );  // make the  x,y the normal right-handed Cartesian way

	    if( drawCube ){
		// top of cube
		g2.setColor( Color.blue);
		draw3DLine( g2, new Point3D( -1, -1, 1 ), new Point3D(  1, -1, 1 ) );
		draw3DLine( g2, new Point3D( -1, -1, 1 ), new Point3D( -1,  1, 1 ) );
		draw3DLine( g2, new Point3D(  1,  1, 1 ), new Point3D( -1,  1, 1 ) );
		draw3DLine( g2, new Point3D(  1,  1, 1 ), new Point3D(  1, -1, 1 ) );
		
		// vertical struts
		g2.setColor( Color.white);
		draw3DLine( g2, new Point3D( -1, -1, -1 ), new Point3D( -1, -1, 1 ) );
		draw3DLine( g2, new Point3D( -1,  1, -1 ), new Point3D( -1,  1, 1 ) );
		draw3DLine( g2, new Point3D(  1, -1, -1 ), new Point3D(  1, -1, 1 ) );
		draw3DLine( g2, new Point3D(  1,  1, -1 ), new Point3D(  1,  1, 1 ) );
		
		// base of cube
		g2.setColor( Color.black);
		draw3DLine( g2, new Point3D( -1, -1, -1 ), new Point3D(  1, -1, -1 ) );
		draw3DLine( g2, new Point3D( -1, -1, -1 ), new Point3D( -1,  1, -1 ) );
		draw3DLine( g2, new Point3D(  1,  1, -1 ), new Point3D( -1,  1, -1 ) );
		draw3DLine( g2, new Point3D(  1,  1, -1 ), new Point3D(  1, -1, -1 ) );
		
		// origin to each principle axis
		g2.setColor( Color.green);
		draw3DLine( g2, new Point3D(  0,  0,  0 ), new Point3D(  1, 0, 0 ) );
		
		g2.setColor( Color.red);
		draw3DLine( g2, new Point3D(  0,  0,  0 ), new Point3D(  0, 1, 0 ) );
		
		g2.setColor( Color.magenta);
		draw3DLine( g2, new Point3D(  0,  0,  0 ), new Point3D(  0, 0, 1 ) );
	    }

	    drawTriangles(g2); 
	}

    }

    public void draw3DLine( Graphics2D g2, Point3D start, Point3D finish ){
	Point2D s =  start.projectPoint();
	Point2D f = finish.projectPoint();
	g2.drawLine( s.x, s.y, f.x, f.y );
    }

    public class Point2D{
	public int x;
	public int y;
	Point2D(){ x=0; y=0; }
	Point2D( int x, int y ){
	    this.x= x;
	    this.y= y;
	}
    }

    private static final double DEGREES_TO_RADIANS = Math.PI / 180.0;

    public int xScreenOrigin = 0;
    public int yScreenOrigin = 0;

    public Point3D screenPosition = new Point3D( 0, 0, 20 );
    public Point3D viewAngle = new Point3D( 0, 0, 180 );

    private double cosTheta = Math.cos( DEGREES_TO_RADIANS * viewAngle.x );
    private double sinTheta = Math.sin( DEGREES_TO_RADIANS * viewAngle.x );
    private double cosPhi   = Math.cos( DEGREES_TO_RADIANS * viewAngle.y );
    private double sinPhi   = Math.sin( DEGREES_TO_RADIANS * viewAngle.y );
    
    private double modelScale = 10.0;

    public void updateAngles(){
	cosTheta = Math.cos( DEGREES_TO_RADIANS * viewAngle.x );
	sinTheta = Math.sin( DEGREES_TO_RADIANS * viewAngle.x );
	cosPhi   = Math.cos( DEGREES_TO_RADIANS * viewAngle.y );
	sinPhi   = Math.sin( DEGREES_TO_RADIANS * viewAngle.y );
    }

    public void setDefaultView(){
	xScreenOrigin = 0;
	yScreenOrigin = 0;
	screenPosition = new Point3D( 0, 0, 20 );
	viewAngle = new Point3D( 0, 0, 180 );
	modelScale = 10.0;
    }

    public class Point3D{
	public double x;
	public double y;
	public double z;
	Point3D(){ x=0.0; y=0.0; z=0.0; }
	Point3D( double x, double y, double z ){ this.x= x; this.y= y; this.z= z; }
	Point3D( int x, int y, int z ){ this.x= x; this.y= y; this.z= z; }

	public Point2D projectPoint(){
	    Point2D retval = new Point2D();
	    double tmpx = screenPosition.x + x * cosTheta - y * sinTheta;
	    double tmpy = screenPosition.y + x * sinTheta + y * cosTheta * sinPhi
		                           + z * cosPhi ;
	    double temp = viewAngle.z / (screenPosition.z
					 + x * sinTheta * cosPhi
					 + y * cosTheta * cosPhi - z * sinPhi  );

	    retval.x = xScreenOrigin + (int)(modelScale * temp * tmpx );
	    retval.y = yScreenOrigin + (int)(modelScale * temp * tmpy );
	    return retval;
	}
    }

    private JSlider xPosition    = null;
    private JSlider yPosition    = null;
    private JSlider zPosition    = null;
    private JSlider thetaView    = null;
    private JSlider phiView      = null;
    private JSlider zAngleView   = null;

    public class ControlPanel extends JPanel implements ActionListener, ChangeListener{
	public ControlPanel( Rectangle bounds ){
	    setLayout(new FlowLayout() );
	    setBounds(bounds);
       	    setOpaque(false);
	    setPreferredSize( new Dimension (bounds.width, bounds.height )  );
	    setBackground( Color.gray );

	    zPosition = new JSlider( 0, 50, 20 );  // min, max, value
	    zPosition.addChangeListener( this );
	    zPosition.setMajorTickSpacing( 10 );
	    zPosition.setPaintTicks( true );
	    zPosition.setPaintLabels( true );
	    add( zPosition);
	    add( new JLabel( "zPos " )  );

	    xPosition = new JSlider( -5, 5, 0 );  // min, max, value
	    xPosition.addChangeListener( this );
	    xPosition.setMajorTickSpacing( 1 );
	    xPosition.setPaintTicks( true );
	    xPosition.setPaintLabels( true );
	    add( xPosition);
	    add( new JLabel( "xPos " )  );

	    yPosition = new JSlider( -5, 5, 0 );  // min, max, value
	    yPosition.addChangeListener( this );
	    yPosition.setMajorTickSpacing( 1 );
	    yPosition.setPaintTicks( true );
	    yPosition.setPaintLabels( true );
	    add( yPosition);
	    add( new JLabel( "yPos " )  );

	    thetaView = new JSlider( 0, 360, 0 );
	    thetaView.addChangeListener( this );
	    thetaView.setMajorTickSpacing( 90 );
	    thetaView.setPaintTicks( true );
	    thetaView.setPaintLabels( true );
	    add( thetaView);
	    add( new JLabel( "Theta" )  );

	    phiView   = new JSlider( 0, 360, 0 );
	    phiView.addChangeListener( this );
	    phiView.setMajorTickSpacing( 90 );
	    phiView.setPaintTicks( true );
	    phiView.setPaintLabels( true );
	    add( phiView);
	    add( new JLabel( "Phi  " )  );

	    zAngleView   = new JSlider( -360, 360, 180);
	    zAngleView.addChangeListener( this );
	    zAngleView.setMajorTickSpacing( 180 );
	    zAngleView.setPaintTicks( true );
	    zAngleView.setPaintLabels( true );
	    add( zAngleView);
	    add( new JLabel( "zAngle" )  );
	}

	public void paintComponent( Graphics g ){
	    Graphics2D g2 = (Graphics2D)g;
	    g2.setColor( Color.gray );
	    g2.fillRect( 0, 0, getWidth(), getHeight() );
	}
	
	public void actionPerformed( ActionEvent ev ){
	    //if( ev.getSource() == something ){
	    //}
	}

	public void stateChanged( ChangeEvent cev ){
	    if( cev.getSource() == xPosition ){
		screenPosition = new Point3D( xPosition.getValue(),
					      screenPosition.y, screenPosition.z );
		da.repaint();
	    }
	    if( cev.getSource() == yPosition ){
		screenPosition = new Point3D( screenPosition.x, yPosition.getValue(),
					      screenPosition.z );
		da.repaint();
	    }
	    else if( cev.getSource() == zPosition ){
		screenPosition = new Point3D( screenPosition.x, screenPosition.y,
					      zPosition.getValue() );
		da.repaint();
	    }
	    else if( cev.getSource() == thetaView ){
		viewAngle = new Point3D( thetaView.getValue(),viewAngle.y,viewAngle.z );
		updateAngles();
		da.repaint();
	    }
	    else if( cev.getSource() == phiView ){
		viewAngle = new Point3D( viewAngle.x, phiView.getValue(), viewAngle.z );
		updateAngles();
		da.repaint();
	    }
	    else if( cev.getSource() == zAngleView ){
		viewAngle = new Point3D( viewAngle.x, viewAngle.y, zAngleView.getValue() );
		updateAngles();
		da.repaint();
	    }
	}
    }


}

/*------------------------------------------------------------------------------
Development Notes:

This uses a really simple projection formula - it is however the beginnings of
simple rendering engine.

It may be useful as a development base for experimenting with projections.

@KAH2004------------------------------------------------------------------------*/
