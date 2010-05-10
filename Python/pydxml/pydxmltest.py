#!/usr/bin/python
"""Unit tests for pydxml XML Parser"""

import pydxml
import unittest

from pydxml import IDXMLParser

class MessageTest(unittest.TestCase):
	"""Test the parser on a sample message"""
	xml = """
	<message from=\"talios@stumpy.asterisk\" to='taxiservice@stumpy.asterisk'>
		<body>Hello!</body>
	</message>
	"""
	parser = IDXMLParser(xml)
	
	def testXML(self):
		"""[Message] Checking the xml property is set"""
		result = self.parser.getxml()
		self.assertEqual(self.xml, result)
	
	def testAttributes(self):
		"""[Message] Checking attribute matching"""
		result = self.parser.getattribute('message', 'from')
		self.assertEqual('talios@stumpy.asterisk', result)
		
		result = self.parser.getattribute('message', 'to')
		self.assertEqual('taxiservice@stumpy.asterisk', result)
		
	def testValues(self):
		"""[Message] Checking value matching"""
		result = self.parser.getvalue('message:body')
		self.assertEqual('Hello!', result)
	
class ExtrasTest(unittest.TestCase):
	"""Test with multiple elements in the xml string"""
	xml = """
	<extras>
		<extra cost="3.00" name="Bike"/>
		<extra cost="40.00" name="Soiling"/>
		<extra cost="88.00" name="Woman"/>
		<extra cost="135.00" name="money"/>
		<extra cost="23827498.00" name="ing"/>
	</extras>
	"""
	parser = IDXMLParser(xml)
	
	def testAttributes(self):
		"""[Extras] Checking attribute matching"""
		result = self.parser.getattribute('extras:extra@1', 'cost')
		self.assertEqual('3.00', result)
		
		result = self.parser.getattribute('extras:extra@3', 'name')
		self.assertEqual('Woman', result)
		
		result = self.parser.getattribute('extras:extra@5', 'name')
		self.assertEqual('ing', result)
		
	def testCounting(self):
		"""[Extras] Checking element counting"""
		result = self.parser.getcount('extras:extra')
		self.assertEqual(5, result)
	
	def testValues(self):
		"""[Extras] Checking value matching"""
		result = self.parser.getvalue('extras:extra@1')
		self.assertEqual('', result)
		
		result = self.parser.getvalue('extras:extra@5')
		self.assertEqual('', result)
	

class BoxTest(unittest.TestCase):
	"""Test with complex xml"""
	xml = """
	<boxes>
		<box name="Administration" side="left" template="boxes/basebox.vm">
			<option type="defaultState">open</option>
		 	<entry action="/userlist.do">User Maintenance</entry>
		 	<entry action="/rolelist.do">Role Maintenance</entry>
		 	<entry action="/ailist.do">Action Maintenance</entry>
		 </box>
		 <box name="Logout" template="boxes/basebox.vm" side=\"left\">
		 	<entry action="/changepassword.vm">Change My Details</entry>
		 </box>
	</boxes>
	"""
	parser = IDXMLParser(xml)
	
	def testCounting(self):
		"""[Box] Checking element counting"""
		result = self.parser.getcount('boxes:box@1:entry')
		self.assertEqual(3, result)
		
		result = self.parser.getcount('boxes:box')
		self.assertEqual(2, result)
		
		result = self.parser.getcount('boxes:box@1:option')
		self.assertEqual(1, result)
	
	def testValues(self):
		"""[Box] Checking value matching"""
		result = self.parser.getvalue('boxes:box@2:entry@1')
		self.assertEqual('Change My Details', result)
		
	def testAttributes(self):
		"""[Box] Checking attribute matching"""
		result = self.parser.getattribute('boxes:box@2', 'template')
		self.assertEqual('boxes/basebox.vm', result)
		
		result = self.parser.getattribute('boxes:box@1:entry@3', 'action')
		self.assertEqual('/ailist.do', result)
	
class ExceptionTest(unittest.TestCase):
	"""Tests that exceptions are raised where they should be"""
	xml = """
	<invalid>
		<badtag attrib='value'>
		<validtag />
		</closeonly>
	</invalid>
	"""
	parser = IDXMLParser(xml)
	
	def testInvalidXMLException(self):
		"""[ERRORS] Check that errors occour for invalid XML"""
		tag = 'invalid:badtag'
		
		self.assertRaises(pydxml.IDXMLBadXMLException, 
			self.parser.getvalue, tag)
		self.assertRaises(pydxml.IDXMLBadXMLException,
			self.parser.getattribute, tag, 'attrib')

if __name__ == "__main__":
    unittest.main()