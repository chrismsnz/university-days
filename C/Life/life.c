/* life.c - simulates Conway's game of life. The rules of the game are:
 *
 *		A) Birth Rule: An organism is born into any empty cell that has exactly three
 *		   living neighbors.
 *		B) Survival Rule: An organism with either two or three neighbors survives from
 *		   one generation to the next.
 *		C) Death Rule: An organism with four or more neighbors dies from overcrowding.
 *		   An organism with fewer than two neightbors dies from loneliness.
 *
 *		This program will simulate these rules and step through each generation of
 *		cells with a one second interval.
 */
 
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>

#define GRID_SIZE 20			
#define GENERATIONS 100			

char grid[GRID_SIZE][GRID_SIZE];
int i;
unsigned int seed;

void init(char [GRID_SIZE][GRID_SIZE]);
void print_grid(char [GRID_SIZE][GRID_SIZE]);
int neighbours(char [GRID_SIZE][GRID_SIZE], int x, int y);
void next_generation(char [GRID_SIZE][GRID_SIZE]);
void copy_grid(char [GRID_SIZE][GRID_SIZE], char [GRID_SIZE][GRID_SIZE]);

int main()
{
	/* Comment me out if you want to make your own patterns!
	 * See init() for more details!
	 */
	printf("Please enter an integer number to spawn random seed : ");
	scanf("%u", &seed);
	printf("Creating random numbers from seed...\n");
	srand(seed);
	
	printf("Creating random grid...\n");
	init(grid);
	
	for (i = 1; i <= GENERATIONS; i++)
	{
		system("clear");
		printf("\nGeneration: %i\n\n",i);	/* Print the generation number on the screen. */
		print_grid(grid);				/* Call print_grid to display the contents of the array. */
		next_generation(grid);				/* Call next_generation to generate the next generation. */
		usleep(85000);
	}
	 	
	return 0;
}

/* init() - Will fill the grid randomly, so we have a first generation.
 * 			You may comment out the random bits and place instructions 
 *			to produce specific patterns here. (e.g. glider or brick).
 */
void init(char A[GRID_SIZE][GRID_SIZE])
{
// 	int count_x, count_y;
// 	for(count_x = 0; count_x < GRID_SIZE; count_x++)
// 	{
// 		for(count_y = 0; count_y < GRID_SIZE; count_y++)
// 		{
// 			if(rand() % 2 == 1)
// 				A[count_x][count_y] = '*';
// 			else
// 				A[count_x][count_y] = ' ';
// 		}
// 	}
	  int count_x, count_y;
	 for(count_x = 0; count_x < GRID_SIZE; count_x++)
	  {
	  	for(count_y = 0; count_y < GRID_SIZE; count_y++)
	  	{
	  		A[count_x][count_y] = ' ';
	  	}
	  } 
	  	 
// 	  A[0][0] = '*';
// 	  A[0][1] = '*';
// 	  A[1][0] = '*';
// 	  A[1][1] = '*';
	  A[11][9] = '*';
	  A[11][10] = '*';
	  A[11][11] = '*';
	  A[12][9] = '*';
	  A[13][10] = '*';
	 
}

void print_grid(char A[GRID_SIZE][GRID_SIZE])
{
	int count_x, count_y;
	
	/* Print column numbers left to right. */
	printf("  ");
	for (count_x = 0; count_x < GRID_SIZE; count_x++)
		printf("%2i ",count_x);
	printf("\n");
	
	for(count_y = 0; count_y < GRID_SIZE; count_y++)
	{
		printf("%2i ",count_y);
		for(count_x = 0; count_x < GRID_SIZE; count_x++)
		{
			printf("%c  ",A[count_x][count_y]);
		}
		printf("\n");
	}
}

/*	neighbours() - Will discover how many neighbours are around 
 *				   grid reference x, y.
 */
int neighbours(char A[GRID_SIZE][GRID_SIZE], int x, int y)
{
	int sum = 0, i; /* Sum and counting vars */
	
	/* Count the neighbours at the left, upper and lower left of x, y */
	if(x - 1 >= 0)
	{
		for(i = y - 1; i <= y + 1; i++)
		{
			if ((i >= 0) && (i < GRID_SIZE))
			{
				if(A[x-1][i] == '*')
					sum++;
			}
		}
	}
	/* Count the neighbours at the right, upper and lower right of x, y */
	if(x + 1 <= GRID_SIZE)
	{
		for(i = y - 1; i <= y + 1; i++)
		{
			if((i >= 0) && (i < GRID_SIZE))
			{
				if(A[x+1][i] == '*')
					sum++;
			}
		}
	}
	/* Count neighbours above cell */
	if(y - 1 >= 0)
	{
		if(A[x][y-1] == '*')
			sum++;
	}
	/* Count neighbours below cell */
	if(y + 1 <= GRID_SIZE)
	{
		if(A[x][y+1] == '*')
			sum++;
	}
	
	return sum;
}

/* next_generation() - creates or destroys cells according to the rules
 * 					   It takes the argument of the board array, creates
 *					   an identical array, does the work in there, then
 *					   copies it using a custom function to the original
 *					   board.
 */
void next_generation(char A[GRID_SIZE][GRID_SIZE])
{
	char work[GRID_SIZE][GRID_SIZE];
	int x, y, n;
	
	for(x = 0; x < GRID_SIZE; x++)
	{
		for(y = 0; y < GRID_SIZE; y++)
		{
			n = neighbours(A, x, y);
			if(A[x][y] == '*')
			{
				if ((n == 2) || (n == 3))
					work[x][y]='*';
				else
					work[x][y]=' ';
			} else {
				if (n == 3)
					work[x][y]='*';
				else
					work[x][y]=' ';
			}
			
		}
	}
	copy_grid(work,A);
}

void copy_grid(char Z[GRID_SIZE][GRID_SIZE], char A[GRID_SIZE][GRID_SIZE])
{
	int x, y;
	
	for(x = 0; x < GRID_SIZE; x++)
	{
		for(y = 0; y < GRID_SIZE; y++)
		{
			A[x][y] = Z[x][y];
		}
	}
}	
