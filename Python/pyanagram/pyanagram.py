#!/usr/bin/python

### Settings!
# The letters you have to work with
letters = 'rapty'
# Words have to be longer than this number
upperlimit = 3
# Wordlists to import words from
filenames = ['english-words.10', 
			'english-words.20', 
			'english-words.35',
			'english-words.70',
			'english-words.50',
			'british-words.10', 
			'british-words.20', 
			'american-words.10',
			'american-words.20']
###

import string

def anagraminate( letterlist, stopper ):
	if stopper == 0: yield []
	else:
		range = xrange( len( letterlist ) )
		for i in range:
			y = anagraminate( letterlist[:i] + letterlist[i+1:], stopper-1 )
			for x in y:
				yield [letterlist[i]] + x

wordlist = []

for filename in filenames:
	dict = open(filename)
	lines = dict.readlines()
	for line in lines:
		wordlist.append(string.strip(line))

for i in range(upperlimit, len(letters)+1):
	combs = anagraminate(letters, i)
	for comb in combs:
		word = ''.join(comb)
		if word in wordlist:
			print word

