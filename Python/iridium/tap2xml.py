#!/usr/bin/python

# Besed on Mikes ciber2XML.py code.
import os
import sys

def out( tag, content, outputfile ):
	outputfile.write( '\t<' + tag + '>' + content + '</' + tag + '>\n' )
	return

def splitIntoRecords( text ):
	records = []
	for i in range( 0, len(text), 160 ):
		records.append( text[i:i+160] )
	
	return records

def recordHeader( line, outputfile ):
	"""Process a TAPII Header record"""
	record_code = line[0:2] # Numeric
	sender = line[2:7] # Alphanumeric
	recipient = line[7:12] # Alphanumeric
	file_seq = line[12:17] # Alphanumeric[1] Numeric[4]
	tax_treatment = line[17:18] # Alphanumeric
	tax_rate = line[18:50] # Numeric
	# unused = line[50:63]
	file_creation_date = line[63:69] # YYMMDD
	file_transmission_date = line[69:75] # YYMMDD
	transfer_cutoff_timestamp = line[75:87] # YYMMDDHHMMSS
	utc_time_offset = line[87:92] # +/-HHMM
	specification_version_number = line[92:94] # Numeric
	international_access_code = line[94:106] # Alphanumeric
	country_code = line[106:114] # Alphanumeric
	# unused = line[114:160]
	
	outputfile.write( '<header>\n' )
	out( 'record_code', record_code, outputfile )
	out( 'sender', sender, outputfile )
	out( 'recipient', recipient, outputfile )
	out( 'file_seq', file_seq, outputfile )
	out( 'tax_treatment', tax_treatment, outputfile )
	out( 'tax_rate', tax_rate, outputfile )
	out( 'file_creation_date', file_creation_date, outputfile )
	out( 'file_transmission_date', file_transmission_date, outputfile )
	out( 'transfer_cutoff_timestamp', transfer_cutoff_timestamp, outputfile )
	out( 'utc_time_offset', utc_time_offset, outputfile )
	out( 'specification_version_number', specification_version_number, outputfile )
	out( 'international_access_code', international_access_code, outputfile )
	out( 'country_code', country_code, outputfile )
	outputfile.write( '</header>\n' )
	
def recordCommon( line, outputfile ):
	"""Process a record"""
	record_code = line[0:2] # Numeric
	chain_reference = line[2:8] # Numeric
	# unused = line[8:9]
	imsi = line[9:24] # Numeric
	imei = line[24:40] # Numeric
	modification_indicator = line[40:41] # Alphanumeric - Always 1
	type_of_number = line[41:42] # Alphanumeric
	numbering_plan = line[42:43] # Alphanumeric
	# Called number when MOC, Calling number when MTC
	number = line[43:64] # Alphanumeric
	service_type = line[64:65] # Alphanumeric
	service_code = line[65:67] # Alphanumeric
	dual_service_type = line[67:68] # Aplhanumeric
	dual_service_code = line[68:70] # Alphanumeric
	radio_channel_requested = line[70:71] # Alphanumeric
	radio_channel_used = line[71:72] # Alphanumeric
	transparency_indicator = line[72:73] # Alphanumeric
	ss_events = line[73:88] # Alphanumeric - not parsed!
	mscid = line[88:103] # Alphanumeric
	location_area = line[103:108] # Numeric
	cell_id = line[108:113] # Numeric
	mobile_station_class = line[113:14] # Numeric
	charging_date = line[114:120] # YYMMDD
	charge_start_time = line[120:126] # HHMMSS
	utc_time_offset_code = line[126:127] # Alphanumeric
	chargable_units = line[127:133] # Numeric
	data_volume_reference = line[133:139] # Numeric
	charge = line[139:148] # Numeric
	charged_item = line[148:149] # Alphanumeric
	tax_rate_code = line[149:150] # Numeric
	exchange_rate_code = line[150:151] # Alphanumeric
	# Destination network when MOC, Originating Network when MTC
	network = line[151:157] # Alphanumeric
	# unused = line[157:160]
	
	out( 'record_code', record_code, outputfile )
	out( 'chain_reference', chain_reference, outputfile )
	out( 'imsi', imsi, outputfile )
	out( 'imei', imei, outputfile )
	out( 'modification_indicator', modification_indicator, outputfile )
	out( 'type_of_number', type_of_number, outputfile )
	out( 'numbering_plan', numbering_plan, outputfile )
	out( 'number', number, outputfile )
	out( 'service_type', service_type, outputfile )
	out( 'service_code', service_code, outputfile )
	out( 'dual_service_type', dual_service_type, outputfile )
	out( 'dual_service_code', dual_service_code, outputfile )
	out( 'radio_channel_requested', radio_channel_requested, outputfile )
	out( 'radio_channel_used', radio_channel_used, outputfile )
	out( 'transparency_indicator', transparency_indicator, outputfile )
	out( 'ss_events', ss_events, outputfile )
	out( 'mscid', mscid, outputfile )
	out( 'location_area', location_area, outputfile )
	out( 'cell_id', cell_id, outputfile )
	out( 'mobile_station_class', mobile_station_class, outputfile )
	out( 'charging_date', charging_date, outputfile )
	out( 'charge_start_time', charge_start_time, outputfile )
	out( 'utc_time_offset_code', utc_time_offset_code, outputfile )
	out( 'chargable_units', chargable_units, outputfile )
	out( 'data_volume_reference', data_volume_reference, outputfile )
	out( 'charge', charge, outputfile )
	out( 'charged_item', charged_item, outputfile )
	out( 'tax_rate_code', tax_rate_code, outputfile )
	out( 'exchange_rate_code', exchange_rate_code, outputfile )
	out( 'network', network, outputfile )

