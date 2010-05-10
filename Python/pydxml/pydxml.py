"""Module PyDXML contains classes and helpful functions which have
been ported from IDXML's Java implementation.

Code released under the same licence as IDXML.
(C)Chris Smith <chris.smith@nothingbutnet.co.nz>
"""

import string
import re

class IDXMLParser:
	"""A Lightweight XML parser based on the IDXML Java implementation 
	written by Peter Harrison and his crew at http://idxml.sf.net
	"""
	
	# Special class functions
	def __init__(self, xml="<xml></xml>"):
		"""IDXMLParser initial method. Takes an optional string of the
		xml to be parsed. Otherwise it needs to be set using setXML().
		Also sets other attributes to default values.
		"""
		# Private Attributes
		self._elementmap = {}
		self._xml = ""
		self._node = ""
		
		# Public Attributes
		self.insert = 0
		self.offset = 0
		self.offsetend = 0
		self.tagoffset = 0
		self.tagoffsetend = 0
		
		self.setxml(xml)
	
	# Private Class functions
	def _findelement(self, startmatcher, endmatcher, pos):
		tmpoffset = self.tagoffset
		for i in range(pos-1):
			smatch = startmatcher.search(self._xml, tmpoffset)
			if smatch:
				tmpoffset = smatch.end()
				# Does this tag end itself? e.g. <foo />
				if self._xml[tmpoffset-2:tmpoffset-1] != '/':
					# It doesn't, find the end tag
					ematch = endmatcher.search(self._xml, tmpoffset)
					if ematch:
						tmpoffset = ematch.end()
					else:
						self._resetoffsets()
						return -1 # XML incomplete
			else:
				self._resetoffsets()
				return -1 # Tag doesn't exist, bad query
		smatch = startmatcher.search(self._xml, tmpoffset)
		if smatch:
			tmpoffset = smatch.start()
		else:
			self._resetoffsets()
			return -1 #Tag doesn't exist, bad query
		return tmpoffset
	
	def _resetoffsets(self):
		"""Resets the values of all the offset variables"""
		self.offset = 0
		self.offsetend = 0
		self.tagoffset = 0
		self.tagoffsetend = 0
	
	def _setnode(self, node=""):
		self.node = node
		self.getoffset(node)
		self.insert = self.offset
	
	def _getparents(self, node):
		"""Returns a list of parents from the given node"""
		parents = node.split(':')
		return parents
	
	# Public Class functions
	def test(self, string):
		"""Internal test function"""
		pass
	
	def getoffset(self, node):
		"""Gets a numerical offset to the given node. Doesn't return
		anything but sets values in the data structure
		"""
		parents = self._getparents(node)
		self._resetoffsets()
		
		for parent in parents:
			elementtype = parent
			specifiedpos = False
			tmpoffset = self.tagoffset
			
			start = elementtype.rfind('@')
			if(start > -1):
				specifiedpos = True	
				elementtype = elementtype[0:start]
			
			# Create compiled regex's that represent the start and end tags
			startmatcher = re.compile('<' + elementtype + '(?!\\w).*?>')
			endmatcher = re.compile('</' + elementtype + '\\s*>')
			
			if specifiedpos:
				pos = int(parent[start+1:])
				tmpoffset = self._findelement(startmatcher, endmatcher, pos)
				if tmpoffset == -1:
					# Invalid position. Raise exception?
					self._resetoffsets()
					return None
			
			smatch = startmatcher.search(self._xml, tmpoffset)
			if smatch:
				self.tagoffset = smatch.start()
				self.offset = smatch.end()
# 				print "Start found : ", smatch.group()
			else:
				self._resetoffsets()
				return None
			
			# Is tag singular or do we look for an end tag
			if self._xml[self.offset-2:self.offset-1] == '/':
				self.offsetend = self.offset
				self.tagoffsetend = self.offset
# 				print "End found (singular tag)"
# 				print "o: ", self.offset, " oe: ", self.offsetend
# 				print "to: ", self.tagoffset, "toe: ", self.tagoffsetend
			else:
				ematch = endmatcher.search(self._xml, self.offset)
				if ematch:
					self.offsetend = ematch.start()
					self.tagoffsetend = ematch.end()
# 					print "End found : ", ematch.group()
# 					print "o: ", self.offset, " oe: ", self.offsetend
# 					print "to: ", self.tagoffset, "toe: ", self.tagoffsetend
				else:
					self._resetoffsets()
					return None # Bad XML
	
	def addnode(self, node):
		"""NOT IMPLEMENTED: Add a node to the XML String."""
		pass

	def deletenode(self, node):
		"""NOT IMPLEMENTED: Delete a node from the XML String."""
		pass
	
	def editvalue(self, node, value):
		"""NOT IMPLEMENTED: Edit a Node's value."""
		pass
	
	def addelement(self, node, value):
		"""NOT IMPLEMENTED: Add an element to the XML string"""
		pass
	
	def getvalue(self, node):
		"""Get a Value of the Element Specified. This does not include 
		the element itself however.
		"""
		self.getoffset(node)
		return self._xml[self.offset:self.offsetend]
	
	def getcount(self, node):
		"""Count the occurences of a specific node"""
		last = node.rfind(':');
		
		if last == -1:
			self.offset = 0
			self.offsetend = len(self._xml)
		else:
			parent = node[0:last]
			self.getoffset(parent)
		
		elementname = node[last+1:]
		startmatcher = re.compile('<' + elementname + '(?!\\w).*?>')
		nodecount = 0
		tmpoffset = self.offset
		smatch = startmatcher.search(self._xml, tmpoffset)
		while smatch and tmpoffset < self.offsetend:
			tmpoffset = smatch.end()
			if tmpoffset < self.offsetend:
				nodecount += 1
			smatch = startmatcher.search(self._xml, tmpoffset)
		return nodecount
	
	def savetofile(self, file):
		"""NOT IMPLEMENTED: Saves the XML String to a file"""
		pass
	
	def loadfromfile(self, file):
		"""Load XML from a given file"""
		pass
	
	def getnode(self):
		"""Returns the current node"""
		return self._node
	
	def getxml(self):
		"""Returns the current XML string"""
		return self._xml
	
	def setxml(self, xml="<xml></xml>"):
		"""Sets the "xml" data member to the given arguement. The 
		argument has a default value of "<xml></xml>" (Empty XML).
		"""
		self._xml = xml
	
	def gettag(self, node):
		"""Returns the entire contents of specified tag - not just the
		value. (i.e. opening/closing tag and value)
		
		NOTE: Deprecated, use getelement()
		"""
		self.getoffset(node)
		return self._xml[self.tagoffset:self.tagoffsetend]
	
	def getelement(self, elementpath):
		"""Get Element - returns a new parser object based on the XML
		for the named element
		"""
		try:
			self._elementmap[elementpath]
		except KeyError:
			getoffset(elementpath)
			result = self._xml[self.tagoffset:tagoffsetend]
			element = IDXMLParser( result )
			self._elementmap[elementpath] = element
		return self._elementmap[elementpath]
	
	def getattribute(self, node, attribute):
		"""Return the value for the attribute associated with the named
		node. Value only."""
		tag = self.gettag(node)
		matcher = re.compile("\\s" + attribute + "\\s*=\\s*([\"'])(.*?)\\1")
		match = matcher.search(tag)
		if match:
			return match.group(2)
		else :
			return ""
