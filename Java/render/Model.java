/* This Class represents the data model. It takes a filename as a param to
 * it's constructor. It will then read in all the data from that file and 
 * provide a set of triangles and points (verticies).
 * 
 * Thanks to Ken Hawaick's "MyProg20" to which this code is based.
 * 
 * The data file needs to be in plain text, with the format:
 * 
 * <number of points>
 * <x y z of point seperated by whitespace>
 * ...
 * <number of triangles>
 * <point1 point2 point3 of triangles>
 * ...
 * 
 * Where pointx corrosponds to the point number from the first section.
 */

import java.io.*;
import javax.vecmath.*;
import javax.media.j3d.TriangleArray;
import java.util.*;

public class Model {
	
	Point3d[] points;
	TriangleArray triangles;
	int nTriangles;
	
	public Model( File file ) {
		Point3d[] vertices;
		
		/* Open the file given as a parameter and read in all the vertices and the
		 * triangle definitions.
		 */
		
		try {
			String line;
			
			/* I can't believe I need all this to read a file ;) */
			FileInputStream fis = new FileInputStream( file );
			InputStreamReader is = new InputStreamReader( fis );
			BufferedReader input = new BufferedReader( is );
			
			/* Read in the line, zap any comments */
			do {
				line = input.readLine();
			} while( line.startsWith( "#" ) );
			
			/* First number is how many Vertices we have */
			int nVertices = Integer.parseInt( line );
			vertices = new Point3d[nVertices];
			
			for( int i = 0; i < nVertices; i++ ) {
				/* Zap any comments */
				do {
					line = input.readLine();
				} while( line.startsWith( "#" ) );
				
				/* Split up the line into tokens. Split on spaces and tabs */
				StringTokenizer strtoks = new StringTokenizer( line, " \t" );
				
				/* Store each token in a double variable */
				double x = Double.parseDouble( strtoks.nextToken() );
				double y = Double.parseDouble( strtoks.nextToken() );
				double z = Double.parseDouble( strtoks.nextToken() );
				
				/* Create a Point3d out of all the doubles */
				vertices[i] = new Point3d( x, y, z );
			}
			
			/* Now to read in the triangles. */
			do {
				line = input.readLine();
			} while( line.startsWith( "#" ) );
			
			/* How many triangles? */
			int nTriangles = Integer.parseInt( line );
			points = new Point3d[nTriangles * 3];
			
			/* Read in all the triangles */
			for( int i = 0; i < nTriangles * 3; i += 3 ) {
				/* Zap any comments */
				do {
					line = input.readLine();
				} while( line.startsWith( "#" ) );
				
				/* Split up the line into tokens. Split on spaces and tabs */
				StringTokenizer strtoks = new StringTokenizer( line, " \t" );
				
				/* Store the point numbers in ints */
				int p1 = Integer.parseInt( strtoks.nextToken() );
				int p2 = Integer.parseInt( strtoks.nextToken() );
				int p3 = Integer.parseInt( strtoks.nextToken() );
				
				/* It's a pain, but the references in the file are 1 based yet arrays 
				 * are 0-based (start from zero) so a small adjustment needs to be made
				 */
				p1 -= 1; p2 -= 1; p3 -= 1;
				
				points[i] = new Point3d( vertices[p1] );
				points[i + 1] = new Point3d( vertices[p2] );
				points[i + 2] = new Point3d( vertices[p3] );
			}			
		} catch( FileNotFoundException e ) {
			System.out.println( "File not found!" );
		} catch( IOException e ) {
			System.out.println( "Cannot Read File!" );
		}
		
		triangles = new TriangleArray( points.length, TriangleArray.COORDINATES );
		triangles.setCoordinates(0, points);
	}

	public TriangleArray getTriangleArray() {
		return triangles;
	}

}
