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

#include <iostream>
#include "linkedlist.h"
#include "node.h"

using namespace std;

LinkedList::LinkedList( void )
{
	size = 0;
	head = NULL;
}

int LinkedList::getSize( void )
{
	return size;
}

void LinkedList::addToFront( int i )
{
	Node *newnode = new Node( i );
	if( newnode == NULL )
	{
		cout << "Out of memory error!" << endl;
		abort();
	}	
	newnode->next = head;
	head = newnode;
	
	size++;
}

void LinkedList::addToBack( int i )
{
	if( size < 1 )
	{
		//Wrong function, sunshine.
		addToFront( i );
		return;
	} else {	
		Node *cur;
		cur = head;
		while( cur->next != NULL )
			cur = cur->next;
		Node *newnode = new Node( i );
		if( newnode == NULL )
		{
			cout << "Out of memory error!" << endl;
			abort();
		}
		cur->next = newnode;
		newnode->next = NULL;
		
		size++;
	}
}

void LinkedList::removeFromFront( void )
{
	if( head == NULL )
		return;

	Node *temp = head->next;
	delete head;
	head = temp;
	
	size--;
}

void LinkedList::removeFromBack( void )
{
	if( size < 2 )
	{
		/* Wrong function, buddy */
		removeFromFront();
		return;
	} else {
		Node *cur1, *cur2;
		cur1 = head;
		cur2 = cur1->next;
		while( cur2->next != NULL )
		{
			cur1 = cur2;
			cur2 = cur1->next;
		}
		delete cur1->next;
		cur1->next = NULL;
			
		size--;
	}
}


