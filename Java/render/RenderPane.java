/* This is the class where all the 3d rendering gets done. */ 

import javax.media.j3d.*;
import com.sun.j3d.utils.universe.*;
import javax.swing.event.*;
import java.awt.event.*;

public class RenderPane extends Canvas3D {
	
	public RenderPane() {
		super( SimpleUniverse.getPreferredConfiguration() );
	}
}
