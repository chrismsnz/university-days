package net.sourceforge.idxml;

import java.io.*;
import java.util.*;
import java.util.regex.*;

/**
 *  IDXMLParser is a lightweight XML Parser for Java.  It is not DOM or SAX based.  The original
 *  parser was found in Peter Harrison's IDTRANS (idtrans.sourceforge.net) project and has since
 *  been moved out to its own project.  Currently, only the java version available. *
 *
 *  IDTrans is licensed under a BSD style licence, availabel at:
 *  http://idtrans.sourceforge.net/license.htm
 *
 *  $Id: IDXMLParser.java,v 1.8 2004/05/31 05:28:07 cheetah100 Exp $
 */
public class IDXMLParser implements IDXMLNode
{
	private HashMap elementMap = new HashMap();
	private String fXML = "<xml></xml>";
	private String fNode = "";
	public int fInsert = 0;
	public int fOffset = 0;
	public int fOffsetEnd = 0;
	public int fTagOffset = 0;
	public int fTagOffsetEnd = 0;

	public IDXMLParser( )
	{
		super();
	}

	public IDXMLParser( String xml )
	{
		super();
		setXML( xml );
	}

	private int getStartOfNthElement( Matcher startElementMatcher, Matcher endElementMatcher, int elementNumber )
	{
		int tmpOffset = fTagOffset;

		if( fXML.length() > 0 )
		{
			for (int g = 1; g < elementNumber; g++)
			{
				if( startElementMatcher.find( tmpOffset ) )
				{
					tmpOffset = startElementMatcher.end();

					// Determine if the tag is its own end tag or has a seperate end tag.
					if ( fXML.charAt(tmpOffset - 2) != '/' )
					{
						// If it is not its own end tag look for another end tag.
						if( endElementMatcher.find( tmpOffset ) )
						{
							tmpOffset = endElementMatcher.end();
						}
						else
						{
							resetOffsets();
							return -1; // Broken XML
						}
					}
				}
				else
				{
					resetOffsets();
					return -1;
				}
			}

			if( startElementMatcher.find( tmpOffset ) )
			{
				tmpOffset = startElementMatcher.start();
			}
			else
			{
				resetOffsets();
				return -1; // Broken Query
			}
		}

		return tmpOffset;
	}

	/**
	 *  Get Offset within XML String
	 *
	 *@param  parent  Description of the Parameter
	 *
	 * fOffset - Start of body of current element.
	 * fOffsetEnd - End of body of current element (not inclusive).
	 * fTagOffset - Start of element including Tag.
	 * fTagOffsetEnd - End of element including End Tag (inclusive).
	 */
	public void getOffset(String parent)
	{
		String[] parents = getParents(parent);
		resetOffsets();

		String elementType;
		int start;
		int startOfTag;
		int endOfTag;
		Matcher startElementMatcher;
		Matcher endElementMatcher;

		// Loop over all element names restricting the search space as we loop.
		for (int f = 0; f < parents.length; f++)
		{
			elementType = parents[f];
			int tmpOffset = fTagOffset;
			int elementNumber;

			// Skipping the elements prior to the one we want.
			start = elementType.lastIndexOf('@');
			if ( start > -1 )
			{
				elementType = elementType.substring( 0, start );
			}

			Pattern p = Pattern.compile( "<" + elementType + "(?!\\w).*?>" );
			startElementMatcher = p.matcher( fXML );
			p = Pattern.compile( "</" + elementType + "\\s*>" );
			endElementMatcher = p.matcher( fXML );

			if ( start > -1 )
			{
				String elementNumberString = parents[f].substring( start + 1 );
				elementNumber = Integer.parseInt( elementNumberString );
				tmpOffset = getStartOfNthElement( startElementMatcher, endElementMatcher, elementNumber );
				if( tmpOffset == -1 )
				{
					resetOffsets();
					return;
				}
			}

			if( startElementMatcher.find( tmpOffset ) )
			{
				fTagOffset = startElementMatcher.start();
				fOffset = startElementMatcher.end();
				//System.out.println( "start found : " + startElementMatcher.group() );
			}
			else
			{
				resetOffsets();
				return;
			}

			// Determine if the tag is its own end tag or has a seperate end tag.
			if ( fXML.charAt(fOffset - 2) == '/' )
			{
				fOffsetEnd = fOffset;
				fTagOffsetEnd = fOffset;
			}
			else
			{
				// If it is not its own end tag look for another end tag.
				if( endElementMatcher.find( fOffset ) )
				{
					fOffsetEnd = endElementMatcher.start();
					fTagOffsetEnd = endElementMatcher.end();
					//System.out.println( "end found : " + endElementMatcher.group() );
				}
				else
				{
					resetOffsets();
					return;
				}
			}
		}
	}

	private void resetOffsets()
	{
		fOffset = 0;
		fOffsetEnd = 0;
		fTagOffset = 0;
		fTagOffsetEnd = 0;
	}

	/**
	 *  Sets the Node Property, and the Offset required to Add Data
	 *
	 *@param  node  The new node value
	 */
	private void setNode(String node)
	{
		fNode = node;
		getOffset(node);
		fInsert = fOffset;
	}


	/**
	 *  Get Parents splits a node into the individual parents of a node in the
	 *  form of an Array.
	 *
	 *@param  node  Description of the Parameter
	 *@return       The parents value
	 */
	private String[] getParents(String node)
	{
		String[] parents = node.split( ":" );

		return parents;
	}