def recordMOC( line, outputfile ):
	"""Process a Mobile Originated Call record"""
	outputfile.write( '<record type="moc">\n' )
	recordCommon( line, outputfile )
	outputfile.write( '</record>\n' )
		
def recordMTC( line, outputfile ):
	"""Process a mobile terminated call record"""
	outputfile.write( '<record type="mtc">\n' )
	recordCommon( line, outputfile )
	outputfile.write( '</record>\n' )
	
def recordTrailer( line, outputfile ):
	"""Process a trailer record"""
	record_code = line[0:2] # Numeric
	sender = line[2:7] # Alphanumeric
	recipient = line[7:12] #Alphanumeric
	file_seq_number = line[12:17] # Numeric
	total_num_records = line[17:23] # Numeric
	first_call_date = line[23:29] # YYMMDD
	first_call_time = line[29:35] # HHMMSS
	fct_utc_offset = line[35:40] # +/-HHMM
	last_call_date = line[40:46] # YYMMDD
	last_call_time = line[46:52] # HHMMSS
	lct_utc_offset = line[52:57] # +/-HHMM
	total_charge = line[58:69] # Numeric
	# unused = line[69:160]
	
	outputfile.write( '<trailer>\n' )
	out( 'record_code', record_code, outputfile )
	out( 'sender', sender, outputfile )
	out( 'recipient', recipient, outputfile )
	out( 'file_seq_number', file_seq_number, outputfile )
	out( 'total_num_records', total_num_records, outputfile )
	out( 'first_call_date', first_call_date, outputfile )
	out( 'first_call_time', first_call_time, outputfile )
	out( 'fct_utc_offset', fct_utc_offset, outputfile )
	out( 'last_call_date', last_call_date, outputfile )
	out( 'last_call_time', last_call_time, outputfile )
	out( 'lct_utc_offset', lct_utc_offset, outputfile )
	out( 'total_charge', total_charge, outputfile )
	outputfile.write( '</trailer>\n' )
	
		
def main():
	print >>sys.stderr, "Iridium TAPII to XML Script"
	
	# Some constants
	REC_HEADER = '10'
	REC_MOC = '20'
	REC_MTC = '30'
	REC_TRAILER = '90'	

	if len( sys.argv ) > 3 or '-h' in sys.argv or '--help' in sys.argv:
		if sys.stdin.isatty():
			if len( sys.argv ) > 3:
				print >>sys.stderr, "Incorrect Number of Arguments"
			print >>sys.stderr, "Syntax is:  " + sys.argv[0] + " [ <inputfile> [ <outputfile> ]]"
			print >>sys.stderr, "if <outputfile> is undefined it reverts to stdout"
			print >>sys.stderr, "if <inputfile> is undefined it reverts to stdin"
		sys.exit(2)
	elif len( sys.argv ) < 3:
		outputfile = sys.stdout
		if len( sys.argv ) < 2:
			inputfile = sys.stdin
		else:
			inputfile = file( sys.argv[1], "r" )
	else:
		inputfile = file( sys.argv[1], "r" )
		outputfile = file( sys.argv[2], "w" )
		
	records = splitIntoRecords( inputfile.read().strip() )
	
	for record in records:
		recordtype = record[0:2]
		
		if recordtype == REC_HEADER:
			recordHeader( record, outputfile )
		elif recordtype == REC_MOC:
			recordMOC( record, outputfile )
		elif recordtype == REC_MTC:
			recordMTC( record, outputfile )
		elif recordtype == REC_TRAILER:
			recordTrailer( record, outputfile )
		else:
			# Record types: 12, 14 ignored
			# Record types: 40, 60 not used.
			pass
	
	inputfile.close()
	outputfile.close()
	print >>sys.stderr, "Script Complete"
	
if __name__ == "__main__":
	main()
