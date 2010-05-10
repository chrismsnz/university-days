// $Id: IDXMLNode.java,v 1.4 2004/05/31 05:28:07 cheetah100 Exp $
package net.sourceforge.idxml;

import java.io.*;
import java.util.*;

public interface IDXMLNode
{
	public String getXML();
	public void setXML( String xml );
	public String getTag(String node);
	public String getAttribute(String node, String attribute);
	public String getValue(String node);
	public int getCount(String node);
	public IDXMLNode getElement( String elementPath );
}