	/**
	 *  Add a Node (not implemented)
	 *
	 *@param  parent  The feature to be added to the Node attribute
	 *@param  node    The feature to be added to the Node attribute
	 */
	public void addNode(String parent, String node)
	{
	}


	/**
	 *  Delete a Node (not implemented)
	 *
	 *@param  node  Description of the Parameter
	 */
	public void deleteNode(String node)
	{
	}


	/**
	 *  Edit a Value (not implemented)
	 *
	 *@param  node   Description of the Parameter
	 *@param  value  Description of the Parameter
	 */
	public void editValue(String node, String value)
	{
	}

	/**
	 *  Add a Element (not implemented)
	 *
	 *@param  node   The feature to be added to the Element attribute
	 *@param  value  The feature to be added to the Element attribute
	 */
	public void addElement(String node, String value)
	{
	}


	/**
	 *  Get a Value of the Element Specified. This does not include the element itself however.
	 *
	 *@param  node  Description of the Parameter
	 *@return       The value value
	 */
	public String getValue(String node)
	{
		getOffset(node);
		return fXML.substring(fOffset, fOffsetEnd);
	}

	/**
	 *  Count Nodes off a specific Node
	 *
	 *@param  node  Description of the Parameter
	 *@return       The count value
	 */
	public int getCount(String node)
	{
		int last = node.lastIndexOf(':');

		if (last == -1)
		{
			fOffset = 0;
			fOffsetEnd = fXML.length();
		}
		else
		{
			String parentNode = node.substring(0, last);
			getOffset(parentNode);
		}

		Pattern p = Pattern.compile( "<" + node.substring(last + 1) + "(?!\\w).*?>" );
		Matcher startElementMatcher = p.matcher( fXML );
		int nodeCount = 0;
		int tmpOffset = fOffset;

		while ( startElementMatcher.find( tmpOffset ) && tmpOffset < fOffsetEnd )
		{
			tmpOffset = startElementMatcher.end();
			if( tmpOffset < fOffsetEnd )
				nodeCount++;
		}

		return nodeCount;
	}

	/**
	 *  Save the XML to a file (not implented)
	 *
	 *@param  filename  Description of the Parameter
	 */
	public void saveToFile(String filename)
	{
	}

	public void loadFromStream(InputStream is) throws IOException
	{
		StringBuffer inputBuf = new StringBuffer();
		byte[] buf = new byte[4 * 1024]; // 4K buffer
		int bytesRead;

		while ((bytesRead = is.read(buf)) != -1)
		{
			inputBuf.append( new String(buf, 0, bytesRead) );
		}
		setXML( inputBuf.toString() );
	}

	/**
	 *  Load XML from a file
	 *
	 *  @param  filename                   Description of the Parameter
	 *  @exception  FileNotFoundException  Description of the Exception
	 *  @exception  IOException            Description of the Exception
	 */
	public void loadFromFile(String filename)
		throws FileNotFoundException, IOException
	{
		// A FileInputStream is for bytes
		FileInputStream fis = null;

		try
		{
			fis = new FileInputStream(filename);
			loadFromStream(fis);
		}
		finally
		{
			if (fis != null)
			{
				fis.close();
			}
		}
	}


	/**
	 *  Get Node - returns the name of the current Node
	 *
	 *@return    The node value
	 */
	public String getNode()
	{
		return fNode;
	}


	/**
	 *  Return the current XML as a String
	 *
	 *@return    The xML value
	 */
	public String getXML()
	{
		return fXML;
	}


	/**
	 *  Load the XML from a String
	 *
	 *@param  xml  The new xML value
	 */
	public void setXML(String xml)
	{
		fXML = xml;
	}


	/**
	 * Get Tag - gets the contents of the specified tag - not the value.
	 *
	 * @param  node  Description of the Parameter
	 * @return       The tag value
	 * @deprecated  Use getElement() instead.
	 */
	public String getTag(String node)
	{
		getOffset(node);
		String result = fXML.substring(fTagOffset, fTagOffsetEnd);
		return result;
	}


	/**
	 * Get Element - returns a new parser object based on the XML for the named
	 * element.
	 *
	 * @param  elementPath  The path of the element to return.
	 * @return An IDXMLNode based on the element.  This is currently another IDXMLParser instance.
	 * @todo   Cache the lookup of offsets.
	 *
	 */
	public IDXMLNode getElement(String elementPath)
	{
		// Lookup elementPath in map.
		IDXMLNode element = (IDXMLNode)elementMap.get( elementPath );

		// If element is not in map, extract it, and put it in the map for later use.
		if ( element == null )
		{
			getOffset(elementPath);
			String result = fXML.substring(fTagOffset, fTagOffsetEnd);
			element = new IDXMLParser();
			element.setXML( result );
			elementMap.put( elementPath, element );
		}

		return element;
	}


	/**
	 *  Get Attribute - gets an attribute value out of the specified tag.
	 *
	 *@param  node       Description of the Parameter
	 *@param  attribute  Description of the Parameter
	 *@return            The attribute value
	 */
	public String getAttribute(String node, String attribute)
	{
		String tag = getTag(node);

		Pattern p = Pattern.compile( "\\s" + attribute + "\\s*=\\s*([\"'])(.*?)\\1" );
		Matcher attributeMatcher = p.matcher( tag );

		if( attributeMatcher.find() )
		{
			return attributeMatcher.group(2);
		}
		else
		{
			return "";
		}
	}
}

