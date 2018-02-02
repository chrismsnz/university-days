#!/usr/bin/python
#
### Student Info
# Smith, Christopher, 02386569, 159.302
# Assignment 1: 8 Puzzle.
#
### Language
# This assignment was written in Python. An open source, interpreted language
# with a mix of imperative, OO and functional programming. Syntax is simple
# and easy to learn.
#
# Developed on Ubuntu Linux but this will run on the interpreter available
# from http://python.org. Documentation is also on that site but a good
# tutorial is available for free at http://diveintopython.org.
#
### Data Structures
#
# The state of the board is stored in a list. The list stores values for the
# board in the following positions:
#
# -------------
# | 3 | 7 | 6 |
# -------------
# | 5 | 1 | 2 |
# -------------
# | 4 | 0 | 8 |
# -------------
#
# The goal is defined as:
#
# -------------
# | 5 | 3 | 6 |
# -------------
# | 7 | 0 | 2 |
# -------------
# | 4 | 1 | 8 |
# -------------
#
# Where 0 denotes the blank tile or space.
goal_state = [5, 7, 4, 3, 0, 1, 6, 2, 8]
starting_state = [3, 5, 4, 7, 1, 0, 6, 2, 8]

### Code begins.
import sys

def display_board( state ):
	print "-------------"
	print "| %i | %i | %i |" % (state[0], state[3], state[6])
	print "-------------"
	print "| %i | %i | %i |" % (state[1], state[4], state[7])
	print "-------------"
	print "| %i | %i | %i |" % (state[2], state[5], state[8])
	print "-------------"
	
def move_up( state ):
	"""Moves the blank tile up on the board. Returns a new state as a list."""
	# Perform an object copy
	new_state = state[:]
	index = new_state.index( 0 )  #this will return index of blank
	# Sanity check
	if index not in [0, 3, 6]:
		# Swap the values.
		temp = new_state[index - 1]
		new_state[index - 1] = new_state[index]
		new_state[index] = temp
		return new_state
	else:
		# Can't move, return None (Pythons NULL)
		return None

def move_down( state ):
	"""Moves the blank tile down on the board. Returns a new state as a list."""
	# Perform object copy
	new_state = state[:]
	index = new_state.index( 0 )
	# Sanity check
	if index not in [2, 5, 8]:
		# Swap the values.
		temp = new_state[index + 1]
		new_state[index + 1] = new_state[index]
		new_state[index] = temp
		return new_state
	else:
		# Can't move, return None.
		return None

def move_left( state ):
	"""Moves the blank tile left on the board. Returns a new state as a list."""
	new_state = state[:]
	index = new_state.index( 0 )
	# Sanity check
	if index not in [0, 1, 2]:
		# Swap the values.
		temp = new_state[index - 3]
		new_state[index - 3] = new_state[index]
		new_state[index] = temp
		return new_state
	else:
		# Can't move it, return None
		return None

def move_right( state ):
	"""Moves the blank tile right on the board. Returns a new state as a list."""
	# Performs an object copy. Python passes by reference.
	new_state = state[:]
	index = new_state.index( 0 )
	# Sanity check
	if index not in [6, 7, 8]:
		# Swap the values.
		temp = new_state[index + 3]
		new_state[index + 3] = new_state[index]
		new_state[index] = temp
		return new_state
	else:
		# Can't move, return None
		return None

def create_node( state, parent, operator, depth,cost):
	return Node( state, parent, operator, depth,cost)

