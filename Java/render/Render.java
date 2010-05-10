/* Smith, Chris, 02386569, Assignment2, 159.235 
 * Coulam, Jonathan, 00273899, Assignment2, 159.235
 * Brown, Geoffrey, 01353489	  
*/


import java.io.*;

import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.universe.*;
import javax.media.j3d.*;
import javax.vecmath.Point3d;

//import com.sun.j3d.utils.*;
//import javax.media.j3d.Behavior;
//import com.sun.j3d.utils.behaviors.mouse.MouseBehavior;


public class Render {
	
	/* Settings */
	private final static String[] modelFileNames = { "dinosaur.dat", 
													 "dinosaur.dat", 
													 "dinosaur.dat",
													 "dinosaur.dat",
													 "dinosaur.dat",
													 "dinosaur.dat" };	
	
	private Model[] modelList;
	private MainWindow mainWindow;
	private Scene scene;
	private SimpleUniverse simpleUniverse;
	private ViewingPlatform viewingPlatform;
	
	
	public static void main( String args[] ) {
		/* Print that info, yeah! */
		System.out.println("------------------------------------------");
		System.out.println("     159.235 Assignment 2, Semester 2     ");
		System.out.println(" Submitted by: Smith, Chris 02386569      ");
		System.out.println("               Coulam, Jonathan 00273899  ");
		System.out.println("               Brown, Geoffrey 01353489   ");
		System.out.println("------------------------------------------");
		
		/* Start the program */
		new Render();
	}
	
	public Render() {
		/* Create the window */
		mainWindow = new MainWindow();
		
		/* Create a list of the models */
		modelList = new Model[modelFileNames.length];
		
		/* Load all the files as specified above */
		for( int i = 0; i < modelFileNames.length; i++ ) {
			modelList[i] = new Model( new File( modelFileNames[i] ) );
		}
				
		/* Link the Canvas3d and the simple universe together */
		simpleUniverse = new SimpleUniverse( mainWindow.getRenderPane() );
		
		/* Create our "Scene" object which generates all the scene elements
		 * Give it a list of models so that it can generate shape3d's for them.
		 */
		scene = new Scene( modelList );
		
		/* Add the scene to the universe */
		simpleUniverse.addBranchGraph( scene.getScene() );
		
		/* Set up the viewing platform to use the nominal view */
		viewingPlatform = simpleUniverse.getViewingPlatform();
		viewingPlatform.setNominalViewingTransform();
					
	}
}
