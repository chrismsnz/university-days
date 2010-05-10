/***************************************************************************
 *   Copyright (C) 2004 by Chris Smith                                     *
 *   chris.rs@xtra.co.nz                                                   *
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 *   This program is distributed in the hope that it will be useful,       *
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of        *
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the         *
 *   GNU General Public License for more details.                          *
 *                                                                         *
 *   You should have received a copy of the GNU General Public License     *
 *   along with this program; if not, write to the                         *
 *   Free Software Foundation, Inc.,                                       *
 *   59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.             *
 ***************************************************************************/

#ifdef HAVE_CONFIG_H
#include <config.h>
#endif

#include <iostream>
#include <cassert>
#include "node.h"
#include "linkedlist.h"
#include "stack.h"
#include "queue.h"

void testNode( void );
void testLinkedList( void );
void testStack( void );
void testQueue( void ); 

using namespace std;

int main(int argc, char *argv[])
{
	cout << "Toolkit test suite:" << endl;
	
	testNode();
	testLinkedList();
	testStack();
	testQueue();
	
	cout << endl << "All tests completed successfully!";
	
	cout << endl;
	return EXIT_SUCCESS;
}

void testNode( void )
{
	cout << endl << "Testing Node Class:" << endl;
	
	/************************************************************/
	
	cout << "- Creating Node with empty constructor arguments...";
	Node *n = new Node;
	assert( n->data == 0 );
	assert( n->next == NULL );
	cout << "passed!" << endl;
	
	cout << "- Creating Node with a constructor argument...";
	n = new Node( 69 );
	assert( n->data == 69 );
	assert( n->next == NULL );
	delete n;
	cout << "passed!" << endl;
	
	cout << "- Testing Node pointer links...";
	Node *p = new Node( 69 );
	n = new Node();
	n->next = p;
	assert( n->next->data == 69 );
	cout << "passed!" << endl;	
	
	/************************************************************/
	
	cout << "Node class tests complete" << endl;
}

void testLinkedList( void )
{
	cout << endl << "Testing LinkedList Class:" << endl;
	
	/************************************************************/
	
	cout << "- Creating LinkedList instance...";
	LinkedList l;
	assert( l.getSize() == 0 );
	assert( l.head == NULL );
	cout << "passed!" << endl;
	
	cout << "- Adding Node to front of LinkedList...";
	l.addToFront( 69 );
	assert( l.head->data == 69 );
	cout << "passed!" << endl;
	
	cout << "- Adding Node to back of LinkedList...";
	l.addToBack( 20 );
	assert( l.head->next->data = 20 );
	cout << "passed!" << endl;
	
	cout << "- Testing getSize() function...";
	assert( l.getSize() == 2 );
	cout << "passed!" << endl;
	
	cout << "- Removing element from back of list...";
	l.removeFromBack();
	assert( l.getSize() == 1 );
	assert( l.head->data == 69 );
	cout << "passed!" << endl;
	
	cout << "- Removing element from front of list...";
	l.removeFromFront();
	assert( l.getSize() == 0 );
	assert( l.head == NULL );
	cout << "passed!" << endl;
	
	/************************************************************/
	
	cout << "LinkedList class tests complete" << endl;
}

void testStack( void )
{
	cout << endl << "Testing Stack Class:" << endl;
	
	/************************************************************/
	
	cout << "- Creating Stack instance...";
	Stack s;
	assert( s.isEmpty() );
	cout << "passed!" << endl;
	
	cout << "- Pushing values on to the stack...";
	s.push( 1 );
	s.push( 2 );
	s.push( 3 );
	assert( !s.isEmpty() );
	cout << "passed!" << endl;
	
	cout << "- Reading a value off the top of the stack...";
	assert( s.top() == 3 );
	cout << "passed!" << endl;
	
	cout << "- Popping the values off the stack...";
	s.pop(); assert( s.top() == 2 );
	s.pop(); assert( s.top() == 1 );
	s.pop();
	assert( s.isEmpty() );
	cout << "passed!" << endl;
	
	/************************************************************/
	
	cout << "Stack class tests complete" << endl;
}

void testQueue( void )
{
	cout << endl << "Testing Queue Class:" << endl;
	
	/************************************************************/
	
	cout << "- Creating Queue instance...";
	Queue q;
	assert( q.isEmpty() );
	cout << "passed!" << endl;
	
	cout << "- Joining values from the queue...";
	q.Join( 1 );
	q.Join( 2 );
	q.Join( 3 );
	assert( !q.isEmpty() );
	cout << "passed!" << endl;
	
	cout << "- Reading a value from the front of the queue...";
	assert( q.Front() == 1 );
	cout << "passed!" << endl;
	
	cout << "- Leaving values from the queue...";
	q.Leave(); assert( q.Front() == 2 );
	q.Leave(); assert( q.Front() == 3 );
	q.Leave();
	assert( q.isEmpty() );
	cout << "passed!" << endl;
		
		
	/************************************************************/
	
	cout << "Queue class tests complete" << endl;
}
	