def expand_node( node,open_nodes,close_nodes):
	"""Returns a list of expanded nodes"""
	expanded_nodes = []
	
	expanded_nodes.append( create_node( move_down( node.state ), node, "d", node.depth + 1,0) )
	expanded_nodes.append( create_node( move_left( node.state ), node, "l", node.depth + 1,0) )
	expanded_nodes.append( create_node( move_up( node.state ), node, "u", node.depth + 1,0) )
	expanded_nodes.append( create_node( move_right( node.state), node, "r", node.depth + 1,0) )
	
	# Filter the list and remove the nodes that are impossible (move function returned None)
	expanded_nodes = [node for node in expanded_nodes if node.state != None] #list comprehension!
	open_state = []
	for o in open_nodes:
		open_state.append(o.state)

	close_state = []
	for c in close_nodes:
		close_state.append(c.state)	
	
	#Remove repeated nodes
	#Remove the nodes that are in open list
	expanded_nodes = [node for node in expanded_nodes if node.state not in open_state]
	#Remove the nodes that are in close list
	expanded_nodes = [node for node in expanded_nodes if node.state not in close_state]

	return expanded_nodes


def solution_path(node):
	moves = []
	states = []
	temp = node
	while True:
		moves.insert(0, temp.operator)
		states.insert(0,temp.state)
		if temp.depth <= 1: break
		temp = temp.parent
	states.insert(0,starting_state)
	return moves,states				

def bfs( start, goal ):
	"""Performs a breadth first search from the start state to the goal"""
	# A list (can act as a queue) for the nodes.
	open_nodes = []
	close_nodes = []
	# Create the queue with the root node in it.
	open_nodes.append( create_node( start, None, None, 0 , 0) )
	while True:
		# We've run out of states, no solution.
		if len( open_nodes ) == 0: return None,None
		# take the node from the front of the queue
		node = open_nodes.pop(0)
		close_nodes.append(node)

		# Append the move we made to moves
		# if this node is the goal, return the moves it took to get here.
		if node.state == goal:
			return solution_path(node)

		# Expand the node and add all the expansions to end of the queue
		open_nodes.extend( expand_node( node, open_nodes,close_nodes) )
		

def dfs( start, goal):
	"""Performs a depth first search from the start state to the goal. Depth param is optional."""
	# A list (can act as a stack too) for the nodes.
	open_nodes = []
	close_nodes = []

	# Create the stack with the root node in it.
	open_nodes.append( create_node( start, None, None, 0 ,0) )
	print("Printing Level")
	while True:
		# We've run out of states, no solution.
		if len( open_nodes ) == 0: return None,None
		# take the node from the front of the queue
		node = open_nodes.pop(0)
		close_nodes.append(node)
		# if this node is the goal, return the moves it took to get here.
		if node.state == goal:
			return solution_path(node)
		

		#Expand the nodes and add all the expansion in the front of open list
		expanded_nodes = expand_node( node, open_nodes,close_nodes )
		expanded_nodes.extend(open_nodes )
		open_nodes = expanded_nodes



def hill_climbing(start,goal):
	""" Perform steepest Hill Climbing Approach. This method involves local minimum search"""
	open_nodes = []
	close_nodes = [] #Required to remove the repition 

	# Create the stack with the root node in it.
	open_nodes.append( create_node( start, None, None, 0,0 ) )
	while True:
		# We've run out of states, no solution.
		if len( open_nodes ) == 0: return None,None
		# take the node from the front of the queue
		node = open_nodes.pop(0)
		close_nodes.append(node)
		# if this node is the goal, return the moves it took to get here.
		if node.state == goal:
			return solution_path(node)

		h1 = h(node.state,goal) #heuristic value of current node	
		
		#Expand the nodes and add all the expansion in the front of open list
		expanded_nodes = expand_node( node, open_nodes,close_nodes )
		successor_nodes = []
		for succ_node in expanded_nodes:
			h2 = h(succ_node.state,goal)
			if(h2<h1):
				successor_nodes.append(succ_node)
		if(len (successor_nodes) != 0):
			successor_nodes.sort( cmp2 )
			successor_nodes.extend(open_nodes )
			open_nodes = successor_nodes



