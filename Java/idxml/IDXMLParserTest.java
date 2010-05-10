/*
 * JobTest.java
 * NetBeans JUnit based test
 *
 * Created on July 9, 2002, 5:46 PM
 */

package net.sourceforge.idxml;

import junit.framework.*;
import java.beans.*;

public class IDXMLParserTest extends TestCase
{

	public IDXMLParserTest(java.lang.String testName)
	{
		super(testName);
	}

	public static Test suite()
	{
		return new TestSuite(IDXMLParserTest.class);
	}

	public void testMessage()
	{
		String xml = "<message from=\"talios@stumpy.asterisk\" to='taxiservice@stumpy.asterisk'> ";
		xml += "<body>hello</body></message>";

		IDXMLParser parser = new IDXMLParser();
		parser.setXML( xml );

		System.out.println( parser.getXML() );

		assertTrue( xml.equals( parser.getXML() ) );

		String testString = parser.getAttribute("message", "from");
		System.out.println( testString );
		assertTrue( "talios@stumpy.asterisk".equals( testString ) );

		testString = parser.getAttribute("message", "to");
		System.out.println( testString );
		assertTrue( "taxiservice@stumpy.asterisk".equals( testString ) );

		testString = parser.getValue("message:body");
		System.out.println( testString );
		assertTrue( "hello".equals( testString ) );

	}

	public void testOffsets()
	{
		/*
		String xml = null;
		xml = "<extras>";
		xml += "<extra cost=\"3.00\" name=\"Bike\"/>";
		xml += "<extra cost=\"40.00\" name=\"Soiling\"/>";
		xml += "</extras>";

		IDXMLParser parser = new IDXMLParser();
		parser.setXML( xml );

//		parser.getOffset("extras:extra@1");
//		assertEquals(8, parser.fTagOffset);
//		assertEquals(40, parser.fTagOffsetEnd);

//		assertEquals("<extra cost=\"3.00\" name=\"Bike\"/>", xml.substring(parser.fTagOffset, parser.fTagOffsetEnd));
		assertEquals("<extra cost=\"3.00\" name=\"Bike\"/>", parser.getTag("extras:extra@1"));
		assertEquals("<extra cost=\"40.00\" name=\"Soiling\"/>", parser.getTag("extras:extra@2"));
		*/
	}

	public void testMultipleElements()
	{
		String xml = null;
		xml = "<extras>";
		xml += "<extra cost=\"3.00\" name=\"Bike\"/>";
		xml += "<extra cost=\"40.00\" name=\"Soiling\"/>";
		xml += "<extra cost=\"88.00\" name=\"Woman\"/>";
		xml += "<extra cost=\"135.00\" name=\"money\"/>";
		xml += "<extra cost=\"23827498.00\" name=\"ing\"/>";
		xml += "</extras>";

		IDXMLParser parser = new IDXMLParser();
		parser.setXML( xml );

		assertTrue( parser.getValue("extras:extra@1") != null);
		assertTrue( parser.getValue("extras:extra@2") != null);

		assertEquals("", parser.getValue("extras:extra@2") );
		assertEquals("", parser.getValue("extras:extra@1") );

		//assertEquals("<extra cost=\"40.00\" name=\"Soiling\"/>", parser.getTag("extras:extra@2") );
		//assertEquals("<extra cost=\"3.00\" name=\"Bike\"/>", parser.getTag("extras:extra@1") );

		assertEquals("40.00", parser.getAttribute("extras:extra@2", "cost"));
		assertEquals("money", parser.getAttribute("extras:extra@4", "name"));
		assertEquals("ing", parser.getAttribute("extras:extra@5", "name"));

		xml = "<boxes>";
		xml += "<box name=\"Administration\" side=\"left\" template=\"boxes/basebox.vm\">";
		xml += "<option type=\"defaultState\">open</option>";
		xml += "<entry action=\"/userlist.do\">User Maintenance</entry>";
		xml += "<entry action=\"/rolelist.do\">Role Maintenance</entry>";
		xml += "<entry action=\"/ailist.do\">Action Maintenance</entry>";
		xml += "</box>";
		xml += "<box name=\"Logout\" template=\"boxes/basebox.vm\" side=\"left\">";
		xml += "<entry action=\"/changepassword.vm\">Change My Details</entry>";
		xml += "</box>";
		xml += "</boxes>";

		parser = new IDXMLParser();
		parser.setXML( xml );

		assertEquals( 3, parser.getCount("boxes:box@1:entry") );
		assertEquals( 2, parser.getCount("boxes:box") );
		assertEquals( 1, parser.getCount("boxes:box@1:option") );
		//assertEquals( 0, parser.getCount("boxes:entry") ); // Known bug :(
		assertEquals( "Change My Details", parser.getValue( "boxes:box@2:entry@1" ) );
		assertEquals( "boxes/basebox.vm", parser.getAttribute( "boxes:box@2", "template" ) );
		assertEquals( "/ailist.do", parser.getAttribute( "boxes:box@1:entry@3", "action" ) );
	}
}
