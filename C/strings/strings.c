/* Smith, Chris, 02386569, Assignment 1, 159.331 */
/* This program will act as an interpreter for a strings-based
 * Language. And may I say what an evil little assignment it was.
 * My kingdom for a parser generator :(
 *
 * Dynamic line reading code (fggets) adapted from:
 * http://www.cpax.org.uk/prg/writings/fgetdata.php
 *
 * Known bugs:
 * - Dies badly with multi-line input :(
 * - A lot of memory isn't free()d. glibc errors when I attempted to.
 */
 
/* Includes */
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

/* Defines */
#define INITSIZE   112
#define DELTASIZE (INITSIZE + 16)

/* Enumerations */
enum {OK = 0, NOMEM};

/* Structures */
struct ident_el {
	/* A linked list element for containing an identifier */
	char *name;
	char *value;
	struct ident_el *next;
};
typedef struct ident_el identifier;

/* Function prototypes */
void printInfo(void);
int fggets(char* *ln, FILE *f, char *eol);
void parseStatement(char *s);
char *evaluateExpression(char *expression);
int setIdentifier(char *name, char *value);
char *getIdentifier(char *name);
char *dupstr(char *string);

/* Program-wide variables */
char *statement;
identifier *ident_head, *curr;
int ident_count;

/* Main loop */
int main(int argc, char *argv[]) {
	/* initialise linked list */
	ident_head = NULL;

	/* Print assignment information */
	printInfo();
	
	/* Main loop */
	while(1) {
		printf(">>> ");
		fggets(&statement, stdin, ";\n");
		parseStatement(statement);
		
		printf("\n");
	}
	
	return 0;	
}

void printInfo(void) {
	printf("------------------------------------\n");
	printf("159.331 Assignment 1 Semester 1 2006\n");
	printf("Submitted by: Smith, Chris, 0238569 \n");
	printf("------------------------------------\n\n");
}

int fggets(char* *ln, FILE *f, char *eol) {
	int cursize, rdsize;
	char *buffer, *temp, *rdpoint, *crlocn;
	
	*ln = NULL;
	
	if (NULL == (buffer = malloc(INITSIZE))) { 
		return NOMEM;
	}

	cursize = rdsize = INITSIZE;
	rdpoint = buffer;
	*buffer = '\0';

	if (NULL == fgets(rdpoint, rdsize, f)) {
		free(buffer);
		return EOF;
	}

	/* initial read succeeded, now decide about expansion */
	while (NULL == (crlocn = strstr(rdpoint, eol))) {
		/* line is not completed, expand */
		/* set up cursize, rdpoint and rdsize, expand buffer */
		rdsize = DELTASIZE + 1;   /* allow for a final '\0' */
		cursize += DELTASIZE;
		
		if (NULL == (temp = realloc(buffer, (size_t)cursize))) {
			/* ran out of memory */
			*ln = buffer; /* partial line, next call may fail */
			return NOMEM;
		}
		buffer = temp;

		/* Read into the '\0' up */
		rdpoint = buffer + (cursize - DELTASIZE - 1);

		/* get the next piece of this line */
		if (NULL == fgets(rdpoint, rdsize, f)) {
			/* early EOF encountered */
			crlocn = strchr(buffer, '\0');
			break;
		}
	} /* while line not complete */

	*crlocn = '\0';  /* mark line end, strip \n */
	rdsize = crlocn - buffer;
	
	if (NULL == (temp = realloc(buffer, (size_t)rdsize + 1))) {
		*ln = buffer;  /* without reducing it */
		return OK;
	}

	*ln = temp;
	return OK;
}