def best_first(start,goal):
	"""" Perform best first search using heuristic function"""
	open_nodes = []
	close_nodes = [] #Required to remove the repition 

	# Create the stack with the root node in it.
	open_nodes.append( create_node( start, None, None, 0 ,0) )
	while True:
		# We've run out of states, no solution.
		if len( open_nodes ) == 0: return None,None
		# take the node from the front of the queue
		node = open_nodes.pop(0)
		close_nodes.append(node)
		# if this node is the goal, return the moves it took to get here.
		if node.state == goal:
			return solution_path(node)

		#Add successor nodes to open list and sort the list to determine global minimum
		open_nodes.extend( expand_node( node, open_nodes,close_nodes) )
		open_nodes.sort(cmp2)
		


def a_star( start, goal ):
	"""Perfoms an A* heuristic search"""
	open_nodes = []
	close_nodes = []
	open_nodes.append( create_node( start, None, None, 0,0 ) )
	while True:
		# We've run out of states - no solution.
		if len( open_nodes ) == 0: return None,None
		
		# Sort the nodes with custom compare function.
		open_nodes.sort( cmp1 )
		
		# take the node from the front of the queue
		best_node = open_nodes.pop(0)
		
		if best_node.state == goal:
			return solution_path(best_node)
		
		#Generating the successor node
		successor_nodes = expand_node(best_node,[],[])
		
		for succ in successor_nodes:
			#g(successor) = g(best_node) + cost of getting successor node from best node
			succ.cost =  best_node.cost + (succ.depth - best_node.depth) 

			#if successor is a goal node then return the moves
			if succ.state == goal:
				return solution_path(succ)
		         
			#if already generated node but not processed i.e. node in open list but has lower cost than old in open update old
			elif succ.state in open_nodes:
				old = [o for o in open_nodes if o.state == succ.state][0]
				succ_f = succ.cost + h(succ,goal)  # f value of successor
				old_f = old.cost + h(old,goal)  #f value of old
				if succ_f < old_f: #update open list successor duplicate with succ value
					old.parent = best_node
					old.cost = succ.cost
				#otherwise ignore successor

		    #if successor is already processed but has lower cost than old node, add in open list
			elif succ.state in close_nodes:
			    old = [c for c in close_nodes if c.state == succ.state][0]
			    succ_f = succ.cost + h(succ,goal) #f value of successor
			    old_f = old.cost + h(old,goal) #f value of old
			    if succ_f < old_f: #update open list successor duplicate with succ value
			    	open_nodes.append(succ)
			    #otherwise ignore successor
			           	
		    #if node neither in open nor in close list add succ in open list
			else:
				open_nodes.append(succ)
		close_nodes.append(best_node)

				
def cmp1( x, y ):
	# Compare function for A*. f(n) = g(n) + h(n). I have used depth (number of moves) for g().
	return (x.depth + h( x.state, goal_state )) - (y.depth + h( x.state, goal_state ))

def cmp2( x, y):
	# Compare function for Hill Climbing and Best First Search i.e h(n).
	return (h( x.state, goal_state ) -  h( x.state, goal_state ))	

def h( state, goal ):
	"""Heuristic for the A* search. Returns an integer based on out of place tiles"""
	score = 0
	for i in range( len( state ) ):
		if state[i] != goal[i]:
			score = score + 1
	return score

# Node data structure
class Node:
	def __init__( self, state, parent, operator, depth,cost):
		# Contains the state of the node
		self.state = state
		# Contains the node that generated this node
		self.parent = parent
		# Contains the operation that generated this node from the parent
		self.operator = operator
		# Contains the depth of this node (parent.depth +1)
		self.depth = depth
		# Contains the cost of this node
		self.cost = cost
		
# Main method
def main():
	### CHANGE THIS FUNCTION TO USE bfs, dfs, best_first, hill_climbing or a_star
	result,states = best_first( starting_state, goal_state )
	if result == None:
		print "No solution found"
	elif result == [None]:
		print "Start node was the goal!"
	else:
		print result
		print len(result), " moves"
		
		#To display board content
		for state in states:
			display_board(state)

# A python-isim. Basically if the file is being run execute the main() function.
if __name__ == "__main__":
	main()
