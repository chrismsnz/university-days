#!/usr/bin/python
import os, sys, ciber2xml, StringIO, urllib, string, re
from ftplib import FTP
from xml.dom import minidom, Node

class CiberUpload:
	def createPrefixList( self ):
		return { '1242':'Bahamas', \
			'1246':'Barbados', \
			'1264':'Anguilla', \
			'1268':['Antigua', 'Barbuda'], \
			'1284':'British Virgin Islands', \
			'1340':'US Virgin Islands', \
			'1345':'Cayman Islands', \
			'1441':'Bermuda', \
			'1473':'Grenada', \
			'1649':'Turks & Caicos', \
			'1664':'Montserrat', \
			'1670':'Mariana Islands', \
			'1758':'St. Lucia', \
			'1767':'Dominica', \
			'1784':'St. Vincent', \
			'1787':'Puerto Rico', \
			'1809':'Dominican Rep.', \
			'1868':'Trinidad & Tobago', \
			'1869':['Nevis', 'St. Kitts'], \
			'1876':'Jamaica', \
			'1':['Canada', 'USA'], \
			'20':'Egypt', \
			'212':'Morocco', \
			'213':'Algeria', \
			'216':'Tunisia', \
			'218':'Libya', \
			'220':'Gambia', \
			'221':'Senegal', \
			'222':'Mauritania', \
			'223':'Mali', \
			'224':'Guinea', \
			'225':'Ivory Coast', \
			'226':'Burkina Faso', \
			'227':'Niger', \
			'228':'Togo', \
			'229':'Benin', \
			'230':'Mauritius', \
			'231':'Liberia', \
			'232':'Sierra Leone', \
			'233':'Ghana', \
			'234':'Nigeria', \
			'235':'Chad', \
			'236':'Central African Rep.', \
			'237':'Cameroon', \
			'238':'Cape Verde Islands', \
			'239':'Sao Tome & Principe', \
			'240':'Equatorial Guinea', \
			'241':'Gabon', \
			'242':'Congo', \
			'243':'Congo, Dem. Rep. of', \
			'244':'Angola', \
			'245':'Guinea Bissau', \
			'246':'Diego Garcia', \
			'247':'Ascension Island', \
			'248':'Seychelles', \
			'249':'Sudan', \
			'250':'Rwanda', \
			'251':'Ethiopia', \
			'252':'Somalia', \
			'253':'Djibouti', \
			'254':'Kenya', \
			'255':'Tanzania', \
			'256':'Uganda', \
			'257':'Burundi', \
			'258':'Mozambique', \
			'260':'Zambia', \
			'261':'Madagascar', \
			'262':'Reunion Island', \
			'263':'Zimbabwe', \
			'264':'Namibia', \
			'265':'Malawi', \
			'266':'Lesotho', \
			'267':'Botswana', \
			'268':'Swaziland', \
			'269':['Comoros', 'Mayotte Islands'], \
			'27':'South Africa', \
			'290':'St. Helena', \
			'291':'Eritrea', \
			'297':'Aruba', \
			'298':'Faeroe Islands', \
			'299':'Greenland', \
			'30':'Greece', \
			'31':'Netherlands', \
			'32':'Belgium', \
			'33':'France', \
			'34':'Spain', \
			'350':'Gibraltar', \
			'351':'Portugal', \
			'352':'Luxembourg', \
			'353':'Ireland', \
			'354':'Iceland', \
			'355':'Albania', \
			'356':'Malta', \
			'357':'Cyprus', \
			'358':'Finland', \
			'359':'Bulgaria', \
			'36':'Hungary', \
			'370':'Lithuania', \
			'371':'Latvia', \
			'372':'Estonia', \
			'373':'Moldova', \
			'374':'Armenia', \
			'375':'Belarus', \
			'376':'Andorra', \
			'377':'Monaco', \
			'378':'San Marino', \
			'380':'Ukraine', \
			'381':['Serbia', 'Yugoslavia'], \
			'385':'Croatia', \
			'386':'Slovenia', \
			'387':'Bosnia', \
			'389':'Macedonia', \
			'39':['Italy', 'Vatican City'], \
			'40':'Romania', \
			'41':'Switzerland', \
			'420':'Czech Republic', \
			'421':'Slovakia', \
			'423':'Liechtenstein', \
			'43':'Austria', \
			'44':'United Kingdom', \
			'45':'Denmark', \
			'46':'Sweden', \
			'47':'Norway', \
			'48':'Poland ', \
			'49':'Germany', \
			'500':'Falkland Islands', \
			'501':'Belize', \
			'502':'Guatemala', \
			'503':'El Salvador', \
			'504':'Honduras', \
			'505':'Nicaragua', \
			'506':'Costa Rica', \
			'507':'Panama', \
			'508':'St. Perre & Miquelon', \
			'509':'Haiti', \
			'51':'Peru', \
			'52':'Mexico', \
			'5399':'Guantanamo Bay', \
			'53':'Cuba', \
			'54':'Argentina', \
			'55':'Brazil', \
			'56':'Chile', \
			'57':'Colombia', \
			'58':'Venezuela', \
			'590':'Guadeloupe', \
			'591':'Bolivia', \
			'592':'Guyana', \
			'593':'Ecuador', \
			'594':'French Guiana', \
			'595':'Paraguay', \
			'596':['French Antilles', 'Martinique'], \
			'597':'Suriname', \
			'598':'Uruguay', \
			'599':'Netherlands Antilles', \
			'60':'Malaysia', \
			'61':['Australia', 'Christmas Island', 'Cocos Islands'], \
			'62':'Indonesia', \
			'63':'Philippines', \
			'64':'New Zealand', \
			'65':'Singapore', \
			'66':'Thailand', \
			'671':'Guam', \
			'672':['Antarctica', 'Norfolk Island'], \
			'673':'Brunei', \
			'674':'Nauru', \
			'675':'Papua New Guinea', \
			'676':'Tonga', \
			'677':'Solomon Islands', \
			'678':'Vanuatu', \
			'679':'Fiji Islands', \
			'680':'Palau', \
			'681':'Wallis & Futuna', \
			'682':'Cook Islands', \
			'683':'Niue', \
			'684':'American Samoa', \
			'685':'Western Samoa', \
			'686':'Kiribati', \
			'687':'New Caledonia', \
			'688':'Tuvalu', \
			'689':'French Polynesia', \
			'691':'Micronesia', \
			'692':'Marshall Islands', \
			'7':['Kazakhstan', 'Russia'], \
			'808':['Midway Island', 'Wake Island'], \
			'81':'Japan', \
			'82':'Korea, South', \
			'84':'Vietnam', \
			'850':'Korea, North', \
			'852':'Hong Kong', \
			'853':'Macau', \
			'855':'Cambodia', \
			'856':'Laos', \
			'86':'China', \
			'880':'Bangladesh', \
			'886':'Taiwan', \
			'90':'Turkey', \
			'91':'India', \
			'92':'Pakistan', \
			'93':'Afghanistan', \
			'94':'Sri Lanka', \
			'95':'Burma (Myanmar)', \
			'95':'Myanmar (Burma)', \
			'960':'Maldives', \
			'961':'Lebanon', \
			'962':'Jordan', \
			'963':'Syria', \
			'964':'Iraq', \
			'965':'Kuwait', \
			'966':'Saudi Arabia', \
			'967':'Yemen', \
			'968':'Oman', \
			'970':'Palestine', \
			'971':'United Arab Emirates', \
			'972':'Israel', \
			'973':'Bahrain', \
			'974':'Qatar', \
			'975':'Bhutan', \
			'976':'Mongolia', \
			'977':'Nepal', \
			'98':'Iran', \
			'992':'Tajikistan', \
			'993':'Turkmenistan', \
			'994':'Azberbaijan', \
			'995':'Georgia', \
			'996':'Kyrgyzstan', \
			'998':'Uzbekistan' }

	def recordError( self, output, calledNumber, mobileNumber, quantity ):
		print >>output, '<errorrecord>'
		print >>output, '	<mobilenumber>' + mobileNumber + '</mobilenumber>'
		print >>output, '	<callednumber>' + calledNumber + '</callednumber>'
		print >>output, '	<chargeabletime>' + repr( quantity ) + '</chargeabletime>'
		print >>output, '</errorrecord>'

	def getTextForElementName( self, node, tagName ):
		nodes = node.getElementsByTagName( tagName )
		return self.getTextFromNode( nodes.item( 0 ) )

	def getTextFromNode( self, node ):
		for child in node.childNodes:
			if child.nodeType == Node.TEXT_NODE:
				return child.nodeValue
		return None

	def getFormattedTime( self, timeInSeconds ):
		return repr( timeInSeconds / 60 ) + ":" + string.zfill( repr( timeInSeconds % 60 ), 2 )

	def getDescription( self, calledNumber, quantity, prefix ):
		return "+" + prefix + " " + calledNumber[len( prefix ):] + " for " + self.getFormattedTime( quantity )

	def getQuantity( self, transaction ):
		chargeable = self.getTextForElementName( transaction, 'chargeable_time' )

		hours = int( chargeable[:2] )
		minutes = int( chargeable[2:4] )
		seconds = int( chargeable[4:] )

		seconds += (hours * 60 * 60) + (minutes * 60)

		return seconds

	def getIDFromXML( self, pageName, params, tagName ):
		if params:
			requestParams = urllib.urlencode( params )
			sock = urllib.urlopen( self.baseOfURL + pageName, requestParams )
			source = sock.read()
			sock.close()

			doc = minidom.parseString( source )
			elementList = doc.getElementsByTagName( tagName )
			if len( elementList ) > 0:
				idlist = elementList.item( 0 ).getElementsByTagName( 'id' )
				if len( idlist ) > 0:
					return int( self.getTextFromNode( idlist.item( 0 ) ) )
			doc.unlink()

		return None

	def getPrefix( self, number ):
		for prefix in [ number[:i] for i in xrange( 4, 0, -1 ) ]:
			if self.prefixList.has_key( prefix ):
				return prefix

		return None

	def getPhoneNumber( self, transaction, length_field, main_number_field, number_overflow_field ):
		numberLength = int( self.getTextForElementName( transaction, length_field ) )
		number = self.getTextForElementName( transaction, main_number_field )

		if numberLength > 10:
			numberOverflow = self.getTextForElementName( transaction, number_overflow_field )
			number += numberOverflow[:(numberLength - 10)]
		else:
			number = number[:numberLength]

		return number

	def createInvoiceLines( self, ciberdoc, errorOutput ):
		invoiceLines = []

		for transactions in ciberdoc.getElementsByTagName('transactions'):
			for transaction in transactions.getElementsByTagName('transaction'):
				calledNumber = self.getPhoneNumber( transaction, 'called_number_len', 'called_number', 'called_Number_over')
				mobileNumber = self.getPhoneNumber( transaction, 'mobile_number_len', 'mobile_number', 'mobile_number_overflow' )

				prefix = self.getPrefix( calledNumber )

				productid = self.getIDFromXML( 'product-find-xml.do', {'description': prefix}, 'product' )
				clientid = self.getIDFromXML( 'client-find-xml.do', {'mobilePhone': mobileNumber}, 'client' )
				quantity = self.getQuantity( transaction )

				if productid and clientid and quantity and prefix:
					description = self.getDescription( calledNumber, quantity, prefix )
					invoiceLines.append( { "product_id":productid, "client_id":clientid, "quantity":(quantity/60.0), "description":description } )
				else:
					self.recordError( errorOutput, calledNumber, mobileNumber, quantity )

		return invoiceLines

	def uploadInvoiceLines( self, invoiceLines ):
		for params in invoiceLines:
			requestParams = urllib.urlencode( params )
			sock = urllib.urlopen( self.baseOfURL + 'item-import-xml.do', requestParams )
			source = sock.read()
			sock.close()

	def main( self, urlbase='http://localhost:8080/nbnaccounts/', ftpaddr='localhost' ):
		self.prefixList = self.createPrefixList()
		self.baseOfURL = urlbase

		iridiumCode = 'C26320'
		serviceProviderCode = '99980'
		filenameRegex = re.compile( iridiumCode + serviceProviderCode + '[0-9]{4}\.dat' )

		ftpconnection = FTP( ftpaddr )
		ftpconnection.login()
		ftpconnection.cwd( '/pub' )
		filenames = ftpconnection.nlst()
		datfiles = [ filename for filename in filenames if filenameRegex.match( filename ) ]

		ftplines = []
		for datfilename in datfiles:
			ftpconnection.retrlines( 'RETR ' + datfilename, ( lambda x: ftplines.append( x ) ) )

		outputfile = StringIO.StringIO()
		outputfile.write( '<transactions>' )
		for line in ftplines:
			if len( line ) < 2: continue

			# Interpret the Line into a Data Structure
			recordtype = line[:2]

			if recordtype=="10":
				ciber2xml.record10( line, outputfile )
			elif recordtype=="20":
				ciber2xml.record20( line, outputfile )
		outputfile.write( '</transactions>' )

		ciberxml = outputfile.getvalue()
		outputfile.close()

		ciberdoc = minidom.parseString( ciberxml )
		invoiceLines = self.createInvoiceLines( ciberdoc, sys.stderr )
		ciberdoc.unlink()

		self.uploadInvoiceLines( invoiceLines )

if __name__ == "__main__":
	uploader = CiberUpload()
	uploader.main( )