void parseStatement(char *statement) {
	char *command, *rest, *identifier, *expression, *temp;

	
	/* Tokenize the command from the statement string */
	command = strtok(statement, " ");
	rest = strtok(NULL, "");
	
	/* One day I'll think of a nicer way to do this */
	if(strcmp(command, "exit") == 0) {
		exit(0);
	} else if(strcmp(command, "print") == 0) {
		/* print <expression> */
		expression = strtok(rest, "");
		printf("%s\n", evaluateExpression(expression));
	} else if(strcmp(command, "printlength") == 0) {
		/* printlength <expression> */
		expression = strtok(rest, "");
		printf("Size is: %d", strlen(evaluateExpression(expression)));
	} else if(strcmp(command, "printwords") == 0) {
		/* printwords <expression> */
		expression = strtok(rest, "");
		char *word, *eval;
		temp = evaluateExpression(expression);
		eval = dupstr(temp);
		printf("Words are:\n");
		word = strtok(eval, " ");
		while(word != NULL) {
			printf("%s\n", word);
			word = strtok(NULL, " ");
		}		
	} else if(strcmp(command, "printwordcount") == 0) {
		/* printwordcount <expression> */
		expression = strtok(rest, "");
		char *word, *eval;
		int c = 0;
		temp = evaluateExpression(expression);
		eval = dupstr(temp);
		word = strtok(eval, " ");
		while(word != NULL) {
			c++;
			word = strtok(NULL, " ");
		}	
		printf("Wordcount is: %d", c);
	} else if(strcmp(command, "set") == 0) {
		/* set <identifier> <expression> */
		identifier = strtok(rest, " ");
		expression = strtok(NULL, "");
		setIdentifier(identifier, evaluateExpression(expression));		
	} else if(strcmp(command, "list") == 0) {
		curr = ident_head;
		printf("Variable list (%d):\n", ident_count);
		while(curr) {
			printf("%s:\t\t%s\n", curr->name, curr->value);
			curr = curr->next;
		}	
	} else if(strcmp(command, "append") == 0) {
		char *ident_val, *exp_val, *new_val;
		/* append <identifier> <expression> */
		identifier = strtok(rest, " ");
		expression = strtok(NULL, "");
		
		ident_val = evaluateExpression(identifier);
		exp_val = evaluateExpression(expression);
		
		new_val = malloc((strlen(ident_val) + strlen(exp_val) + 1) * sizeof(char));
		strcpy(new_val, ident_val);
		strcat(new_val, exp_val);
		
		setIdentifier(identifier, new_val);
		
	} else {
		printf("Invalid Statement");
	}
}

char *evaluateExpression(char *exp) {
	char *value, *working, *rest, *expression;
	
	expression = dupstr(exp);
	/* Since this is a recurring function if we have
	 * an empty string return immediately
	 */
	if(strcmp(expression, "") == 0) {
		return "";
	}
	
	/* First, find out whether the first value is an identifier, 
	 * literal or constant.
	 *
	 * First move the expression into a temporary variable.
	 */
	working = dupstr(expression);
	
	/* look at the first token */
	value = strtok(working, " ");

	/* if it starts with a " it's a literal */
	if(*value == '"') {
		value = strtok(expression, "\"");
	} else /* Not a literal. Either an identifier or constant. */
	if(strcmp(value, "SPACE") == 0) {
		value = " ";
	} else if(strcmp(value, "TAB") == 0) {
		value = "\t";
	} else if(strcmp(value, "NEWLINE") == 0) {
		value = "\n";
	} else {
		/* Must be an identifier */
		value = getIdentifier(value);
	}
	
	/* Set the rest of the string for further processing */
	rest = strtok(NULL, "");
	
	if(rest == NULL) {
		return value;
	} else {
		char *junk, *val;
		junk = strtok(rest, " ");
		rest = strtok(NULL, "");
		rest = evaluateExpression(rest);
		val = malloc((strlen(value) + strlen(rest) + 1) * sizeof(char));
		strcpy(val, value);
		return strcat(val, rest);
	}
}
int setIdentifier(char *name, char *value) {
	
	/* Search to see if there is an identifier by the given name */
	curr = ident_head;
	
	while(curr) {
		if(strcmp(curr->name, name) == 0) {
			curr->value = value;
			return 1;
		}
		curr = curr->next;
	}
	
	// Not found, create a new one!
	curr = (identifier *)malloc(sizeof(identifier));
	curr->name = name;
	curr->value = value;
	curr->next = ident_head;
	ident_head = curr;
	ident_count++;
	
	return 0;	
}

char *getIdentifier(char *name) {
	
	curr = ident_head;
	
	while (curr) {
		if(strcmp(curr->name, name) == 0) {
			return curr->value;
		}
		curr = curr->next;
	}
	return "";
}

char *dupstr(char *string) {
	char *new;
	
	new = (char *)malloc((strlen(string) +1) * sizeof(char));
	strcpy(new, string);
	return new;
}
