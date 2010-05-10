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
#ifndef LINKEDLIST_H
#define LINKEDLIST_H

#include "node.h"

/**
@author Chris Smith
*/
class LinkedList {
	int size;
public:
	LinkedList();
	int getSize();
	void addToFront( int i );
	void addToBack( int i );
	void removeFromFront();
	void removeFromBack();
	Node *head;
};

#endif
