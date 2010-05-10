#!/usr/bin/python
import os, sys

def out( tag, content, outputfile ):
	outputfile.write( '<' + tag + '>' + content + '</' + tag + '>\n' )
	return

def commonrecord( line, outputfile ):
	record_type = line[0:2]
	return_code = line[2:3]
	return_reason_code = line[3:5]
	invalid_field_id = line[5:8]
	home_carrier = line[8:13]
	mobile_number_len = line[13:15]
	mobile_number = line[15:25]
	mobile_number_overflow = line[25:30]
	electronic_ser_no = line[30:41]
	call_date = line[41:47]
	serving_carrier = line[47:52]
	total_charges = line[52:63]
	total_state_tax = line[63:74]
	total_local_tax = line[74:85]
	call_direction = line[85:86]
	call_completion = line[86:87]
	call_termination = line[87:88]
	called_number_len = line[88:90]
	called_number = line[90:100]
	called_Number_over = line[100:105]
	temp_local_dir = line[105:115]
	currency = line[115:117]
	original_batch_seq = line[117:120]
	initial_cell_site = line[120:123]
	timezone_indicator = line[123:125]
	daylight_savings = line[125:126]
	message_accounting = line[126:136]
	connect_time = line[136:142]
	chargeable_time = line[142:148]
	elapsed_time = line[148:154]
	rate_period = line[154:156]
	multi_rate_period = line[156:157]
	charge = line[157:168]
	misc_surcharge = line[168:179]
	misc_surcharge_desc = line[179:180]
	printed_call = line[180:195]
	fraud_indicator = line[195:197]
	fraud_subindicator = line[197:198]
	filler = line[198:209]
	reserved = line[209:229]
	special_features = line[229:234]
	called_place = line[234:244]
	called_state = line[244:246]
	serving_place = line[246:256]
	serving_state = line[256:258]

	out( "record_type", record_type, outputfile )
	out( "return_code", return_code, outputfile )
	out( "return_reason_code", return_reason_code, outputfile )
	out( "invalid_field_id", invalid_field_id, outputfile )
	out( "home_carrier", home_carrier, outputfile )
	out( "mobile_number_len", mobile_number_len, outputfile )
	out( "mobile_number", mobile_number, outputfile )
	out( "mobile_number_overflow", mobile_number_overflow, outputfile )
	out( "electronic_ser_no", electronic_ser_no , outputfile )
	out( "call_date", call_date, outputfile )
	out( "serving_carrier", serving_carrier, outputfile )
	out( "total_charges", total_charges, outputfile )
	out( "total_state_tax", total_state_tax, outputfile )
	out( "call_direction", call_direction, outputfile )
	out( "call_completion", call_completion, outputfile )
	out( "call_termination", call_termination, outputfile )
	out( "called_number_len", called_number_len, outputfile )
	out( "called_number", called_number, outputfile )
	out( "called_Number_over", called_Number_over, outputfile )
	out( "temp_local_dir", temp_local_dir, outputfile )
	out( "currency", currency, outputfile )
	out( "original_batch_seq", original_batch_seq, outputfile )
	out( "initial_cell_site", initial_cell_site, outputfile )
	out( "timezone_indicator", timezone_indicator, outputfile )
	out( "daylight_savings", daylight_savings, outputfile )
	out( "message_accounting", message_accounting, outputfile )
	out( "connect_time", connect_time, outputfile )
	out( "chargeable_time", chargeable_time, outputfile )
	out( "elapsed_time", elapsed_time, outputfile )
	out( "rate_period", rate_period, outputfile )
	out( "multi_rate_period", multi_rate_period, outputfile )
	out( "charge", charge, outputfile )
	out( "misc_surcharge", misc_surcharge, outputfile )
	out( "misc_surcharge_desc", misc_surcharge_desc, outputfile )
	out( "printed_call", printed_call, outputfile )
	out( "fraud_indicator", fraud_indicator, outputfile )
	out( "fraud_subindicator", fraud_subindicator, outputfile )
	out( "filler", filler, outputfile )
	out( "reserved", reserved, outputfile )
	out( "special_features", special_features, outputfile )
	out( "called_place", called_place, outputfile )
	out( "called_state", called_state, outputfile )
	out( "serving_place", serving_place, outputfile )
	out( "serving_state", serving_state, outputfile )

def record10( line, outputfile ):
	outputfile.write( '<transaction>\n' )
	commonrecord( line, outputfile )
	outputfile.write( '</transaction>\n' )

def record20( line, outputfile ):
	outputfile.write( '<transaction>\n' )

	commonrecord( line, outputfile )

	toll_connect_time = line[258:264]
	toll_charge_time = line[264:270]
	toll_elapsed_time = line[270:276]
	toll_tarrif = line[276:278]
	toll_rate_period = line[278:280]
	toll_multirate = line[280:281]
	toll_rate_class = line[281:282]
	toll_rating_NPA = line[282:288]
	toll_charge = line[288:299]
	toll_state_tax = line[299:310]
	toll_local_tax = line[310:321]
	toll_carrier = line[321:326]

	out( "toll_connect_time", toll_connect_time, outputfile )
	out( "toll_charge_time", toll_charge_time, outputfile )
	out( "toll_elapsed_time", toll_elapsed_time, outputfile )
	out( "toll_tarrif", toll_tarrif, outputfile )
	out( "toll_rate_period", toll_rate_period, outputfile )
	out( "toll_multirate", toll_multirate, outputfile )
	out( "toll_rate_class", toll_rate_class, outputfile )
	out( "toll_rating_NPA", toll_rating_NPA, outputfile )
	out( "toll_charge", toll_charge, outputfile )
	out( "toll_state_tax", toll_state_tax, outputfile )
	out( "toll_local_tax", toll_local_tax, outputfile )
	out( "toll_carrier", toll_carrier, outputfile )

	outputfile.write( '</transaction>\n' )

def main():
	print >>sys.stderr, "Iridium CIBER to XML Script"

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

	for line in inputfile:
		if len( line ) < 2: continue

		# Interpret the Line into a Data Structure
		recordtype = line[0:2]
		print >>sys.stderr, recordtype

		if recordtype=="10":
			record10( line, outputfile )
		elif recordtype=="20":
			record20( line, outputfile )

	# Close Files
	inputfile.close()
	outputfile.close()
	print >>sys.stderr, "Script Complete"


if __name__ == "__main__":
	main()
