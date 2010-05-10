/* This class controls the entire scene with functions for manipulating it */
import java.awt.Color;

import javax.media.j3d.*;
import javax.vecmath.*;

import com.sun.j3d.utils.geometry.GeometryInfo;
import com.sun.j3d.utils.geometry.NormalGenerator;
import com.sun.j3d.utils.geometry.Stripifier;

public class Scene {
	
	BranchGroup scene;
	BranchGroup completeScene;
	TransformGroup rotatingScene;
	TransformGroup wrapper;
	
	int spacer = 0;

	public Scene( Model[] modelList ) {
		
		scene = new BranchGroup();
		
		/* Create a light source, this can be changed easily */
		AmbientLight aLight = new AmbientLight( new Color3f( Color.GREEN ) );
		aLight.setInfluencingBounds( new BoundingSphere( new Point3d( 0, 0, 0), 5 ) );
		scene.addChild( aLight );
		
		DirectionalLight dLight = new DirectionalLight();
		dLight.setInfluencingBounds( new BoundingSphere( new Point3d( 0, 0, 0), 5 ) );
		dLight.setColor( new Color3f( Color.GREEN ) );
		scene.addChild( dLight );
		
		for( int i = 0; i < modelList.length; i++ ) {
			System.out.println( "Loading model: " + i );
			
			/* Create an appearance object which will hold all the attriutes for this model */
			Appearance app = new Appearance();
			
			/* Create a coloring attributes object which will become part of the appearance */
			ColoringAttributes colors = new ColoringAttributes();
			colors.setColor( new Color3f( Color.GREEN ) );
			colors.setShadeModel( ColoringAttributes.NICEST );
			
			/* Set up the appearance object */
			app.setColoringAttributes( colors );
			app.setMaterial( new Material() );
			
			/* Bundle our models into geometry info objects so we can generate the
			 * normal vectors for every face
			 */
			GeometryInfo gi = new GeometryInfo( modelList[i].getTriangleArray() );
			NormalGenerator ng = new NormalGenerator();
			ng.generateNormals( gi );
			
			/* Stripifing creates all the triangles as a strip, saving on redraw */
			Stripifier strip = new Stripifier();
			strip.stripify( gi );
			
			/* Create the shape3d */
			Shape3D model = new Shape3D( gi.getGeometryArray(), app );
			
			TransformGroup rotate = new TransformGroup();
			Transform3D trans = new Transform3D();
			trans.rotX( Math.PI /-2 );
			rotate.setTransform( trans );
			rotate.addChild( model );
			
			TransformGroup place = new TransformGroup();
			Transform3D translation = new Transform3D();
			
			/* oooheee, if?then:else */
			spacer = ( (i % 2) == 1?(i * -1):i);
			
			translation.setTranslation( new Vector3d( spacer, 0, 0 ) );
			place.setTransform( translation );
			place.addChild( rotate );
			
			scene.addChild( place );
		}
		
		/* Compile, optimise and complete the scene */
		scene.compile();
		wrapper = new TransformGroup();
		wrapper.setCapability( TransformGroup.ALLOW_TRANSFORM_WRITE );
		wrapper.addChild( scene );
		
		rotatingScene = new TransformGroup();
		
		/* This transform applies to the ENTIRE SCENE */
		Transform3D trans = new Transform3D();
		trans.setScale( 0.5 );
		trans.setTranslation( new Vector3d( 0, -0.5, -5) );
		rotatingScene.setTransform( trans );
		rotatingScene.setCapability( TransformGroup.ALLOW_TRANSFORM_WRITE );
		rotatingScene.addChild( createRotation( wrapper ) );
		rotatingScene.addChild( wrapper );
		
		completeScene = new BranchGroup();
		completeScene.addChild( rotatingScene );
	}	
	
	public BranchGroup getScene() {
		return completeScene;
	}
	
	public RotationInterpolator createRotation( TransformGroup obj ){
		
		//Create a new Behavior object that will perform the desired
		// operation on the specified transform object and add it into
		// the scene graph.
		Transform3D yAxis = new Transform3D();
		Alpha rotAlpha = new Alpha(-1, Alpha.INCREASING_ENABLE,	0, 0, 4000, 0, 0, 0, 0, 0);
		RotationInterpolator rot = new RotationInterpolator( rotAlpha, obj, yAxis, 0.0f, 2*(float)Math.PI);
		BoundingSphere bounds = new BoundingSphere( new Point3d( 0.0,0.0,0.0 ), 100.0 );
		rot.setSchedulingBounds(bounds);
		return rot;
		
	}
	
	
	
	
}
