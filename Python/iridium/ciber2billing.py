# Iridium Billing Interface to CRM
# (C) Copyright 2004 Nothing But Net Limited
# Author: Peter Harrison

# This generates an XML Interface file to the CRM system.

# Who - identify who is sending the data.
# When - When did the transaction occur
# What - What is the descrtiption of the "product". This may have sub elements.
# Quantity - How many of the product are sold?
# Cost - Cost of the product sold per unit
# Price - Price to the Customer per unit
# Total Cost - Total Cost (ie Quantity x Cost)
# Total Price - Total Price (ie Quantity x Price)

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
	chargable_time = line[142:148]
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
	
	
	trans_customer =
	trans_date =	
	trans_product =
	trans_quantity =
	trans_cost = 
	trans_price =
	
	return

def record10( line, outputfile ):
	outputfile.write( '<transaction>\n' )
	commonrecord( line, outputfile )
	outputfile.write( '</transaction>\n' )
	return
	
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
	return

	

print "Iridium CIBER to XML Script"

if len( sys.argv ) < 3:
	print( "Too Few Arguments" )
	print( "Syntax is:  ciber2xml <inputfile> <outputfile>" )
	print( "" )
	sys.exit(2)
	
inputfile = open( sys.argv[1], "r" )
outputfile = open( sys.argv[2], "w" )

while 1:	
	# Load a Line
	line = inputfile.readline()
	if line=="": break
	#print( line )

	# Interprete the Line into a Data Structure
	recordtype = line[0:2]
	print( recordtype )
	
	if recordtype=="10":
		record10( line, outputfile )
		
	if recordtype=="20":
		record20( line, outputfile )
	
# Close Files
inputfile.close()
outputfile.close()
print( "Script Complete" )
